# ============================================================
#   校园宣传素材智能管理系统 - 一键打包脚本 (PowerShell)
#   自动下载JRE + 构建前后端 + 打包Windows安装程序
# ============================================================

param(
    [switch]$SkipJreDownload = $false,
    [switch]$SkipBuild = $false
)

$ErrorActionPreference = "Stop"
$ProgressPreference = "Continue"

# 颜色输出函数
function Write-Info($msg) { Write-Host "[INFO] $msg" -ForegroundColor Cyan }
function Write-Success($msg) { Write-Host "[OK] $msg" -ForegroundColor Green }
function Write-Warn($msg) { Write-Host "[WARN] $msg" -ForegroundColor Yellow }
function Write-Err($msg) { Write-Host "[ERROR] $msg" -ForegroundColor Red }

Write-Host "========================================" -ForegroundColor White
Write-Host "  校园宣传素材智能管理系统" -ForegroundColor White
Write-Host "  一键打包脚本" -ForegroundColor White
Write-Host "========================================" -ForegroundColor White
Write-Host ""

$ProjectRoot = $PSScriptRoot
$BackendDir = Join-Path $ProjectRoot "backend"
$InstallerDir = Join-Path $ProjectRoot "installer"
$DistDir = Join-Path $ProjectRoot "dist"
$BuildDir = Join-Path $ProjectRoot "build"

# 步骤1: 检查环境
Write-Info "步骤1/6: 检查构建环境..."

$tools = @(
    @{Name="Node.js"; Cmd="node"; MinVersion="18.0.0"},
    @{Name="NPM"; Cmd="npm"; MinVersion="9.0.0"},
    @{Name="Maven"; Cmd="mvn"; MinVersion="3.8.0"},
    @{Name="Java"; Cmd="java"; MinVersion="21"}
)

foreach ($tool in $tools) {
    try {
        $versionOutput = & $tool.Cmd --version 2>&1
        Write-Success "$($tool.Name): $versionOutput"
    } catch {
        Write-Err "$($tool.Name) 未安装"
        exit 1
    }
}

# 步骤2: 构建前端
if (-not $SkipBuild) {
    Write-Info "步骤2/6: 构建前端..."
    Set-Location $ProjectRoot
    npm install --silent
    if ($LASTEXITCODE -ne 0) { Write-Err "npm install 失败"; exit 1 }
    npm run build
    if ($LASTEXITCODE -ne 0) { Write-Err "npm run build 失败"; exit 1 }
    Write-Success "前端构建完成"
} else {
    Write-Warn "跳过前端构建"
}

# 步骤3: 构建后端
if (-not $SkipBuild) {
    Write-Info "步骤3/6: 构建后端..."
    Set-Location $BackendDir
    mvn clean package -DskipTests -q
    if ($LASTEXITCODE -ne 0) { Write-Err "mvn package 失败"; exit 1 }
    Write-Success "后端构建完成"
} else {
    Write-Warn "跳过后端构建"
}

# 步骤4: 下载JRE
Write-Info "步骤4/6: 准备Java运行环境..."
$RuntimeDir = Join-Path $InstallerDir "runtime"

if ($SkipJreDownload) {
    Write-Warn "跳过JRE下载（使用已有JRE）"
} elseif (Test-Path "$RuntimeDir\bin\java.exe") {
    Write-Success "检测到已有JRE，跳过下载"
} else {
    Write-Info "下载 JRE 21 (约150MB)..."
    $JreUrl = "https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_windows-x64_bin.zip"
    $JreZip = Join-Path $BuildDir "jre.zip"
    
    if (-not (Test-Path $BuildDir)) { New-Item -ItemType Directory -Path $BuildDir | Out-Null }
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile($JreUrl, $JreZip)
        
        Write-Info "解压JRE..."
        Expand-Archive -Path $JreZip -DestinationPath $BuildDir -Force
        
        # 移动到runtime目录
        $extractedDir = Get-ChildItem $BuildDir -Directory | Where-Object { $_.Name -like "jdk-21*" } | Select-Object -First 1
        if ($extractedDir) {
            if (Test-Path $RuntimeDir) { Remove-Item $RuntimeDir -Recurse -Force }
            Move-Item $extractedDir.FullName $RuntimeDir
            Remove-Item $JreZip
            Write-Success "JRE安装完成"
        }
    } catch {
        Write-Warn "JRE下载失败: $_"
        Write-Warn "请手动下载JRE 21并解压到 installer\runtime\ 目录"
    }
}

# 步骤5: 复制文件到安装目录
Write-Info "步骤5/6: 准备安装包文件..."

# 复制JAR包
$JarSource = Join-Path $BackendDir "target\campus-media-system-1.0.0.jar"
$JarDest = Join-Path $InstallerDir "app.jar"
if (Test-Path $JarSource) {
    Copy-Item $JarSource $JarDest -Force
    Write-Success "复制JAR包"
} else {
    Write-Err "未找到JAR包: $JarSource"
    exit 1
}

# 下载WinSW
$WinswPath = Join-Path $InstallerDir "winsw.exe"
$WinswXmlPath = Join-Path $InstallerDir "CampusMediaService.xml"
$WinswServicePath = Join-Path $InstallerDir "CampusMediaService.exe"

if (-not (Test-Path $WinswPath)) {
    Write-Info "下载WinSW..."
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile(
            "https://github.com/winsw/winsw/releases/download/v3.0.0-alpha.10/WinSW-x64.exe",
            $WinswPath
        )
        Copy-Item $WinswPath $WinswServicePath -Force
        Write-Success "WinSW下载完成"
    } catch {
        Write-Warn "WinSW下载失败，请手动下载"
    }
}

# 步骤6: 生成安装包
Write-Info "步骤6/6: 生成安装包..."

$OutputDir = Join-Path $InstallerDir "Output"
if (-not (Test-Path $OutputDir)) { New-Item -ItemType Directory -Path $OutputDir | Out-Null }

# 检查Inno Setup
$InnoSetup = Get-Command "iscc" -ErrorAction SilentlyContinue
if ($InnoSetup) {
    Write-Info "使用 Inno Setup 编译..."
    Set-Location $InstallerDir
    & iscc installer.iss
    if ($LASTEXITCODE -eq 0) {
        Write-Success "安装包生成成功！"
        Get-ChildItem $OutputDir -Filter "*.exe" | ForEach-Object {
            $size = [math]::Round($_.Length / 1MB, 2)
            Write-Host "  文件: $($_.Name) (大小: $size MB)" -ForegroundColor Green
        }
    } else {
        Write-Err "Inno Setup 编译失败"
    }
} else {
    Write-Warn "未检测到 Inno Setup，跳过安装包编译"
    Write-Info "可手动安装 Inno Setup 后运行: iscc installer\installer.iss"
    
    # 提供绿色版压缩包
    Write-Info "生成绿色版压缩包..."
    $GreenPath = Join-Path $OutputDir "CampusMediaSystem-Green-1.0.0.zip"
    
    if (Test-Path $GreenPath) { Remove-Item $GreenPath -Force }
    Compress-Archive -Path "$InstallerDir\*" -DestinationPath $GreenPath -Force
    
    $size = [math]::Round((Get-Item $GreenPath).Length / 1MB, 2)
    Write-Success "绿色版已生成: $GreenPath ($size MB)"
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  打包完成！" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "安装包位置: $OutputDir" -ForegroundColor Cyan
Write-Host ""
