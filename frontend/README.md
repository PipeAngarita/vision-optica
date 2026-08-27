# VISIÓN Óptica — Componente Front-end

Evidencia GA7-220501096-AA4-EV03 — Componente front-end del proyecto formativo y proyectos de clase.

(Construido sobre el diseño front-end de la evidencia GA6-220501096-AA4-EV03, agregando comentarios de código en HTML, CSS y JS conforme a los estándares de codificación definidos en la evidencia GA7-220501096-AA1-EV02.)

## Descripción

Componente front-end del sistema de gestión para óptica **VISIÓN**, construido con **HTML5**, **CSS3** y **JavaScript**, aplicando los principios de usabilidad y diseño definidos en las evidencias anteriores (estructura de componentes, elementos HTML, prototipos).

## Estándares de codificación aplicados

- Comentarios de encabezado (docblock) en cada archivo HTML, CSS y JS, indicando módulo y evidencia.
- Comentarios de sección en CSS (`/* Toolbar */`, `/* Modal */`, `/* Table */`, etc.) delimitando cada bloque de estilos.
- Comentarios JSDoc en cada función JavaScript (`@param`, descripción de propósito).
- Comentarios HTML marcando los bloques estructurales de cada vista (sidebar, toolbar, modal, tabla, toast).
- Nomenclatura: clases CSS en kebab-case, funciones JS en camelCase — según lo definido en el informe de estándares (GA7-AA1-EV02).

## Estructura de carpetas

```
VISION-Frontend/
├── login.html              Vista de inicio de sesión
├── dashboard.html           Panel principal con indicadores
├── pedidos.html              Gestión de pedidos (búsqueda, filtros, modal)
├── notificaciones.html       Centro de envío de notificaciones
├── css/
│   ├── variables.css        Paleta de colores y reset global compartido
│   ├── login.css
│   ├── dashboard.css
│   ├── pedidos.css
│   └── notificaciones.css
└── js/
    ├── login.js
    ├── dashboard.js
    ├── pedidos.js
    └── notificaciones.js
```

## Cómo verlo

Abrir `login.html` en cualquier navegador. Desde ahí se puede navegar a las demás vistas (usuario/contraseña: cualquier valor, o usar los accesos rápidos de Administrador/Empleado).

## Tecnologías

- **HTML5** semántico (`<nav>`, `<table>`, `<form>`, etc.)
- **CSS3** con variables (custom properties), Flexbox y Grid — hojas de estilo externas separadas por vista
- **JavaScript** (vanilla) para interactividad: filtros de tabla, modal de registro, plantillas automáticas de mensajes y notificaciones tipo toast
- **Tailwind CSS** (CDN) como apoyo puntual de utilidades
- Tipografías **DM Sans** y **Playfair Display** vía Google Fonts

## Paleta de colores

| Color | Hex |
|---|---|
| Navy (primario) | `#0f2744` |
| Blue (secundario) | `#1d5fa8` |
| Sky (interacción) | `#3b8fe8` |
| Frost (fondo suave) | `#e8f2fc` |
| Gold (acento) | `#c9a84c` |

