@echo off
echo.
echo ========================================
echo 🚀 ENHANCED AI TEST RUNNER
echo ========================================
echo.

echo 🔧 Compiling and running Enhanced AI tests...
echo.

cd /d "%~dp0"

REM Compile the project
echo 📦 Compiling project...
call mvn compile -q
if %errorlevel% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo.

REM Run the enhanced AI test tool
echo 🧪 Running Enhanced AI tests...
echo.

java -cp "target/classes;target/dependency/*" com.mycompany.ebookwebsite.ai.EnhancedAITestTool

echo.
echo ========================================
echo ✅ Enhanced AI tests completed!
echo ========================================
echo.

REM Interactive mode option
echo.
set /p choice="🎮 Run interactive mode? (y/n): "
if /i "%choice%"=="y" (
    echo.
    echo 🎮 Starting interactive mode...
    java -cp "target/classes;target/dependency/*" com.mycompany.ebookwebsite.ai.EnhancedAITestTool interactive
)

echo.
echo 👋 Test runner finished!
pause 