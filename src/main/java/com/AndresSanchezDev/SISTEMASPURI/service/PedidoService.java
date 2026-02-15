package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.entity.TipoFechaPedido;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PedidoService {
    DetalleListaPedidoDTO registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData, boolean forzarGuardar, TipoFechaPedido tipoFecha);

    List<Pedido> findAll();
    Map<String, Object> eliminarPedidosAntiguos();
    List<DetalleListaPedidoDTO> listarTodosPedidosHoy();

    List<DetalleListaPedidoDTO> listarPedidosRegistradosHoy();
    List<DetalleListaPedidoDTO> listarTodosPedidosManana();
    List<DetalleListaPedidoDTO> listarPedidosRegistradosManana();
    List<DetalleListaPedidoDTO> listarTodosPedidosPasadoManana();
    List<DetalleListaPedidoDTO> listarPedidosRegistradosPasadoManana();

    List<Pedido> obtenerTodosLosPedidosDeHoyReporteWeb();
    List<Pedido> obtenerTodosLosPedidosDeMananaReporteWeb();


    Optional<Pedido> findById(Long id);
    boolean verificarPedidoExistente(Long idCliente, String tipoFecha);
    Optional<Pedido> obtenerPorId(Long id);

    Pedido save(Pedido pedido);

    Pedido update(Long id, Pedido pedidoNuevo);

    void deleteById(Long id);

    List<Pedido> pedidosHoy();

    long countPedidosTotales();

    List<ReporteProductoDTO> reporteProductosRegistrados();
    List<ReporteProductoDTO> reporteProductosRegistradosHoy();
    List<ReporteProductoDTO> reporteProductosRegistradosManana();

    List<ItemPedidoDTO> validarStock(List<ItemPedidoDTO> items);

    Optional<PedidoResponseDTO.PedidoDTO> obtenerPedidoCompleto(Long idPedido);

    void actualizarOrdenPedidos(List<Map<String, Object>> ordenMap);
    //FUNCIONES PARA CAMBIAR ESTADOS DEL PEDIDO:

    void cambiarEstado(Long pedidoId, CambiarEstadoPedidoDTO dto);

    void actualizarEstadoyRepartidor(Long id, CambiarEstadoPedidoDTO dto);

    void validarTransicionEstado(String estadoActual, String nuevoEstado);
    // void registrarAnulacion(Long pedidoId, String motivo);

    Double obtenerEfectivoDelDia(Long idRepartidor);
}
