-- =====================================================
-- SCRIPT TỰ ĐỘNG CẬP NHẬT CONTENT_URL CHO CHAPTER
-- Mapping từ Database sang thư mục bookreal
-- =====================================================

-- Bước 1: Tạo bảng tạm để mapping category
CREATE TABLE #CategoryMapping (
    release_type NVARCHAR(100),
    folder_name NVARCHAR(100)
);

-- Insert mapping giữa release_type trong DB và tên folder trong bookreal
INSERT INTO #CategoryMapping (release_type, folder_name) VALUES
('Văn học Việt Nam', 'Văn học Việt Nam'),
('Tiểu thuyết phương Tây', 'Tiểu thuyết phương Tây'),
('Tiểu thuyết Trung Quốc', 'Tiểu thuyết Trung Quốc'),
('Truyện ngắn', 'Truyện ngắn'),
('Truyện ma', 'Truyện ma'),
('Truyện cười', 'Truyện cười'),
('Truyện tuổi teen', 'Truyện tuổi teen'),
('Trinh thám', 'Trinh thám'),
('Phiêu lưu', 'Phiêu lưu'),
('Huyền bí - Giả tưởng', 'Huyền bí - Giả tưởng'),
('Kiếm hiệp - Tiên hiệp', 'Kiếm hiệp - Tiên hiệp'),
('Cổ tích - Thần thoại', 'Cổ tích - Thần thoại'),
('Hồi ký - Tùy bút', 'Hồi ký - Tùy bút'),
('Thơ hay', 'Thơ hay'),
('Kinh tế', 'Kinh tế'),
('Marketing', 'Marketing'),
('Khoa học - Kỹ thuật', 'Khoa học - Kỹ thuật'),
('Công nghệ thông tin', 'Công nghệ thông tin'),
('Lịch sử - Chính trị', 'Lịch sử - Chính trị'),
('Văn hóa - Tôn giáo', 'Văn hóa - Tôn giáo'),
('Tâm lý - Kỹ năng sống', 'Tâm lý - Kỹ năng sống'),
('Thể thao - Nghệ thuật', 'Thể thao - Nghệ thuật'),
('Ẩm thực - Nấu ăn', 'Ẩm thực - Nấu ăn'),
('Y học', 'Y học'),
('Tử vi - Phong thủy', 'Tử vi - Phong thủy'),
('Triết học', 'Triết học'),
('Nông - Lâm - Ngư', 'Nông - Lâm - Ngư'),
('Ngoại ngữ', 'Ngoại ngữ'),
('Kiến trúc - Xây dựng', 'Kiến trúc - Xây dựng'),
('Pháp luật', 'Pháp luật'),
('Sách giáo khoa', 'Sách giáo khoa');

-- Bước 2: Cập nhật contentUrl cho tất cả chapter
UPDATE c
SET c.content_url = 
    'bookreal/' + 
    ISNULL(cm.folder_name, 'Văn học Việt Nam') + '/' + 
    e.title + '/' + 
    'chuong_' + 
    RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + 
    '.txt'
FROM Chapters c
INNER JOIN Ebooks e ON c.ebook_id = e.id
LEFT JOIN #CategoryMapping cm ON e.release_type = cm.release_type
WHERE c.content_url IS NULL OR c.content_url = '';

-- Bước 3: Hiển thị kết quả cập nhật
SELECT 
    c.id AS chapter_id,
    c.ebook_id,
    e.title AS book_title,
    e.release_type,
    c.number AS chapter_number,
    c.content_url AS new_content_url,
    'bookreal/' + ISNULL(cm.folder_name, 'Văn học Việt Nam') + '/' + e.title + '/chuong_' + RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + '.txt' AS expected_path
FROM Chapters c
INNER JOIN Ebooks e ON c.ebook_id = e.id
LEFT JOIN #CategoryMapping cm ON e.release_type = cm.release_type
ORDER BY c.ebook_id, c.number;

-- Bước 4: Xóa bảng tạm
DROP TABLE #CategoryMapping;

-- =====================================================
-- SCRIPT KIỂM TRA FILE TỒN TẠI (CHO REFERENCE)
-- =====================================================

-- Query để kiểm tra xem có bao nhiêu chapter đã được cập nhật
SELECT 
    COUNT(*) AS total_chapters,
    COUNT(CASE WHEN content_url IS NOT NULL AND content_url != '' THEN 1 END) AS chapters_with_content_url,
    COUNT(CASE WHEN content_url IS NULL OR content_url = '' THEN 1 END) AS chapters_without_content_url
FROM Chapters;

-- Query để xem danh sách sách và số chapter của chúng
SELECT 
    e.id AS book_id,
    e.title AS book_title,
    e.release_type,
    COUNT(c.id) AS chapter_count,
    MIN(c.number) AS first_chapter,
    MAX(c.number) AS last_chapter
FROM Ebooks e
LEFT JOIN Chapters c ON e.id = c.ebook_id
GROUP BY e.id, e.title, e.release_type
ORDER BY e.id;

-- =====================================================
-- SCRIPT CẬP NHẬT RIÊNG CHO SÁCH CỤ THỂ
-- =====================================================

-- Cập nhật cho sách "Tuyển Tập Truyện Ngắn Vũ Trọng Phụng" (ID 385)
UPDATE c
SET c.content_url = 'bookreal/Văn học Việt Nam/Tuyển Tập Truyện Ngắn Vũ Trọng Phụng/chuong_' + RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + '.txt'
FROM Chapters c
WHERE c.ebook_id = 385;

-- Kiểm tra kết quả cho sách cụ thể
SELECT 
    c.id AS chapter_id,
    c.ebook_id,
    c.number AS chapter_number,
    c.content_url,
    'bookreal/Văn học Việt Nam/Tuyển Tập Truyện Ngắn Vũ Trọng Phụng/chuong_' + RIGHT('000' + CAST(CAST(c.number AS INT) AS VARCHAR(10)), 3) + '.txt' AS expected_path
FROM Chapters c
WHERE c.ebook_id = 385
ORDER BY c.number; 