@echo off
chcp 65001 >nul
title 校园宣传素材智能管理系统

echo ========================================
echo   校园宣传素材智能管理系统
echo   Campus Media Management System
echo ========================================
echo.

REM 设置Java路径
set JAVA_HOME=%~dp0runtime
set PATH=%JAVA_HOME%\bin;%PATH%

REM 检查Java环境
echo [1/4] 检查Java环境...
java -version 2>&1 | findstr /R "version" >nul
if errorlevel 1 (
    echo [错误] 未找到Java运行环境！
    echo 请确保 runtime 目录包含完整的JRE
    pause
    exit /b 1
)
echo    Java环境正常
echo.

REM 创建必要目录
echo [2/4] 初始化目录...
if not exist "data" mkdir data
if not exist "logs" mkdir logs
if not exist "uploads" mkdir uploads
echo    目录初始化完成
echo.

REM 检查端口占用
echo [3/4] 检查端口8080...
netstat -ano | findstr :8080 >nul
if not errorlevel 1 (
    echo [警告] 端口8080已被占用！
    echo 请关闭占用8080端口的程序后重试
    pause
    exit /b 1
)
echo    端口检查通过
echo.

REM 启动应用
echo [4/4] 启动应用服务...
echo.
echo ========================================
echo   系统已启动！
echo   访问地址: http://localhost:8080
echo   默认账号: admin / admin
echo   按 Ctrl+C 可停止服务
echo ========================================
echo.

java -Xms256m -Xmx1024m -jar -Dfile.encoding=UTF-8 app.jar

echo.
echo 服务已停止
pause
