package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Producto;
import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import com.AndresSanchezDev.SISTEMASPURI.repository.ProductoFaltanteRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoFaltanteRepository productoFaltanteRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository,ProductoFaltanteRepository productoFaltanteRepository) {
        this.productoRepository = productoRepository;
        this.productoFaltanteRepository = productoFaltanteRepository;
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Page<Producto> findByTipo(String tipo, Pageable pageable) {
        return productoRepository.findByTipo(tipo, pageable);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public long countProductosStockBajo() {
        return productoRepository.countProductosStockBajo();
    }

    @Override
    public List<ProductoFaltante> listarFaltantes() {
        return productoFaltanteRepository.findAll();
    }

    @Override
    public void deleteAllProductosFaltantes() {
        productoFaltanteRepository.deleteAll();
    }

}