package com.AndresSanchezDev.SISTEMASPURI.controller;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.service.BoletaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
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
            long inicio = System.currentTimeMillis();
            byte[] pdfBytes = service.generarBoleta(boletaId);
            log.info("Boleta {} generada en {}ms", boletaId, System.currentTimeMillis() - inicio);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            headers.setContentDisposition(
                    ContentDisposition.inline()
                            .filename("boleta_" + boletaId + ".pdf")
                            .build()
            );
            headers.setCacheControl(CacheControl.noCache());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("❌ Error generando boleta {}: {}", boletaId, e.getMessage(), e);
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