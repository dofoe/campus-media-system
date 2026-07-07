@echo off
REM =========================================
REM  Campus Media System - Diagnostic Tool
REM  Helps identify environment issues
REM =========================================

echo ========================================
echo   Campus Media System - Diagnostic
echo ========================================
echo.

echo [1] Current directory:
cd
echo.

echo [2] Java check:
java -version 2>&1
if errorlevel 1 (
    echo   FAIL: Java not found
) else (
    echo   OK: Java found
)
echo.

echo [3] Maven check:
call mvn --version >nul 2>&1
if errorlevel 1 (
    echo   FAIL: Maven not found or not working
) else (
    call mvn --version 2>&1 | findstr "Apache Maven"
    echo   OK: Maven is working
)
echo.

echo [4] Node.js check:
node --version 2>&1
if errorlevel 1 (
    echo   FAIL: Node.js not found
) else (
    echo   OK: Node.js found
)
echo.

echo [5] npm check:
npm --version 2>&1
if errorlevel 1 (
    echo   FAIL: npm not found
) else (
    echo   OK: npm found
)
echo.

echo [6] Project files check:
if exist "vite.config.js" (
    echo   OK: Frontend project found
) else (
    echo   WARN: vite.config.js not found (maybe wrong directory)
)

if exist "backend\pom.xml" (
    echo   OK: Backend project found
) else (
    echo   WARN: backend/pom.xml not found
)
echo.

echo ========================================
echo   Diagnostic complete
echo ========================================
echo.
echo If all checks show OK, you can run build or start
echo.
pause