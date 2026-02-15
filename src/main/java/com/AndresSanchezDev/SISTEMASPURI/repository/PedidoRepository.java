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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
        SELECT COALESCE(SUM(p.efectivo), 0.0)
        FROM Pedido p
        WHERE p.estado = 'entregado'
        AND p.idRepartidor = :idRepartidor
        AND DATE(p.fechaEntrega) = DATE(:fecha)
    """)
    Double sumarEfectivoDelDia(
            @Param("idRepartidor") Long idRepartidor,
            @Param("fecha") LocalDateTime fecha
    );

    @Query(
            value = "SELECT * FROM pedido p ORDER BY p.id DESC",
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
    SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
    FROM Pedido p
    WHERE p.cliente.id = :idCliente
    AND p.estado = 'registrado'
    AND p.fechaPedido >= :inicioDia
    AND p.fechaPedido < :finDia
""")
    boolean existePedidoRegistrado(
            @Param("idCliente") Long idCliente,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    // ✅  Query para productos con filtro de fecha
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
    AND p.fecha_pedido >= :inicioDia
    AND p.fecha_pedido < :finDia
    GROUP BY pr.id, pr.nombre, pr.stock_actual, pr.stock_minimo, pr.unidad_medida
    ORDER BY pr.nombre ASC
""", nativeQuery = true)
    List<ReporteProductoDTO> reporteProductosRegistradosConFechas(
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    // 🔥 NUEVO: Query que acepta rango de fechas (00:00 - 23:59 hora Perú)
    @Query("""
    SELECT 
        p.id as id,
        c.nombreContacto AS nombreCliente,
        c.direccion AS direccion,
        p.estado AS estado,
        c.tieneCredito AS tieneCredito,
        p.total AS total,
         p.orden
    FROM Pedido p 
    JOIN p.cliente c
    WHERE p.estado = 'registrado'
    AND p.fechaPedido >= :inicioDia
    AND p.fechaPedido < :finDia
     ORDER BY p.orden ASC
""")
    List<DetalleListaPedidoDTO> listarPedidosRegistradosHoyConFechas(
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    @Query("""
        SELECT p 
        FROM Pedido p 
        WHERE p.fechaPedido >= :inicioDia
        AND p.fechaPedido < :finDia
        ORDER BY p.fechaPedido DESC
    """)
    List<Pedido> listarPedidosPorRangoFecha(
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

    // 🔥 NUEVO: Query para admin que acepta rango de fechas
    @Query("""
    SELECT 
        p.id as id,
        c.nombreContacto AS nombreCliente,
        c.direccion AS direccion,
        p.estado AS estado,
        c.tieneCredito AS tieneCredito,
        p.total AS total,
        p.orden
    FROM Pedido p 
    JOIN p.cliente c
    WHERE p.fechaPedido >= :inicioDia
    AND p.fechaPedido < :finDia
    ORDER BY p.orden ASC
""")
    List<DetalleListaPedidoDTO> listarTodosPedidosHoyConFechas(
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia
    );

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

    @Modifying
    @Query("UPDATE Pedido p SET p.estado = :estado WHERE p.id = :id")
    int actualizarEstado(@Param("id") Long id, @Param("estado") String estado);

    @Modifying
    @Query("""
        UPDATE Pedido p 
        SET p.estado = :nuevoEstado, 
            p.idRepartidor = :idRepartidor,
            p.fechaEntrega = :fechaEntrega
        WHERE p.id = :pedidoId
    """)
    int actualizarEstadoyRepartidor(
            @Param("pedidoId") Long pedidoId,
            @Param("nuevoEstado") String nuevoEstado,
            @Param("idRepartidor") Long idRepartidor,
            @Param("fechaEntrega") LocalDateTime fechaEntrega
    );

    @Query("SELECT p.estado FROM Pedido p WHERE p.id = :id")
    String obtenerEstadoPedido(@Param("id") Long id);
}