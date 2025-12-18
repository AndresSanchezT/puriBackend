package com.AndresSanchezDev.SISTEMASPURI.repository;

import com.AndresSanchezDev.SISTEMASPURI.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findByTipo(String tipo, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Producto p WHERE p.stockActual < p.stockMinimo")
    long countProductosStockBajo();

    //PARA 20 PRODUCTOS APROX ESTA BIEN // 200ms - con una query batch nativa seria mas optimo para mas productos
    @Modifying
    @Transactional
    @Query("""
    UPDATE Producto p 
    SET p.stockActual = CASE 
        WHEN (p.stockActual - :cantidad) < 0 THEN 0 
        ELSE (p.stockActual - :cantidad) 
    END,
    p.fechaActualizacion = CURRENT_DATE
    WHERE p.id = :productoId
""")
    void decrementarStock(@Param("productoId") Long productoId,
                          @Param("cantidad") Integer cantidad);
}