package com.axity.curso.retailservice.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axity.curso.retailservice.dto.PedidoRequestDTO;
import com.axity.curso.retailservice.service.PedidoAsyncService;
import com.axity.curso.retailservice.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Creación y seguimiento de pedidos de clientes")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;
    private final PedidoAsyncService pedidoAsyncService;

    public PedidoController(PedidoService pedidoService, PedidoAsyncService pedidoAsyncService) {
        this.pedidoService = pedidoService;
        this.pedidoAsyncService = pedidoAsyncService;
    }

    @GetMapping("/test-async")
    @Operation(
            summary = "Demo comportamiento async",
            description = "Retorna respuesta HTTP inmediata mientras las tareas async "
                    + "corren en background. Observa los logs para ver el cambio de hilo.")
    public ResponseEntity<Map<String, String>> testAsync() {
        String hiloActual = Thread.currentThread().getName();
        log.info("[SYNC] Hilo: {} | Test async iniciado", hiloActual);

        pedidoAsyncService.enviarNotificacionConfirmacion(0L);
        pedidoAsyncService.registrarAuditoriaMovimiento(0L);
        pedidoAsyncService.verificarAlertaReabastecimiento(0L, "SKU-TEST", 3, 5);

        log.info("[SYNC] Hilo: {} | Respuesta enviada — async corriendo en background", hiloActual);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("mensaje", "Respuesta sincrónica inmediata");
        response.put("hilo_principal", hiloActual);
        response.put("instruccion",
                "Revisa la consola — los hilos retail-async-* aparecen DESPUÉS de esta respuesta");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Valida y descuenta stock de forma transaccional; notificación y auditoría se ejecutan en background.")
    @ApiResponse(responseCode = "201", description = "Pedido creado; tareas asíncronas iniciadas")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido", description = "Consulta el encabezado, sus detalles y el estado actual.")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<?> obtenerPedido(
            @Parameter(description = "Identificador del pedido") @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del pedido", description = "Actualiza el estado a EN_PROCESO, ENVIADO o CANCELADO.")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "400", description = "Estado inválido")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<?> cambiarEstado(
            @Parameter(description = "Identificador del pedido") @PathVariable Long id,
            @Parameter(description = "Nuevo estado: EN_PROCESO, ENVIADO o CANCELADO")
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(pedidoService.cambiarEstadoPedido(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela el pedido y restituye el stock de sus productos.")
    @ApiResponse(responseCode = "204", description = "Pedido cancelado y stock restituido")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    public ResponseEntity<?> cancelarPedido(
            @Parameter(description = "Identificador del pedido") @PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
