-- Script thêm dữ liệu test nhanh cho EbookWebsite
-- Chạy script này trong SQL Server Management Studio

USE EBookWebsite; -- Thay đổi tên database nếu cần

-- 1. Thêm Tags nếu chưa có
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Tiểu thuyết')
    INSERT INTO Tags (name) VALUES (N'Tiểu thuyết');
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Lập trình')
    INSERT INTO Tags (name) VALUES (N'Lập trình');
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Khoa học')
    INSERT INTO Tags (name) VALUES (N'Khoa học');
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Văn học')
    INSERT INTO Tags (name) VALUES (N'Văn học');
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Kinh tế')
    INSERT INTO Tags (name) VALUES (N'Kinh tế');
IF NOT EXISTS (SELECT * FROM Tags WHERE name = N'Công nghệ')
    INSERT INTO Tags (name) VALUES (N'Công nghệ');

-- 2. Thêm Users nếu chưa có
IF NOT EXISTS (SELECT * FROM Users WHERE username = 'admin')
    INSERT INTO Users (username, email, password_hash, role, status) 
    VALUES ('admin', 'admin@test.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', 'active');

-- 3. Xóa sách cũ (nếu có)
DELETE FROM Ebooks;

-- 4. Thêm sách test mới
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, is_premium, price, view_count) VALUES 
(N'Java Programming Masterclass', N'Học lập trình Java từ cơ bản đến nâng cao', 'completed', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/4f8cff/fff?text=Java', 1, 150000, 500),
(N'Web Development Fundamentals', N'Cơ bản về phát triển web với HTML, CSS, JavaScript', 'ongoing', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/6ee7b7/fff?text=Web', 0, 0, 300),
(N'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', N'Tác phẩm nổi tiếng của Nguyễn Nhật Ánh', 'completed', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/ff6b6b/fff?text=Story', 0, 0, 800),
(N'Harry Potter và Hòn Đá Phù Thủy', N'Phần đầu tiên của series Harry Potter', 'completed', 'English', 'active', 'public', 1, 'https://via.placeholder.com/300x400/6c5ce7/fff?text=Magic', 1, 200000, 1200),
(N'Sherlock Holmes - A Study in Scarlet', N'Truyện trinh thám kinh điển của Arthur Conan Doyle', 'completed', 'English', 'active', 'public', 1, 'https://via.placeholder.com/300x400/ffd93d/fff?text=Detective', 1, 180000, 600),
(N'Python cho người mới bắt đầu', N'Hướng dẫn lập trình Python từ zero', 'ongoing', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/00b894/fff?text=Python', 0, 0, 400),
(N'Đắc Nhân Tâm', N'Sách về nghệ thuật đối nhân xử thế', 'completed', 'Vietnamese', 'active', 'public', 1, 'https://via.placeholder.com/300x400/e17055/fff?text=Self-Help', 0, 0, 900),
(N'Clean Code', N'Viết code sạch và dễ bảo trì', 'completed', 'English', 'active', 'public', 1, 'https://via.placeholder.com/300x400/0984e3/fff?text=Code', 1, 250000, 350);

-- 5. Kiểm tra kết quả
SELECT '=== KẾT QUẢ ===' as Info;
SELECT 'Số sách: ' + CAST(COUNT(*) AS VARCHAR) as BookCount FROM Ebooks;
SELECT 'Số tags: ' + CAST(COUNT(*) AS VARCHAR) as TagCount FROM Tags;
SELECT 'Số users: ' + CAST(COUNT(*) AS VARCHAR) as UserCount FROM Users;

-- 6. Hiển thị sách theo loại
SELECT 'Sách Premium:' as Category;
SELECT id, title, is_premium, price, view_count FROM Ebooks WHERE is_premium = 1 AND status = 'active' ORDER BY view_count DESC;

SELECT 'Sách Miễn phí:' as Category;
SELECT id, title, is_premium, price, view_count FROM Ebooks WHERE is_premium = 0 AND status = 'active' ORDER BY view_count DESC;

SELECT 'Thể loại:' as Category;
SELECT id, name FROM Tags ORDER BY id; 