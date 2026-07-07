@echo off
chcp 65001 >nul
echo ========================================
echo   校园宣传素材智能管理系统 - 服务卸载
echo ========================================
echo.

net session >nul 2>&1
if errorlevel 1 (
    echo [错误] 请以管理员身份运行此脚本！
    pause
    exit /b 1
)

echo [1/3] 停止服务...
net stop CampusMediaService 2>nul
timeout /t 3 >nul

echo [2/3] 卸载服务...
CampusMediaService.exe uninstall 2>nul
if errorlevel 1 (
    winsw.exe uninstall 2>nul
)

echo [3/3] 完成卸载
echo.
echo 服务已卸载，但数据文件保留在 data 目录
echo 如需完全清理，请手动删除安装目录
echo.
pause
