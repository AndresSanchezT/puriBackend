package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoService{
    List<DetallePedido> findAll();
    Optional<DetallePedido> findById(Long id);
    DetallePedido save(DetallePedido detallePedido);
    void deleteById(Long id);
}
