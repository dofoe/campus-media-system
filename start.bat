@echo off
chcp 65001 >nul
title Campus Media System

cd /d "D:\campus-media-system"

echo ============================================
echo   Campus Media System
echo ============================================
echo.
echo URL: http://localhost:5173
echo Account: admin / admin
echo.
echo Starting...
echo.

pnpm run dev 2>nul || npm run dev