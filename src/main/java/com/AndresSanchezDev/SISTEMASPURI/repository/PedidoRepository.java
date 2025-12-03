package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleListaPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE FUNCTION('DATE', p.fechaPedido) = CURRENT_DATE")
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
        END AS estado
    FROM pedido p
    INNER JOIN detalle_pedido dp ON p.id = dp.id_pedido
    INNER JOIN producto pr ON dp.id_producto = pr.id
    WHERE p.estado = 'registrado'
    GROUP BY pr.id, pr.nombre, pr.stock_actual, pr.stock_minimo
    ORDER BY pr.nombre ASC
""", nativeQuery = true)
    List<ReporteProductoDTO> reporteProductosRegistrados();

    @Query(value = """
    SELECT c.nombre_cliente,
            c.direccion,
            c.referencia,
            c.telefono,
            v.observaciones,
            u.nombre_usuario,
            
        
    FROM pedido p
        INNER JOIN visita v ON p.id_visita = v.id
    INNER JOIN usuario u ON p.id_vendedor = u.id
    INNER JOIN cliente c ON p.id_cliente = c.id   
    INNER JOIN detalle_pedido dp ON p.id = dp.id_pedido
    INNER JOIN producto pr ON dp.id_producto = pr.id
    WHERE p.estado = 'registrado'
    GROUP BY pr.id, pr.nombre, pr.stock_actual, pr.stock_minimo
    ORDER BY pr.nombre ASC
""", nativeQuery = true)
    Optional<Pedido> obtenerPedidoCompleto(@Param("id") Long id);


    @Query("""
    SELECT 
        p.id as id,
        c.nombreContacto AS nombreCliente,
        c.direccion AS direccion,
        p.estado AS estado,
        c.tieneCredito AS tieneCredito
    FROM Pedido p 
    JOIN p.cliente c
""")
    List<DetalleListaPedidoDTO> listarPedidosMinimos();
}
