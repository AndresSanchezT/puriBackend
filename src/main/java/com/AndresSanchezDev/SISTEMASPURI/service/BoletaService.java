package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.Cliente;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.BoletaDTO;
import net.sf.jasperreports.engine.JRException;

import java.util.List;
import java.util.Optional;

public interface BoletaService {
    List<Boleta> findAll();

    Optional<Boleta> findById(Long id);

    Boleta save(Boleta boleta);

    void deleteById(Long id);

    String actualizarEstado(Long id, ActualizarEstadoBoletaDTO dto);

    //    byte[] generarBoleta(Long boletaId) throws JRException;
    BoletaDTO obtenerDatosCompletosParaPDF(Long boletaId);

}
