package com.axity.curso.retailservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axity.curso.retailservice.dto.ProductoRequestDTO;
import com.axity.curso.retailservice.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Administración del catálogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un producto activo en el catálogo.")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o precio no positivo")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crearProducto(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto", description = "Busca un producto activo por su identificador.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<?> obtenerProducto(
            @Parameter(description = "Identificador del producto") @PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Filtra el catálogo activo por categoría y precio máximo.")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @ApiResponse(responseCode = "400", description = "Precio máximo inválido")
    public ResponseEntity<?> listarProductos(
            @Parameter(description = "Categoría de producto")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio máximo inclusivo")
            @RequestParam(required = false) Double precioMax) {
        List<?> productos = productoService.listarProductos(categoria, precioMax);
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto activo.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o precio no positivo")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<?> actualizarProducto(
            @Parameter(description = "Identificador del producto") @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Desactiva el producto sin borrarlo físicamente.")
    @ApiResponse(responseCode = "204", description = "Producto desactivado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<?> eliminarProducto(
            @Parameter(description = "Identificador del producto") @PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
