package com.axity.curso.retailservice.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(
        @NotBlank String cliente,
        @NotEmpty List<@NotNull @Valid DetallePedidoRequestDTO> detalles) {
}
