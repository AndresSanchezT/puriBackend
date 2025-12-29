package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.math.BigDecimal;

public class ReporteProductoDTO {

    private String nombreProducto;
    private Double totalProductos;
    private Double stockActual;
    private Double stockMinimo;
    private String estado;

    public ReporteProductoDTO() {}

    public ReporteProductoDTO(String nombreProducto, Double totalProductos, Double stockActual, Double stockMinimo, String estado) {
        this.nombreProducto = nombreProducto;
        this.totalProductos = totalProductos;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Double totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Double getStockActual() {
        return stockActual;
    }

    public void setStockActual(Double stockActual) {
        this.stockActual = stockActual;
    }

    public Double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}