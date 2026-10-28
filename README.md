# RetailOrder Service

Microservicio REST para administrar un catálogo retail, consultar disponibilidad y gestionar pedidos. Está construido con Java 21 y Spring Boot, utiliza Spring Data JPA para persistencia, H2 como base de datos de desarrollo y SpringDoc para documentar la API.

## Funcionalidades

- Alta, consulta, filtrado, actualización y baja lógica de productos.
- Creación de pedidos con varios productos y cálculo del total.
- Validación y descuento transaccional del inventario al crear un pedido.
- Cancelación idempotente de pedidos y restitución del stock.
- Cambio de estado de pedidos: `EN_PROCESO`, `ENVIADO` y `CANCELADO`.
- Ejecución asíncrona de tareas de notificación, auditoría y revisión de reabastecimiento.
- Respuestas de error centralizadas para recursos no encontrados, precios inválidos, falta de stock y validaciones.
- Documentación interactiva de la API con Swagger UI.

> Las tareas asíncronas están preparadas como puntos de integración y actualmente registran su actividad en el log de la aplicación; no hay una integración externa de correo, auditoría o inventario.

## Tecnologías

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- H2
- SpringDoc OpenAPI 3.0.1
- Maven Wrapper

## Requisitos

- JDK 21
- No se requiere instalar Maven por separado: el proyecto incluye Maven Wrapper.

## Ejecución

Desde la raíz del proyecto, en Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación inicia por defecto en `http://localhost:8080`.

### Swagger UI y OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Especificación OpenAPI: `http://localhost:8080/v3/api-docs`

### Base de datos H2

La configuración local utiliza una base H2 en memoria llamada `retaildb`. Los datos se conservan mientras la aplicación está en ejecución y se pierden al detenerla.

- Consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:retaildb`
- Usuario: `sa`
- Contraseña: vacía

La configuración de base de datos, Hibernate y consola está en `src/main/resources/application.properties`.

### Carga automática de datos de ejemplo

Al iniciar la aplicación, Spring Boot ejecuta automáticamente [`src/main/resources/seed-h2.sql`](./src/main/resources/seed-h2.sql) después de que Hibernate crea o actualiza las tablas. El script carga 50 productos, 20 pedidos y 40 detalles, asociados a 10 nombres de cliente distintos. En el modelo actual el cliente se almacena como texto en cada pedido; no hay una tabla independiente de clientes.

La configuración se define mediante `spring.sql.init.mode=always`, `spring.sql.init.data-locations=classpath:seed-h2.sql` y `spring.jpa.defer-datasource-initialization=true`. Como H2 está configurada en memoria, al reiniciar la aplicación la base se crea de nuevo y los datos semilla se insertan otra vez. El script no es idempotente y no debe reutilizarse sin limpiar previamente una base persistente. Las inserciones SQL no disparan notificaciones ni otras operaciones de servicio.

## Arquitectura

El código está organizado bajo `com.axity.curso.retailservice`:

| Paquete | Responsabilidad |
|---|---|
| `model` | Entidades JPA `Product`, `Pedido`, `DetallePedido` y estado del pedido. |
| `dto` | Contratos separados de solicitud y respuesta para productos, pedidos y detalles. |
| `repository` | Repositorios Spring Data JPA. |
| `service` | Interfaces y lógica de negocio para productos y pedidos. |
| `controller` | Endpoints REST de productos y pedidos. |
| `exception` | Excepciones de dominio, `GlobalExceptionHandler` y formato de error. |
| `config` | Configuración de OpenAPI. |

`RetailserviceApplication` habilita el procesamiento asíncrono con `@EnableAsync`.

### Modelo de dominio

- **Product**: SKU único, nombre, descripción, precio, stock, categoría y bandera `activo`. El borrado lógico desactiva el producto; las consultas de catálogo solo devuelven productos activos.
- **Pedido**: cliente, fecha, estado y relación uno-a-muchos con sus detalles.
- **DetallePedido**: cantidad, precio unitario capturado al confirmar el pedido y referencias al pedido y al producto.

## API REST

Todos los endpoints usan el prefijo `/api/v1`.

### Productos

| Método | Ruta | Descripción | Respuestas principales |
|---|---|---|---|
| `POST` | `/productos` | Crea un producto. | `201`, `400` |
| `GET` | `/productos/{id}` | Consulta un producto activo. | `200`, `404` |
| `GET` | `/productos?categoria={categoria}&precioMax={precio}` | Lista productos activos y permite filtrar por categoría y precio máximo inclusivo. | `200`, `400` |
| `PUT` | `/productos/{id}` | Reemplaza los datos del producto activo. | `200`, `400`, `404` |
| `DELETE` | `/productos/{id}` | Desactiva el producto sin borrarlo físicamente. | `204`, `404` |

Ejemplo de creación:

```json
{
  "sku": "SKU-100",
  "nombre": "Café",
  "descripcion": "Café molido",
  "precio": 12.50,
  "stock": 40,
  "categoria": "abarrotes"
}
```

El precio debe ser mayor a cero; el stock puede ser cero o mayor. El SKU, nombre y categoría son obligatorios.

Ejemplos de filtros:

```text
GET /api/v1/productos?categoria=abarrotes
GET /api/v1/productos?precioMax=20
GET /api/v1/productos?categoria=abarrotes&precioMax=20
```

### Pedidos

| Método | Ruta | Descripción | Respuestas principales |
|---|---|---|---|
| `POST` | `/pedidos` | Crea un pedido, valida y descuenta el stock; inicia las tareas asíncronas. | `201`, `404`, `409`, `400` |
| `GET` | `/pedidos/{id}` | Consulta el pedido, sus detalles y el total. | `200`, `404` |
| `PATCH` | `/pedidos/{id}/estado?nuevoEstado={estado}` | Cambia el estado del pedido. | `200`, `400`, `404` |
| `DELETE` | `/pedidos/{id}` | Cancela el pedido y restituye el stock. | `204`, `404` |

Ejemplo de creación:

```json
{
  "cliente": "Cliente de ejemplo",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
}
```

El pedido requiere un cliente y al menos un detalle. Cada detalle debe tener un `productoId` y una cantidad positiva. El precio unitario se toma del producto al crear el pedido; el total se calcula sumando los subtotales.

Ejemplos de cambio de estado:

```text
PATCH /api/v1/pedidos/1/estado?nuevoEstado=ENVIADO
PATCH /api/v1/pedidos/1/estado?nuevoEstado=EN_PROCESO
```

Si se usa `nuevoEstado=CANCELADO`, también se realiza la restitución del inventario. Se recomienda usar `DELETE /api/v1/pedidos/{id}` para expresar la operación de cancelación. Cancelar un pedido ya cancelado no vuelve a sumar stock.

## Errores

Las respuestas de error siguen este formato:

```json
{
  "timestamp": "2026-10-28T21:00:00Z",
  "status": 409,
  "error": "StockInsuficienteException",
  "message": "Stock insuficiente para el producto SKU-100",
  "path": "/api/v1/pedidos"
}
```

| HTTP | Caso |
|---|---|
| `400 Bad Request` | Solicitud inválida, validación fallida o precio no positivo. |
| `404 Not Found` | Producto activo o pedido no encontrado. |
| `409 Conflict` | Stock insuficiente para completar el pedido. |

## Pruebas

Ejecuta las pruebas y verifica la compilación con:

```powershell
.\mvnw.cmd test
```

Las pruebas de integración cubren el descuento y la restitución del inventario, la falta de stock, el precio inválido y el borrado lógico de productos.
