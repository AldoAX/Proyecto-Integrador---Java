package com.axity.curso.retailservice.dto;

import java.math.BigDecimal;

public record DetallePedidoResponseDTO(
        Long id,
        Long productoId,
        String sku,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal) {
}
