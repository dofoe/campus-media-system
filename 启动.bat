@echo off
chcp 65001 >nul
title 校园宣传素材智能管理系统

cd /d "D:\campus-media-system"

echo ============================================
echo    校园宣传素材智能管理系统
echo ============================================
echo.
echo 访问地址: http://localhost:5173
echo 默认账号: admin / admin
echo.
echo 正在启动...
echo.

pnpm run dev 2>nul || npm run dev