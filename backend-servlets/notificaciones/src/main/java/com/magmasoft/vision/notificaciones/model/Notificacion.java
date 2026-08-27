package com.magmasoft.vision.notificaciones.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una notificacion (correo o SMS) enviada a un cliente
 * de la optica VISION, informandole sobre el estado de su pedido.
 *
 * Modulo: Notificaciones
 * Proyecto: MagmaSoft - VISION
 */
public class Notificacion {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String clienteNombre;
    private String clienteContacto;
    private String canal;      // "Email" o "SMS"
    private String tipo;       // "Lente listo", "Recordatorio", "Promocion", "Nuevo modelo"
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    public Notificacion() {
    }

    public Notificacion(int id, String clienteNombre, String clienteContacto, String canal,
                         String tipo, String asunto, String mensaje, LocalDateTime fechaEnvio) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.clienteContacto = clienteContacto;
        this.canal = canal;
        this.tipo = tipo;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteContacto() {
        return clienteContacto;
    }

    public void setClienteContacto(String clienteContacto) {
        this.clienteContacto = clienteContacto;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Fecha de envio formateada para mostrar en la vista JSP.
     */
    public String getFechaEnvioFormateada() {
        return fechaEnvio != null ? fechaEnvio.format(FORMATO_FECHA) : "";
    }
}
