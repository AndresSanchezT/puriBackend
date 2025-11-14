package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Repartidor;
import com.AndresSanchezDev.SISTEMASPURI.entity.Visita;

import java.util.List;
import java.util.Optional;

public interface VisitaService {
    List<Visita> findAll();
    Optional<Visita> findById(Long id);
    Visita save(Visita visita);
    void deleteById(Long id);
}
