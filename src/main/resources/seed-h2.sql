-- Cargar una sola vez en una base vacia desde la consola H2.
-- En este modelo, los clientes son nombres guardados en orders.cliente;
-- no existe una tabla independiente de clientes.

INSERT INTO products (sku, name, description, price, stock, category, activo) VALUES
    ('SKU-001', 'Cafe molido', 'Cafe molido de tueste medio, 500 g', 12.50, 38, 'abarrotes', TRUE),
    ('SKU-002', 'Te negro', 'Caja con 20 sobres', 5.00, 42, 'abarrotes', TRUE),
    ('SKU-003', 'Azucar', 'Azucar refinada, 1 kg', 3.25, 45, 'abarrotes', TRUE),
    ('SKU-004', 'Arroz', 'Arroz blanco, 1 kg', 2.80, 46, 'abarrotes', TRUE),
    ('SKU-005', 'Pasta', 'Pasta de trigo, 500 g', 2.40, 47, 'abarrotes', TRUE),
    ('SKU-006', 'Aceite vegetal', 'Botella de aceite vegetal, 1 l', 4.75, 43, 'abarrotes', TRUE),
    ('SKU-007', 'Frijol', 'Frijol negro, 1 kg', 3.90, 44, 'abarrotes', TRUE),
    ('SKU-008', 'Harina', 'Harina de trigo, 1 kg', 2.60, 45, 'abarrotes', TRUE),
    ('SKU-009', 'Avena', 'Avena integral, 500 g', 3.50, 43, 'abarrotes', TRUE),
    ('SKU-010', 'Cereal', 'Cereal de maiz, 400 g', 5.80, 42, 'abarrotes', TRUE),
    ('SKU-011', 'Leche entera', 'Leche entera, 1 l', 1.90, 48, 'lacteos', TRUE),
    ('SKU-012', 'Leche deslactosada', 'Leche deslactosada, 1 l', 2.20, 46, 'lacteos', TRUE),
    ('SKU-013', 'Yogur natural', 'Yogur natural, 1 kg', 4.60, 44, 'lacteos', TRUE),
    ('SKU-014', 'Queso manchego', 'Queso manchego, 400 g', 6.90, 42, 'lacteos', TRUE),
    ('SKU-015', 'Mantequilla', 'Mantequilla sin sal, 200 g', 3.80, 43, 'lacteos', TRUE),
    ('SKU-016', 'Pan blanco', 'Pan de caja, 680 g', 3.40, 45, 'panaderia', TRUE),
    ('SKU-017', 'Pan integral', 'Pan integral de caja, 680 g', 3.90, 43, 'panaderia', TRUE),
    ('SKU-018', 'Galletas de avena', 'Paquete de galletas, 250 g', 2.75, 44, 'panaderia', TRUE),
    ('SKU-019', 'Tortillas', 'Tortillas de maiz, 1 kg', 2.10, 48, 'panaderia', TRUE),
    ('SKU-020', 'Pastel de chocolate', 'Rebanada individual', 4.50, 40, 'panaderia', TRUE),
    ('SKU-021', 'Agua natural', 'Botella de agua, 1.5 l', 1.25, 48, 'bebidas', TRUE),
    ('SKU-022', 'Agua mineral', 'Botella de agua mineral, 1 l', 1.80, 46, 'bebidas', TRUE),
    ('SKU-023', 'Jugo de naranja', 'Jugo de naranja, 1 l', 3.60, 44, 'bebidas', TRUE),
    ('SKU-024', 'Refresco de cola', 'Refresco de cola, 2 l', 3.25, 45, 'bebidas', TRUE),
    ('SKU-025', 'Limonada', 'Limonada, 1 l', 2.90, 43, 'bebidas', TRUE),
    ('SKU-026', 'Manzana', 'Manzana roja por kg', 2.50, 48, 'frutas', TRUE),
    ('SKU-027', 'Platano', 'Platano por kg', 1.80, 47, 'frutas', TRUE),
    ('SKU-028', 'Naranja', 'Naranja por kg', 2.10, 46, 'frutas', TRUE),
    ('SKU-029', 'Fresas', 'Fresas, charola de 500 g', 4.80, 42, 'frutas', TRUE),
    ('SKU-030', 'Aguacate', 'Aguacate por kg', 5.50, 41, 'frutas', TRUE),
    ('SKU-031', 'Jabon para ropa', 'Detergente en polvo, 1 kg', 6.50, 43, 'limpieza', TRUE),
    ('SKU-032', 'Lavatrastes', 'Liquido lavatrastes, 750 ml', 3.20, 45, 'limpieza', TRUE),
    ('SKU-033', 'Limpiador multiusos', 'Limpiador multiusos, 1 l', 3.75, 44, 'limpieza', TRUE),
    ('SKU-034', 'Cloro', 'Blanqueador, 1 l', 2.40, 46, 'limpieza', TRUE),
    ('SKU-035', 'Esponjas', 'Paquete con 3 esponjas', 1.90, 47, 'limpieza', TRUE),
    ('SKU-036', 'Jabon de tocador', 'Paquete con 4 barras', 3.60, 44, 'cuidado-personal', TRUE),
    ('SKU-037', 'Shampoo', 'Shampoo familiar, 700 ml', 5.90, 42, 'cuidado-personal', TRUE),
    ('SKU-038', 'Pasta dental', 'Pasta dental, 150 ml', 2.80, 46, 'cuidado-personal', TRUE),
    ('SKU-039', 'Desodorante', 'Desodorante en aerosol, 150 ml', 4.75, 43, 'cuidado-personal', TRUE),
    ('SKU-040', 'Papel higienico', 'Paquete con 4 rollos', 3.50, 45, 'cuidado-personal', TRUE),
    ('SKU-041', 'Taza ceramica', 'Taza de ceramica, 350 ml', 8.75, 42, 'hogar', TRUE),
    ('SKU-042', 'Plato llano', 'Plato llano de ceramica', 6.25, 43, 'hogar', TRUE),
    ('SKU-043', 'Vaso de vidrio', 'Vaso de vidrio, 400 ml', 3.50, 46, 'hogar', TRUE),
    ('SKU-044', 'Contenedor plastico', 'Contenedor con tapa, 1 l', 4.20, 44, 'hogar', TRUE),
    ('SKU-045', 'Toalla de cocina', 'Toalla de algodon', 5.50, 42, 'hogar', TRUE),
    ('SKU-046', 'Alimento para perro', 'Alimento seco para perro, 2 kg', 12.90, 40, 'mascotas', TRUE),
    ('SKU-047', 'Alimento para gato', 'Alimento seco para gato, 1.5 kg', 10.50, 41, 'mascotas', TRUE),
    ('SKU-048', 'Arena para gato', 'Arena aglomerante, 4 kg', 7.80, 42, 'mascotas', TRUE),
    ('SKU-049', 'Correa para perro', 'Correa ajustable para perro', 9.50, 40, 'mascotas', TRUE),
    ('SKU-050', 'Juguete para gato', 'Juguete con plumas', 3.90, 44, 'mascotas', TRUE);

INSERT INTO orders (cliente, fecha, estado) VALUES
    ('Cliente Demo 01', TIMESTAMP '2026-10-01 09:00:00', 'ENVIADO'),
    ('Cliente Demo 02', TIMESTAMP '2026-10-01 10:00:00', 'ENVIADO'),
    ('Cliente Demo 03', TIMESTAMP '2026-10-02 09:00:00', 'ENVIADO'),
    ('Cliente Demo 04', TIMESTAMP '2026-10-02 10:00:00', 'ENVIADO'),
    ('Cliente Demo 05', TIMESTAMP '2026-10-03 09:00:00', 'ENVIADO'),
    ('Cliente Demo 06', TIMESTAMP '2026-10-03 10:00:00', 'ENVIADO'),
    ('Cliente Demo 07', TIMESTAMP '2026-10-04 09:00:00', 'ENVIADO'),
    ('Cliente Demo 08', TIMESTAMP '2026-10-04 10:00:00', 'ENVIADO'),
    ('Cliente Demo 09', TIMESTAMP '2026-10-05 09:00:00', 'ENVIADO'),
    ('Cliente Demo 10', TIMESTAMP '2026-10-05 10:00:00', 'ENVIADO'),
    ('Cliente Demo 01', TIMESTAMP '2026-10-06 09:00:00', 'EN_PROCESO'),
    ('Cliente Demo 02', TIMESTAMP '2026-10-06 10:00:00', 'EN_PROCESO'),
    ('Cliente Demo 03', TIMESTAMP '2026-10-07 09:00:00', 'EN_PROCESO'),
    ('Cliente Demo 04', TIMESTAMP '2026-10-07 10:00:00', 'EN_PROCESO'),
    ('Cliente Demo 05', TIMESTAMP '2026-10-08 09:00:00', 'EN_PROCESO'),
    ('Cliente Demo 06', TIMESTAMP '2026-10-08 10:00:00', 'EN_PROCESO'),
    ('Cliente Demo 07', TIMESTAMP '2026-10-09 09:00:00', 'EN_PROCESO'),
    ('Cliente Demo 08', TIMESTAMP '2026-10-09 10:00:00', 'EN_PROCESO'),
    ('Cliente Demo 09', TIMESTAMP '2026-10-10 09:00:00', 'EN_PROCESO'),
    ('Cliente Demo 10', TIMESTAMP '2026-10-10 10:00:00', 'EN_PROCESO');

INSERT INTO order_items (cantidad, precio_unitario, pedido_id, producto_id)
SELECT v.cantidad, p.price, o.id, p.id
FROM (
    VALUES
        ('Cliente Demo 01', TIMESTAMP '2026-10-01 09:00:00', 'SKU-001', 2),
        ('Cliente Demo 01', TIMESTAMP '2026-10-01 09:00:00', 'SKU-002', 1),
        ('Cliente Demo 02', TIMESTAMP '2026-10-01 10:00:00', 'SKU-003', 2),
        ('Cliente Demo 02', TIMESTAMP '2026-10-01 10:00:00', 'SKU-004', 1),
        ('Cliente Demo 03', TIMESTAMP '2026-10-02 09:00:00', 'SKU-005', 2),
        ('Cliente Demo 03', TIMESTAMP '2026-10-02 09:00:00', 'SKU-006', 1),
        ('Cliente Demo 04', TIMESTAMP '2026-10-02 10:00:00', 'SKU-007', 2),
        ('Cliente Demo 04', TIMESTAMP '2026-10-02 10:00:00', 'SKU-008', 1),
        ('Cliente Demo 05', TIMESTAMP '2026-10-03 09:00:00', 'SKU-009', 2),
        ('Cliente Demo 05', TIMESTAMP '2026-10-03 09:00:00', 'SKU-010', 1),
        ('Cliente Demo 06', TIMESTAMP '2026-10-03 10:00:00', 'SKU-011', 2),
        ('Cliente Demo 06', TIMESTAMP '2026-10-03 10:00:00', 'SKU-012', 1),
        ('Cliente Demo 07', TIMESTAMP '2026-10-04 09:00:00', 'SKU-013', 2),
        ('Cliente Demo 07', TIMESTAMP '2026-10-04 09:00:00', 'SKU-014', 1),
        ('Cliente Demo 08', TIMESTAMP '2026-10-04 10:00:00', 'SKU-015', 2),
        ('Cliente Demo 08', TIMESTAMP '2026-10-04 10:00:00', 'SKU-016', 1),
        ('Cliente Demo 09', TIMESTAMP '2026-10-05 09:00:00', 'SKU-017', 2),
        ('Cliente Demo 09', TIMESTAMP '2026-10-05 09:00:00', 'SKU-018', 1),
        ('Cliente Demo 10', TIMESTAMP '2026-10-05 10:00:00', 'SKU-019', 2),
        ('Cliente Demo 10', TIMESTAMP '2026-10-05 10:00:00', 'SKU-020', 1),
        ('Cliente Demo 01', TIMESTAMP '2026-10-06 09:00:00', 'SKU-021', 2),
        ('Cliente Demo 01', TIMESTAMP '2026-10-06 09:00:00', 'SKU-022', 1),
        ('Cliente Demo 02', TIMESTAMP '2026-10-06 10:00:00', 'SKU-023', 2),
        ('Cliente Demo 02', TIMESTAMP '2026-10-06 10:00:00', 'SKU-024', 1),
        ('Cliente Demo 03', TIMESTAMP '2026-10-07 09:00:00', 'SKU-025', 2),
        ('Cliente Demo 03', TIMESTAMP '2026-10-07 09:00:00', 'SKU-026', 1),
        ('Cliente Demo 04', TIMESTAMP '2026-10-07 10:00:00', 'SKU-027', 2),
        ('Cliente Demo 04', TIMESTAMP '2026-10-07 10:00:00', 'SKU-028', 1),
        ('Cliente Demo 05', TIMESTAMP '2026-10-08 09:00:00', 'SKU-029', 2),
        ('Cliente Demo 05', TIMESTAMP '2026-10-08 09:00:00', 'SKU-030', 1),
        ('Cliente Demo 06', TIMESTAMP '2026-10-08 10:00:00', 'SKU-031', 2),
        ('Cliente Demo 06', TIMESTAMP '2026-10-08 10:00:00', 'SKU-032', 1),
        ('Cliente Demo 07', TIMESTAMP '2026-10-09 09:00:00', 'SKU-033', 2),
        ('Cliente Demo 07', TIMESTAMP '2026-10-09 09:00:00', 'SKU-034', 1),
        ('Cliente Demo 08', TIMESTAMP '2026-10-09 10:00:00', 'SKU-035', 2),
        ('Cliente Demo 08', TIMESTAMP '2026-10-09 10:00:00', 'SKU-036', 1),
        ('Cliente Demo 09', TIMESTAMP '2026-10-10 09:00:00', 'SKU-037', 2),
        ('Cliente Demo 09', TIMESTAMP '2026-10-10 09:00:00', 'SKU-038', 1),
        ('Cliente Demo 10', TIMESTAMP '2026-10-10 10:00:00', 'SKU-039', 2),
        ('Cliente Demo 10', TIMESTAMP '2026-10-10 10:00:00', 'SKU-040', 1)
) AS v(cliente, fecha, sku, cantidad)
JOIN orders o ON o.cliente = v.cliente AND o.fecha = v.fecha
JOIN products p ON p.sku = v.sku;

-- Descuenta del stock inicial las unidades asociadas a los pedidos semilla.
UPDATE products p
SET stock = p.stock - (
    SELECT COALESCE(SUM(i.cantidad), 0)
    FROM order_items i
    JOIN orders o ON o.id = i.pedido_id
    WHERE i.producto_id = p.id
      AND o.fecha >= TIMESTAMP '2026-10-01 00:00:00'
      AND o.fecha < TIMESTAMP '2026-10-11 00:00:00'
)
WHERE EXISTS (
    SELECT 1
    FROM order_items i
    JOIN orders o ON o.id = i.pedido_id
    WHERE i.producto_id = p.id
      AND o.fecha >= TIMESTAMP '2026-10-01 00:00:00'
      AND o.fecha < TIMESTAMP '2026-10-11 00:00:00'
);
