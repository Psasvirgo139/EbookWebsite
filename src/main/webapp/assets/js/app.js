// ============================
// scroll - main bundle JS
// ============================
// tiny helper
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
// 1. Data from API
// ----------------------------
let stories = [];
let updates = [];

// Load data from API
async function loadLatestBooks() {
    try {
        // Get context path dynamically - fix for localhost:9999
        let contextPath = '';
        const pathParts = window.location.pathname.split('/').filter(p => p.length > 0);
        if (pathParts.length > 0 && pathParts[0] !== 'index.jsp') {
            contextPath = '/' + pathParts[0];
        }
        
        console.log('Current URL:', window.location.href);
        console.log('Context path:', contextPath);
        console.log('Fetching from:', contextPath + '/latest');
        
        const response = await fetch(contextPath + '/latest');
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const books = await response.json();
        console.log('Loaded books:', books.length);
        
        // Convert books to stories format
        stories = books.map(book => ({
            id: book.id,
            title: book.title,
            author: 'Tác giả', // Will be updated when we have author data
            desc: book.description || 'Không có mô tả',
            cover: book.coverUrl || 'https://i.imgur.com/placeholder.jpg',
            tags: [book.releaseType || 'Tiểu thuyết'],
            status: book.status || 'Đang ra',
            rating: 4.5
        }));
        
        // Convert books to updates format
        updates = books.map(book => ({
            id: book.id,
            title: book.title,
            cover: book.coverUrl || 'https://i.imgur.com/placeholder.jpg',
            chapters: [{name: 'Chap 1', time: 'Mới'}]
        }));
        
        // Re-render after loading data
        console.log('Rendering featured stories:', stories.length);
        console.log('Rendering updates:', updates.length);
        renderFeatured();
        renderUpdates();
        
    } catch (error) {
        console.error('Error loading books:', error);
        // Fallback to demo data if API fails
        stories = [
            { id: 1, title: 'Thần Thoại Bắc Âu', author: 'Rick Riordan', desc: 'Cuộc phiêu lưu huyền thoại…', cover: 'https://i.imgur.com/bwAGJto.jpg', tags: ['Hành động','Thần thoại'], status: 'Hoàn thành', rating: 4.8 },
            { id: 2, title: 'Ma Đạo Tổ Sư', author: 'Mặc Hương Đồng Khứu', desc: 'Một thế giới ma pháp…', cover: 'https://i.imgur.com/ps3z4I6.jpg', tags: ['Đam mỹ','Kỳ ảo'], status: 'Đang ra', rating: 4.7 },
            { id: 3, title: 'One Piece', author: 'Eiichiro Oda', desc: 'Hành trình vua hải tặc…', cover: 'https://i.imgur.com/6Wjt3AI.jpg', tags: ['Shounen','Phiêu lưu'], status: 'Đang ra', rating: 4.9 },
            { id: 4, title: 'Hệ Thống Tu Tiên', author: 'Tiêu Dao', desc: 'Thanh niên xuyên không…', cover: 'https://i.imgur.com/7gkTd7U.jpg', tags: ['Tiên hiệp','Xuyên không'], status: 'Đang ra', rating: 4.5 },
            { id: 5, title: 'Người Ở Bên Kia', author: 'Trần Thanh', desc: 'Mối tình đan xen bi kịch…', cover: 'https://i.imgur.com/DMk5C6Z.jpg', tags: ['Ngôn tình','Bi kịch'], status: 'Hoàn thành', rating: 4.3 }
        ];
        
        updates = [
            { id: 1, title: 'Những Cô Gái Thú Nhân', cover: 'https://i.imgur.com/D1rkV8F.png', chapters: [{name:'Chap 92', time:'22h'} ,{name:'Chap 91',time:'22h'},{name:'Chap 90',time:'22h'}] },
            { id: 2, title: 'Vị Vua Mạnh Nhất', cover: 'https://i.imgur.com/MFkD0pX.png', chapters: [{name:'Chap 127', time:'2d'},{name:'Chap 126',time:'2d'}] },
            { id: 3, title: 'Phạm Nhân Bé Con', cover: 'https://i.imgur.com/yfWccFA.png', chapters: [{name:'Chap 60',time:'6d'}] },
            { id: 4, title: 'Phế Vật Dòng Bá Tước', cover: 'https://i.imgur.com/1wNjP7j.png', chapters: [{name:'Chap 151',time:'1w'}] }
        ];
        
        renderFeatured();
        renderUpdates();
    }
}

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
  $('#themeIcon').textContent = isDark?'☀️':'🌙';
})();
$('#toggleTheme').addEventListener('click',()=>{
  const dark = document.documentElement.dataset.theme==='dark';
  document.documentElement.dataset.theme = dark?'':'dark';
  $('#themeIcon').textContent = dark?'🌙':'☀️';
  LS.set('scroll_theme', dark?'light':'dark');
});

// ----------------------------
// 3. Render helpers
// ----------------------------
const storyCard = (s, i)=>{
  // Get context path the same way as loadLatestBooks
  let contextPath = '';
  const pathParts = window.location.pathname.split('/').filter(p => p.length > 0);
  if (pathParts.length > 0 && pathParts[0] !== 'index.jsp') {
      contextPath = '/' + pathParts[0];
  }
  return `<div class="story-card"><img class="story-thumb" src="${s.cover}" alt="${s.title}"><div class="story-content"><a class="story-title" href="${contextPath}/read?id=${s.id}">${s.title}</a><div class="story-desc">${s.desc}</div><div class="story-tags">${s.tags.map(t=>`<span class="story-tag">${t}</span>`).join('')}</div><div class="story-info"><span>${s.status}</span><span>⭐ ${s.rating}</span></div><div class="story-actions"><a href="${contextPath}/read?id=${s.id}" class="read-btn">Đọc</a><button class="fav-btn ${favorites.includes(s.title)?'active':''}" data-idx="${i}">${favorites.includes(s.title)?'♥':'♡'} Yêu thích</button></div></div></div>`;
};

const updateCard = u=>{
  // Get context path the same way as loadLatestBooks
  let contextPath = '';
  const pathParts = window.location.pathname.split('/').filter(p => p.length > 0);
  if (pathParts.length > 0 && pathParts[0] !== 'index.jsp') {
      contextPath = '/' + pathParts[0];
  }
  return `<div class="update-card"><img class="update-thumb" src="${u.cover}" alt="${u.title}"><div class="update-content"><a class="update-title" href="${contextPath}/read?id=${u.id}">${u.title}</a><div class="update-chapters">${u.chapters.map(c=>`<div class="update-chapter"><strong>${c.name}</strong><time>${c.time}</time></div>`).join('')}</div></div></div>`;
};

function renderFeatured(){
  console.log('renderFeatured called with', stories.length, 'stories');
  const grid = $('#storiesGrid');
  if (!grid) {
    console.error('storiesGrid element not found!');
    return;
  }
  grid.innerHTML = stories.map(storyCard).join('');
  console.log('Featured stories rendered to DOM');
  $$('.fav-btn').forEach(btn=>btn.addEventListener('click',toggleFav));
}
function renderUpdates(){
  console.log('renderUpdates called with', updates.length, 'updates');
  const grid = $('#updateGrid');
  if (!grid) {
    console.error('updateGrid element not found!');
    return;
  }
  grid.innerHTML = updates.map(updateCard).join('');
  console.log('Updates rendered to DOM');
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
loadLatestBooks(); // Load data from API instead of using demo data

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
