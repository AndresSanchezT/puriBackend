package com.AndresSanchezDev.SISTEMASPURI.controller;


import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.VendedorStatsDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Usuario;
import com.AndresSanchezDev.SISTEMASPURI.entity.Vendedor;
import com.AndresSanchezDev.SISTEMASPURI.service.UsuarioService;
import com.AndresSanchezDev.SISTEMASPURI.service.VendedorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorService vendedorService;
    private final UsuarioService usuarioService;

    public VendedorController(VendedorService vendedorService, UsuarioService usuarioService) {
        this.vendedorService = vendedorService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Vendedor> getById(@PathVariable Long id) {
        return vendedorService.findById(id);
    }

    @PostMapping
    public Vendedor create(@RequestBody Vendedor vendedor) {
        return vendedorService.save(vendedor);
    }

    @PutMapping("/{id}")
    public Vendedor update(@PathVariable Long id, @RequestBody Vendedor vendedor) {
        vendedor.setId(id);
        return vendedorService.save(vendedor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vendedorService.deleteById(id);
    }

    @GetMapping("/estadisticas")
    public List<VendedorStatsDTO> getEstadisticas() {
        return vendedorService.obtenerEstadisticasVendedores();
    }

}