package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Cliente;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData);
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    Pedido save(Pedido pedido);
    void deleteById(Long id);
}
