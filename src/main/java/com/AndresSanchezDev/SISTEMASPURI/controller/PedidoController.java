package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.*;

import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;

import com.AndresSanchezDev.SISTEMASPURI.entity.ProductoFaltante;
import com.AndresSanchezDev.SISTEMASPURI.entity.TipoFechaPedido;
import com.AndresSanchezDev.SISTEMASPURI.service.BoletaService;
import com.AndresSanchezDev.SISTEMASPURI.service.PedidoService;
import com.AndresSanchezDev.SISTEMASPURI.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final BoletaService boletaService;

    public PedidoController(PedidoService service, ProductoService productoService, BoletaService boletaService) {
        this.pedidoService = service;
        this.productoService = productoService;
        this.boletaService = boletaService;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/reporte/hoy")
    public ResponseEntity<List<Pedido>> obtenerReportePedidosHoy() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidosDeHoyReporteWeb();
        return ResponseEntity.ok(pedidos);
    }
    @GetMapping("/reporte/manana")
    public ResponseEntity<List<Pedido>> obtenerReportePedidosManana() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidosDeMananaReporteWeb();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/all-mobile")
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosHoy() {
        return pedidoService.listarPedidosRegistradosHoy();
    }
    @GetMapping("/all-mobile-admin")
    public List<DetalleListaPedidoDTO> listarTodosPedidosHoy() {
        return pedidoService.listarTodosPedidosHoy();
    }

    @GetMapping("/all-mobile/manana")
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosManana() {
        return pedidoService.listarPedidosRegistradosManana();
    }

    @GetMapping("/all-mobile-admin/manana")
    public List<DetalleListaPedidoDTO> listarTodosPedidosManana() {
        return pedidoService.listarTodosPedidosManana();
    }

    // ✅ Nuevos endpoints para PASADO MAÑANA
    @GetMapping("/all-mobile/pasado-manana")
    public List<DetalleListaPedidoDTO> listarPedidosRegistradosPasadoManana() {
        return pedidoService.listarPedidosRegistradosPasadoManana();
    }

    @GetMapping("/all-mobile-admin/pasado-manana")
    public List<DetalleListaPedidoDTO> listarTodosPedidosPasadoManana() {
        return pedidoService.listarTodosPedidosPasadoManana();
    }

    @GetMapping("/{id}")
    public Optional<PedidoResponseDTO.PedidoDTO> obtenerDetallePedidoCompletoPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoCompleto(id);
    }

    @GetMapping("/{id}/completo")
    public Optional<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @GetMapping("/hoy")
    public List<Pedido> countPedidosHoy() {
        return pedidoService.pedidosHoy();
    }

    @GetMapping("/total")
    public long countPedidosTotales() {
        return pedidoService.countPedidosTotales();
    }

    @GetMapping("/productos-registrados")
    public List<ReporteProductoDTO> reporteProductos() {
        return pedidoService.reporteProductosRegistrados();
    }

    @GetMapping("/productos-registrados/manana")
    public List<ReporteProductoDTO> reporteProductosManana() {
        return pedidoService.reporteProductosRegistradosManana();
    }
    @GetMapping("/productos-registrados/hoy")
    public List<ReporteProductoDTO> reporteProductosHoy() {
        return pedidoService.reporteProductosRegistradosHoy();
    }

    @GetMapping("/faltantes")
    public List<ProductoFaltante> listarFaltantes() {
        return productoService.listarFaltantes();
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido) {
        return pedidoService.save(pedido);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> verificarPedidoExistente(
            @PathVariable Long id,
            @RequestParam String tipoFecha
    ) {
        try {
            boolean existe = pedidoService.verificarPedidoExistente(id, tipoFecha);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }

    @PostMapping("/registrar/{idCliente}/{idVendedor}")
    public ResponseEntity<DetalleListaPedidoDTO> registrarPedido(
            @PathVariable Long idCliente,
            @PathVariable Long idVendedor,
            @RequestParam(defaultValue = "false") boolean forzar,
            @RequestParam(defaultValue = "HOY") TipoFechaPedido tipoFecha, // ✅ Nuevo parámetro
            @RequestBody Pedido pedidoData) {

        DetalleListaPedidoDTO pedido = pedidoService.registrarPedidoConVisitaYDetalles(
                idCliente, idVendedor, pedidoData, forzar, tipoFecha); // ✅ Pasar tipoFecha

        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/validar-pedido")
    public ResponseEntity<List<ItemPedidoDTO>> validarPedido(@RequestBody List<ItemPedidoDTO> data) {
        List<ItemPedidoDTO> faltantes = pedidoService.validarStock(data);
        return ResponseEntity.ok(faltantes);
    }

    @PutMapping("/{id}")
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return pedidoService.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            pedidoService.deleteById(id);
            return ResponseEntity.ok()
                    .body(Map.of("mensaje", "Pedido eliminado exitosamente"));

        } catch (IllegalStateException e) {
            // Error: tiene boleta asociada
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "mensaje", e.getMessage(),
                            "codigo", "PEDIDO_TIENE_BOLETA"
                    ));

        } catch (EntityNotFoundException e) {
            // Error: pedido no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", e.getMessage(),
                            "codigo", "PEDIDO_NO_ENCONTRADO"
                    ));

        } catch (Exception e) {
            // Error genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al eliminar el pedido",
                            "codigo", "ERROR_INTERNO"
                    ));
        }
    }

    @PutMapping("/actualizar-orden")
    public ResponseEntity<Void> actualizarOrdenPedidos(@RequestBody List<OrdenPedidoRequest> ordenRequests) {
        try {
            for (OrdenPedidoRequest request : ordenRequests) {
                Long id = request.getId();
                Integer orden = request.getOrden();

                Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(id);

                if (pedidoOpt.isPresent()) {
                    Pedido pedido = pedidoOpt.get();
                    pedido.setOrden(orden);
                    pedidoService.save(pedido);
                }
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/antiguos")
    public ResponseEntity<?> eliminarPedidosAntiguos() {
        try {
            Map<String, Object> resultado = pedidoService.eliminarPedidosAntiguos();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/mobil/{id}")
    public ResponseEntity<?> deleteMobil(@PathVariable Long id) {
        try {
            // 1. Buscar si existe una boleta asociada a este pedido
            Boleta boleta = boletaService.findByPedidoId(id);
            if (boleta != null) {
                boletaService.deleteById(boleta.getId());
            }

            // 2. Ahora eliminar el pedido
            pedidoService.deleteById(id);

            return ResponseEntity.ok()
                    .body(Map.of("mensaje", "Pedido eliminado exitosamente"));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "mensaje", e.getMessage(),
                            "codigo", "PEDIDO_TIENE_BOLETA"
                    ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", e.getMessage(),
                            "codigo", "PEDIDO_NO_ENCONTRADO"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al eliminar el pedido",
                            "codigo", "ERROR_INTERNO"
                    ));
        }
    }

    @DeleteMapping("/resetear-faltantes")
    public void resetearProductosFaltantes(){
        this.productoService.deleteAllProductosFaltantes();
    }

    /**
     * Endpoint para cambiar el estado de un pedido
     * PATCH /api/pedidos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoPedidoDTO dto) {
        try {
            pedidoService.cambiarEstado(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado actualizado correctamente");
            response.put("nuevoEstado", dto.getNuevoEstado());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(409).body(error); // 409 Conflict

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar el estado del pedido");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PatchMapping("/{id}/estado-movil")
    public ResponseEntity<?> cambiarEstadoyRepartidor(
            @PathVariable Long id,
            @RequestBody CambiarEstadoPedidoDTO dto) {
        try {
            pedidoService.actualizarEstadoyRepartidor(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado actualizado y repartidor asignado correctamente");
            response.put("nuevoEstado", dto.getNuevoEstado());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(409).body(error); // 409 Conflict

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar el estado del pedido");
            return ResponseEntity.internalServerError().body(error);
        }
    }
    @GetMapping("/efectivo-del-dia/{idRepartidor}")
    public ResponseEntity<Map<String, Object>> obtenerEfectivoDelDia(
            @PathVariable Long idRepartidor) {
        try {
            Double efectivo = pedidoService.obtenerEfectivoDelDia(idRepartidor);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("idRepartidor", idRepartidor);
            response.put("efectivo", efectivo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener efectivo del día");
            error.put("efectivo", 0.0);

            return ResponseEntity.internalServerError().body(error);
        }
    }
}