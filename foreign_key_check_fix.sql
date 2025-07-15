-- ========================================
-- Foreign Key Constraint Check and Fix Script
-- Generated on: 2025-07-14 00:55:14
-- Use this script to check and fix foreign key issues
-- ========================================

USE EbookWebsitePRJ;
GO

-- ========================================
-- CHECK FOR ORPHANED RECORDS
-- ========================================

PRINT 'Checking for orphaned records...';

-- Check EbookAuthors -> Authors
SELECT 'EbookAuthors -> Authors' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookAuthors ea
LEFT JOIN Authors a ON ea.author_id = a.id
WHERE a.id IS NULL;

-- Check EbookAuthors -> Ebooks
SELECT 'EbookAuthors -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookAuthors ea
LEFT JOIN Ebooks e ON ea.ebook_id = e.id
WHERE e.id IS NULL;

-- Check EbookTags -> Tags
SELECT 'EbookTags -> Tags' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookTags et
LEFT JOIN Tags t ON et.tag_id = t.id
WHERE t.id IS NULL;

-- Check EbookTags -> Ebooks
SELECT 'EbookTags -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookTags et
LEFT JOIN Ebooks e ON et.ebook_id = e.id
WHERE e.id IS NULL;

-- Check Chapters -> Ebooks
SELECT 'Chapters -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM Chapters c
LEFT JOIN Ebooks e ON c.ebook_id = e.id
WHERE e.id IS NULL;

-- Check Chapters -> Volumes
SELECT 'Chapters -> Volumes' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM Chapters c
LEFT JOIN Volumes v ON c.volume_id = v.id
WHERE v.id IS NULL AND c.volume_id IS NOT NULL;

-- ========================================
-- FIX ORPHANED RECORDS
-- ========================================

PRINT 'Fixing orphaned records...';

-- Delete orphaned EbookAuthors records
DELETE ea
FROM EbookAuthors ea
LEFT JOIN Authors a ON ea.author_id = a.id
LEFT JOIN Ebooks e ON ea.ebook_id = e.id
WHERE a.id IS NULL OR e.id IS NULL;

-- Delete orphaned EbookTags records
DELETE et
FROM EbookTags et
LEFT JOIN Tags t ON et.tag_id = t.id
LEFT JOIN Ebooks e ON et.ebook_id = e.id
WHERE t.id IS NULL OR e.id IS NULL;

-- Delete orphaned Chapters records
DELETE c
FROM Chapters c
LEFT JOIN Ebooks e ON c.ebook_id = e.id
WHERE e.id IS NULL;

-- Delete orphaned Chapters records (volume_id)
DELETE c
FROM Chapters c
LEFT JOIN Volumes v ON c.volume_id = v.id
WHERE v.id IS NULL AND c.volume_id IS NOT NULL;

-- ========================================
-- VERIFY FIXES
-- ========================================

PRINT 'Verifying fixes...';

-- Check again for orphaned records
SELECT 'EbookAuthors -> Authors' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookAuthors ea
LEFT JOIN Authors a ON ea.author_id = a.id
WHERE a.id IS NULL;

SELECT 'EbookAuthors -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookAuthors ea
LEFT JOIN Ebooks e ON ea.ebook_id = e.id
WHERE e.id IS NULL;

SELECT 'EbookTags -> Tags' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookTags et
LEFT JOIN Tags t ON et.tag_id = t.id
WHERE t.id IS NULL;

SELECT 'EbookTags -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM EbookTags et
LEFT JOIN Ebooks e ON et.ebook_id = e.id
WHERE e.id IS NULL;

SELECT 'Chapters -> Ebooks' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM Chapters c
LEFT JOIN Ebooks e ON c.ebook_id = e.id
WHERE e.id IS NULL;

SELECT 'Chapters -> Volumes' as Relationship, 
       COUNT(*) as OrphanedRecords
FROM Chapters c
LEFT JOIN Volumes v ON c.volume_id = v.id
WHERE v.id IS NULL AND c.volume_id IS NOT NULL;

PRINT 'Foreign key check and fix completed!';
