package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.util.List;

public class BoletaDTO {
    private Long id;
    private String codigo;
    private String fechaEmision; // Ya formateada como String
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private ClienteDTO cliente;
    private String observaciones; // Del pedido
    private List<DetalleBoletaDTO> detalles;

    // Constructores, getters y setters
    public BoletaDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIgv() { return igv; }
    public void setIgv(Double igv) { this.igv = igv; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetalleBoletaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleBoletaDTO> detalles) { this.detalles = detalles; }
}
