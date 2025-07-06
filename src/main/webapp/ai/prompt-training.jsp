<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prompt Training Lab - EbookWebsite</title>
    <link rel="stylesheet" href="${ctx}/assets/css/style.css">
    <style>
        .training-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 20px;
            color: white;
        }
        
        .training-header {
            text-align: center;
            margin-bottom: 30px;
            padding: 20px;
            background: rgba(255,255,255,0.1);
            border-radius: 15px;
            backdrop-filter: blur(10px);
        }
        
        .training-header h1 {
            margin: 0;
            font-size: 2.2rem;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }
        
        .lab-grid {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .lab-section {
            background: rgba(255,255,255,0.95);
            color: #333;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
        }
        
        .lab-section h3 {
            margin-top: 0;
            color: #667eea;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        
        .input-group {
            margin-bottom: 15px;
        }
        
        .input-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        
        .input-group input, .input-group textarea {
            width: 100%;
            padding: 10px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .input-group input:focus, .input-group textarea:focus {
            border-color: #667eea;
            outline: none;
        }
        
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s;
            margin-right: 10px;
            margin-bottom: 10px;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        
        .btn-danger {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
        }
        
        .btn-success {
            background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
        }
        
        .result-box {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-top: 15px;
            font-family: 'Courier New', monospace;
            white-space: pre-wrap;
            max-height: 200px;
            overflow-y: auto;
        }
        
        .result-box.success {
            background: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
        }
        
        .result-box.error {
            background: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }
        
        .similarity-meter {
            width: 100%;
            height: 20px;
            background: #e9ecef;
            border-radius: 10px;
            overflow: hidden;
            margin: 10px 0;
        }
        
        .similarity-fill {
            height: 100%;
            background: linear-gradient(90deg, #ff6b6b 0%, #ffa500 50%, #4ecdc4 100%);
            transition: width 0.5s ease;
        }
        
        .session-info {
            background: rgba(255,255,255,0.95);
            color: #333;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 20px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }
        
        .info-item {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        
        .info-item strong {
            color: #667eea;
        }
        
        .advanced-chat {
            grid-column: 1 / -1;
            background: rgba(255,255,255,0.95);
            color: #333;
            padding: 25px;
            border-radius: 15px;
        }
        
        .chat-messages {
            height: 300px;
            overflow-y: auto;
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 15px;
            margin: 15px 0;
            background: #f8f9fa;
        }
        
        .message {
            margin-bottom: 15px;
            padding: 10px 15px;
            border-radius: 10px;
            max-width: 80%;
        }
        
        .message.user {
            background: #667eea;
            color: white;
            margin-left: auto;
        }
        
        .message.ai {
            background: white;
            border: 2px solid #667eea;
            color: #333;
        }
        
        .loading {
            display: none;
            text-align: center;
            color: #667eea;
            font-style: italic;
            padding: 10px;
        }
        
        .feature-badge {
            display: inline-block;
            background: #4ecdc4;
            color: white;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 11px;
            margin: 2px;
        }
    </style>
</head>
<body>
    <%@ include file="/common/header.jsp" %>
    
    <main class="training-container">
        <div class="training-header">
            <h1>🧠 Prompt Training Lab</h1>
            <p>Chào mừng <strong>${userName}</strong> đến với phòng thí nghiệm AI nâng cao!</p>
            <div style="margin-top: 15px;">
                <span class="feature-badge">Vector Similarity</span>
                <span class="feature-badge">Session Memory</span>
                <span class="feature-badge">Prompt Validation</span>
                <span class="feature-badge">Database-only Responses</span>
                <span class="feature-badge">Smart Link Generation</span>
            </div>
        </div>
        
        <!-- Session Information -->
        <div class="session-info">
            <h3>📊 Thông Tin Session</h3>
            <div class="info-grid">
                <div class="info-item">
                    <strong>User ID:</strong> ${userId}
                </div>
                <div class="info-item">
                    <strong>Session:</strong> <span id="sessionId">Loading...</span>
                </div>
                <div class="info-item">
                    <strong>Sách đã hỏi:</strong> <span id="askedBooks">Loading...</span>
                </div>
                <div class="info-item">
                    <strong>Từ khóa quan tâm:</strong> <span id="keywords">Loading...</span>
                </div>
            </div>
            <div style="text-align: center; margin-top: 15px;">
                <button class="btn" onclick="loadSessionInfo()">🔄 Tải lại thông tin</button>
                <button class="btn btn-danger" onclick="clearSession()">🗑️ Xóa session</button>
            </div>
        </div>
        
        <div class="lab-grid">
            <!-- Vector Similarity Testing -->
            <div class="lab-section">
                <h3>🔍 Test Vector Similarity</h3>
                <div class="input-group">
                    <label>Câu hỏi 1:</label>
                    <input type="text" id="query1" placeholder="VD: Tìm sách khoa học viễn tưởng">
                </div>
                <div class="input-group">
                    <label>Câu hỏi 2:</label>
                    <input type="text" id="query2" placeholder="VD: Sách sci-fi hay">
                </div>
                <button class="btn" onclick="testSimilarity()">🧮 Tính Similarity</button>
                
                <div id="similarityResult" class="result-box" style="display: none;"></div>
                <div class="similarity-meter" style="display: none;" id="similarityMeter">
                    <div class="similarity-fill" id="similarityFill"></div>
                </div>
            </div>
            
            <!-- Prompt Validation -->
            <div class="lab-section">
                <h3>✅ Validate Prompt Response</h3>
                <div class="input-group">
                    <label>Prompt:</label>
                    <input type="text" id="promptInput" placeholder="VD: Tìm sách về lập trình">
                </div>
                <div class="input-group">
                    <label>Response:</label>
                    <textarea id="responseInput" rows="3" placeholder="Nhập response để validate..."></textarea>
                </div>
                <button class="btn" onclick="validatePrompt()">🔍 Validate</button>
                
                <div id="validationResult" class="result-box" style="display: none;"></div>
            </div>
            
            <!-- Link Generation Testing -->
            <div class="lab-section">
                <h3>🔗 Test Link Generation</h3>
                <div style="background: #e8f4fd; padding: 10px; border-radius: 5px; margin-bottom: 15px; font-size: 12px;">
                    <strong>🔗 QUY TẮC GỬI LINK SÁCH:</strong><br>
                    ✅ Chỉ gửi link sách người dùng hỏi cụ thể<br>
                    ✅ Trả lời từng sách riêng biệt, không gộp link<br>
                    ✅ Không spam hoặc gửi link hàng loạt<br>
                    ✅ Max 3 links, chặn câu hỏi chung
                </div>
                <div class="input-group">
                    <label>User Question:</label>
                    <input type="text" id="linkQuestion" placeholder="VD: Tìm sách Harry Potter">
                </div>
                <div class="input-group">
                    <label>AI Response:</label>
                    <textarea id="linkResponse" rows="3" placeholder="Có 3 cuốn sách Harry Potter trong hệ thống..."></textarea>
                </div>
                <button class="btn" onclick="testLinkGeneration()">🔗 Generate Links</button>
                <button class="btn btn-success" onclick="testLinkSample()">📝 Test Sample</button>
                
                <div id="linkResult" class="result-box" style="display: none;"></div>
            </div>
        </div>
        
        <!-- Advanced Chat Section -->
        <div class="advanced-chat">
            <h3>💬 Advanced AI Chat với Prompt Training</h3>
            <div class="chat-messages" id="chatMessages">
                <div class="message ai">
                    🎯 Xin chào! Đây là AI Chat với Prompt Training đầy đủ:
                    <br>• ✅ Vector similarity caching (85%+ threshold)
                    <br>• ✅ Session memory across tabs
                    <br>• ✅ Database-only responses (không bịa thêm)
                    <br>• ✅ Response validation và cleaning
                    <br>• ✅ Smart link generation (anti-spam rules)
                    <br><br>Hãy thử hỏi tôi về sách để xem hệ thống hoạt động!
                </div>
            </div>
            
            <div class="loading" id="chatLoading">🤔 AI đang xử lý với prompt training...</div>
            
            <div style="display: flex; gap: 10px; margin-top: 15px;">
                <input type="text" id="chatInput" placeholder="Nhập câu hỏi để test prompt training..." 
                       style="flex: 1; padding: 12px; border-radius: 8px; border: 2px solid #ddd;">
                <button class="btn" onclick="sendAdvancedChat()">📤 Gửi</button>
            </div>
            
            <div style="margin-top: 10px; text-align: center;">
                <button class="btn btn-success" onclick="sendSample('Tìm sách khoa học viễn tưởng')">🚀 Test Sci-Fi</button>
                <button class="btn btn-success" onclick="sendSample('Sách của Nguyễn Nhật Ánh')">✍️ Test Author</button>
                <button class="btn btn-success" onclick="sendSample('Đề xuất sách hay cho tôi')">🎯 Test Recommendation</button>
                <button class="btn btn-success" onclick="sendSample('Tôi muốn đọc sách Harry Potter')">🔗 Test Link Gen</button>
            </div>
        </div>
    </main>
    
    <%@ include file="/common/footer.jsp" %>
    
    <script>
        const ctx = '${ctx}';
        
        // Load session info on page load
        document.addEventListener('DOMContentLoaded', function() {
            loadSessionInfo();
        });
        
        // Enter key support
        document.getElementById('chatInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendAdvancedChat();
            }
        });
        
        // Load session information
        function loadSessionInfo() {
            fetch(ctx + '/ai/prompt-training', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'api_action=session_info'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('sessionId').textContent = data.session_id.substring(0, 12) + '...';
                    document.getElementById('askedBooks').textContent = data.asked_books.length + ' cuốn';
                    document.getElementById('keywords').textContent = data.keywords.join(', ') || 'Chưa có';
                } else {
                    console.error('Failed to load session info:', data.error);
                }
            })
            .catch(error => console.error('Error:', error));
        }
        
        // Test vector similarity
        function testSimilarity() {
            const query1 = document.getElementById('query1').value.trim();
            const query2 = document.getElementById('query2').value.trim();
            
            if (!query1 || !query2) {
                alert('Vui lòng nhập cả hai câu hỏi');
                return;
            }
            
            fetch(ctx + '/ai/prompt-training', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'api_action=test_similarity&query1=' + encodeURIComponent(query1) + '&query2=' + encodeURIComponent(query2)
            })
            .then(response => response.json())
            .then(data => {
                const resultDiv = document.getElementById('similarityResult');
                const meterDiv = document.getElementById('similarityMeter');
                const fillDiv = document.getElementById('similarityFill');
                
                if (data.success) {
                    resultDiv.className = 'result-box success';
                    resultDiv.innerHTML = `
🎯 Kết quả Vector Similarity:
• Câu 1: "${data.query1}"
• Câu 2: "${data.query2}"
• Similarity: ${data.similarity}%
• Threshold: ${data.threshold}%
• Should Cache: ${data.should_cache ? '✅ CÓ' : '❌ KHÔNG'}
• Vector 1 size: ${data.vector1_size} terms
• Vector 2 size: ${data.vector2_size} terms

${data.should_cache ? '💾 Câu hỏi này sẽ được cache để tái sử dụng!' : '🔄 Câu hỏi này sẽ được xử lý mới.'}`;
                    
                    // Update similarity meter
                    fillDiv.style.width = data.similarity + '%';
                    meterDiv.style.display = 'block';
                } else {
                    resultDiv.className = 'result-box error';
                    resultDiv.innerHTML = '❌ Lỗi: ' + data.error;
                }
                
                resultDiv.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                const resultDiv = document.getElementById('similarityResult');
                resultDiv.className = 'result-box error';
                resultDiv.innerHTML = '❌ Lỗi kết nối: ' + error.message;
                resultDiv.style.display = 'block';
            });
        }
        
        // Validate prompt response
        function validatePrompt() {
            const prompt = document.getElementById('promptInput').value.trim();
            const response = document.getElementById('responseInput').value.trim();
            
            if (!prompt || !response) {
                alert('Vui lòng nhập cả prompt và response');
                return;
            }
            
            fetch(ctx + '/ai/prompt-training', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'api_action=validate_prompt&prompt=' + encodeURIComponent(prompt) + '&response=' + encodeURIComponent(response)
            })
            .then(response => response.json())
            .then(data => {
                const resultDiv = document.getElementById('validationResult');
                
                if (data.success) {
                    resultDiv.className = data.is_valid ? 'result-box success' : 'result-box error';
                    resultDiv.innerHTML = `
🔍 Kết quả Validation:
• Prompt: "${data.original_prompt}"
• Valid: ${data.is_valid ? '✅ HỢP LỆ' : '❌ KHÔNG HỢP LỆ'}
• Rules: ${data.validation_rules}

📝 Original Response:
${data.original_response}

✅ Validated Response:
${data.validated_response}`;
                } else {
                    resultDiv.className = 'result-box error';
                    resultDiv.innerHTML = '❌ Lỗi: ' + data.error;
                }
                
                resultDiv.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                const resultDiv = document.getElementById('validationResult');
                resultDiv.className = 'result-box error';
                resultDiv.innerHTML = '❌ Lỗi kết nối: ' + error.message;
                resultDiv.style.display = 'block';
            });
        }
        
        // Test link generation với quy tắc nghiêm ngặt
        function testLinkGeneration() {
            const question = document.getElementById('linkQuestion').value.trim();
            const response = document.getElementById('linkResponse').value.trim();
            
            if (!question || !response) {
                alert('Vui lòng nhập cả question và response');
                return;
            }
            
            fetch(ctx + '/ai/prompt-training', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'api_action=test_link_generation&question=' + encodeURIComponent(question) + '&response=' + encodeURIComponent(response)
            })
            .then(response => response.json())
            .then(data => {
                const resultDiv = document.getElementById('linkResult');
                
                if (data.success) {
                    // Kiểm tra tuân thủ quy tắc
                    const followsRules = checkLinkRules(data, question);
                    resultDiv.className = followsRules.isValid ? 'result-box success' : 'result-box error';
                    
                    let linkDetails = '';
                    if (data.links_details && data.links_details.length > 0) {
                        linkDetails = '\n\n📚 LINKS ĐƯỢC TẠO (Từng sách riêng biệt):\n';
                        data.links_details.forEach((link, index) => {
                            linkDetails += `\n${index + 1}. 📚 **${link.book_title}** - ${link.book_author}\n`;
                            linkDetails += `   [Đọc sách](${link.url}) | Type: ${link.link_type}\n`;
                        });
                    } else {
                        linkDetails = '\n\n❌ Không có links (quy tắc anti-spam được áp dụng)';
                    }
                    
                    resultDiv.innerHTML = `
🔗 KẾT QUẢ KIỂM TRA QUY TẮC LINK SÁCH:

${followsRules.ruleCheck}

📋 Chi tiết:
• Question: "${data.original_question}"
• Links Generated: ${data.links_generated} / ${data.anti_spam_rules.max_links_allowed}
• Question Type: ${data.metadata.question_type}

📝 Original Response:
${data.original_response.substring(0, 100)}...

🔗 Enhanced Response (Format từng sách riêng biệt):
${data.enhanced_response}

${linkDetails}

📊 Anti-spam Rules Applied:
• Max Links: ✅ ${data.anti_spam_rules.max_links_allowed}
• Duplicate Prevention: ✅ ${data.anti_spam_rules.duplicate_prevention}
• General Question Filter: ✅ ${data.anti_spam_rules.general_question_filtering}
• Specific Book Priority: ✅ ${data.anti_spam_rules.specific_book_priority}`;
                } else {
                    resultDiv.className = 'result-box error';
                    resultDiv.innerHTML = '❌ Lỗi: ' + data.error;
                }
                
                resultDiv.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                const resultDiv = document.getElementById('linkResult');
                resultDiv.className = 'result-box error';
                resultDiv.innerHTML = '❌ Lỗi kết nối: ' + error.message;
                resultDiv.style.display = 'block';
            });
        }
        
        // Kiểm tra tuân thủ quy tắc link
        function checkLinkRules(data, question) {
            let ruleCheck = '🔍 KIỂM TRA QUY TẮC:\n';
            let isValid = true;
            
            // Rule 1: Chỉ gửi link sách cụ thể
            const isGeneralQuestion = question.toLowerCase().includes('làm thế nào') || 
                                    question.toLowerCase().includes('tại sao') ||
                                    question.toLowerCase().includes('ở đâu');
            
            if (isGeneralQuestion && data.links_generated > 0) {
                ruleCheck += '❌ RULE 1: Gửi link cho câu hỏi chung (KHÔNG HỢP LỆ)\n';
                isValid = false;
            } else if (!isGeneralQuestion && data.links_generated > 0) {
                ruleCheck += '✅ RULE 1: Chỉ gửi link cho sách được hỏi cụ thể\n';
            } else {
                ruleCheck += '✅ RULE 1: Không gửi link cho câu hỏi chung\n';
            }
            
            // Rule 2: Trả lời từng sách riêng biệt
            const hasIndividualFormat = data.enhanced_response.includes('📚 **') && 
                                       data.enhanced_response.includes('[Đọc sách]') &&
                                       !data.enhanced_response.includes('📚 **Liên kết sách:**');
            
            if (data.links_generated > 0) {
                if (hasIndividualFormat) {
                    ruleCheck += '✅ RULE 2: Trả lời từng sách riêng biệt (Format đúng)\n';
                } else {
                    ruleCheck += '❌ RULE 2: Gộp links lại (Format sai)\n';
                    isValid = false;
                }
            } else {
                ruleCheck += '⚪ RULE 2: N/A (Không có links)\n';
            }
            
            // Rule 3: Không spam link
            if (data.links_generated <= 3) {
                ruleCheck += `✅ RULE 3: Không spam link (${data.links_generated}/3 max)\n`;
            } else {
                ruleCheck += `❌ RULE 3: Spam link (${data.links_generated}/3 max)\n`;
                isValid = false;
            }
            
            return { isValid, ruleCheck };
        }
        
        // Test với sample data
        function testLinkSample() {
            document.getElementById('linkQuestion').value = 'Gợi ý 2 sách hay cho tôi';
            document.getElementById('linkResponse').value = 'Dựa trên sở thích, tôi đề xuất: "Dế Mèn Phiêu Lưu Ký" của Tô Hoài và "Tôi Thấy Hoa Vàng Trên Cỏ Xanh" của Nguyễn Nhật Ánh';
            testLinkGeneration();
        }
        
        // Send advanced chat message
        function sendAdvancedChat() {
            const message = document.getElementById('chatInput').value.trim();
            if (!message) return;
            
            // Add user message to chat
            addChatMessage(message, 'user');
            document.getElementById('chatInput').value = '';
            
            // Show loading
            document.getElementById('chatLoading').style.display = 'block';
            
            fetch(ctx + '/ai/prompt-training', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'api_action=advanced_chat&message=' + encodeURIComponent(message)
            })
            .then(response => response.json())
            .then(data => {
                document.getElementById('chatLoading').style.display = 'none';
                
                if (data.success) {
                    let aiResponse = data.reply;
                    
                    // Add metadata
                    aiResponse += `
                    
📊 Metadata:
• Session: ${data.session_id.substring(0, 8)}...
• Cache Hit Rate: ${data.cache_hit_rate}%
• Features: ${data.features.join(', ')}
• Prompt Training: ✅ Enabled`;
                    
                    addChatMessage(aiResponse, 'ai');
                    
                    // Refresh session info
                    loadSessionInfo();
                } else {
                    addChatMessage('❌ Lỗi: ' + data.error, 'ai');
                }
            })
            .catch(error => {
                document.getElementById('chatLoading').style.display = 'none';
                addChatMessage('❌ Lỗi kết nối: ' + error.message, 'ai');
                console.error('Error:', error);
            });
        }
        
        // Add message to chat
        function addChatMessage(text, sender) {
            const chatMessages = document.getElementById('chatMessages');
            const messageDiv = document.createElement('div');
            messageDiv.className = `message ${sender}`;
            messageDiv.innerHTML = text.replace(/\n/g, '<br>');
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
        
        // Send sample message
        function sendSample(message) {
            document.getElementById('chatInput').value = message;
            sendAdvancedChat();
        }
        
        // Clear session
        function clearSession() {
            if (confirm('Bạn có chắc muốn xóa session memory?')) {
                fetch(ctx + '/ai/prompt-training', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'api_action=clear_session'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('✅ Đã xóa session memory thành công');
                        loadSessionInfo();
                        
                        // Clear chat messages
                        const chatMessages = document.getElementById('chatMessages');
                        chatMessages.innerHTML = `
                            <div class="message ai">
                                🎯 Session đã được reset! Tôi sẽ bắt đầu ghi nhớ lại từ đầu.
                            </div>
                        `;
                    } else {
                        alert('❌ Lỗi: ' + data.error);
                    }
                })
                .catch(error => {
                    alert('❌ Lỗi kết nối: ' + error.message);
                    console.error('Error:', error);
                });
            }
        }
    </script>
</body>
</html>
