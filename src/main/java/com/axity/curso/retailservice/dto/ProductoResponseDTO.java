package com.axity.curso.retailservice.dto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long id,
        String sku,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String categoria,
        boolean activo) {
}
