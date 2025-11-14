package com.AndresSanchezDev.SISTEMASPURI.controller;



import com.AndresSanchezDev.SISTEMASPURI.entity.Visita;

import com.AndresSanchezDev.SISTEMASPURI.service.VisitaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/visitas")
public class VisitaController {

    private final VisitaService service;

    public VisitaController(VisitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Visita> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Visita> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Visita create(@RequestBody Visita visita) {
        return service.save(visita);
    }

    @PutMapping("/{id}")
    public Visita update(@PathVariable Long id, @RequestBody Visita visita) {
        visita.setId(id);
        return service.save(visita);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}

