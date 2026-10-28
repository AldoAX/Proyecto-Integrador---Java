package com.axity.curso.retailservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.axity.curso.retailservice.dto.DetallePedidoResponseDTO;
import com.axity.curso.retailservice.dto.PedidoRequestDTO;
import com.axity.curso.retailservice.dto.PedidoResponseDTO;
import com.axity.curso.retailservice.exception.RecursoNoEncontradoException;
import com.axity.curso.retailservice.exception.StockInsuficienteException;
import com.axity.curso.retailservice.model.DetallePedido;
import com.axity.curso.retailservice.model.EstadoPedido;
import com.axity.curso.retailservice.model.Pedido;
import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.repository.PedidoRepository;
import com.axity.curso.retailservice.repository.ProductoRepository;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoAsyncService pedidoAsyncService;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository,
            PedidoAsyncService pedidoAsyncService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.pedidoAsyncService = pedidoAsyncService;
    }

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {
        log.info("[SYNC] Hilo: {} | INICIO crearPedido | cliente: {}",
                Thread.currentThread().getName(), dto.cliente());
        Map<Long, Product> productos = new HashMap<>();
        Map<Long, Integer> cantidadesSolicitadas = new HashMap<>();

        for (Long productoId : new TreeSet<>(dto.detalles().stream()
                .map(detalle -> detalle.productoId()).toList())) {
            Product producto = productoRepository.buscarActivoPorIdParaActualizar(productoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró el producto con ID " + productoId));
            productos.put(productoId, producto);
        }
        for (var detalleDto : dto.detalles()) {
            cantidadesSolicitadas.merge(detalleDto.productoId(), detalleDto.cantidad(), Integer::sum);
        }

        cantidadesSolicitadas.forEach((productoId, cantidad) -> {
            Product producto = productos.get(productoId);
            log.info("[SYNC] Hilo: {} | Verificando stock productoId: {}",
                    Thread.currentThread().getName(), productoId);
            if (producto.getStock() < cantidad) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para el producto " + producto.getSku()
                                + ". Disponible: " + producto.getStock()
                                + ", solicitado: " + cantidad);
            }
        });

        Pedido pedido = new Pedido();
        pedido.setCliente(dto.cliente());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.EN_PROCESO);

        for (var detalleDto : dto.detalles()) {
            Product producto = productos.get(detalleDto.productoId());
            producto.setStock(producto.getStock() - detalleDto.cantidad());

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.cantidad());
            detalle.setPrecioUnitario(producto.getPrice());
            pedido.agregarDetalle(detalle);
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("[SYNC] Hilo: {} | Pedido GUARDADO id: {}",
                Thread.currentThread().getName(), pedidoGuardado.getId());
        log.info("[SYNC] Hilo: {} | Respuesta lista — delegando tareas async...",
                Thread.currentThread().getName());
        pedidoAsyncService.enviarNotificacionConfirmacion(pedidoGuardado.getId());
        pedidoAsyncService.registrarAuditoriaMovimiento(pedidoGuardado.getId());
        cantidadesSolicitadas.forEach((productoId, cantidad) -> {
            int stockRestante = productos.get(productoId).getStock();
            pedidoAsyncService.verificarAlertaReabastecimiento(productoId, stockRestante);
        });
        return convertir(pedidoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        return convertir(buscarPedido(id));
    }

    @Override
    public PedidoResponseDTO cambiarEstadoPedido(Long id, String nuevoEstado) {
        log.info("[SYNC] Hilo: {} | INICIO cambiarEstadoPedido | pedidoId: {} | nuevoEstado: {}",
                Thread.currentThread().getName(), id, nuevoEstado);
        EstadoPedido estado = EstadoPedido.valueOf(nuevoEstado.trim().toUpperCase());
        Pedido pedido = buscarPedido(id);
        if (estado == EstadoPedido.CANCELADO) {
            log.info("[SYNC] Hilo: {} | Estado CANCELADO solicitado | pedidoId: {}",
                    Thread.currentThread().getName(), id);
            cancelarPedido(id);
            return convertir(pedido);
        }
        if (pedido.getEstado() == EstadoPedido.CANCELADO && estado != EstadoPedido.CANCELADO) {
            throw new IllegalArgumentException("No se puede cambiar el estado de un pedido cancelado");
        }
        pedido.setEstado(estado);
        log.info("[SYNC] Hilo: {} | Actualizando estado | pedidoId: {} | estado: {}",
                Thread.currentThread().getName(), id, pedido.getEstado());
        return convertir(pedidoRepository.save(pedido));
    }

    @Override
    public void cancelarPedido(Long id) {
        log.info("[SYNC] Hilo: {} | INICIO cancelarPedido | pedidoId: {}",
                Thread.currentThread().getName(), id);
        Pedido pedido = buscarPedido(id);
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            log.info("[SYNC] Hilo: {} | Pedido ya cancelado | pedidoId: {}",
                    Thread.currentThread().getName(), id);
            return;
        }
        Map<Long, Integer> cantidadesPorProducto = new HashMap<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            cantidadesPorProducto.merge(
                    detalle.getProducto().getId(), detalle.getCantidad(), Integer::sum);
        }
        for (Long productoId : new TreeSet<>(cantidadesPorProducto.keySet())) {
            Product producto = productoRepository.buscarPorIdParaActualizar(productoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró el producto con ID " + productoId));
            producto.setStock(producto.getStock() + cantidadesPorProducto.get(productoId));
        }
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
        log.info("[SYNC] Hilo: {} | Pedido cancelado y stock restituido | pedidoId: {}",
                Thread.currentThread().getName(), id);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el pedido con ID " + id));
    }

    private PedidoResponseDTO convertir(Pedido pedido) {
        List<DetallePedidoResponseDTO> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : pedido.getDetalles()) {
            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
            detalles.add(new DetallePedidoResponseDTO(
                    detalle.getId(),
                    detalle.getProducto().getId(),
                    detalle.getProducto().getSku(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    subtotal));
        }
        return new PedidoResponseDTO(
                pedido.getId(), pedido.getCliente(), pedido.getFecha(),
                pedido.getEstado(), List.copyOf(detalles), total);
    }
}
