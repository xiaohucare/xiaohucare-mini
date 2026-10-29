#!/bin/bash
# =====================================================================
# 停止脚本：进程识别口径与 start.sh 保持一致（jar 绝对路径）
# 旧版按 'xiaohucare-sevenstay-platform' 匹配，而实际启动的是
# xiaohucare-platform.jar，永远匹配不到 → 一直报 does not started，
# 连带 restart.sh 完全不可用
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

have() { command -v "$1" >/dev/null 2>&1; }

app_pids() {
    if have pgrep; then
        pgrep -f "$JAR_PATH" 2>/dev/null
    else
        ps -ef | grep -F "$JAR_PATH" | grep -v grep | awk '{print $2}'
    fi
}

PIDS=$(app_pids)
if [ -z "$PIDS" ]; then
    echo "ERROR: $SERVER_NAME 未在运行"
    exit 1
fi

if [ "$1" != "skip" ]; then
    # 用 bash 调，避免归档后丢了执行位就跑不起来（失败也不阻断停止）
    bash "$BIN_DIR/dump.sh" >/dev/null 2>&1 || true
fi

echo "Stopping the $SERVER_NAME ..."
for PID in $PIDS; do
    echo "  kill $PID"
    kill "$PID" >/dev/null 2>&1
done

# 先软杀等 30s，还不退再强杀，避免脚本无限等待
COUNT=0
while [ "$COUNT" -lt 30 ]; do
    STILL=""
    for PID in $PIDS; do
        if kill -0 "$PID" 2>/dev/null; then
            STILL="$STILL $PID"
        fi
    done
    if [ -z "$STILL" ]; then
        break
    fi
    printf '.'
    sleep 1
    COUNT=$((COUNT + 1))
done
printf '\n'

if [ -n "$STILL" ]; then
    echo "WARN: 30s 内未退出，强制 kill -9:$STILL"
    for PID in $STILL; do
        kill -9 "$PID" >/dev/null 2>&1
    done
    sleep 1
fi
if [ -n "$(app_pids)" ]; then
    echo "ERROR: 进程仍未停止，请手动检查：$(echo $(app_pids))"
    exit 1
fi

echo "OK!"
echo "PID: $(echo $PIDS) 已停止"
