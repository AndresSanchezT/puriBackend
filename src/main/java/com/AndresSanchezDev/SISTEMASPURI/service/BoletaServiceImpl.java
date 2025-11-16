package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.Boleta;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.ActualizarEstadoBoletaDTO;
import com.AndresSanchezDev.SISTEMASPURI.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletaServiceImpl implements BoletaService {
    @Autowired
    private BoletaRepository repository;

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


}
