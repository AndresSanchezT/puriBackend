package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public long countPedidosTotales(){
        return service.countPedidosTotales();
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido) {
        return service.save(pedido);
    }

    @PostMapping("/registrar/{idCliente}/{idVendedor}")
    public Pedido registrarPedido(
            @PathVariable Long idCliente,
            @PathVariable Long idVendedor,
            @RequestBody Pedido pedidoData) {
       return service.registrarPedidoConVisitaYDetalles(idCliente, idVendedor, pedidoData);
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