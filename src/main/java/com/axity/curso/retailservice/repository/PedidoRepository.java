package com.axity.curso.retailservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axity.curso.retailservice.model.EstadoPedido;
import com.axity.curso.retailservice.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);
}
