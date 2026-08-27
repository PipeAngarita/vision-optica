-- ════════════════════════════════════════════════════════════════
-- MagmaSoft - VISION | Script de base de datos
-- Modulo: Pedidos (evidencia GA7-220501096-AA2-EV01)
--
-- Estructura basada en los campos ya definidos en el prototipo
-- front-end pedidos.html (evidencia GA6-220501096-AA4-EV03) y en el
-- modal "Registrar nuevo pedido": nombre y telefono del cliente,
-- tipo de lente, medico tratante, fecha estimada y estado.
-- ════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS magmasoft_vision
    CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;

USE magmasoft_vision;

CREATE TABLE IF NOT EXISTS pedido (
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    cliente_nombre      VARCHAR(100)    NOT NULL,
    cliente_telefono    VARCHAR(20)     NOT NULL,
    cliente_correo      VARCHAR(100),
    tipo_lente          ENUM('MONOFOCAL','BIFOCAL','PROGRESIVO') NOT NULL DEFAULT 'MONOFOCAL',
    medico_tratante     VARCHAR(100),
    fecha_estimada      DATE,
    estado              ENUM('EN_PROCESO','LISTO','ENTREGADO') NOT NULL DEFAULT 'EN_PROCESO',
    fecha_registro      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Datos de ejemplo (los mismos del prototipo front-end, para pruebas)
INSERT INTO pedido (cliente_nombre, cliente_telefono, tipo_lente, medico_tratante, fecha_estimada, estado) VALUES
    ('Ana Garcia',   '310-555-0101', 'BIFOCAL',    'Dra. Martinez', '2026-09-15', 'EN_PROCESO'),
    ('Luis Torres',  '321-555-0202', 'MONOFOCAL',  'Dr. Perez',     '2026-09-12', 'LISTO'),
    ('Maria Lopez',  '300-555-0303', 'PROGRESIVO', 'Dra. Ramirez',  '2026-09-18', 'EN_PROCESO'),
    ('Carlos Ruiz',  '315-555-0404', 'BIFOCAL',    'Dr. Suarez',    '2026-09-10', 'ENTREGADO'),
    ('Sofia Mora',   '312-555-0505', 'MONOFOCAL',  'Dra. Gomez',    '2026-09-20', 'LISTO');
