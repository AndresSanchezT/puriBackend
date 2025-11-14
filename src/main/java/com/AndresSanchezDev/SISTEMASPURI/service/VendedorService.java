package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.VendedorStatsDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Vendedor;

import java.util.List;
import java.util.Optional;

public interface VendedorService {
    List<Vendedor> findAll();
    Optional<Vendedor> findById(Long id);
    Vendedor save(Vendedor vendedor);
    void deleteById(Long id);
    List<VendedorStatsDTO> obtenerEstadisticasVendedores();
}
