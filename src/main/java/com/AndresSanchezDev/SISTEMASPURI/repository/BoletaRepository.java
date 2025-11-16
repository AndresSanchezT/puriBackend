package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    @Query(value = "SELECT * FROM boleta ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Boleta findUltimaBoleta();
}
