-- ========================================
-- 👑 PREMIUM SUBSCRIPTIONS TABLE CREATION
-- ========================================

PRINT '🚀 Creating PremiumSubscriptions table...';

-- Tạo table PremiumSubscriptions để theo dõi thời hạn premium
CREATE TABLE PremiumSubscriptions (
    id int IDENTITY(1,1) PRIMARY KEY,
    user_id int NOT NULL,
    start_date date NOT NULL,           -- Ngày bắt đầu premium
    expiry_date date NOT NULL,          -- Ngày hết hạn premium
    payment_method varchar(20) NOT NULL, -- 'coin' hoặc 'vnd'
    amount decimal(10,2) NOT NULL,      -- Số tiền hoặc coin đã trả
    status varchar(20) NOT NULL DEFAULT 'active', -- 'active', 'expired', 'cancelled', 'replaced'
    created_at datetime2 NOT NULL DEFAULT GETDATE(),
    updated_at datetime2 NOT NULL DEFAULT GETDATE(),
    
    -- Foreign key constraint
    CONSTRAINT FK_PremiumSubscriptions_Users 
        FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    
    -- Check constraints
    CONSTRAINT CK_PremiumSubscriptions_PaymentMethod 
        CHECK (payment_method IN ('coin', 'vnd')),
    CONSTRAINT CK_PremiumSubscriptions_Status 
        CHECK (status IN ('active', 'expired', 'cancelled', 'replaced')),
    CONSTRAINT CK_PremiumSubscriptions_Amount 
        CHECK (amount > 0),
    CONSTRAINT CK_PremiumSubscriptions_Dates 
        CHECK (expiry_date > start_date)
);

-- Tạo index để tối ưu query
CREATE INDEX IX_PremiumSubscriptions_UserId 
    ON PremiumSubscriptions(user_id);

CREATE INDEX IX_PremiumSubscriptions_ExpiryDate 
    ON PremiumSubscriptions(expiry_date);

CREATE INDEX IX_PremiumSubscriptions_Status 
    ON PremiumSubscriptions(status);

CREATE INDEX IX_PremiumSubscriptions_UserStatus 
    ON PremiumSubscriptions(user_id, status) 
    INCLUDE (expiry_date);

PRINT '✅ PremiumSubscriptions table created successfully!';

-- ========================================
-- 📊 SAMPLE DATA (Optional)
-- ========================================

PRINT '📊 Inserting sample premium subscriptions...';

-- Sample premium subscription (chỉ chạy nếu có users)
IF EXISTS (SELECT 1 FROM Users WHERE id = 1)
BEGIN
    INSERT INTO PremiumSubscriptions (user_id, start_date, expiry_date, payment_method, amount, status)
    VALUES 
    (1, '2025-01-01', '2025-02-01', 'vnd', 100000.00, 'active'),
    (1, '2024-12-01', '2025-01-01', 'coin', 100.00, 'expired');
    
    PRINT '✅ Sample data inserted!';
END
ELSE
BEGIN
    PRINT '⚠️ No users found, skipping sample data insertion';
END

-- ========================================
-- 🧪 TEST EXPIRY DATE CALCULATION
-- ========================================

PRINT '🧪 Testing expiry date calculation edge cases...';

-- Test cases cho edge dates
DECLARE @TestResults TABLE (
    StartDate date,
    CalculatedExpiry date,
    ExpectedBehavior varchar(100)
);

-- Test normal cases
INSERT INTO @TestResults VALUES ('2025-01-15', '2025-02-15', 'Normal case: add 1 month');
INSERT INTO @TestResults VALUES ('2025-02-28', '2025-03-28', 'Normal case: February to March');

-- Test edge cases (these would be handled by PremiumService logic)
INSERT INTO @TestResults VALUES ('2025-01-31', '2025-03-01', 'Edge case: Jan 31 -> March 1 (Feb 31 does not exist)');
INSERT INTO @TestResults VALUES ('2025-03-31', '2025-05-01', 'Edge case: Mar 31 -> May 1 (Apr 31 does not exist)');
INSERT INTO @TestResults VALUES ('2024-01-31', '2024-03-01', 'Edge case: Jan 31 -> March 1 (leap year)');

SELECT 
    StartDate,
    CalculatedExpiry,
    ExpectedBehavior,
    CASE 
        WHEN DATEADD(month, 1, StartDate) = CalculatedExpiry THEN '✅ Standard calculation'
        ELSE '⚠️ Edge case handling required'
    END AS CalculationNote
FROM @TestResults;

-- ========================================
-- 📝 USAGE EXAMPLES
-- ========================================

PRINT '📝 Usage examples:';
PRINT '';
PRINT '-- Kiểm tra premium status của user:';
PRINT 'SELECT * FROM PremiumSubscriptions WHERE user_id = 1 AND status = ''active'' AND expiry_date >= CAST(GETDATE() AS date);';
PRINT '';
PRINT '-- Tìm subscription expired:';
PRINT 'SELECT * FROM PremiumSubscriptions WHERE status = ''active'' AND expiry_date < CAST(GETDATE() AS date);';
PRINT '';
PRINT '-- Thống kê premium users:';
PRINT 'SELECT COUNT(*) as ActivePremiumUsers FROM PremiumSubscriptions WHERE status = ''active'' AND expiry_date >= CAST(GETDATE() AS date);';

PRINT '';
PRINT '🎉 Premium Subscriptions system setup completed!';
PRINT '========================================'; 