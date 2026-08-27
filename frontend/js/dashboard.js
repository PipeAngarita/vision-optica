/**
 * dashboard.js
 * Modulo: Dashboard - Proyecto MagmaSoft / VISION
 * Evidencia: GA7-220501096-AA4-EV03
 *
 * Genera dinamicamente el mini grafico de barras "Pedidos por dia"
 * que se muestra en el panel principal.
 */

// Datos de ejemplo: cantidad de pedidos registrados por dia (Lun a Sab)
const data = [3, 5, 2, 7, 4, 6];

// Valor maximo, usado para escalar la altura de cada barra en porcentaje
const max = Math.max(...data);

const container = document.getElementById('barChart');

// Crea una barra por cada dato, resaltando el dia actual (indice 3 = Jueves)
data.forEach((v, i) => {
  const bar = document.createElement('div');
  bar.className = 'bar' + (i === 3 ? ' active' : '');
  bar.style.height = (v / max * 100) + '%';
  bar.title = v + ' pedidos';
  container.appendChild(bar);
});
