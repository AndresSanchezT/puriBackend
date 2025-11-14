package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

public record VendedorStatsDTO(
        Long idUsuario,
        String nombreCompleto,
        int totalPedidos,
        Double montoTotalVendido,
        int totalVisitas
) {}