package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import com.AndresSanchezDev.SISTEMASPURI.entity.Rol;

public class LoginResponse {
    private String token;
    private Long id;
    private String username;
    private Rol role;
    private String nombre;

    public LoginResponse() {
    }

    public LoginResponse(String token,Long id, String username, Rol role, String nombre) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
