/**
 * notificaciones.js
 * Modulo: Notificaciones - Proyecto MagmaSoft / VISION
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * Logica de la vista de notificaciones: seleccion de canal de envio,
 * autocompletado de plantillas de mensaje segun el tipo de
 * notificacion, y confirmacion visual (toast) del envio.
 */

/**
 * Marca visualmente como seleccionado el canal de envio elegido
 * (correo o SMS).
 *
 * @param {HTMLElement} el - Tarjeta de canal presionada por el usuario
 */
function selectChannel(el) {
  document.querySelectorAll('.channel-opt').forEach(o => o.classList.remove('selected'));
  el.classList.add('selected');
}

/**
 * Autocompleta el asunto y el mensaje del formulario segun el tipo
 * de notificacion seleccionado, usando plantillas predefinidas.
 *
 * @param {string} type - Tipo de notificacion ('listo' | 'modelo' | 'cita' | 'promo')
 */
function fillTemplate(type) {
  const templates = {
    listo: {
      asunto: 'Su lente ya está listo para retirar — VISIÓN Óptica',
      msg: 'Estimado cliente,\n\nNos complace informarle que su pedido de lentes ya se encuentra listo para ser retirado en nuestra óptica.\n\nPor favor acérquese en horario de atención (L-S 8am-6pm) y presente su comprobante.\n\n¡Gracias por confiar en VISIÓN Óptica!'
    },
    modelo: {
      asunto: '¡Nuevos modelos disponibles! — VISIÓN Óptica',
      msg: 'Estimado cliente,\n\nQueremos invitarle a conocer nuestra nueva colección de monturas y lentes que acaba de llegar.\n\nVisítenos y descubra diseños modernos con la mejor calidad óptica.\n\n¡Le esperamos!'
    },
    cita: {
      asunto: 'Recordatorio de cita — VISIÓN Óptica',
      msg: 'Estimado cliente,\n\nLe recordamos que tiene una cita programada próximamente en nuestra óptica.\n\nSi necesita reprogramar, contáctenos con anticipación.\n\n¡Hasta pronto!'
    },
    promo: {
      asunto: 'Oferta especial para usted — VISIÓN Óptica',
      msg: 'Estimado cliente,\n\nTenemos una promoción exclusiva para usted: 20% de descuento en su próxima compra de monturas.\n\nVálido hasta fin de mes. ¡No se la pierda!'
    },
  };

  if (templates[type]) {
    document.getElementById('asunto').value = templates[type].asunto;
    document.getElementById('mensaje').value = templates[type].msg;
  }
}

/**
 * Muestra un mensaje de confirmacion temporal (toast) en la esquina
 * de la pantalla, y lo oculta automaticamente despues de 3.2 segundos.
 *
 * @param {string} msg - Texto a mostrar en el toast
 */
function showToast(msg) {
  const t = document.getElementById('toast');
  document.getElementById('toastMsg').textContent = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 3200);
}

/** Simula el envio de una notificacion individual al cliente seleccionado. */
function sendNotif() {
  showToast('Notificación enviada correctamente');
}

/** Simula el envio masivo de una notificacion a todos los clientes registrados. */
function sendBulk() {
  showToast('Notificación masiva enviada a 134 clientes');
}
