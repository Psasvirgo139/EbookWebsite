-- ========================================
-- DATABASE MIGRATION FOR AI SUMMARY FEATURE
-- ========================================
-- 
-- 📝 PURPOSE: Add AI summary and file management columns to Ebooks table
-- 🎯 FEATURES: 
--    - AI-generated summary storage
--    - File path management for content reading
--    - Original filename preservation
--
-- ⚠️  IMPORTANT: Run this script on your SQL Server database
-- 📅 Created: $(date)
-- ========================================

USE [YourDatabaseName]; -- Replace with your actual database name

-- Check if columns already exist before adding them
-- This prevents errors if migration is run multiple times

-- Add summary column for AI-generated summaries
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'summary')
BEGIN
    ALTER TABLE [dbo].[Ebooks] 
    ADD [summary] NTEXT NULL;
    PRINT '✅ Added summary column to Ebooks table';
END
ELSE
BEGIN
    PRINT '⚠️ Summary column already exists in Ebooks table';
END

-- Add file_name column for actual file path/name
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'file_name')
BEGIN
    ALTER TABLE [dbo].[Ebooks] 
    ADD [file_name] NVARCHAR(500) NULL;
    PRINT '✅ Added file_name column to Ebooks table';
END
ELSE
BEGIN
    PRINT '⚠️ File_name column already exists in Ebooks table';
END

-- Add original_file_name column for preserving original upload name
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'original_file_name')
BEGIN
    ALTER TABLE [dbo].[Ebooks] 
    ADD [original_file_name] NVARCHAR(500) NULL;
    PRINT '✅ Added original_file_name column to Ebooks table';
END
ELSE
BEGIN
    PRINT '⚠️ Original_file_name column already exists in Ebooks table';
END

-- Add indexes for better performance
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'IX_Ebooks_FileName')
BEGIN
    CREATE INDEX IX_Ebooks_FileName ON [dbo].[Ebooks] ([file_name]);
    PRINT '✅ Created index on file_name column';
END
ELSE
BEGIN
    PRINT '⚠️ Index on file_name already exists';
END

-- Update existing records to have default values
UPDATE [dbo].[Ebooks] 
SET [summary] = 'Chưa có tóm tắt AI. Sẽ được tạo khi đọc sách lần đầu.'
WHERE [summary] IS NULL;

PRINT '✅ Updated existing records with default summary';

-- Show current table structure
PRINT '📊 Current Ebooks table structure:';
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    CHARACTER_MAXIMUM_LENGTH, 
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Ebooks'
ORDER BY ORDINAL_POSITION;

PRINT '========================================';
PRINT '🎉 AI Summary Migration Completed Successfully!';
PRINT '========================================';
PRINT '';
PRINT '📋 WHAT WAS ADDED:';
PRINT '   ✅ summary (NTEXT) - For AI-generated summaries';
PRINT '   ✅ file_name (NVARCHAR(500)) - For file path management';  
PRINT '   ✅ original_file_name (NVARCHAR(500)) - For original names';
PRINT '   ✅ Index on file_name for performance';
PRINT '';
PRINT '🚀 NEXT STEPS:';
PRINT '   1. Restart your application server';
PRINT '   2. Test AI summary generation by reading a book';
PRINT '   3. Check book detail pages for AI summary display';
PRINT '   4. Verify summary persistence in database';
PRINT '';
PRINT '💡 USAGE:';
PRINT '   - AI summaries will be auto-generated when reading full books';
PRINT '   - Summaries are displayed in book detail and reading pages';
PRINT '   - Admin can edit summaries via the edit summary feature';
PRINT '   - All summaries are persisted to database';
PRINT '========================================'; 