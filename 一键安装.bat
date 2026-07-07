@echo off
chcp 65001 >nul
title 校园宣传素材智能管理系统 - 一键安装

echo ============================================
echo    校园宣传素材智能管理系统 - 一键安装
echo ============================================
echo.
echo 安装目录: D:\campus-media-system
echo.

cd /d "%~dp0"

echo [1/5] 检查系统环境...
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo 错误: Node.js 未安装
    echo 请先安装 Node.js 18+ : https://nodejs.org/
    echo.
    pause
    exit /b 1
)
echo ✓ Node.js 已安装

pnpm --version >nul 2>&1
if %errorlevel% equ 0 (
    set "PKG=pnpm"
    echo ✓ 使用 pnpm
) else (
    npm --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo 错误: npm 未安装
        pause
        exit /b 1
    )
    set "PKG=npm"
    echo ✓ 使用 npm
)

echo.
echo [2/5] 创建目录...
mkdir "D:\campus-media-system" 2>nul
mkdir "D:\data\campus-media\uploads" 2>nul
mkdir "D:\data\campus-media\dist" 2>nul
echo ✓ 目录创建完成

echo.
echo [3/5] 复制文件...
xcopy "%~dp0*" "D:\campus-media-system\" /E /I /Y /EXCLUDE:exclude.txt >nul 2>nul || xcopy "%~dp0*" "D:\campus-media-system\" /E /I /Y >nul
echo ✓ 文件复制完成

echo.
echo [4/5] 安装依赖...
cd /d "D:\campus-media-system"
%PKG% install
if %errorlevel% neq 0 (
    echo 依赖安装失败，请检查网络连接
    pause
    exit /b 1
)
echo ✓ 依赖安装完成

echo.
echo [5/5] 构建项目...
%PKG% run build
if %errorlevel% neq 0 (
    echo 构建失败
    pause
    exit /b 1
)
echo ✓ 构建完成

echo.
echo ============================================
echo    安装成功！
echo ============================================
echo.
echo 项目目录: D:\campus-media-system
echo 数据目录: D:\data\campus-media
echo.
echo 默认账号: admin / admin
echo.
echo 是否立即启动？(Y/N)
choice /c YN /n /m "请选择: "
if %errorlevel% equ 1 (
    echo.
    echo 正在启动...
    echo 访问地址: http://localhost:5173
    %PKG% run dev
) else (
    echo.
    echo 运行 D:\campus-media-system\启动.bat 可启动系统
    pause
)