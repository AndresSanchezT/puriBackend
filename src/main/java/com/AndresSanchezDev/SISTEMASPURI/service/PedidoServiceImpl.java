package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;
import com.AndresSanchezDev.SISTEMASPURI.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

    // Zona horaria de Perú (constante para reutilizar)
    private static final ZoneId PERU_ZONE = ZoneId.of("America/Lima");

    // Estados válidos del sistema
    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "registrado", "entregado", "anulado"
    );

    @Override
    public boolean verificarPedidoExistente(Long idCliente, String tipoFecha) {
        // Reutilizar la lógica que ya tienes
        int diasDesdeHoy = switch (tipoFecha.toUpperCase()) {
            case "HOY" -> 0;
            case "MANANA" -> 1;
            case "PASADO_MANANA" -> 2;
            default -> 0;
        };

        LocalDate fecha = LocalDate.now(PERU_ZONE).plusDays(diasDesdeHoy);
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.plusDays(1).atStartOfDay();

        return pedidoRepository.existePedidoRegistrado(idCliente, inicioDia, finDia);
    }

    @Transactional
    @Override
    public DetalleListaPedidoDTO registrarPedidoConVisitaYDetalles(
            Long idCliente,
            Long idVendedor,
            Pedido pedidoData,
            boolean forzarGuardar,
            TipoFechaPedido tipoFecha) { // ✅ Nuevo parámetro

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

        // ✅ 3. Calcular la fecha según el tipo
        LocalDateTime fechaPedido = calcularFechaPedido(tipoFecha != null ? tipoFecha : TipoFechaPedido.HOY);

//        // 4. Crear visita
//        Visita visita = new Visita();
//        visita.setCliente(cliente);
//        visita.setVendedor(vendedor);
//        visita.setFecha(LocalDateTime.now()); // La visita siempre es ahora
//        visita.setEstado("Pendiente");
//        visita.setObservaciones("Sin observaciones");
//        visitaRepository.save(visita);

        // 5. Construir pedido
        pedidoData.setCliente(cliente);
        pedidoData.setVendedor(vendedor);
//        pedidoData.setVisita(visita);
        pedidoData.setFechaPedido(fechaPedido); // ✅ Usar la fecha calculada
        pedidoData.setEstado("registrado");
        pedidoData.setCredito(pedidoData.getTotal());
        pedidoData.setObservaciones(pedidoData.getObservaciones());

        pedidoData.setIgv(0.0);

        pedidoData.getDetallePedidos().forEach(det -> det.setPedido(pedidoData));

        Pedido pedidoGuardado = pedidoRepository.save(pedidoData);

        // 6. Registrar BOLETA
        Boleta boleta = new Boleta();
        boleta.setCodigo(generarCodigoBoleta());
        boleta.setPedido(pedidoGuardado);
        boleta.setCliente(cliente);
        boleta.setVendedor(vendedor);
        boleta.setFechaEmision(fechaPedido); // ✅ Usar la misma fecha calculada
        boleta.setSubtotal(pedidoGuardado.getSubtotal());
        boleta.setIgv(pedidoGuardado.getIgv());
        boleta.setTotal(pedidoGuardado.getTotal());
        boleta.setEstado("REGISTRADA");
        boleta.setFechaRegistro(LocalDateTime.now()); // El registro siempre es ahora
        boletaRepository.save(boleta);

        // 7. ACTUALIZAR STOCK (solo si no faltó stock o si se fuerza)
        if (faltantes.isEmpty() || forzarGuardar) {
            for (DetallePedido det : pedidoData.getDetallePedidos()) {
                productoRepository.decrementarStock(
                        det.getProducto().getId(),
                        det.getCantidad()
                );
            }

            productoRepository.flush();
        }

        // ✅ 8. OBTENER Y DEVOLVER EL DTO
        return pedidoRepository.findDetallePedidoMinimosById(pedidoGuardado.getId())
                .orElseThrow(() -> new RuntimeException("Error al obtener el pedido creado"));
    }

    /**
     * ✅ Método auxiliar para calcular la fecha según el tipo
     */
    private LocalDateTime calcularFechaPedido(TipoFechaPedido tipo) {
        LocalDateTime ahora = LocalDateTime.now(PERU_ZONE); // Hora actual de Perú

        return switch (tipo) {
            case HOY -> ahora; // Mantiene fecha y hora actual
            case MANANA -> ahora.plusDays(1);
            case PASADO_MANANA -> ahora.plusDays(2);
        };
    }

    @Transactional
    @Override
    public void actualizarOrdenPedidos(List<Map<String, Object>> ordenMap) {
        for (Map<String, Object> item : ordenMap) {
            Long id = ((Number) item.get("id")).longValue();
            Integer orden = ((Number) item.get("orden")).intValue();

            // Actualizar solo el campo orden
            pedidoRepository.findById(id).ifPresent(pedido -> {
                pedido.setOrden(orden);
                pedidoRepository.save(pedido);
            });
        }
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }
    @Override
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosHoy() {
        return listarPedidosRegistradosPorDia(0);
    }

    @Override
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosManana() {
        return listarPedidosRegistradosPorDia(1);
    }

    @Override
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosPasadoManana() {
        return listarPedidosRegistradosPorDia(2);
    }

    @Override
    public List<DetalleListaPedidoDTO> listarTodosPedidosHoy() {
        return listarTodosPedidosPorDia(0);
    }

    @Override
    public List<DetalleListaPedidoDTO> listarTodosPedidosManana() {
        return listarTodosPedidosPorDia(1);
    }

    @Override
    public List<DetalleListaPedidoDTO> listarTodosPedidosPasadoManana() {
        return listarTodosPedidosPorDia(2);
    }


    private List<DetalleListaPedidoDTO> listarPedidosRegistradosPorDia(int diasDesdeHoy) {
        LocalDate fecha = LocalDate.now(PERU_ZONE).plusDays(diasDesdeHoy);

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.plusDays(1).atStartOfDay();

        List<DetalleListaPedidoDTO> pedidos = pedidoRepository.listarPedidosRegistradosHoyConFechas(inicioDia, finDia);

        // ✅ ORDENAR por campo 'orden' antes de devolver
        pedidos.sort((p1, p2) -> {
            Integer orden1 = p1.getOrden() != null ? p1.getOrden() : Integer.MAX_VALUE;
            Integer orden2 = p2.getOrden() != null ? p2.getOrden() : Integer.MAX_VALUE;
            return orden1.compareTo(orden2);
        });

        return pedidos;
    }

    private List<DetalleListaPedidoDTO> listarTodosPedidosPorDia(int diasDesdeHoy) {
        LocalDate fecha = LocalDate.now(PERU_ZONE).plusDays(diasDesdeHoy);

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.plusDays(1).atStartOfDay();

        List<DetalleListaPedidoDTO> pedidos = pedidoRepository.listarTodosPedidosHoyConFechas(inicioDia, finDia);

        // ✅ ORDENAR por campo 'orden' antes de devolver
        pedidos.sort((p1, p2) -> {
            Integer orden1 = p1.getOrden() != null ? p1.getOrden() : Integer.MAX_VALUE;
            Integer orden2 = p2.getOrden() != null ? p2.getOrden() : Integer.MAX_VALUE;
            return orden1.compareTo(orden2);
        });

        return pedidos;
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidosDeHoyReporteWeb() {
        LocalDate hoyPeru = LocalDate.now(PERU_ZONE);
        LocalDateTime inicioDia = hoyPeru.atStartOfDay();
        LocalDateTime finDia = hoyPeru.atTime(LocalTime.MAX);

        return pedidoRepository.listarPedidosPorRangoFecha(inicioDia, finDia);
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidosDeMananaReporteWeb(){
        LocalDate hoyPeru = LocalDate.now(PERU_ZONE).plusDays(1);
        LocalDateTime inicioDia = hoyPeru.atStartOfDay();
        LocalDateTime finDia = hoyPeru.atTime(LocalTime.MAX);

        return pedidoRepository.listarPedidosPorRangoFecha(inicioDia, finDia);
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
    @Transactional
    public void deleteById(Long id) {
        // Verificar si el pedido existe
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));

        // Verificar si tiene una boleta asociada
        Optional<Boleta> boletaAsociada = boletaRepository.findByPedidoId(id);

        if (boletaAsociada.isPresent()) {
            Boleta boleta = boletaAsociada.get();
            throw new IllegalStateException(
                    "No se puede eliminar el pedido #" + id + " porque tiene la boleta " +
                            boleta.getCodigo() + " asociada. Elimine primero la boleta."
            );
        }

        // Si no tiene boleta, eliminar normalmente
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
    public List<ReporteProductoDTO> reporteProductosRegistradosManana() {
        LocalDate manana = LocalDate.now(PERU_ZONE).plusDays(1);

        LocalDateTime inicioDia = manana.atStartOfDay();
        LocalDateTime finDia = manana.plusDays(1).atStartOfDay();

        return pedidoRepository.reporteProductosRegistradosConFechas(inicioDia, finDia);
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
    @Transactional
    @Override
    public void actualizarEstadoyRepartidor(Long pedidoId, CambiarEstadoPedidoDTO dto) {
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
        Long idRepartidor = dto.getIdRepartidor();

        // 🔥 Si el estado es "entregado", guardar la fecha actual
        LocalDateTime fechaEntrega = "entregado".equals(nuevoEstado)
                ? LocalDateTime.now()
                : null;


        // Actualizar estado de forma optimizada
        int filasActualizadas = pedidoRepository.actualizarEstadoyRepartidor(pedidoId, nuevoEstado,idRepartidor,fechaEntrega);

        if (filasActualizadas == 0) {
            throw new RuntimeException("No se pudo actualizar el estado del pedido");
        }
    }
    @Override
    public Double obtenerEfectivoDelDia(Long idRepartidor) {
        LocalDateTime hoy = LocalDateTime.now();
        return pedidoRepository.sumarEfectivoDelDia(idRepartidor, hoy);
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
