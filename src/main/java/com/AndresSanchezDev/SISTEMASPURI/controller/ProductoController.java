package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Producto;
import com.AndresSanchezDev.SISTEMASPURI.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/all")
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    @GetMapping
    public Page<Producto> getAll(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "9") int limit,
            @RequestParam(defaultValue = "") String tipo
    ) {
        Pageable pageable = PageRequest.of(offset, limit);
        if (!tipo.isEmpty()) {
            return productoService.findByTipo(tipo, pageable);
        } else {
            return productoService.findAll(pageable);
        }
    }

    @GetMapping("/stockBajos")
    public long countProductosStockBajo() {
        return productoService.countProductosStockBajo();
    }

    @PostMapping("/comprar")
    public String comprarProducto(@RequestParam Long productoId, @RequestParam int cantidad) {
        try {
            productoService.disminuirStock(productoId, cantidad);
            return "Compra realizada, stock actualizado";
        } catch (RuntimeException e) {
            return "error";
        }
    }

    @GetMapping("/{id}")
    public Optional<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id);
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public Producto update(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        return productoService.save(producto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productoService.deleteById(id);
    }
}
