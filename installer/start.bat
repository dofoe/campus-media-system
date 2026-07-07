@echo off
chcp 65001 >nul
title Campus Media System

echo ========================================
echo   Campus Media System
echo ========================================
echo.

set JAVA_HOME=%~dp0runtime
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/4] Checking Java environment...
java -version 2>&1 | findstr /R "version" >nul
if errorlevel 1 (
    echo [ERROR] Java runtime not found!
    echo Please check if runtime folder contains valid JRE
    pause
    exit /b 1
)
echo    Java OK
echo.

echo [2/4] Initializing directories...
if not exist "data" mkdir data
if not exist "logs" mkdir logs
if not exist "uploads" mkdir uploads
echo    Directories OK
echo.

echo [3/4] Checking port 8080...
netstat -ano | findstr :8080 >nul
if not errorlevel 1 (
    echo [WARN] Port 8080 is occupied!
    echo Please stop the program using port 8080
    pause
    exit /b 1
)
echo    Port OK
echo.

echo [4/4] Starting service...
echo.
echo ========================================
echo   System started!
echo   Access: http://localhost:8080
echo   Default: admin / admin
echo   Press Ctrl+C to stop
echo ========================================
echo.

java -Xms256m -Xmx1024m -jar -Dfile.encoding=UTF-8 app.jar

echo.
echo Service stopped
pause