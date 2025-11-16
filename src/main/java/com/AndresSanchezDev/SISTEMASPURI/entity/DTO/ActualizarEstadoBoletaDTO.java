package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class ActualizarEstadoBoletaDTO {
    private String nuevoEstado;       // "ANULADO" o "PAGADO"
    private String informacion;       // Motivo o comentario


    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}