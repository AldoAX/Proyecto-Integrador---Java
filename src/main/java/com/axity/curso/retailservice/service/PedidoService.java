package com.axity.curso.retailservice.service;

import com.axity.curso.retailservice.dto.PedidoRequestDTO;
import com.axity.curso.retailservice.dto.PedidoResponseDTO;

public interface PedidoService {

    PedidoResponseDTO crearPedido(PedidoRequestDTO dto);

    PedidoResponseDTO obtenerPedidoPorId(Long id);

    PedidoResponseDTO cambiarEstadoPedido(Long id, String nuevoEstado);

    void cancelarPedido(Long id);
}
