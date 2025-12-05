#!/bin/bash

# ===========================================
# Spring Boot 应用启动脚本
# ===========================================

# 应用配置
APP_NAME="@project.artifactId@"
APP_VERSION="@project.version@"
JAR_FILE="$APP_NAME.jar"

# Java 配置
JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk}
JAVA_OPTS="-server -Xms512m -Xmx2048m -XX:MaxMetaspaceSize=512m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/heapdump.hprof"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai"

# 应用配置
APP_OPTS="--spring.config.location=./config/application.yml"
APP_OPTS="$APP_OPTS --logging.config=./config/logback-spring.xml"

# 目录设置
BASE_DIR=$(cd "$(dirname "$0")/.."; pwd)
LIB_DIR="$BASE_DIR/lib"
CONFIG_DIR="$BASE_DIR/config"
LOG_DIR="$BASE_DIR/logs"
PID_FILE="$BASE_DIR/$APP_NAME.pid"

# 创建目录
mkdir -p "$LOG_DIR"
mkdir -p "$CONFIG_DIR"

# 构建 classpath：递归添加 lib 及其子目录下的所有 jar（包含 lib/spring）
CLASSPATH="$BASE_DIR/$JAR_FILE"
if command -v find > /dev/null 2>&1; then
    while IFS= read -r -d '' jar; do
        CLASSPATH="$CLASSPATH:$jar"
    done < <(find "$LIB_DIR" -type f -name '*.jar' -print0)
else
    for jar in "$LIB_DIR"/*.jar "$LIB_DIR"/*/*.jar; do
        [ -f "$jar" ] && CLASSPATH="$CLASSPATH:$jar"
    done
fi

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 函数定义
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查是否在运行
is_running() {
    if [ -f "$PID_FILE" ]; then
        local pid=$(cat "$PID_FILE")
        if ps -p "$pid" > /dev/null 2>&1; then
            return 0
        else
            rm -f "$PID_FILE"
            return 1
        fi
    fi
    return 1
}

# 启动应用
start() {
    if is_running; then
        print_warn "$APP_NAME 已经在运行 (PID: $(cat $PID_FILE))"
        return 0
    fi

    print_info "正在启动 $APP_NAME v$APP_VERSION ..."
    print_info "工作目录: $BASE_DIR"
    print_info "Java 路径: $JAVA_HOME"
    print_info "JVM 参数: $JAVA_OPTS"
    print_info "应用参数: $APP_OPTS"

    cd "$BASE_DIR" || exit 1

    # 启动命令
    nohup "$JAVA_HOME/bin/java" $JAVA_OPTS \
        -cp "$CLASSPATH" \
        -Dloader.path="$LIB_DIR,$LIB_DIR/spring" \
        org.springframework.boot.loader.PropertiesLauncher \
        $APP_OPTS \
        > "$LOG_DIR/console.log" 2>&1 &

    local pid=$!
    echo $pid > "$PID_FILE"

    sleep 2

    if is_running; then
        print_info "$APP_NAME 启动成功! (PID: $pid)"
        print_info "日志文件: $LOG_DIR/console.log"
    else
        print_error "$APP_NAME 启动失败!"
        rm -f "$PID_FILE"
        tail -n 50 "$LOG_DIR/console.log"
        return 1
    fi
}

# 停止应用
stop() {
    if ! is_running; then
        print_warn "$APP_NAME 没有在运行"
        return 0
    fi

    local pid=$(cat "$PID_FILE")
    print_info "正在停止 $APP_NAME (PID: $pid) ..."

    # 先尝试优雅关闭
    kill -15 "$pid" 2>/dev/null

    # 等待最多 30 秒
    local count=0
    while is_running; do
        sleep 1
        count=$((count + 1))
        if [ $count -ge 30 ]; then
            print_warn "优雅关闭超时，强制停止..."
            kill -9 "$pid" 2>/dev/null
            break
        fi
    done

    if is_running; then
        print_error "无法停止 $APP_NAME"
        return 1
    else
        rm -f "$PID_FILE"
        print_info "$APP_NAME 已停止"
    fi
}

# 重启应用
restart() {
    stop
    sleep 2
    start
}

# 查看状态
status() {
    if is_running; then
        local pid=$(cat "$PID_FILE")
        print_info "$APP_NAME 正在运行 (PID: $pid)"
        # 显示内存使用
        if command -v ps > /dev/null; then
            ps -p "$pid" -o pid,ppid,%cpu,%mem,cmd
        fi
        return 0
    else
        print_info "$APP_NAME 已停止"
        return 1
    fi
}

# 查看日志
log() {
    if [ ! -f "$LOG_DIR/console.log" ]; then
        print_error "日志文件不存在: $LOG_DIR/console.log"
        return 1
    fi

    local lines=${1:-50}
    tail -f -n "$lines" "$LOG_DIR/console.log"
}

# 帮助信息
usage() {
    echo "使用方法: $0 {start|stop|restart|status|log|help}"
    echo
    echo "命令说明:"
    echo "  start   启动应用"
    echo "  stop    停止应用"
    echo "  restart 重启应用"
    echo "  status  查看状态"
    echo "  log [n] 查看日志，n为行数（默认50）"
    echo "  help    显示帮助"
    echo
    echo "环境变量:"
    echo "  JAVA_HOME  Java安装路径"
    echo "  JAVA_OPTS  JVM参数"
}

# 主逻辑
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    log)
        log "$2"
        ;;
    help|*)
        usage
        ;;
esac

exit 0