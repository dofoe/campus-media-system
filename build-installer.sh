#!/bin/bash
# ============================================================
#   校园宣传素材智能管理系统 - 一键打包脚本 (Linux/macOS)
# ============================================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info() { echo -e "${CYAN}[INFO]${NC} $1"; }
success() { echo -e "${GREEN}[OK]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

echo "========================================"
echo "  校园宣传素材智能管理系统"
echo "  一键打包脚本"
echo "========================================"
echo ""

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
BACKEND_DIR="$PROJECT_ROOT/backend"
INSTALLER_DIR="$PROJECT_ROOT/installer"

# 检查环境
info "步骤1/5: 检查构建环境..."

command -v node >/dev/null 2>&1 || error "Node.js 未安装"
command -v npm >/dev/null 2>&1 || error "NPM 未安装"
command -v mvn >/dev/null 2>&1 || error "Maven 未安装"
command -v java >/dev/null 2>&1 || error "Java 未安装"

success "环境检查通过"

# 构建前端
info "步骤2/5: 构建前端..."
cd "$PROJECT_ROOT"
npm install
npm run build
success "前端构建完成"

# 构建后端
info "步骤3/5: 构建后端..."
cd "$BACKEND_DIR"
mvn clean package -DskipTests -q
success "后端构建完成"

# 复制JAR包
info "步骤4/5: 准备安装包文件..."
JAR_FILE="$BACKEND_DIR/target/campus-media-system-1.0.0.jar"
if [ -f "$JAR_FILE" ]; then
    cp "$JAR_FILE" "$INSTALLER_DIR/app.jar"
    success "复制JAR包"
else
    error "未找到JAR包"
fi

# 生成安装包
info "步骤5/5: 生成安装包..."

OUTPUT_DIR="$INSTALLER_DIR/Output"
mkdir -p "$OUTPUT_DIR"

# 生成Linux版本
cd "$PROJECT_ROOT"
tar czf "$OUTPUT_DIR/CampusMediaSystem-Linux-1.0.0.tar.gz" \
    --exclude="node_modules" \
    --exclude="dist" \
    --exclude="backend/target" \
    --exclude=".git" \
    .

# 生成可启动的shell脚本
cat > "$OUTPUT_DIR/start.sh" << 'EOF'
#!/bin/bash
# 启动脚本（需要JRE 21+）
DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -d "$DIR/runtime" ]; then
    export JAVA_HOME="$DIR/runtime"
    export PATH="$JAVA_HOME/bin:$PATH"
fi
java -Xms256m -Xmx1024m -jar -Dfile.encoding=UTF-8 "$DIR/app.jar"
EOF
chmod +x "$OUTPUT_DIR/start.sh"

# 创建绿色版
cd "$INSTALLER_DIR"
zip -r "$OUTPUT_DIR/CampusMediaSystem-Green-1.0.0.zip" \
    start.bat stop.bat install-service.bat uninstall-service.bat \
    CampusMediaService.xml README.txt 使用说明.txt app.jar \
    -x "Output/*" 2>/dev/null || true

success "安装包生成完成"
echo ""
echo "========================================"
echo "  打包完成！"
echo "========================================"
echo ""
echo "安装包位置: $OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"
echo ""
