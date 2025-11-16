package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ItemPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BoletaRepository boletaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    @Override
    public Pedido registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData, boolean forzarGuardar) {
        // 1. Validar stock antes de guardar
        List<ItemPedidoDTO> items = pedidoData.getDetallePedidos()
                .stream()
                .map(d -> new ItemPedidoDTO(d.getProducto().getId(), d.getCantidad()))
                .toList();

        List<ItemPedidoDTO> faltantes = validarStock(items);

        if (!faltantes.isEmpty() && !forzarGuardar) {
            throw new RuntimeException("Falta stock para: " +
                    faltantes.stream().map(ItemPedidoDTO::getProductoId).toList());
        }

        // 2. Buscar cliente y vendedor
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Usuario vendedor = usuarioRepository.findById(idVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        // 3. Crear visita
        Visita visita = new Visita();
        visita.setCliente(cliente);
        visita.setVendedor(vendedor);
        visita.setFecha(LocalDate.now());
        visita.setEstado("Pendiente");
        visita.setObservaciones("Sin observaciones");
        visitaRepository.save(visita);

        // 4. Construir pedido
        pedidoData.setCliente(cliente);
        pedidoData.setVendedor(vendedor);
        pedidoData.setVisita(visita);
        pedidoData.setFechaPedido(LocalDate.now());
        pedidoData.setEstado("registrado");

        double subtotal = pedidoData.getDetallePedidos().stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();

        pedidoData.setSubtotal(subtotal);
        pedidoData.setIgv(subtotal * 0.18);
        pedidoData.setTotal(subtotal * 1.18);

        pedidoData.getDetallePedidos().forEach(det -> det.setPedido(pedidoData));

        Pedido pedidoGuardado = pedidoRepository.save(pedidoData);

        // 5. Registrar BOLETA
        Boleta boleta = new Boleta();
        boleta.setCodigo(generarCodigoBoleta());
        boleta.setPedido(pedidoGuardado);
        boleta.setCliente(cliente);
        boleta.setVendedor(vendedor);
        boleta.setFechaEmision(LocalDateTime.now());
        boleta.setSubtotal(pedidoGuardado.getSubtotal());
        boleta.setIgv(pedidoGuardado.getIgv());
        boleta.setTotal(pedidoGuardado.getTotal());
        boleta.setEstado("REGISTRADA");
        boleta.setFechaRegistro(LocalDateTime.now());
        boletaRepository.save(boleta);

        // 6. ACTUALIZAR STOCK (solo si NO faltó stock o si se forzó)
        if (faltantes.isEmpty() || forzarGuardar) {
            for (DetallePedido det : pedidoData.getDetallePedidos()) {
                Producto producto = det.getProducto();
                int nuevoStock = producto.getStockActual() - det.getCantidad();
                producto.setStockActual(Math.max(nuevoStock, 0));
                productoRepository.save(producto);
            }
        }

        // Opcional: guardar faltantes en una tabla
        // faltanteProductoService.guardarFaltantes(pedidoGuardado, faltantes);

        return pedidoGuardado;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> pedidosHoy(){
        return pedidoRepository.pedidosHoy();
    }

    @Override
    public long countPedidosTotales() {
        return pedidoRepository.count();
    }

    @Override
    public List<ReporteProductoDTO> reporteProductosRegistrados() {
        return pedidoRepository.reporteProductosRegistrados();
    }

    @Override
    public List<ItemPedidoDTO> validarStock(List<ItemPedidoDTO> items) {
        List<ItemPedidoDTO> productosConFaltante = new ArrayList<>();

        for (ItemPedidoDTO item : items) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            item.setStockActual(producto.getStockActual());
            item.setCantidadFaltante(Math.max(0, item.getCantidadSolicitada() - producto.getStockActual()));

            if (item.getCantidadFaltante() > 0) {
                productosConFaltante.add(item);
            }
        }
        return productosConFaltante;
    }

    // ----------------------------------------------------------
// Método para generar códigos únicos
// ----------------------------------------------------------
    private String generarCodigoBoleta() {

        Boleta ultima = boletaRepository.findUltimaBoleta();

        int numero = 1;

        if (ultima != null) {
            // Ejemplo código: "BOL-00023"
            String codigoAnterior = ultima.getCodigo();
            String numeroStr = codigoAnterior.substring(4); // "00023"
            numero = Integer.parseInt(numeroStr) + 1;
        }

        // Formato 5 dígitos → 00001, 00002, 00003...
        String numeroFormateado = String.format("%05d", numero);

        return "BOL-" + numeroFormateado;
    }
}
