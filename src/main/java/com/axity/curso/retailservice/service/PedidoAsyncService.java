package com.axity.curso.retailservice.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PedidoAsyncService {

    private static final Logger log = LoggerFactory.getLogger(PedidoAsyncService.class);
    private static final int UMBRAL_REABASTECIMIENTO = 5;

    @Async("taskExecutor")
    public CompletableFuture<Void> enviarNotificacionConfirmacion(Long pedidoId) {
        log.info("[ASYNC] Hilo: {} | INICIO notificación pedido: {}",
                Thread.currentThread().getName(), pedidoId);
        try {
            Thread.sleep(2000); // TODO: remover en producción — solo demo async
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ASYNC] Hilo: {} | FIN notificación pedido: {} ✓",
                Thread.currentThread().getName(), pedidoId);
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> registrarAuditoriaMovimiento(Long pedidoId) {
        log.info("[ASYNC] Hilo: {} | INICIO auditoría pedido: {}",
                Thread.currentThread().getName(), pedidoId);
        try {
            Thread.sleep(3000); // TODO: remover en producción — solo demo async
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ASYNC] Hilo: {} | FIN auditoría pedido: {} ✓",
                Thread.currentThread().getName(), pedidoId);
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> verificarAlertaReabastecimiento(Long productoId, int stockActual) {
        log.info("[ASYNC] Hilo: {} | INICIO alerta reabastecimiento producto: {}",
                Thread.currentThread().getName(), productoId);
        try {
            Thread.sleep(1500); // TODO: remover en producción — solo demo async
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (stockActual <= UMBRAL_REABASTECIMIENTO) {
            log.warn("Reabastecimiento requerido para el producto {}. Stock actual: {}",
                    productoId, stockActual);
        }
        log.info("[ASYNC] Hilo: {} | FIN alerta reabastecimiento producto: {}",
                Thread.currentThread().getName(), productoId);
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> verificarAlertaReabastecimiento(
            Long productoId, String sku, int stockActual, int stockMinimo) {
        log.info("[ASYNC] Hilo: {} | INICIO alerta reabastecimiento producto: {} ({})",
                Thread.currentThread().getName(), productoId, sku);
        try {
            Thread.sleep(1500); // TODO: remover en producción — solo demo async
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (stockActual <= stockMinimo) {
            log.warn("Reabastecimiento requerido para el producto {} ({}). Stock actual: {}, mínimo: {}",
                    productoId, sku, stockActual, stockMinimo);
        }
        log.info("[ASYNC] Hilo: {} | FIN alerta reabastecimiento producto: {} ({})",
                Thread.currentThread().getName(), productoId, sku);
        return CompletableFuture.completedFuture(null);
    }
}
