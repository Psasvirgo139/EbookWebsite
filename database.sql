-- Tạo database
CREATE DATABASE EBookWebsite;
USE EBookWebsite;

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

use ebookwebsite;
go
CREATE TABLE UserCoins (
    user_id INT NOT NULL PRIMARY KEY,
    coins INT NOT NULL DEFAULT 0,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE UnlockedChapters (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    chapter_id INT NOT NULL,
    coin_spent INT NOT NULL,
    unlocked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (chapter_id) REFERENCES Chapters(id) ON DELETE CASCADE,
    
    UNIQUE(user_id, chapter_id)
);

CREATE INDEX IX_UserCoins_UserId ON UserCoins(user_id);
CREATE INDEX IX_UnlockedChapters_UserId ON UnlockedChapters(user_id);
CREATE INDEX IX_UnlockedChapters_ChapterId ON UnlockedChapters(chapter_id);
CREATE INDEX IX_Chapters_AccessLevel ON Chapters(access_level);

use ebookwebsite;
go

CREATE TABLE Orders	(
	Id INT PRIMARY KEY Identity,
	UserId INT NOT NULL,
	OrderDate DATETIME DEFAULT GETDATE(),
	TotalAmount DECIMAL NOT NULL,
	Status NVARCHAR(20) DEFAULT 'Processing' CHECK (Status IN ('Processing', 'Completed', 'Failed')),
	FOREIGN KEY (UserID) REFERENCES Users(id)
	);

	CREATE TABLE EbookAI (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ebook_id INT NOT NULL,
    file_name NVARCHAR(500) NULL,
    original_file_name NVARCHAR(500) NULL,
    summary NTEXT NULL,
    metadata_json NVARCHAR(MAX) NULL,
    ai_analysis_json NVARCHAR(MAX) NULL,
    filterStatus NVARCHAR(50) NULL,
    filterMessage NVARCHAR(MAX) NULL,
    rejection_reason NVARCHAR(MAX) NULL,
    file_size BIGINT NULL,
    file_path NVARCHAR(MAX) NULL,
    dropbox_link NVARCHAR(MAX) NULL,
    dropbox_file_id NVARCHAR(255) NULL,
    status NVARCHAR(50) DEFAULT 'completed',
    created_at DATETIME2(7) DEFAULT GETDATE(),
    updated_at DATETIME2(7) DEFAULT GETDATE(),

    CONSTRAINT FK_EbookAI_Ebooks FOREIGN KEY (ebook_id) REFERENCES Ebooks(id) ON DELETE CASCADE
);

CREATE TABLE PremiumSubscriptions (
    id int IDENTITY(1,1) PRIMARY KEY,
    user_id int NOT NULL,
    start_date date NOT NULL,           -- Ngày bắt đầu premium
    expiry_date date NOT NULL,          -- Ngày hết hạn premium
    payment_method varchar(20) NOT NULL, -- 'coin' hoặc 'vnd'
    amount decimal(10,2) NOT NULL,      -- Số tiền hoặc coin đã trả
    status varchar(20) NOT NULL DEFAULT 'active', -- 'active', 'expired', 'cancelled', 'replaced'
    created_at datetime2 NOT NULL DEFAULT GETDATE(),
    updated_at datetime2 NOT NULL DEFAULT GETDATE(),
    
    -- Foreign key constraint
    CONSTRAINT FK_PremiumSubscriptions_Users 
        FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    
    -- Check constraints
    CONSTRAINT CK_PremiumSubscriptions_PaymentMethod 
        CHECK (payment_method IN ('coin', 'vnd')),
    CONSTRAINT CK_PremiumSubscriptions_Status 
        CHECK (status IN ('active', 'expired', 'cancelled', 'replaced')),
    CONSTRAINT CK_PremiumSubscriptions_Amount 
        CHECK (amount > 0),
    CONSTRAINT CK_PremiumSubscriptions_Dates 
        CHECK (expiry_date > start_date)
);

-- Tạo index để tối ưu query
CREATE INDEX IX_PremiumSubscriptions_UserId 
    ON PremiumSubscriptions(user_id);

CREATE INDEX IX_PremiumSubscriptions_ExpiryDate 
    ON PremiumSubscriptions(expiry_date);

CREATE INDEX IX_PremiumSubscriptions_Status 
    ON PremiumSubscriptions(status);

CREATE INDEX IX_PremiumSubscriptions_UserStatus 
    ON PremiumSubscriptions(user_id, status) 
    INCLUDE (expiry_date);


INSERT INTO UserCoins (user_id, coins, last_updated) 
VALUES (2, 1000, CURRENT_TIMESTAMP);

-- Cập nhật một số chapter thành premium (ví dụ)
-- UPDATE Chapters SET access_level = 'premium' WHERE number > 5; 

INSERT INTO Ebooks (
    title,
    description,
    release_type,
    language,
    status,
    visibility,
    uploader_id,
    cover_url
)
VALUES (
    N'Không Gia Đình',
    N'Tác phẩm nổi tiếng của Hector Malot kể về hành trình của cậu bé Remi.',
    N'volume',           -- vì đây là sách nguyên cuốn
    N'vi',               -- tiếng Việt
    N'completed',        -- sách đã hoàn thành
    N'public',
    2,                   -- ID người upload (bạn cần chỉnh theo ID thật của bạn trong bảng Users)
    N'C:\Users\NgocKhoi\Desktop\New folder\Không gia đình - Hecto Malot.pdf'
);

INSERT INTO Volumes (
    ebook_id,
    title,
    number,
    published_at,
    access_level
)
VALUES (
    1,                           
    N'Toàn tập',
    1,
    GETDATE(),
    'premium'
);

INSERT INTO Chapters (
    ebook_id,
    volume_id,
    title,
    number,
    content_url,
    access_level
)
VALUES (
    1,                             
    1,                             
    N'Trọn Bộ “Không Gia Đình”',
    1.0,
    N'C:\Users\NgocKhoi\Desktop\New folder\Không gia đình - Hecto Malot.pdf',
    'premium'
);

IF EXISTS (SELECT 1 FROM Users WHERE id = 2)
BEGIN
    INSERT INTO PremiumSubscriptions (
        user_id, start_date, expiry_date, payment_method, amount, status
    ) VALUES (
        2, '2024-12-01', '2025-01-01', 'coin', 100.00, 'expired'
    );

    INSERT INTO PremiumSubscriptions (
        user_id, start_date, expiry_date, payment_method, amount, status
    ) VALUES (
        2, '2025-01-01', '2025-02-01', 'vnd', 100000.00, 'active'
    );
END;