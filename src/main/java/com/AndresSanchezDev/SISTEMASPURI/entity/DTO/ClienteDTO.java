package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class ClienteDTO {
    private String nombreContacto;
    private String direccion;
    private String telefono;

    public ClienteDTO() {}

    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
