-- XÓA DATABASE CŨ (nếu có)
IF DB_ID('EBookWebsite') IS NOT NULL
    DROP DATABASE EBookWebsite;
GO

-- TẠO DATABASE MỚI
CREATE DATABASE EBookWebsiteTest1;
GO
USE EBookWebsiteTest1;
GO

-- USER INFORMATION
CREATE TABLE UserInfor (
    id INT IDENTITY(1,1) PRIMARY KEY,
    phone NVARCHAR(20),
    birthday DATE,
    gender NVARCHAR(10),
    address NVARCHAR(255),
    introduction NVARCHAR(MAX)
);

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
    status NVARCHAR(20) DEFAULT 'active',
    last_login DATETIME NULL,
    is_premium BIT DEFAULT 0,
    FOREIGN KEY (userinfor_id) REFERENCES UserInfor(id)
);

-- TAGS
CREATE TABLE Tags (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
);

-- AUTHORS
CREATE TABLE Authors (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    bio NVARCHAR(MAX),
    avatar_url NVARCHAR(MAX)
);

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
    view_count INT DEFAULT 0,
    cover_url NVARCHAR(MAX),
    is_premium BIT DEFAULT 0,
    price FLOAT DEFAULT 0,
    FOREIGN KEY (uploader_id) REFERENCES Users(id)
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

-- EBOOK-AUTHORS
CREATE TABLE EbookAuthors (
    ebook_id INT,
    author_id INT,
    role NVARCHAR(100),
    PRIMARY KEY (ebook_id, author_id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (author_id) REFERENCES Authors(id)
);

-- VOLUMES
CREATE TABLE Volumes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ebook_id INT NOT NULL,
    title NVARCHAR(255),
    number INT,
    published_at DATE,
    access_level NVARCHAR(20) DEFAULT 'public',
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
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
    access_level NVARCHAR(20) DEFAULT 'public',
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    is_free BIT DEFAULT 1,
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (volume_id) REFERENCES Volumes(id)
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
    parent_comment_id INT NULL,
    like_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id),
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id),
    FOREIGN KEY (parent_comment_id) REFERENCES Comments(id)
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

-- ORDERS (Giao dịch mua premium)
CREATE TABLE Orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    ebook_id INT NOT NULL,
    amount FLOAT NOT NULL,
    status NVARCHAR(20) DEFAULT 'pending',
    payment_method NVARCHAR(50),
    order_date DATETIME DEFAULT GETDATE(),
    payment_date DATETIME,
    transaction_id NVARCHAR(100),
    notes NVARCHAR(MAX),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (ebook_id) REFERENCES Ebooks(id)
);

INSERT INTO UserInfor (phone, birthday, gender, address, introduction)
VALUES 
('0123456789', '1990-01-01', 'Male', N'Hà Nội', N'Quản trị viên demo'),
('0987654321', '1995-05-10', 'Female', N'Hồ Chí Minh', N'Bạn đọc yêu thích truyện'),
('0911222333', '2000-12-20', 'Male', N'Đà Nẵng', N'Bạn đọc mới');

INSERT INTO Users (username, email, password_hash, userinfor_id, is_premium)
VALUES 
('admin', 'admin@email.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, 1),
('user1', 'user1@email.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 2, 0),
('user2', 'user2@email.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 3, 1);

delete from Users;
TRUNCATE TABLE Users;

select * from Users;
INSERT INTO Users (username, email, password_hash, is_premium)
VALUES ('user3', 'user3@email.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 0);

INSERT INTO Tags (name) VALUES 
(N'Java'), (N'Web'), (N'Lập trình'), (N'Tiểu thuyết'), (N'Kinh dị'), (N'Hài hước'), (N'Truyện ngắn');

INSERT INTO Authors (name, bio) VALUES 
(N'Nguyễn Văn A', N'Chuyên gia Java'),
(N'Nguyễn Nhật Ánh', N'Nhà văn nổi tiếng với truyện thiếu nhi'),
(N'Arthur Conan Doyle', N'Tác giả Sherlock Holmes'),
(N'J.K. Rowling', N'Tác giả Harry Potter');

INSERT INTO Ebooks (title, description, release_type, language, uploader_id, is_premium, price)
VALUES 
(N'Sách Java Premium', N'Giới thiệu lập trình Java nâng cao', 'completed', 'vi', 1, 1, 50000),
(N'Sách Web Cơ Bản', N'Giới thiệu lập trình web', 'ongoing', 'vi', 1, 0, 0),
(N'Kính Vạn Hoa', N'Truyện thiếu nhi nổi tiếng', 'completed', 'vi', 2, 0, 0),
(N'Sherlock Holmes', N'Truyện trinh thám kinh điển', 'completed', 'en', 3, 1, 70000),
(N'Harry Potter', N'Truyện phép thuật nổi tiếng', 'completed', 'en', 1, 1, 90000);

INSERT INTO EbookTags (ebook_id, tag_id, is_primary) VALUES 
(1, 1, 1), (2, 2, 1), (3, 4, 1), (4, 2, 1), (5, 1, 1), (5, 3, 0);

INSERT INTO EbookAuthors (ebook_id, author_id, role) VALUES 
(1, 1, N'Tác giả chính'), (2, 1, N'Tác giả chính'), (3, 1, N'Tác giả chính'), (4, 2, N'Tác giả chính'), (5, 3, N'Tác giả chính');

INSERT INTO Volumes (ebook_id, title, number, published_at)
VALUES 
(1, N'Tập 1', 1, '2023-01-01'), 
(2, N'Tập 1', 1, '2023-01-01'),
(3, N'Tập 1', 1, '2022-01-01'),
(4, N'Tập 1', 1, '2021-01-01'),
(5, N'Tập 1', 1, '2020-01-01');

INSERT INTO Chapters (ebook_id, volume_id, title, number, is_free)
VALUES 
(1, 1, N'Chương 1 (Free)', 1, 1), 
(1, 1, N'Chương 2 (Premium)', 2, 0), 
(2, 2, N'Chương 1', 1, 1),
(3, 3, N'Chương 1: Khởi đầu', 1, 1),
(3, 3, N'Chương 2: Bí mật', 2, 0),
(4, 4, N'Chương 1: Vụ án đầu tiên', 1, 1),
(4, 4, N'Chương 2: Manh mối', 2, 0),
(5, 5, N'Chương 1: Cậu bé sống sót', 1, 1),
(5, 5, N'Chương 2: Thư mời nhập học', 2, 0);

INSERT INTO Favorites (user_id, ebook_id, chapter_id) VALUES 
(1, 1, 1), (1, 2, 3), (2, 3, 4), (3, 4, 6), (1, 5, 8);

INSERT INTO Comments (user_id, ebook_id, chapter_id, content)
VALUES 
(1, 1, 1, N'Bài viết rất hay!'), 
(1, 2, 3, N'Bài này hữu ích.'),
(2, 3, 4, N'Truyện này tuổi thơ của mình!'),
(3, 4, 6, N'Rất thích các vụ án của Holmes!'),
(1, 5, 8, N'Harry Potter là huyền thoại!');

INSERT INTO UserReads (user_id, ebook_id, last_read_chapter_id)
VALUES 
(1, 1, 1), (1, 2, 3), (2, 3, 5), (3, 4, 7), (1, 5, 9);

INSERT INTO Orders (user_id, ebook_id, amount, status, payment_method, order_date, payment_date, transaction_id, notes)
VALUES 
(1, 1, 50000, 'completed', 'credit_card', GETDATE(), GETDATE(), 'TXN001', N'Mua sách premium'),
(2, 4, 70000, 'completed', 'paypal', GETDATE(), GETDATE(), 'TXN002', N'Mua Sherlock Holmes'),
(3, 5, 90000, 'completed', 'credit_card', GETDATE(), GETDATE(), 'TXN003', N'Mua Harry Potter');


UPDATE Users
SET role = 'admin'
WHERE id = 1;

    

UPDATE Ebooks SET status = 'active';