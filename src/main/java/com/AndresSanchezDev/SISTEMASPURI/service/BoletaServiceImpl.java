package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.DetalleBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DetallePedido;
import com.AndresSanchezDev.SISTEMASPURI.entity.Pedido;
import com.AndresSanchezDev.SISTEMASPURI.repository.BoletaRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.PedidoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BoletaServiceImpl implements BoletaService {
    @Autowired
    private BoletaRepository repository;
    @Autowired
    private PedidoRepository pedidoRepository;

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

    @Override
    public byte[] generarBoleta(Long boletaId) throws JRException {
        Boleta boleta = repository.findById(boletaId).orElseThrow();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("codigo", boleta.getCodigo());
        parametros.put("nombreNegocio", boleta.getPedido().getCliente().getNombreNegocio());
        parametros.put("nombreContacto", boleta.getPedido().getCliente().getNombreContacto());
        parametros.put("direccion", boleta.getPedido().getCliente().getDireccion());
        parametros.put("telefono", boleta.getPedido().getCliente().getTelefono());
        parametros.put("subtotal", BigDecimal.valueOf(boleta.getSubtotal()));
        parametros.put("igv", BigDecimal.valueOf(boleta.getIgv()));
        parametros.put("total", BigDecimal.valueOf(boleta.getTotal()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        parametros.put("fechaEmision", boleta.getFechaEmision().format(formatter));

        //  Cargar logo correctamente como InputStream
        InputStream logoStream = getClass().getResourceAsStream("/reportes/puri_logo.png");
        if (logoStream == null) {
            System.out.println("⚠️ Logo no encontrado en /reportes/puri_logo.png");
        }
        parametros.put("logo", logoStream);

        // 3. Crear lista de detalles
        List<DetalleBoletaDTO> detalles = new ArrayList<>();
        for (DetallePedido detalle : boleta.getPedido().getDetallePedidos()) {
            DetalleBoletaDTO dto = new DetalleBoletaDTO();
            dto.setCodigoProducto(detalle.getProducto().getCodigo());
            dto.setNombreProducto(detalle.getProducto().getNombre());
            dto.setUnidadMedida(detalle.getProducto().getUnidadMedida());
            dto.setCantidad(detalle.getCantidad());
            dto.setPrecioUnitario(BigDecimal.valueOf(detalle.getPrecioUnitario()));
            dto.setSubtotalDetalle(BigDecimal.valueOf(detalle.getSubtotal()));
            detalles.add(dto);
        }

        // 4. Cargar jrxml
        InputStream reportStream = getClass().getResourceAsStream("/reportes/puri_boleta.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // 6. Data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);

        // 7. Llenar reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parametros,
                dataSource
        );

        // 8. Exportar
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


}
