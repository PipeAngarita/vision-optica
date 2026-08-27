/**
 * login.js
 * Modulo: Login - Proyecto MagmaSoft / VISION
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * Contiene la logica de interaccion del formulario de inicio de sesion:
 * el envio manual del formulario y los accesos rapidos de prueba
 * (Administrador / Empleado).
 */

/**
 * Maneja el envio del formulario de login.
 * Valida que ambos campos tengan contenido, muestra un estado de carga
 * en el boton y redirige al dashboard tras una breve espera simulada.
 *
 * @param {SubmitEvent} e - Evento de envio del formulario
 */
function handleLogin(e) {
  e.preventDefault();
  const u = document.getElementById('user').value;
  const p = document.getElementById('pass').value;

  if (u && p) {
    // Feedback visual mientras se "verifica" el acceso
    document.querySelector('.btn-primary').textContent = 'Verificando...';
    setTimeout(() => { window.location.href = 'dashboard.html'; }, 800);
  }
}

/**
 * Autocompleta el formulario con credenciales de prueba y redirige
 * directamente al dashboard. Usado por los botones de acceso rapido
 * (Administrador / Empleado).
 *
 * @param {string} u - Usuario de prueba
 * @param {string} p - Contrasena de prueba
 */
function quickLogin(u, p) {
  document.getElementById('user').value = u;
  document.getElementById('pass').value = p;
  setTimeout(() => { window.location.href = 'dashboard.html'; }, 400);
}
