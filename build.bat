@echo off
REM =========================================
REM  Campus Media System - Build Script
REM  Builds frontend and backend
REM =========================================

setlocal

echo ========================================
echo   Campus Media System - Build
echo ========================================
echo.

REM Step 1: Check Java
echo [1/5] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found. Install JDK 21: https://adoptium.net/
    pause
    exit /b 1
)
echo   OK
echo.

REM Step 2: Check Maven
echo [2/5] Checking Maven...
call mvn --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven not found or not working
    echo.
    echo Your Maven is installed but not accessible.
    echo.
    echo Common fixes:
    echo   1. Restart your computer after installing Maven
    echo   2. Make sure PATH includes: D:\Program Files\apache-maven4.0\apache-maven-4.0.0-rc-5\bin
    echo   3. Open a NEW Command Prompt after adding PATH
    echo.
    echo Current PATH: %PATH%
    echo.
    pause
    exit /b 1
)
echo   OK
echo.

REM Step 3: Check Node.js
echo [3/5] Checking Node.js...
node --version >nul 2>&1
if errorlevel 1 (
    echo WARN: Node.js not found, skipping frontend build
    echo   Frontend will be served from backend's built-in copy
    set SKIP_FRONTEND=1
) else (
    echo   OK
    set SKIP_FRONTEND=0
)
echo.

REM Step 4: Build frontend
echo [4/5] Building frontend...
if "%SKIP_FRONTEND%"=="0" (
    call npm install --silent
    if errorlevel 1 (
        echo WARN: npm install failed, skipping frontend
        goto build_backend
    )
    call npm run build
    if errorlevel 1 (
        echo WARN: npm run build failed, skipping frontend
    ) else (
        echo   Frontend: OK
    )
) else (
    echo   Skipped
)
echo.

:build_backend
REM Step 5: Build backend
echo [5/5] Building backend...
cd backend
call mvn clean package -DskipTests
if errorlevel 1 (
    echo.
    echo ERROR: Backend build failed!
    echo.
    echo Check error messages above. Common fixes:
    echo   1. Configure Maven mirror: %USERPROFILE%\.m2\settings.xml
    echo   2. Add aliyun mirror for faster downloads
    echo   3. Make sure JAVA_HOME is set correctly
    echo.
    cd ..
    pause
    exit /b 1
)
cd ..
echo   Backend: OK
echo.

echo ========================================
echo   Build complete!
echo ========================================
echo.
echo Next: run start.bat to launch the system
echo.
pause
endlocal