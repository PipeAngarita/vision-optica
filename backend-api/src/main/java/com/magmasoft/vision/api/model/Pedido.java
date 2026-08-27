package com.magmasoft.vision.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo Pedido.
 * Campos identicos a los del modulo backend-jdbc/pedidos
 * (evidencia GA7-220501096-AA2-EV01) para compartir la misma BD.
 *
 * Tabla: pedido
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class Pedido {

    private int           id;
    private String        clienteNombre;
    private String        clienteTelefono;
    private String        clienteCorreo;
    /** MONOFOCAL | BIFOCAL | PROGRESIVO */
    private String        tipoLente;
    private String        medicoTratante;
    private LocalDate     fechaEstimada;
    /** EN_PROCESO | LISTO | ENTREGADO */
    private String        estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Pedido() {}

    // ── Getters y Setters ────────────────────────────────────────
    public int           getId()                   { return id; }
    public void          setId(int id)             { this.id = id; }
    public String        getClienteNombre()        { return clienteNombre; }
    public void          setClienteNombre(String v){ this.clienteNombre = v; }
    public String        getClienteTelefono()      { return clienteTelefono; }
    public void          setClienteTelefono(String v){ this.clienteTelefono = v; }
    public String        getClienteCorreo()        { return clienteCorreo; }
    public void          setClienteCorreo(String v){ this.clienteCorreo = v; }
    public String        getTipoLente()            { return tipoLente; }
    public void          setTipoLente(String v)    { this.tipoLente = v; }
    public String        getMedicoTratante()       { return medicoTratante; }
    public void          setMedicoTratante(String v){ this.medicoTratante = v; }
    public LocalDate     getFechaEstimada()        { return fechaEstimada; }
    public void          setFechaEstimada(LocalDate v){ this.fechaEstimada = v; }
    public String        getEstado()               { return estado; }
    public void          setEstado(String v)       { this.estado = v; }
    public LocalDateTime getFechaRegistro()        { return fechaRegistro; }
    public void          setFechaRegistro(LocalDateTime v){ this.fechaRegistro = v; }
    public LocalDateTime getFechaActualizacion()   { return fechaActualizacion; }
    public void          setFechaActualizacion(LocalDateTime v){ this.fechaActualizacion = v; }
}
