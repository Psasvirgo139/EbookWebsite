/**
 * JavaScript functions for payment page functionality
 */

function confirmPremiumUpgrade() {
    var currentCoins = parseInt(document.getElementById('currentCoins').value) || 0;
    var premiumCost = 100;
    
    if (currentCoins < premiumCost) {
        alert('Bạn không có đủ coin để nâng cấp Premium!\n' +
              'Cần: ' + premiumCost + ' coins\n' +
              'Hiện có: ' + currentCoins + ' coins');
        return;
    }
    
    var remainingCoins = currentCoins - premiumCost;
    
    var message = 'Xác nhận nâng cấp Premium?\n\n' +
                  '💰 Coins hiện tại: ' + currentCoins + ' coins\n' +
                  '💸 Chi phí: ' + premiumCost + ' coins\n' +
                  '💵 Coins còn lại: ' + remainingCoins + ' coins\n\n' +
                  '✨ Bạn sẽ trở thành Premium User và có thể xem tất cả chapter premium miễn phí!';
    
    if (confirm(message)) {
        document.getElementById('premiumCoinForm').submit();
    }
} 