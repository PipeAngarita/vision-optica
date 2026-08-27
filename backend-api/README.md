# vision-api – REST API MagmaSoft-Vision

Módulo de servicios web REST para el sistema de gestión de óptica **MagmaSoft-Vision**.

**Evidencia:** GA7-220501096-AA5-EV03
**Aprendiz:** Juan Felipe Angarita Rodriguez | Mayo 2026
**Repositorio:** https://github.com/PipeAngarita/vision-optica

---

## Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java       | 11      | Lenguaje principal |
| Servlet API| 4.0.1   | Endpoints REST (sin frameworks) |
| Gson       | 2.10.1  | Serialización / deserialización JSON |
| MySQL      | 8.x     | Base de datos (compartida con backend-jdbc) |
| Tomcat     | 9.x     | Servidor de aplicaciones |
| Maven      | 3.x     | Gestión de dependencias y build |

---

## Estructura del módulo

```
backend-api/
├── pom.xml
├── DOCUMENTACION_API.md          ← Documentación completa de todos los servicios
├── REPOSITORIO_GITHUB.txt        ← Enlace al repositorio
└── src/main/
    ├── java/com/magmasoft/vision/api/
    │   ├── filter/
    │   │   ├── CorsFilter.java        ← Habilita CORS para el front-end
    │   │   └── EncodingFilter.java    ← UTF-8 global
    │   ├── model/
    │   │   ├── Pedido.java            ← Modelo pedido (= backend-jdbc)
    │   │   ├── Notificacion.java      ← Modelo notificacion (= backend-servlets)
    │   │   └── Producto.java          ← Modelo producto (catálogo)
    │   ├── dao/
    │   │   ├── PedidoDao.java         ← CRUD pedidos con JDBC
    │   │   ├── NotificacionDao.java   ← CRUD notificaciones con JDBC
    │   │   └── ProductoDao.java       ← CRUD productos con JDBC
    │   ├── servlet/
    │   │   ├── PedidoApiServlet.java      ← GET/POST/PUT/PATCH/DELETE /api/pedidos
    │   │   ├── NotificacionApiServlet.java ← GET/POST/PATCH/DELETE /api/notificaciones
    │   │   ├── ProductoApiServlet.java    ← GET/POST/PUT/DELETE /api/productos
    │   │   └── HealthServlet.java         ← GET /api/health
    │   └── util/
    │       ├── DatabaseConnection.java    ← Conexión JDBC a MySQL
    │       └── ApiResponse.java           ← Respuestas JSON estándar + helpers
    ├── resources/
    │   └── api_extension_db.sql   ← Extiende la BD del módulo backend-jdbc
    └── webapp/
        └── WEB-INF/
            └── web.xml            ← Mapeo de servlets y filtros
```

---

## Endpoints disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/health` | Estado de la API |
| GET | `/api/pedidos` | Listar todos los pedidos |
| GET | `/api/pedidos?estado=EN_PROCESO` | Filtrar por estado |
| GET | `/api/pedidos/{id}` | Obtener pedido por ID |
| POST | `/api/pedidos` | Crear nuevo pedido |
| PUT | `/api/pedidos/{id}` | Actualizar pedido completo |
| PATCH | `/api/pedidos/{id}` | Cambiar solo el estado |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |
| GET | `/api/notificaciones` | Listar notificaciones |
| GET | `/api/notificaciones/{id}` | Notificación por ID |
| POST | `/api/notificaciones` | Enviar notificación |
| PATCH | `/api/notificaciones/{id}` | Cambiar estado |
| DELETE | `/api/notificaciones/{id}` | Eliminar notificación |
| GET | `/api/productos` | Listar catálogo |
| GET | `/api/productos/{id}` | Producto por ID |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Inactivar producto |

---

## Cómo ejecutar

### 1. Base de datos
```bash
# Primero ejecutar el script base (si no lo has hecho):
mysql -u root -p < ../backend-jdbc/pedidos/src/main/resources/magmasoft_vision_db.sql

# Luego extender con las tablas que necesita la API:
mysql -u root -p < src/main/resources/api_extension_db.sql
```

### 2. Compilar
```bash
mvn clean package
```

### 3. Desplegar en Tomcat
```bash
cp target/vision-api.war $TOMCAT_HOME/webapps/
```

### 4. Verificar
```
http://localhost:8080/vision-api/api/health
```

---

## Modo demo (sin BD)

Si MySQL no está disponible, **todos los endpoints responden con datos de
ejemplo** en lugar de fallar. El campo `"fuente": "demo"` aparece en la
respuesta para indicarlo. Esto permite probar la API sin necesidad de tener
la base de datos configurada.
