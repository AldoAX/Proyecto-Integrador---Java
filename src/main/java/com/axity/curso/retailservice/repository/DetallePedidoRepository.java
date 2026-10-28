package com.axity.curso.retailservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axity.curso.retailservice.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
