<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/header.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Liên hệ | Scroll</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://fonts.googleapis.com/css?family=Inter:400,600,700&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Inter', Arial, sans-serif;
      background: #fafbfc;
      color: #22223b;
      margin: 0;
      padding: 0;
    }
    .policy-container {
      max-width: 820px;
      margin: 38px auto 0 auto;
      background: #fff;
      border-radius: 18px;
      box-shadow: 0 4px 24px 0 rgba(80,60,120,0.07);
      padding: 38px 22px 32px 22px;
    }
    .policy-title {
      font-size: 2.1em;
      font-weight: 700;
      color: #764ba2;
      margin-bottom: 18px;
      letter-spacing: -1px;
    }
    .policy-section {
      margin-bottom: 22px;
      line-height: 1.7;
    }
    .policy-section-title {
      font-size: 1.18em;
      font-weight: 600;
      color: #3a2c6b;
      margin-bottom: 7px;
      margin-top: 18px;
    }
    .policy-list {
      margin: 0 0 0 18px;
      padding: 0;
    }
    .policy-list li {
      margin-bottom: 6px;
      line-height: 1.7;
    }
    .policy-contact {
      margin-top: 10px;
      font-weight: 500;
      color: #5f4bb6;
    }
    .policy-back {
      display: inline-block;
      margin-bottom: 18px;
      color: #764ba2;
      text-decoration: none;
      font-weight: 600;
      font-size: 1.05em;
      transition: color 0.18s;
    }
    .policy-back:hover {
      color: #b39ddb;
      text-decoration: underline;
    }
    @media (max-width: 600px) {
      .policy-container {
        padding: 16px 4vw 18px 4vw;
      }
      .policy-title {
        font-size: 1.3em;
      }
    }
  </style>
</head>
<body>
  <div class="policy-container">
    <div class="policy-title">Liên hệ</div>
    <div class="policy-section">
      Xin chào bạn – độc giả thân yêu của Scroll.vn,
    </div>
    <div class="policy-section">
      Lời đầu tiên, Scroll xin gửi đến bạn và những người thân yêu lời chúc sức khỏe, thành công và nhiều niềm vui trong cuộc sống.
    </div>
    <div class="policy-section">
      Scroll được xây dựng với sứ mệnh:
      <ul class="policy-list">
        <li>📘 Chia sẻ sách miễn phí đến những bạn đọc chưa có điều kiện tiếp cận tài liệu chất lượng.</li>
        <li>📙 Khuyến khích văn hóa đọc có trách nhiệm – nếu bạn có điều kiện, hãy mua sách giấy để ủng hộ Tác giả và Nhà xuất bản.</li>
        <li>📗 Tổng hợp sách từ nhiều nguồn trên Internet với tinh thần tôn trọng bản quyền và cộng đồng chia sẻ tri thức.</li>
      </ul>
    </div>
    <div class="policy-section">
      Nếu bạn có bất kỳ góp ý, phản hồi, yêu cầu gỡ sách, hoặc đề xuất hợp tác, vui lòng liên hệ với chúng tôi qua:
      <ul class="policy-list">
        <li>📩 <b>Email:</b> <a href="mailto:scrollteam@gmail.com">scrollteam@gmail.com</a></li>
      </ul>
    </div>
    <div class="policy-section">
      ⏱ Chúng tôi cam kết phản hồi trong vòng 7 ngày làm việc.
    </div>
  </div>
  <%@ include file="/common/footer.jsp" %>
</body>
</html> 