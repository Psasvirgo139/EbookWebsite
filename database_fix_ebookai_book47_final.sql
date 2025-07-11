-- ========================================
-- FINAL FIX SCRIPT: EBOOKAI BOOK 47
-- ========================================
-- 
-- 📝 PURPOSE: Sửa dữ liệu EbookAI cho book 47 dựa trên database hiện tại
-- 🎯 ISSUES FOUND:
--    - summary = "Test summary from debug tool" (cần xóa để tạo lại)
--    - file_path có thể cần điều chỉnh
-- 📅 Created: 2025-01-13
-- ========================================

USE [EBookWebsite]; -- Đúng tên database của bạn

PRINT '🔧 FINAL FIX SCRIPT FOR BOOK 47 - STARTING...';
PRINT '========================================';

-- ========================================
-- STEP 1: VERIFY CURRENT STATE
-- ========================================

PRINT 'Step 1: Verifying current state...';

SELECT 
    id,
    ebook_id,
    file_name,
    original_file_name,
    LEFT(summary, 50) + '...' AS summary_preview,
    file_path,
    status,
    updated_at
FROM [EbookAI]
WHERE ebook_id = 47;

-- ========================================
-- STEP 2: CLEAR TEST SUMMARY
-- ========================================

PRINT 'Step 2: Clearing test summary to regenerate real summary...';

UPDATE [EbookAI] 
SET 
    summary = NULL,
    file_path = NULL,  -- Clear để servlet tự tìm
    updated_at = GETDATE()
WHERE ebook_id = 47;

PRINT '✅ Test summary cleared - AI sẽ tạo summary thật khi đọc sách';

-- ========================================
-- STEP 3: VERIFY FINAL STATE
-- ========================================

PRINT 'Step 3: Verifying final state...';

SELECT 
    id,
    ebook_id,
    file_name,
    original_file_name,
    CASE 
        WHEN summary IS NULL THEN 'NULL - SẼ TẠO KHI ĐỌC SÁCH' 
        ELSE LEFT(summary, 50) + '...'
    END AS summary_status,
    file_path,
    status,
    updated_at
FROM [EbookAI]
WHERE ebook_id = 47;

-- ========================================
-- STEP 4: VERIFY FILE EXISTS
-- ========================================

PRINT 'Step 4: Instructions to verify file exists...';

PRINT '========================================';
PRINT '🎉 FINAL FIX COMPLETED!';
PRINT '========================================';
PRINT '';
PRINT '📋 WHAT WAS FIXED:';
PRINT '   ✅ Cleared test summary - AI sẽ tạo summary thật';
PRINT '   ✅ Cleared file_path - servlet sẽ tự tìm file';
PRINT '   ✅ Giữ nguyên file_name và original_file_name';
PRINT '';
PRINT '🚀 NEXT STEPS:';
PRINT '   1. Restart Tomcat server';
PRINT '   2. Truy cập: http://localhost:9999/EbookWebsite/book/read?id=47';
PRINT '   3. AI sẽ tạo summary thật từ 161,909 characters nội dung sách';
PRINT '   4. Kiểm tra console logs để debug';
PRINT '';
PRINT '📁 FILE EXPECTED:';
PRINT '   D:\EbookWebsite\uploads\Nhà Thờ Đức Bà Paris.pdf';
PRINT '   Size: 502,702 bytes';
PRINT '   Content: 161,909 characters';
PRINT '';
PRINT '🔍 DEBUG INFO:';
PRINT '   - getUploadsPath() sẽ tìm D:\EbookWebsite\uploads';
PRINT '   - readBookContent() sẽ đọc file từ file_name';
PRINT '   - OpenAI sẽ tạo summary từ nội dung thật';
PRINT '   - Summary sẽ được lưu vào database';
PRINT '========================================'; 