@echo off
chcp 65001 >nul
echo ========================================
echo   校园宣传素材智能管理系统 - 服务安装
echo ========================================
echo.

REM 设置安装目录
set INSTALL_DIR=%~dp0
cd /d "%INSTALL_DIR%"

REM 检查管理员权限
net session >nul 2>&1
if errorlevel 1 (
    echo [错误] 请以管理员身份运行此脚本！
    pause
    exit /b 1
)

echo [1/5] 复制WinSW工具...
if not exist "winsw.exe" (
    echo [错误] 缺少 winsw.exe 文件
    pause
    exit /b 1
)

echo [2/5] 注册Windows服务...
CampusMediaService.exe install

if errorlevel 1 (
    echo [警告] 使用兼容模式安装...
    winsw.exe install
)

echo [3/5] 启动Windows服务...
CampusMediaService.exe start

if errorlevel 1 (
    winsw.exe start
)

echo [4/5] 设置服务为自动启动...
sc config CampusMediaService start= auto

echo [5/5] 完成安装！
echo.
echo ========================================
echo   服务安装完成
echo   服务名称: CampusMediaService
echo   访问地址: http://localhost:8080
echo   默认账号: admin / admin
echo ========================================
echo.
echo 常用命令:
echo   启动服务: net start CampusMediaService
echo   停止服务: net stop CampusMediaService
echo   卸载服务: CampusMediaService.exe uninstall
echo.
pause
