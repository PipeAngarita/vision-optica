<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--
    notificaciones.jsp
    Modulo: Notificaciones - Proyecto MagmaSoft / VISION
    Evidencia: GA7-220501096-AA2-EV02

    Elementos JSP utilizados:
      - Directiva de pagina:  <%@ page %>
      - Directiva de libreria: <%@ taglib %> (JSTL)
      - Expresiones EL:        ${ ... }
      - Etiquetas JSTL core:   <c:forEach>, <c:if>, <c:choose>
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>VISION Optica - Notificaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

<header class="topbar">
    <div class="brand">
        <span class="brand-eye">&#9673;</span>
        <div>
            <h1>VISION <span class="brand-sub">OPTICA</span></h1>
        </div>
    </div>
    <div class="topbar-meta">Modulo de Notificaciones &middot; MagmaSoft</div>
</header>

<main class="content">

    <c:if test="${not empty mensajeExito}">
        <div class="alert alert-success">${mensajeExito}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <div class="grid-2">

        <!-- ==================== FORMULARIO ==================== -->
        <section class="card">
            <h2>Enviar notificacion</h2>
            <p class="muted">Correo o SMS directo al cliente</p>

            <form action="${pageContext.request.contextPath}/notificaciones" method="post">

                <label for="clienteNombre">Nombre del cliente</label>
                <input type="text" id="clienteNombre" name="clienteNombre" placeholder="Ej: Ana Garcia" required>

                <label for="clienteContacto">Correo o telefono</label>
                <input type="text" id="clienteContacto" name="clienteContacto" placeholder="correo@ejemplo.com o 300-000-0000" required>

                <label for="canal">Canal de envio</label>
                <select id="canal" name="canal">
                    <option value="Email">Correo electronico</option>
                    <option value="SMS">SMS / Mensaje de texto</option>
                </select>

                <label for="tipo">Tipo de notificacion</label>
                <select id="tipo" name="tipo">
                    <option value="Lente listo">Lente listo para retirar</option>
                    <option value="Recordatorio">Recordatorio de cita</option>
                    <option value="Promocion">Promocion</option>
                    <option value="Nuevo modelo">Nuevo modelo disponible</option>
                </select>

                <label for="asunto">Asunto</label>
                <input type="text" id="asunto" name="asunto" placeholder="Asunto del mensaje">

                <label for="mensaje">Mensaje</label>
                <textarea id="mensaje" name="mensaje" rows="4" placeholder="Escriba el contenido del mensaje..." required></textarea>

                <button type="submit" class="btn-primary">Enviar notificacion &rarr;</button>
            </form>
        </section>

        <!-- ==================== HISTORIAL ==================== -->
        <section class="card">
            <div class="card-header-row">
                <div>
                    <h2>Historial de notificaciones</h2>
                    <p class="muted">Total registradas: ${totalEnviadas}</p>
                </div>
            </div>

            <div class="filtros">
                Filtrar por canal:
                <a href="${pageContext.request.contextPath}/notificaciones"
                   class="chip ${empty canalFiltro ? 'chip-active' : ''}">Todos</a>
                <a href="${pageContext.request.contextPath}/notificaciones?canal=Email"
                   class="chip ${canalFiltro == 'Email' ? 'chip-active' : ''}">Email</a>
                <a href="${pageContext.request.contextPath}/notificaciones?canal=SMS"
                   class="chip ${canalFiltro == 'SMS' ? 'chip-active' : ''}">SMS</a>
            </div>

            <table class="tabla">
                <thead>
                <tr>
                    <th>Cliente</th>
                    <th>Tipo</th>
                    <th>Canal</th>
                    <th>Fecha</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty historial}">
                        <tr>
                            <td colspan="4" class="vacio">No hay notificaciones registradas todavia.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="n" items="${historial}">
                            <tr>
                                <td>
                                    <strong>${n.clienteNombre}</strong><br>
                                    <span class="muted">${n.clienteContacto}</span>
                                </td>
                                <td>${n.tipo}</td>
                                <td>
                                    <span class="badge badge-${n.canal == 'Email' ? 'email' : 'sms'}">${n.canal}</span>
                                </td>
                                <td>${n.fechaEnvioFormateada}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </section>

    </div>
</main>

</body>
</html>
