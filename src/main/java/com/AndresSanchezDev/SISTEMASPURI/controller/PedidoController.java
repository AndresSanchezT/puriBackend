package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleListaPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ItemPedidoDTO;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.PedidoResponseDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;

import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import com.AndresSanchezDev.SISTEMASPURI.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;
    private final ProductoService productoService;

    public PedidoController(PedidoService service, ProductoService productoService) {
        this.service = service;
        this.productoService = productoService;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return service.findAll();
    }

    @GetMapping("/all-mobile")
    public List<DetalleListaPedidoDTO> findAllDetallesPedido() {
        return service.findAllDetallesPedido();
    }

    @GetMapping("/{id}")
    public Optional<PedidoResponseDTO.PedidoDTO> obtenerDetallePedidoCompletoPorId(@PathVariable Long id) {
        return service.obtenerPedidoCompleto(id);
    }

    @GetMapping("/hoy")
    public List<Pedido> countPedidosHoy() {
        return service.pedidosHoy();
    }

    @GetMapping("/total")
    public long countPedidosTotales() {
        return service.countPedidosTotales();
    }

    @GetMapping("/productos-registrados")
    public List<ReporteProductoDTO> reporteProductos() {
        return service.reporteProductosRegistrados();
    }

    @GetMapping("/faltantes")
    public List<ProductoFaltante> listarFaltantes() {
        return productoService.listarFaltantes();
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido) {
        return service.save(pedido);
    }

    @PostMapping("/registrar/{idCliente}/{idVendedor}")
    public ResponseEntity<Pedido> registrarPedido(
            @PathVariable Long idCliente,
            @PathVariable Long idVendedor,
            @RequestParam(defaultValue = "false") boolean forzar,
            @RequestBody Pedido pedidoData) {

        Pedido pedido = service.registrarPedidoConVisitaYDetalles(
                idCliente, idVendedor, pedidoData, forzar);

        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/validar-pedido")
    public ResponseEntity<List<ItemPedidoDTO>> validarPedido(@RequestBody List<ItemPedidoDTO> data) {
        List<ItemPedidoDTO> faltantes = service.validarStock(data);
        return ResponseEntity.ok(faltantes);
    }

    @PutMapping("/{id}")
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @DeleteMapping("/resetear-faltantes")
    public void resetearProductosFaltantes(){
        this.productoService.deleteAllProductosFaltantes();
    }
}