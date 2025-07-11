# 👑 Premium Expiration System

## Tổng quan
Hệ thống kiểm tra và xử lý thời hạn premium tự động cho EbookWebsite. Bao gồm:
- ✅ Tính toán expiry date thông minh với edge cases
- ✅ Kiểm tra thời hạn premium tự động
- ✅ Scheduler chạy định kỳ  
- ✅ Admin dashboard để quản lý
- ✅ Test suite đầy đủ

## 🏗️ Kiến trúc hệ thống

### 1. Model Layer
**PremiumSubscription.java**
- Theo dõi thời hạn premium của user
- Methods: `isActive()`, `isExpired()`, `getDaysRemaining()`
- Tự động tính toán status

### 2. DAO Layer  
**PremiumSubscriptionDAO.java**
- Database operations cho premium subscriptions
- Methods: `getActiveSubscription()`, `getExpiredSubscriptions()`, `updateStatus()`
- Optimized queries với indexing

### 3. Service Layer
**PremiumService.java**
- Business logic chính
- `calculateExpiryDate()` - Tính expiry date thông minh
- `processExpiredSubscriptions()` - Xử lý expired users
- Edge case handling cho ngày không tồn tại

**PremiumExpirationScheduler.java** 
- Scheduler tự động chạy định kỳ
- Chạy daily tại 00:00 (backup check đã tắt)
- Manual trigger cho admin
- Singleton pattern

### 4. Controller Layer
**PremiumManagementServlet.java**
- Admin dashboard để quản lý premium
- Manual trigger expiration checks
- View scheduler status và stats

### 5. Integration
**AppContextListener.java**
- Tự động start/stop scheduler khi app khởi động/tắt
- Integrated vào application lifecycle

## 📅 Logic tính Expiry Date

### Quy tắc cơ bản
- Mỗi lần đăng ký premium kéo dài **1 tháng**
- Tính theo **ngày**, không kể giờ
- Start date + 1 tháng = Expiry date

### Edge Cases được xử lý
```java
// Normal cases
15/1/2025 + 1 tháng = 15/2/2025 ✅
28/2/2025 + 1 tháng = 28/3/2025 ✅

// Edge cases (ngày không tồn tại)
31/1/2025 + 1 tháng = 31/3/2025 ✅ (vì Feb không có 31)
31/3/2025 + 1 tháng = 1/5/2025 ✅ (vì Apr không có 31)
29/1/2024 + 1 tháng = 29/2/2024 ✅ (leap year)

// Year boundary
31/12/2025 + 1 tháng = 31/1/2026 ✅
```

## ⏰ Scheduler System

### Tần suất chạy
1. **Daily Check**: Mỗi ngày lúc 00:00
2. **Immediate Check**: Ngay khi app start (sau 5 giây)
3. **Manual Trigger**: Admin có thể trigger bất kỳ lúc nào

_Backup check (mỗi 6 giờ) đã được tắt theo yêu cầu_

### Quy trình xử lý Expired
1. Tìm tất cả subscriptions có `status='active'` và `expiry_date < today`
2. Update status thành `'expired'`
3. Downgrade user role từ `'premium'` về `'user'`
4. Log processing results

## 🗄️ Database Schema

### PremiumSubscriptions Table
```sql
CREATE TABLE PremiumSubscriptions (
    id int IDENTITY(1,1) PRIMARY KEY,
    user_id int NOT NULL,
    start_date date NOT NULL,
    expiry_date date NOT NULL,
    payment_method varchar(20) NOT NULL, -- 'coin' hoặc 'vnd'
    amount decimal(10,2) NOT NULL,
    status varchar(20) NOT NULL DEFAULT 'active', -- 'active', 'expired', 'cancelled', 'replaced'
    created_at datetime2 NOT NULL DEFAULT GETDATE(),
    updated_at datetime2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_PremiumSubscriptions_Users 
        FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);
```

### Indexes tối ưu
- `IX_PremiumSubscriptions_UserId` 
- `IX_PremiumSubscriptions_ExpiryDate`
- `IX_PremiumSubscriptions_Status`
- `IX_PremiumSubscriptions_UserStatus`

## 🎮 Admin Dashboard

### Truy cập
URL: `/admin/premium-management`
Yêu cầu: Admin role

### Chức năng
- **View Scheduler Status**: Kiểm tra scheduler có đang chạy không
- **Manual Trigger**: Trigger expiration check ngay lập tức
- **View Stats**: Thống kê premium subscriptions (coming soon)
- **Monitor Logs**: Xem logs processing

### Actions
```
?action=status   - JSON status scheduler
?action=trigger  - Manual expiration check  
?action=stats    - Premium statistics
```

## 🧪 Testing

### Test Files
- `PremiumServiceTest.java` - Test logic tính expiry date
- `PremiumExpirationTest.java` - Test toàn bộ system
- `create_premium_subscriptions_table.sql` - Test edge cases

### Run Tests
```bash
# Test expiry date calculation
java com.mycompany.ebookwebsite.TestAI.PremiumServiceTest

# Test toàn bộ system  
java com.mycompany.ebookwebsite.TestAI.PremiumExpirationTest
```

## 🚀 Setup & Deployment

### 1. Database Setup
```sql
-- Chạy file SQL để tạo table
sqlcmd -S server -d database -i create_premium_subscriptions_table.sql
```

### 2. Application Start
Scheduler tự động start khi application khởi động qua `AppContextListener`

### 3. Verify Setup
1. Check logs cho message: "⏰ Premium Expiration Scheduler started successfully!"
2. Truy cập admin dashboard: `/admin/premium-management`
3. Trigger manual check để test

## 📋 API Usage

### Check Premium Status
```java
PremiumService premiumService = new PremiumService();
boolean isPremium = premiumService.isPremiumUser(userId);
```

### Get Premium Info
```java
PremiumSubscription subscription = premiumService.getUserPremiumSubscription(userId);
if (subscription != null) {
    long daysRemaining = subscription.getDaysRemaining();
    LocalDate expiryDate = subscription.getExpiryDate();
}
```

### Create Premium Subscription
```java
try {
    PremiumSubscription newSub = premiumService.createPremiumSubscription(
        userId, "coin", 100.0
    );
    System.out.println("Premium created: " + newSub.getExpiryDate());
} catch (SQLException e) {
    // Handle error
}
```

### Manual Trigger (Admin only)
```java
PremiumExpirationScheduler scheduler = PremiumExpirationScheduler.getInstance();
int processed = scheduler.triggerManualCheck();
System.out.println("Processed: " + processed + " expired subscriptions");
```

## 🔍 Monitoring & Logs

### Log Messages
```
⏰ Premium Expiration Scheduler started successfully!
🔍 Starting premium expiration check at 2025-01-20 00:00:00
✅ Premium expiration check completed: 3 subscriptions processed in 2 seconds
🔧 Manual premium expiration check triggered
❌ Error during premium expiration check
```

### Key Metrics
- Số subscriptions processed mỗi lần chạy
- Thời gian chạy (duration)
- Tần suất lỗi (nếu có)
- Scheduler uptime

## 🛠️ Troubleshooting

### Scheduler không chạy
1. Check logs cho errors khi start
2. Verify database connection
3. Check AppContextListener có được load không

### Database errors
1. Verify table PremiumSubscriptions exists
2. Check foreign key constraints
3. Verify indexes

### Manual trigger fails
1. Check admin permissions
2. Verify database connection
3. Check logs cho specific errors

## 📈 Future Enhancements

### Planned Features
- [ ] Premium statistics dashboard
- [ ] Email notifications trước khi expire
- [ ] Multiple subscription tiers
- [ ] Auto-renewal options
- [ ] Detailed analytics

### Scaling Considerations
- Partitioning cho large datasets
- Caching frequently accessed data
- Distributed scheduling cho multiple servers

## 🔐 Security

### Access Control
- Admin dashboard yêu cầu admin role
- Scheduler chỉ process internal data
- No external API exposure

### Data Protection
- Sensitive data logging minimized
- Database constraints prevent invalid data
- Proper error handling không leak info

---

## 📞 Support

Nếu có vấn đề với hệ thống premium expiration:
1. Check logs đầu tiên
2. Test với manual trigger
3. Verify database state
4. Review scheduler status

**Happy Premium Management! 👑** 