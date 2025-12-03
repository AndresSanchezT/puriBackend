package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoResponseDTO {

    public static class ClienteDTO {
        private String nombre;
        private String direccion;
        private String telefono;

        public ClienteDTO() {}

        public ClienteDTO(Cliente cliente) {
            this.nombre = cliente.getNombreContacto();
            this.direccion = cliente.getDireccion();
            this.telefono = cliente.getTelefono();
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }
    }

    public static class UsuarioDTO {
        private String nombreVendedor;
        public UsuarioDTO() {}
        public UsuarioDTO(Usuario usuario) {
            this.nombreVendedor = usuario.getNombre();
        }
        public String getNombreVendedor() {
            return nombreVendedor;
        }
        public void setNombreVendedor(String nombreVendedor) {
            this.nombreVendedor = nombreVendedor;
        }
    }

    public static class VisitaDTO {
        private String observaciones;

        public VisitaDTO() {}

        public VisitaDTO(Visita visita) {
            this.observaciones = visita.getObservaciones();
        }

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }
    }

    public static class ProductoDTO {
        private Long id;
        private String nombre;
        private Double precio;
        private String unidadMedida;

        public ProductoDTO() {}

        public ProductoDTO(Producto producto) {
            this.id = producto.getId();
            this.nombre = producto.getNombre();
            this.precio = producto.getPrecio();
            this.unidadMedida = producto.getUnidadMedida();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        public String getUnidadMedida() {
            return unidadMedida;
        }

        public void setUnidadMedida(String unidadMedida) {
            this.unidadMedida = unidadMedida;
        }
    }

    public static class DetallePedidoDTO {
        private Long id;
        private ProductoDTO producto;
        private int cantidad;
        private double precioUnitario;
        private Double subtotal;

        public DetallePedidoDTO() {}

        public DetallePedidoDTO(DetallePedido detalle) {
            this.id = detalle.getId();
            this.producto = new ProductoDTO(detalle.getProducto());
            this.cantidad = detalle.getCantidad();
            this.precioUnitario = detalle.getPrecioUnitario();
            this.subtotal = detalle.getSubtotal();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ProductoDTO getProducto() {
            return producto;
        }

        public void setProducto(ProductoDTO producto) {
            this.producto = producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(double precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public Double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(Double subtotal) {
            this.subtotal = subtotal;
        }
    }

    public static class PedidoDTO {
        private Long id;
        private ClienteDTO cliente;
        private UsuarioDTO vendedor;
        private VisitaDTO visita;
        private Double subtotal;
        private Double igv;
        private Double total;
        private String estado;
        private String observaciones;
        private List<DetallePedidoDTO> detallePedidos;

        public PedidoDTO() {}

        public PedidoDTO(Pedido pedido) {
            this.id = pedido.getId();
            this.cliente = pedido.getCliente() != null ? new ClienteDTO(pedido.getCliente()) : null;
            this.vendedor = pedido.getVendedor() != null ? new UsuarioDTO(pedido.getVendedor()) : null;
            this.visita = pedido.getVisita() != null ? new VisitaDTO(pedido.getVisita()) : null;
            this.subtotal = pedido.getSubtotal();
            this.igv = pedido.getIgv();
            this.total = pedido.getTotal();
            this.estado = pedido.getEstado();
            this.observaciones = pedido.getObservaciones();
            this.detallePedidos = pedido.getDetallePedidos() != null
                    ? pedido.getDetallePedidos().stream()
                    .map(DetallePedidoDTO::new)
                    .collect(Collectors.toList())
                    : new ArrayList<>();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ClienteDTO getCliente() {
            return cliente;
        }

        public void setCliente(ClienteDTO cliente) {
            this.cliente = cliente;
        }

        public UsuarioDTO getVendedor() {
            return vendedor;
        }
        public void setVendedor(UsuarioDTO vendedor) {
            this.vendedor = vendedor;
        }
        public VisitaDTO getVisita() {
            return visita;
        }

        public void setVisita(VisitaDTO visita) {
            this.visita = visita;
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

        public List<DetallePedidoDTO> getDetallePedidos() {
            return detallePedidos;
        }

        public void setDetallePedidos(List<DetallePedidoDTO> detallePedidos) {
            this.detallePedidos = detallePedidos;
        }
    }
}