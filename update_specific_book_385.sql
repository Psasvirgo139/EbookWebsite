-- =====================================================
-- SCRIPT CẬP NHẬT CONTENT_URL CHO SÁCH CỤ THỂ
-- Sách: "Tuyển Tập Truyện Ngắn Vũ Trọng Phụng" (ID 385)
-- =====================================================

-- Bước 1: Kiểm tra sách hiện tại
SELECT 
    e.id AS book_id,
    e.title AS book_title,
    e.release_type,
    COUNT(c.id) AS chapter_count
FROM Ebooks e
LEFT JOIN Chapters c ON e.id = c.ebook_id
WHERE e.id = 385
GROUP BY e.id, e.title, e.release_type;

-- Bước 2: Xem chapter hiện tại
SELECT 
    c.id AS chapter_id,
    c.ebook_id,
    c.number AS chapter_number,
    c.title AS chapter_title,
    c.content_url AS current_content_url
FROM Chapters c
WHERE c.ebook_id = 385
ORDER BY c.number;

-- Bước 3: Cập nhật contentUrl cho tất cả chapter của sách 385
UPDATE c
SET c.content_url = 'bookreal/Văn học Việt Nam/Tuyển Tập Truyện Ngắn Vũ Trọng Phụng/chuong_' + 
                    RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + '.txt'
FROM Chapters c
WHERE c.ebook_id = 385;

-- Bước 4: Kiểm tra kết quả sau khi cập nhật
SELECT 
    c.id AS chapter_id,
    c.ebook_id,
    c.number AS chapter_number,
    c.title AS chapter_title,
    c.content_url AS new_content_url,
    'bookreal/Văn học Việt Nam/Tuyển Tập Truyện Ngắn Vũ Trọng Phụng/chuong_' + 
    RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + '.txt' AS expected_path
FROM Chapters c
WHERE c.ebook_id = 385
ORDER BY c.number;

-- Bước 5: Thống kê kết quả
SELECT 
    COUNT(*) AS total_chapters,
    COUNT(CASE WHEN content_url IS NOT NULL AND content_url != '' THEN 1 END) AS chapters_with_content_url,
    COUNT(CASE WHEN content_url IS NULL OR content_url = '' THEN 1 END) AS chapters_without_content_url
FROM Chapters
WHERE ebook_id = 385; 