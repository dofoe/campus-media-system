@echo off
chcp 65001 >nul
echo ========================================
echo   Campus Media System - Install Service
echo ========================================
echo.

set INSTALL_DIR=%~dp0
cd /d "%INSTALL_DIR%"

net session >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Please run as Administrator!
    pause
    exit /b 1
)

echo [1/5] Copying WinSW...
if not exist "winsw.exe" (
    echo [ERROR] winsw.exe not found
    pause
    exit /b 1
)

echo [2/5] Registering Windows Service...
CampusMediaService.exe install

echo [3/5] Starting Windows Service...
CampusMediaService.exe start

echo [4/5] Setting auto-start...
sc config CampusMediaService start= auto

echo [5/5] Installation complete!
echo.
echo ========================================
echo   Service installed successfully
echo   Service Name: CampusMediaService
echo   Access: http://localhost:8080
echo   Default: admin / admin
echo ========================================
echo.
echo Commands:
echo   Start:   net start CampusMediaService
echo   Stop:    net stop CampusMediaService
echo   Uninstall: CampusMediaService.exe uninstall
echo.
pause