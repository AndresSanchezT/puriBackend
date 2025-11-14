package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.service.BoletaService;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private final BoletaService service;

    public BoletaController(BoletaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Boleta> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Boleta> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Boleta create(@RequestBody Boleta boleta) {
        return service.save(boleta);
    }

    @PutMapping("/{id}")
    public Boleta update(@PathVariable Long id, @RequestBody Boleta boleta) {
        boleta.setId(id);
        return service.save(boleta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}