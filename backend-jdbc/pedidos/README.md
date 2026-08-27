# MagmaSoft - VISION | Módulo de Pedidos (JDBC)

Evidencia **GA7-220501096-AA2-EV01** — Codificación de módulos del software
(conexión con base de datos por medio de JDBC).

## Descripción

Módulo de acceso a datos del sistema de gestión para óptica **VISIÓN**, que
permite insertar, consultar, actualizar y eliminar pedidos de lentes en una
base de datos MySQL, usando JDBC puro (sin frameworks de persistencia).

Los campos del pedido corresponden a los ya definidos en el prototipo
front-end `pedidos.html` y su modal "Registrar nuevo pedido"
(evidencia GA6-220501096-AA4-EV03): nombre y teléfono del cliente, tipo de
lente, médico tratante, fecha estimada y estado.

## Tecnologías

- Java 11
- JDBC (`java.sql.*`) con `PreparedStatement` (protección contra inyección SQL)
- MySQL 8.x (driver `mysql-connector-j`)
- Maven

## Estándares de codificación aplicados

Siguiendo lo definido en la evidencia GA7-220501096-AA1-EV02 (estándares de
codificación del proyecto):

| Elemento | Convención | Ejemplo |
|---|---|---|
| Paquetes | minúsculas, dominio invertido | `com.magmasoft.vision.pedidos.dao` |
| Clases | PascalCase, sustantivo | `PedidoDAO`, `ConexionBD` |
| Métodos | camelCase, verbo + sustantivo | `insertar()`, `consultarPorId()`, `actualizar()`, `eliminar()` |
| Variables | camelCase, nombre descriptivo | `clienteNombre`, `fechaEstimada`, `idGenerado` |
| Constantes | UPPER_SNAKE_CASE | `SQL_INSERTAR`, `SQL_CONSULTAR_TODOS` |

## Estructura del proyecto

```
proyecto/
├── pom.xml
└── src/main/
    ├── java/com/magmasoft/vision/pedidos/
    │   ├── modelo/
    │   │   └── Pedido.java
    │   ├── conexion/
    │   │   └── ConexionBD.java
    │   ├── dao/
    │   │   └── PedidoDAO.java          (insertar, consultarTodos, consultarPorId,
    │   │                                 consultarPorEstado, actualizar, eliminar)
    │   └── prueba/
    │       └── PruebaPedidoDAO.java    (clase main que ejercita las 4 operaciones)
    └── resources/
        └── magmasoft_vision_db.sql     (script de creación de la tabla + datos de ejemplo)
```

## Cómo ejecutarlo

```bash
# 1. Crear la base de datos
mysql -u root -p < src/main/resources/magmasoft_vision_db.sql

# 2. Ajustar usuario/clave en ConexionBD.java si es necesario

# 3. Compilar y ejecutar la clase de prueba
mvn compile exec:java -Dexec.mainClass="com.magmasoft.vision.pedidos.prueba.PruebaPedidoDAO"
```

### Resultado de la prueba (verificado localmente)

Antes de entregar esta evidencia, el módulo fue probado de extremo a extremo
contra una base de datos real (MySQL/MariaDB), ejecutando en orden las
cuatro operaciones sobre la tabla `pedido`:

```
== 1. CONSULTAR TODOS (estado inicial) ==
Pedido{id=1, clienteNombre='Ana Garcia', tipoLente='BIFOCAL', estado='EN_PROCESO', ...}
... (5 pedidos de ejemplo)

== 2. INSERTAR nuevo pedido ==
Pedido insertado con id = 6

== 3. CONSULTAR POR ID ==
Pedido{id=6, clienteNombre='Jorge Ramirez', tipoLente='PROGRESIVO', estado='EN_PROCESO', ...}

== 4. ACTUALIZAR pedido ==
Actualizacion exitosa: true
Pedido actualizado: Pedido{id=6, ..., estado='LISTO', ...}

== 5. CONSULTAR POR ESTADO (LISTO) ==
(3 pedidos con estado LISTO, incluido el id=6 recien actualizado)

== 6. ELIMINAR pedido de prueba ==
Eliminacion exitosa: true

== 7. CONSULTAR TODOS (estado final) ==
Total de pedidos al finalizar: 5 (igual al inicial, ya que el pedido de prueba fue eliminado)
```

Las cinco operaciones (insertar, consultar por id, consultar por estado,
actualizar y eliminar) se ejecutaron correctamente contra la base de datos.

> **Nota:** la validación anterior se hizo localmente con MariaDB
> (compatible a nivel de protocolo con MySQL), sustituyendo temporalmente el
> driver JDBC solo para la prueba. El código entregado apunta al driver
> oficial de MySQL (`com.mysql.cj.jdbc.Driver` / `mysql-connector-j`), que es
> el motor definido para el proyecto.

## Integración con el repositorio del proyecto

Este módulo se agrega en el repositorio ya existente del proyecto, en una
carpeta independiente (por ejemplo `backend-jdbc/pedidos`), junto al módulo
de Notificaciones (Servlets + JSP, evidencia GA7-AA2-EV02) y al front-end
(`VISION-Frontend/`) de las evidencias GA6-AA4.
