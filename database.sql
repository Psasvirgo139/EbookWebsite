-- Tạo database
CREATE DATABASE EBookWebsite;
USE EBookWebsite;
select*from Users;
-- USERS
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL,
    email NVARCHAR(255) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    avatar_url NVARCHAR(MAX),
    role NVARCHAR(20) DEFAULT 'user',
    created_at DATETIME DEFAULT GETDATE(),
    userinfor_id INT NULL,
    status NVARCHAR(20) DEFAULT 'active',  -- Thêm cột status để theo dõi trạng thái (active, banned, deleted...)
    last_login DATETIME NULL              -- Thêm cột last_login để theo dõi thời gian đăng nhập cuối
);

-- USER INFORMATION
CREATE TABLE UserInfor (
    id INT IDENTITY(1,1) PRIMARY KEY,
    phone NVARCHAR(20),
    birthday DATE,
    gender NVARCHAR(10),
    address NVARCHAR(255),
    introduction NVARCHAR(MAX)
);

ALTER TABLE Users
ADD CONSTRAINT FK_Users_UserInfor
FOREIGN KEY (userinfor_id) REFERENCES UserInfor(id);

-- EBOOKS
CREATE TABLE Ebooks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    release_type NVARCHAR(20) NOT NULL,
    language NVARCHAR(50),
    status NVARCHAR(20) DEFAULT 'ongoing',
    visibility NVARCHAR(20) DEFAULT 'public',
    uploader_id INT,
    created_at DATETIME DEFAULT GETDATE(),
    view_count INT DEFAULT 0,             -- Thêm cột view_count để theo dõi lượt xem
    cover_url NVARCHAR(MAX),              -- Thêm cột cover_url để lưu đường dẫn bìa sách
    FOREIGN KEY (uploader_id) REFERENCES Users(id)
);

-- TAGS
CREATE TABLE Tags (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    CONSTRAINT UK_Tags_Name UNIQUE (name)  -- Thêm ràng buộc UNIQUE trên cột name để tránh trùng lặp
);

-- EBOOK-TAGS
CREATE TABLE EbookTags (
    ebook_id INT,
    tag_id INT,
    is_primary BIT DEFAULT 0,
    PRIMARY KEY (ebook_id, tag_id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (tag_id) REFERENCES Tags(id)
);

-- VOLUMES
CREATE TABLE Volumes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ebook_id INT NOT NULL,
    title NVARCHAR(255),
    number INT,
    published_at DATE,
    access_level NVARCHAR(20) DEFAULT 'public', -- Thêm cột access_level để quản lý quyền truy cập (public, premium...)
    view_count INT DEFAULT 0,                  -- Thêm cột view_count để theo dõi lượt xem
    like_count INT DEFAULT 0,                  -- Thêm cột like_count để theo dõi lượt thích
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id)
);

-- CHAPTERS
CREATE TABLE Chapters (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ebook_id INT NOT NULL,
    volume_id INT NULL,
    title NVARCHAR(255),
    number DECIMAL(5,1),
    content_url NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    access_level NVARCHAR(20) DEFAULT 'public', -- Thêm cột access_level để quản lý quyền truy cập (public, premium...)
    view_count INT DEFAULT 0,                  -- Thêm cột view_count để theo dõi lượt xem
    like_count INT DEFAULT 0,                  -- Thêm cột like_count để theo dõi lượt thích
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (volume_id) REFERENCES Volumes(id)
);

-- AUTHORS
CREATE TABLE Authors (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    bio NVARCHAR(MAX),
    avatar_url NVARCHAR(MAX),
    CONSTRAINT UK_Authors_Name UNIQUE (name)   -- Thêm ràng buộc UNIQUE trên cột name để tránh trùng lặp
);

-- EBOOK-AUTHORS
CREATE TABLE EbookAuthors (
    ebook_id INT,
    author_id INT,
    role NVARCHAR(100),
    PRIMARY KEY (ebook_id, author_id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (author_id) REFERENCES Authors(id)
);

-- FAVORITES
CREATE TABLE Favorites (
    user_id INT,
    ebook_id INT,
    chapter_id INT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    PRIMARY KEY (user_id, ebook_id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id)
);

-- COMMENTS
CREATE TABLE Comments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    chapter_id INT NULL,
    content NVARCHAR(MAX) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    parent_comment_id INT NULL,                -- Thêm cột parent_comment_id để hỗ trợ bình luận trả lời
    like_count INT DEFAULT 0,                  -- Thêm cột like_count để theo dõi lượt thích bình luận
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id),
    FOREIGN KEY (parent_comment_id) REFERENCES Comments(id) -- Thêm khóa ngoại cho parent_comment_id
);

-- USER READS
CREATE TABLE UserReads (
    user_id INT,
    ebook_id INT,
    last_read_chapter_id INT,
    last_read_at DATETIME DEFAULT GETDATE(),
    PRIMARY KEY (user_id, ebook_id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (last_read_chapter_id) REFERENCES Chapters(id)
);

select*from Users;

-- Dữ liệu thông tin cá nhân cho user
INSERT INTO UserInfor (phone, birthday, gender, address, introduction)
VALUES 
('0912345678', '2002-08-12', 'Nữ', N'Đà Nẵng', N'Tôi là Công Chúa Giáng Trần, thích đọc sách và coding!'), -- id = 1
('0987654321', '2000-01-01', 'Nam', N'Hà Nội', N'Tôi là admin hệ thống.'),                                 -- id = 2
('0901122334', '2003-10-20', 'Nữ', N'Hồ Chí Minh', N'Bạn đọc thân thiện!');                                 -- id = 3
-- Thêm tài khoản user thường
INSERT INTO Users (username, email, password_hash, avatar_url, role, userinfor_id, status)
VALUES 
(N'giangtran', N'giangtran@example.com', N'123456', 'https://i.imgur.com/Vz8s1cC.png', 'user', 1, 'active');

-- Thêm tài khoản admin
INSERT INTO Users (username, email, password_hash, avatar_url, role, userinfor_id, status)
VALUES 
(N'admin', N'admin@example.com', N'admin123', 'https://i.imgur.com/IrFODtE.png', 'admin', 2, 'active');

-- Thêm tài khoản user thường (thứ 2)
INSERT INTO Users (username, email, password_hash, avatar_url, role, userinfor_id, status)
VALUES 
(N'thuynhi', N'thuynhi@example.com', N'654321', NULL, 'user', 3, 'active');
SELECT * FROM Users WHERE (username = 'giangtran' OR email = 'giangtran@example.com') AND password_hash = '123456' AND status != 'deleted'


INSERT INTO Users (username, email, password_hash, avatar_url, role, userinfor_id, status)
VALUES 
(N'sa', N'sa@example.com', N'123456', NULL, 'user', NULL, 'active');


ALTER TABLE Users
ADD reset_token VARCHAR(128) NULL,
    reset_token_expiry DATETIME NULL;



-- Tạo index cho reset_token để tìm kiếm nhanh hơn
CREATE INDEX IX_Users_ResetToken ON Users(reset_token);

-- Tạo index cho reset_token_expiry để dọn dẹp token hết hạn
CREATE INDEX IX_Users_ResetTokenExpiry ON Users(reset_token_expiry);

-- Thêm comment cho các cột mới
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'Token để reset password', 
    @level0type = N'SCHEMA', @level0name = N'dbo', 
    @level1type = N'TABLE', @level1name = N'Users', 
    @level2type = N'COLUMN', @level2name = N'reset_token';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'Thời gian hết hạn của reset token', 
    @level0type = N'SCHEMA', @level0name = N'dbo', 
    @level1type = N'TABLE', @level1name = N'Users', 
    @level2type = N'COLUMN', @level2name = N'reset_token_expiry';

-- Tạo stored procedure để dọn dẹp token hết hạn
CREATE PROCEDURE CleanupExpiredResetTokens
AS
BEGIN
    SET NOCOUNT ON;
    
    UPDATE Users 
    SET reset_token = NULL, 
        reset_token_expiry = NULL 
    WHERE reset_token_expiry IS NOT NULL 
      AND reset_token_expiry < GETDATE();
      
    PRINT 'Đã dọn dẹp ' + CAST(@@ROWCOUNT AS VARCHAR) + ' token hết hạn';
END;


SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Users' 
  AND COLUMN_NAME IN ('reset_token', 'reset_token_expiry')
ORDER BY COLUMN_NAME;

PRINT 'Cập nhật database hoàn tất!';
PRINT 'Các cột mới đã được thêm vào bảng Users:';
PRINT '- reset_token: Lưu token để reset password';
PRINT '- reset_token_expiry: Thời gian hết hạn của token';
PRINT '';
PRINT 'Lưu ý:';
PRINT '1. Chạy stored procedure CleanupExpiredResetTokens để dọn dẹp token hết hạn';
PRINT '2. Có thể tạo SQL Server Agent Job để tự động dọn dẹp';
PRINT '3. Kiểm tra index đã được tạo để tối ưu hiệu suất'; 


-- Thêm user mới vào bảng Users
INSERT INTO Users (
    username, 
    email, 
    password_hash, 
    avatar_url, 
    role, 
    userinfor_id, 
    status
)
VALUES (
    N'trankimthang', -- hoặc tên gì bạn muốn
    N'trankimthang857@gmail.com',
    N'123456', -- Đổi thành hash của mật khẩu thật sự (vd: '123456')
    NULL,               -- avatar_url (nếu có thì điền link)
    'user',             -- role
    NULL,               -- userinfor_id (nếu có thông tin cá nhân thì thêm sau)
    'active'
);


select * from Users;


-- Sample data for EbookWebsite
-- Run this script to populate the database with sample books
ctive', 'public', 1, 'https://i.imgur.com/yfWccFA.png', 650, GETDATE());

-- Insert sample chapters
INSERT INTO Chapters (ebook_id, title, number, content_url, access_level, view_count, like_count, created_at) VALUES
(1, 'Chương 1: Khởi đầu', 1.0, '/content/servlet1.txt', 'public', 500, 45, GETDATE()),
(1, 'Chương 2: Thế giới mới', 2.0, '/content/java1.txt', 'public', 420, 38, GETDATE()),
(2, 'Chương 1: Gặp gỡ', 1.0, '/content/servlet1.txt', 'public', 800, 67, GETDATE()),
(2, 'Chương 2: Ma pháp', 2.0, '/content/java1.txt', 'public', 720, 58, GETDATE()),
(3, 'Chương 1: Bắt đầu hành trình', 1.0, '/content/servlet1.txt', 'public', 1200, 89, GETDATE()),
(3, 'Chương 2: Băng Mũ Rơm', 2.0, '/content/java1.txt', 'public', 1100, 76, GETDATE()),
(4, 'Chương 1: Xuyên không', 1.0, '/content/servlet1.txt', 'public', 600, 52, GETDATE()),
(4, 'Chương 2: Hệ thống', 2.0, '/content/java1.txt', 'public', 550, 48, GETDATE());

-- Insert sample comments
INSERT INTO Comments (user_id, ebook_id, content, created_at) VALUES
(1, 1, 'Truyện rất hay, đáng đọc!', GETDATE()),
(1, 1, 'Tác giả viết rất chi tiết', GETDATE()),
(1, 2, 'Ma pháp trong truyện rất thú vị', GETDATE()),
(1, 3, 'One Piece là truyện hay nhất!', GETDATE());



INSERT INTO Authors (name, bio, avatar_url) VALUES
(N'Rick Riordan',          N'Tác giả nổi tiếng với các series thần thoại.',                    NULL),
(N'Mặc Hương Đồng Khứu',   N'Tác giả đam mỹ nổi tiếng – “Ma Đạo Tổ Sư”.',                     NULL),
(N'Eiichiro Oda',          N'Tác giả “One Piece” – kỷ lục manga bán chạy nhất mọi thời đại.', NULL),
(N'Tiêu Dao',              N'Tác giả truyện tu tiên và hệ thống tu luyện.',                   NULL);


/* ==========================
   SAMPLE DATA – TAGS
   Đúng cấu trúc: id (IDENTITY), name
========================== */

-- Tuỳ chọn dọn sạch dữ liệu thử nếu cần
-- TRUNCATE TABLE Tags;

INSERT INTO Tags (name) VALUES
(N'Hành động'),
(N'Thần thoại'),
(N'Đam mỹ'),
(N'Kỳ ảo'),
(N'Shounen'),
(N'Phiêu lưu'),
(N'Tiên hiệp'),
(N'Xuyên không'),
(N'Ngôn tình'),
(N'Bi kịch');


-- Link books with authors
INSERT INTO EbookAuthors (ebook_id, author_id, role) VALUES
(1, 1, 'Tác giả chính'),
(2, 2, 'Tác giả chính'),
(3, 3, 'Tác giả chính'),
(4, 4, 'Tác giả chính');

-- Link books with tags
INSERT INTO EbookTags (ebook_id, tag_id, is_primary) VALUES
(1, 1, 1), (1, 2, 0),
(2, 3, 1), (2, 4, 0),
(3, 5, 1), (3, 6, 0),
(4, 7, 1), (4, 8, 0),
(5, 9, 1), (5, 10, 0); 

-- Thay vì:
-- ALTER TABLE Book ADD summary TEXT;
-- ALTER TABLE Book ADD content TEXT;
-- ALTER TABLE Book ADD filterStatus VARCHAR(20);
-- ALTER TABLE Book ADD filterMessage TEXT;

-- Hãy dùng:
ALTER TABLE Ebooks ADD summary TEXT;
ALTER TABLE Ebooks ADD content TEXT;
ALTER TABLE Ebooks ADD filterStatus VARCHAR(20);
ALTER TABLE Ebooks ADD filterMessage TEXT;


-- SQL để thêm các sách mới vào bảng Ebooks
-- Chạy từng câu lệnh INSERT một

-- 1. Sách phiêu lưu ma thuật
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at) 
VALUES (
    'Alex và Luna - Cuộc phiêu lưu ma thuật',
    'Câu chuyện về cậu bé Alex học ma thuật với familiar Luna, cùng nhau đối mặt với những thử thách và kẻ thù bí ẩn.',
    'Tiểu thuyết',
    'Tiếng Việt',
    'Đang ra',
    'public',
    1,
    'https://i.imgur.com/magic_cover.jpg',
    0,
    GETDATE()
);

-- 2. Sách ngôn tình
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at) 
VALUES (
    'Tình yêu và những bí mật',
    'Câu chuyện tình yêu đầy bi kịch của Minh Anh, một cô gái trẻ phát hiện ra sự thật đau lòng về người mình yêu.',
    'Tiểu thuyết',
    'Tiếng Việt',
    'Hoàn thành',
    'public',
    1,
    'https://i.imgur.com/romance_cover.jpg',
    0,
    GETDATE()
);

-- 3. Sách trinh thám
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at) 
VALUES (
    'Án mạng ở biệt thự',
    'Thám tử Trần Minh điều tra vụ án mạng bí ẩn tại một biệt thự sang trọng, nơi mọi thứ không như vẻ bề ngoài.',
    'Tiểu thuyết',
    'Tiếng Việt',
    'Hoàn thành',
    'public',
    1,
    'https://i.imgur.com/mystery_cover.jpg',
    0,
    GETDATE()
);

-- 4. Sách self-help
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at) 
VALUES (
    'Hành trình tìm kiếm bản thân',
    'Câu chuyện về Mai Linh và hành trình thay đổi cuộc đời, từ một nhân viên marketing thành công đến một nhà văn tự do hạnh phúc.',
    'Self-help',
    'Tiếng Việt',
    'Hoàn thành',
    'public',
    1,
    'https://i.imgur.com/selfhelp_cover.jpg',
    0,
    GETDATE()
);

-- 5. Sách khoa học viễn tưởng
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at) 
VALUES (
    'Thế giới công nghệ tương lai',
    'Một cái nhìn về thế giới năm 2050, nơi công nghệ đã phát triển vượt bậc và thay đổi hoàn toàn cách con người sống.',
    'Khoa học viễn tưởng',
    'Tiếng Việt',
    'Hoàn thành',
    'public',
    1,
    'https://i.imgur.com/scifi_cover.jpg',
    0,
    GETDATE()
);

-- Kiểm tra kết quả
SELECT id, title, description, status, created_at FROM Ebooks ORDER BY created_at DESC;

SELECT COUNT(*) FROM Ebooks;
-- Kiểm tra có sách không
SELECT COUNT(*) FROM Ebooks;

-- Xem danh sách sách
SELECT id, title, description, status FROM Ebooks;
INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, created_at)
VALUES 
(N'Alex và Luna – Cuộc phiêu lưu ma thuật', N'Sách file TXT upload', N'Tiểu thuyết', N'Tiếng Việt', N'Đang ra', N'public', 1, NULL, 0, GETDATE()),
(N'Án mạng ở biệt thự', N'Sách file TXT upload', N'Trinh thám', N'Tiếng Việt', N'Hoàn thành', N'public', 1, NULL, 0, GETDATE()),
(N'Hành trình tìm kiếm bản thân', N'Sách file TXT upload', N'Self-help', N'Tiếng Việt', N'Hoàn thành', N'public', 1, NULL, 0, GETDATE()),
(N'Thế giới công nghệ tương lai', N'Sách file TXT upload', N'Khoa học viễn tưởng', N'Tiếng Việt', N'Hoàn thành', N'public', 1, NULL, 0, GETDATE()),
(N'Tình yêu và những bí mật', N'Sách file TXT upload', N'Ngôn tình', N'Tiếng Việt', N'Hoàn thành', N'public', 1, NULL, 0, GETDATE());

ALTER TABLE Ebooks ADD file_name NVARCHAR(255);


UPDATE Ebooks 
SET file_name = 'alexvalunacuocphieuluvamathuat.txt'
WHERE title LIKE '%Alex và Luna%' OR title LIKE '%Alex va Luna%';

UPDATE Ebooks 
SET file_name = 'anmangoobiethu.txt'
WHERE title LIKE '%Án mạng%' OR title LIKE '%An mang%';

UPDATE Ebooks 
SET file_name = 'hanhtrinhtimkiembanthan.txt'
WHERE title LIKE '%Hành trình%' OR title LIKE '%Hanh trinh%';

UPDATE Ebooks 
SET file_name = 'thegioicongnghetuonglai.txt'
WHERE title LIKE '%Thế giới công nghệ%' OR title LIKE '%The gioi cong nghe%';

UPDATE Ebooks 
SET file_name = 'tinhyeuvanhungbimat.txt'
WHERE title LIKE '%Tình yêu%' OR title LIKE '%Tinh yeu%';

select*from Ebooks;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'original_file_name')
BEGIN
    ALTER TABLE Ebooks 
    ADD original_file_name NVARCHAR(255) NULL;
    PRINT '✅ Đã thêm cột original_file_name';
END
ELSE
BEGIN
    PRINT '⚠️  Cột original_file_name đã tồn tại, bỏ qua bước này';
END
GO

-- 2. Tạo index cho original_file_name để search nhanh hơn
PRINT '📊 Tạo index cho original_file_name...';
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Ebooks_OriginalFileName')
BEGIN
    CREATE INDEX IX_Ebooks_OriginalFileName ON Ebooks(original_file_name);
    PRINT '✅ Đã tạo index IX_Ebooks_OriginalFileName';
END
ELSE
BEGIN
    PRINT '⚠️  Index IX_Ebooks_OriginalFileName đã tồn tại';
END
GO

-- 4. Kiểm tra kết quả
PRINT '';
PRINT '📋 Kiểm tra kết quả cập nhật...';
SELECT 
    id,
    title,
    file_name as 'Normalized FileName',
    original_file_name as 'Original FileName',
    CASE 
        WHEN original_file_name IS NOT NULL THEN '✅ Có original filename'
        ELSE '❌ Chưa có original filename' 
    END as status
FROM Ebooks
WHERE status != 'deleted'
ORDER BY id;

PRINT '📊 Thống kê cập nhật:';
SELECT 
    COUNT(*) as total_books,
    SUM(CASE WHEN file_name IS NOT NULL THEN 1 ELSE 0 END) as books_with_filename,
    SUM(CASE WHEN original_file_name IS NOT NULL THEN 1 ELSE 0 END) as books_with_original_filename,
    SUM(CASE WHEN file_name IS NOT NULL AND original_file_name IS NOT NULL THEN 1 ELSE 0 END) as books_with_both_filenames
FROM Ebooks
WHERE status != 'deleted';

-- 6. Tạo view để dễ dàng xem thông tin file
PRINT '';
PRINT '👀 Tạo view để xem thông tin file...';
IF OBJECT_ID('vw_EbookFiles', 'V') IS NOT NULL
    DROP VIEW vw_EbookFiles;
GO

CREATE VIEW vw_EbookFiles AS
SELECT 
    id,
    title,
    original_file_name,
    file_name,
    CASE 
        WHEN file_name IS NULL THEN '❌ Không có file'
        WHEN original_file_name IS NULL THEN '⚠️ Thiếu original filename'
        ELSE '✅ Đầy đủ'
    END as file_status,
    'uploads/' + file_name as file_path,
    created_at
FROM Ebooks 
WHERE status != 'deleted';
GO

PRINT '✅ Đã tạo view vw_EbookFiles';

PRINT '';
PRINT '🧪 Test view vw_EbookFiles:';
SELECT TOP 5 * FROM vw_EbookFiles ORDER BY id;
select*from Users;
select*from Ebooks;

SELECT * FROM UserRead WHERE user_id = 12;

  SELECT name FROM sys.tables;
SELECT * FROM UserReads WHERE user_id = 12;

SELECT id, title, release_type FROM Ebooks WHERE id IN (42);

-- Lịch sử chat persistent
CREATE TABLE ChatHistory (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES Users(id),
    session_id VARCHAR(255),
    message TEXT,
    response TEXT,
    context_type VARCHAR(50), -- 'book', 'author', 'general'
    context_id INT, -- ID của book/author nếu có
    created_at DATETIME DEFAULT GETDATE(),
    embedding_used BIT DEFAULT 0
);

-- Vector embeddings để cache queries
CREATE TABLE ChatEmbeddings (
    id INT IDENTITY(1,1) PRIMARY KEY,
    query_hash VARCHAR(255) UNIQUE,
    query_text TEXT,
    embedding_vector TEXT, -- JSON array of floats
    response_template TEXT,
    usage_count INT DEFAULT 1,
    created_at DATETIME DEFAULT GETDATE(),
    last_used DATETIME DEFAULT GETDATE()
);

-- User behavior tracking để personalization
CREATE TABLE UserBehavior (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES Users(id),
    action_type VARCHAR(50), -- 'view_book', 'search', 'chat_question'
    target_id INT, -- book_id, author_id, etc
    target_type VARCHAR(50), -- 'book', 'author', 'genre'
    action_data TEXT, -- JSON metadata
    created_at DATETIME DEFAULT GETDATE()
);



-- Thêm các cột mới cho bảng Ebooks
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'metadata_json')
BEGIN
    ALTER TABLE Ebooks ADD metadata_json NVARCHAR(MAX);
    PRINT '✅ Đã thêm cột metadata_json';
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'ai_analysis_json')
BEGIN
    ALTER TABLE Ebooks ADD ai_analysis_json NVARCHAR(MAX);
    PRINT '✅ Đã thêm cột ai_analysis_json';
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'rejection_reason')
BEGIN
    ALTER TABLE Ebooks ADD rejection_reason NVARCHAR(MAX);
    PRINT '✅ Đã thêm cột rejection_reason';
END

-- Cập nhật cột status để hỗ trợ các trạng thái mới
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[Ebooks]') AND name = 'status' AND system_type_id = 231)
BEGIN
    -- Tạo bảng tạm để lưu dữ liệu
    CREATE TABLE #TempEbooks (
        id INT,
        status NVARCHAR(20)
    );
    
    -- Sao chép dữ liệu vào bảng tạm
    INSERT INTO #TempEbooks (id, status)
    SELECT id, status FROM Ebooks;
    
    -- Xóa cột status cũ
    ALTER TABLE Ebooks DROP COLUMN status;
    
    -- Thêm cột status mới
    ALTER TABLE Ebooks ADD status NVARCHAR(20) DEFAULT 'draft' CHECK (status IN ('draft', 'pending', 'public', 'rejected', 'ongoing', 'completed'));
    
    -- Cập nhật lại dữ liệu
    UPDATE e
    SET e.status = CASE t.status 
        WHEN 'ongoing' THEN 'draft'
        WHEN 'completed' THEN 'public'
        ELSE t.status
    END
    FROM Ebooks e
    JOIN #TempEbooks t ON e.id = t.id;
    
    -- Xóa bảng tạm
    DROP TABLE #TempEbooks;
    
    PRINT '✅ Đã cập nhật cột status';
END

-- Tạo view để dễ dàng xem thông tin sách và metadata
IF OBJECT_ID('vw_EbookDetails', 'V') IS NOT NULL
    DROP VIEW vw_EbookDetails;
GO

CREATE VIEW vw_EbookDetails AS
SELECT 
    e.id,
    e.title,
    e.description,
    e.release_type,
    e.language,
    e.status,
    e.visibility,
    e.uploader_id,
    e.created_at,
    e.view_count,
    e.cover_url,
    e.file_name,
    e.original_file_name,
    e.metadata_json,
    e.ai_analysis_json,
    e.rejection_reason,
    u.username as uploader_name,
    (SELECT STRING_AGG(a.name, ', ') 
     FROM EbookAuthors ea 
     JOIN Authors a ON ea.author_id = a.id 
     WHERE ea.ebook_id = e.id) as authors,
    (SELECT STRING_AGG(t.name, ', ') 
     FROM EbookTags et 
     JOIN Tags t ON et.tag_id = t.id 
     WHERE et.ebook_id = e.id) as tags
FROM Ebooks e
LEFT JOIN Users u ON e.uploader_id = u.id;
GO

PRINT '✅ Đã tạo view vw_EbookDetails';

-- Tạo stored procedure để cập nhật trạng thái sách
IF OBJECT_ID('sp_UpdateBookStatus', 'P') IS NOT NULL
    DROP PROCEDURE sp_UpdateBookStatus;
GO

CREATE PROCEDURE sp_UpdateBookStatus
    @book_id INT,
    @new_status NVARCHAR(20),
    @rejection_reason NVARCHAR(MAX) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    
    IF @new_status NOT IN ('draft', 'pending', 'public', 'rejected')
    BEGIN
        RAISERROR ('Invalid status. Allowed values: draft, pending, public, rejected', 16, 1);
        RETURN;
    END
    
    BEGIN TRY
        BEGIN TRANSACTION;
        
        UPDATE Ebooks
        SET status = @new_status,
            rejection_reason = CASE 
                WHEN @new_status = 'rejected' THEN @rejection_reason
                ELSE NULL
            END,
            visibility = CASE
                WHEN @new_status = 'public' THEN 'public'
                ELSE 'private'
            END
        WHERE id = @book_id;
        
        COMMIT TRANSACTION;
        
        SELECT 'Success' as result;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
            
        SELECT 
            ERROR_MESSAGE() as error_message,
            ERROR_LINE() as error_line,
            ERROR_NUMBER() as error_number;
    END CATCH
END;
GO

PRINT '✅ Đã tạo stored procedure sp_UpdateBookStatus';

-- Tạo index cho tìm kiếm
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Ebooks_Status')
BEGIN
    CREATE INDEX IX_Ebooks_Status ON Ebooks(status);
    PRINT '✅ Đã tạo index IX_Ebooks_Status';
END

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Ebooks_Visibility')
BEGIN
    CREATE INDEX IX_Ebooks_Visibility ON Ebooks(visibility);
    PRINT '✅ Đã tạo index IX_Ebooks_Visibility';
END

-- Kiểm tra kết quả
PRINT '';
PRINT '📋 Kiểm tra cấu trúc bảng Ebooks:';
SELECT 
    c.name as column_name,
    t.name as data_type,
    c.max_length,
    c.is_nullable
FROM sys.columns c
JOIN sys.types t ON c.system_type_id = t.system_type_id
WHERE object_id = OBJECT_ID('Ebooks')
ORDER BY c.column_id;

PRINT '';
PRINT '📊 Test view vw_EbookDetails:';
SELECT TOP 5 * FROM vw_EbookDetails ORDER BY created_at DESC;

PRINT '';
PRINT '🔍 Test stored procedure:';
EXEC sp_UpdateBookStatus @book_id = 1, @new_status = 'pending';
SELECT id, title, status, visibility FROM Ebooks WHERE id = 1;