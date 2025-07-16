-- ========================================
-- Data Verification Script
-- Generated on: 2025-07-14 00:55:14
-- Run this before adding foreign key constraints
-- ========================================

USE EbookWebsitePRJ;
GO

PRINT 'Verifying data integrity...';

-- Check Authors table
SELECT 'Authors' as TableName, COUNT(*) as RecordCount FROM Authors;
SELECT TOP 10 * FROM Authors ORDER BY id;

-- Check Ebooks table
SELECT 'Ebooks' as TableName, COUNT(*) as RecordCount FROM Ebooks;
SELECT TOP 10 * FROM Ebooks ORDER BY id;

-- Check EbookAuthors table
SELECT 'EbookAuthors' as TableName, COUNT(*) as RecordCount FROM EbookAuthors;
SELECT TOP 10 * FROM EbookAuthors ORDER BY ebook_id, author_id;

-- Check for invalid author_id in EbookAuthors
SELECT 'Invalid author_id in EbookAuthors' as Issue, 
       ea.author_id, ea.ebook_id
FROM EbookAuthors ea
LEFT JOIN Authors a ON ea.author_id = a.id
WHERE a.id IS NULL;

-- Check for invalid ebook_id in EbookAuthors
SELECT 'Invalid ebook_id in EbookAuthors' as Issue, 
       ea.author_id, ea.ebook_id
FROM EbookAuthors ea
LEFT JOIN Ebooks e ON ea.ebook_id = e.id
WHERE e.id IS NULL;

-- Check Tags table
SELECT 'Tags' as TableName, COUNT(*) as RecordCount FROM Tags;
SELECT TOP 10 * FROM Tags ORDER BY id;

-- Check EbookTags table
SELECT 'EbookTags' as TableName, COUNT(*) as RecordCount FROM EbookTags;
SELECT TOP 10 * FROM EbookTags ORDER BY ebook_id, tag_id;

-- Check for invalid tag_id in EbookTags
SELECT 'Invalid tag_id in EbookTags' as Issue, 
       et.tag_id, et.ebook_id
FROM EbookTags et
LEFT JOIN Tags t ON et.tag_id = t.id
WHERE t.id IS NULL;

-- Check for invalid ebook_id in EbookTags
SELECT 'Invalid ebook_id in EbookTags' as Issue, 
       et.tag_id, et.ebook_id
FROM EbookTags et
LEFT JOIN Ebooks e ON et.ebook_id = e.id
WHERE e.id IS NULL;

-- Check Chapters table
SELECT 'Chapters' as TableName, COUNT(*) as RecordCount FROM Chapters;
SELECT TOP 10 * FROM Chapters ORDER BY ebook_id, number;

-- Check for invalid ebook_id in Chapters
SELECT 'Invalid ebook_id in Chapters' as Issue, 
       c.id, c.ebook_id, c.title
FROM Chapters c
LEFT JOIN Ebooks e ON c.ebook_id = e.id
WHERE e.id IS NULL;

PRINT 'Data verification completed!';
