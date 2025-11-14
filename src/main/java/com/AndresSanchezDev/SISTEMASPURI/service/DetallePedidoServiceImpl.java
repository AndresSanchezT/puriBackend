package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import com.AndresSanchezDev.SISTEMASPURI.entity.Visita;
import com.AndresSanchezDev.SISTEMASPURI.repository.DetallePedidoRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {
    @Autowired
    private DetallePedidoRepository repository;

    @Override
    public List<DetallePedido> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DetallePedido> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public DetallePedido save(DetallePedido detallePedido) {
        return repository.save(detallePedido);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
