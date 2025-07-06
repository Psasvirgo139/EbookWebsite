@echo off
echo ========================================
echo 🔥 CROSS-TOPIC CONNECTION IMPROVEMENTS
echo ========================================
echo.

echo 🏗️ Building project...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ Build failed!
    pause
    exit /b 1
)

echo.
echo 🧪 Running Cross-Topic Connection Tests...
echo.

echo 🔗 Test 1: Cross-Topic Connections
echo ----------------------------------------
call mvn test -Dtest=CrossTopicConnectionTest#testCrossTopicConnections -q
if %errorlevel% neq 0 echo ⚠️ Cross-topic connections test failed

echo.
echo 📚 Test 2: Enhanced Book Tracking
echo ----------------------------------------
call mvn test -Dtest=CrossTopicConnectionTest#testEnhancedBookTracking -q
if %errorlevel% neq 0 echo ⚠️ Enhanced book tracking test failed

echo.
echo 🔍 Test 3: Input Validation Fix
echo ----------------------------------------
call mvn test -Dtest=CrossTopicConnectionTest#testInputValidationFix -q
if %errorlevel% neq 0 echo ⚠️ Input validation fix test failed

echo.
echo 🧠 Test 4: Context Continuity Enhancement
echo ----------------------------------------
call mvn test -Dtest=CrossTopicConnectionTest#testContextContinuityEnhancement -q
if %errorlevel% neq 0 echo ⚠️ Context continuity enhancement test failed

echo.
echo 🏆 Test 5: Comprehensive Improvements
echo ----------------------------------------
call mvn test -Dtest=CrossTopicConnectionTest#testComprehensiveImprovements -q
if %errorlevel% neq 0 echo ⚠️ Comprehensive improvements test failed

echo.
echo ========================================
echo ✅ All Cross-Topic Tests Completed!
echo ========================================
echo.
echo 📊 Test Summary:
echo - Cross-topic connections (AI -> ML -> DL)
echo - Enhanced book tracking with avoidance
echo - Input validation fixes
echo - Context continuity improvements
echo - Comprehensive feature validation
echo.
pause 