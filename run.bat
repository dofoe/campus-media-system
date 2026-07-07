@echo off
chcp 65001 >nul
title 校园宣传素材智能管理系统 - 绿色版启动

echo ========================================
echo   校园宣传素材智能管理系统
echo   绿色版 - 解压即用
echo ========================================
echo.

REM 检查Java环境
echo [1/4] 检查Java环境...
java -version 2>&1 | findstr /R "version" >nul
if errorlevel 1 (
    echo.
    echo [错误] 未检测到Java环境！
    echo.
    echo 请安装 JDK 21 或 JRE 21:
    echo   下载地址: https://adoptium.net/
    echo   安装后重启此脚本
    echo.
    pause
    exit /b 1
)
echo    Java环境正常
echo.

REM 检查Node.js（用于构建前端）
echo [2/4] 检查构建环境...
where node >nul 2>&1
if errorlevel 1 (
    echo [警告] 未检测到Node.js，将跳过前端构建
    echo    如需完整功能，请安装 Node.js 18+: https://nodejs.org/
    set SKIP_FRONTEND=1
) else (
    echo    Node.js已安装
    set SKIP_FRONTEND=0
)
echo.

REM 创建必要目录
echo [3/4] 初始化目录...
if not exist "data" mkdir data
if not exist "logs" mkdir logs
if not exist "uploads" mkdir uploads
if not exist "backend\target" mkdir backend\target
echo    目录初始化完成
echo.

REM 构建检查
echo [4/4] 检查构建状态...

REM 检查后端JAR是否存在
if not exist "backend\target\campus-media-system-1.0.0.jar" (
    echo.
    echo [提示] 后端未构建，正在构建...
    echo.
    
    REM 检查Maven
    where mvn >nul 2>&1
    if errorlevel 1 (
        echo [错误] 未检测到Maven！
        echo   请安装 Maven 3.8+: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
    
    cd backend
    call mvn clean package -DskipTests -q
    if errorlevel 1 (
        echo [错误] 后端构建失败！
        cd ..
        pause
        exit /b 1
    )
    cd ..
    echo    后端构建完成
)

REM 检查前端是否构建
if not exist "dist\index.html" (
    if "%SKIP_FRONTEND%"=="0" (
        echo.
        echo [提示] 前端未构建，正在构建...
        call npm install --silent
        call npm run build
        if errorlevel 1 (
            echo [警告] 前端构建失败，将使用后端内置前端
        ) else (
            echo    前端构建完成
        )
    )
)

echo.
echo ========================================
echo   正在启动系统...
echo ========================================
echo.

REM 启动后端服务
java -Xms256m -Xmx1024m -jar -Dfile.encoding=UTF-8 backend\target\campus-media-system-1.0.0.jar

echo.
echo 服务已停止
pause