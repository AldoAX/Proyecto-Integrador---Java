package com.axity.curso.retailservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetallePedidoRequestDTO(
        @NotNull @Positive Long productoId,
        @NotNull @Positive Integer cantidad) {
}
