#!/bin/bash
# =====================================================================
# xiaohucare-platform 启动脚本（JDK 17+，Linux / macOS 通用）
#
# 用法:
#   ./start.sh            # 启动
#   ./start.sh status     # 查看运行状态
#   ./start.sh debug      # 远程调试（8000 端口）
#   ./start.sh jmx        # 开启 JMX（1099 端口）
#
# 常用环境变量:
#   JAVA_HOME / JAVA_HOME_DEFAULT            指定 JDK（默认自动探测 JDK 17）
#   JAVA_HEAP=2g                            堆大小，默认 1024m
#   MAX_WAIT=180                            等待启动就绪的秒数，默认 90
#   JAVA_DEBUG_BIND / JAVA_DEBUG_PORT       debug 模式监听地址与端口（默认 127.0.0.1:8000）
#   JAVA_JMX_PORT                           JMX 端口（默认 1099，仅本地）
# =====================================================================

SERVER_NAME='xiaohucare-platform'
JAR_NAME="${SERVER_NAME}.jar"

# ---- 定位自身：解析软链接、路径含空格也安全（不要直接用 dirname $0）----
PRG="$0"
while [ -h "$PRG" ]; do
    _link=$(ls -ld "$PRG" | sed 's/.*-> //')
    case "$_link" in
        /*) PRG="$_link" ;;
        *)  PRG="$(dirname "$PRG")/$_link" ;;
    esac
done
BIN_DIR=$(cd "$(dirname "$PRG")" && pwd)
DEPLOY_DIR=$(cd "$BIN_DIR/.." && pwd)
JAR_PATH="$DEPLOY_DIR/$JAR_NAME"

# ---- 配置目录：assembly 打包到 config/，早期部署可能叫 conf/，两个都认 ----
CONF_DIR=""
for _d in "$DEPLOY_DIR/conf" "$DEPLOY_DIR/config"; do
    if [ -f "$_d/application.yml" ]; then
        CONF_DIR="$_d"
        break
    fi
done
if [ -z "$CONF_DIR" ]; then
    echo "ERROR: $DEPLOY_DIR 下既没有 conf/ 也没有 config/application.yml"
    ls -la "$DEPLOY_DIR"
    exit 1
fi
if [ ! -f "$JAR_PATH" ]; then
    echo "ERROR: 找不到启动包 $JAR_PATH"
    exit 1
fi

have() { command -v "$1" >/dev/null 2>&1; }

# 进程识别统一用 jar 绝对路径：它出现在 -cp 参数里，start/stop/dump 口径一致
app_pids() {
    if have pgrep; then
        pgrep -f "$JAR_PATH" 2>/dev/null
    else
        ps -ef | grep -F "$JAR_PATH" | grep -v grep | awk '{print $2}'
    fi
}

# 端口是否在监听。返回 0=在监听 1=没监听 2=系统没有 ss/netstat 无法判断
# 只比对 "地址:端口" 的结尾，避免 8187 匹配到 18187 / 81870
port_listening() {
    _port="$1"
    if have ss; then
        ss -ltn 2>/dev/null | awk '{print $4}' | grep -qE "[:.]${_port}$"
    elif have netstat; then
        netstat -ltn 2>/dev/null | awk '{print $4}' | grep -qE "[:.]${_port}$"
    else
        return 2
    fi
}

# 只取顶层 server: 块里的第一个 port:，去掉引号和 CRLF 残留的 \r，非纯数字视为未配置
# （application.yml 是 Windows 换行，$2 尾部会带 \r，不清理会把 8187 判成非法）
SERVER_PORT=$(awk '/^server:/{flag=1; next} flag && /^[[:space:]]+port:/{gsub(/["\047\r]/, "", $2); print $2; exit}' \
    "$CONF_DIR/application.yml" 2>/dev/null)
case "$SERVER_PORT" in
    ''|*[!0-9]*) SERVER_PORT="" ;;
esac

PIDS=$(app_pids)

if [ "$1" = "status" ]; then
    if [ -n "$PIDS" ]; then
        echo "The $SERVER_NAME is running."
        echo "PID: $(echo $PIDS)"
        if [ -n "$SERVER_PORT" ] && port_listening "$SERVER_PORT"; then
            echo "PORT: $SERVER_PORT (listening)"
        fi
        exit 0
    fi
    echo "The $SERVER_NAME is stopped"
    exit 0
fi

if [ -n "$PIDS" ]; then
    echo "ERROR: $SERVER_NAME 已在运行，PID: $(echo $PIDS)"
    exit 1
fi

if [ -n "$SERVER_PORT" ]; then
    port_listening "$SERVER_PORT"
    _port_rc=$?
    if [ "$_port_rc" -eq 0 ]; then
        echo "ERROR: 端口 $SERVER_PORT 已被占用，无法启动"
        exit 1
    elif [ "$_port_rc" -eq 2 ]; then
        echo "WARN: 系统没有 ss 也没有 netstat，跳过端口占用检查"
    fi
fi

# =====================================================================
# JDK 探测（支持 JDK 17+）
# 优先级：环境变量 JAVA_HOME > JAVA_HOME_DEFAULT（可自行指定服务器路径）
#         > macOS java_home > 各发行版常见 JDK 17 路径 > 报错
# =====================================================================
JAVA_HOME_DEFAULT="${JAVA_HOME_DEFAULT:-/www/server/java/jdk-17.0.8}"

if [ -z "$JAVA_HOME" ] && [ -x "$JAVA_HOME_DEFAULT/bin/java" ]; then
    JAVA_HOME="$JAVA_HOME_DEFAULT"
fi
# macOS 本机调试用（Linux 上这个文件不存在，自动跳过）
if [ -z "$JAVA_HOME" ] && [ -x /usr/libexec/java_home ]; then
    JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null || /usr/libexec/java_home 2>/dev/null)
fi
if [ -z "$JAVA_HOME" ]; then
    for _cand in \
        /www/server/java/jdk-17* \
        /usr/lib/jvm/java-17* \
        /usr/lib/jvm/jre-17* \
        /usr/lib/jvm/jdk-17* \
        /opt/java/jdk-17* \
        /usr/local/java/jdk-17*; do
        if [ -x "$_cand/bin/java" ]; then
            JAVA_HOME="$_cand"
            break
        fi
    done
fi
if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/java" ]; then
    echo "ERROR: 未找到可用的 JDK 17+"
    echo "  已知尝试：\$JAVA_HOME、$JAVA_HOME_DEFAULT、/www/server/java/jdk-17*、/usr/lib/jvm/java-17*"
    echo "  请显式指定：JAVA_HOME=/path/to/jdk-17 ./start.sh"
    exit 1
fi
JAVA_BIN="$JAVA_HOME/bin/java"

# 校验版本 >= 17（兼容 JDK 8 的 1.8.x 输出格式）
JAVA_VERSION_RAW=$("$JAVA_BIN" -version 2>&1 | head -1 | sed -n 's/.*version "\([^"]*\)".*/\1/p')
JAVA_MAJOR=$(printf '%s' "$JAVA_VERSION_RAW" | awk -F. '{print ($1 == 1 ? $2 : $1)}')
case "$JAVA_MAJOR" in
    ''|*[!0-9]*)
        echo "ERROR: 无法识别 JDK 版本（输出：${JAVA_VERSION_RAW:-空}），请确认 $JAVA_BIN 可正常执行"
        exit 1
        ;;
esac
if [ "$JAVA_MAJOR" -lt 17 ]; then
    echo "ERROR: 当前 JDK 版本为 $JAVA_VERSION_RAW，项目要求 JDK 17+"
    exit 1
fi

echo "JAVA_HOME   : $JAVA_HOME"
echo "Java version: $JAVA_VERSION_RAW"
echo "DEPLOY_DIR  : $DEPLOY_DIR"
echo "CONF_DIR    : $CONF_DIR"
echo "SERVER_PORT : ${SERVER_PORT:-未配置}"

LOGS_DIR="$DEPLOY_DIR/logs"
if [ ! -d "$LOGS_DIR" ]; then
    mkdir -p "$LOGS_DIR"
fi
STDOUT_FILE="$LOGS_DIR/stdout.log"
timestamp=$(date +%Y%m%d-%H%M%S)
BACK_STDOUT_FILE="$LOGS_DIR/z_${timestamp}_stdout.log"
if [ -f "$STDOUT_FILE" ]; then
    echo "备份日志 $BACK_STDOUT_FILE ..."
    cp "$STDOUT_FILE" "$BACK_STDOUT_FILE"
fi

# ---- JVM 参数 ----
JAVA_HEAP=${JAVA_HEAP:-1024m}
# 不写 -Xmn：JDK 17 默认 G1，固定新生代大小会破坏它的分区自适应
JAVA_MEM_OPTS=" -server -Xms$JAVA_HEAP -Xmx$JAVA_HEAP -Xss1m -XX:MaxMetaspaceSize=${JAVA_METASPACE:-512m} -XX:+DisableExplicitGC "
# OOM 时自动 dump 堆，便于事后排查
JAVA_EXT_OPTS=" -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOGS_DIR "
JAVA_ENV_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai "
# JDK 17 模块系统：放开 MyBatis / Redisson / Druid 等框架用到的反射
JAVA_ADD_OPENS=" --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED "
# 配置目录按“目录”交给 Spring（additional-location + optional）：
#   1) 目录形式才会连带加载 application-druid.yml 等 profile 文件，指定单个文件不会；
#   2) additional 保留 jar 内 classpath 的默认配置，resources/ 里的 application-oss.yml 才不会丢；
#   3) optional 让配置文件缺失时给出明确报错而不是直接崩。
# logging.config 必须显式指定：否则 logback 会先去 classpath 拿 resources/logback.xml，
# config/logback.xml 这套生产配置永远不会被读到。
JAVA_CONF_OPTS=" -Dlog.path=$LOGS_DIR -Dspring.config.additional-location=optional:file:$CONF_DIR/ -Dlogging.config=$CONF_DIR/logback.xml "

JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    # 安全默认：JDWP 只绑定回环地址，避免任意人通过 8000 端口执行任意代码。
    # 确实需要远程调试时（不推荐，用完请关闭）：
    #   JAVA_DEBUG_BIND=0.0.0.0 JAVA_DEBUG_PORT=8000 ./start.sh debug
    # 并通过防火墙/SSH 隧道限制来源。
    JAVA_DEBUG_BIND="${JAVA_DEBUG_BIND:-127.0.0.1}"
    JAVA_DEBUG_PORT="${JAVA_DEBUG_PORT:-8000}"
    JAVA_DEBUG_OPTS=" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${JAVA_DEBUG_BIND}:${JAVA_DEBUG_PORT} "
    if [ "$JAVA_DEBUG_BIND" != "127.0.0.1" ] && [ "$JAVA_DEBUG_BIND" != "localhost" ]; then
        echo "WARN: JDWP 监听在 $JAVA_DEBUG_BIND:$JAVA_DEBUG_PORT（非回环地址），该端口可执行任意代码，请务必限制访问来源"
    fi
fi

JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    # JMX 默认仅本地连接；需要远程时请同时开启口令认证：
    #   JMX_PASSWORD_FILE=/path/jmx.password JMX_LOGIN_FILE=/path/jmx.access ./start.sh jmx
    JAVA_JMX_PORT="${JAVA_JMX_PORT:-1099}"
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=${JAVA_JMX_PORT} -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=true "
    if [ -n "$JMX_PASSWORD_FILE" ] && [ -n "$JMX_LOGIN_FILE" ]; then
        JAVA_JMX_OPTS="${JAVA_JMX_OPTS} -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=${JMX_PASSWORD_FILE} -Dcom.sun.management.jmxremote.access.file=${JMX_LOGIN_FILE} -Dcom.sun.management.jmxremote.local.only=false "
    fi
fi

echo "Starting the $SERVER_NAME ..."
# logback.xml 里 <property name="log.path" value="./logs"/> 是相对路径，
# 而且 logback 的 local 作用域优先于 -Dlog.path，光传系统属性没用，
# 必须先切到部署根目录，日志才会稳定落在 $DEPLOY_DIR/logs
cd "$DEPLOY_DIR" || { echo "ERROR: 无法进入 $DEPLOY_DIR"; exit 1; }
nohup "$JAVA_BIN" $JAVA_MEM_OPTS $JAVA_EXT_OPTS $JAVA_ENV_OPTS $JAVA_ADD_OPENS $JAVA_CONF_OPTS \
    $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS \
    -cp "$JAR_PATH:$DEPLOY_DIR/lib/*:$DEPLOY_DIR/resources/" com.xiaohucare.app.MainApplication \
    > "$STDOUT_FILE" 2>&1 &
APP_PID=$!

# ---- 等待就绪：有端口就等端口监听，没有就退化为等进程；超时/进程退出都明确报错 ----
MAX_WAIT=${MAX_WAIT:-90}
WAIT=0
READY=0
printf 'Waiting'
while [ "$WAIT" -lt "$MAX_WAIT" ]; do
    if [ -n "$SERVER_PORT" ]; then
        if port_listening "$SERVER_PORT"; then
            READY=1
            break
        fi
    elif [ "$WAIT" -ge 5 ]; then
        # 没解析到端口时不做端口校验，进程存活即可认为起来了
        READY=1
        break
    fi
    if ! kill -0 "$APP_PID" 2>/dev/null; then
        printf '\n'
        echo "ERROR: 进程已退出，启动失败。最近日志："
        tail -n 60 "$STDOUT_FILE"
        exit 1
    fi
    printf '.'
    sleep 1
    WAIT=$((WAIT + 1))
done

printf '\n'
if [ "$READY" -ne 1 ]; then
    echo "ERROR: 等待 ${MAX_WAIT}s 仍未就绪（端口 ${SERVER_PORT:-未配置}）"
    echo "最近日志："
    tail -n 60 "$STDOUT_FILE"
    exit 1
fi

echo "OK!"
echo "PID: $(echo $(app_pids))"
echo "STDOUT: $STDOUT_FILE"
