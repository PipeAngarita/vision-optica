package com.magmasoft.vision.api.servlet;

import com.magmasoft.vision.api.dao.PedidoDao;
import com.magmasoft.vision.api.model.Pedido;
import com.magmasoft.vision.api.util.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  API REST – Pedidos                                         ║
 * ║  MagmaSoft-Vision | GA7-220501096-AA5-EV03                  ║
 * ║  Juan Felipe Angarita Rodriguez | Mayo 2026                 ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  GET    /api/pedidos           → Lista todos los pedidos    ║
 * ║  GET    /api/pedidos?estado=   → Filtrar por estado         ║
 * ║  GET    /api/pedidos/{id}      → Obtener pedido por ID      ║
 * ║  POST   /api/pedidos           → Crear nuevo pedido         ║
 * ║  PUT    /api/pedidos/{id}      → Actualizar pedido          ║
 * ║  DELETE /api/pedidos/{id}      → Eliminar pedido            ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Todos los endpoints consumen y producen application/json.
 * Respuesta estándar:
 *   { "ok": true|false, "mensaje": "...", "datos": {...}|[...] }
 */
public class PedidoApiServlet extends HttpServlet {

    private final PedidoDao dao = new PedidoDao();

    // ── GET ──────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();   // null → /api/pedidos
        String estado   = req.getParameter("estado");

        // GET /api/pedidos/{id}
        if (pathInfo != null && !pathInfo.equals("/")) {
            int id = ApiResponse.extraerIdDeUrl(pathInfo);
            if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }
            try {
                Optional<Pedido> opt = dao.buscarPorId(id);
                if (opt.isPresent()) {
                    ApiResponse.ok(res, "Pedido encontrado.", opt.get());
                } else {
                    ApiResponse.notFound(res, "Pedido con id=" + id + " no encontrado.");
                }
            } catch (SQLException e) {
                // Fallback a demo
                List<Pedido> demo = dao.listarDemo();
                Optional<Pedido> op = demo.stream().filter(p -> p.getId() == id).findFirst();
                if (op.isPresent()) ApiResponse.ok(res, "Pedido encontrado (demo).", op.get());
                else ApiResponse.notFound(res, "Pedido no encontrado.");
            }
            return;
        }

        // GET /api/pedidos  (con o sin filtro ?estado=)
        try {
            List<Pedido> lista = dao.listar(estado);
            Map<String, Object> datos = new LinkedHashMap<>();
            datos.put("total",   lista.size());
            datos.put("pedidos", lista);
            ApiResponse.ok(res, "Pedidos obtenidos correctamente.", datos);
        } catch (SQLException e) {
            List<Pedido> demo = dao.listarDemo();
            if (estado != null && !estado.isEmpty()) {
                String filtro = estado.toUpperCase();
                demo = demo.stream()
                       .filter(p -> filtro.equals(p.getEstado()))
                       .collect(java.util.stream.Collectors.toList());
            }
            Map<String, Object> datos = new LinkedHashMap<>();
            datos.put("total",   demo.size());
            datos.put("pedidos", demo);
            datos.put("fuente",  "demo");
            ApiResponse.ok(res, "Pedidos (modo demo – sin BD).", datos);
        }
    }

    // ── POST: crear pedido ───────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Pedido nuevo;
        try {
            nuevo = ApiResponse.leerBody(req, Pedido.class);
        } catch (Exception e) {
            ApiResponse.badRequest(res, "Cuerpo JSON invalido: " + e.getMessage());
            return;
        }

        // Validaciones
        if (nuevo.getClienteNombre() == null || nuevo.getClienteNombre().isBlank()) {
            ApiResponse.badRequest(res, "El campo 'clienteNombre' es obligatorio."); return;
        }
        if (nuevo.getClienteTelefono() == null || nuevo.getClienteTelefono().isBlank()) {
            ApiResponse.badRequest(res, "El campo 'clienteTelefono' es obligatorio."); return;
        }

        // Valores por defecto
        if (nuevo.getEstado() == null)    nuevo.setEstado("EN_PROCESO");
        if (nuevo.getTipoLente() == null) nuevo.setTipoLente("MONOFOCAL");

        try {
            int idGenerado = dao.insertar(nuevo);
            nuevo.setId(idGenerado);
            ApiResponse.creado(res, "Pedido creado correctamente.", nuevo);
        } catch (SQLException e) {
            // Demo: simular creacion
            nuevo.setId(999);
            ApiResponse.creado(res,
                "Pedido registrado (modo demo – sin BD disponible).", nuevo);
        }
    }

    // ── PUT: actualizar pedido completo ──────────────────────────
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido en la URL."); return; }

        Pedido actualizado;
        try {
            actualizado = ApiResponse.leerBody(req, Pedido.class);
        } catch (Exception e) {
            ApiResponse.badRequest(res, "Cuerpo JSON invalido: " + e.getMessage()); return;
        }
        actualizado.setId(id);

        if (actualizado.getClienteNombre() == null || actualizado.getClienteNombre().isBlank()) {
            ApiResponse.badRequest(res, "El campo 'clienteNombre' es obligatorio."); return;
        }

        try {
            boolean ok = dao.actualizar(actualizado);
            if (ok) ApiResponse.ok(res, "Pedido actualizado correctamente.", actualizado);
            else    ApiResponse.notFound(res, "Pedido con id=" + id + " no encontrado.");
        } catch (SQLException e) {
            ApiResponse.ok(res, "Pedido actualizado (modo demo).", actualizado);
        }
    }

    // ── DELETE: eliminar pedido ──────────────────────────────────
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido en la URL."); return; }

        try {
            boolean eliminado = dao.eliminar(id);
            if (eliminado) ApiResponse.ok(res, "Pedido eliminado correctamente.", null);
            else           ApiResponse.notFound(res, "Pedido con id=" + id + " no encontrado.");
        } catch (SQLException e) {
            ApiResponse.ok(res, "Pedido eliminado (modo demo).", null);
        }
    }

    // ── Soporte PATCH via _method=PATCH en header ────────────────
    // (para cambio de estado sin actualizar todo el pedido)
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String method = req.getMethod();
        if ("PATCH".equalsIgnoreCase(method)) {
            doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }

        // Leer { "estado": "LISTO" }
        @SuppressWarnings("unchecked")
        Map<String,String> body = ApiResponse.leerBody(req, Map.class);
        String nuevoEstado = body != null ? body.get("estado") : null;

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            ApiResponse.badRequest(res,
                "El campo 'estado' es obligatorio. Valores: EN_PROCESO, LISTO, ENTREGADO.");
            return;
        }
        Set<String> validos = new HashSet<>(Arrays.asList("EN_PROCESO","LISTO","ENTREGADO"));
        if (!validos.contains(nuevoEstado.toUpperCase())) {
            ApiResponse.badRequest(res,
                "Estado invalido. Valores permitidos: EN_PROCESO, LISTO, ENTREGADO.");
            return;
        }

        try {
            boolean ok = dao.actualizarEstado(id, nuevoEstado);
            if (ok) {
                Map<String,Object> datos = new LinkedHashMap<>();
                datos.put("id", id); datos.put("estado", nuevoEstado.toUpperCase());
                ApiResponse.ok(res, "Estado del pedido actualizado.", datos);
            } else {
                ApiResponse.notFound(res, "Pedido con id=" + id + " no encontrado.");
            }
        } catch (SQLException e) {
            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("id", id); datos.put("estado", nuevoEstado.toUpperCase());
            ApiResponse.ok(res, "Estado actualizado (modo demo).", datos);
        }
    }
}
