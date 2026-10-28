package com.axity.curso.retailservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.axity.curso.retailservice.dto.DetallePedidoRequestDTO;
import com.axity.curso.retailservice.dto.PedidoRequestDTO;
import com.axity.curso.retailservice.dto.ProductoRequestDTO;
import com.axity.curso.retailservice.exception.PrecioInvalidoException;
import com.axity.curso.retailservice.exception.StockInsuficienteException;
import com.axity.curso.retailservice.repository.PedidoRepository;
import com.axity.curso.retailservice.repository.ProductoRepository;
import com.axity.curso.retailservice.service.PedidoService;
import com.axity.curso.retailservice.service.ProductoService;

@SpringBootTest
class RetailOrderServiceTests {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private javax.sql.DataSource dataSource;

    @BeforeEach
    void limpiarDatos() {
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
    }

    @Test
    void crearPedidoDescuentaStockYCancelarLoRestituye() {
        var producto = productoService.crearProducto(
                new ProductoRequestDTO("SKU-100", "Café", null,
                        new BigDecimal("12.50"), 8, "abarrotes"));

        var pedido = pedidoService.crearPedido(new PedidoRequestDTO("Cliente 1",
                List.of(new DetallePedidoRequestDTO(producto.id(), 3))));

        assertEquals(new BigDecimal("37.50"), pedido.total());
        assertEquals(5, productoRepository.findById(producto.id()).orElseThrow().getStock());

        pedidoService.cancelarPedido(pedido.id());
        pedidoService.cancelarPedido(pedido.id());

        assertEquals(8, productoRepository.findById(producto.id()).orElseThrow().getStock());
        assertEquals("CANCELADO", pedidoService.obtenerPedidoPorId(pedido.id()).estado().name());
    }

    @Test
    void pedidoSinStockSuficienteNoModificaInventario() {
        var producto = productoService.crearProducto(
                new ProductoRequestDTO("SKU-200", "Té", null,
                        new BigDecimal("5.00"), 2, "abarrotes"));

        assertThrows(StockInsuficienteException.class, () -> pedidoService.crearPedido(
                new PedidoRequestDTO("Cliente 2",
                        List.of(new DetallePedidoRequestDTO(producto.id(), 3)))));

        assertEquals(2, productoRepository.findById(producto.id()).orElseThrow().getStock());
        assertTrue(pedidoRepository.findAll().isEmpty());
    }

    @Test
    void productoConPrecioNoPositivoSeRechazaYSoftDeleteLoOculta() {
        assertThrows(PrecioInvalidoException.class, () -> productoService.crearProducto(
                new ProductoRequestDTO("SKU-300", "Azúcar", null,
                        BigDecimal.ZERO, 4, "abarrotes")));

        var producto = productoService.crearProducto(
                new ProductoRequestDTO("SKU-301", "Azúcar", null,
                        new BigDecimal("3.00"), 4, "abarrotes"));
        productoService.eliminarProducto(producto.id());

        assertTrue(productoService.listarProductos(null, null).isEmpty());
    }

    @Test
    void scriptSemillaCargaProductosPedidosClientesYDetalles() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("seed-h2.sql"));
        }

        assertEquals(50, productoRepository.count());
        assertEquals(20, pedidoRepository.count());
        assertEquals(40, countRows("order_items"));
        assertEquals(10, countDistinct("orders", "cliente"));
        assertEquals(36, productoRepository.findById(
                productoRepository.findAll().stream()
                        .filter(producto -> producto.getSku().equals("SKU-001"))
                        .findFirst().orElseThrow().getId())
                .orElseThrow().getStock());
    }

    private long countRows(String table) throws Exception {
        try (Connection connection = dataSource.getConnection();
                var statement = connection.createStatement();
                var result = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
            result.next();
            return result.getLong(1);
        }
    }

    private long countDistinct(String table, String column) throws Exception {
        try (Connection connection = dataSource.getConnection();
                var statement = connection.createStatement();
                var result = statement.executeQuery(
                        "SELECT COUNT(DISTINCT " + column + ") FROM " + table)) {
            result.next();
            return result.getLong(1);
        }
    }
}
