package com.AndresSanchezDev.SISTEMASPURI.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @OneToOne
    private Visita visita;
    private LocalDate fechaPedido;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private String observaciones;
    @OneToMany(mappedBy = "pedido",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detallePedidos;

    public Pedido() {}

    public Pedido(Long id, Usuario vendedor, Cliente cliente, Visita visita, LocalDate fechaPedido, Double subtotal, Double igv, Double total, String estado, String observaciones, List<DetallePedido> detallePedidos) {
        this.id = id;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.visita = visita;
        this.fechaPedido = fechaPedido;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;
        this.detallePedidos = detallePedidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public void setDetallePedidos(List<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }
}
