<#
.SYNOPSIS
    Campus Media System - One-click Build Script for Windows

.DESCRIPTION
    Automatically builds frontend, backend, downloads JRE, and packages installer.

.EXAMPLE
    .\build-installer.ps1
#>

param(
    [switch]$SkipJreDownload = $false,
    [switch]$SkipBuild = $false
)

$ErrorActionPreference = "Stop"
$ProgressPreference = "Continue"

function Write-Info($msg) { Write-Host "[INFO] $msg" -ForegroundColor Cyan }
function Write-Success($msg) { Write-Host "[OK] $msg" -ForegroundColor Green }
function Write-Warn($msg) { Write-Host "[WARN] $msg" -ForegroundColor Yellow }
function Write-Err($msg) { Write-Host "[ERROR] $msg" -ForegroundColor Red }

Write-Host "========================================" -ForegroundColor White
Write-Host "  Campus Media System" -ForegroundColor White
Write-Host "  One-click Build Script" -ForegroundColor White
Write-Host "========================================" -ForegroundColor White
Write-Host ""

$ProjectRoot = $PSScriptRoot
$BackendDir = Join-Path $ProjectRoot "backend"
$InstallerDir = Join-Path $ProjectRoot "installer"
$BuildDir = Join-Path $ProjectRoot "build"

Write-Info "Step 1/6: Checking build environment..."

$tools = @(
    @{Name="Node.js"; Cmd="node"},
    @{Name="NPM"; Cmd="npm"},
    @{Name="Maven"; Cmd="mvn"},
    @{Name="Java"; Cmd="java"}
)

foreach ($tool in $tools) {
    try {
        $versionOutput = & $tool.Cmd --version 2>&1
        Write-Success "$($tool.Name): OK"
    }
    catch {
        Write-Err "$($tool.Name) not installed"
        Write-Err "Please install from: $($tool.Name) official website"
        exit 1
    }
}

if (-not $SkipBuild) {
    Write-Info "Step 2/6: Building frontend..."
    Set-Location $ProjectRoot
    npm install --silent
    if ($LASTEXITCODE -ne 0) {
        Write-Err "npm install failed"
        exit 1
    }
    npm run build
    if ($LASTEXITCODE -ne 0) {
        Write-Err "npm run build failed"
        exit 1
    }
    Write-Success "Frontend build completed"
} else {
    Write-Warn "Skip frontend build"
}

if (-not $SkipBuild) {
    Write-Info "Step 3/6: Building backend..."
    Set-Location $BackendDir
    mvn clean package -DskipTests -q
    if ($LASTEXITCODE -ne 0) {
        Write-Err "mvn package failed"
        exit 1
    }
    Write-Success "Backend build completed"
} else {
    Write-Warn "Skip backend build"
}

Write-Info "Step 4/6: Preparing Java runtime..."
$RuntimeDir = Join-Path $InstallerDir "runtime"

if ($SkipJreDownload) {
    Write-Warn "Skip JRE download (using existing)"
} elseif (Test-Path "$RuntimeDir\bin\java.exe") {
    Write-Success "Found existing JRE, skip download"
} else {
    Write-Info "Downloading JRE 21 (~150MB)..."
    $JreUrl = "https://download.java.net/java/GA/jdk21.0.2/f2283984656d49d69e91c558476027ac/13/GPL/openjdk-21.0.2_windows-x64_bin.zip"
    $JreZip = Join-Path $BuildDir "jre.zip"
    
    if (-not (Test-Path $BuildDir)) {
        New-Item -ItemType Directory -Path $BuildDir | Out-Null
    }
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile($JreUrl, $JreZip)
        
        Write-Info "Extracting JRE..."
        Expand-Archive -Path $JreZip -DestinationPath $BuildDir -Force
        
        $extractedDir = Get-ChildItem $BuildDir -Directory | Where-Object { $_.Name -like "jdk-21*" } | Select-Object -First 1
        if ($extractedDir) {
            if (Test-Path $RuntimeDir) {
                Remove-Item $RuntimeDir -Recurse -Force
            }
            Move-Item $extractedDir.FullName $RuntimeDir
            Remove-Item $JreZip
            Write-Success "JRE installation completed"
        }
    }
    catch {
        Write-Warn "JRE download failed: $_"
        Write-Warn "Please download JRE 21 manually and extract to installer\runtime\"
    }
}

Write-Info "Step 5/6: Preparing installer files..."

$JarSource = Join-Path $BackendDir "target\campus-media-system-1.0.0.jar"
$JarDest = Join-Path $InstallerDir "app.jar"
if (Test-Path $JarSource) {
    Copy-Item $JarSource $JarDest -Force
    Write-Success "Copied JAR package"
} else {
    Write-Err "JAR file not found: $JarSource"
    exit 1
}

$WinswPath = Join-Path $InstallerDir "winsw.exe"
$WinswServicePath = Join-Path $InstallerDir "CampusMediaService.exe"

if (-not (Test-Path $WinswPath)) {
    Write-Info "Downloading WinSW..."
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.DownloadFile(
            "https://github.com/winsw/winsw/releases/download/v3.0.0-alpha.10/WinSW-x64.exe",
            $WinswPath
        )
        Copy-Item $WinswPath $WinswServicePath -Force
        Write-Success "WinSW download completed"
    }
    catch {
        Write-Warn "WinSW download failed, please download manually"
    }
}

Write-Info "Step 6/6: Generating installer package..."

$OutputDir = Join-Path $InstallerDir "Output"
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Path $OutputDir | Out-Null
}

$InnoSetup = Get-Command "iscc" -ErrorAction SilentlyContinue
if ($InnoSetup) {
    Write-Info "Compiling with Inno Setup..."
    Set-Location $InstallerDir
    & iscc installer.iss
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Installer package generated successfully!"
        Get-ChildItem $OutputDir -Filter "*.exe" | ForEach-Object {
            $fileSize = [math]::Round($_.Length / 1MB, 2)
            Write-Host ("  File: " + $_.Name + " (Size: " + $fileSize + " MB)") -ForegroundColor Green
        }
    } else {
        Write-Err "Inno Setup compilation failed"
    }
} else {
    Write-Warn "Inno Setup not detected, skip EXE compilation"
    Write-Info "You can install Inno Setup and run: iscc installer\installer.iss"
    
    Write-Info "Generating green version zip..."
    $GreenPath = Join-Path $OutputDir "CampusMediaSystem-Green-1.0.0.zip"
    
    if (Test-Path $GreenPath) {
        Remove-Item $GreenPath -Force
    }
    Compress-Archive -Path "$InstallerDir\*" -DestinationPath $GreenPath -Force
    
    $fileSize = [math]::Round((Get-Item $GreenPath).Length / 1MB, 2)
    Write-Success ("Green version generated: " + $GreenPath + " (" + $fileSize + " MB)")
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Build completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host ("Installer location: " + $OutputDir) -ForegroundColor Cyan
Write-Host ""
