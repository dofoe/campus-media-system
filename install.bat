@echo off
chcp 65001 >nul
title Campus Media System - Auto Install

echo ============================================
echo   Campus Media System - Auto Install
echo ============================================
echo.
echo Install to: D:\campus-media-system
echo.

cd /d "%~dp0"

echo [1/5] Checking environment...
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Node.js not installed
    echo Please install Node.js 18+ from https://nodejs.org/
    echo.
    pause
    exit /b 1
)
echo OK: Node.js installed

where pnpm >nul 2>&1
if %errorlevel% equ 0 (
    set "PKG=pnpm"
    echo OK: Using pnpm
) else (
    where npm >nul 2>&1
    if %errorlevel% neq 0 (
        echo ERROR: npm not installed
        pause
        exit /b 1
    )
    set "PKG=npm"
    echo OK: Using npm
)

echo.
echo [2/5] Creating directories...
if not exist "D:\campus-media-system" mkdir "D:\campus-media-system"
if not exist "D:\data\campus-media\uploads" mkdir "D:\data\campus-media\uploads"
if not exist "D:\data\campus-media\dist" mkdir "D:\data\campus-media\dist"
echo OK: Directories created

echo.
echo [3/5] Copying files...
xcopy "%~dp0*" "D:\campus-media-system\" /E /I /Y >nul
echo OK: Files copied

echo.
echo [4/5] Installing dependencies...
cd /d "D:\campus-media-system"
%PKG% install
if %errorlevel% neq 0 (
    echo ERROR: Failed to install dependencies
    pause
    exit /b 1
)
echo OK: Dependencies installed

echo.
echo [5/5] Building project...
%PKG% run build
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo OK: Build completed

echo.
echo ============================================
echo   Installation Complete!
echo ============================================
echo.
echo Project: D:\campus-media-system
echo Data: D:\data\campus-media
echo.
echo Default account: admin / admin
echo.
echo Press any key to start the system...
pause >nul

cd /d "D:\campus-media-system"
start cmd /k "%PKG% run dev"

echo.
echo System starting...
echo Visit: http://localhost:5173
echo.
pause