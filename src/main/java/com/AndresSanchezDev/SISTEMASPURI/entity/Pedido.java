package com.AndresSanchezDev.SISTEMASPURI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long idRepartidor;
    @OneToOne
    private Visita visita;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private String observaciones;
    @OneToMany(mappedBy = "pedido",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DetallePedido> detallePedidos = new ArrayList<>();
    private Double yape;
    private Double plin;
    private Double credito;
    private Double efectivo;

    public Pedido() {}

    public Pedido(Long id, Usuario vendedor, Cliente cliente, Long idRepartidor, Visita visita, LocalDateTime fechaPedido, LocalDateTime fechaEntrega, Double subtotal, Double igv, Double total, String estado, String observaciones, List<DetallePedido> detallePedidos, Double yape, Double plin, Double credito, Double efectivo) {
        this.id = id;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.idRepartidor = idRepartidor;
        this.visita = visita;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;
        this.detallePedidos = detallePedidos;
        this.yape = yape;
        this.plin = plin;
        this.credito = credito;
        this.efectivo = efectivo;
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

    public Long getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(Long idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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

    public Double getYape() {
        return yape;
    }

    public void setYape(Double yape) {
        this.yape = yape;
    }

    public Double getPlin() {
        return plin;
    }

    public void setPlin(Double plin) {
        this.plin = plin;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

    public Double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Double efectivo) {
        this.efectivo = efectivo;
    }
}
