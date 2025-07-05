/**
 * JavaScript cho hệ thống coin và unlock chapter
 */

// Hiển thị modal confirm unlock chapter
function showUnlockModal(chapterId, chapterTitle, unlockCost, userCoins) {
    const modal = document.getElementById('unlockModal');
    const modalBody = modal.querySelector('.modal-body');
    
    if (userCoins < unlockCost) {
        modalBody.innerHTML = `
            <div class="alert alert-warning">
                <h5>Không đủ coin!</h5>
                <p>Bạn cần <strong>${unlockCost} coins</strong> để mở khóa chapter: <strong>${chapterTitle}</strong></p>
                <p>Bạn hiện có: <strong>${userCoins} coins</strong></p>
                <p>Hãy liên hệ admin để nạp thêm coin.</p>
            </div>
        `;
        modal.querySelector('.btn-unlock').style.display = 'none';
    } else {
        modalBody.innerHTML = `
            <div class="alert alert-info">
                <h5>Xác nhận mở khóa chapter</h5>
                <p>Chapter: <strong>${chapterTitle}</strong></p>
                <p>Phí: <strong>${unlockCost} coins</strong></p>
                <p>Coin hiện tại: <strong>${userCoins} coins</strong></p>
                <p>Coin sau khi mở khóa: <strong>${userCoins - unlockCost} coins</strong></p>
            </div>
        `;
        modal.querySelector('.btn-unlock').style.display = 'inline-block';
        modal.querySelector('.btn-unlock').onclick = () => unlockChapter(chapterId);
    }
    
    // Hiển thị modal (Bootstrap 5)
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
}

// Thực hiện unlock chapter
async function unlockChapter(chapterId) {
    try {
        const response = await fetch('/unlock-chapter', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=unlock&chapterId=${chapterId}`
        });
        
        const result = await response.json();
        
        if (result.success) {
            // Đóng modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('unlockModal'));
            modal.hide();
            
            // Hiển thị thông báo thành công
            showNotification(result.message, 'success');
            
            // Cập nhật coin hiển thị
            updateCoinDisplay(result.remainingCoins);
            
            // Reload trang để hiển thị nội dung chapter
            setTimeout(() => {
                window.location.reload();
            }, 1500);
            
        } else {
            showNotification(result.message, 'error');
        }
        
    } catch (error) {
        console.error('Error unlocking chapter:', error);
        showNotification('Có lỗi xảy ra khi mở khóa chapter', 'error');
    }
}

// Kiểm tra quyền truy cập chapter
async function checkChapterAccess(chapterId) {
    try {
        const response = await fetch(`/unlock-chapter?action=check&chapterId=${chapterId}`);
        const result = await response.json();
        
        return {
            accessible: result.accessible,
            userCoins: result.userCoins,
            unlockCost: result.unlockCost,
            isPremium: result.isPremium
        };
        
    } catch (error) {
        console.error('Error checking chapter access:', error);
        return { accessible: false };
    }
}

// Hiển thị thông báo
function showNotification(message, type = 'info') {
    // Tạo notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Thêm vào body
    document.body.appendChild(notification);
    
    // Tự động xóa sau 5 giây
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

// Cập nhật hiển thị coin
function updateCoinDisplay(newCoinAmount) {
    const coinElements = document.querySelectorAll('.user-coins, .coin-display, .header-coin-display');
    coinElements.forEach(element => {
        if (element.textContent.includes('💰')) {
            // Update header coin display format: 💰 123
            element.innerHTML = `💰 ${newCoinAmount}`;
        } else if (element.classList.contains('header-coin-display')) {
            // Update header specific display
            element.innerHTML = `💰 ${newCoinAmount}`;
        } else {
            // Update other coin displays
            element.textContent = newCoinAmount;
        }
        
        // Add animation effect
        element.classList.add('coin-flip');
        setTimeout(() => {
            element.classList.remove('coin-flip');
        }, 800);
    });
    
    // Update profile page coin stats if exists
    const statNumbers = document.querySelectorAll('.stat-number');
    statNumbers.forEach((element, index) => {
        // First stat item is usually coins in profile page
        if (index === 0 && element.closest('.stat-item').textContent.includes('Coins')) {
            element.textContent = newCoinAmount;
            element.classList.add('coin-flip');
            setTimeout(() => {
                element.classList.remove('coin-flip');
            }, 800);
        }
    });
}

// Khởi tạo khi DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Thêm event listener cho các nút unlock
    document.querySelectorAll('.btn-unlock-chapter').forEach(button => {
        button.addEventListener('click', function() {
            const chapterId = this.dataset.chapterId;
            const chapterTitle = this.dataset.chapterTitle;
            const unlockCost = parseInt(this.dataset.unlockCost);
            const userCoins = parseInt(this.dataset.userCoins);
            
            showUnlockModal(chapterId, chapterTitle, unlockCost, userCoins);
        });
    });
    
    // Kiểm tra và hiển thị trạng thái chapter premium
    updateChapterStatus();
});

// Cập nhật trạng thái hiển thị chapter premium
async function updateChapterStatus() {
    const chapterElements = document.querySelectorAll('[data-chapter-id]');
    
    for (const element of chapterElements) {
        const chapterId = element.dataset.chapterId;
        const accessInfo = await checkChapterAccess(chapterId);
        
        if (accessInfo.isPremium) {
            // Thêm class premium
            element.classList.add('premium-chapter');
            
            if (!accessInfo.accessible) {
                // Chapter chưa unlock
                element.classList.add('locked-chapter');
                
                // Thêm icon khóa
                const lockIcon = document.createElement('i');
                lockIcon.className = 'fas fa-lock text-warning';
                lockIcon.title = 'Chapter Premium - Cần mở khóa';
                element.appendChild(lockIcon);
            } else {
                // Chapter đã unlock
                element.classList.add('unlocked-chapter');
                
                // Thêm icon đã mở khóa
                const unlockIcon = document.createElement('i');
                unlockIcon.className = 'fas fa-unlock text-success';
                unlockIcon.title = 'Chapter đã mở khóa';
                element.appendChild(unlockIcon);
            }
        }
    }
} 