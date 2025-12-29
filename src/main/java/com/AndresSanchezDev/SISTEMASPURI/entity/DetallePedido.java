package com.AndresSanchezDev.SISTEMASPURI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    @JsonIgnore
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    private Double cantidad;
    private double precioUnitario;
    private Double subtotal;

    public DetallePedido() {
    }

    public DetallePedido(Long id, Pedido pedido, Producto producto, Double cantidad, double precioUnitario, Double subtotal) {
        this.id = id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
