#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}============================================"
echo -e "  校园宣传素材智能管理系统 - 快速部署脚本"
echo -e "============================================${NC}"

echo -e "\n${YELLOW}1. 安装系统依赖...${NC}"

if command -v apt-get &> /dev/null; then
    echo -e "${YELLOW}正在更新软件源...${NC}"
    sudo apt-get update -y
    
    echo -e "${YELLOW}正在安装 Node.js...${NC}"
    curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
    sudo apt-get install -y nodejs
    
    echo -e "${YELLOW}正在安装 Nginx...${NC}"
    sudo apt-get install -y nginx
    
    echo -e "${YELLOW}正在安装 PostgreSQL...${NC}"
    sudo apt-get install -y postgresql postgresql-contrib
    
    echo -e "${YELLOW}正在安装 pnpm...${NC}"
    curl -fsSL https://get.pnpm.io/install.sh | sh -
fi

echo -e "${GREEN}✓ 系统依赖安装完成${NC}"

echo -e "\n${YELLOW}2. 配置数据库...${NC}"

sudo -u postgres psql -c "CREATE DATABASE campus_media;"
sudo -u postgres psql -c "CREATE USER campus_user WITH PASSWORD 'campus_media_2024';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE campus_media TO campus_user;"

echo -e "${GREEN}✓ 数据库配置完成${NC}"

echo -e "\n${YELLOW}3. 导入表结构...${NC}"
cd "$(dirname "$0")/.."
sudo -u postgres psql -d campus_media -f sql/campus_media.sql

echo -e "${GREEN}✓ 表结构导入完成${NC}"

echo -e "\n${YELLOW}4. 配置 Nginx 反向代理...${NC}"
cat > /etc/nginx/sites-available/campus-media << 'EOF'
server {
    listen 80;
    server_name _;

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

sudo ln -sf /etc/nginx/sites-available/campus-media /etc/nginx/sites-enabled/
sudo nginx -t && sudo systemctl reload nginx

echo -e "${GREEN}✓ Nginx 配置完成${NC}"

echo -e "\n${YELLOW}5. 设置文件权限...${NC}"
sudo chown -R www-data:www-data /data/campus-media
sudo chmod -R 755 /data/campus-media

echo -e "${GREEN}✓ 权限设置完成${NC}"

echo -e "\n${GREEN}============================================"
echo -e "  部署完成！"
echo -e "============================================${NC}"

echo -e "\n${YELLOW}访问地址:${NC}"
echo -e "  http://localhost"

echo -e "\n${YELLOW}数据库信息:${NC}"
echo -e "  数据库名: campus_media"
echo -e "  用户名: campus_user"
echo -e "  密码: campus_media_2024"

echo -e "\n${YELLOW}默认管理员账号:${NC}"
echo -e "  用户名: admin"
echo -e "  密码: admin"

echo -e "\n${GREEN}✓ 部署脚本执行完毕${NC}"
