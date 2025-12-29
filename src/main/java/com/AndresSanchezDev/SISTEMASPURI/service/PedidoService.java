package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    DetalleListaPedidoDTO registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData, boolean forzarGuardar);

    List<Pedido> findAll();

    List<DetalleListaPedidoDTO> findAllDetallesPedido();

    Optional<Pedido> findById(Long id);

    Pedido save(Pedido pedido);

    Pedido update(Long id, Pedido pedidoNuevo);

    void deleteById(Long id);

    List<Pedido> pedidosHoy();

    long countPedidosTotales();

    List<ReporteProductoDTO> reporteProductosRegistrados();

    List<ItemPedidoDTO> validarStock(List<ItemPedidoDTO> items);

    Optional<PedidoResponseDTO.PedidoDTO> obtenerPedidoCompleto(Long idPedido);

    //FUNCIONES PARA CAMBIAR ESTADOS DEL PEDIDO:

    void cambiarEstado(Long pedidoId, CambiarEstadoPedidoDTO dto);

    void validarTransicionEstado(String estadoActual, String nuevoEstado);
     // void registrarAnulacion(Long pedidoId, String motivo);
}
