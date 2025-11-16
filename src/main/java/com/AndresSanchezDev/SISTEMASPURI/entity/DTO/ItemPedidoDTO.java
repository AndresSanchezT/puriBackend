package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class ItemPedidoDTO {
    private Long productoId;
    private int cantidadSolicitada;
    private int stockActual;
    private int cantidadFaltante;

    public ItemPedidoDTO() {
    }

    public ItemPedidoDTO(Long productoId, int cantidadSolicitada) {
        this.productoId = productoId;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public ItemPedidoDTO(Long productoId, int cantidadSolicitada, int stockActual, int cantidadFaltante) {
        this.productoId = productoId;
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

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getCantidadFaltante() {
        return cantidadFaltante;
    }

    public void setCantidadFaltante(int cantidadFaltante) {
        this.cantidadFaltante = cantidadFaltante;
    }
}