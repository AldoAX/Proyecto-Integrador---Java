package com.axity.curso.retailservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.axity.curso.retailservice.model.EstadoPedido;

public record PedidoResponseDTO(
        Long id,
        String cliente,
        LocalDateTime fecha,
        EstadoPedido estado,
        List<DetallePedidoResponseDTO> detalles,
        BigDecimal total) {
}
