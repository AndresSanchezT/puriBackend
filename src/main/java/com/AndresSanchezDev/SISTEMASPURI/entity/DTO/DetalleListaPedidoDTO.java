package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public interface DetalleListaPedidoDTO {
    Long getId();
    String getNombreCliente();
    String getDireccion();
    String getEstado();
    Boolean getTieneCredito();
}