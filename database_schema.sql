-- =====================================================
-- EBookWebsite Database Schema
-- =====================================================

-- Tạo database
CREATE DATABASE EBookWebsite;
GO

USE EBookWebsite;
GO

-- Bảng Users
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    email NVARCHAR(100) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    avatar_url NVARCHAR(500),
    role NVARCHAR(20) DEFAULT 'user' CHECK (role IN ('admin', 'user', 'moderator')),
    status NVARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deleted', 'banned')),
    userinfor_id INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    last_login DATETIME2,
    is_premium BIT DEFAULT 0
);

-- Bảng UserInfor (Thông tin chi tiết user)
CREATE TABLE UserInfor (
    id INT IDENTITY(1,1) PRIMARY KEY,
    phone NVARCHAR(20),
    birth_day DATE,
    gender NVARCHAR(10) CHECK (gender IN ('male', 'female', 'other')),
    address NVARCHAR(500),
    introduction NVARCHAR(1000),
    status NVARCHAR(20) DEFAULT 'active'
);

-- Bảng Authors
CREATE TABLE Authors (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    bio NVARCHAR(1000),
    avatar_url NVARCHAR(500)
);

-- Bảng Tags
CREATE TABLE Tags (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE
);

-- Bảng Ebooks
CREATE TABLE Ebooks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(2000),
    release_type NVARCHAR(50) DEFAULT 'completed',
    language NVARCHAR(20) DEFAULT 'Vietnamese',
    status NVARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'draft', 'completed')),
    visibility NVARCHAR(20) DEFAULT 'public' CHECK (visibility IN ('public', 'private', 'premium')),
    uploader_id INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    view_count INT DEFAULT 0,
    cover_url NVARCHAR(500),
    is_premium BIT DEFAULT 0,
    price DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (uploader_id) REFERENCES Users(id)
);

-- Bảng Volumes
CREATE TABLE Volumes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ebook_id INT NOT NULL,
    title NVARCHAR(200) NOT NULL,
    number INT NOT NULL,
    published_at DATE,
    access_level NVARCHAR(20) DEFAULT 'free' CHECK (access_level IN ('free', 'premium')),
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE
);

-- Bảng Chapters
CREATE TABLE Chapters (
    id INT IDENTITY(1,1) PRIMARY KEY,
    volume_id INT NOT NULL,
    title NVARCHAR(200) NOT NULL,
    content NVARCHAR(MAX),
    chapter_number DECIMAL(5,2) NOT NULL,
    word_count INT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (volume_id) REFERENCES Volumes(id) ON DELETE CASCADE
);

-- Bảng Comments
CREATE TABLE Comments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    chapter_id INT,
    content NVARCHAR(1000) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id) ON DELETE CASCADE
);

-- Bảng Favorites
CREATE TABLE Favorites (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    chapter_id INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id) ON DELETE CASCADE
);

-- Bảng UserRead (Lịch sử đọc)
CREATE TABLE UserRead (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    last_read_chapter_id INT,
    last_read_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (last_read_chapter_id) REFERENCES Chapters(id) ON DELETE SET NULL
);

-- Bảng Orders
CREATE TABLE Orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'cancelled', 'refunded')),
    payment_method NVARCHAR(50),
    order_date DATETIME2 DEFAULT GETDATE(),
    payment_date DATETIME2,
    transaction_id NVARCHAR(100),
    notes NVARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id)
);

-- Bảng EbookAuthors (Many-to-Many)
CREATE TABLE EbookAuthors (
    ebook_id INT NOT NULL,
    author_id INT NOT NULL,
    role NVARCHAR(50) DEFAULT 'author',
    PRIMARY KEY (ebook_id, author_id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES Authors(id) ON DELETE CASCADE
);

-- Bảng EbookTags (Many-to-Many)
CREATE TABLE EbookTags (
    ebook_id INT NOT NULL,
    tag_id INT NOT NULL,
    is_primary BIT DEFAULT 0,
    PRIMARY KEY (ebook_id, tag_id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags(id) ON DELETE CASCADE
);

-- =====================================================
-- INDEXES để tối ưu performance
-- =====================================================

-- Index cho Users
CREATE INDEX IX_Users_Username ON Users(username);
CREATE INDEX IX_Users_Email ON Users(email);
CREATE INDEX IX_Users_Role ON Users(role);
CREATE INDEX IX_Users_Status ON Users(status);

-- Index cho Ebooks
CREATE INDEX IX_Ebooks_Title ON Ebooks(title);
CREATE INDEX IX_Ebooks_Status ON Ebooks(status);
CREATE INDEX IX_Ebooks_Uploader ON Ebooks(uploader_id);
CREATE INDEX IX_Ebooks_Premium ON Ebooks(is_premium);
CREATE INDEX IX_Ebooks_ViewCount ON Ebooks(view_count);

-- Index cho Chapters
CREATE INDEX IX_Chapters_Volume ON Chapters(volume_id);
CREATE INDEX IX_Chapters_Number ON Chapters(chapter_number);

-- Index cho Comments
CREATE INDEX IX_Comments_Ebook ON Comments(ebook_id);
CREATE INDEX IX_Comments_User ON Comments(user_id);
CREATE INDEX IX_Comments_Created ON Comments(created_at);

-- Index cho Orders
CREATE INDEX IX_Orders_User ON Orders(user_id);
CREATE INDEX IX_Orders_Status ON Orders(status);
CREATE INDEX IX_Orders_Date ON Orders(order_date);

-- =====================================================
-- SAMPLE DATA
-- =====================================================

-- Insert sample tags
INSERT INTO Tags (name) VALUES 
('Tiểu thuyết'), ('Truyện ngắn'), ('Khoa học viễn tưởng'), 
('Fantasy'), ('Romance'), ('Hành động'), ('Trinh thám'),
('Lịch sử'), ('Kinh dị'), ('Hài hước');

-- Insert sample authors
INSERT INTO Authors (name, bio) VALUES 
('Nguyễn Nhật Ánh', 'Nhà văn nổi tiếng với các tác phẩm tuổi học trò'),
('Kim Dung', 'Tác giả tiểu thuyết võ hiệp nổi tiếng'),
('J.K. Rowling', 'Tác giả series Harry Potter'),
('Stephen King', 'Nhà văn kinh dị nổi tiếng');

-- Insert sample users
INSERT INTO Users (username, email, password_hash, role) VALUES 
('admin', 'admin@ebook.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin'),
('user1', 'user1@example.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'user');

-- Insert sample ebooks
INSERT INTO Ebooks (title, description, uploader_id, cover_url) VALUES 
('Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'Tác phẩm nổi tiếng của Nguyễn Nhật Ánh', 1, 'https://example.com/cover1.jpg'),
('Harry Potter và Hòn Đá Phù Thủy', 'Tác phẩm đầu tiên trong series Harry Potter', 1, 'https://example.com/cover2.jpg');

-- Insert sample volumes
INSERT INTO Volumes (ebook_id, title, number) VALUES 
(1, 'Tập 1', 1),
(2, 'Phần 1', 1);

-- Insert sample chapters
INSERT INTO Chapters (volume_id, title, content, chapter_number) VALUES 
(1, 'Chương 1: Khởi đầu', 'Nội dung chương 1...', 1.0),
(1, 'Chương 2: Phát triển', 'Nội dung chương 2...', 2.0),
(2, 'Chương 1: Cậu bé sống sót', 'Nội dung chương 1...', 1.0);

-- Insert sample ebook-tag relationships
INSERT INTO EbookTags (ebook_id, tag_id, is_primary) VALUES 
(1, 1, 1), (1, 5, 0),
(2, 4, 1), (2, 1, 0);

-- Insert sample ebook-author relationships
INSERT INTO EbookAuthors (ebook_id, author_id, role) VALUES 
(1, 1, 'author'),
(2, 3, 'author');

GO

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedure để lấy sách theo trang
CREATE PROCEDURE GetBooksByPage
    @Offset INT,
    @PageSize INT
AS
BEGIN
    SELECT * FROM Ebooks 
    WHERE status = 'active'
    ORDER BY created_at DESC
    OFFSET @Offset ROWS
    FETCH NEXT @PageSize ROWS ONLY;
END
GO

-- Procedure để đếm tổng số sách
CREATE PROCEDURE CountAllBooks
AS
BEGIN
    SELECT COUNT(*) FROM Ebooks WHERE status = 'active';
END
GO

-- Procedure để tăng view count
CREATE PROCEDURE IncrementViewCount
    @BookId INT
AS
BEGIN
    UPDATE Ebooks 
    SET view_count = view_count + 1 
    WHERE id = @BookId;
END
GO

PRINT 'Database EBookWebsite created successfully!'; 