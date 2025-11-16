package com.AndresSanchezDev.SISTEMASPURI.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String codigo;
    @OneToOne
    private Pedido pedido;
    @ManyToOne
    private Usuario vendedor;
    @ManyToOne
    private Cliente cliente;
    private LocalDateTime fechaEmision;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private String motivoAnulacion;
    private LocalDateTime fechaAnulacion;
    private int idAuxiliar;
    private LocalDateTime fechaRegistro;

    public Boleta() {
    }

    public Boleta(LocalDateTime fechaEmision, Long id, String codigo, Pedido pedido, Usuario vendedor, Cliente cliente, Double subtotal, Double igv, Double total, String estado, String motivoAnulacion, LocalDateTime fechaAnulacion, int idAuxiliar, LocalDateTime fechaRegistro) {
        this.fechaEmision = fechaEmision;
        this.id = id;
        this.codigo = codigo;
        this.pedido = pedido;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.motivoAnulacion = motivoAnulacion;
        this.fechaAnulacion = fechaAnulacion;
        this.idAuxiliar = idAuxiliar;
        this.fechaRegistro = fechaRegistro;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }

    public LocalDateTime getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(LocalDateTime fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public int getIdAuxiliar() {
        return idAuxiliar;
    }

    public void setIdAuxiliar(int idAuxiliar) {
        this.idAuxiliar = idAuxiliar;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
