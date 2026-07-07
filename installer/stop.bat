@echo off
chcp 65001 >nul
echo 正在停止校园宣传素材智能管理系统...
echo.

REM 停止Windows服务（如果已注册）
net stop CampusMediaService 2>nul

REM 通过端口查找并停止Java进程
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    echo 终止进程 PID: %%a
    taskkill /F /PID %%a 2>nul
)

echo.
echo 服务已停止
timeout /t 2 >nul
