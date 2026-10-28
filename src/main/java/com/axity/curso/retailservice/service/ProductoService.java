package com.axity.curso.retailservice.service;

import java.util.List;

import com.axity.curso.retailservice.dto.ProductoRequestDTO;
import com.axity.curso.retailservice.dto.ProductoResponseDTO;

public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO dto);

    ProductoResponseDTO obtenerProductoPorId(Long id);

    List<ProductoResponseDTO> listarProductos(String categoria, Double precioMax);

    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto);

    void eliminarProducto(Long id);
}
