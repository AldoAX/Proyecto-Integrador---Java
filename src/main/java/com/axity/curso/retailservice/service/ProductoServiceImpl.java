package com.axity.curso.retailservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.axity.curso.retailservice.dto.ProductoRequestDTO;
import com.axity.curso.retailservice.dto.ProductoResponseDTO;
import com.axity.curso.retailservice.exception.PrecioInvalidoException;
import com.axity.curso.retailservice.exception.RecursoNoEncontradoException;
import com.axity.curso.retailservice.model.Product;
import com.axity.curso.retailservice.repository.ProductoRepository;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        validarPrecio(dto.precio());
        if (productoRepository.existsBySkuIgnoreCase(dto.sku())) {
            throw new IllegalArgumentException("Ya existe un producto con SKU " + dto.sku());
        }
        Product producto = new Product(
                dto.sku(), dto.nombre(), dto.descripcion(), dto.precio(),
                dto.stock(), dto.categoria());
        return convertir(productoRepository.save(producto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        return convertir(buscarActivo(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos(String categoria, Double precioMax) {
        BigDecimal precio = precioMax == null ? null : BigDecimal.valueOf(precioMax);
        if (precio != null && precio.signum() < 0) {
            throw new PrecioInvalidoException("El precio máximo no puede ser negativo");
        }

        List<Product> productos;
        if (categoria != null && !categoria.isBlank() && precio != null) {
            productos = productoRepository
                    .findByCategoryIgnoreCaseAndActivoTrueAndPriceLessThanEqual(categoria, precio);
        } else if (categoria != null && !categoria.isBlank()) {
            productos = productoRepository.findByCategoryIgnoreCaseAndActivoTrue(categoria);
        } else if (precio != null) {
            productos = productoRepository.findByActivoTrueAndPriceLessThanEqual(precio);
        } else {
            productos = productoRepository.findByActivoTrue();
        }
        return productos.stream().map(this::convertir).toList();
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        validarPrecio(dto.precio());
        Product producto = buscarActivo(id);
        if (productoRepository.existsBySkuIgnoreCaseAndIdNot(dto.sku(), id)) {
            throw new IllegalArgumentException("Ya existe un producto con SKU " + dto.sku());
        }
        producto.setSku(dto.sku());
        producto.setName(dto.nombre());
        producto.setDescription(dto.descripcion());
        producto.setPrice(dto.precio());
        producto.setStock(dto.stock());
        producto.setCategory(dto.categoria());
        return convertir(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Long id) {
        Product producto = buscarActivo(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private Product buscarActivo(Long id) {
        return productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el producto con ID " + id));
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.signum() <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero");
        }
    }

    private ProductoResponseDTO convertir(Product producto) {
        return new ProductoResponseDTO(
                producto.getId(), producto.getSku(), producto.getName(),
                producto.getDescription(), producto.getPrice(), producto.getStock(),
                producto.getCategory(), producto.isActivo());
    }
}
