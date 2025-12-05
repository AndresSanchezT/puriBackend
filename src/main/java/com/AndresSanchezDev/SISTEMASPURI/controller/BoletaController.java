package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.service.BoletaService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private final BoletaService service;

    public BoletaController(BoletaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Boleta> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Boleta> getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{boletaId}/pdf")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Long boletaId) {
        try {
            byte[] pdfBytes = service.generarBoleta(boletaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // ✅ Cambiar de attachment() a inline()
            headers.setContentDisposition(
                    ContentDisposition.inline()
                            .filename("boleta_" + boletaId + ".pdf")
                            .build()
            );

            // ✅ Agregar estos headers adicionales
            headers.add("Content-Type", "application/pdf");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping
    public Boleta create(@RequestBody Boleta boleta) {
        return service.save(boleta);
    }

    @PutMapping("/{id}")
    public Boleta update(@PathVariable Long id, @RequestBody Boleta boleta) {
        boleta.setId(id);
        return service.save(boleta);
    }

    @PutMapping("/{id}/estado")
    public String actualizarEstadoBoleta(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoBoletaDTO dto
    ) {
        return service.actualizarEstado(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}