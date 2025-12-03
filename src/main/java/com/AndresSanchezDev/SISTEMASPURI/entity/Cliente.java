package com.AndresSanchezDev.SISTEMASPURI.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreContacto;
    private String nombreNegocio;
    private String direccion;
    private String referencia;
    private String estado;
    private String telefono;
    private LocalDate fechaRegistro;
    private LocalDate fechaActualizacion;
    private Double latitud;
    private Double longitud;
    private Boolean tieneCredito;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<Pedido>();

    public Cliente() {
    }

    public Cliente(Long id, String nombreContacto, String nombreNegocio, String direccion, String referencia, String estado, String telefono, LocalDate fechaRegistro, LocalDate fechaActualizacion, Double latitud, Double longitud, Boolean tieneCredito) {
        this.id = id;
        this.nombreContacto = nombreContacto;
        this.nombreNegocio = nombreNegocio;
        this.direccion = direccion;
        this.referencia = referencia;
        this.estado = estado;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tieneCredito = tieneCredito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getTieneCredito() {
        return tieneCredito;
    }

    public void setTieneCredito(Boolean tieneCredito) {
        this.tieneCredito = tieneCredito;
    }
}
