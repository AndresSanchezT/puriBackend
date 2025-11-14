package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import com.AndresSanchezDev.SISTEMASPURI.repository.BoletaRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoletaServiceImpl implements BoletaService {
    @Autowired
    private BoletaRepository repository;

    @Override
    public List<Boleta> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Boleta> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Boleta save(Boleta boleta) {
        return repository.save(boleta);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
