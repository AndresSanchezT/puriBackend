package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}
