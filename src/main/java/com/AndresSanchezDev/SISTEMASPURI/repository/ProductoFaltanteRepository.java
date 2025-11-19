package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoFaltanteRepository extends JpaRepository<ProductoFaltante, Long> {

}
