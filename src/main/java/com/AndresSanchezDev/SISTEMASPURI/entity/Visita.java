package com.AndresSanchezDev.SISTEMASPURI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    @JsonIgnore
    private Usuario vendedor;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    private LocalDate fecha;
    private String estado;
    private String observaciones;

    public Visita() {
    }

    public Visita(Long id, Usuario vendedor, Cliente cliente, LocalDate fecha, String estado, String observaciones) {
        this.id = id;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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
}
