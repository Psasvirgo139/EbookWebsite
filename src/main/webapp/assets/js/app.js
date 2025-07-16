// ============================
// scroll - main bundle JS
// ============================
// tiny helper

console.log('=== app.js loaded ===');
const $ = (sel, par = document) => par.querySelector(sel);
const $$ = (sel, par = document) => par.querySelectorAll(sel);
const LS = {
  get: (k, def = null) => {
    try { return JSON.parse(localStorage.getItem(k)) ?? def; } catch { return def; }
  },
  set: (k, v) => localStorage.setItem(k, JSON.stringify(v))
};
const debounce = (fn, d = 150) => {
  let t; return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), d); };
};

// ----------------------------
// 2. Theme toggle
// ----------------------------
(function initTheme(){
  const saved = LS.get('scroll_theme');
  const prefers = window.matchMedia('(prefers-color-scheme: dark)').matches;
  const isDark = saved ? saved==='dark' : prefers;
  document.documentElement.dataset.theme = isDark?'dark':'';
  const themeIcon = $('#themeIcon');
  if (themeIcon) themeIcon.textContent = isDark?'☀️':'🌙';
})();
const toggleThemeBtn = $('#toggleTheme');
if (toggleThemeBtn) {
  toggleThemeBtn.addEventListener('click',()=>{
    const dark = document.documentElement.dataset.theme==='dark';
    document.documentElement.dataset.theme = dark?'':'dark';
    const themeIcon = $('#themeIcon');
    if (themeIcon) themeIcon.textContent = dark?'🌙':'☀️';
    LS.set('scroll_theme', dark?'light':'dark');
  });
}

// ----------------------------
// 3. Render helpers (KHÔNG còn render stories demo)
// ----------------------------
// (Đã loại bỏ toàn bộ biến stories, updates và renderFeatured, renderUpdates, toggleFav)

// ----------------------------
// 4. Search suggest (REAL DB)
const suggestList = $('#suggestList');
const searchInput = $('#searchInput');
const searchForm = searchInput.closest('form');

searchInput.addEventListener('input', debounce(async e => {
  const kw = e.target.value.trim();
  if (!kw) { suggestList.classList.remove('show'); return; }
  // Gọi API lấy sách thật
  try {
    const res = await fetch(`/api/book-suggest?keyword=${encodeURIComponent(kw)}`);
    const data = await res.json();
    if (data.length === 0) {
      suggestList.innerHTML = `<li>Không tìm thấy…</li>`;
    } else {
      suggestList.innerHTML = data.map(book =>
        `<li data-id="${book.id}" data-title="${book.title}"><b>${book.title}</b>${book.author ? ' – <em>' + book.author + '</em>' : ''}</li>`
      ).join('');
    }
    suggestList.classList.add('show');
  } catch {
    suggestList.innerHTML = `<li>Lỗi kết nối</li>`;
    suggestList.classList.add('show');
  }
}, 120));

searchInput.addEventListener('keydown', e => {
  if (e.key === 'Enter') {
    suggestList.classList.remove('show');
    // Cho phép submit form bình thường
  }
});
suggestList.addEventListener('click', e => {
  if (e.target.tagName === 'LI') {
    searchInput.value = e.target.dataset.title;
    suggestList.classList.remove('show');
    // Submit form tìm kiếm
    if (searchForm) searchForm.submit();
  }
});
// hide suggest on blur
searchInput.addEventListener('blur', () => setTimeout(() => suggestList.classList.remove('show'), 150));

const clearBtn = document.querySelector('.header-search-clear');
searchInput.addEventListener('input', () => {
  clearBtn.style.display = searchInput.value ? 'flex' : 'none';
});
clearBtn.addEventListener('click', () => {
  searchInput.value = '';
  clearBtn.style.display = 'none';
  searchInput.focus();
  suggestList.classList.remove('show');
});

// ----------------------------
// 5. Account dropdown & modal
// ----------------------------
$('#accountBtn').addEventListener('click',e=>{
  e.stopPropagation();
  $('#accountMenu').classList.toggle('show');
  $('#accountBtn').setAttribute('aria-expanded', $('#accountMenu').classList.contains('show'));
});
document.body.addEventListener('click',e=>{
  if(!$('#accountDropdown').contains(e.target)) $('#accountMenu').classList.remove('show');
});

// quick modal using <dialog>
const dlg = /** @type {HTMLDialogElement} */($('#modalAuth'));
function openAuth(isReg=false){
  dlg.querySelector('#modalTitle').textContent = isReg?'Đăng ký':'Đăng nhập';
  dlg.showModal();
}
$('#loginBtn').onclick = ()=>openAuth(false);
$('#registerBtn').onclick = ()=>openAuth(true);
$('#closeModal').onclick = ()=>dlg.close();

dlg.addEventListener('close',()=>$('#accountMenu').classList.remove('show'));

// ----------------------------
// 6. Mobile nav toggle
// ----------------------------
$('#hamburgerBtn').addEventListener('click',()=>$('#mainNav').classList.toggle('show-mobile'));
window.addEventListener('resize',()=>$('#mainNav').classList.remove('show-mobile'));

// Sidebar menu hiệu ứng mượt
const hamburgerMenu = document.getElementById('hamburgerMenu');
const sidebarMenu = document.getElementById('sidebarMenu');
const sidebarClose = document.getElementById('sidebarClose');
let sidebarBackdrop = null;

function showSidebarMenu() {
  sidebarMenu.style.display = 'block';
  setTimeout(() => sidebarMenu.classList.add('show'), 10); // trigger CSS transition
  // Tạo overlay mờ
  sidebarBackdrop = document.createElement('div');
  sidebarBackdrop.className = 'sidebar-backdrop';
  sidebarBackdrop.onclick = hideSidebarMenu;
  document.body.appendChild(sidebarBackdrop);
  document.body.style.overflow = 'hidden';
}
function hideSidebarMenu() {
  sidebarMenu.classList.remove('show');
  document.body.style.overflow = '';
  if (sidebarBackdrop) {
    sidebarBackdrop.remove();
    sidebarBackdrop = null;
  }
  setTimeout(() => { sidebarMenu.style.display = 'none'; }, 300); // chờ transition
}
if (hamburgerMenu && sidebarMenu) {
  hamburgerMenu.addEventListener('click', showSidebarMenu);
}
if (sidebarClose) {
  sidebarClose.addEventListener('click', hideSidebarMenu);
}

// ----------------------------
// 7. Init
// ----------------------------
// lấy phần tử
const hamburgerBtn = $('#hamburgerBtn');
const mainNav      = $('#mainNav');
let backdropEl     = null;

// khi click vào hamburger / close
hamburgerBtn.addEventListener('click', () => {
  const isOpen = mainNav.classList.toggle('show-mobile');

  // đổi icon và label
  hamburgerBtn.innerHTML = isOpen ? '×' : '&#9776;';
  hamburgerBtn.setAttribute(
    'aria-label',
    isOpen ? 'Đóng menu di động' : 'Mở menu di động'
  );

  // tạo hoặc xóa backdrop
  if (isOpen) showBackdrop();
  else         hideBackdrop();
});

// khi resize lớn hơn mobile, luôn reset
window.addEventListener('resize', () => {
  if (window.innerWidth > 768) {
    mainNav.classList.remove('show-mobile');
    hamburgerBtn.innerHTML = '&#9776;';
    hamburgerBtn.setAttribute('aria-label', 'Mở menu di động');
    hideBackdrop();
  }
});

function showBackdrop() {
  backdropEl = document.createElement('div');
  backdropEl.className = 'nav-backdrop';

  // click vào backdrop cũng đóng menu
  backdropEl.addEventListener('click', () => {
    mainNav.classList.remove('show-mobile');
    hamburgerBtn.innerHTML = '&#9776;';
    hamburgerBtn.setAttribute('aria-label', 'Mở menu di động');
    hideBackdrop();
  });

  document.body.appendChild(backdropEl);
}

function hideBackdrop() {
  if (backdropEl) {
    backdropEl.remove();
    backdropEl = null;
  }
}
// ==== COMMENT SYSTEM LOGIC DISABLED FOR SERVLET VERSION ====
// (Toàn bộ logic comment bằng JS đã bị vô hiệu hóa, chỉ dùng servlet truyền thống)

// Remove any JavaScript vote handling to avoid conflicts with servlet
document.addEventListener('DOMContentLoaded', function() {
    // Disable any JavaScript vote handling
    const voteBtns = document.querySelectorAll('.like-btn, .dislike-btn');
    voteBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            // Let the form submit naturally to servlet
        });
    });
});

// 1. Lấy tất cả section có id, lấy tất cả nav-link
const sections = document.querySelectorAll('main section[id]');
const navLinks = document.querySelectorAll('.main-nav a');

// 2. Tạo observer
const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      // khi 1 section vào viewport, active link tương ứng
      navLinks.forEach(link => {
        link.classList.toggle(
          'active',
          link.dataset.page === entry.target.id
        );
      });
    }
  });
}, {
  rootMargin: '-50% 0px -50% 0px', // kích hoạt khi section ở giữa màn hình
  threshold: 0
});

// 3. Observe từng section
sections.forEach(sec => observer.observe(sec));

// 4. Khi click link, set active ngay lập tức
navLinks.forEach(link => {
  link.addEventListener('click', () => {
    navLinks.forEach(l => l.classList.remove('active'));
    link.classList.add('active');
  });
});

// trong app.js, sau khi init mobile nav
const genreItem = document.querySelector('.main-nav .nav-item');
const genreLink = genreItem.querySelector('.nav-link');

genreLink.addEventListener('click', e => {
  if (window.innerWidth <= 768) {
    e.preventDefault();
    genreItem.classList.toggle('open');
  }
});

// 9. Comment System - Advanced (like/dislike, edit, delete, realtime)
class AdvancedCommentSystem extends CommentSystem {
    constructor() {
        super();
        this.deleteCommentId = null;
        this.initAdvancedEvents();
    }

    initAdvancedEvents() {
        // Like/Dislike - DISABLED FOR SERVLET VERSION
        // document.addEventListener('submit', (e) => {
        //     if (e.target.matches('.like-btn') || e.target.matches('.dislike-btn')) {
        //         e.preventDefault();
        //         const form = e.target.closest('form');
        //         const commentId = form.querySelector('input[name="commentId"]').value;
        //         const type = form.querySelector('button[type="submit"]').dataset.type;
        //         this.voteComment(commentId, type);
        //     }
        // });

        // Delete
        document.addEventListener('click', (e) => {
            if (e.target.matches('.delete-btn')) {
                e.preventDefault();
                this.deleteCommentId = e.target.dataset.commentId;
                this.showDeleteModal();
            }
        });
        document.getElementById('cancelDeleteBtn').onclick = () => this.hideDeleteModal();
        document.getElementById('confirmDeleteBtn').onclick = () => this.confirmDelete();

        // Edit (simple inline, không popup)
        document.addEventListener('click', (e) => {
            if (e.target.matches('.edit-btn')) {
                e.preventDefault();
                this.showEditForm(e.target.dataset.commentId);
            }
        });
    }

    voteComment(commentId, type) {
        // DISABLED: Use servlet form submission instead
        console.log('Vote comment disabled in favor of servlet');
    }

    showDeleteModal() {
        document.getElementById('deleteModal').style.display = 'flex';
    }
    hideDeleteModal() {
        document.getElementById('deleteModal').style.display = 'none';
        this.deleteCommentId = null;
    }
    confirmDelete() {
        if (!this.deleteCommentId) return;
        fetch('/api/comment/delete', {
            method: 'POST',
            body: new URLSearchParams({ commentId: this.deleteCommentId })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                // Đánh dấu đã xóa trên giao diện
                document.getElementById(`content-${this.deleteCommentId}`).textContent = 'Bình luận này đã bị xóa.';
                document.getElementById(`deleted-flag-${this.deleteCommentId}`).textContent = ' (Đã xóa)';
            } else {
                this.showMessage(data.message || 'Có lỗi khi xóa', 'error');
            }
            this.hideDeleteModal();
        });
    }

    showEditForm(commentId) {
        const contentDiv = document.getElementById(`content-${commentId}`);
        if (!contentDiv) return;
        const oldContent = contentDiv.textContent;
        contentDiv.innerHTML = `<textarea id="edit-textarea-${commentId}" class="form-control" style="width:100%; min-height:60px;">${oldContent}</textarea>
            <button class="btn btn-sm btn-success" id="saveEditBtn-${commentId}">Lưu</button>
            <button class="btn btn-sm btn-secondary" id="cancelEditBtn-${commentId}">Hủy</button>`;
        document.getElementById(`saveEditBtn-${commentId}`).onclick = () => this.saveEdit(commentId, oldContent);
        document.getElementById(`cancelEditBtn-${commentId}`).onclick = () => { contentDiv.textContent = oldContent; };
    }

    saveEdit(commentId, oldContent) {
        const textarea = document.getElementById(`edit-textarea-${commentId}`);
        const newContent = textarea.value.trim();
        if (!newContent || newContent === oldContent) return;
        fetch('/api/comment/edit', {
            method: 'POST',
            body: new URLSearchParams({ commentId, oldContent })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById(`content-${commentId}`).textContent = newContent;
                document.getElementById(`edited-flag-${commentId}`).textContent = ' (Đã sửa)';
            } else {
                this.showMessage(data.message || 'Có lỗi khi sửa', 'error');
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', function() {
    if (document.querySelector('.comments-section')) {
        console.log('Initializing AdvancedCommentSystem...');
        new AdvancedCommentSystem();
    }
});
