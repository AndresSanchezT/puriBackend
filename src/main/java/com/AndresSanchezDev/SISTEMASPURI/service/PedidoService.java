package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ItemPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData, boolean forzarGuardar);
    List<Pedido> findAll();
    Optional<Pedido> findById(Long id);
    Pedido save(Pedido pedido);
    void deleteById(Long id);
    List<Pedido> pedidosHoy();
    long countPedidosTotales();
    List<ReporteProductoDTO> reporteProductosRegistrados();
    List<ItemPedidoDTO> validarStock(List<ItemPedidoDTO> items);
}
