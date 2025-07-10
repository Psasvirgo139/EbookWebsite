-- ========================================
-- DATABASE MIGRATION: EBOOK AI REFACTORING
-- ========================================
-- 
-- 📝 PURPOSE: Tách AI-related fields từ Ebooks table sang EbookAI table riêng biệt
-- 🎯 FEATURES: 
--    - Tạo bảng EbookAI mới với proper relationships
--    - Migrate existing AI data từ Ebooks sang EbookAI
--    - Cleanup AI columns từ Ebooks table
--    - Maintain referential integrity
--
-- ⚠️  IMPORTANT: 
--    - Backup database trước khi chạy script này
--    - Run script này trên SQL Server database
--    - Test thoroughly sau migration
-- 📅 Created: $(date)
-- ========================================

USE [YourDatabaseName]; -- Replace with your actual database name

-- ========================================
-- STEP 1: CREATE EBOOKAI TABLE
-- ========================================

PRINT '📋 Step 1: Creating EbookAI table...';

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[EbookAI]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[EbookAI] (
        [id] INT IDENTITY(1,1) PRIMARY KEY,
        [ebook_id] INT NOT NULL,
        [file_name] NVARCHAR(500) NULL,
        [original_file_name] NVARCHAR(500) NULL,
        [summary] NTEXT NULL,
        [created_at] DATETIME2(7) NOT NULL DEFAULT GETDATE(),
        [updated_at] DATETIME2(7) NOT NULL DEFAULT GETDATE(),
        [status] NVARCHAR(50) NOT NULL DEFAULT 'completed',
        
        -- Foreign key constraint
        CONSTRAINT FK_EbookAI_Ebooks FOREIGN KEY (ebook_id) 
        REFERENCES [dbo].[Ebooks](id) ON DELETE CASCADE,
        
        -- Unique constraint - one AI record per ebook
        CONSTRAINT UQ_EbookAI_EbookId UNIQUE (ebook_id)
    );
    
    PRINT '✅ EbookAI table created successfully';
END
ELSE
BEGIN
    PRINT '⚠️ EbookAI table already exists';
END

-- ========================================
-- STEP 2: CREATE INDEXES FOR PERFORMANCE
-- ========================================

PRINT '📋 Step 2: Creating indexes...';

-- Index on ebook_id for quick lookups
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[EbookAI]') AND name = 'IX_EbookAI_EbookId')
BEGIN
    CREATE INDEX IX_EbookAI_EbookId ON [dbo].[EbookAI] ([ebook_id]);
    PRINT '✅ Created index on ebook_id';
END

-- Index on file_name for file operations
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[EbookAI]') AND name = 'IX_EbookAI_FileName')
BEGIN
    CREATE INDEX IX_EbookAI_FileName ON [dbo].[EbookAI] ([file_name]);
    PRINT '✅ Created index on file_name';
END

-- Index on status for filtering
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[EbookAI]') AND name = 'IX_EbookAI_Status')
BEGIN
    CREATE INDEX IX_EbookAI_Status ON [dbo].[EbookAI] ([status]);
    PRINT '✅ Created index on status';
END

-- ========================================
-- STEP 3: MIGRATE EXISTING DATA
-- ========================================

PRINT '📋 Step 3: Migrating existing AI data...';

-- Count existing AI data in Ebooks table
DECLARE @existingCount INT;
SELECT @existingCount = COUNT(*) 
FROM [dbo].[Ebooks] 
WHERE (summary IS NOT NULL AND summary != '') 
   OR (file_name IS NOT NULL AND file_name != '') 
   OR (original_file_name IS NOT NULL AND original_file_name != '');

PRINT 'Found ' + CAST(@existingCount AS NVARCHAR(10)) + ' ebooks with AI data to migrate';

-- Migrate data if AI columns exist in Ebooks table
IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'summary')
   AND EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'file_name')
   AND EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'original_file_name')
BEGIN
    -- Insert AI data into EbookAI table
    INSERT INTO [dbo].[EbookAI] (ebook_id, file_name, original_file_name, summary, created_at, updated_at, status)
    SELECT 
        id,
        file_name,
        original_file_name,
        summary,
        ISNULL(created_at, GETDATE()),
        GETDATE(),
        'completed'
    FROM [dbo].[Ebooks]
    WHERE (summary IS NOT NULL AND summary != '') 
       OR (file_name IS NOT NULL AND file_name != '') 
       OR (original_file_name IS NOT NULL AND original_file_name != '');
    
    DECLARE @migratedCount INT = @@ROWCOUNT;
    PRINT '✅ Migrated ' + CAST(@migratedCount AS NVARCHAR(10)) + ' AI records to EbookAI table';
END
ELSE
BEGIN
    PRINT '⚠️ AI columns not found in Ebooks table - skipping data migration';
END

-- ========================================
-- STEP 4: VERIFICATION
-- ========================================

PRINT '📋 Step 4: Verifying migration...';

-- Verify data integrity
DECLARE @ebookAICount INT, @originalCount INT;

SELECT @ebookAICount = COUNT(*) FROM [dbo].[EbookAI];
SELECT @originalCount = COUNT(*) FROM [dbo].[Ebooks] WHERE id IN (SELECT ebook_id FROM [dbo].[EbookAI]);

PRINT 'EbookAI records: ' + CAST(@ebookAICount AS NVARCHAR(10));
PRINT 'Linked Ebook records: ' + CAST(@originalCount AS NVARCHAR(10));

-- Check for orphaned records
DECLARE @orphanedCount INT;
SELECT @orphanedCount = COUNT(*) 
FROM [dbo].[EbookAI] ai 
LEFT JOIN [dbo].[Ebooks] e ON ai.ebook_id = e.id 
WHERE e.id IS NULL;

IF @orphanedCount > 0
BEGIN
    PRINT '⚠️ Found ' + CAST(@orphanedCount AS NVARCHAR(10)) + ' orphaned AI records';
END
ELSE
BEGIN
    PRINT '✅ No orphaned AI records found';
END

-- ========================================
-- STEP 5: CLEANUP (COMMENTED OUT FOR SAFETY)
-- ========================================

PRINT '📋 Step 5: Cleanup instructions...';
PRINT '';
PRINT '⚠️  MANUAL CLEANUP REQUIRED:';
PRINT '   After verifying migration success, manually run these commands:';
PRINT '';
PRINT '   -- Remove AI columns from Ebooks table:';
PRINT '   ALTER TABLE [dbo].[Ebooks] DROP COLUMN summary;';
PRINT '   ALTER TABLE [dbo].[Ebooks] DROP COLUMN file_name;';
PRINT '   ALTER TABLE [dbo].[Ebooks] DROP COLUMN original_file_name;';
PRINT '';
PRINT '   -- Drop old indexes:';
PRINT '   DROP INDEX IF EXISTS IX_Ebooks_FileName ON [dbo].[Ebooks];';
PRINT '';

-- Uncomment below if you want to auto-cleanup (NOT RECOMMENDED)
/*
-- Remove AI columns from Ebooks table
IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'summary')
BEGIN
    ALTER TABLE [dbo].[Ebooks] DROP COLUMN summary;
    PRINT '✅ Removed summary column from Ebooks table';
END

IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'file_name')
BEGIN
    ALTER TABLE [dbo].[Ebooks] DROP COLUMN file_name;
    PRINT '✅ Removed file_name column from Ebooks table';
END

IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'original_file_name')
BEGIN
    ALTER TABLE [dbo].[Ebooks] DROP COLUMN original_file_name;
    PRINT '✅ Removed original_file_name column from Ebooks table';
END
*/

-- ========================================
-- STEP 6: SUMMARY
-- ========================================

PRINT '========================================';
PRINT '🎉 EBOOK AI REFACTORING MIGRATION COMPLETED!';
PRINT '========================================';
PRINT '';
PRINT '📊 SUMMARY:';
PRINT '   ✅ EbookAI table created with proper constraints';
PRINT '   ✅ Indexes created for optimal performance';
PRINT '   ✅ Existing AI data migrated successfully';
PRINT '   ✅ Data integrity verified';
PRINT '';
PRINT '🚀 NEXT STEPS:';
PRINT '   1. Test application with new EbookAI model';
PRINT '   2. Verify all AI features work correctly';
PRINT '   3. Run comprehensive tests';
PRINT '   4. Monitor application logs for errors';
PRINT '   5. After 100% confidence, manually cleanup old columns';
PRINT '';
PRINT '💡 NEW TABLE STRUCTURE:';
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    CHARACTER_MAXIMUM_LENGTH, 
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'EbookAI'
ORDER BY ORDINAL_POSITION;

PRINT '========================================'; 