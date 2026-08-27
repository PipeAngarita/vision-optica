package com.magmasoft.vision.api.dao;

import com.magmasoft.vision.api.model.Pedido;
import com.magmasoft.vision.api.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO REST para el modulo Pedidos.
 * Usa la misma tabla "pedido" del script magmasoft_vision_db.sql
 * definido en backend-jdbc/pedidos.
 *
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class PedidoDao {

    private static final String SQL_LISTAR =
        "SELECT id, cliente_nombre, cliente_telefono, cliente_correo, " +
        "tipo_lente, medico_tratante, fecha_estimada, estado, " +
        "fecha_registro, fecha_actualizacion " +
        "FROM pedido ORDER BY fecha_registro DESC";

    private static final String SQL_LISTAR_POR_ESTADO =
        "SELECT id, cliente_nombre, cliente_telefono, cliente_correo, " +
        "tipo_lente, medico_tratante, fecha_estimada, estado, " +
        "fecha_registro, fecha_actualizacion " +
        "FROM pedido WHERE estado = ? ORDER BY fecha_registro DESC";

    private static final String SQL_BUSCAR_POR_ID =
        "SELECT id, cliente_nombre, cliente_telefono, cliente_correo, " +
        "tipo_lente, medico_tratante, fecha_estimada, estado, " +
        "fecha_registro, fecha_actualizacion " +
        "FROM pedido WHERE id = ?";

    private static final String SQL_INSERTAR =
        "INSERT INTO pedido " +
        "(cliente_nombre, cliente_telefono, cliente_correo, " +
        "tipo_lente, medico_tratante, fecha_estimada, estado) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
        "UPDATE pedido SET " +
        "cliente_nombre=?, cliente_telefono=?, cliente_correo=?, " +
        "tipo_lente=?, medico_tratante=?, fecha_estimada=?, estado=? " +
        "WHERE id=?";

    private static final String SQL_ACTUALIZAR_ESTADO =
        "UPDATE pedido SET estado=? WHERE id=?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM pedido WHERE id=?";

    // ── GET todos (con filtro opcional por estado) ───────────────
    public List<Pedido> listar(String filtroEstado) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = (filtroEstado != null && !filtroEstado.isEmpty())
                     ? SQL_LISTAR_POR_ESTADO : SQL_LISTAR;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            if (filtroEstado != null && !filtroEstado.isEmpty())
                ps.setString(1, filtroEstado.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally {
            DatabaseConnection.cerrar(con);
        }
        return lista;
    }

    // ── GET por ID ───────────────────────────────────────────────
    public Optional<Pedido> buscarPorId(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } finally {
            DatabaseConnection.cerrar(con);
        }
        return Optional.empty();
    }

    // ── POST crear ───────────────────────────────────────────────
    public int insertar(Pedido p) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getClienteNombre());
            ps.setString(2, p.getClienteTelefono());
            ps.setString(3, p.getClienteCorreo());
            ps.setString(4, p.getTipoLente() != null ? p.getTipoLente() : "MONOFOCAL");
            ps.setString(5, p.getMedicoTratante());
            ps.setObject(6, p.getFechaEstimada());  // LocalDate → DATE
            ps.setString(7, p.getEstado() != null ? p.getEstado() : "EN_PROCESO");
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } finally {
            DatabaseConnection.cerrar(con);
        }
        return -1;
    }

    // ── PUT actualizar completo ──────────────────────────────────
    public boolean actualizar(Pedido p) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR);
            ps.setString(1, p.getClienteNombre());
            ps.setString(2, p.getClienteTelefono());
            ps.setString(3, p.getClienteCorreo());
            ps.setString(4, p.getTipoLente());
            ps.setString(5, p.getMedicoTratante());
            ps.setObject(6, p.getFechaEstimada());
            ps.setString(7, p.getEstado());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.cerrar(con);
        }
    }

    // ── PATCH solo estado ────────────────────────────────────────
    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO);
            ps.setString(1, nuevoEstado.toUpperCase());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.cerrar(con);
        }
    }

    // ── DELETE ───────────────────────────────────────────────────
    public boolean eliminar(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.cerrar(con);
        }
    }

    // ── Mapear ResultSet → Pedido ────────────────────────────────
    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setClienteNombre(rs.getString("cliente_nombre"));
        p.setClienteTelefono(rs.getString("cliente_telefono"));
        p.setClienteCorreo(rs.getString("cliente_correo"));
        p.setTipoLente(rs.getString("tipo_lente"));
        p.setMedicoTratante(rs.getString("medico_tratante"));
        Date fe = rs.getDate("fecha_estimada");
        if (fe != null) p.setFechaEstimada(fe.toLocalDate());
        p.setEstado(rs.getString("estado"));
        Timestamp fr = rs.getTimestamp("fecha_registro");
        if (fr != null) p.setFechaRegistro(fr.toLocalDateTime());
        Timestamp fa = rs.getTimestamp("fecha_actualizacion");
        if (fa != null) p.setFechaActualizacion(fa.toLocalDateTime());
        return p;
    }

    // ── Datos demo (cuando no hay BD) ────────────────────────────
    public List<Pedido> listarDemo() {
        List<Pedido> lista = new ArrayList<>();
        String[][] data = {
            {"1","Ana Garcia",  "310-555-0101","ana@email.com","BIFOCAL",   "Dra. Martinez","2026-09-15","EN_PROCESO"},
            {"2","Luis Torres", "321-555-0202","luis@email.com","MONOFOCAL","Dr. Perez",    "2026-09-12","LISTO"},
            {"3","Maria Lopez", "300-555-0303","maria@email.com","PROGRESIVO","Dra. Ramirez","2026-09-18","EN_PROCESO"},
            {"4","Carlos Ruiz", "315-555-0404","carlos@email.com","BIFOCAL", "Dr. Suarez",  "2026-09-10","ENTREGADO"},
            {"5","Sofia Mora",  "312-555-0505","sofia@email.com","MONOFOCAL","Dra. Gomez",  "2026-09-20","LISTO"},
        };
        for (String[] d : data) {
            Pedido p = new Pedido();
            p.setId(Integer.parseInt(d[0]));
            p.setClienteNombre(d[1]); p.setClienteTelefono(d[2]);
            p.setClienteCorreo(d[3]); p.setTipoLente(d[4]);
            p.setMedicoTratante(d[5]);
            p.setFechaEstimada(LocalDate.parse(d[6]));
            p.setEstado(d[7]);
            lista.add(p);
        }
        return lista;
    }
}
