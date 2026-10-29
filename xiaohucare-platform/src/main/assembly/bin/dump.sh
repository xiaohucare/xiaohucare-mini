#!/bin/bash
# =====================================================================
# 诊断信息采集：jstack / jmap / jstat / 系统状态
#
# 旧版用 `ps -f | grep java | grep $DEPLOY_DIR/conf` 找进程，而 start.sh
# 传的是 config/ 目录，参数里根本没有 conf 字样 → 永远匹配不到，
# stop.sh 调它每次都静默失败，OOM 现场无法回溯。
# 现在与 start.sh / stop.sh 统一按 jar 绝对路径识别进程。
# =====================================================================

SERVER_NAME='xiaohucare-platform'
JAR_NAME="${SERVER_NAME}.jar"

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
LOGS_DIR="$DEPLOY_DIR/logs"

have() { command -v "$1" >/dev/null 2>&1; }

app_pids() {
    if have pgrep; then
        pgrep -f "$JAR_PATH" 2>/dev/null
    else
        ps -ef | grep -F "$JAR_PATH" | grep -v grep | awk '{print $2}'
    fi
}

# jstack/jmap 必须与运行进程同版本，否则会 attach 失败：
# 优先用 JAVA_HOME 下的工具，没有则退回 PATH
JAVA_TOOL_DIR=""
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/jstack" ]; then
    JAVA_TOOL_DIR="$JAVA_HOME/bin/"
elif have jstack; then
    JAVA_TOOL_DIR=""
fi

if [ -z "$JAVA_TOOL_DIR" ] && ! have jstack; then
    echo "ERROR: 找不到 jstack，请安装完整 JDK 或设置 JAVA_HOME"
    exit 1
fi

PIDS=$(app_pids)
if [ -z "$PIDS" ]; then
    echo "ERROR: $SERVER_NAME 未在运行，无法采集诊断信息"
    exit 1
fi

DUMP_DIR="$LOGS_DIR/dump"
DATE_DIR="$DUMP_DIR/$(date +%Y%m%d%H%M%S)"
mkdir -p "$DATE_DIR"

echo "Dumping the $SERVER_NAME ..."
for PID in $PIDS; do
    echo "  PID $PID -> $DATE_DIR"
    # 3 秒钟的线程快照比单次 jstack 更容易抓到死锁/卡顿
    "${JAVA_TOOL_DIR}jstack" "$PID" > "$DATE_DIR/jstack-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jstack" -l "$PID" > "$DATE_DIR/jstack-l-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jinfo" "$PID" > "$DATE_DIR/jinfo-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jstat" -gcutil "$PID" 1000 5 > "$DATE_DIR/jstat-gcutil-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jstat" -gccapacity "$PID" > "$DATE_DIR/jstat-gccapacity-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jmap" -heap "$PID" > "$DATE_DIR/jmap-heap-$PID.dump" 2>&1
    "${JAVA_TOOL_DIR}jmap" -histo "$PID" > "$DATE_DIR/jmap-histo-$PID.dump" 2>&1
done

# ---- 系统层：能取到就存一份，取不到就跳过，不再逐个判断奇怪路径 ----
for _cmd in "free -t:free" "uptime:uptime" "vmstat 1 3:vmstat" \
            "ss -ltn:ss" "netstat -an:netstat" "iostat:iostat" "mpstat:mpstat" "sar:sar"; do
    _run="${_cmd%:*}"
    _name="${_cmd##*:}"
    _bin=$(echo "$_run" | awk '{print $1}')
    if have "$_bin"; then
        $_run > "$DATE_DIR/$_name.dump" 2>&1
    fi
done
if have lsof; then
    for PID in $PIDS; do
        lsof -p "$PID" > "$DATE_DIR/lsof-$PID.dump" 2>&1
    done
fi

echo "OK!"
echo "DUMP: $DATE_DIR"
