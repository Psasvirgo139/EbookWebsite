@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🔧 Maven Compile with Low Memory
echo ========================================
echo.

set MAVEN_OPTS=-Xmx512m -Xms256m

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
echo 🎉 Project compiled with low memory settings!
echo.
pause 