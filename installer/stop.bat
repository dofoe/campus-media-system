@echo off
chcp 65001 >nul
echo Stopping Campus Media System...
echo.

net stop CampusMediaService 2>nul

for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do (
    echo Killing process PID: %%a
    taskkill /F /PID %%a 2>nul
)

echo.
echo Service stopped
timeout /t 2 >nul