package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.math.BigDecimal;

public class DetalleBoletaDTO {
    private String codigoProducto;
    private String nombreProducto;
    private String unidadMedida;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalDetalle;

    // Getters y Setters
    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotalDetalle() { return subtotalDetalle; }
    public void setSubtotalDetalle(BigDecimal subtotalDetalle) { this.subtotalDetalle = subtotalDetalle; }
}