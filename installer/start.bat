@echo off
title Campus Media System
setlocal

echo ========================================
echo   Campus Media System
echo ========================================
echo.

REM Find Java - check runtime folder first
set JAVA_CMD=java
if exist "%~dp0runtime\bin\java.exe" (
    set "JAVA_CMD=%~dp0runtime\bin\java.exe"
)

echo [1/3] Checking Java...
"%JAVA_CMD%" -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Java runtime not found!
    echo.
    echo If you have JDK 21 installed:
    echo   1. Add it to PATH
    echo   2. Or download JRE and extract to 'runtime' folder
    echo.
    echo Download: https://adoptium.net/
    echo.
    pause
    exit /b 1
)
echo   Java: OK
echo.

echo [2/3] Checking app.jar...
set JAR_FILE=%~dp0app.jar
if not exist "%JAR_FILE%" (
    echo ERROR: app.jar not found!
    echo Please make sure app.jar is in the same directory as this script.
    pause
    exit /b 1
)
echo   app.jar: OK
echo.

echo [3/3] Creating directories...
cd /d "%~dp0"
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

"%JAVA_CMD%" -Xms256m -Xmx1024m -Dfile.encoding=UTF-8 -jar "%JAR_FILE%"

echo.
echo System stopped
pause
endlocal