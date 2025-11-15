package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.math.BigDecimal;

public interface VendedorStatsDTO {
    Long getIdUsuario();
    String getNombreCompleto();
    Long getTotalPedidos();
    Double getMontoTotalVendido();
    Long getTotalVisitas();
}