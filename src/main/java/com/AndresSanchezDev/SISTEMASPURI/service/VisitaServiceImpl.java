package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Repartidor;
import com.AndresSanchezDev.SISTEMASPURI.entity.Visita;
import com.AndresSanchezDev.SISTEMASPURI.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitaServiceImpl implements VisitaService{

    @Autowired
    private VisitaRepository repository;

    @Override
    public List<Visita> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Visita> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Visita save(Visita visita) {
        return repository.save(visita);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
