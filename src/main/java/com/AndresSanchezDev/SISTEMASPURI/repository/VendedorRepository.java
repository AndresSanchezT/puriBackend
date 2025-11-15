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

        -- TOTAL DE PEDIDOS
        (
            SELECT COUNT(*)
            FROM pedido p
            WHERE p.id_vendedor = u.id
        ) AS totalPedidos,

        -- MONTO TOTAL VENDIDO (dos decimales, sin duplicados)
        (
            SELECT ROUND(COALESCE(SUM(p.total), 0), 2)
            FROM pedido p
            WHERE p.id_vendedor = u.id
        ) AS montoTotalVendido,

        -- TOTAL DE VISITAS
        (
            SELECT COUNT(*)
            FROM visita v
            WHERE v.id_vendedor = u.id
        ) AS totalVisitas

    FROM usuario u
    GROUP BY u.id, u.nombre
    ORDER BY montoTotalVendido DESC
    """,
            nativeQuery = true)
    List<VendedorStatsDTO> obtenerEstadisticasVendedores();

}
