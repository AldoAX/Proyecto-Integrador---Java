# RetailOrder Service — Colección Postman

## Importar la colección
1. Abrir Postman
2. File > Import
3. Seleccionar el archivo `RetailOrder_Collection.json`
4. La colección aparece lista en el panel izquierdo

## Variable de entorno
La colección usa una variable `baseUrl` configurada por defecto en:
  http://localhost:8080

Para cambiarla: clic en la colección > Variables > editar baseUrl

## Orden sugerido para la demo

### Paso 1 — Crear datos base
1. Crear Producto (POST /api/v1/productos)
2. Verificar producto creado (GET /api/v1/productos/1)

### Paso 2 — Flujo sincrónico de pedido
3. Crear Pedido con stock suficiente (POST /api/v1/pedidos)
4. Consultar pedido creado (GET /api/v1/pedidos/1)
5. Agregar ítem al pedido (POST /api/v1/order-items)

### Paso 3 — Demostración de errores
6. ⚠ Crear producto con precio negativo → esperar 400
7. ⚠ Buscar producto inexistente → esperar 404
8. ⚠ Crear pedido con stock insuficiente → esperar 409

### Paso 4 — Demostración async (tener consola visible)
9. GET /api/v1/pedidos/test-async
   → La respuesta llega inmediata
   → En consola aparecen los hilos retail-async-* segundos después

### Paso 5 — Ciclo de vida completo
10. Cambiar estado pedido a ENVIADO
11. Soft Delete de producto → verificar que no aparece en listado
