package com.magmasoft.vision.pedidos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de datos que representa un pedido de lentes del sistema
 * MagmaSoft - VISION.
 *
 * Corresponde a los campos definidos en el prototipo front-end
 * pedidos.html (evidencia GA6-220501096-AA4-EV03).
 *
 * Evidencia: GA7-220501096-AA2-EV01
 */
public class Pedido {

    private int id;
    private String clienteNombre;
    private String clienteTelefono;
    private String clienteCorreo;
    private String tipoLente;      // MONOFOCAL | BIFOCAL | PROGRESIVO
    private String medicoTratante;
    private LocalDate fechaEstimada;
    private String estado;         // EN_PROCESO | LISTO | ENTREGADO
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Pedido() {
    }

    public Pedido(String clienteNombre, String clienteTelefono, String clienteCorreo,
                  String tipoLente, String medicoTratante, LocalDate fechaEstimada, String estado) {
        this.clienteNombre = clienteNombre;
        this.clienteTelefono = clienteTelefono;
        this.clienteCorreo = clienteCorreo;
        this.tipoLente = tipoLente;
        this.medicoTratante = medicoTratante;
        this.fechaEstimada = fechaEstimada;
        this.estado = estado;
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

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public void setClienteCorreo(String clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    public String getTipoLente() {
        return tipoLente;
    }

    public void setTipoLente(String tipoLente) {
        this.tipoLente = tipoLente;
    }

    public String getMedicoTratante() {
        return medicoTratante;
    }

    public void setMedicoTratante(String medicoTratante) {
        this.medicoTratante = medicoTratante;
    }

    public LocalDate getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", clienteNombre='" + clienteNombre + '\'' +
                ", tipoLente='" + tipoLente + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaEstimada=" + fechaEstimada +
                '}';
    }
}
