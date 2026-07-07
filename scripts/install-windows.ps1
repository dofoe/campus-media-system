# 校园宣传素材智能管理系统 - Windows一键安装脚本
# 使用方法: 右键 -> 使用 PowerShell 运行

param(
    [string]$InstallPath = "D:\campus-media-system"
)

$ErrorActionPreference = "Stop"
$Host.UI.RawUI.WindowTitle = "校园宣传素材智能管理系统 - 一键安装"

Write-Host "============================================" -ForegroundColor Green
Write-Host "  校园宣传素材智能管理系统 - 一键安装" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green

# 检查管理员权限
function Test-Administrator {
    $currentUser = [Security.Principal.WindowsIdentity]::GetCurrent()
    $principal = New-Object Security.Principal.WindowsPrincipal($currentUser)
    return $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

Write-Host ""
Write-Host "[1/7] 检查系统环境..." -ForegroundColor Yellow

# 检查 Node.js
$nodePath = Get-Command node -ErrorAction SilentlyContinue
if (-not $nodePath) {
    Write-Host "错误: Node.js 未安装" -ForegroundColor Red
    Write-Host "请先安装 Node.js 18+ : https://nodejs.org/" -ForegroundColor Yellow
    Write-Host "按任意键退出..."
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    exit 1
}

$nodeVersion = (node --version).Replace("v", "")
Write-Host "✓ Node.js 版本: $nodeVersion" -ForegroundColor Green

# 检查包管理器
$pnpmPath = Get-Command pnpm -ErrorAction SilentlyContinue
if ($pnpmPath) {
    $packageManager = "pnpm"
    Write-Host "✓ 包管理器: pnpm" -ForegroundColor Green
} else {
    $npmPath = Get-Command npm -ErrorAction SilentlyContinue
    if ($npmPath) {
        $packageManager = "npm"
        Write-Host "✓ 包管理器: npm" -ForegroundColor Green
    } else {
        Write-Host "错误: npm 或 pnpm 未安装" -ForegroundColor Red
        Write-Host "按任意键退出..."
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        exit 1
    }
}

Write-Host ""
Write-Host "[2/7] 创建安装目录..." -ForegroundColor Yellow

# 创建安装目录
if (-not (Test-Path $InstallPath)) {
    New-Item -ItemType Directory -Path $InstallPath -Force | Out-Null
    Write-Host "✓ 创建目录: $InstallPath" -ForegroundColor Green
} else {
    Write-Host "✓ 目录已存在: $InstallPath" -ForegroundColor Green
}

# 创建数据目录
$dataPath = "D:\data\campus-media"
$uploadsPath = "$dataPath\uploads"
$logsPath = "$dataPath\logs"
$distPath = "$dataPath\dist"

New-Item -ItemType Directory -Path $uploadsPath -Force | Out-Null
New-Item -ItemType Directory -Path $logsPath -Force | Out-Null
New-Item -ItemType Directory -Path $distPath -Force | Out-Null

Write-Host "✓ 数据目录创建完成" -ForegroundColor Green

Write-Host ""
Write-Host "[3/7] 复制项目文件..." -ForegroundColor Yellow

# 获取脚本所在目录（项目根目录）
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
if ($scriptDir -eq "") {
    $scriptDir = Get-Location
}

# 复制所有文件到安装目录
$sourcePath = Split-Path -Parent $scriptDir
if (Test-Path "$sourcePath\src") {
    # 从压缩包解压后的目录复制
    Copy-Item -Path "$sourcePath\*" -Destination $InstallPath -Recurse -Force
} elseif (Test-Path "$scriptDir\src") {
    # 直接从当前目录复制
    Copy-Item -Path "$scriptDir\*" -Destination $InstallPath -Recurse -Force -Exclude "scripts"
}

Write-Host "✓ 文件复制完成" -ForegroundColor Green

Write-Host ""
Write-Host "[4/7] 安装项目依赖..." -ForegroundColor Yellow

Set-Location $InstallPath

if ($packageManager -eq "pnpm") {
    pnpm install
} else {
    npm install
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 依赖安装失败" -ForegroundColor Red
    Write-Host "按任意键退出..."
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    exit 1
}

Write-Host "✓ 依赖安装完成" -ForegroundColor Green

Write-Host ""
Write-Host "[5/7] 构建生产版本..." -ForegroundColor Yellow

if ($packageManager -eq "pnpm") {
    pnpm run build
} else {
    npm run build
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 构建失败" -ForegroundColor Red
    Write-Host "按任意键退出..."
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    exit 1
}

Write-Host "✓ 构建完成" -ForegroundColor Green

Write-Host ""
Write-Host "[6/7] 复制构建产物..." -ForegroundColor Yellow

Copy-Item -Path "$InstallPath\dist\*" -Destination $distPath -Recurse -Force

Write-Host "✓ 产物复制完成" -ForegroundColor Green

Write-Host ""
Write-Host "[7/7] 创建启动脚本..." -ForegroundColor Yellow

# 创建启动脚本
$startScript = @"
@echo off
cd /d "$InstallPath"
$packageManager run dev
"@

$startScriptPath = "$InstallPath\start-dev.bat"
Set-Content -Path $startScriptPath -Value $startScript -Encoding ASCII

# 创建生产启动脚本
$startProdScript = @"
@echo off
echo 校园宣传素材智能管理系统
echo 访问地址: http://localhost
echo.
echo 请配置 Nginx 指向 $distPath
echo 或使用开发模式运行
pause
"@

$startProdScriptPath = "$InstallPath\start-prod.bat"
Set-Content -Path $startProdScriptPath -Value $startProdScript -Encoding ASCII

Write-Host "✓ 启动脚本创建完成" -ForegroundColor Green

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "  安装完成！" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green

Write-Host ""
Write-Host "安装信息:" -ForegroundColor Yellow
Write-Host "  项目目录: $InstallPath"
Write-Host "  数据目录: $dataPath"
Write-Host "  上传目录: $uploadsPath"
Write-Host "  日志目录: $logsPath"

Write-Host ""
Write-Host "启动方式:" -ForegroundColor Yellow
Write-Host "  开发模式: 运行 $InstallPath\start-dev.bat"
Write-Host "  生产模式: 运行 $InstallPath\start-prod.bat"

Write-Host ""
Write-Host "默认管理员账号:" -ForegroundColor Yellow
Write-Host "  用户名: admin"
Write-Host "  密码: admin"

Write-Host ""
Write-Host "是否立即启动开发服务器？(Y/N)" -ForegroundColor Cyan
$response = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

if ($response.Character -eq "Y" -or $response.Character -eq "y") {
    Write-Host ""
    Write-Host "正在启动开发服务器..." -ForegroundColor Yellow
    Write-Host "访问地址: http://localhost:5173" -ForegroundColor Green
    Set-Location $InstallPath
    if ($packageManager -eq "pnpm") {
        pnpm run dev
    } else {
        npm run dev
    }
} else {
    Write-Host ""
    Write-Host "安装完成，按任意键退出..."
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}