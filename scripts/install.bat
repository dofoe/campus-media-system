@echo off
chcp 65001 >nul
title 校园宣传素材智能管理系统 - 一键安装

echo ============================================
echo    校园宣传素材智能管理系统 - 一键安装脚本
echo ============================================

echo.
echo [1/6] 检查系统环境...

node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Node.js 未安装
    echo 请先安装 Node.js 18+
    pause
    exit /b 1
)

echo ✓ Node.js 已安装

pnpm --version >nul 2>&1
if %errorlevel% equ 0 (
    set "PACKAGE_MANAGER=pnpm"
    echo ✓ 包管理器: pnpm
) else (
    npm --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo 错误: npm 或 pnpm 未安装
        pause
        exit /b 1
    )
    set "PACKAGE_MANAGER=npm"
    echo ✓ 包管理器: npm
)

echo.
echo [2/6] 安装项目依赖...

cd /d "%~dp0\.."

if "%PACKAGE_MANAGER%"=="pnpm" (
    pnpm install
) else (
    npm install
)

if %errorlevel% neq 0 (
    echo 错误: 依赖安装失败
    pause
    exit /b 1
)

echo ✓ 依赖安装完成

echo.
echo [3/6] 构建生产版本...

if "%PACKAGE_MANAGER%"=="pnpm" (
    pnpm run build
) else (
    npm run build
)

if %errorlevel% neq 0 (
    echo 错误: 构建失败
    pause
    exit /b 1
)

echo ✓ 构建完成

echo.
echo [4/6] 创建目录结构...

mkdir "C:\data\campus-media\uploads" 2>nul
mkdir "C:\data\campus-media\logs" 2>nul
mkdir "C:\data\campus-media\dist" 2>nul

echo ✓ 目录创建完成

echo.
echo [5/6] 复制构建产物...

xcopy "dist" "C:\data\campus-media\dist" /E /I /Y >nul

echo ✓ 复制完成

echo.
echo ============================================
echo    安装完成！
echo ============================================

echo.
echo 部署信息:
echo   前端文件: C:\data\campus-media\dist
echo   上传目录: C:\data\campus-media\uploads
echo   日志目录: C:\data\campus-media\logs

echo.
echo 启动方式:
echo   开发模式: cd /d "%cd%" ^&^& %PACKAGE_MANAGER% run dev
echo   生产模式: 配置Nginx指向 C:\data\campus-media\dist

echo.
echo 默认管理员账号:
echo   用户名: admin
echo   密码: admin

echo.
echo 按任意键退出...
pause >nul
