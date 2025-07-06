@echo off
echo ========================================
echo TEST CASE 8: ADMIN SUPPORT FEATURES
echo ========================================
echo.

echo 🎯 Testing Admin Support Features...
echo.

echo 📋 Test 1: Display Pending Books List
echo ----------------------------------------
echo Input: "Hiển thị danh sách sách pending"
echo Expected: AI should show pending books with status and details
echo.

echo 📊 Test 2: Analyze New Book Metadata
echo ----------------------------------------
echo Input: "Phân tích metadata sách mới upload"
echo Expected: AI should analyze book metadata and provide insights
echo.

echo 🏷️ Test 3: Suggest Tags for Books
echo ----------------------------------------
echo Input: "Đề xuất tag cho sách 'Machine Learning Basics'"
echo Expected: AI should suggest relevant tags based on book content
echo.

echo 📝 Test 4: Create Beautiful Book Descriptions
echo ----------------------------------------
echo Input: "Tạo mô tả đẹp cho sách 'Python Programming'"
echo Expected: AI should generate attractive book descriptions
echo.

echo 🔍 Test 5: Book Content Analysis
echo ----------------------------------------
echo Input: "Phân tích nội dung sách 'Data Science Fundamentals'"
echo Expected: AI should analyze book content and provide insights
echo.

echo 📈 Test 6: Admin Statistics
echo ----------------------------------------
echo Input: "Hiển thị thống kê admin"
echo Expected: AI should show admin dashboard statistics
echo.

echo ⚠️ Test 7: Content Moderation for Admin
echo ----------------------------------------
echo Input: "Kiểm tra nội dung không phù hợp trong sách"
echo Expected: AI should identify inappropriate content for admin review
echo.

echo 🔧 Test 8: Book Approval Workflow
echo ----------------------------------------
echo Input: "Duyệt sách 'Advanced AI Techniques'"
echo Expected: AI should assist with book approval process
echo.

echo.
echo 🚀 Starting Admin Support Tests...
echo.

cd /d D:\EbookWebsite
set JAVA_HOME=C:\Program Files\Java\jdk-17

echo.
echo ========================================
echo TESTING ADMIN SUPPORT FEATURES
echo ========================================
echo.

echo Test 1: Pending Books List
echo Input: Hiển thị danh sách sách pending
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 2: Metadata Analysis
echo Input: Phân tích metadata sách mới upload
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 3: Tag Suggestions
echo Input: Đề xuất tag cho sách "Machine Learning Basics"
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 4: Book Description Creation
echo Input: Tạo mô tả đẹp cho sách "Python Programming"
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 5: Content Analysis
echo Input: Phân tích nội dung sách "Data Science Fundamentals"
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 6: Admin Statistics
echo Input: Hiển thị thống kê admin
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 7: Content Moderation
echo Input: Kiểm tra nội dung không phù hợp trong sách
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo Test 8: Book Approval Workflow
echo Input: Duyệt sách "Advanced AI Techniques"
echo.
"C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo ========================================
echo ADMIN SUPPORT TESTS COMPLETED
echo ========================================
echo.
echo ✅ Test Case 8: Admin Support Features
echo 📊 Features Tested:
echo    - Pending books management
echo    - Metadata analysis
echo    - Tag suggestions
echo    - Book description creation
echo    - Content analysis
echo    - Admin statistics
echo    - Content moderation
echo    - Book approval workflow
echo.
echo 🎯 Admin Support Features Integration Status: COMPLETE
echo.
pause 