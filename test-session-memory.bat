@echo off
echo ========================================
echo 🔥 TEST SESSION MEMORY IMPROVEMENTS
echo ========================================
echo.

echo 🚀 Building project...
call mvn clean compile -q

echo.
echo 🧠 Testing Session Memory and Context Awareness...
echo.

cd D:\EbookWebsite
set JAVA_HOME=C:\Program Files\Java\jdk-17
set MAVEN_HOME=C:\Program Files\NetBeans-17\netbeans\java\maven

echo 📋 Running Session Memory Test...
echo.

"%MAVEN_HOME%\bin\mvn.cmd" -Dexec.vmArgs= "-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}" -Dexec.executable=java -Dexec.mainClass=com.mycompany.ebookwebsite.ai.test.AIChatTester -Dexec.classpathScope=runtime -Dexec.appArgs= "-Dmaven.ext.class.path=C:\Program Files\NetBeans-17\netbeans\java\maven-nblib\netbeans-eventspy.jar" --no-transfer-progress org.codehaus.mojo:exec-maven-plugin:3.1.0:exec

echo.
echo ✅ Session Memory Test Completed!
echo.
echo 📊 Expected Improvements:
echo - ✅ Session memory should work properly
echo - ✅ Context awareness should be maintained
echo - ✅ AI should remember previous conversations
echo - ✅ Conversation history should be preserved
echo.
pause 