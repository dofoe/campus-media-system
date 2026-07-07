@echo off
chcp 65001 >nul
echo ========================================
echo   Campus Media System - Uninstall Service
echo ========================================
echo.

net session >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Please run as Administrator!
    pause
    exit /b 1
)

echo [1/3] Stopping service...
net stop CampusMediaService 2>nul
timeout /t 3 >nul

echo [2/3] Uninstalling service...
CampusMediaService.exe uninstall 2>nul

echo [3/3] Uninstall complete
echo.
echo Service uninstalled, but data files remain in data folder
echo To clean completely, manually delete the installation directory
echo.
pause