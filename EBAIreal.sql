/* 1) Đảm bảo chỉ chèn những ebook CHƯA có trong EbookAI */
INSERT INTO dbo.EbookAI
(
    -- KHÔNG liệt kê cột [id] nếu là IDENTITY
    ebook_id,
    file_name,
    original_file_name,
    summary,
    metadata_json,
    ai_analysis_json,
    filterStatus,
    filterMessage,
    rejection_reason,
    file_size,
    file_path,
    dropbox_link,
    dropbox_file_id,
    status,
    created_at,
    updated_at
)
SELECT
    e.id                                        AS ebook_id,
    NULL                                        AS file_name,          -- sẽ cập nhật sau
    NULL                                        AS original_file_name, -- ...
    e.[description]                             AS summary,            -- tận dụng mô tả
    NULL                                        AS metadata_json,
    NULL                                        AS ai_analysis_json,
    NULL                                        AS filterStatus,
    NULL                                        AS filterMessage,
    NULL                                        AS rejection_reason,
    NULL                                        AS file_size,
    NULL                                        AS file_path,
    NULL                                        AS dropbox_link,
    NULL                                        AS dropbox_file_id,
    'pending'                                   AS [status],           -- tuỳ quy ước của bạn
    SYSDATETIME()                               AS created_at,         -- hoặc e.created_at
    SYSDATETIME()                               AS updated_at
FROM dbo.Ebooks e
LEFT JOIN dbo.EbookAI ai
       ON ai.ebook_id = e.id
WHERE ai.ebook_id IS NULL;   -- bảo đảm không trùng



ALTER TABLE EbookAI
ADD cover_url NVARCHAR(255);  -- hoặc kiểu khác tùy vào schema của bạn


UPDATE ai
SET ai.cover_url = eb.cover_url
FROM EbookAI ai
JOIN Ebooks eb ON ai.ebook_id = eb.id;
