#!/bin/bash
# 校园宣传素材智能管理系统 - Linux/macOS 绿色版启动脚本

echo "========================================"
echo "  校园宣传素材智能管理系统"
echo "  绿色版 - 解压即用"
echo "========================================"
echo ""

# 检查Java
echo "[1/4] 检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到Java环境！"
    echo "请安装 JDK 21: https://adoptium.net/"
    exit 1
fi
echo "   Java环境正常"
echo ""

# 创建目录
echo "[2/4] 初始化目录..."
mkdir -p data logs uploads backend/target
echo "   目录初始化完成"
echo ""

# 检查Maven
echo "[3/4] 检查构建环境..."
if ! command -v mvn &> /dev/null; then
    echo "[警告] 未检测到Maven，如果后端未构建将失败"
fi
echo ""

# 构建检查
echo "[4/4] 检查构建状态..."

JAR_FILE="backend/target/campus-media-system-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "[提示] 后端未构建，正在构建..."
    cd backend
    mvn clean package -DskipTests -q || {
        echo "[错误] 后端构建失败！"
        exit 1
    }
    cd ..
    echo "   后端构建完成"
fi

if [ ! -f "dist/index.html" ]; then
    if command -v npm &> /dev/null; then
        echo "[提示] 前端未构建，正在构建..."
        npm install --silent
        npm run build || echo "[警告] 前端构建失败"
    fi
fi

echo ""
echo "========================================"
echo "  正在启动系统..."
echo "========================================"
echo ""
echo "访问地址: http://localhost:8080"
echo "默认账号: admin / admin"
echo ""

java -Xms256m -Xmx1024m -jar -Dfile.encoding=UTF-8 "$JAR_FILE"