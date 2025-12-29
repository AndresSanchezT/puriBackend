package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class ItemPedidoDTO {
    private Long productoId;
    private String nombre;
    private Double cantidadSolicitada;
    private Double stockActual;
    private Double cantidadFaltante;

    public ItemPedidoDTO() {
    }

    public ItemPedidoDTO(Long productoId, Double cantidadSolicitada) {
        this.productoId = productoId;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public ItemPedidoDTO(Long productoId,String nombre,  Double stockActual,  Double cantidadSolicitada, Double cantidadFaltante) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.cantidadSolicitada = cantidadSolicitada;
        this.stockActual = stockActual;
        this.cantidadFaltante = cantidadFaltante;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Double getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Double cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Double getStockActual() {
        return stockActual;
    }

    public void setStockActual(Double stockActual) {
        this.stockActual = stockActual;
    }

    public Double getCantidadFaltante() {
        return cantidadFaltante;
    }

    public void setCantidadFaltante(Double cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }
}