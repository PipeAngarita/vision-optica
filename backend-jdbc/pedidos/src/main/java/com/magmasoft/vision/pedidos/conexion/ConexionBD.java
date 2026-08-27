package com.magmasoft.vision.pedidos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad de conexion a la base de datos del proyecto MagmaSoft - VISION,
 * usando JDBC contra un motor MySQL / MariaDB.
 *
 * Evidencia: GA7-220501096-AA2-EV01
 */
public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/magmasoft_vision"
                    + "?useSSL=false&serverTimezone=America/Bogota&characterEncoding=UTF-8";
    private static final String USUARIO = "magmasoft";
    private static final String CLAVE = "magma2026";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontro el driver JDBC de MySQL: " + e.getMessage());
        }
    }

    private ConexionBD() {
        // Clase utilitaria, no debe instanciarse
    }

    /**
     * Abre y retorna una nueva conexion a la base de datos.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    /**
     * Cierra una conexion de forma segura, ignorando errores si ya estaba cerrada.
     */
    public static void cerrar(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ignorada) {
                // Conexion ya cerrada o invalida; no se requiere accion adicional
            }
        }
    }
}
