# 📚 EbookWebsite - Website Đọc Truyện Online

Một website đọc truyện online hoàn chỉnh được xây dựng bằng Java Web với kiến trúc MVC, hỗ trợ đọc truyện, quản lý người dùng, và hệ thống premium.

## 🚀 Tính Năng Chính

### 👥 Quản Lý Người Dùng
- ✅ Đăng ký, đăng nhập, đăng xuất
- ✅ Quản lý thông tin cá nhân
- ✅ Quên mật khẩu và đặt lại mật khẩu
- ✅ Phân quyền người dùng (User/Admin)

### 📖 Quản Lý Truyện
- ✅ Xem danh sách truyện với phân trang
- ✅ Tìm kiếm và lọc truyện
- ✅ Xem chi tiết truyện
- ✅ Đọc truyện theo chương
- ✅ Hệ thống Volume và Chapter
- ✅ Quản lý tác giả và tag

### 💬 Tương Tác
- ✅ Bình luận truyện
- ✅ Yêu thích truyện
- ✅ Theo dõi tiến độ đọc

### 💎 Hệ Thống Premium
- ✅ Truyện premium có phí
- ✅ Quản lý đơn hàng
- ✅ Hệ thống thanh toán

### 🔧 Admin Dashboard
- ✅ Quản lý truyện (CRUD)
- ✅ Quản lý tag
- ✅ Thống kê và báo cáo
- ✅ Quản lý người dùng

## 🛠️ Công Nghệ Sử Dụng

### Backend
- **Java 17** - Ngôn ngữ lập trình chính
- **Jakarta EE 10** - Enterprise Java
- **Servlets & JSP** - Web framework
- **Maven** - Build tool và dependency management

### Database
- **Microsoft SQL Server** - Hệ quản trị cơ sở dữ liệu
- **JDBC** - Kết nối database

### Frontend
- **HTML5, CSS3, JavaScript** - Frontend cơ bản
- **Bootstrap 5** - UI framework
- **Font Awesome** - Icon library
- **JSTL** - JSP Standard Tag Library

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Nginx** - Reverse proxy và load balancing
- **Maven** - Build automation

## 📁 Cấu Trúc Dự Án

```
EbookWebsite/
├── src/
│   ├── main/
│   │   ├── java/com/mycompany/ebookwebsite/
│   │   │   ├── controller/          # Servlets
│   │   │   ├── dao/                 # Data Access Objects
│   │   │   ├── model/               # Entity classes
│   │   │   ├── service/             # Business logic
│   │   │   ├── filter/              # Servlet filters
│   │   │   ├── listener/            # Context listeners
│   │   │   └── utils/               # Utility classes
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   └── webapp/
│   │       ├── admin/               # Admin pages
│   │       ├── book/                # Book pages
│   │       ├── common/              # Shared components
│   │       ├── content/             # Static content
│   │       └── WEB-INF/
│   └── test/                        # Test files
├── database_schema.sql              # Database schema
├── pom.xml                          # Maven configuration
├── Dockerfile                       # Docker configuration
├── docker-compose.yml               # Docker Compose
├── nginx.conf                       # Nginx configuration
├── deploy.bat                       # Deployment script
└── README.md                        # Project documentation
```

## 🚀 Hướng Dẫn Cài Đặt

### Yêu Cầu Hệ Thống
- Java 17 hoặc cao hơn
- Maven 3.6+
- Microsoft SQL Server 2019+
- Tomcat 10+ (cho deployment thủ công)
- Docker & Docker Compose (cho deployment containerized)

### Cách 1: Chạy Thủ Công

#### 1. Cài Đặt Database
```sql
-- Chạy script database_schema.sql trong SQL Server
-- Hoặc sử dụng SQL Server Management Studio
```

#### 2. Cấu Hình Database
Chỉnh sửa file `src/main/webapp/META-INF/context.xml`:
```xml
<Resource name="jdbc/ebookwebsite" 
          auth="Container" 
          type="javax.sql.DataSource"
          username="your_username" 
          password="your_password"
          driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
          url="jdbc:sqlserver://localhost:1433;databaseName=EbookWebsite;encrypt=true;trustServerCertificate=true"
          maxTotal="20" 
          maxIdle="10"
          maxWaitMillis="-1"/>
```

#### 3. Build và Deploy
```bash
# Build project
mvn clean package

# Copy WAR file to Tomcat
cp target/EbookWebsite-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh
```

### Cách 2: Sử Dụng Docker (Khuyến Nghị)

#### 1. Clone Repository
```bash
git clone <repository-url>
cd EbookWebsite
```

#### 2. Chạy với Docker Compose
```bash
# Build và chạy toàn bộ hệ thống
docker-compose up -d

# Xem logs
docker-compose logs -f

# Dừng hệ thống
docker-compose down
```

#### 3. Truy Cập Ứng Dụng
- Website: http://localhost:8080
- Admin Dashboard: http://localhost:8080/admin/dashboard.jsp

## 🔐 Tài Khoản Mặc Định

### Admin Account
- **Username:** admin
- **Password:** admin

### Test User Account
- **Username:** user
- **Password:** user

## 📊 Database Schema

### Bảng Chính
- **Users** - Thông tin người dùng
- **Ebooks** - Thông tin truyện
- **Volumes** - Tập truyện
- **Chapters** - Chương truyện
- **Authors** - Tác giả
- **Tags** - Thẻ phân loại
- **Comments** - Bình luận
- **Favorites** - Yêu thích
- **Orders** - Đơn hàng
- **UserReads** - Tiến độ đọc

### Quan Hệ
- Một truyện có nhiều Volume
- Một Volume có nhiều Chapter
- Một truyện có nhiều Author (Many-to-Many)
- Một truyện có nhiều Tag (Many-to-Many)
- Một user có thể yêu thích nhiều truyện
- Một user có thể bình luận nhiều truyện

## 🔧 Cấu Hình Nâng Cao

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=1433
DB_NAME=EbookWebsite
DB_USER=sa
DB_PASSWORD=your_password

# Application
JAVA_OPTS=-Xmx512m -Xms256m
CATALINA_OPTS=-Djava.security.egd=file:/dev/./urandom
```

### SSL/HTTPS Configuration
1. Tạo SSL certificates
2. Cập nhật `nginx.conf`
3. Uncomment HTTPS server block
4. Restart Nginx

### Load Balancing
Thêm nhiều application servers vào upstream trong `nginx.conf`:
```nginx
upstream ebookwebsite {
    server app1:8080;
    server app2:8080;
    server app3:8080;
}
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Manual Testing
1. Đăng ký tài khoản mới
2. Đăng nhập và test các chức năng
3. Test admin dashboard
4. Test đọc truyện và bình luận

## 📈 Monitoring và Logging

### Application Logs
- Tomcat logs: `$TOMCAT_HOME/logs/`
- Application logs: `src/main/webapp/WEB-INF/logs/`

### Database Monitoring
```sql
-- Xem số lượng truyện
SELECT COUNT(*) FROM Ebooks WHERE status = 'active';

-- Xem user đăng ký mới nhất
SELECT TOP 10 * FROM Users ORDER BY created_at DESC;

-- Xem truyện được xem nhiều nhất
SELECT TOP 10 title, view_count FROM Ebooks ORDER BY view_count DESC;
```

## 🚀 Deployment

### Production Deployment
1. **Database Setup**
   ```bash
   # Tạo production database
   CREATE DATABASE EbookWebsite_Prod;
   ```

2. **Application Deployment**
   ```bash
   # Build production WAR
   mvn clean package -Pprod
   
   # Deploy to production server
   scp target/EbookWebsite-1.0-SNAPSHOT.war user@server:/opt/tomcat/webapps/
   ```

3. **SSL Configuration**
   ```bash
   # Install SSL certificates
   sudo certbot --nginx -d yourdomain.com
   ```

### CI/CD Pipeline
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build with Maven
        run: mvn clean package
      - name: Deploy to Server
        run: |
          scp target/EbookWebsite-1.0-SNAPSHOT.war user@server:/opt/tomcat/webapps/
```

## 🔒 Bảo Mật

### Security Headers
- X-Frame-Options: SAMEORIGIN
- X-XSS-Protection: 1; mode=block
- X-Content-Type-Options: nosniff
- Content-Security-Policy: default-src 'self'

### Rate Limiting
- Login: 5 requests/minute
- API: 10 requests/second
- Admin: 20 requests/second

### SQL Injection Prevention
- Sử dụng PreparedStatement
- Input validation và sanitization
- Parameterized queries

## 🐛 Troubleshooting

### Lỗi Thường Gặp

#### 1. Database Connection Error
```
Error: Cannot connect to database
Solution: Kiểm tra connection string và credentials
```

#### 2. Tomcat Startup Error
```
Error: Port 8080 already in use
Solution: sudo netstat -tulpn | grep :8080
```

#### 3. Docker Container Issues
```bash
# Xem logs
docker-compose logs app

# Restart container
docker-compose restart app

# Rebuild image
docker-compose build --no-cache
```

### Performance Issues
1. **Database Optimization**
   ```sql
   -- Tạo indexes
   CREATE INDEX idx_ebooks_status ON Ebooks(status);
   CREATE INDEX idx_chapters_book_id ON Chapters(book_id);
   ```

2. **Caching**
   - Implement Redis caching
   - Use CDN for static assets
   - Enable browser caching

3. **Connection Pooling**
   - Tăng maxTotal trong context.xml
   - Monitor connection usage

## 📝 API Documentation

### REST Endpoints
```
GET    /api/books              # Lấy danh sách truyện
GET    /api/books/{id}         # Lấy chi tiết truyện
POST   /api/books              # Tạo truyện mới
PUT    /api/books/{id}         # Cập nhật truyện
DELETE /api/books/{id}         # Xóa truyện

GET    /api/chapters/{id}      # Lấy nội dung chương
POST   /api/comments           # Tạo bình luận
POST   /api/favorites          # Thêm yêu thích
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Email:** support@ebookwebsite.com
- **Documentation:** [Wiki](https://github.com/your-repo/wiki)
- **Issues:** [GitHub Issues](https://github.com/your-repo/issues)

## 🗺️ Roadmap

### Version 2.0 (Q2 2024)
- [ ] Mobile app (React Native)
- [ ] Advanced search filters
- [ ] Reading progress sync
- [ ] Social features (following, sharing)
- [ ] Payment gateway integration

### Version 3.0 (Q3 2024)
- [ ] AI-powered recommendations
- [ ] Multi-language support
- [ ] Advanced analytics
- [ ] Content moderation tools
- [ ] API for third-party integrations

---

**⭐ Nếu dự án này hữu ích, hãy cho chúng tôi một star!** 