@echo off
chcp 65001 >nul
REM ========================================
REM   校园宣传素材智能管理系统 - 构建安装包
REM ========================================
echo.
echo [1/5] 检查Node.js环境...
where node >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Node.js，请先安装Node.js 18+
    pause
    exit /b 1
)

echo [2/5] 构建前端...
cd /d "%~dp0\.."
call npm install
if errorlevel 1 goto :error
call npm run build
if errorlevel 1 goto :error

echo [3/5] 构建后端...
cd backend
call mvn clean package -DskipTests
if errorlevel 1 goto :error

echo [4/5] 复制构建文件...
if not exist "..\dist" mkdir "..\dist"
copy /Y "target\campus-media-system-1.0.0.jar" "..\dist\app.jar" >nul

cd ..
if not exist "installer\Output" mkdir "installer\Output"

echo [5/5] 检查 Inno Setup...
where iscc >nul 2>&1
if errorlevel 1 (
    echo [警告] 未检测到Inno Setup, 请手动运行 installer\installer.iss
    echo.
    echo 文件已就绪:
    echo   installer\app.jar
    echo   installer\installer.iss
    pause
    exit /b 0
)

echo 正在使用 Inno Setup 编译安装包...
cd installer
iscc installer.iss
if errorlevel 1 goto :error

echo.
echo ========================================
echo   构建完成！
echo   安装包: installer\Output\*.exe
echo ========================================
pause
exit /b 0

:error
echo.
echo [错误] 构建失败！
pause
exit /b 1
