@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🚀 AI Chat Test with Low Memory
echo ========================================
echo.

set MAVEN_OPTS=-Xmx512m -Xms256m -XX:MaxPermSize=128m

echo 🔧 Setting MAVEN_OPTS: %MAVEN_OPTS%
echo.

echo 🧹 Cleaning project...
call mvn clean -q

if %errorlevel% neq 0 (
    echo ❌ Clean failed!
    pause
    exit /b 1
)

echo.
echo ✅ Clean successful!
echo.

echo 🔨 Compiling with low memory...
call mvn compile -q

if %errorlevel% neq 0 (
    echo ❌ Compile failed!
    pause
    exit /b 1
)

echo.
echo ✅ Compile successful!
echo.

echo 🚀 Running AI Chat Test...
echo.

call mvn exec:java -Dexec.mainClass="com.mycompany.ebookwebsite.ai.AIChatTester" -q

echo.
echo 🎉 AI Chat Test completed!
echo.
pause 