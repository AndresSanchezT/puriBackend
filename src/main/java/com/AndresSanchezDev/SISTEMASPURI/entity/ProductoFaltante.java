package com.AndresSanchezDev.SISTEMASPURI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductoFaltante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;

    private String nombreProducto;

    private Double stockActual;

    private Double cantidadSolicitada;

    private Double cantidadFaltante;

    public ProductoFaltante() {
    }

    public ProductoFaltante(Long id, Long productoId, String nombreProducto, Double stockActual, Double cantidadSolicitada, Double cantidadFaltante) {
        this.id = id;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.stockActual = stockActual;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadFaltante = cantidadFaltante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getStockActual() {
        return stockActual;
    }

    public void setStockActual(Double stockActual) {
        this.stockActual = stockActual;
    }

    public Double getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Double cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Double getCantidadFaltante() {
        return cantidadFaltante;
    }

    public void setCantidadFaltante(Double cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }
}
