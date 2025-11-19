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

    private int stockActual;

    private int cantidadSolicitada;

    private int cantidadFaltante;

    public ProductoFaltante() {
    }

    public ProductoFaltante(Long id, Long productoId, String nombreProducto, int stockActual, int cantidadSolicitada, int cantidadFaltante) {
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

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getCantidadFaltante() {
        return cantidadFaltante;
    }

    public void setCantidadFaltante(int cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }
}
