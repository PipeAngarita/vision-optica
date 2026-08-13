/* ═══════════════════════════════════════════════
   MagmaSoft – components.js
   Inyecta topbar, sidebar y bottom-nav en cada página
═══════════════════════════════════════════════ */

(function() {
  const TOPBAR_HTML = `
<nav class="topbar">
  <div style="display:flex;align-items:center;gap:.8rem;">
    <button id="mob-menu-btn" class="mob-menu-btn btn btn-primary btn-sm" style="display:none;padding:.4rem .6rem;font-size:1rem;">☰</button>
    <a href="../index.html" class="topbar-brand">
      <div class="brand-icon">👓</div>
      <div>
        <div>MagmaSoft</div>
        <span>Sistema de Gestión para Óptica</span>
      </div>
    </a>
  </div>
  <div class="topbar-right">
    <div class="topbar-notif" title="Notificaciones">
      🔔
      <span class="notif-badge">3</span>
    </div>
    <div id="notif-dropdown" class="hidden" style="position:absolute;top:56px;right:140px;width:300px;background:#fff;border-radius:10px;box-shadow:0 4px 20px rgba(0,0,0,.15);z-index:200;overflow:hidden;">
      <div style="padding:.8rem 1rem;border-bottom:1px solid #eee;font-weight:700;font-size:.85rem;color:#1a3a5c;">Notificaciones</div>
      <div id="notif-list"></div>
    </div>
    <div class="topbar-user">
      <div class="user-avatar js-user-initials">JF</div>
      <div>
        <div class="user-name js-user-name">Juan Felipe</div>
        <div class="user-role js-user-role">ADMIN</div>
      </div>
    </div>
  </div>
</nav>`;

  const SIDEBAR_HTML = `
<aside class="sidebar" id="sidebar">
  <div class="sidebar-section">
    <div class="sidebar-label">Principal</div>
    <a href="../index.html"             class="nav-link" data-page="index">   <span class="nav-icon">🏠</span> Inicio</a>
    <a href="../pages/catalogo.html"    class="nav-link" data-page="catalogo"><span class="nav-icon">🕶️</span> Catálogo</a>
    <a href="../pages/pedidos.html"     class="nav-link" data-page="pedidos"> <span class="nav-icon">📋</span> Pedidos</a>
    <a href="../pages/inventario.html"  class="nav-link" data-page="inventario"><span class="nav-icon">📦</span> Inventario</a>
  </div>
  <hr class="sidebar-divider">
  <div class="sidebar-section">
    <div class="sidebar-label">Gestión</div>
    <a href="../pages/clientes.html"    class="nav-link" data-page="clientes">   <span class="nav-icon">👥</span> Clientes</a>
    <a href="../pages/notificaciones.html" class="nav-link" data-page="notificaciones"><span class="nav-icon">🔔</span> Notificaciones <span class="nav-badge">3</span></a>
  </div>
  <hr class="sidebar-divider">
  <div class="sidebar-section">
    <a href="../pages/login.html" class="nav-link" style="color:#c0392b;"><span class="nav-icon">🚪</span> Cerrar Sesión</a>
  </div>
</aside>`;

  const BOTTOM_NAV_HTML = `
<nav class="bottom-nav">
  <a href="../index.html"            data-page="index">    <span class="bn-icon">🏠</span>Inicio</a>
  <a href="../pages/pedidos.html"    data-page="pedidos">  <span class="bn-icon">📋</span>Pedidos</a>
  <a href="../pages/catalogo.html"   data-page="catalogo"> <span class="bn-icon">🕶️</span>Catálogo</a>
  <a href="../pages/clientes.html"   data-page="clientes"> <span class="bn-icon">👥</span>Clientes</a>
</nav>`;

  const TOAST_HTML = `<div id="toast-container"></div>`;

  function inject() {
    // Topbar
    const tb = document.getElementById('topbar-placeholder');
    if (tb) tb.outerHTML = TOPBAR_HTML;

    // Sidebar
    const sb = document.getElementById('sidebar-placeholder');
    if (sb) sb.outerHTML = SIDEBAR_HTML;

    // Bottom nav
    const bn = document.getElementById('bottom-nav-placeholder');
    if (bn) bn.outerHTML = BOTTOM_NAV_HTML;

    // Toast container
    document.body.insertAdjacentHTML('beforeend', TOAST_HTML);

    // Active link by filename
    const page = location.pathname.split('/').pop().replace('.html','') || 'index';
    document.querySelectorAll('.nav-link[data-page], .bottom-nav a[data-page]').forEach(link => {
      if (link.dataset.page === page) link.classList.add('active');
    });

    // Notif dropdown content
    const notifList = document.getElementById('notif-list');
    if (notifList && typeof MAGMA !== 'undefined') {
      notifList.innerHTML = MAGMA.notifications.map(n => `
        <div style="padding:.7rem 1rem;border-bottom:1px solid #f5f5f5;display:flex;gap:.6rem;align-items:flex-start;${n.read?'opacity:.55':''}">
          <span>${n.type==='warning'?'⚠️':n.type==='success'?'✅':'ℹ️'}</span>
          <div>
            <div style="font-size:.82rem;color:#333;">${n.msg}</div>
            <div style="font-size:.72rem;color:#aaa;">${n.time}</div>
          </div>
        </div>`).join('');
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inject);
  } else {
    inject();
  }
})();
