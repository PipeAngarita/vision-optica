package com.magmasoft.vision.api.dao;

import com.magmasoft.vision.api.model.Producto;
import com.magmasoft.vision.api.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO REST para Productos.
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class ProductoDao {

    private static final String SQL_LISTAR =
        "SELECT id, codigo_sku, nombre, descripcion, categoria, marca, " +
        "precio_venta, stock, activo FROM producto WHERE activo=1 ORDER BY nombre";

    private static final String SQL_BUSCAR =
        "SELECT id, codigo_sku, nombre, descripcion, categoria, marca, " +
        "precio_venta, stock, activo FROM producto WHERE id=?";

    private static final String SQL_INSERTAR =
        "INSERT INTO producto (codigo_sku, nombre, descripcion, categoria, " +
        "marca, precio_venta, stock) VALUES (?,?,?,?,?,?,?)";

    private static final String SQL_ACTUALIZAR =
        "UPDATE producto SET codigo_sku=?, nombre=?, descripcion=?, " +
        "categoria=?, marca=?, precio_venta=?, stock=? WHERE id=?";

    private static final String SQL_ELIMINAR =
        "UPDATE producto SET activo=0 WHERE id=?";

    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ResultSet rs = con.prepareStatement(SQL_LISTAR).executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally { DatabaseConnection.cerrar(con); }
        return lista;
    }

    public Optional<Producto> buscarPorId(int id) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_BUSCAR);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } finally { DatabaseConnection.cerrar(con); }
        return Optional.empty();
    }

    public int insertar(Producto p) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getCodigoSku()); ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion()); ps.setString(4, p.getCategoria());
            ps.setString(5, p.getMarca()); ps.setBigDecimal(6, p.getPrecioVenta());
            ps.setInt(7, p.getStock());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } finally { DatabaseConnection.cerrar(con); }
        return -1;
    }

    public boolean actualizar(Producto p) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR);
            ps.setString(1, p.getCodigoSku()); ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion()); ps.setString(4, p.getCategoria());
            ps.setString(5, p.getMarca()); ps.setBigDecimal(6, p.getPrecioVenta());
            ps.setInt(7, p.getStock()); ps.setInt(8, p.getId());
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

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id")); p.setCodigoSku(rs.getString("codigo_sku"));
        p.setNombre(rs.getString("nombre")); p.setDescripcion(rs.getString("descripcion"));
        p.setCategoria(rs.getString("categoria")); p.setMarca(rs.getString("marca"));
        p.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        p.setStock(rs.getInt("stock")); p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    public List<Producto> listarDemo() {
        List<Producto> lista = new ArrayList<>();
        Object[][] data = {
            {1,"RB3025","Montura Ray-Ban Aviator","Clasica estilo aviador","MONTURA","Ray-Ban",new BigDecimal("320000"),18},
            {2,"OX8118","Montura Oakley Crosslink","Deportiva alta gama","MONTURA","Oakley",new BigDecimal("410000"),3},
            {3,"TR-GEN8","Lentes Transitions GEN8","Fotocromáticos 8a gen","LENTE","Essilor",new BigDecimal("185000"),42},
            {4,"ACV-30","Lentes Contacto Acuvue","Diarios alta comodidad","LENTE","J&J",new BigDecimal("95000"),4},
            {5,"KIT-01","Kit Limpieza Lentes","Spray + pano + estuche","ACCESORIO","Generico",new BigDecimal("28000"),65},
        };
        for (Object[] d : data) {
            Producto p = new Producto();
            p.setId((int)d[0]); p.setCodigoSku((String)d[1]); p.setNombre((String)d[2]);
            p.setDescripcion((String)d[3]); p.setCategoria((String)d[4]);
            p.setMarca((String)d[5]); p.setPrecioVenta((BigDecimal)d[6]);
            p.setStock((int)d[7]);
            lista.add(p);
        }
        return lista;
    }
}
