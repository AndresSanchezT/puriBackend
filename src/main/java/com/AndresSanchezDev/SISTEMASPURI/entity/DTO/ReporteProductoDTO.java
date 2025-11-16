package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.math.BigDecimal;

public class ReporteProductoDTO {

    private String nombreProducto;
    private BigDecimal totalProductos;
    private Integer stockActual;
    private Integer stockMinimo;
    private String estado;

    public ReporteProductoDTO() {}

    public ReporteProductoDTO(String nombreProducto, BigDecimal totalProductos, Integer stockActual, Integer stockMinimo, String estado) {
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

    public BigDecimal getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(BigDecimal totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}