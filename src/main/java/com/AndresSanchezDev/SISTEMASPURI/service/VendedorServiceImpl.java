package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.VendedorStatsDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Vendedor;
import com.AndresSanchezDev.SISTEMASPURI.repository.VendedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorServiceImpl implements VendedorService {

    private final VendedorRepository vendedorRepository;

    public VendedorServiceImpl(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public List<Vendedor> findAll() {
        return vendedorRepository.findAll();
    }

    @Override
    public Optional<Vendedor> findById(Long id) {
        return vendedorRepository.findById(id);
    }

    @Override
    public Vendedor save(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    @Override
    public void deleteById(Long id) {
        vendedorRepository.deleteById(id);
    }

    @Override
    public List<VendedorStatsDTO> obtenerEstadisticasVendedores() {
        return vendedorRepository.obtenerEstadisticasVendedores();
    }
}