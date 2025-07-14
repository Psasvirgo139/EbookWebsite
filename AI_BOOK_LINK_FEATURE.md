# 🤖 AI BOOK LINK FEATURE

## 📋 Tổng quan

Tính năng cho phép AI Chat tạo link trực tiếp đến sách khi user muốn đọc sách cụ thể.

## 🎯 Tính năng chính

### 1. **Gợi ý sách với link**
- AI sẽ hiển thị danh sách sách có sẵn trong database
- Mỗi sách có thông tin chi tiết và link trực tiếp

### 2. **Tạo link khi user muốn đọc sách**
- User có thể nói: "Tôi muốn đọc cuốn sách đầu tiên"
- AI sẽ trả về: "Đây là sách **Tên sách**: http://localhost:9999/EbookWebsite/book/detail?id=47"

### 3. **Hỗ trợ nhiều cách gọi**
- Theo số thứ tự: "sách đầu tiên", "sách thứ hai", "thứ nhất", "thứ hai"
- Theo tên sách: "Tôi muốn đọc Nhà Thờ Đức Bà Paris"

## 🏗️ Kiến trúc

### **Model mới: BookWithLink**
```java
public class BookWithLink {
    private int id;
    private String title;
    private String directLink; // Link trực tiếp
    private String shortDescription;
    // ... other fields
}
```

### **Utils methods mới:**
- `getAvailableBooksWithLinks(int limit)` - Lấy sách với link
- `findBookByIndex(int index)` - Tìm sách theo số thứ tự
- `findBookByTitle(String title)` - Tìm sách theo tên

### **AI Chat Service cải tiến:**
- Nhận diện câu hỏi gợi ý sách
- Nhận diện yêu cầu đọc sách cụ thể
- Tạo link trực tiếp cho user

## 🔄 Luồng hoạt động

### **Scenario 1: Gợi ý sách**
```
User: "Hãy gợi ý cho tôi 3 cuốn sách"
AI: "📚 Dưới đây là những cuốn sách có sẵn trong thư viện:
1. **Nhà Thờ Đức Bà Paris** (Phiêu lưu)
   Mô tả ngắn gọn...
2. **Ba Chàng Ngốc** (Tiểu thuyết)
   Mô tả ngắn gọn...
Bạn muốn đọc cuốn nào? Hãy nhập tên hoặc số thứ tự!"
```

### **Scenario 2: Đọc sách cụ thể**
```
User: "Tôi muốn đọc cuốn sách đầu tiên"
AI: "Đây là sách **Nhà Thờ Đức Bà Paris**: http://localhost:9999/EbookWebsite/book/detail?id=47"
```

## 🧪 Test Cases

### **Test 1: Gợi ý sách**
- Input: "Hãy gợi ý cho tôi 3 cuốn sách"
- Expected: Danh sách sách với thông tin chi tiết

### **Test 2: Đọc sách theo số**
- Input: "Tôi muốn đọc cuốn sách đầu tiên"
- Expected: Link trực tiếp đến sách đầu tiên

### **Test 3: Đọc sách theo tên**
- Input: "Tôi muốn đọc Nhà Thờ Đức Bà Paris"
- Expected: Link trực tiếp đến sách đó

### **Test 4: Sách không tồn tại**
- Input: "Tôi muốn đọc sách không có"
- Expected: Thông báo không tìm thấy sách

## 🔧 Cách sử dụng

### **Trong AI Chat:**
1. Truy cập `/ai/chat`
2. Hỏi: "Hãy gợi ý cho tôi sách"
3. Chọn sách muốn đọc: "Tôi muốn đọc cuốn sách đầu tiên"
4. Click vào link được tạo ra

### **Các câu hỏi được hỗ trợ:**
- "Hãy gợi ý sách"
- "Tôi muốn đọc cuốn sách đầu tiên"
- "Đọc cuốn sách thứ hai"
- "Tôi muốn đọc [tên sách]"

## ✅ Lợi ích

1. **Trải nghiệm người dùng tốt hơn** - Link trực tiếp thay vì phải tìm kiếm
2. **Tương tác tự nhiên** - User có thể nói tự nhiên với AI
3. **Tích hợp database** - Chỉ hiển thị sách có thực tế trong database
4. **Mở rộng dễ dàng** - Có thể thêm nhiều pattern nhận diện

## 🚀 Tương lai

- Thêm nhận diện theo tác giả
- Thêm nhận diện theo thể loại
- Thêm link đến chapter cụ thể
- Thêm thông tin chi tiết hơn về sách 