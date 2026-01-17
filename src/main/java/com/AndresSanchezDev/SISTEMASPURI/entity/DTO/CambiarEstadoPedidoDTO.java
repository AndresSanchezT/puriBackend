package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public class CambiarEstadoPedidoDTO {
    private String nuevoEstado;
    private String motivoAnulacion;
    private Long idRepartidor;// Opcional, para cuando se anule

    public CambiarEstadoPedidoDTO() {}

    public CambiarEstadoPedidoDTO(String nuevoEstado, String motivoAnulacion,Long idRepartidor) {
        this.nuevoEstado = nuevoEstado;
        this.motivoAnulacion = motivoAnulacion;
        this.idRepartidor = idRepartidor;
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

    public Long getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(Long idRepartidor) {
        this.idRepartidor = idRepartidor;
    }
}
