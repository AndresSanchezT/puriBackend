package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleListaPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ItemPedidoDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.PedidoResponseDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ReporteProductoDTO;
import com.AndresSanchezDev.SISTEMASPURI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductoFaltanteRepository productoFaltanteRepository;

    @Transactional
    @Override
    public Pedido registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor,
                                                    Pedido pedidoData, boolean forzarGuardar) {

        // 1. Validar stock antes de guardar (NO toca DB)
        List<ItemPedidoDTO> items = pedidoData.getDetallePedidos()
                .stream()
                .map(d -> new ItemPedidoDTO(d.getProducto().getId(), d.getCantidad()))
                .toList();

        List<ItemPedidoDTO> faltantes = validarStock(items);

        if (!faltantes.isEmpty() && !forzarGuardar) {
            throw new RuntimeException("Falta stock para: " +
                    faltantes.stream().map(ItemPedidoDTO::getNombre).toList());
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
        visita.setFecha(LocalDateTime.now());
        visita.setEstado("Pendiente");
        visita.setObservaciones("Sin observaciones");
        visitaRepository.save(visita);

        // 4. Construir pedido
        pedidoData.setCliente(cliente);
        pedidoData.setVendedor(vendedor);
        pedidoData.setVisita(visita);
        pedidoData.setFechaPedido(LocalDateTime.now());
        pedidoData.setEstado("registrado");
        pedidoData.setObservaciones(pedidoData.getObservaciones());

//        double subtotal = pedidoData.getDetallePedidos().stream()
//                .mapToDouble(DetallePedido::getSubtotal)
//                .sum();

//        pedidoData.setSubtotal(subtotal);
        pedidoData.setIgv(0.0);
//        pedidoData.setTotal(subtotal + pedidoData.getIgv());

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

        // 6. ACTUALIZAR STOCK (solo si no faltó stock o si se fuerza)
        if (faltantes.isEmpty() || forzarGuardar) {
            for (DetallePedido det : pedidoData.getDetallePedidos()) {
                productoRepository.decrementarStock(
                        det.getProducto().getId(),
                        det.getCantidad()
                );
            }

            // Flush para asegurar que los updates se ejecuten
            productoRepository.flush();
        }

        return pedidoGuardado;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }


    @Override
    public List<DetalleListaPedidoDTO> findAllDetallesPedido() {
        return pedidoRepository.listarPedidosMinimos();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido update(Long id, Pedido pedidoNuevo) {

        Pedido pedidoDB = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidoDB.setSubtotal(pedidoNuevo.getSubtotal());
        pedidoDB.setIgv(pedidoNuevo.getIgv());
        pedidoDB.setTotal(pedidoNuevo.getTotal());

        // --- DETALLES (TU LÓGICA YA CORRECTA) ---
        Map<Long, DetallePedido> existentes = pedidoDB.getDetallePedidos().stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(DetallePedido::getId, d -> d));

        List<DetallePedido> nuevos = new ArrayList<>();

        for (DetallePedido dNuevo : pedidoNuevo.getDetallePedidos()) {

            if (dNuevo.getId() != null && existentes.containsKey(dNuevo.getId())) {
                DetallePedido dDB = existentes.get(dNuevo.getId());
                dDB.setCantidad(dNuevo.getCantidad());
                dDB.setPrecioUnitario(dNuevo.getPrecioUnitario());
                dDB.setSubtotal(dNuevo.getSubtotal());
                nuevos.add(dDB);
            } else {
                dNuevo.setId(null);
                dNuevo.setPedido(pedidoDB);
                nuevos.add(dNuevo);
            }
        }

        pedidoDB.getDetallePedidos().clear();
        pedidoDB.getDetallePedidos().addAll(nuevos);

        // --- SINCRONIZAR BOLETA (SI EXISTE) ---
        boletaRepository.findByPedidoId(pedidoDB.getId())
                .ifPresent(boleta -> {
                    boleta.setSubtotal(pedidoDB.getSubtotal());
                    boleta.setIgv(pedidoDB.getIgv());
                    boleta.setTotal(pedidoDB.getTotal());
                    boleta.setFechaRegistro(LocalDateTime.now());
                });

        return pedidoRepository.save(pedidoDB);
    }

    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> pedidosHoy() {
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

        // Obtener ids únicos
        List<Long> ids = items.stream()
                .map(ItemPedidoDTO::getProductoId)
                .distinct()
                .toList();

        // Traer productos existentes
        Map<Long, Producto> map = productoRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        Producto::getId,
                        p -> p,
                        (a, b) -> a
                ));

        List<ItemPedidoDTO> faltantes = new ArrayList<>();

        for (ItemPedidoDTO item : items) {

            Producto p = map.get(item.getProductoId());

            if (p == null) {
                throw new RuntimeException("Producto no encontrado: " + item.getProductoId());
            }

            int faltante = Math.max(0, item.getCantidadSolicitada() - p.getStockActual());

            if (faltante > 0) {
                ItemPedidoDTO dto = new ItemPedidoDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getStockActual(),
                        item.getCantidadSolicitada(),
                        faltante
                );

                faltantes.add(dto);

                // 🔥 Guardar faltante en BD
                ProductoFaltante pf = new ProductoFaltante();
                pf.setProductoId(p.getId());
                pf.setNombreProducto(p.getNombre());
                pf.setStockActual(p.getStockActual());
                pf.setCantidadSolicitada(item.getCantidadSolicitada());
                pf.setCantidadFaltante(faltante);

                productoFaltanteRepository.save(pf);
            }
        }

        return faltantes;
    }

    @Override
    public Optional<PedidoResponseDTO.PedidoDTO> obtenerPedidoCompleto(Long idPedido) {
            return pedidoRepository.obtenerDetallesPedidoCompletoPorId(idPedido);
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
