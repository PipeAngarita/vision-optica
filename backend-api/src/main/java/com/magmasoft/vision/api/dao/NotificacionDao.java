package com.magmasoft.vision.api.dao;

import com.magmasoft.vision.api.model.Notificacion;
import com.magmasoft.vision.api.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DAO REST para Notificaciones.
 * Usa tabla "notificacion" de la BD magmasoft_vision.
 *
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class NotificacionDao {

    private static final String SQL_LISTAR =
        "SELECT id, canal, destinatario, mensaje, estado, fecha_envio, id_pedido " +
        "FROM notificacion ORDER BY fecha_envio DESC";

    private static final String SQL_BUSCAR_POR_ID =
        "SELECT id, canal, destinatario, mensaje, estado, fecha_envio, id_pedido " +
        "FROM notificacion WHERE id=?";

    private static final String SQL_INSERTAR =
        "INSERT INTO notificacion (canal, destinatario, mensaje, estado, id_pedido) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR_ESTADO =
        "UPDATE notificacion SET estado=? WHERE id=?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM notificacion WHERE id=?";

    public List<Notificacion> listar() throws SQLException {
        List<Notificacion> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ResultSet rs = con.prepareStatement(SQL_LISTAR).executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally { DatabaseConnection.cerrar(con); }
        return lista;
    }

    public Optional<Notificacion> buscarPorId(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } finally { DatabaseConnection.cerrar(con); }
        return Optional.empty();
    }

    public int insertar(Notificacion n) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, n.getCanal() != null ? n.getCanal() : "EMAIL");
            ps.setString(2, n.getDestinatario());
            ps.setString(3, n.getMensaje());
            ps.setString(4, "ENVIADA");
            if (n.getIdPedido() != null) ps.setInt(5, n.getIdPedido());
            else ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } finally { DatabaseConnection.cerrar(con); }
        return -1;
    }

    public boolean actualizarEstado(int id, String estado) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO);
            ps.setString(1, estado); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } finally { DatabaseConnection.cerrar(con); }
    }

    public boolean eliminar(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally { DatabaseConnection.cerrar(con); }
    }

    private Notificacion mapear(ResultSet rs) throws SQLException {
        Notificacion n = new Notificacion();
        n.setId(rs.getInt("id"));
        n.setCanal(rs.getString("canal"));
        n.setDestinatario(rs.getString("destinatario"));
        n.setMensaje(rs.getString("mensaje"));
        n.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("fecha_envio");
        if (ts != null) n.setFechaEnvio(ts.toLocalDateTime());
        int idPed = rs.getInt("id_pedido");
        if (!rs.wasNull()) n.setIdPedido(idPed);
        return n;
    }

    /** Demo cuando no hay BD */
    public List<Notificacion> listarDemo() {
        List<Notificacion> lista = new ArrayList<>();
        String[][] data = {
            {"1","EMAIL","ana@email.com","Su pedido #1 está EN_PROCESO","ENVIADA","1"},
            {"2","SMS",  "321-555-0202","Su pedido #2 está LISTO para recoger","ENVIADA","2"},
            {"3","EMAIL","maria@email.com","Recordatorio: pedido #3 en preparacion","PENDIENTE","3"},
        };
        for (String[] d : data) {
            Notificacion n = new Notificacion();
            n.setId(Integer.parseInt(d[0])); n.setCanal(d[1]);
            n.setDestinatario(d[2]); n.setMensaje(d[3]);
            n.setEstado(d[4]); n.setIdPedido(Integer.parseInt(d[5]));
            n.setFechaEnvio(LocalDateTime.now().minusHours(Integer.parseInt(d[0])));
            lista.add(n);
        }
        return lista;
    }
}
