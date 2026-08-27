package com.magmasoft.vision.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo Producto – catalogo de lentes y monturas de la optica.
 *
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class Producto {

    private int           id;
    private String        codigoSku;
    private String        nombre;
    private String        descripcion;
    private String        categoria;   // MONTURA | LENTE | ACCESORIO
    private String        marca;
    private BigDecimal    precioVenta;
    private int           stock;
    private boolean       activo;
    private LocalDateTime fechaCreacion;

    public Producto() { this.activo = true; }

    // ── Getters y Setters ────────────────────────────────────────
    public int           getId()             { return id; }
    public void          setId(int id)       { this.id = id; }
    public String        getCodigoSku()      { return codigoSku; }
    public void          setCodigoSku(String v){ this.codigoSku = v; }
    public String        getNombre()         { return nombre; }
    public void          setNombre(String v) { this.nombre = v; }
    public String        getDescripcion()    { return descripcion; }
    public void          setDescripcion(String v){ this.descripcion = v; }
    public String        getCategoria()      { return categoria; }
    public void          setCategoria(String v){ this.categoria = v; }
    public String        getMarca()          { return marca; }
    public void          setMarca(String v)  { this.marca = v; }
    public BigDecimal    getPrecioVenta()    { return precioVenta; }
    public void          setPrecioVenta(BigDecimal v){ this.precioVenta = v; }
    public int           getStock()          { return stock; }
    public void          setStock(int v)     { this.stock = v; }
    public boolean       isActivo()          { return activo; }
    public void          setActivo(boolean v){ this.activo = v; }
    public LocalDateTime getFechaCreacion()  { return fechaCreacion; }
    public void          setFechaCreacion(LocalDateTime v){ this.fechaCreacion = v; }
}
