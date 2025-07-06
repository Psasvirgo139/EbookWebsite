@echo off
echo ========================================
echo 👤 USER PREFERENCE ANALYSIS IMPROVEMENTS
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
echo 🧪 Running User Preference Analysis Tests...
echo.

echo 👤 Test 1: User Preference Detection
echo ----------------------------------------
call mvn test -Dtest=UserPreferenceAnalysisTest#testUserPreferenceDetection -q
if %errorlevel% neq 0 echo ⚠️ User preference detection test failed

echo.
echo 🎭 Test 2: Genre Combination Analysis
echo ----------------------------------------
call mvn test -Dtest=UserPreferenceAnalysisTest#testGenreCombinationAnalysis -q
if %errorlevel% neq 0 echo ⚠️ Genre combination analysis test failed

echo.
echo 🎯 Test 3: Personalized Recommendations
echo ----------------------------------------
call mvn test -Dtest=UserPreferenceAnalysisTest#testPersonalizedRecommendations -q
if %errorlevel% neq 0 echo ⚠️ Personalized recommendations test failed

echo.
echo 🧠 Test 4: Preference-Based Context Building
echo ----------------------------------------
call mvn test -Dtest=UserPreferenceAnalysisTest#testPreferenceBasedContextBuilding -q
if %errorlevel% neq 0 echo ⚠️ Preference-based context building test failed

echo.
echo 🏆 Test 5: Comprehensive User Preference Analysis
echo ----------------------------------------
call mvn test -Dtest=UserPreferenceAnalysisTest#testComprehensiveUserPreferenceAnalysis -q
if %errorlevel% neq 0 echo ⚠️ Comprehensive user preference analysis test failed

echo.
echo ========================================
echo ✅ All User Preference Analysis Tests Completed!
echo ========================================
echo.
echo 📊 Test Summary:
echo - User preference detection and tracking
echo - Genre combination analysis
echo - Personalized book recommendations
echo - Preference-based context building
echo - Comprehensive user preference analysis
echo.
pause 