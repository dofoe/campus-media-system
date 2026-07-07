# 校园宣传素材智能管理系统 - Windows一键安装部署

## 一键安装方案已实现

经过重构，系统已支持 Windows一键安装 部署模式，零外部依赖。

---

## 核心改动

| 改动 | 原方案 | 新方案 | 效果 |
|------|--------|--------|------|
| 数据库 | PostgreSQL（需安装服务） | H2嵌入式（文件数据库） | 零配置 |
| Web服务器 | Nginx（需安装配置） | Spring Boot内嵌Tomcat | 启动即用 |
| 前端部署 | Nginx托管 | 打包进JAR包 | 单文件分发 |
| JDK | 需用户安装 | 打包JRE 21 | 免装Java |
| Windows服务 | 手动配置 | WinSW自动注册 | 开机自启 |
| 安装器 | 手动部署 | Inno Setup一键安装 | 双击安装 |

---

## 部署架构对比

### 改造前（多组件）
```
Windows Server
├── PostgreSQL 14  (200MB) 需安装服务
├── Nginx         (10MB)   需配置
├── Node.js       (50MB)   仅构建时需要
├── JDK 21        (180MB)  需配置JAVA_HOME
└── Spring Boot JAR (100MB)
合计：~540MB，6+ 步骤
```

### 改造后（一体化）
```
Windows Server
└── 单个安装包 (≈200MB)
    ├── app.jar (含前后端)
    ├── runtime/JRE 21
    ├── WinSW (服务注册)
    └── start.bat / stop.bat
合计：~200MB，1 步安装
```

---

## 一键安装包目录结构

```
CampusMediaSystem-v1.0.0/
├── app.jar                    # 主程序（含前后端，~80MB）
├── runtime/                   # JRE 21（~120MB）
│   └── bin/java.exe
├── winsw.exe                  # Windows服务工具
├── CampusMediaService.exe     # 服务包装器
├── CampusMediaService.xml     # 服务配置
├── start.bat                  # 启动脚本
├── stop.bat                   # 停止脚本
├── install-service.bat        # 注册Windows服务
├── uninstall-service.bat      # 卸载Windows服务
├── README.txt                 # 快速说明
├── 使用说明.txt                # 详细文档
└── data/                      # 数据目录（自动创建）
    ├── campus_media.mv.db     # H2数据库文件
    └── uploads/               # 上传文件
```

---

## 三种部署方式

### 方式1：一键安装包（推荐商用）

**安装步骤：**
1. 双击 CampusMediaSystemSetup-1.0.0.exe
2. 选择安装目录（默认 C:\Program Files\校园宣传素材智能管理系统）
3. 勾选注册为Windows服务
4. 点击安装

**特性：**
- 自动创建桌面快捷方式
- 自动注册Windows服务（开机自启）
- 自动启动服务
- 提供控制面板卸载入口
- 自动处理防火墙

**安装包生成：**
```powershell
# 在Windows上运行（需安装 Inno Setup）
.\build-installer.ps1
# 输出: installer\Output\*.exe
```

---

### 方式2：绿色版（解压即用）

**使用步骤：**
1. 解压 CampusMediaSystem-Green-1.0.0.zip 到任意目录
2. 双击 start.bat 启动
3. 浏览器访问 http://localhost:8080
4. 默认账号 admin / admin

**适用场景：**
- 临时演示
- U盘携带
- 无管理员权限环境
- 快速测试

---

### 方式3：注册为Windows服务

**适用场景：**
- 服务器长期运行
- 需要开机自启
- 无人值守运维

**步骤：**
1. 右键 install-service.bat → 以管理员身份运行
2. 服务自动启动并设为自动启动
3. 可在 services.msc 中管理

**常用命令：**
```cmd
# 启动服务
net start CampusMediaService

# 停止服务
net stop CampusMediaService

# 查看服务状态
sc query CampusMediaService

# 卸载服务（管理员）
uninstall-service.bat
```

---

## 构建一键安装包

### 前置环境（构建机需要）
| 软件 | 版本 | 用途 |
|------|------|------|
| Node.js | 18+ | 构建前端 |
| JDK 21 | 21+ | 构建后端 |
| Maven | 3.8+ | 构建后端 |
| Inno Setup | 6.x | 生成EXE安装包（可选） |

### 一键构建命令

**Windows (PowerShell):**
```powershell
cd 项目根目录
.\build-installer.ps1
```

**Linux/macOS:**
```bash
cd 项目根目录
chmod +x build-installer.sh
./build-installer.sh
```

### 构建流程
1. npm install           # 安装前端依赖
2. npm run build         # 构建前端静态资源
3. mvn package           # 构建后端JAR
4. 下载 JRE 21           # 自动下载到 installer\runtime\
5. 下载 WinSW            # Windows服务工具
6. 复制文件到 installer\
7. 调用 Inno Setup       # 生成 EXE 安装包

### 构建产物
```
installer/Output/
├── CampusMediaSystemSetup-1.0.0.exe    # EXE安装包（推荐）
├── CampusMediaSystem-Green-1.0.0.zip   # 绿色版压缩包
└── CampusMediaSystem-Linux-1.0.0.tar.gz # Linux版本
```

---

## 安装包大小估算

| 组件 | 体积 |
|------|------|
| JRE 21 | ~150MB |
| app.jar (含前后端) | ~80MB |
| WinSW | < 1MB |
| Inno Setup安装器自身 | ~2MB |
| 总安装包 | ~230MB |

---

## 端口和访问

| 项目 | 值 |
|------|------|
| 默认端口 | 8080 |
| 访问地址 | http://localhost:8080 |
| H2控制台 | http://localhost:8080/h2-console |
| 默认账号 | admin |
| 默认密码 | admin |

### 修改端口
编辑 application-h2.yml：
```yaml
server:
  port: 9090  # 修改为其他端口
```

---

## 数据备份

### 备份内容
```
安装目录/
├── data/        ← H2数据库文件
└── uploads/     ← 上传的文件
```

### 一键备份（管理员）
```cmd
xcopy /E /I /Y "C:\Program Files\校园宣传素材智能管理系统\data" D:\Backup\data
xcopy /E /I /Y "C:\Program Files\校园宣传素材智能管理系统\uploads" D:\Backup\uploads
```

### 恢复
将备份目录覆盖回原位置，重启服务。

---

## 故障排查

### Q1: 启动报错端口被占用
**解决：**
```cmd
netstat -ano | findstr :8080
taskkill /F /PID <进程ID>
```
或修改 application-h2.yml 改用其他端口。

### Q2: 服务无法启动
**查看日志：**
```cmd
type logs\app.log
```

### Q3: 忘记管理员密码
**重置（删除数据库重启）：**
```cmd
net stop CampusMediaService
del data\campus_media*
net start CampusMediaService
```
系统会使用初始数据重建。

---

## 商用部署清单

部署到生产环境前，请完成：

- [ ] 修改默认密码（admin/admin）
- [ ] 修改JWT密钥（application-h2.yml 中的 jwt.secret）
- [ ] 配置定期备份（推荐每日）
- [ ] 配置防火墙规则（仅开放 8080 给可信IP）
- [ ] 配置HTTPS（如需外网访问，使用反向代理）
- [ ] 设置日志归档（避免日志占满磁盘）
- [ ] 注册Windows服务（开机自启）

---

## 总结

- 支持Windows一键安装
- 零外部依赖（JRE内置）
- 支持Windows服务（开机自启）
- 绿色版（解压即用）
- EXE安装包（专业）
- 数据持久化（文件数据库）
- 完整备份机制

系统现已具备完整的Windows一键安装部署能力。
