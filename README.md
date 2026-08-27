# MagmaSoft - VISION

Sistema de gestión para óptica. Proyecto SENA ADSO.

## Estructura del repositorio

```
vision-optica/
├── frontend/                          Prototipo front-end (HTML + CSS + JS)
│   ├── login.html
│   ├── dashboard.html
│   ├── pedidos.html
│   ├── notificaciones.html
│   ├── css/
│   └── js/
│
├── backend-servlets/
│   └── notificaciones/                Módulo Notificaciones (Servlets + JSP)
│       ├── pom.xml
│       └── src/main/
│           ├── java/com/magmasoft/vision/notificaciones/
│           └── webapp/
│
└── backend-jdbc/
    └── pedidos/                       Módulo Pedidos (JDBC - MySQL)
        ├── pom.xml
        └── src/main/
            ├── java/com/magmasoft/vision/pedidos/
            └── resources/
                └── magmasoft_vision_db.sql
```

## Evidencias SENA relacionadas

| Módulo | Evidencia | Descripción |
|---|---|---|
| `frontend/` | GA6-220501096-AA4-EV03 | Diseño front-end (HTML/CSS/JS) |
| `backend-servlets/notificaciones/` | GA7-220501096-AA2-EV02 | Servlets + JSP, formularios, GET/POST |
| `backend-jdbc/pedidos/` | GA7-220501096-AA2-EV01 | Conexión JDBC, CRUD completo |

Cada módulo backend trae su propio `README.md` con instrucciones de
ejecución y detalles técnicos.
