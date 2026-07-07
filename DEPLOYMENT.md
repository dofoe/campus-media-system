# 校园宣传素材智能管理系统 - 部署说明

## 系统概述

校园宣传素材智能管理系统是一套基于 Vue3 + Element Plus 的前端管理系统，用于校园宣传素材的上传、分类、检索和管理。

## 技术栈

- **前端框架**: Vue 3 + Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **构建工具**: Vite 6

## 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0 或 pnpm >= 8.0.0
- PostgreSQL >= 14.0（后端数据库）
- Nginx（生产环境反向代理）

## 目录结构

```
campus-media-system/
├── src/                 # 前端源码
│   ├── api/            # API接口封装
│   ├── router/         # 路由配置
│   ├── store/          # 状态管理
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   └── layout/         # 布局组件
├── sql/                # 数据库表结构
│   └── campus_media.sql
├── scripts/            # 部署脚本
│   ├── install.sh      # Linux/Mac安装脚本
│   ├── install.bat     # Windows安装脚本
│   └── deploy.sh       # Ubuntu快速部署脚本
├── dist/               # 构建产物
├── package.json
├── vite.config.js
└── DEPLOYMENT.md
```

## 一键安装

### Linux / Mac

```bash
cd campus-media-system
chmod +x scripts/install.sh
./scripts/install.sh
```

### Windows

```cmd
cd campus-media-system
scripts\install.bat
```

## 快速部署（Ubuntu Server）

```bash
cd campus-media-system
chmod +x scripts/deploy.sh
sudo ./scripts/deploy.sh
```

## 手动安装步骤

### 1. 安装依赖

```bash
pnpm install
# 或
npm install
```

### 2. 构建生产版本

```bash
pnpm run build
# 或
npm run build
```

### 3. 配置数据库

```sql
-- 创建数据库
CREATE DATABASE campus_media;
CREATE USER campus_user WITH PASSWORD 'campus_media_2024';
GRANT ALL PRIVILEGES ON DATABASE campus_media TO campus_user;

-- 导入表结构
psql -d campus_media -f sql/campus_media.sql
```

### 4. 配置 Nginx

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /data/campus-media/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /uploads/ {
        alias /data/campus-media/uploads/;
        expires 30d;
    }
}
```

## 启动方式

### 开发模式

```bash
pnpm run dev
# 或
npm run dev
```

访问: http://localhost:5173

### 生产模式

配置 Nginx 指向 `dist` 目录即可。

## 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin | 超级管理员 |

## 数据库表结构

系统包含以下数据表：

| 表名 | 说明 |
|------|------|
| departments | 部门信息 |
| roles | 角色信息 |
| users | 用户信息 |
| categories | 素材分类 |
| tags | 标签信息 |
| media | 素材信息 |
| media_tags | 素材标签关联 |
| download_logs | 下载日志 |
| operation_logs | 操作日志 |
| audit_rules | 审核规则 |

## 配置说明

### API 代理配置

开发环境下，API 请求通过 `vite.config.js` 中的 proxy 配置转发到后端：

```javascript
proxy: {
    '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
    }
}
```

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| VITE_API_BASE_URL | API基础地址 | /api |
| VITE_APP_TITLE | 应用标题 | 校园宣传素材智能管理系统 |

## 常见问题

### 1. 构建失败

确保 Node.js 版本 >= 18，并且已正确安装依赖。

### 2. 页面无法访问

检查 Nginx 配置是否正确，确保 `dist` 目录有正确的文件权限。

### 3. 数据库连接失败

检查 PostgreSQL 服务是否启动，确认数据库用户名和密码正确。

## 许可证

MIT License
