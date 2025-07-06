@echo off
echo ========================================
echo 📚 BOOK LINK COORDINATION IMPROVEMENTS
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
echo 🧪 Running Book Link Coordination Tests...
echo.

echo 📚 Test 1: Book Link Coordination
echo ----------------------------------------
call mvn test -Dtest=BookLinkCoordinationTest#testBookLinkCoordination -q
if %errorlevel% neq 0 echo ⚠️ Book link coordination test failed

echo.
echo 🔄 Test 2: Repetition Avoidance
echo ----------------------------------------
call mvn test -Dtest=BookLinkCoordinationTest#testRepetitionAvoidance -q
if %errorlevel% neq 0 echo ⚠️ Repetition avoidance test failed

echo.
echo 🔍 Test 3: Input Validation Fix
echo ----------------------------------------
call mvn test -Dtest=BookLinkCoordinationTest#testInputValidationFix -q
if %errorlevel% neq 0 echo ⚠️ Input validation fix test failed

echo.
echo 🎯 Test 4: Context-Aware Recommendations
echo ----------------------------------------
call mvn test -Dtest=BookLinkCoordinationTest#testContextAwareRecommendations -q
if %errorlevel% neq 0 echo ⚠️ Context-aware recommendations test failed

echo.
echo 🏆 Test 5: Comprehensive Book Coordination
echo ----------------------------------------
call mvn test -Dtest=BookLinkCoordinationTest#testComprehensiveBookCoordination -q
if %errorlevel% neq 0 echo ⚠️ Comprehensive book coordination test failed

echo.
echo ========================================
echo ✅ All Book Link Coordination Tests Completed!
echo ========================================
echo.
echo 📊 Test Summary:
echo - Book link coordination (1 question → 1-2 links)
echo - Repetition avoidance
echo - Input validation fixes
echo - Context-aware recommendations
echo - Comprehensive book coordination
echo.
pause 