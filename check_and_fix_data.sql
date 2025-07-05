-- Script kiểm tra và sửa dữ liệu cho EbookWebsite
-- Chạy script này trong SQL Server Management Studio hoặc Azure Data Studio

USE EBookWebsite; -- Thay đổi tên database nếu cần

-- 1. Kiểm tra dữ liệu hiện tại
SELECT '=== KIỂM TRA DỮ LIỆU HIỆN TẠI ===' as Info;

-- Kiểm tra bảng Ebooks
SELECT 'Ebooks table:' as TableName;
SELECT id, title, status, is_premium, view_count, created_at 
FROM Ebooks 
ORDER BY id;

-- Kiểm tra bảng Tags
SELECT 'Tags table:' as TableName;
SELECT id, name FROM Tags ORDER BY id;

-- Kiểm tra bảng Users
SELECT 'Users table:' as TableName;
SELECT id, username, email, role, status FROM Users ORDER BY id;

-- 2. Cập nhật status cho sách (nếu cần)
SELECT '=== CẬP NHẬT STATUS SÁCH ===' as Info;

-- Cập nhật tất cả sách thành status = 'active'
UPDATE Ebooks SET status = 'active' WHERE status IS NULL OR status != 'active';

-- Kiểm tra lại sau khi cập nhật
SELECT 'Sách sau khi cập nhật:' as Info;
SELECT id, title, status, is_premium, view_count 
FROM Ebooks 
ORDER BY id;

-- 3. Thêm dữ liệu mẫu nếu không có sách nào
SELECT '=== KIỂM TRA VÀ THÊM DỮ LIỆU MẪU ===' as Info;

-- Đếm số sách
DECLARE @bookCount INT = (SELECT COUNT(*) FROM Ebooks);
SELECT 'Số sách hiện tại: ' + CAST(@bookCount AS VARCHAR) as BookCount;

-- Nếu không có sách nào, thêm dữ liệu mẫu
IF @bookCount = 0
BEGIN
    SELECT 'Thêm dữ liệu mẫu...' as Info;
    
    -- Thêm sách mẫu
    INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, is_premium, price, view_count) VALUES 
    (N'Sách Java Premium', N'Giới thiệu lập trình Java nâng cao', 'completed', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/4f8cff/fff?text=Java', 1, 50000, 150),
    (N'Sách Web Cơ Bản', N'Giới thiệu lập trình web', 'ongoing', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/6ee7b7/fff?text=Web', 0, 0, 200),
    (N'Kính Vạn Hoa', N'Truyện thiếu nhi nổi tiếng', 'completed', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/ff6b6b/fff?text=Story', 0, 0, 300),
    (N'Sherlock Holmes', N'Truyện trinh thám kinh điển', 'completed', 'English', 'active', 'public', 1, 'https://via.placeholder.com/300x400/ffd93d/fff?text=Detective', 1, 70000, 250),
    (N'Harry Potter', N'Truyện phép thuật nổi tiếng', 'completed', 'English', 'active', 'public', 1, 'https://via.placeholder.com/300x400/6c5ce7/fff?text=Magic', 1, 90000, 400);
    
    SELECT 'Đã thêm 5 cuốn sách mẫu' as Result;
END
ELSE
BEGIN
    SELECT 'Đã có dữ liệu sách, không cần thêm mẫu' as Result;
END

-- 4. Kiểm tra kết quả cuối cùng
SELECT '=== KẾT QUẢ CUỐI CÙNG ===' as Info;

-- Sách premium
SELECT 'Sách Premium:' as Category;
SELECT id, title, status, is_premium, price, view_count 
FROM Ebooks 
WHERE is_premium = 1 AND status = 'active'
ORDER BY view_count DESC;

-- Sách miễn phí
SELECT 'Sách Miễn phí:' as Category;
SELECT id, title, status, is_premium, price, view_count 
FROM Ebooks 
WHERE is_premium = 0 AND status = 'active'
ORDER BY view_count DESC;

-- Sách mới nhất
SELECT 'Sách Mới nhất:' as Category;
SELECT id, title, status, created_at, view_count 
FROM Ebooks 
WHERE status = 'active'
ORDER BY created_at DESC;

-- Thể loại
SELECT 'Thể loại:' as Category;
SELECT id, name FROM Tags ORDER BY id; 