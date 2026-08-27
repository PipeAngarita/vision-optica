package com.magmasoft.vision.api.servlet;

import com.magmasoft.vision.api.dao.NotificacionDao;
import com.magmasoft.vision.api.model.Notificacion;
import com.magmasoft.vision.api.util.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  API REST – Notificaciones                                  ║
 * ║  MagmaSoft-Vision | GA7-220501096-AA5-EV03                  ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  GET    /api/notificaciones        → Lista todas            ║
 * ║  GET    /api/notificaciones/{id}   → Por ID                 ║
 * ║  POST   /api/notificaciones        → Crear / enviar         ║
 * ║  PATCH  /api/notificaciones/{id}   → Cambiar estado         ║
 * ║  DELETE /api/notificaciones/{id}   → Eliminar               ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class NotificacionApiServlet extends HttpServlet {

    private final NotificacionDao dao = new NotificacionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            int id = ApiResponse.extraerIdDeUrl(pathInfo);
            if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }
            try {
                Optional<Notificacion> opt = dao.buscarPorId(id);
                if (opt.isPresent()) ApiResponse.ok(res, "Notificacion encontrada.", opt.get());
                else                 ApiResponse.notFound(res, "Notificacion id=" + id + " no encontrada.");
            } catch (SQLException e) {
                List<Notificacion> demo = dao.listarDemo();
                Optional<Notificacion> op = demo.stream().filter(n -> n.getId() == id).findFirst();
                if (op.isPresent()) ApiResponse.ok(res, "Notificacion encontrada (demo).", op.get());
                else ApiResponse.notFound(res, "No encontrada.");
            }
            return;
        }

        try {
            List<Notificacion> lista = dao.listar();
            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("total", lista.size()); datos.put("notificaciones", lista);
            ApiResponse.ok(res, "Notificaciones obtenidas.", datos);
        } catch (SQLException e) {
            List<Notificacion> demo = dao.listarDemo();
            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("total", demo.size()); datos.put("notificaciones", demo);
            datos.put("fuente", "demo");
            ApiResponse.ok(res, "Notificaciones (modo demo).", datos);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Notificacion nueva;
        try { nueva = ApiResponse.leerBody(req, Notificacion.class); }
        catch (Exception e) { ApiResponse.badRequest(res, "JSON invalido."); return; }

        if (nueva.getDestinatario() == null || nueva.getDestinatario().isBlank()) {
            ApiResponse.badRequest(res, "El campo 'destinatario' es obligatorio."); return;
        }
        if (nueva.getMensaje() == null || nueva.getMensaje().isBlank()) {
            ApiResponse.badRequest(res, "El campo 'mensaje' es obligatorio."); return;
        }
        if (nueva.getCanal() == null) nueva.setCanal("EMAIL");

        try {
            int id = dao.insertar(nueva);
            nueva.setId(id); nueva.setEstado("ENVIADA");
            ApiResponse.creado(res, "Notificacion enviada correctamente.", nueva);
        } catch (SQLException e) {
            nueva.setId(999); nueva.setEstado("ENVIADA");
            ApiResponse.creado(res, "Notificacion registrada (modo demo).", nueva);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }
        try {
            boolean ok = dao.eliminar(id);
            if (ok) ApiResponse.ok(res, "Notificacion eliminada.", null);
            else    ApiResponse.notFound(res, "Notificacion id=" + id + " no encontrada.");
        } catch (SQLException e) {
            ApiResponse.ok(res, "Eliminada (modo demo).", null);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) doPatch(req, res);
        else super.service(req, res);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }
        @SuppressWarnings("unchecked")
        Map<String,String> body = ApiResponse.leerBody(req, Map.class);
        String estado = body != null ? body.get("estado") : null;
        if (estado == null || estado.isBlank()) {
            ApiResponse.badRequest(res, "'estado' es obligatorio: PENDIENTE, ENVIADA, ERROR."); return;
        }
        try {
            dao.actualizarEstado(id, estado.toUpperCase());
            Map<String,Object> r = new LinkedHashMap<>();
            r.put("id", id); r.put("estado", estado.toUpperCase());
            ApiResponse.ok(res, "Estado actualizado.", r);
        } catch (SQLException e) {
            ApiResponse.ok(res, "Estado actualizado (demo).",
                Map.of("id", id, "estado", estado.toUpperCase()));
        }
    }
}
