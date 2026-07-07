# 快速开始指南

## 从GitHub下载后如何运行？

GitHub仓库是**源代码仓库**，下载的zip需要先构建才能运行。

---

## 方式1：最简单（推荐新手）

### Windows用户

1. 确保已安装：
   - **JDK 21**：https://adoptium.net/（选择JDK 21，安装后重启）
   - **Maven**：https://maven.apache.org/download.cgi（解压后配置PATH）
   - **Node.js 18+**：https://nodejs.org/（可选，但推荐安装）

2. 解压下载的zip文件

3. 双击运行 `run.bat`

4. 等待构建完成后，浏览器访问 http://localhost:8080

5. 默认账号：`admin` / 密码：`admin`

### Linux/macOS用户

```bash
# 1. 解压zip
unzip campus-media-system-main.zip
cd campus-media-system-main

# 2. 给脚本执行权限
chmod +x run.sh

# 3. 运行
./run.sh

# 4. 浏览器访问 http://localhost:8080
# 默认账号：admin / admin
```

---

## 方式2：手动构建（开发者）

### 步骤1：安装环境

| 软件 | 版本 | 下载地址 |
|------|------|---------|
| JDK | 21+ | https://adoptium.net/ |
| Maven | 3.8+ | https://maven.apache.org/ |
| Node.js | 18+ | https://nodejs.org/ |

### 步骤2：构建前端

```bash
# Windows
npm install
npm run build

# Linux/macOS
npm install
npm run build
```

### 步骤3：构建后端

```bash
# Windows
cd backend
mvn clean package -DskipTests
cd ..

# Linux/macOS
cd backend
mvn clean package -DskipTests
cd ..
```

### 步骤4：启动服务

```bash
# Windows
java -jar backend\target\campus-media-system-1.0.0.jar

# Linux/macOS
java -jar backend/target/campus-media-system-1.0.0.jar
```

### 步骤5：访问

浏览器打开 http://localhost:8080

---

## 方式3：生成安装包（进阶）

如果您想生成 `CampusMediaSystemSetup-1.0.0.exe` 安装包：

### Windows

```powershell
# 需要额外安装 Inno Setup 6.x
# https://jrsoftware.org/isdl.php

# 运行打包脚本
.\build-installer.ps1

# 安装包位置
# installer\Output\CampusMediaSystemSetup-1.0.0.exe
```

### Linux/macOS

```bash
chmod +x build-installer.sh
./build-installer.sh

# 输出
# installer/Output/CampusMediaSystem-Green-1.0.0.zip
# installer/Output/CampusMediaSystem-Linux-1.0.0.tar.gz
```

---

## 常见问题

### Q: 运行run.bat提示"未检测到Java环境"

**解决**：安装JDK 21
- 下载：https://adoptium.net/
- 选择 JDK 21（Windows x64）
- 安装后**重启电脑**
- 验证：打开cmd输入 `java -version`

### Q: 运行run.bat提示"未检测到Maven"

**解决**：安装Maven
- 下载：https://maven.apache.org/download.cgi
- 选择 Binary zip archive
- 解压到如 `C:\Program Files\Apache\maven`
- 配置环境变量：
  - 新建 `MAVEN_HOME` = `C:\Program Files\Apache\maven`
  - 编辑 `Path`，添加 `%MAVEN_HOME%\bin`
- 验证：打开cmd输入 `mvn -version`

### Q: 构建很慢/卡住

**解决**：配置Maven国内镜像

编辑 `%USERPROFILE%\.m2\settings.xml`（如不存在则创建）：

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

### Q: 端口8080被占用

**解决**：
```cmd
# 查看占用
netstat -ano | findstr :8080

# 结束进程
taskkill /F /PID <进程号>
```

或修改 `backend\src\main\resources\application.yml`：
```yaml
server:
  port: 9090  # 改用其他端口
```

---

## 访问地址

| 项目 | 地址 |
|------|------|
| 系统首页 | http://localhost:8080 |
| H2数据库控制台 | http://localhost:8080/h2-console |
| GitHub仓库 | https://github.com/dofoe/campus-media-system |

---

## 下一步

- 查看 [INSTALL_WINDOWS.md](INSTALL_WINDOWS.md) 了解完整部署方案
- 查看 [DEPLOYMENT.md](DEPLOYMENT.md) 了解生产环境部署