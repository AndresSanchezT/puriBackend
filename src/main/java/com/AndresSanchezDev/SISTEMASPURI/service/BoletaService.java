package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface BoletaService {
    List<Boleta> findAll();
    Optional<Boleta> findById(Long id);
    Boleta save(Boleta boleta);
    void deleteById(Long id);
}
