package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleListaPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.PedidoResponseDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query(
            value = "SELECT * FROM pedido p  ORDER BY p.id DESC",
            nativeQuery = true
    )
    List<Pedido> pedidosHoy();

    @Query(value = """
    SELECT 
        pr.nombre AS nombreProducto,
        COALESCE(SUM(dp.cantidad), 0) AS totalProductos,
        pr.stock_actual AS stockActual,
        pr.stock_minimo AS stockMinimo,
        CASE 
            WHEN pr.stock_actual <= 0 THEN 'AGOTADO'
            WHEN pr.stock_actual < pr.stock_minimo THEN 'BAJO STOCK'
            ELSE 'OK'
        END AS estado,
        pr.unidad_medida AS unidadMedida,
        CASE 
            WHEN pr.unidad_medida IN ('KGR') 
            THEN GROUP_CONCAT(dp.cantidad ORDER BY p.id SEPARATOR ',')
            ELSE NULL
        END AS cantidadesPorPedido
    FROM pedido p
    INNER JOIN detalle_pedido dp ON p.id = dp.id_pedido
    INNER JOIN producto pr ON dp.id_producto = pr.id
    WHERE p.estado = 'registrado'
    GROUP BY pr.id, pr.nombre, pr.stock_actual, pr.stock_minimo, pr.unidad_medida
    ORDER BY pr.nombre ASC
""", nativeQuery = true)
    List<ReporteProductoDTO> reporteProductosRegistrados();


    @Query("""
    SELECT 
        p.id as id,
        c.nombreContacto AS nombreCliente,
        c.direccion AS direccion,
        p.estado AS estado,
        c.tieneCredito AS tieneCredito,
        p.total AS total
    FROM Pedido p 
    JOIN p.cliente c
    WHERE p.estado = 'registrado'
""")
    List<DetalleListaPedidoDTO> listarPedidosRegistrados();

    @Query("""
    SELECT 
        p.id as id,
        c.nombreContacto AS nombreCliente,
        c.direccion AS direccion,
        p.estado AS estado,
        c.tieneCredito AS tieneCredito,
        p.total AS total
    FROM Pedido p 
    JOIN p.cliente c
    WHERE p.id = :id
""")
    Optional<DetalleListaPedidoDTO> findDetallePedidoMinimosById(@Param("id") Long id);


    @Query("SELECT DISTINCT p FROM Pedido p " +
            "LEFT JOIN FETCH p.cliente " +
            "LEFT JOIN FETCH p.vendedor " +
            "LEFT JOIN FETCH p.visita " +
            "LEFT JOIN FETCH p.detallePedidos dp " +
            "LEFT JOIN FETCH dp.producto " +
            "WHERE p.id = :id")
    Optional<PedidoResponseDTO.PedidoDTO> obtenerDetallesPedidoCompletoPorId(@Param("id") Long id);

    // Query optimizada que solo actualiza el estado sin cargar toda la entidad
    @Modifying
    @Query("UPDATE Pedido p SET p.estado = :estado WHERE p.id = :id")
    int actualizarEstado(@Param("id") Long id, @Param("estado") String estado);

    // Verificar si el pedido existe y obtener su estado actual
    @Query("SELECT p.estado FROM Pedido p WHERE p.id = :id")
    String obtenerEstadoPedido(@Param("id") Long id);


}
