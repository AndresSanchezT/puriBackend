package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
