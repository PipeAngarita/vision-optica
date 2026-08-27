/**
 * pedidos.js
 * Modulo: Gestion de Pedidos - Proyecto MagmaSoft / VISION
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * Logica de la vista de pedidos: filtrado por estado, busqueda por
 * texto sobre la tabla, y apertura/cierre del modal de registro de
 * un nuevo pedido.
 */

// Filtro de estado actualmente activo (todos | en_proceso | listo | entregado)
let currentFilter = 'todos';

/**
 * Marca como activo el boton de filtro seleccionado y vuelve a
 * aplicar el filtrado sobre la tabla de pedidos.
 *
 * @param {HTMLElement} el - Boton de filtro que el usuario presiono
 * @param {string} f - Valor del filtro asociado al boton (ej: 'listo')
 */
function setFilter(el, f) {
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  el.classList.add('active');
  currentFilter = f;
  filterTable();
}

/**
 * Aplica, sobre las filas de la tabla de pedidos, el filtro de estado
 * activo y el texto de busqueda ingresado. Oculta las filas que no
 * cumplan alguna de las dos condiciones y actualiza el contador de
 * resultados visibles.
 */
function filterTable() {
  const q = document.getElementById('searchInput').value.toLowerCase();
  const rows = document.querySelectorAll('#tableBody tr');
  let vis = 0;

  rows.forEach(r => {
    const estado = r.dataset.estado;
    const text = r.textContent.toLowerCase();
    const matchFilter = currentFilter === 'todos' || estado === currentFilter;
    const matchSearch = !q || text.includes(q);

    r.style.display = (matchFilter && matchSearch) ? '' : 'none';
    if (matchFilter && matchSearch) vis++;
  });

  document.getElementById('countLabel').textContent = `Mostrando ${vis} pedido${vis !== 1 ? 's' : ''}`;
}

/** Muestra el modal de registro de nuevo pedido. */
function openModal() {
  document.getElementById('modal').classList.add('open');
}

/** Oculta el modal de registro de nuevo pedido. */
function closeModal() {
  document.getElementById('modal').classList.remove('open');
}
