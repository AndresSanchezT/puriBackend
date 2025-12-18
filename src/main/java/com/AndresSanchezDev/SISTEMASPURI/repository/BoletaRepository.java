package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    @Query(value = "SELECT * FROM boleta ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Boleta findUltimaBoleta();

    @Query("SELECT b FROM Boleta b " +
            "LEFT JOIN FETCH b.pedido p " +
            "LEFT JOIN FETCH p.cliente c " +
            "LEFT JOIN FETCH p.detallePedidos d " +
            "LEFT JOIN FETCH d.producto prod " +
            "WHERE b.id = :id")
    Optional<Boleta> findByIdConDetalles(@Param("id") Long id);

    Optional<Boleta> findByPedidoId(Long pedidoId);
}
