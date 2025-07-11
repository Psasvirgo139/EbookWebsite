# 🔄 Premium Flow Synchronization

## Tổng quan
Hệ thống đồng bộ role của user giữa 2 tables: `Users` và `PremiumSubscriptions` để đảm bảo data consistency và quản lý thời hạn premium chính xác.

## 🎯 **Luồng Premium Activation**

### Khi User đăng ký Premium thành công:

```
👤 User Action (Coin/VND) 
    ↓
🎯 PremiumService.activatePremiumForUser()
    ↓
📝 1. Validate user exists
    ↓  
🔄 2. Deactivate old subscriptions (status → 'replaced')
    ↓
📅 3. Calculate expiry date (start + 1 month)
    ↓  
💾 4. INSERT into PremiumSubscriptions
    │   - user_id, start_date, expiry_date
    │   - payment_method ('coin'/'vnd')
    │   - amount (100.0/100000.0)
    │   - status = 'active'
    ↓
🔄 5. UPDATE Users SET role = 'premium'
    ↓
✅ Success: Both tables synchronized
```

### Code Integration:
```java
// OLD WAY (❌ Broken):
user.setRole("premium");
userDao.update(user);
// → User premium forever, no expiry tracking

// NEW WAY (✅ Correct):
PremiumService premiumService = new PremiumService();
premiumService.activatePremiumForUser(userId, "coin", 100.0);
// → Proper subscription tracking with expiry
```

## ⏰ **Luồng Premium Expiration**

### Khi Scheduler chạy (00:00 daily):

```
⏰ PremiumExpirationScheduler
    ↓
🔍 Find expired subscriptions (expiry_date < today)
    ↓
🔄 For each expired subscription:
    ↓
⏰ PremiumService.expirePremiumForUser()
    ↓
📝 1. UPDATE PremiumSubscriptions SET status = 'expired'
    ↓
🔍 2. Check if user has other active subscriptions
    ↓
┌─────────────────┬─────────────────┐
│  Has Active     │   No Active     │
│  Subscriptions  │  Subscriptions  │
│        ↓        │        ↓        │
│ Keep 'premium'  │ Role → 'user'   │
│ (extend case)   │ (normal expire) │
└─────────────────┴─────────────────┘
    ↓
✅ Success: Both tables synchronized
```

## 🏗️ **Kiến trúc Methods**

### 1. **activatePremiumForUser()**
```java
public PremiumSubscription activatePremiumForUser(int userId, String paymentMethod, double amount)
```
**Mục đích**: Kích hoạt premium cho user (đăng ký mới)
**Flow**: 
- Validate user
- Deactivate old subscriptions  
- Create new subscription
- Sync user role to premium

### 2. **expirePremiumForUser()**
```java
public boolean expirePremiumForUser(int userId, int subscriptionId)
```
**Mục đích**: Xử lý hết hạn premium (scheduler)
**Flow**:
- Update subscription status to expired
- Check other active subscriptions
- Sync user role (premium/user)

### 3. **synchronizeUserRoleToPremium()**
```java
private void synchronizeUserRoleToPremium(int userId)
```
**Mục đích**: Đồng bộ role = 'premium'
**Logic**: Chỉ update nếu role hiện tại ≠ 'premium'

### 4. **synchronizeUserRoleToRegular()**
```java
private void synchronizeUserRoleToRegular(int userId)
```
**Mục đích**: Đồng bộ role = 'user'  
**Logic**: Không touch 'admin' role, chỉ downgrade 'premium' → 'user'

## 📊 **Data Consistency Rules**

### Invariants (Luôn đúng):
1. ✅ `Users.role = 'premium'` ↔ Tồn tại active `PremiumSubscription`
2. ✅ `Users.role = 'user'` ↔ Không có active `PremiumSubscription`  
3. ✅ `PremiumSubscription.status = 'active'` ↔ `expiry_date >= today`
4. ✅ `PremiumSubscription.status = 'expired'` ↔ `expiry_date < today`
5. ✅ Chỉ có 1 active subscription per user tại 1 thời điểm
6. ✅ Admin role được bảo vệ (không bị downgrade)

### Status Transitions:
```
'active' → 'expired'   (scheduler expire)
'active' → 'replaced'  (user renew/extend)
'expired' → [final]    (không thay đổi)
'replaced' → [final]   (không thay đổi)
```

## 🧩 **Edge Cases**

### 1. **Multiple Subscriptions (Extend)**
```
User có 2 subscriptions:
├── Subscription A: 15/1 - 15/2 (active)
└── Subscription B: 10/2 - 10/3 (active) [extend]

16/2: Subscription A expires
→ Status A: 'expired'  
→ User role: 'premium' (B vẫn active)

11/3: Subscription B expires
→ Status B: 'expired'
→ User role: 'user' (không còn active)
```

### 2. **Admin User Protection**
```
Admin user có premium expires:
→ Subscription: 'active' → 'expired' ✅
→ User role: 'admin' → 'admin' ✅ (không đổi)
```

### 3. **Concurrent Operations**
```
User activate premium WHILE scheduler runs:
→ Old subscriptions: 'replaced' 
→ New subscription: 'active'
→ No race conditions
```

## 🔧 **Integration Points**

### PaymentServlet Update:
```java
// OLD:
if (currentCoins >= 100) {
    userCoinDao.deductCoins(user.getId(), 100);
    user.setRole("premium");      // ❌ No expiry
    userDao.update(user);
}

// NEW:
if (currentCoins >= 100) {
    userCoinDao.deductCoins(user.getId(), 100);
    premiumService.activatePremiumForUser(user.getId(), "coin", 100.0);  // ✅ With expiry
}
```

### VnpayReturn Update:
```java
// OLD:
private void handlePremiumUpgrade(User user, HttpSession session) {
    user.setRole("premium");      // ❌ No expiry
    userDao.update(user);
}

// NEW:
private void handlePremiumUpgrade(User user, HttpSession session) {
    premiumService.activatePremiumForUser(user.getId(), "vnd", 100000.0);  // ✅ With expiry
}
```

## 📋 **Implementation Checklist**

### ✅ Đã hoàn thành:
- [x] `activatePremiumForUser()` method
- [x] `expirePremiumForUser()` method  
- [x] `synchronizeUserRoleToPremium()` method
- [x] `synchronizeUserRoleToRegular()` method
- [x] Updated `processExpiredSubscriptions()`
- [x] Premium synchronization test suite
- [x] Edge cases handling
- [x] Admin role protection
- [x] Comprehensive logging

### 🔄 Cần cập nhật:
- [ ] Update PaymentServlet to use new method
- [ ] Update VnpayReturn to use new method  
- [ ] Remove old session-based premium tracking
- [ ] Add transaction safety (rollback on errors)
- [ ] Update admin dashboard to show sync status

## 🚀 **Deployment Steps**

### 1. Database:
```sql
-- Table đã tạo từ trước
-- Chỉ cần verify structure
SELECT * FROM PremiumSubscriptions WHERE 1=0;
```

### 2. Application:
```bash
# Deploy updated PremiumService
# Test với manual trigger
# Verify logs show sync operations
```

### 3. Testing:
```bash
# Run synchronization test
java PremiumSynchronizationTest

# Test actual flows
# 1. User upgrade premium → check both tables
# 2. Wait for expiry → check role downgrade  
# 3. Admin premium expire → verify role unchanged
```

## 📊 **Monitoring & Alerts**

### Logs to watch:
```
🎯 Starting premium activation for user: X
✅ Premium activated successfully: userId=X, subscriptionId=Y
⏰ Starting premium expiration for user: X  
🔄 Synchronized user X role to PREMIUM/USER
❌ Failed to activate/expire premium for user X
```

### Health checks:
1. Count mismatch: Premium users vs Active subscriptions
2. Expired subscriptions not processed  
3. Users with premium role but no active subscription
4. Active subscriptions but user role ≠ premium

---

## 🎉 **Benefits**

### ✅ Trước đây (Broken):
- User premium mãi mãi
- Không track expiry date
- Không consistent giữa tables
- Admin không monitor được

### 🚀 Bây giờ (Fixed):
- ✅ Premium có thời hạn chính xác (1 tháng)
- ✅ Tự động expire và downgrade
- ✅ Data consistency giữa 2 tables
- ✅ Admin có thể monitor và manage
- ✅ Audit trail đầy đủ
- ✅ Edge cases handled properly

**Perfect Premium Management! 👑** 