# 校园宣传素材智能管理系统 - 商用部署指南

## 一、系统架构

```
+------------------+        +------------------+        +------------------+
|     Nginx        |        |   Spring Boot    |        |   PostgreSQL     |
|   (80/443)       |<------>|    (8080)        |<------>|     (5432)       |
|  静态资源+反向代理 |        |   REST API       |        |    业务数据       |
+------------------+        +------------------+        +------------------+
         ^
         |
+------------------+
|   用户浏览器      |
+------------------+
```

## 二、环境要求

### 服务器配置（建议）

| 组件 | 最低配置 | 推荐配置 |
|------|---------|---------|
| CPU | 2核 | 4核 |
| 内存 | 4GB | 8GB |
| 硬盘 | 50GB SSD | 100GB SSD |
| 带宽 | 5Mbps | 10Mbps |
| 操作系统 | Ubuntu 20.04+ / CentOS 8+ | Ubuntu 22.04 LTS |

### 软件依赖

| 软件 | 版本要求 | 用途 |
|------|---------|------|
| JDK | 21+ | 后端运行环境 |
| PostgreSQL | 14+ | 关系型数据库 |
| Nginx | 1.20+ | 反向代理/负载均衡 |
| Node.js | 18+ | 前端构建 |
| Maven | 3.8+ | 后端构建 |

## 三、部署步骤

### 步骤1：安装系统依赖

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装常用工具
sudo apt install -y curl wget git vim unzip zip net-tools

# 安装 JDK 21
sudo apt install -y openjdk-21-jdk
java -version

# 安装 Maven
sudo apt install -y maven
mvn -version

# 安装 Node.js 20
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
node -v && npm -v

# 安装 PostgreSQL 14
sudo apt install -y postgresql postgresql-contrib
```

### 步骤2：配置 PostgreSQL 数据库

```bash
# 启动 PostgreSQL
sudo systemctl enable postgresql
sudo systemctl start postgresql

# 创建数据库和用户
sudo -u postgres psql << EOF
CREATE DATABASE campus_media WITH ENCODING = 'UTF8' LC_COLLATE = 'zh_CN.UTF-8' LC_CTYPE = 'zh_CN.UTF-8';
CREATE USER campus_admin WITH PASSWORD 'CampusMedia@2024';
GRANT ALL PRIVILEGES ON DATABASE campus_media TO campus_admin;
ALTER DATABASE campus_media OWNER TO campus_admin;
\c campus_media
-- 启用 UUID 扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOF

# 导入表结构和初始数据
cd /opt/campus-media-system
sudo -u postgres psql -d campus_media -f sql/campus_media.sql

# 配置 PostgreSQL 远程访问（如需外网连接）
sudo sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/14/main/postgresql.conf
echo "host    campus_media    campus_admin    0.0.0.0/0    md5" | sudo tee -a /etc/postgresql/14/main/pg_hba.conf
sudo systemctl restart postgresql
```

### 步骤3：部署后端服务

```bash
# 创建工作目录
sudo mkdir -p /opt/campus-media-system
cd /opt/campus-media-system

# 克隆或上传代码（根据实际情况选择）
# 方式1：从 GitHub 克隆
git clone https://github.com/dofoe/campus-media-system.git .

# 方式2：手动上传代码到 /opt/campus-media-system

# 进入后端目录
cd backend

# 修改数据库配置
sudo tee src/main/resources/application-prod.yml << 'EOF'
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/campus_media
    username: campus_admin
    password: CampusMedia@2024
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false

jwt:
  secret: campus-media-system-jwt-secret-key-change-this-in-production
  expiration: 86400000

logging:
  level:
    root: WARN
    com.campus.media: INFO
  file:
    name: /var/log/campus-media/app.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

# 文件上传配置
upload:
  path: /opt/campus-media-system/uploads
  max-file-size: 100MB
  max-request-size: 100MB
EOF

# 构建项目
mvn clean package -DskipTests -P prod

# 创建启动脚本
sudo tee /opt/campus-media-system/start-backend.sh << 'EOF'
#!/bin/bash
APP_NAME=campus-media-system
JAR_PATH=/opt/campus-media-system/backend/target/campus-media-system-1.0.0.jar
LOG_PATH=/var/log/campus-media

mkdir -p $LOG_PATH

nohup java -jar \
  -Xms512m \
  -Xmx2048m \
  -XX:+UseG1GC \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=$LOG_PATH \
  -Dspring.profiles.active=prod \
  -Dfile.encoding=UTF-8 \
  $JAR_PATH \
  > $LOG_PATH/backend.log 2>&1 &

echo "Backend started, PID: $!"
EOF

sudo chmod +x /opt/campus-media-system/start-backend.sh

# 创建 Systemd 服务
sudo tee /etc/systemd/system/campus-media-backend.service << 'EOF'
[Unit]
Description=Campus Media System Backend
After=network.target postgresql.service
Wants=postgresql.service

[Service]
Type=simple
User=www-data
Group=www-data
WorkingDirectory=/opt/campus-media-system/backend
Environment="JAVA_OPTS=-Xms512m -Xmx2048m -XX:+UseG1GC"
Environment="SPRING_PROFILES_ACTIVE=prod"
ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/campus-media-system/backend/target/campus-media-system-1.0.0.jar
ExecStop=/bin/kill -SIGTERM $MAINPID
Restart=always
RestartSec=5
StandardOutput=append:/var/log/campus-media/backend.log
StandardError=append:/var/log/campus-media/backend.log

[Install]
WantedBy=multi-user.target
EOF

# 创建日志目录并设置权限
sudo mkdir -p /var/log/campus-media
sudo chown -R www-data:www-data /var/log/campus-media
sudo chown -R www-data:www-data /opt/campus-media-system

# 启动后端服务
sudo systemctl daemon-reload
sudo systemctl enable campus-media-backend
sudo systemctl start campus-media-backend

# 查看状态
sudo systemctl status campus-media-backend
```

### 步骤4：部署前端服务

```bash
cd /opt/campus-media-system

# 安装依赖
npm install

# 修改生产环境配置
sudo tee .env.production << 'EOF'
VITE_API_BASE_URL=/api
VITE_APP_TITLE=校园宣传素材智能管理系统
EOF

# 修改 vite.config.js 生产代理（如需）
# 生产环境通常由 Nginx 反向代理，无需修改

# 构建生产包
npm run build

# 确认 dist 目录生成
ls -la dist/
```

### 步骤5：配置 Nginx

```bash
# 安装 Nginx
sudo apt install -y nginx

# 创建 Nginx 配置文件
sudo tee /etc/nginx/sites-available/campus-media << 'EOF'
server {
    listen 80;
    server_name _;  # 修改为实际域名，如 campus-media.example.edu.cn

    # 前端静态资源
    location / {
        root /opt/campus-media-system/dist;
        index index.html;
        try_files $uri $uri/ /index.html;

        # 静态资源缓存
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
            expires 30d;
            add_header Cache-Control "public, immutable";
        }
    }

    # 后端 API 代理
    location /api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 文件上传目录
    location /uploads/ {
        alias /opt/campus-media-system/uploads/;
        expires 30d;
        add_header Cache-Control "public";
    }

    # 安全响应头
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;

    # 禁用服务器版本显示
    server_tokens off;

    # Gzip 压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    gzip_min_length 1000;
}
EOF

# 启用站点
sudo ln -sf /etc/nginx/sites-available/campus-media /etc/nginx/sites-enabled/
sudo rm -f /etc/nginx/sites-enabled/default

# 测试配置
sudo nginx -t

# 重启 Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx
```

### 步骤6：配置 SSL/HTTPS（生产环境必需）

```bash
# 安装 Certbot
sudo apt install -y certbot python3-certbot-nginx

# 申请证书（替换为实际域名）
sudo certbot --nginx -d campus-media.example.edu.cn --non-interactive --agree-tos -m admin@example.edu.cn

# 自动续期测试
sudo certbot renew --dry-run

# 配置定时任务自动续期（通常 certbot 会自动创建）
sudo systemctl enable certbot.timer
```

### 步骤7：防火墙配置

```bash
# 配置 UFW
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw --force enable

# 查看状态
sudo ufw status verbose
```

### 步骤8：系统初始化

```bash
# 创建上传目录
sudo mkdir -p /opt/campus-media-system/uploads
sudo chown -R www-data:www-data /opt/campus-media-system/uploads

# 验证后端运行
sudo systemctl status campus-media-backend

# 验证 Nginx 运行
sudo systemctl status nginx

# 测试 API 接口
curl -X POST http://localhost/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# 预期返回：包含 token 的 JSON 响应
```

## 四、默认账号

| 用户名 | 密码 | 角色 | 数据范围 |
|--------|------|------|---------|
| admin | admin | 超级管理员 | 全部 |

> ⚠️ **安全警告**：首次登录后请立即修改默认密码！

## 五、系统维护

### 查看日志

```bash
# 后端日志
sudo tail -f /var/log/campus-media/backend.log

# Nginx 访问日志
sudo tail -f /var/log/nginx/access.log

# Nginx 错误日志
sudo tail -f /var/log/nginx/error.log

# Systemd 服务日志
sudo journalctl -u campus-media-backend -f
```

### 重启服务

```bash
# 重启后端
sudo systemctl restart campus-media-backend

# 重启 Nginx
sudo systemctl restart nginx

# 重启 PostgreSQL
sudo systemctl restart postgresql
```

### 备份数据

```bash
#!/bin/bash
# /opt/campus-media-system/backup.sh

BACKUP_DIR="/opt/backups/campus-media"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="campus_media"

mkdir -p $BACKUP_DIR

# 备份数据库
pg_dump -U campus_admin -h localhost $DB_NAME | gzip > $BACKUP_DIR/db_${DATE}.sql.gz

# 备份上传文件
tar czf $BACKUP_DIR/uploads_${DATE}.tar.gz -C /opt/campus-media-system uploads/

# 保留最近30天备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +30 -delete

echo "Backup completed: $DATE"
```

添加定时任务：
```bash
crontab -e
# 每天凌晨3点备份
0 3 * * * /opt/campus-media-system/backup.sh >> /var/log/campus-media/backup.log 2>&1
```

### 更新部署

```bash
cd /opt/campus-media-system

# 拉取最新代码
git pull origin main

# 更新后端
cd backend
mvn clean package -DskipTests -P prod
sudo systemctl restart campus-media-backend

# 更新前端
cd ..
npm install
npm run build
sudo systemctl restart nginx
```

## 六、故障排查

### 1. 后端无法启动

```bash
# 检查端口占用
sudo lsof -i :8080

# 检查日志
sudo journalctl -u campus-media-backend --no-pager -n 50

# 检查数据库连接
sudo -u postgres psql -d campus_media -c "SELECT 1"
```

### 2. 前端页面空白

```bash
# 检查 dist 目录
ls -la /opt/campus-media-system/dist/

# 检查 Nginx 配置
sudo nginx -t

# 检查错误日志
sudo tail -f /var/log/nginx/error.log
```

### 3. 数据库连接失败

```bash
# 检查 PostgreSQL 状态
sudo systemctl status postgresql

# 检查用户权限
sudo -u postgres psql -c "\du"

# 检查数据库
sudo -u postgres psql -c "\l"
```

## 七、安全加固建议

1. **修改默认密码**：首次登录后立即修改 admin 密码
2. **JWT 密钥**：生产环境务必修改 `application-prod.yml` 中的 `jwt.secret`
3. **数据库密码**：使用强密码，避免使用默认密码
4. **防火墙**：仅开放必要的端口
5. **定期更新**：及时更新系统补丁和依赖版本
6. **日志审计**：定期审查操作日志和下载日志
7. **HTTPS**：生产环境强制使用 HTTPS

## 八、访问地址

| 环境 | 地址 |
|------|------|
| 开发环境 | http://localhost:5173 |
| 生产环境 | http://your-domain.com 或 https://your-domain.com |
| API 文档 | http://your-domain.com/api (Swagger 可后续集成) |

---

**部署完成时间**: 约 30-60 分钟  
**GitHub 仓库**: https://github.com/dofoe/campus-media-system
