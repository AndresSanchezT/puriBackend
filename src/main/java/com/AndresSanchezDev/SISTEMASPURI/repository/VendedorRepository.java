package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.VendedorStatsDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    @Query(value = """
        SELECT 
          u.id AS idUsuario,
          u.nombre AS nombreCompleto,
          COUNT(DISTINCT p.id) AS totalPedidos,
          COALESCE(SUM(p.total), 0) AS montoTotalVendido,
          COUNT(DISTINCT v.id) AS totalVisitas
        FROM usuario u
        LEFT JOIN pedido p ON u.id = p.id_vendedor
        LEFT JOIN visita v ON u.id = v.id_vendedor
        WHERE u.rol = 'vendedor'
        GROUP BY u.id, u.nombre
        ORDER BY montoTotalVendido DESC
        """, nativeQuery = true)
    List<VendedorStatsDTO> obtenerEstadisticasVendedores();
}
