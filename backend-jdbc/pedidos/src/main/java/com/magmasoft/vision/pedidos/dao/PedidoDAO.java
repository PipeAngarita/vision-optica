package com.magmasoft.vision.pedidos.dao;

import com.magmasoft.vision.pedidos.conexion.ConexionBD;
import com.magmasoft.vision.pedidos.modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object del modulo Pedidos. Encapsula las operaciones de
 * insercion, consulta, actualizacion y eliminacion (CRUD) contra la
 * tabla "pedido", usando JDBC con PreparedStatement.
 *
 * Evidencia: GA7-220501096-AA2-EV01
 */
public class PedidoDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO pedido (cliente_nombre, cliente_telefono, cliente_correo, " +
                    "tipo_lente, medico_tratante, fecha_estimada, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_CONSULTAR_TODOS =
            "SELECT * FROM pedido ORDER BY fecha_registro DESC";

    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT * FROM pedido WHERE id = ?";

    private static final String SQL_CONSULTAR_POR_ESTADO =
            "SELECT * FROM pedido WHERE estado = ? ORDER BY fecha_registro DESC";

    private static final String SQL_ACTUALIZAR =
            "UPDATE pedido SET cliente_nombre = ?, cliente_telefono = ?, cliente_correo = ?, " +
                    "tipo_lente = ?, medico_tratante = ?, fecha_estimada = ?, estado = ? " +
                    "WHERE id = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM pedido WHERE id = ?";

    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * @param pedido datos del pedido a registrar (sin id)
     * @return el id autogenerado del pedido insertado
     */
    public int insertar(Pedido pedido) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, pedido.getClienteNombre());
            sentencia.setString(2, pedido.getClienteTelefono());
            sentencia.setString(3, pedido.getClienteCorreo());
            sentencia.setString(4, pedido.getTipoLente());
            sentencia.setString(5, pedido.getMedicoTratante());
            sentencia.setDate(6, pedido.getFechaEstimada() != null ? Date.valueOf(pedido.getFechaEstimada()) : null);
            sentencia.setString(7, pedido.getEstado());

            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Consulta todos los pedidos registrados.
     */
    public List<Pedido> consultarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_TODOS);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                pedidos.add(mapearPedido(resultado));
            }
        }
        return pedidos;
    }

    /**
     * Consulta un pedido puntual por su identificador.
     */
    public Optional<Pedido> consultarPorId(int id) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearPedido(resultado));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Consulta los pedidos filtrados por estado (EN_PROCESO, LISTO, ENTREGADO).
     */
    public List<Pedido> consultarPorEstado(String estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ESTADO)) {

            sentencia.setString(1, estado);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(mapearPedido(resultado));
                }
            }
        }
        return pedidos;
    }

    /**
     * Actualiza los datos de un pedido existente.
     *
     * @param pedido datos actualizados; debe incluir un id valido
     * @return true si se actualizo un registro, false si no existia
     */
    public boolean actualizar(Pedido pedido) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {

            sentencia.setString(1, pedido.getClienteNombre());
            sentencia.setString(2, pedido.getClienteTelefono());
            sentencia.setString(3, pedido.getClienteCorreo());
            sentencia.setString(4, pedido.getTipoLente());
            sentencia.setString(5, pedido.getMedicoTratante());
            sentencia.setDate(6, pedido.getFechaEstimada() != null ? Date.valueOf(pedido.getFechaEstimada()) : null);
            sentencia.setString(7, pedido.getEstado());
            sentencia.setInt(8, pedido.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un pedido por su identificador.
     *
     * @return true si se elimino un registro, false si no existia
     */
    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Convierte la fila actual de un ResultSet en un objeto Pedido.
     */
    private Pedido mapearPedido(ResultSet resultado) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(resultado.getInt("id"));
        pedido.setClienteNombre(resultado.getString("cliente_nombre"));
        pedido.setClienteTelefono(resultado.getString("cliente_telefono"));
        pedido.setClienteCorreo(resultado.getString("cliente_correo"));
        pedido.setTipoLente(resultado.getString("tipo_lente"));
        pedido.setMedicoTratante(resultado.getString("medico_tratante"));

        Date fechaEstimada = resultado.getDate("fecha_estimada");
        if (fechaEstimada != null) {
            pedido.setFechaEstimada(fechaEstimada.toLocalDate());
        }

        pedido.setEstado(resultado.getString("estado"));

        Timestamp fechaRegistro = resultado.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            pedido.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        Timestamp fechaActualizacion = resultado.getTimestamp("fecha_actualizacion");
        if (fechaActualizacion != null) {
            pedido.setFechaActualizacion(fechaActualizacion.toLocalDateTime());
        }

        return pedido;
    }
}
