-- ════════════════════════════════════════════════════════════════
-- MagmaSoft-Vision | Extensión de BD para el módulo REST API
-- Evidencia GA7-220501096-AA5-EV03
-- Juan Felipe Angarita Rodriguez | Mayo 2026
--
-- Ejecutar DESPUÉS del script ya existente:
--   backend-jdbc/pedidos/src/main/resources/magmasoft_vision_db.sql
--
-- La tabla "pedido" ya existe. Este script agrega las tablas
-- "notificacion" y "producto" que necesita la API REST.
-- ════════════════════════════════════════════════════════════════

USE magmasoft_vision;

-- ── Tabla: producto ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS producto (
    id            INT           PRIMARY KEY AUTO_INCREMENT,
    codigo_sku    VARCHAR(50)   NOT NULL UNIQUE,
    nombre        VARCHAR(100)  NOT NULL,
    descripcion   TEXT,
    categoria     ENUM('MONTURA','LENTE','ACCESORIO') NOT NULL DEFAULT 'MONTURA',
    marca         VARCHAR(50),
    precio_venta  DECIMAL(10,2) NOT NULL CHECK (precio_venta >= 0),
    stock         INT           NOT NULL DEFAULT 0 CHECK (stock >= 0),
    activo        BOOLEAN       NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ── Tabla: notificacion ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notificacion (
    id           INT         PRIMARY KEY AUTO_INCREMENT,
    canal        ENUM('EMAIL','SMS','PUSH') NOT NULL DEFAULT 'EMAIL',
    destinatario VARCHAR(150) NOT NULL,
    mensaje      TEXT        NOT NULL,
    estado       ENUM('PENDIENTE','ENVIADA','ERROR') NOT NULL DEFAULT 'ENVIADA',
    fecha_envio  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_pedido    INT,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id) ON DELETE SET NULL
);

-- ── Datos de prueba: productos ───────────────────────────────────
INSERT IGNORE INTO producto
    (codigo_sku, nombre, descripcion, categoria, marca, precio_venta, stock)
VALUES
    ('RB3025',  'Montura Ray-Ban Aviator',  'Clasica estilo aviador',       'MONTURA',   'Ray-Ban',  320000, 18),
    ('OX8118',  'Montura Oakley Crosslink', 'Deportiva alta gama',          'MONTURA',   'Oakley',   410000,  3),
    ('TR-GEN8', 'Lentes Transitions GEN8',  'Fotocromáticos 8a generacion', 'LENTE',     'Essilor',  185000, 42),
    ('ACV-30',  'Lentes Contacto Acuvue',   'Diarios alta comodidad',       'LENTE',     'J&J',       95000,  4),
    ('CA8055',  'Montura Carrera 8055',      'Diseño europeo contemporaneo', 'MONTURA',   'Carrera',  275000, 11),
    ('KIT-01',  'Kit Limpieza de Lentes',   'Spray + paño + estuche',       'ACCESORIO', 'Generico',  28000, 65);

-- ── Datos de prueba: notificaciones ─────────────────────────────
INSERT IGNORE INTO notificacion (canal, destinatario, mensaje, estado, id_pedido)
VALUES
    ('EMAIL', 'ana@email.com',    'Su pedido está EN_PROCESO. Le avisaremos cuando esté listo.', 'ENVIADA', 1),
    ('SMS',   '321-555-0202',     'Hola Luis, su pedido ya está LISTO. Puede pasar a recogerlo.','ENVIADA', 2),
    ('EMAIL', 'maria@email.com',  'Recordatorio: su pedido #3 está en preparacion.',              'ENVIADA', 3);

-- ── Vista útil: pedidos con conteo de notificaciones ────────────
CREATE OR REPLACE VIEW v_pedidos_resumen AS
SELECT
    p.id,
    p.cliente_nombre,
    p.cliente_telefono,
    p.tipo_lente,
    p.estado,
    p.fecha_estimada,
    p.fecha_registro,
    COUNT(n.id) AS total_notificaciones
FROM pedido p
LEFT JOIN notificacion n ON n.id_pedido = p.id
GROUP BY p.id;
