package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    private Long id;
    private Long id_vendedor;  // ✅ Recibir como ID
    private Long id_cliente;   // ✅ Recibir como ID
    private Long visita_id;    // ✅ Recibir como ID
    private LocalDate fechaPedido;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String estado;
    private String observaciones;
    private List<ProductoDTO> productos; // ✅ Productos del pedido

    // Constructor vacío
    public PedidoDTO() {
    }

    // Constructor completo
    public PedidoDTO(Long id, Long id_vendedor, Long id_cliente, Long visita_id,
                     LocalDate fechaPedido, Double subtotal, Double igv, Double total,
                     String estado, String observaciones, List<ProductoDTO> productos) {
        this.id = id;
        this.id_vendedor = id_vendedor;
        this.id_cliente = id_cliente;
        this.visita_id = visita_id;
        this.fechaPedido = fechaPedido;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;
        this.productos = productos;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(Long id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Long getVisita_id() {
        return visita_id;
    }

    public void setVisita_id(Long visita_id) {
        this.visita_id = visita_id;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }

    // Clase interna para los productos del pedido
    public static class ProductoDTO {
        private Long id_producto;
        private Integer cantidad;
        private Double precio_unitario;
        private Double subtotal;

        public ProductoDTO() {
        }

        public ProductoDTO(Long id_producto, Integer cantidad, Double precio_unitario, Double subtotal) {
            this.id_producto = id_producto;
            this.cantidad = cantidad;
            this.precio_unitario = precio_unitario;
            this.subtotal = subtotal;
        }

        public Long getId_producto() {
            return id_producto;
        }

        public void setId_producto(Long id_producto) {
            this.id_producto = id_producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Double getPrecio_unitario() {
            return precio_unitario;
        }

        public void setPrecio_unitario(Double precio_unitario) {
            this.precio_unitario = precio_unitario;
        }

        public Double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(Double subtotal) {
            this.subtotal = subtotal;
        }
    }
}