#!/bin/bash
# =====================================================================
# 重启：先停再起
#   ./restart.sh          # 正常重启（停止前会采集一份诊断信息）
#   ./restart.sh fast     # 跳过诊断采集，直接停
# =====================================================================

BIN_DIR=$(cd "$(dirname "$0")" && pwd)

# 进程本来就没起来时 stop.sh 会返回 1，这不是错误，照常往下启动
if [ "$1" = "fast" ] || [ "$1" = "skip" ]; then
    bash "$BIN_DIR/stop.sh" skip || true
else
    bash "$BIN_DIR/stop.sh" || true
fi

exec bash "$BIN_DIR/start.sh"
