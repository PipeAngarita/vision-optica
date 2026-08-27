# MagmaSoft - VISION | Módulo de Notificaciones

Evidencia **GA7-220501096-AA2-EV02** — Módulos de software codificados y probados
(codificación del módulo front-end web con Servlets).

## Descripción

Módulo de notificaciones del sistema de gestión para óptica **VISIÓN**. Permite
registrar el envío de una notificación (correo o SMS) a un cliente y consultar
el historial de notificaciones enviadas, filtrando por canal.

Es la versión funcional (Java, Servlets + JSP) del prototipo front-end
`notificaciones.html` construido en la evidencia GA6-220501096-AA4-EV03.

## Tecnologías

- Java 11
- Servlets (`javax.servlet-api` 4.0.1) — anotación `@WebServlet`, sin `web.xml`
- JSP con JSTL (`taglibs-standard` / `jstl 1.2`) — directivas, EL y etiquetas `<c:forEach>`, `<c:if>`, `<c:choose>`
- Maven (empaquetado `.war`)
- Servidor de referencia: Apache Tomcat 9.x

## Estructura del proyecto

```
proyecto/
├── pom.xml
├── src/main/java/com/magmasoft/vision/notificaciones/
│   ├── model/
│   │   └── Notificacion.java
│   └── servlet/
│       └── NotificacionServlet.java
└── src/main/webapp/
    ├── index.jsp                 (redirige al servlet /notificaciones)
    ├── notificaciones.jsp        (formulario + historial)
    └── css/estilo.css
```

## Flujo de la aplicación

1. `GET /notificaciones` → `NotificacionServlet.doGet()` arma el historial
   (con filtro opcional `?canal=Email` o `?canal=SMS`) y lo reenvía (`forward`)
   a `notificaciones.jsp`.
2. El usuario diligencia el formulario HTML (cliente, contacto, canal, tipo,
   asunto, mensaje) y lo envía por `POST` a `/notificaciones`.
3. `NotificacionServlet.doPost()` valida los campos obligatorios, agrega la
   notificación al historial y hace `redirect` de vuelta a `GET /notificaciones`
   (patrón Post/Redirect/Get, evita el reenvío del formulario al recargar).

> **Nota sobre persistencia:** esta evidencia se enfoca en Servlets, formularios
> HTML y JSP (GA7-AA2-EV02); por eso el historial se maneja en una lista en
> memoria. La conexión a base de datos por JDBC del proyecto se documenta en el
> módulo correspondiente a la evidencia GA7-AA2-EV01.

## Cómo ejecutarlo

Con Maven y Tomcat 9 instalados:

```bash
mvn tomcat7:run
```

y abrir `http://localhost:8080/vision/`

O generando el `.war` y desplegándolo manualmente:

```bash
mvn package
# copiar target/magmasoft-vision-notificaciones.war a la carpeta webapps/ de Tomcat
```

## Integración con el repositorio del proyecto

Este módulo se agrega dentro del repositorio ya existente del proyecto,
en una carpeta independiente (por ejemplo `backend-servlets/notificaciones`),
junto al módulo JDBC (Login, Producto, Pedido, Cliente, Inventario) de la
evidencia GA7-AA2-EV01, y al front-end (`VISION-Frontend/`) de las evidencias
GA6-AA4. Los nombres de paquete (`com.magmasoft.vision.notificaciones.*`) se
eligieron para no chocar con las clases del otro módulo.
