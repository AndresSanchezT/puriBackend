package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.service.DetallePedidoService;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detallePedidos")
public class DetallePedidoController {

    private final DetallePedidoService service;

    public DetallePedidoController(DetallePedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetallePedido> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DetallePedido> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public DetallePedido create(@RequestBody DetallePedido detallePedido) {
        return service.save(detallePedido);
    }

    @PutMapping("/{id}")
    public DetallePedido update(@PathVariable Long id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);
        return service.save(detallePedido);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}