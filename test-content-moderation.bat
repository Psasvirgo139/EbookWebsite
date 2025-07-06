@echo off
echo ========================================
echo 🛡️ CONTENT MODERATION & SAFETY FEATURES
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
echo 🧪 Running Content Moderation Tests...
echo.

echo 🛡️ Test 1: Inappropriate Content Detection
echo ----------------------------------------
call mvn test -Dtest=ContentModerationTest#testInappropriateContentDetection -q
if %errorlevel% neq 0 echo ⚠️ Inappropriate content detection test failed

echo.
echo 🎭 Test 2: Sensitive Political Content
echo ----------------------------------------
call mvn test -Dtest=ContentModerationTest#testSensitivePoliticalContent -q
if %errorlevel% neq 0 echo ⚠️ Sensitive political content test failed

echo.
echo ✅ Test 3: Appropriate Content Handling
echo ----------------------------------------
call mvn test -Dtest=ContentModerationTest#testAppropriateContentHandling -q
if %errorlevel% neq 0 echo ⚠️ Appropriate content handling test failed

echo.
echo 📊 Test 4: Moderation Response Quality
echo ----------------------------------------
call mvn test -Dtest=ContentModerationTest#testModerationResponseQuality -q
if %errorlevel% neq 0 echo ⚠️ Moderation response quality test failed

echo.
echo 🏆 Test 5: Comprehensive Content Moderation
echo ----------------------------------------
call mvn test -Dtest=ContentModerationTest#testComprehensiveContentModeration -q
if %errorlevel% neq 0 echo ⚠️ Comprehensive content moderation test failed

echo.
echo ========================================
echo ✅ All Content Moderation Tests Completed!
echo ========================================
echo.
echo 📊 Test Summary:
echo - Inappropriate content detection and moderation
echo - Sensitive political content handling
echo - Appropriate content processing
echo - Moderation response quality
echo - Comprehensive content safety features
echo.
pause 