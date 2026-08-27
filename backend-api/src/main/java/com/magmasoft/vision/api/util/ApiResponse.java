package com.magmasoft.vision.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilidades para construir y escribir respuestas JSON en los servlets REST.
 *
 * Todos los endpoints de la API devuelven un objeto JSON con la estructura:
 * {
 *   "ok":      true | false,
 *   "mensaje": "descripcion de resultado",
 *   "datos":   { ... } | [ ... ] | null
 * }
 *
 * MagmaSoft-Vision REST API
 * GA7-220501096-AA5-EV03 | Juan Felipe Angarita Rodriguez
 */
public class ApiResponse {

    // ── Gson configurado para Java Time (LocalDate, LocalDateTime) ──
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDate.class,
            (JsonSerializer<LocalDate>) (src, type, ctx) ->
                ctx.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>) (json, type, ctx) ->
                LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
        .registerTypeAdapter(LocalDateTime.class,
            (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                ctx.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .create();

    public static Gson gson() { return GSON; }

    // ── Enviar respuesta 200 OK con datos ───────────────────────────
    public static void ok(HttpServletResponse res, String mensaje, Object datos)
            throws IOException {
        escribir(res, HttpServletResponse.SC_OK, true, mensaje, datos);
    }

    // ── Enviar respuesta 201 Created ────────────────────────────────
    public static void creado(HttpServletResponse res, String mensaje, Object datos)
            throws IOException {
        escribir(res, HttpServletResponse.SC_CREATED, true, mensaje, datos);
    }

    // ── Enviar respuesta 400 Bad Request ────────────────────────────
    public static void badRequest(HttpServletResponse res, String mensaje)
            throws IOException {
        escribir(res, HttpServletResponse.SC_BAD_REQUEST, false, mensaje, null);
    }

    // ── Enviar respuesta 404 Not Found ──────────────────────────────
    public static void notFound(HttpServletResponse res, String mensaje)
            throws IOException {
        escribir(res, HttpServletResponse.SC_NOT_FOUND, false, mensaje, null);
    }

    // ── Enviar respuesta 500 Internal Server Error ──────────────────
    public static void error(HttpServletResponse res, String mensaje)
            throws IOException {
        escribir(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, mensaje, null);
    }

    // ── Escritor base ────────────────────────────────────────────────
    private static void escribir(HttpServletResponse res, int status,
                                  boolean ok, String mensaje, Object datos)
            throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok",      ok);
        body.put("mensaje", mensaje);
        body.put("datos",   datos);

        res.getWriter().write(GSON.toJson(body));
    }

    // ── Extraer ID de la URL (/api/pedidos/5 → 5) ───────────────────
    public static int extraerIdDeUrl(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) return -1;
        try {
            String[] partes = pathInfo.split("/");
            return Integer.parseInt(partes[partes.length - 1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Leer body JSON del request ───────────────────────────────────
    public static <T> T leerBody(javax.servlet.http.HttpServletRequest req, Class<T> clazz)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        String linea;
        try (java.io.BufferedReader br = req.getReader()) {
            while ((linea = br.readLine()) != null) sb.append(linea);
        }
        return GSON.fromJson(sb.toString(), clazz);
    }

    private ApiResponse() {}
}
