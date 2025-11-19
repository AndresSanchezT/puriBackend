package com.AndresSanchezDev.SISTEMASPURI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String codigo;
    private String nombre;
    private Double precio;
    private int stockActual;
    private int stockMinimo;
    private int cantidadFaltante;
    private String unidadMedida;
    private String estado;
    private String tipo;
    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedidos = new ArrayList<>();

    public Producto() {
    }

    public Producto(Long id, String codigo, String nombre, Double precio, int stockActual, int stockMinimo,int cantidadFaltante, String unidadMedida, String estado, String tipo, String descripcion, LocalDate fechaCreacion, LocalDate fechaActualizacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.cantidadFaltante= cantidadFaltante;
        this.unidadMedida = unidadMedida;
        this.estado = estado;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getCantidadFaltante() {
        return cantidadFaltante;
    }
    public void setCantidadFaltante(int cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }
    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
