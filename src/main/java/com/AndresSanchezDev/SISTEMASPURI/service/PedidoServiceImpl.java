package com.AndresSanchezDev.SISTEMASPURI.service;

import com.AndresSanchezDev.SISTEMASPURI.entity.*;
import com.AndresSanchezDev.SISTEMASPURI.repository.ClienteRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.PedidoRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.UsuarioRepository;
import com.AndresSanchezDev.SISTEMASPURI.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> pedidosHoy(){
        return pedidoRepository.pedidosHoy();
    }

    @Override
    public long countPedidosTotales() {
        return pedidoRepository.count();
    }

    /**
     * Registra un nuevo pedido creando automáticamente una visita
     * y asociando cliente, vendedor y la visita creada.
     */
    @Transactional
    public Pedido registrarPedidoConVisitaYDetalles(Long idCliente, Long idVendedor, Pedido pedidoData) {
        // Buscar cliente y vendedor
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Usuario vendedor = usuarioRepository.findById(idVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        // Crear la visita
        Visita visita = new Visita();
        visita.setCliente(cliente);
        visita.setVendedor(vendedor);
        visita.setFecha(LocalDate.now());
        visita.setEstado("Pendiente");
        visita.setObservaciones("Sin observaciones");
        visitaRepository.save(visita);

        // Asignar cliente, vendedor y visita al pedido
        pedidoData.setCliente(cliente);
        pedidoData.setVendedor(vendedor);
        pedidoData.setVisita(visita);
        pedidoData.setFechaPedido(LocalDate.now());
        pedidoData.setEstado("registrado");

        // Calcular totales
        double subtotal = pedidoData.getDetallePedidos().stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        pedidoData.setSubtotal(subtotal);
        pedidoData.setIgv(igv);
        pedidoData.setTotal(total);

        // Enlazar cada detalle con el pedido antes de guardar
        pedidoData.getDetallePedidos().forEach(detalle -> detalle.setPedido(pedidoData));

        // Guardar todo en cascada
        Pedido pedidoGuardado = pedidoRepository.save(pedidoData);

        return pedidoGuardado;
    }
}
