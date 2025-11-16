package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ItemPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.PedidoValidacionDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Pedido> getById(@PathVariable Long id) {
        return service.findById(id);
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
    public ResponseEntity<List<ItemPedidoDTO>> validarPedido(@RequestBody PedidoValidacionDTO data) {
        List<ItemPedidoDTO> faltantes = service.validarStock(data.getItems());
        return ResponseEntity.ok(faltantes);
    }
    @PutMapping("/{id}")
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedido.setId(id);
        return service.save(pedido);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}