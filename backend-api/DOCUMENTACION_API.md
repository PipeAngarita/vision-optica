# MagmaSoft-Vision – Documentación REST API
**Evidencia:** GA7-220501096-AA5-EV03
**Aprendiz:** Juan Felipe Angarita Rodriguez
**Fecha:** Mayo 2026
**Repositorio:** https://github.com/PipeAngarita/vision-optica
**URL base (local):** `http://localhost:8080/vision-api/api`

---

## Información general

| Campo | Detalle |
|-------|---------|
| Protocolo | HTTP/1.1 |
| Formato | JSON (`Content-Type: application/json`) |
| Codificación | UTF-8 |
| Autenticación | Ninguna (alcance formativo) |
| CORS | Habilitado para todos los orígenes (`*`) |

### Estructura de respuesta estándar

Todos los endpoints devuelven el mismo formato:

```json
{
  "ok":      true,
  "mensaje": "Descripción del resultado",
  "datos":   { } o [ ] o null
}
```

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ok` | boolean | `true` si la operación fue exitosa |
| `mensaje` | string | Descripción legible del resultado |
| `datos` | object / array / null | Payload de la respuesta |

### Códigos de estado HTTP

| Código | Significado |
|--------|-------------|
| 200 OK | Consulta o actualización exitosa |
| 201 Created | Recurso creado correctamente |
| 400 Bad Request | Datos enviados inválidos o incompletos |
| 404 Not Found | Recurso no encontrado |
| 500 Internal Server Error | Error del servidor |

---

## 1. Health Check

### `GET /api/health`

Verifica que la API está funcionando y muestra todos los endpoints disponibles.

**Parámetros:** Ninguno

**Ejemplo de respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "API funcionando correctamente.",
  "datos": {
    "api": "MagmaSoft-Vision REST API",
    "version": "1.0.0",
    "evidencia": "GA7-220501096-AA5-EV03",
    "autor": "Juan Felipe Angarita Rodriguez",
    "timestamp": "2026-05-10T09:00:00",
    "baseDatos": "CONECTADA",
    "endpoints": { "...": "..." }
  }
}
```

---

## 2. API de Pedidos

Gestiona los pedidos de lentes del sistema. Usa la tabla `pedido` ya definida
en el módulo `backend-jdbc/pedidos` (evidencia GA7-AA2-EV01).

### Modelo Pedido

```json
{
  "id":              1,
  "clienteNombre":   "Ana Garcia",
  "clienteTelefono": "310-555-0101",
  "clienteCorreo":   "ana@email.com",
  "tipoLente":       "BIFOCAL",
  "medicoTratante":  "Dra. Martinez",
  "fechaEstimada":   "2026-09-15",
  "estado":          "EN_PROCESO",
  "fechaRegistro":   "2026-05-10T08:30:00",
  "fechaActualizacion": "2026-05-10T10:45:00"
}
```

| Campo | Tipo | Valores / Notas |
|-------|------|-----------------|
| `id` | int | Auto-generado |
| `clienteNombre` | string | **Obligatorio** |
| `clienteTelefono` | string | **Obligatorio** |
| `clienteCorreo` | string | Opcional |
| `tipoLente` | string | `MONOFOCAL` \| `BIFOCAL` \| `PROGRESIVO` |
| `medicoTratante` | string | Opcional |
| `fechaEstimada` | date | Formato `YYYY-MM-DD` |
| `estado` | string | `EN_PROCESO` \| `LISTO` \| `ENTREGADO` |

---

### `GET /api/pedidos`

Lista todos los pedidos, ordenados por fecha de registro descendente.

**Query params opcionales:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `estado` | string | Filtrar por estado: `EN_PROCESO`, `LISTO`, `ENTREGADO` |

**Ejemplos:**
```
GET /api/pedidos
GET /api/pedidos?estado=EN_PROCESO
GET /api/pedidos?estado=LISTO
```

**Respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "Pedidos obtenidos correctamente.",
  "datos": {
    "total": 5,
    "pedidos": [
      {
        "id": 1,
        "clienteNombre": "Ana Garcia",
        "clienteTelefono": "310-555-0101",
        "tipoLente": "BIFOCAL",
        "estado": "EN_PROCESO"
      }
    ]
  }
}
```

---

### `GET /api/pedidos/{id}`

Obtiene un pedido específico por su ID.

**Path param:** `id` (int) – ID del pedido

**Ejemplo:**
```
GET /api/pedidos/1
```

**Respuesta exitosa (200):**
```json
{
  "ok": true,
  "mensaje": "Pedido encontrado.",
  "datos": { "id": 1, "clienteNombre": "Ana Garcia", "..." }
}
```

**Pedido no encontrado (404):**
```json
{
  "ok": false,
  "mensaje": "Pedido con id=99 no encontrado.",
  "datos": null
}
```

---

### `POST /api/pedidos`

Crea un nuevo pedido en el sistema.

**Body (JSON):**
```json
{
  "clienteNombre":   "Carlos Ruiz",
  "clienteTelefono": "315-555-9900",
  "clienteCorreo":   "carlos@email.com",
  "tipoLente":       "PROGRESIVO",
  "medicoTratante":  "Dr. Suarez",
  "fechaEstimada":   "2026-10-01",
  "estado":          "EN_PROCESO"
}
```

**Campos obligatorios:** `clienteNombre`, `clienteTelefono`

**Respuesta exitosa (201):**
```json
{
  "ok": true,
  "mensaje": "Pedido creado correctamente.",
  "datos": { "id": 6, "clienteNombre": "Carlos Ruiz", "..." }
}
```

**Validación fallida (400):**
```json
{
  "ok": false,
  "mensaje": "El campo 'clienteNombre' es obligatorio.",
  "datos": null
}
```

---

### `PUT /api/pedidos/{id}`

Actualiza todos los campos de un pedido existente.

**Path param:** `id` (int)

**Body:** Mismo formato que POST (todos los campos)

**Ejemplo:**
```
PUT /api/pedidos/1
```
```json
{
  "clienteNombre":   "Ana Garcia",
  "clienteTelefono": "310-555-0101",
  "tipoLente":       "PROGRESIVO",
  "estado":          "LISTO"
}
```

**Respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "Pedido actualizado correctamente.",
  "datos": { "id": 1, "..." }
}
```

---

### `PATCH /api/pedidos/{id}`

Actualiza **únicamente el estado** de un pedido (operación parcial).

**Path param:** `id` (int)

**Body:**
```json
{
  "estado": "LISTO"
}
```

**Valores válidos:** `EN_PROCESO` · `LISTO` · `ENTREGADO`

**Respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "Estado del pedido actualizado.",
  "datos": { "id": 1, "estado": "LISTO" }
}
```

---

### `DELETE /api/pedidos/{id}`

Elimina un pedido de la base de datos.

**Path param:** `id` (int)

**Ejemplo:**
```
DELETE /api/pedidos/1
```

**Respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "Pedido eliminado correctamente.",
  "datos": null
}
```

---

## 3. API de Notificaciones

Gestiona las notificaciones enviadas a los clientes cuando el estado
de su pedido cambia. Compatible con el módulo `backend-servlets/notificaciones`
(evidencia GA7-AA2-EV02).

### Modelo Notificacion

```json
{
  "id":           1,
  "canal":        "EMAIL",
  "destinatario": "ana@email.com",
  "mensaje":      "Su pedido está listo para recoger.",
  "estado":       "ENVIADA",
  "fechaEnvio":   "2026-05-10T10:45:00",
  "idPedido":     1
}
```

| Campo | Tipo | Valores / Notas |
|-------|------|-----------------|
| `canal` | string | `EMAIL` \| `SMS` \| `PUSH`. Default: `EMAIL` |
| `destinatario` | string | **Obligatorio** (correo o teléfono) |
| `mensaje` | string | **Obligatorio** |
| `estado` | string | `PENDIENTE` \| `ENVIADA` \| `ERROR` |
| `idPedido` | int | Opcional – ID del pedido relacionado |

---

### `GET /api/notificaciones`

Lista todas las notificaciones, ordenadas por fecha descendente.

**Respuesta (200):**
```json
{
  "ok": true,
  "mensaje": "Notificaciones obtenidas.",
  "datos": {
    "total": 3,
    "notificaciones": [ { "id": 1, "canal": "EMAIL", "..." } ]
  }
}
```

---

### `GET /api/notificaciones/{id}`

Obtiene una notificación por ID.

---

### `POST /api/notificaciones`

Crea y envía una nueva notificación.

**Body:**
```json
{
  "canal":        "EMAIL",
  "destinatario": "luis@email.com",
  "mensaje":      "Su pedido #2 ya está listo para recoger.",
  "idPedido":     2
}
```

**Respuesta (201):**
```json
{
  "ok": true,
  "mensaje": "Notificacion enviada correctamente.",
  "datos": { "id": 4, "canal": "EMAIL", "estado": "ENVIADA", "..." }
}
```

---

### `PATCH /api/notificaciones/{id}`

Actualiza el estado de una notificación.

**Body:**
```json
{ "estado": "ERROR" }
```

**Valores:** `PENDIENTE` · `ENVIADA` · `ERROR`

---

### `DELETE /api/notificaciones/{id}`

Elimina una notificación.

---

## 4. API de Productos (Catálogo)

Gestiona el catálogo de lentes y monturas disponibles en la óptica.

### Modelo Producto

```json
{
  "id":          1,
  "codigoSku":   "RB3025",
  "nombre":      "Montura Ray-Ban Aviator",
  "descripcion": "Clásica estilo aviador",
  "categoria":   "MONTURA",
  "marca":       "Ray-Ban",
  "precioVenta": 320000,
  "stock":       18,
  "activo":      true
}
```

| Campo | Tipo | Valores / Notas |
|-------|------|-----------------|
| `codigoSku` | string | **Obligatorio**, único |
| `nombre` | string | **Obligatorio** |
| `categoria` | string | `MONTURA` \| `LENTE` \| `ACCESORIO` |
| `precioVenta` | decimal | **Obligatorio**, ≥ 0 |
| `stock` | int | Default 0 |

---

### `GET /api/productos`

Lista todos los productos activos del catálogo.

---

### `GET /api/productos/{id}`

Obtiene un producto por ID.

---

### `POST /api/productos`

Crea un nuevo producto en el catálogo.

**Body:**
```json
{
  "codigoSku":   "MB-PRO1",
  "nombre":      "Montura Boss Titanio",
  "descripcion": "Titanio ultraligero",
  "categoria":   "MONTURA",
  "marca":       "Hugo Boss",
  "precioVenta": 520000,
  "stock":       7
}
```

---

### `PUT /api/productos/{id}`

Actualiza un producto existente (todos los campos).

---

### `DELETE /api/productos/{id}`

Inactiva un producto del catálogo (eliminación lógica, `activo=0`).

---

## Pruebas con curl

```bash
# Health check
curl http://localhost:8080/vision-api/api/health

# Listar pedidos
curl http://localhost:8080/vision-api/api/pedidos

# Filtrar por estado
curl "http://localhost:8080/vision-api/api/pedidos?estado=EN_PROCESO"

# Obtener pedido por ID
curl http://localhost:8080/vision-api/api/pedidos/1

# Crear pedido
curl -X POST http://localhost:8080/vision-api/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"clienteNombre":"Test","clienteTelefono":"300-000-0000","tipoLente":"MONOFOCAL"}'

# Cambiar estado (PATCH)
curl -X PATCH http://localhost:8080/vision-api/api/pedidos/1 \
  -H "Content-Type: application/json" \
  -d '{"estado":"LISTO"}'

# Eliminar pedido
curl -X DELETE http://localhost:8080/vision-api/api/pedidos/1

# Listar productos
curl http://localhost:8080/vision-api/api/productos

# Enviar notificacion
curl -X POST http://localhost:8080/vision-api/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{"canal":"EMAIL","destinatario":"cliente@email.com","mensaje":"Su pedido está listo","idPedido":2}'
```
