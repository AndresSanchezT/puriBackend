package com.AndresSanchezDev.SISTEMASPURI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
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
    private Vendedor vendedor;
    @ManyToOne
    private Cliente cliente;
    private LocalDate fechaEmision;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private String motivoAnulacion;
    private LocalDate fechaAnulacion;
    private int idAuxiliar;
    private LocalDate fechaRegistro;

    public Boleta() {
    }

    public Boleta(LocalDate fechaEmision, Long id, String codigo, Pedido pedido, Vendedor vendedor, Cliente cliente, Double subtotal, Double igv, Double total, String estado, String motivoAnulacion, LocalDate fechaAnulacion, int idAuxiliar, LocalDate fechaRegistro) {
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

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
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

    public LocalDate getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(LocalDate fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public int getIdAuxiliar() {
        return idAuxiliar;
    }

    public void setIdAuxiliar(int idAuxiliar) {
        this.idAuxiliar = idAuxiliar;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
