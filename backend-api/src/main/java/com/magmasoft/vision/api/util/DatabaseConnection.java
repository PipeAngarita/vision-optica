package com.magmasoft.vision.api.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad de conexion a MySQL.
 * Misma base de datos usada por el modulo backend-jdbc/pedidos.
 *
 * MagmaSoft-Vision REST API
 * GA7-220501096-AA5-EV03 | Juan Felipe Angarita Rodriguez
 */
public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/magmasoft_vision" +
        "?useSSL=false&serverTimezone=America/Bogota&characterEncoding=UTF-8";
    private static final String USER     = "root";
    private static final String PASSWORD = "magma2026";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver MySQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void cerrar(Connection con) {
        if (con != null) {
            try { con.close(); } catch (SQLException ignored) {}
        }
    }

    private DatabaseConnection() {}
}
