@echo off
REM =========================================
REM  Campus Media System - Start Script
REM  Simple and robust, no fancy features
REM =========================================

setlocal

echo ========================================
echo   Campus Media System
echo ========================================
echo.

REM Step 1: Find Java
echo [1/3] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install JDK 21:
    echo   https://adoptium.net/
    echo.
    echo After installation, restart this script
    echo.
    pause
    exit /b 1
)
echo   Java: OK
echo.

REM Step 2: Check if JAR exists
echo [2/3] Checking build status...
set JAR_FILE=backend\target\campus-media-system-1.0.0.jar

if not exist "%JAR_FILE%" (
    echo   JAR not found, need to build first
    echo.
    echo Please run build.bat first, or:
    echo   cd backend
    echo   mvn clean package -DskipTests
    echo.
    pause
    exit /b 1
)
echo   JAR: OK
echo.

REM Step 3: Create directories
echo [3/3] Creating directories...
if not exist "data" mkdir data
if not exist "logs" mkdir logs
if not exist "uploads" mkdir uploads
echo   Directories: OK
echo.

echo ========================================
echo   Starting system...
echo ========================================
echo.
echo   URL: http://localhost:8080
echo   Login: admin / admin
echo.
echo   Press Ctrl+C to stop
echo.

java -Xms256m -Xmx1024m -Dfile.encoding=UTF-8 -jar "%JAR_FILE%"

echo.
echo System stopped
pause
endlocal