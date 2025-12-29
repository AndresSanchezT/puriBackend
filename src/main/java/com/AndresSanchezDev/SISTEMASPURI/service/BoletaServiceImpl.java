package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.Cliente;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import com.AndresSanchezDev.SISTEMASPURI.repository.BoletaRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.PedidoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class BoletaServiceImpl implements BoletaService {
    // ========== CONFIGURACIÓN FIJA DE LA EMPRESA ==========
    private static final String RUC_EMPRESA = "10407072507";
    private static final String NOMBRE_EMPRESA = "J & R DISTRIBUCIONES";
    private static final String DIRECCION_EMPRESA = "Los proceres, chuquitanta. San Martin de Porres, 512000";
    private static final String TELEFONO_EMPRESA = "987437118";
    private static final String EMAIL_EMPRESA = "contacto@jyrdistribuciones.com";

    @Autowired
    private BoletaRepository repository;
    @Autowired
    private PedidoRepository pedidoRepository;

    // ========== OPTIMIZACIÓN 1: Cachear reportes compilados ==========
    private JasperReport jasperReport;
    private InputStream logoStream;

    @Override
    public List<Boleta> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Boleta> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Boleta save(Boleta boleta) {
        return repository.save(boleta);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public String actualizarEstado(Long id, ActualizarEstadoBoletaDTO dto) {
        Boleta boleta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));

        String estado = dto.getNuevoEstado();

        switch (estado.toUpperCase()) {

            case "ANULADO":
                if (boleta.getEstado().equals("ANULADO")) {
                    throw new RuntimeException("La boleta ya está anulada");
                }
                boleta.setEstado("ANULADO");
                boleta.setMotivoAnulacion(dto.getInformacion());
                boleta.setFechaAnulacion(LocalDateTime.now());
                break;

            case "PAGADO":
                if (boleta.getEstado().equals("PAGADO")) {
                    throw new RuntimeException("La boleta ya está pagada");
                }
                boleta.setEstado("PAGADO");
                boleta.setMotivoAnulacion(dto.getInformacion()); // opcional limpiar motivo
                break;

            default:
                throw new RuntimeException("Estado no válido: " + estado);
        }

        repository.save(boleta);
        return "Boleta actualizada";
    }

    @PostConstruct
    public void init() {
        try {
            log.info("🔄 Cargando reporte de boleta...");

            InputStream jrxmlStream = getClass()
                    .getResourceAsStream("/reportes/puri_boleta.jrxml");

            if (jrxmlStream == null) {
                throw new RuntimeException("❌ No se encontró /reportes/puri_boleta.jrxml");
            }

            // ✅ SIEMPRE compilar desde JRXML (evita EOFException)
            jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            log.info("✅ Reporte de boleta compilado correctamente: {}", jasperReport.getName());

        } catch (Exception e) {
            log.error("❌ Error cargando reporte de boleta", e);
            throw new RuntimeException("No se pudo cargar el reporte", e);
        }
    }
    @Override
    public byte[] generarBoleta(Long boletaId) throws JRException {
        long inicio = System.currentTimeMillis();
        log.info("🔄 Iniciando generación de boleta ID: {}", boletaId);

        try {
            // Cargar boleta con todos los datos necesarios
            Boleta boleta = repository.findByIdConDetalles(boletaId)
                    .orElseThrow(() -> new RuntimeException("Boleta no encontrada: " + boletaId));
            Cliente c = boleta.getPedido().getCliente();
            log.info("🔍 DEBUG Cliente:");
            log.info("   - ID: {}", c.getId());
            log.info("   - nombreNegocio: '{}'", c.getNombreNegocio());
            log.info("   - nombreContacto: '{}'", c.getNombreContacto());
            log.info("   - direccion: '{}'", c.getDireccion());
            log.info("   - telefono: '{}'", c.getTelefono());

            log.debug("✅ Datos cargados en {}ms", System.currentTimeMillis() - inicio);

            // Preparar parámetros
            Map<String, Object> parametros = prepararParametros(boleta);

            // Preparar detalles
            List<DetalleBoletaDTO> detalles = prepararDetalles(boleta);

            log.debug("📋 Productos en boleta: {}", detalles.size());

            // Generar PDF
            long t1 = System.currentTimeMillis();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    dataSource
            );

            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            log.info("✅ PDF generado en {}ms (Tamaño: {} KB)",
                    System.currentTimeMillis() - t1,
                    pdf.length / 1024);
            log.info("⏱️ Tiempo total generación: {}ms", System.currentTimeMillis() - inicio);

            return pdf;

        } catch (Exception e) {
            log.error("❌ Error generando boleta {}: {}", boletaId, e.getMessage(), e);
            throw new JRException("Error generando boleta: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> prepararParametros(Boleta boleta) {
        Map<String, Object> parametros = new HashMap<>();

        // ========== DATOS DE LA EMPRESA (FIJOS) ==========
        parametros.put("ruc", RUC_EMPRESA);
        parametros.put("nombreEmpresa", NOMBRE_EMPRESA);
        parametros.put("direccionEmpresa", DIRECCION_EMPRESA);
        parametros.put("telefonoEmpresa", TELEFONO_EMPRESA);
        parametros.put("emailEmpresa", EMAIL_EMPRESA);

        // ========== DATOS DE LA BOLETA ==========
        parametros.put("codigo", boleta.getCodigo());

        // Fecha formateada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        parametros.put("fechaEmision", boleta.getFechaEmision().format(formatter));

        // ========== DATOS DEL CLIENTE ==========
        Cliente cliente = boleta.getPedido().getCliente();
        parametros.put("nombreCliente", cliente.getNombreContacto());
        parametros.put("direccionCliente", cliente.getDireccion() != null ? cliente.getDireccion() : "-");
        parametros.put("telefonoCliente", cliente.getTelefono() != null ? cliente.getTelefono() : "-");

        // ========== TOTALES ==========
        parametros.put("subtotal", BigDecimal.valueOf(boleta.getSubtotal()));
        parametros.put("igv", BigDecimal.valueOf(boleta.getIgv()));

        // ✅ Verificar si algún producto tiene unidad de medida "KGR"
        boolean tieneProductoKGR = boleta.getPedido().getDetallePedidos().stream()
                .anyMatch(detalle -> detalle.getProducto() != null &&
                        "KGR".equals(detalle.getProducto().getUnidadMedida()));

        // Si tiene producto KGR, enviar null, sino enviar el total
        parametros.put("total", tieneProductoKGR ? null : BigDecimal.valueOf(boleta.getTotal()));

        // ========== OBSERVACIONES ==========
        String observaciones = boleta.getPedido().getObservaciones();
        parametros.put("observaciones",
                observaciones != null && !observaciones.isBlank()
                        ? observaciones
                        : "Gracias por su preferencia.");

        // ========== LOGO ==========
        try {
            InputStream logoStream = getClass().getResourceAsStream("/reportes/puri_logo.png");
            if (logoStream != null) {
                parametros.put("logo", logoStream);
                log.debug("✅ Logo cargado");
            } else {
                log.warn("⚠️ Logo no encontrado en /reportes/puri_logo.png");
                parametros.put("logo", null);
            }
        } catch (Exception e) {
            log.error("❌ Error cargando logo", e);
            parametros.put("logo", null);
        }

        return parametros;
    }

    private List<DetalleBoletaDTO> prepararDetalles(Boleta boleta) {

        final int FILAS_FIJAS = 11;

        List<DetallePedido> detallesPedido =
                boleta.getPedido() != null
                        ? boleta.getPedido().getDetallePedidos()
                        : Collections.emptyList();

        List<DetalleBoletaDTO> resultado = new ArrayList<>();

        // 1️⃣ Mapear los detalles reales
        for (DetallePedido detalle : detallesPedido) {

            DetalleBoletaDTO dto = new DetalleBoletaDTO();

            if (detalle.getProducto() != null) {
                dto.setCodigoProducto(detalle.getProducto().getCodigo());
                dto.setNombreProducto(detalle.getProducto().getNombre());
                dto.setUnidadMedida(detalle.getProducto().getUnidadMedida());
            }

            BigDecimal cantidad = BigDecimal.valueOf(detalle.getCantidad()).stripTrailingZeros();
            dto.setCantidad(cantidad);
            dto.setPrecioUnitario(
                    BigDecimal.valueOf(detalle.getPrecioUnitario())
            );
            if(detalle.getProducto() != null &&
                    "KGR".equals(detalle.getProducto().getUnidadMedida())){
                dto.setSubtotalDetalle(null);
            } else {
                dto.setSubtotalDetalle(
                        detalle.getSubtotal() != null
                                ? BigDecimal.valueOf(detalle.getSubtotal())
                                : null
                );
            }

            resultado.add(dto);
        }

        // 2️⃣ Completar filas vacías
        while (resultado.size() < FILAS_FIJAS) {
            resultado.add(new DetalleBoletaDTO());
        }

        return resultado;
    }
}
