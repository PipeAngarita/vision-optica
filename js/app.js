/* ═══════════════════════════════════════════════
   MagmaSoft – app.js  (datos y utilidades globales)
   GA6-220501096-AA3-EV03 / AA4-EV03
═══════════════════════════════════════════════ */

'use strict';

// ── Simulated data store ─────────────────────────────────────────
const MAGMA = {

  currentUser: {
    id: 1, name: 'Juan Felipe Angarita Rodriguez',
    initials: 'JF', role: 'ADMIN', email: 'jf.angarita@magmasoft.co'
  },

  products: [
    { id:1, sku:'RB3025',   name:'Montura Ray-Ban Aviator',  brand:'Ray-Ban',  cat:'Gafas de Sol',      price:320000, stock:18, min:5,  icon:'🕶️', active:true },
    { id:2, sku:'OX8118',   name:'Montura Oakley Crosslink', brand:'Oakley',   cat:'Deportivas',        price:410000, stock:3,  min:5,  icon:'👓', active:true },
    { id:3, sku:'TR-GEN8',  name:'Lentes Transitions GEN8',  brand:'Essilor',  cat:'Gafas Formuladas',  price:185000, stock:42, min:10, icon:'🔵', active:true },
    { id:4, sku:'CA8055',   name:'Montura Carrera 8055',      brand:'Carrera',  cat:'Gafas de Sol',      price:275000, stock:11, min:5,  icon:'🕶️', active:true },
    { id:5, sku:'ACV-30',   name:'Lentes Contacto Acuvue',   brand:'J&J',      cat:'Lentes de Contacto',price:95000,  stock:4,  min:8,  icon:'💧', active:true },
    { id:6, sku:'KIT-01',   name:'Kit Limpieza de Lentes',   brand:'Genérico', cat:'Accesorios',        price:28000,  stock:65, min:10, icon:'🧴', active:true },
    { id:7, sku:'EST-001',  name:'Estuche Premium Negro',     brand:'Genérico', cat:'Accesorios',        price:35000,  stock:2,  min:10, icon:'🎒', active:true },
    { id:8, sku:'MB-PRO1',  name:'Montura Boss Titanio',      brand:'Hugo Boss',cat:'Gafas Formuladas',  price:520000, stock:7,  min:5,  icon:'👓', active:true },
  ],

  orders: [
    { id:'ORD-0045', date:'10/05/2026', client:'Ana Torres',    items:2, total:505000, status:'En Proceso', employee:'J. Angarita' },
    { id:'ORD-0044', date:'10/05/2026', client:'Luis Gómez',    items:1, total:320000, status:'Registrado', employee:'M. López' },
    { id:'ORD-0043', date:'09/05/2026', client:'María López',   items:3, total:880000, status:'Listo',      employee:'J. Angarita' },
    { id:'ORD-0042', date:'09/05/2026', client:'Carlos Ruiz',   items:1, total:410000, status:'Entregado',  employee:'S. Martínez' },
    { id:'ORD-0041', date:'08/05/2026', client:'Pedro Silva',   items:2, total:220000, status:'Entregado',  employee:'M. López' },
    { id:'ORD-0040', date:'07/05/2026', client:'Laura Vega',    items:1, total:95000,  status:'Cancelado',  employee:'J. Angarita' },
    { id:'ORD-0039', date:'06/05/2026', client:'Sofía Mora',    items:2, total:460000, status:'Entregado',  employee:'J. Angarita' },
    { id:'ORD-0038', date:'05/05/2026', client:'Diego Castro',  items:1, total:185000, status:'Entregado',  employee:'M. López' },
  ],

  notifications: [
    { id:1, type:'info',    msg:'ORD-0045 actualizado a "En Proceso"',       read:false, time:'hace 5 min' },
    { id:2, type:'warning', msg:'Stock mínimo: Montura Oakley Crosslink',    read:false, time:'hace 18 min' },
    { id:3, type:'success', msg:'ORD-0043 listo para entrega',               read:false, time:'hace 1 hora' },
    { id:4, type:'info',    msg:'Nuevo pedido ORD-0045 registrado',          read:true,  time:'hace 2 horas' },
  ],

  orderHistory: [
    { status:'Registrado', date:'10/05/2026 · 09:12 a.m.', by:'J. Angarita', note:'Pedido creado', done:true, current:false },
    { status:'En Proceso', date:'10/05/2026 · 10:45 a.m.', by:'J. Angarita', note:'En preparación en laboratorio', done:false, current:true },
    { status:'Listo',      date:'Pendiente',                by:'—',           note:'El cliente será notificado', done:false, current:false },
    { status:'Entregado',  date:'Pendiente',                by:'—',           note:'Confirmación de entrega', done:false, current:false },
  ],

  categories: ['Todos','Gafas de Sol','Gafas Formuladas','Deportivas','Lentes de Contacto','Accesorios'],
  orderStatuses: ['Todos','Registrado','En Proceso','Listo','Entregado','Cancelado'],
};

// ── Formatters ───────────────────────────────────────────────────
function fmtMoney(n) {
  return '$' + n.toLocaleString('es-CO');
}

function stockClass(p) {
  if (p.stock === 0)        return 'stock-crit';
  if (p.stock < p.min)      return 'stock-low';
  return 'stock-ok';
}

function stockLabel(p) {
  if (p.stock === 0)   return 'Sin stock';
  if (p.stock < p.min) return `Stock bajo: ${p.stock}`;
  return `Stock: ${p.stock}`;
}

function statusChip(status) {
  const map = {
    'Registrado': 'chip-blue',
    'En Proceso': 'chip-orange',
    'Listo':      'chip-green',
    'Entregado':  'chip-gray',
    'Cancelado':  'chip-red',
  };
  return `<span class="chip ${map[status]||'chip-gray'}">${status}</span>`;
}

function progressBar(p) {
  const pct = Math.min(100, Math.round((p.stock / (p.min * 3)) * 100));
  let cls = 'bar-ok';
  if (p.stock < p.min) cls = 'bar-low';
  if (p.stock === 0)   cls = 'bar-crit';
  return `<div class="progress-wrap"><div class="progress-bar ${cls}" style="width:${pct}%"></div></div>`;
}

// ── Toast system ─────────────────────────────────────────────────
function showToast(msg, type='info', duration=3000) {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { info:'ℹ️', success:'✅', warn:'⚠️', error:'❌' };
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `<span>${icons[type]||'ℹ️'}</span> ${msg}`;
  container.appendChild(toast);
  setTimeout(() => { toast.style.opacity='0'; toast.style.transition='opacity .4s'; setTimeout(()=>toast.remove(), 400); }, duration);
}

// ── Modal helpers ────────────────────────────────────────────────
function openModal(id) {
  const m = document.getElementById(id);
  if (m) m.classList.add('open');
}
function closeModal(id) {
  const m = document.getElementById(id);
  if (m) m.classList.remove('open');
}
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay')) e.target.classList.remove('open');
  if (e.target.classList.contains('modal-close')) e.target.closest('.modal-overlay').classList.remove('open');
});

// ── Sidebar active link ──────────────────────────────────────────
function setActiveNav() {
  const page = location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-link').forEach(link => {
    const href = link.getAttribute('href') || '';
    link.classList.toggle('active', href === page || href.includes(page.replace('.html','')));
  });
}

// ── Mobile sidebar toggle ────────────────────────────────────────
function initMobileSidebar() {
  const btn  = document.getElementById('mob-menu-btn');
  const side = document.getElementById('sidebar');
  if (!btn || !side) return;
  btn.addEventListener('click', () => side.classList.toggle('open'));
  document.addEventListener('click', e => {
    if (!side.contains(e.target) && !btn.contains(e.target)) side.classList.remove('open');
  });
}

// ── Notification badge count ─────────────────────────────────────
function updateNotifBadge() {
  const unread = MAGMA.notifications.filter(n=>!n.read).length;
  const badge = document.querySelector('.notif-badge');
  if (badge) { badge.textContent = unread; badge.style.display = unread ? 'flex' : 'none'; }
}

// ── Search/filter helper ─────────────────────────────────────────
function filterList(items, query, fields) {
  if (!query) return items;
  const q = query.toLowerCase();
  return items.filter(item => fields.some(f => String(item[f]||'').toLowerCase().includes(q)));
}

// ── On DOM ready ─────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  setActiveNav();
  initMobileSidebar();
  updateNotifBadge();

  // Set user info
  const nameEls = document.querySelectorAll('.js-user-name');
  const initEls = document.querySelectorAll('.js-user-initials');
  const roleEls = document.querySelectorAll('.js-user-role');
  nameEls.forEach(el => el.textContent = MAGMA.currentUser.name.split(' ').slice(0,2).join(' '));
  initEls.forEach(el => el.textContent = MAGMA.currentUser.initials);
  roleEls.forEach(el => el.textContent = MAGMA.currentUser.role);

  // Notif dropdown toggle
  const notifBtn = document.querySelector('.topbar-notif');
  const notifDrop = document.getElementById('notif-dropdown');
  if (notifBtn && notifDrop) {
    notifBtn.addEventListener('click', e => {
      e.stopPropagation();
      notifDrop.classList.toggle('hidden');
    });
    document.addEventListener('click', () => notifDrop && notifDrop.classList.add('hidden'));
  }
});
