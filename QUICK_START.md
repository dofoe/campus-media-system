# Quick Start Guide

## How to run after downloading from GitHub?

The GitHub repo contains **source code**, not pre-built packages. You need to build it first.

---

## Step 0: Run Diagnostic First

**First, double-click `check-env.bat`** to check your environment.

This will tell you exactly what's missing.

---

## Option A: Fastest (Most Users)

### On Windows

1. Install **JDK 21**: https://adoptium.net/
   - Choose "JDK 21 LTS", Windows x64, .msi installer
   - **Important**: Check "Set JAVA_HOME variable" during installation
   - Restart your computer after installation

2. Install **Apache Maven**: https://maven.apache.org/download.cgi
   - Download "Binary zip archive" (apache-maven-3.9.6-bin.zip)
   - Extract to `C:\apache-maven-3.9.6`
   - Add to PATH: `C:\apache-maven-3.9.6\bin`
   - How to add to PATH:
     - Right-click "This PC" → Properties → Advanced system settings → Environment Variables
     - Find "Path" under System variables → Edit → New → paste the path → OK
   - Open a **new** Command Prompt and type `mvn -version` to verify

3. Install **Node.js 18+** (optional but recommended): https://nodejs.org/

4. **Double-click `build.bat`** to build

5. **Double-click `start.bat`** to run

6. Open browser: http://localhost:8080

7. Login with: `admin` / `admin`

---

## Option B: Manual Build (Developers)

### Step 1: Install required tools

| Tool | Version | Download |
|------|---------|----------|
| JDK | 21+ | https://adoptium.net/ |
| Maven | 3.8+ | https://maven.apache.org/download.cgi |
| Node.js | 18+ | https://nodejs.org/ |

### Step 2: Verify installation

Open a new Command Prompt and run:
```cmd
java -version
mvn -version
node -v
npm -v
```

All should show version numbers.

### Step 3: Build frontend

```cmd
cd campus-media-system
npm install
npm run build
```

### Step 4: Build backend

```cmd
cd backend
mvn clean package -DskipTests
cd ..
```

### Step 5: Start server

```cmd
java -jar backend\target\campus-media-system-1.0.0.jar
```

### Step 6: Access

Open browser: http://localhost:8080

---

## Option C: Generate EXE Installer (Advanced)

To generate `CampusMediaSystemSetup-1.0.0.exe`:

### On Windows

```cmd
# Need to install Inno Setup 6.x first:
# https://jrsoftware.org/isdl.php

# Then run build script
build-installer.ps1

# Output: installer\Output\CampusMediaSystemSetup-*.exe
```

---

## Common Problems

### Q: Double-click .bat and nothing happens?

A: The script likely has an error and exits immediately.

**Fix**: 
1. Open Command Prompt (type `cmd` in address bar)
2. Type the script name manually: `build.bat`
3. Now you can see the error message

### Q: "Maven not installed" even after installing?

A: PATH not updated.

**Fix**:
1. Open a **new** Command Prompt (not the old one)
2. Type `mvn -version`
3. If still not found, restart your computer
4. Double-check PATH has `C:\apache-maven-3.9.6\bin`

### Q: Maven downloads are very slow?

A: Configure Alibaba mirror.

Create file `C:\Users\YOUR_USERNAME\.m2\settings.xml`:
```xml
<?xml version="1.0"?>
<settings>
    <mirrors>
        <mirror>
            <id>aliyun</id>
            <mirrorOf>central</mirrorOf>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>
</settings>
```

### Q: Port 8080 is occupied?

A:
```cmd
# Find the process
netstat -ano | findstr :8080

# Kill it (replace PID with the number from above)
taskkill /F /PID 12345
```

Or edit `backend\src\main\resources\application.yml` to use a different port.

### Q: Java not found error?

A: Install JDK 21 and make sure JAVA_HOME is set.

**Check**:
```cmd
echo %JAVA_HOME%
java -version
```

If `%JAVA_HOME%` is empty, reinstall JDK with the "Set JAVA_HOME" option checked.

---

## URLs

| Item | URL |
|------|-----|
| System | http://localhost:8080 |
| H2 Console | http://localhost:8080/h2-console |
| GitHub | https://github.com/dofoe/campus-media-system |

---

## Next Steps

- Read [INSTALL_WINDOWS.md](INSTALL_WINDOWS.md) for full Windows deployment guide
- Read [DEPLOYMENT.md](DEPLOYMENT.md) for production deployment
