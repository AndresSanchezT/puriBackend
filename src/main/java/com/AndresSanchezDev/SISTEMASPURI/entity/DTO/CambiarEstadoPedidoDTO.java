package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class CambiarEstadoPedidoDTO {
    private String nuevoEstado;
    private String motivoAnulacion; // Opcional, para cuando se anule

    public CambiarEstadoPedidoDTO() {}

    public CambiarEstadoPedidoDTO(String nuevoEstado, String motivoAnulacion) {
        this.nuevoEstado = nuevoEstado;
        this.motivoAnulacion = motivoAnulacion;
    }

    // Getters y Setters
    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
}
