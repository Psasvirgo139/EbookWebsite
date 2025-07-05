@echo off
echo ========================================
echo EbookWebsite Deployment Script
echo ========================================

echo.
echo [1/5] Cleaning previous build...
call mvn clean

echo.
echo [2/5] Compiling project...
call mvn compile

echo.
echo [3/5] Running tests...
call mvn test

echo.
echo [4/5] Building WAR file...
call mvn package

echo.
echo [5/5] Deployment completed!
echo.
echo Next steps:
echo 1. Copy target/EbookWebsite-1.0-SNAPSHOT.war to your Tomcat webapps folder
echo 2. Start Tomcat server
echo 3. Access: http://localhost:8080/EbookWebsite
echo.
echo Default admin credentials:
echo Username: admin
echo Password: admin
echo.
pause 