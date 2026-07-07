#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}============================================"
echo -e "  校园宣传素材智能管理系统 - 一键安装脚本"
echo -e "============================================${NC}"

echo -e "\n${YELLOW}1. 检查系统环境...${NC}"

if ! command -v node &> /dev/null; then
    echo -e "${RED}错误: Node.js 未安装${NC}"
    echo -e "${YELLOW}请先安装 Node.js 18+${NC}"
    exit 1
fi

if ! command -v npm &> /dev/null && ! command -v pnpm &> /dev/null; then
    echo -e "${RED}错误: npm 或 pnpm 未安装${NC}"
    exit 1
fi

NODE_VERSION=$(node --version | cut -d'v' -f2)
echo -e "${GREEN}✓ Node.js 版本: $NODE_VERSION${NC}"

if command -v pnpm &> /dev/null; then
    PACKAGE_MANAGER="pnpm"
else
    PACKAGE_MANAGER="npm"
fi
echo -e "${GREEN}✓ 包管理器: $PACKAGE_MANAGER${NC}"

echo -e "\n${YELLOW}2. 安装项目依赖...${NC}"
cd "$(dirname "$0")/.."

if [ "$PACKAGE_MANAGER" = "pnpm" ]; then
    pnpm install
else
    npm install
fi

echo -e "${GREEN}✓ 依赖安装完成${NC}"

echo -e "\n${YELLOW}3. 构建生产版本...${NC}"
if [ "$PACKAGE_MANAGER" = "pnpm" ]; then
    pnpm run build
else
    npm run build
fi

echo -e "${GREEN}✓ 构建完成${NC}"

echo -e "\n${YELLOW}4. 创建目录结构...${NC}"
mkdir -p /data/campus-media/uploads
mkdir -p /data/campus-media/logs
mkdir -p /data/campus-media/nginx

echo -e "${GREEN}✓ 目录创建完成${NC}"

echo -e "\n${YELLOW}5. 配置 Nginx...${NC}"
cat > /data/campus-media/nginx/campus-media.conf << 'EOF'
server {
    listen 80;
    server_name localhost;

    location / {
        root /data/campus-media/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location /uploads/ {
        alias /data/campus-media/uploads/;
        expires 30d;
    }
}
EOF

echo -e "${GREEN}✓ Nginx 配置完成${NC}"

echo -e "\n${YELLOW}6. 复制构建产物...${NC}"
cp -r dist/* /data/campus-media/dist/ 2>/dev/null || true

echo -e "${GREEN}============================================"
echo -e "  安装完成！"
echo -e "============================================${NC}"

echo -e "\n${YELLOW}部署信息:${NC}"
echo -e "  前端文件: /data/campus-media/dist"
echo -e "  上传目录: /data/campus-media/uploads"
echo -e "  日志目录: /data/campus-media/logs"
echo -e "  Nginx配置: /data/campus-media/nginx/campus-media.conf"

echo -e "\n${YELLOW}启动方式:${NC}"
echo -e "  开发模式: cd $(pwd) && ${PACKAGE_MANAGER} run dev"
echo -e "  生产模式: 配置Nginx指向 /data/campus-media/dist"

echo -e "\n${YELLOW}默认管理员账号:${NC}"
echo -e "  用户名: admin"
echo -e "  密码: admin"

echo -e "\n${GREEN}✓ 安装脚本执行完毕${NC}"
