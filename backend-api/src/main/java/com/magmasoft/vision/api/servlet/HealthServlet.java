package com.magmasoft.vision.api.servlet;

import com.magmasoft.vision.api.util.ApiResponse;
import com.magmasoft.vision.api.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check endpoint.
 * GET /api/health → estado de la API y la conexión a BD.
 *
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("api",        "MagmaSoft-Vision REST API");
        info.put("version",    "1.0.0");
        info.put("evidencia",  "GA7-220501096-AA5-EV03");
        info.put("autor",      "Juan Felipe Angarita Rodriguez");
        info.put("timestamp",  LocalDateTime.now().toString());
        info.put("repositorio","https://github.com/PipeAngarita/vision-optica");

        // Verificar conexion a BD
        String bdEstado;
        try (Connection con = DatabaseConnection.getConnection()) {
            bdEstado = con.isValid(2) ? "CONECTADA" : "ERROR";
        } catch (Exception e) {
            bdEstado = "NO_DISPONIBLE (modo demo activo)";
        }
        info.put("baseDatos", bdEstado);

        // Endpoints disponibles
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("GET    /api/health",                   "Estado de la API");
        endpoints.put("GET    /api/pedidos",                  "Listar pedidos");
        endpoints.put("GET    /api/pedidos?estado=EN_PROCESO", "Filtrar por estado");
        endpoints.put("GET    /api/pedidos/{id}",             "Pedido por ID");
        endpoints.put("POST   /api/pedidos",                  "Crear pedido");
        endpoints.put("PUT    /api/pedidos/{id}",             "Actualizar pedido");
        endpoints.put("PATCH  /api/pedidos/{id}",             "Cambiar estado");
        endpoints.put("DELETE /api/pedidos/{id}",             "Eliminar pedido");
        endpoints.put("GET    /api/notificaciones",           "Listar notificaciones");
        endpoints.put("GET    /api/notificaciones/{id}",      "Notificacion por ID");
        endpoints.put("POST   /api/notificaciones",           "Crear notificacion");
        endpoints.put("PATCH  /api/notificaciones/{id}",      "Cambiar estado");
        endpoints.put("DELETE /api/notificaciones/{id}",      "Eliminar notificacion");
        endpoints.put("GET    /api/productos",                "Listar catalogo");
        endpoints.put("GET    /api/productos/{id}",           "Producto por ID");
        endpoints.put("POST   /api/productos",                "Crear producto");
        endpoints.put("PUT    /api/productos/{id}",           "Actualizar producto");
        endpoints.put("DELETE /api/productos/{id}",           "Inactivar producto");
        info.put("endpoints", endpoints);

        ApiResponse.ok(res, "API funcionando correctamente.", info);
    }
}
