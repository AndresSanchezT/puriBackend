package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Producto;
import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    Page<Producto> findAll(Pageable pageable);
    Page<Producto> findByTipo(String tipo, Pageable pageable);
    Optional<Producto> findById(Long id);
    Producto save(Producto producto);
    void deleteById(Long id);
    long countProductosStockBajo();
    List<ProductoFaltante> listarFaltantes();
    void deleteAllProductosFaltantes();
}