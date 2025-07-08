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
// 1. Demo data
// ----------------------------
const stories = [
  { title: 'Thần Thoại Bắc Âu', author: 'Rick Riordan', desc: 'Cuộc phiêu lưu huyền thoại…', cover: 'https://i.imgur.com/bwAGJto.jpg', tags: ['Hành động','Thần thoại'], status: 'Hoàn thành', rating: 4.8 },
  { title: 'Ma Đạo Tổ Sư', author: 'Mặc Hương Đồng Khứu', desc: 'Một thế giới ma pháp…', cover: 'https://i.imgur.com/ps3z4I6.jpg', tags: ['Đam mỹ','Kỳ ảo'], status: 'Đang ra', rating: 4.7 },
  { title: 'One Piece', author: 'Eiichiro Oda', desc: 'Hành trình vua hải tặc…', cover: 'https://i.imgur.com/6Wjt3AI.jpg', tags: ['Shounen','Phiêu lưu'], status: 'Đang ra', rating: 4.9 },
  { title: 'Hệ Thống Tu Tiên', author: 'Tiêu Dao', desc: 'Thanh niên xuyên không…', cover: 'https://i.imgur.com/7gkTd7U.jpg', tags: ['Tiên hiệp','Xuyên không'], status: 'Đang ra', rating: 4.5 },
  { title: 'Người Ở Bên Kia', author: 'Trần Thanh', desc: 'Mối tình đan xen bi kịch…', cover: 'https://i.imgur.com/DMk5C6Z.jpg', tags: ['Ngôn tình','Bi kịch'], status: 'Hoàn thành', rating: 4.3 }
];

const updates = [
  { title: 'Những Cô Gái Thú Nhân', cover: 'https://i.imgur.com/D1rkV8F.png', chapters: [{name:'Chap 92', time:'22h'} ,{name:'Chap 91',time:'22h'},{name:'Chap 90',time:'22h'}] },
  { title: 'Vị Vua Mạnh Nhất', cover: 'https://i.imgur.com/MFkD0pX.png', chapters: [{name:'Chap 127', time:'2d'},{name:'Chap 126',time:'2d'}] },
  { title: 'Phạm Nhân Bé Con', cover: 'https://i.imgur.com/yfWccFA.png', chapters: [{name:'Chap 60',time:'6d'}] },
  { title: 'Phế Vật Dòng Bá Tước', cover: 'https://i.imgur.com/1wNjP7j.png', chapters: [{name:'Chap 151',time:'1w'}] }
];

// user state
let currentUser = LS.get('scroll_user');
let favorites = LS.get('scroll_fav', []);

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
// 3. Render helpers
// ----------------------------
const storyCard = (s, i)=>`<div class="story-card"><img class="story-thumb" src="${s.cover}" alt="${s.title}"><div class="story-content"><a class="story-title" href="#">${s.title}</a><div class="story-desc">${s.desc}</div><div class="story-tags">${s.tags.map(t=>`<span class="story-tag">${t}</span>`).join('')}</div><div class="story-info"><span>${s.status}</span><span>⭐ ${s.rating}</span></div><div class="story-actions"><button class="read-btn">Đọc</button><button class="fav-btn ${favorites.includes(s.title)?'active':''}" data-idx="${i}">${favorites.includes(s.title)?'♥':'♡'} Yêu thích</button></div></div></div>`;

const updateCard = u=>`<div class="update-card"><img class="update-thumb" src="${u.cover}" alt="${u.title}"><div class="update-content"><a class="update-title" href="#">${u.title}</a><div class="update-chapters">${u.chapters.map(c=>`<div class="update-chapter"><strong>${c.name}</strong><time>${c.time}</time></div>`).join('')}</div></div></div>`;

function renderFeatured(){
  $('#storiesGrid').innerHTML = stories.map(storyCard).join('');
  $$('.fav-btn').forEach(btn=>btn.addEventListener('click',toggleFav));
}
function renderUpdates(){
  $('#updateGrid').innerHTML = updates.map(updateCard).join('');
}
function toggleFav(e){
  const idx = +e.currentTarget.dataset.idx;
  const title = stories[idx].title;
  if(favorites.includes(title)) favorites = favorites.filter(t=>t!==title);
  else favorites.push(title);
  LS.set('scroll_fav', favorites);
  renderFeatured();
}

// ----------------------------
// 4. Search suggest
// ----------------------------
const suggestList = $('#suggestList');
$('#searchInput').addEventListener('input', debounce(e=>{
  const kw = e.target.value.trim().toLowerCase();
  if(!kw){ suggestList.classList.remove('show'); return; }
  const found = stories.filter(s=>s.title.toLowerCase().includes(kw)||s.author.toLowerCase().includes(kw));
  suggestList.innerHTML = found.length?found.slice(0,6).map(s=>`<li>${s.title} – <em>${s.author}</em></li>`).join(''):`<li>Không tìm thấy…</li>`;
  suggestList.classList.add('show');
},120));
$('#searchInput').addEventListener('keydown',e=>{
  if(e.key==='Enter'){ e.preventDefault(); suggestList.classList.remove('show'); }
});
suggestList.addEventListener('click',e=>{
  if(e.target.tagName==='LI'){ $('#searchInput').value = e.target.innerText.split('–')[0].trim(); suggestList.classList.remove('show'); }
});

// hide suggest on blur
$('#searchInput').addEventListener('blur',()=>setTimeout(()=>suggestList.classList.remove('show'),150));

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

// ----------------------------
// 7. Init
// ----------------------------
renderFeatured();
renderUpdates();

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
        // Like/Dislike
        document.addEventListener('submit', (e) => {
            if (e.target.matches('.like-btn') || e.target.matches('.dislike-btn')) {
                e.preventDefault();
                const form = e.target.closest('form');
                const commentId = form.querySelector('input[name="commentId"]').value;
                const type = form.querySelector('button[type="submit"]').dataset.type;
                this.voteComment(commentId, type);
            }
        });

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
        fetch(`/api/comment/vote/${type}`, {
            method: 'POST',
            body: new URLSearchParams({ commentId })
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById(`like-count-${commentId}`).textContent = data.likeCount;
                document.getElementById(`dislike-count-${commentId}`).textContent = data.dislikeCount;
            } else {
                this.showMessage(data.message || 'Có lỗi khi vote', 'error');
            }
        });
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
