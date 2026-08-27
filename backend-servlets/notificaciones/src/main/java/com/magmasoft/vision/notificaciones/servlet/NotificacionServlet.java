package com.magmasoft.vision.notificaciones.servlet;

import com.magmasoft.vision.notificaciones.model.Notificacion;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Servlet del modulo de Notificaciones de VISION.
 *
 * doGet  -> muestra el formulario de envio y el historial (con filtro opcional por canal).
 * doPost -> procesa el envio de una nueva notificacion y la agrega al historial.
 *
 * Nota: el almacenamiento se maneja en memoria para esta evidencia (GA7-AA2-EV02,
 * enfocada en Servlets + JSP). La persistencia con JDBC hacia la base de datos
 * del proyecto se integra en el modulo correspondiente a la evidencia GA7-AA2-EV01.
 *
 * Proyecto: MagmaSoft - VISION
 */
@WebServlet("/notificaciones")
public class NotificacionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final List<Notificacion> historial = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    // Datos de ejemplo iniciales, tomados del prototipo front-end notificaciones.html
    static {
        agregarEjemplo("Luis Torres", "321-555-0202", "Email", "Lente listo", "Su lente ya esta listo para retirar");
        agregarEjemplo("Sofia Mora", "312-555-0505", "SMS", "Lente listo", "Su lente ya esta listo para retirar");
        agregarEjemplo("Ana Garcia", "310-555-0101", "SMS", "Recordatorio", "Recuerde su cita de control visual");
    }

    private static void agregarEjemplo(String nombre, String contacto, String canal, String tipo, String mensaje) {
        historial.add(new Notificacion(contadorId.getAndIncrement(), nombre, contacto, canal, tipo,
                tipo + " - VISION Optica", mensaje, LocalDateTime.now().minusDays(historial.size() + 1)));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String canalFiltro = request.getParameter("canal");
        List<Notificacion> lista;

        if (canalFiltro != null && !canalFiltro.trim().isEmpty()) {
            lista = historial.stream()
                    .filter(n -> n.getCanal().equalsIgnoreCase(canalFiltro))
                    .collect(Collectors.toList());
        } else {
            lista = new ArrayList<>(historial);
        }

        request.setAttribute("historial", lista);
        request.setAttribute("canalFiltro", canalFiltro);
        request.setAttribute("totalEnviadas", historial.size());

        if ("1".equals(request.getParameter("enviado"))) {
            request.setAttribute("mensajeExito", "Notificacion enviada correctamente.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/notificaciones.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String clienteNombre = request.getParameter("clienteNombre");
        String clienteContacto = request.getParameter("clienteContacto");
        String canal = request.getParameter("canal");
        String tipo = request.getParameter("tipo");
        String asunto = request.getParameter("asunto");
        String mensaje = request.getParameter("mensaje");

        if (esVacio(clienteNombre) || esVacio(mensaje)) {
            request.setAttribute("error", "El nombre del cliente y el mensaje son obligatorios.");
            doGet(request, response);
            return;
        }

        Notificacion nueva = new Notificacion(
                contadorId.getAndIncrement(),
                clienteNombre.trim(),
                clienteContacto,
                canal,
                tipo,
                asunto,
                mensaje.trim(),
                LocalDateTime.now()
        );

        historial.add(0, nueva);

        // Patron Post/Redirect/Get: evita el reenvio del formulario al recargar la pagina
        response.sendRedirect(request.getContextPath() + "/notificaciones?enviado=1");
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
