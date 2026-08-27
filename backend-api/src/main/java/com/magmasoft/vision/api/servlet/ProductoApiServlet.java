package com.magmasoft.vision.api.servlet;

import com.magmasoft.vision.api.dao.ProductoDao;
import com.magmasoft.vision.api.model.Producto;
import com.magmasoft.vision.api.util.ApiResponse;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  API REST – Productos (Catálogo)                            ║
 * ║  MagmaSoft-Vision | GA7-220501096-AA5-EV03                  ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  GET    /api/productos         → Lista catálogo activo      ║
 * ║  GET    /api/productos/{id}    → Obtener por ID             ║
 * ║  POST   /api/productos         → Crear producto             ║
 * ║  PUT    /api/productos/{id}    → Actualizar producto        ║
 * ║  DELETE /api/productos/{id}    → Inactivar producto         ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class ProductoApiServlet extends HttpServlet {

    private final ProductoDao dao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        // GET /api/productos/{id}
        if (pathInfo != null && !pathInfo.equals("/")) {
            int id = ApiResponse.extraerIdDeUrl(pathInfo);
            if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }
            try {
                Optional<Producto> opt = dao.buscarPorId(id);
                if (opt.isPresent()) ApiResponse.ok(res, "Producto encontrado.", opt.get());
                else                 ApiResponse.notFound(res, "Producto id=" + id + " no encontrado.");
            } catch (SQLException e) {
                List<Producto> demo = dao.listarDemo();
                Optional<Producto> op = demo.stream().filter(p -> p.getId() == id).findFirst();
                if (op.isPresent()) ApiResponse.ok(res, "Producto encontrado (demo).", op.get());
                else ApiResponse.notFound(res, "No encontrado.");
            }
            return;
        }

        // GET /api/productos
        try {
            List<Producto> lista = dao.listar();
            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("total",    lista.size());
            datos.put("productos", lista);
            ApiResponse.ok(res, "Catalogo obtenido correctamente.", datos);
        } catch (SQLException e) {
            List<Producto> demo = dao.listarDemo();
            Map<String,Object> datos = new LinkedHashMap<>();
            datos.put("total",    demo.size());
            datos.put("productos", demo);
            datos.put("fuente",   "demo");
            ApiResponse.ok(res, "Catalogo (modo demo – sin BD).", datos);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Producto nuevo;
        try { nuevo = ApiResponse.leerBody(req, Producto.class); }
        catch (Exception e) { ApiResponse.badRequest(res, "JSON invalido."); return; }

        if (nuevo.getNombre() == null || nuevo.getNombre().isBlank()) {
            ApiResponse.badRequest(res, "'nombre' es obligatorio."); return;
        }
        if (nuevo.getCodigoSku() == null || nuevo.getCodigoSku().isBlank()) {
            ApiResponse.badRequest(res, "'codigoSku' es obligatorio."); return;
        }
        if (nuevo.getPrecioVenta() == null) {
            ApiResponse.badRequest(res, "'precioVenta' es obligatorio."); return;
        }

        try {
            int id = dao.insertar(nuevo);
            nuevo.setId(id);
            ApiResponse.creado(res, "Producto creado correctamente.", nuevo);
        } catch (SQLException e) {
            nuevo.setId(999);
            ApiResponse.creado(res, "Producto registrado (modo demo).", nuevo);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido en la URL."); return; }

        Producto actualizado;
        try { actualizado = ApiResponse.leerBody(req, Producto.class); }
        catch (Exception e) { ApiResponse.badRequest(res, "JSON invalido."); return; }
        actualizado.setId(id);

        if (actualizado.getNombre() == null || actualizado.getNombre().isBlank()) {
            ApiResponse.badRequest(res, "'nombre' es obligatorio."); return;
        }

        try {
            boolean ok = dao.actualizar(actualizado);
            if (ok) ApiResponse.ok(res, "Producto actualizado.", actualizado);
            else    ApiResponse.notFound(res, "Producto id=" + id + " no encontrado.");
        } catch (SQLException e) {
            ApiResponse.ok(res, "Producto actualizado (demo).", actualizado);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = ApiResponse.extraerIdDeUrl(req.getPathInfo());
        if (id < 1) { ApiResponse.badRequest(res, "ID invalido."); return; }

        try {
            boolean ok = dao.eliminar(id);
            if (ok) ApiResponse.ok(res, "Producto eliminado del catalogo.", null);
            else    ApiResponse.notFound(res, "Producto id=" + id + " no encontrado.");
        } catch (SQLException e) {
            ApiResponse.ok(res, "Producto eliminado (demo).", null);
        }
    }
}
