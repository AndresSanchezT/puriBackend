package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
