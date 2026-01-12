package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;
import com.AndresSanchezDev.SISTEMASPURI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    // Estados válidos del sistema
    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "registrado", "entregado", "anulado"
    );

    @Transactional
    @Override
    public DetalleListaPedidoDTO registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor,
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

        // ✅ 7. OBTENER Y DEVOLVER EL DTO
        return pedidoRepository.findDetallePedidoMinimosById(pedidoGuardado.getId())
                .orElseThrow(() -> new RuntimeException("Error al obtener el pedido creado"));
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<DetalleListaPedidoDTO> findAllDetallesPedido() {
        return pedidoRepository.listarPedidosRegistrados();
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
        pedidoDB.setTotal(pedidoNuevo.getTotal());
        pedidoDB.setYape(pedidoNuevo.getYape());
        pedidoDB.setPlin(pedidoNuevo.getPlin());
        pedidoDB.setEfectivo(pedidoNuevo.getEfectivo());
        pedidoDB.setCredito(pedidoNuevo.getCredito());
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
                    boleta.setFechaActualizacion(LocalDateTime.now());
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
    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
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

            Double faltante = Math.max(0, item.getCantidadSolicitada() - p.getStockActual());

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

    //FUNCIONES PARA CAMBIAR ESTADO DE PEDIDOS:
    @Transactional
    @Override
    public void cambiarEstado(Long pedidoId, CambiarEstadoPedidoDTO dto) {
        // Validar que el estado sea válido
        String nuevoEstado = dto.getNuevoEstado().toLowerCase();
        if (!ESTADOS_VALIDOS.contains(nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Estado inválido. Estados permitidos: " + ESTADOS_VALIDOS
            );
        }

        // Obtener estado actual del pedido
        String estadoActual = pedidoRepository.obtenerEstadoPedido(pedidoId);
        if (estadoActual == null) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId);
        }

        // Validar transiciones de estado permitidas
        validarTransicionEstado(estadoActual, nuevoEstado);

        // Actualizar estado de forma optimizada
        int filasActualizadas = pedidoRepository.actualizarEstado(pedidoId, nuevoEstado);

        if (filasActualizadas == 0) {
            throw new RuntimeException("No se pudo actualizar el estado del pedido");
        }

        // Si se anula, registrar el motivo (opcional: crear tabla de auditoría)
//        if ("anulado".equals(nuevoEstado) && dto.getMotivoAnulacion() != null) {
//            registrarAnulacion(pedidoId, dto.getMotivoAnulacion());
//        }
    }

    @Override
    public void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        // Un pedido anulado no puede cambiar de estado
        if ("anulado".equals(estadoActual)) {
            throw new IllegalStateException(
                    "No se puede cambiar el estado de un pedido anulado"
            );
        }

        // Un pedido entregado solo puede ser anulado
        if ("entregado".equals(estadoActual) && !"anulado".equals(nuevoEstado)) {
            throw new IllegalStateException(
                    "Un pedido entregado solo puede ser anulado"
            );
        }

        // Un pedido registrado puede ser entregado o anulado
        if ("registrado".equals(estadoActual)) {
            if (!("entregado".equals(nuevoEstado) || "anulado".equals(nuevoEstado))) {
                throw new IllegalStateException(
                        "Un pedido registrado solo puede pasar a 'entregado' o 'anulado'"
                );
            }
        }
    }

    /**
     * Registra el motivo de anulación (opcional)
     * Podrías crear una tabla de auditoría para esto
     */
//    @Override
//    public void registrarAnulacion(Long pedidoId, String motivo) {
//        // Aquí podrías guardar en una tabla de auditoría
//        // Por ahora solo actualizamos el campo observaciones
//        Pedido pedido = pedidoRepository.findById(pedidoId)
//                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
//
//        String observacionActual = pedido.getObservaciones() != null
//                ? pedido.getObservaciones()
//                : "";
//        pedido.setObservaciones(
//                observacionActual + " | ANULADO: " + motivo
//        );
//        pedidoRepository.save(pedido);
//    }


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
