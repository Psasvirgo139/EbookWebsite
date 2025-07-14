# 🔢 AI BOOK COUNT FEATURE

## 📋 Tổng quan

Tính năng cho phép AI Chat đọc và hiểu số lượng sách mà user muốn được gợi ý từ message, thay vì hard-code một số lượng cố định.

## 🎯 Tính năng chính

### 1. **Đọc số lượng từ user message**
- AI sẽ phân tích message để tìm số lượng sách user muốn
- Hỗ trợ cả số Ả Rập (1, 2, 3...) và số tiếng Việt (một, hai, ba...)
- Giới hạn từ 1-10 sách để tránh quá tải

### 2. **Pattern matching thông minh**
- Regex pattern: `\b(\d+)\s*(cuốn|cuốn sách|sách|book|books)\b`
- Hỗ trợ các format: "3 cuốn sách", "5 sách", "1 book"
- Fallback về từ khóa: "một", "hai", "ba", "bốn", "năm"

### 3. **Response động**
- Hiển thị đúng số lượng sách được yêu cầu
- Thông báo: "Dưới đây là X cuốn sách có sẵn trong thư viện"

## 🏗️ Kiến trúc

### **Method mới: extractBookCountFromMessage**
```java
private int extractBookCountFromMessage(String userMessage) {
    // 1. Tìm số bằng regex
    // 2. Parse và validate (1-10)
    // 3. Fallback về từ khóa tiếng Việt
    // 4. Default: 3 sách
}
```

### **Logic cải tiến:**
```java
// Thay vì hard-code
List<BookWithLink> books = Utils.getAvailableBooksWithLinks(5);

// Bây giờ đọc từ user message
int bookCount = extractBookCountFromMessage(userMessage);
List<BookWithLink> books = Utils.getAvailableBooksWithLinks(bookCount);
```

## 🔄 Luồng hoạt động

### **Scenario 1: User yêu cầu 3 sách**
```
User: "Hãy gợi ý cho tôi 3 cuốn sách"
AI: "📚 Dưới đây là 3 cuốn sách có sẵn trong thư viện:
1. **Sách 1** (Thể loại)
   Mô tả...
   🔗 Xem chi tiết
2. **Sách 2** (Thể loại)
   Mô tả...
   🔗 Xem chi tiết
3. **Sách 3** (Thể loại)
   Mô tả...
   🔗 Xem chi tiết"
```

### **Scenario 2: User yêu cầu 1 sách**
```
User: "Gợi ý 1 cuốn sách"
AI: "📚 Dưới đây là 1 cuốn sách có sẵn trong thư viện:
1. **Sách 1** (Thể loại)
   Mô tả...
   🔗 Xem chi tiết"
```

### **Scenario 3: User không chỉ định số lượng**
```
User: "Đề xuất sách"
AI: "📚 Dưới đây là 3 cuốn sách có sẵn trong thư viện: (default)"
```

## 🧪 Test Cases

### **Test 1: Số Ả Rập**
- Input: "Hãy gợi ý cho tôi 3 cuốn sách"
- Expected: 3 sách

### **Test 2: Số tiếng Việt**
- Input: "Gợi ý hai cuốn sách"
- Expected: 2 sách

### **Test 3: Không có số**
- Input: "Đề xuất sách"
- Expected: 3 sách (default)

### **Test 4: Số vượt quá giới hạn**
- Input: "Hãy gợi ý cho tôi 15 cuốn sách"
- Expected: 10 sách (giới hạn tối đa)

### **Test 5: Số 0 hoặc âm**
- Input: "Gợi ý 0 cuốn sách"
- Expected: 1 sách (giới hạn tối thiểu)

## 🔧 Cách sử dụng

### **Các format được hỗ trợ:**
- "Hãy gợi ý cho tôi 3 cuốn sách"
- "Đề xuất 5 sách"
- "Gợi ý 1 cuốn sách"
- "Hãy gợi ý cho tôi 10 cuốn sách"
- "Gợi ý hai cuốn sách"
- "Đề xuất ba cuốn sách"
- "Đề xuất sách" (default 3)

### **Giới hạn:**
- Tối thiểu: 1 sách
- Tối đa: 10 sách
- Default: 3 sách (khi không chỉ định)

## ✅ Lợi ích

1. **Linh hoạt** - User có thể yêu cầu số lượng sách mong muốn
2. **Thông minh** - AI hiểu được nhiều format khác nhau
3. **An toàn** - Giới hạn số lượng để tránh quá tải
4. **User-friendly** - Hỗ trợ cả số Ả Rập và tiếng Việt

## 🔍 Pattern Matching

### **Regex Pattern:**
```regex
\b(\d+)\s*(cuốn|cuốn sách|sách|book|books)\b
```

### **Ví dụ matches:**
- "3 cuốn sách" → 3
- "5 sách" → 5
- "1 book" → 1
- "10 cuốn" → 10

### **Từ khóa tiếng Việt:**
- "một" → 1
- "hai" → 2
- "ba" → 3
- "bốn" → 4
- "năm" → 5
- ... đến "mười" → 10

## 🚀 Tương lai

- Thêm hỗ trợ cho số lớn hơn (11-20)
- Thêm hỗ trợ cho số thập phân ("2.5 cuốn sách")
- Thêm hỗ trợ cho số La Mã
- Thêm hỗ trợ cho các ngôn ngữ khác 