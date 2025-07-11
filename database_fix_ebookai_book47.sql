-- ========================================
-- FIX SCRIPT: EBOOKAI DATA FOR BOOK 47
-- ========================================
-- 
-- 📝 PURPOSE: Khắc phục dữ liệu EbookAI cho sách "Nhà Thờ Đức Bà Paris" (ID: 47)
-- 🎯 APPROACH: Sử dụng trực tiếp file_name và original_file_name
-- 📅 Created: 2025-01-13
-- 🔧 FIXED: Cập nhật đúng file name từ uploads directory
-- ========================================

USE [EbookWebsite]; -- Thay thế bằng tên database thực tế

PRINT '🔧 EBOOKAI FIX SCRIPT - STARTING...';
PRINT '========================================';

-- ========================================
-- STEP 1: KIỂM TRA SÁCH BOOK 47
-- ========================================

PRINT 'Step 1: Checking book 47...';

DECLARE @bookExists INT = 0;
DECLARE @bookTitle NVARCHAR(500) = '';

SELECT @bookExists = COUNT(*), @bookTitle = MAX(title) 
FROM [Ebooks]
WHERE id = 47;

IF @bookExists = 0
BEGIN
    PRINT '❌ Book 47 không tồn tại!';
    RETURN;
END

PRINT '✅ Book 47 tồn tại: ' + @bookTitle;

-- ========================================
-- STEP 2: KIỂM TRA/TẠO BẢNG EBOOKAI
-- ========================================

PRINT 'Step 2: Checking EbookAI table...';

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EbookAI')
BEGIN
    PRINT '📋 Creating EbookAI table...';
    
    CREATE TABLE EbookAI (
        id INT IDENTITY(1,1) PRIMARY KEY,
        ebook_id INT NOT NULL,
        file_name NVARCHAR(255),
        original_file_name NVARCHAR(255),
        summary NVARCHAR(MAX),
        metadata_json NVARCHAR(MAX),
        ai_analysis_json NVARCHAR(MAX),
        filterStatus NVARCHAR(50),
        filterMessage NVARCHAR(255),
        rejection_reason NVARCHAR(255),
        file_size BIGINT,
        file_path NVARCHAR(MAX),
        dropbox_link NVARCHAR(MAX),
        dropbox_file_id NVARCHAR(255),
        status NVARCHAR(50) DEFAULT 'pending',
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME,
        FOREIGN KEY (ebook_id) REFERENCES Ebooks(id)
    );
    
    PRINT '✅ EbookAI table created successfully';
END
ELSE
BEGIN
    PRINT '✅ EbookAI table already exists';
END

-- ========================================
-- STEP 3: KIỂM TRA RECORD BOOK 47
-- ========================================

PRINT 'Step 3: Checking EbookAI record for book 47...';

DECLARE @aiRecordExists INT = 0;
DECLARE @currentFileName NVARCHAR(255) = '';
DECLARE @currentSummary NVARCHAR(MAX) = '';

SELECT @aiRecordExists = COUNT(*), 
       @currentFileName = MAX(file_name),
       @currentSummary = MAX(summary)
FROM [EbookAI]
WHERE ebook_id = 47;

IF @aiRecordExists = 0
BEGIN
    PRINT '📝 Tạo EbookAI record mới cho book 47...';
    
    INSERT INTO [EbookAI] (
        ebook_id, 
        file_name, 
        original_file_name, 
        summary,
        status,
        created_at
    ) VALUES (
        47,
        'Nhà Thờ Đức Bà Paris.pdf',
        'Nhà Thờ Đức Bà Paris.pdf',
        'Chua có tóm tắt AI. Sẽ được tạo khi đọc sách lần đầu.',
        'pending',
        GETDATE()
    );
    
    PRINT '✅ EbookAI record created for book 47';
END
ELSE
BEGIN
    PRINT '✅ EbookAI record already exists for book 47';
    PRINT '📁 Current file_name: ' + ISNULL(@currentFileName, 'NULL');
    PRINT '📝 Current summary: ' + CASE 
        WHEN @currentSummary IS NULL THEN 'NULL' 
        WHEN LEN(@currentSummary) = 0 THEN 'EMPTY'
        ELSE 'EXISTS (' + CAST(LEN(@currentSummary) AS NVARCHAR(10)) + ' chars)'
    END;
END

-- ========================================
-- STEP 4: CẬP NHẬT FILE NAME CHÍNH XÁC
-- ========================================

PRINT 'Step 4: Updating correct file names...';

UPDATE [EbookAI] 
SET file_name = 'Nhà Thờ Đức Bà Paris.pdf',
    original_file_name = 'Nhà Thờ Đức Bà Paris.pdf',
    updated_at = GETDATE()
WHERE ebook_id = 47;

PRINT '✅ File names updated successfully';

-- ========================================
-- STEP 5: CLEAR EXISTING SUMMARY (để tạo lại)
-- ========================================

PRINT 'Step 5: Clearing existing summary to regenerate...';

UPDATE [EbookAI] 
SET summary = NULL,
    updated_at = GETDATE()
WHERE ebook_id = 47;

PRINT '✅ Summary cleared - will be regenerated when reading book';

-- ========================================
-- STEP 6: VERIFY FINAL RESULT
-- ========================================

PRINT 'Step 6: Verifying final result...';

SELECT 
    e.id AS book_id,
    e.title AS book_title,
    ai.file_name,
    ai.original_file_name,
    CASE 
        WHEN ai.summary IS NULL THEN 'NULL - WILL BE GENERATED' 
        WHEN LEN(ai.summary) = 0 THEN 'EMPTY'
        ELSE 'EXISTS (' + CAST(LEN(ai.summary) AS NVARCHAR(10)) + ' chars)'
    END AS summary_status,
    ai.status,
    ai.created_at,
    ai.updated_at
FROM [Ebooks] e
JOIN [EbookAI] ai ON e.id = ai.ebook_id
WHERE e.id = 47;

PRINT '========================================';
PRINT '🎉 EBOOKAI FIX SCRIPT COMPLETED!';
PRINT '========================================';
PRINT '';
PRINT '📋 NEXT STEPS:';
PRINT '1. Restart Tomcat server';
PRINT '2. Access book 47: http://localhost:9999/EbookWebsite/book/read?id=47';
PRINT '3. AI summary sẽ được tạo tự động khi đọc sách';
PRINT '4. Kiểm tra console logs để debug nếu còn lỗi';
PRINT ''; 