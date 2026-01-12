package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;

import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;

import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import com.AndresSanchezDev.SISTEMASPURI.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public PedidoController(PedidoService service, ProductoService productoService) {
        this.pedidoService = service;
        this.productoService = productoService;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/all-mobile")
    public List<DetalleListaPedidoDTO> findAllDetallesPedido() {
        return pedidoService.findAllDetallesPedido();
    }

    @GetMapping("/{id}")
    public Optional<PedidoResponseDTO.PedidoDTO> obtenerDetallePedidoCompletoPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoCompleto(id);
    }

    @GetMapping("/{id}/completo")
    public Optional<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @GetMapping("/hoy")
    public List<Pedido> countPedidosHoy() {
        return pedidoService.pedidosHoy();
    }

    @GetMapping("/total")
    public long countPedidosTotales() {
        return pedidoService.countPedidosTotales();
    }

    @GetMapping("/productos-registrados")
    public List<ReporteProductoDTO> reporteProductos() {
        return pedidoService.reporteProductosRegistrados();
    }

    @GetMapping("/faltantes")
    public List<ProductoFaltante> listarFaltantes() {
        return productoService.listarFaltantes();
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido) {
        return pedidoService.save(pedido);
    }

    @PostMapping("/registrar/{idCliente}/{idVendedor}")
    public ResponseEntity<DetalleListaPedidoDTO> registrarPedido(
            @PathVariable Long idCliente,
            @PathVariable Long idVendedor,
            @RequestParam(defaultValue = "false") boolean forzar,
            @RequestBody Pedido pedidoData) {

        DetalleListaPedidoDTO pedido = pedidoService.registrarPedidoConVisitaYDetalles(
                idCliente, idVendedor, pedidoData, forzar);

        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/validar-pedido")
    public ResponseEntity<List<ItemPedidoDTO>> validarPedido(@RequestBody List<ItemPedidoDTO> data) {
        List<ItemPedidoDTO> faltantes = pedidoService.validarStock(data);
        return ResponseEntity.ok(faltantes);
    }

    @PutMapping("/{id}")
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return pedidoService.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
    }

    @DeleteMapping("/resetear-faltantes")
    public void resetearProductosFaltantes(){
        this.productoService.deleteAllProductosFaltantes();
    }

    /**
     * Endpoint para cambiar el estado de un pedido
     * PATCH /api/pedidos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoPedidoDTO dto) {
        try {
            pedidoService.cambiarEstado(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado actualizado correctamente");
            response.put("nuevoEstado", dto.getNuevoEstado());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(409).body(error); // 409 Conflict

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar el estado del pedido");
            return ResponseEntity.internalServerError().body(error);
        }
    }
}