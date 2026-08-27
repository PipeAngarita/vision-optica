package com.magmasoft.vision.api.model;

import java.time.LocalDateTime;

/**
 * Modelo Notificacion.
 * Campos compatibles con el modulo backend-servlets/notificaciones
 * (evidencia GA7-220501096-AA2-EV02).
 *
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class Notificacion {

    private int           id;
    /** EMAIL | SMS | PUSH */
    private String        canal;
    private String        destinatario;
    private String        mensaje;
    /** PENDIENTE | ENVIADA | ERROR */
    private String        estado;
    private LocalDateTime fechaEnvio;
    /** Número de orden del pedido al que pertenece, opcional */
    private Integer       idPedido;

    public Notificacion() {
        this.estado    = "PENDIENTE";
        this.fechaEnvio = LocalDateTime.now();
    }

    // ── Getters y Setters ────────────────────────────────────────
    public int           getId()             { return id; }
    public void          setId(int id)       { this.id = id; }
    public String        getCanal()          { return canal; }
    public void          setCanal(String v)  { this.canal = v; }
    public String        getDestinatario()   { return destinatario; }
    public void          setDestinatario(String v){ this.destinatario = v; }
    public String        getMensaje()        { return mensaje; }
    public void          setMensaje(String v){ this.mensaje = v; }
    public String        getEstado()         { return estado; }
    public void          setEstado(String v) { this.estado = v; }
    public LocalDateTime getFechaEnvio()     { return fechaEnvio; }
    public void          setFechaEnvio(LocalDateTime v){ this.fechaEnvio = v; }
    public Integer       getIdPedido()       { return idPedido; }
    public void          setIdPedido(Integer v){ this.idPedido = v; }
}
