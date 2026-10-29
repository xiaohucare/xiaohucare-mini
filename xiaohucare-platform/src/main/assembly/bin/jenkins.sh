#!/bin/bash
# =====================================================================
# CI/CD 发布钩子示例（Jenkins / 云效 等任意 CI 均可调用）
#
# 约定：CI 在仓库根执行 `mvn -pl xiaohucare-platform -am -DskipTests clean package`
#      产物为 xiaohucare-platform/target/xiaohucare-platform.zip
#
# 可用环境变量：
#   ARTIFACT_DIR   zip 所在目录，默认 ./xiaohucare-platform/target
#   DEPLOY_DIR     部署目录，默认 /opt/xiaohucare
#   BACKUP_KEEP    保留的历史备份数，默认 5
#
# 注意：配置与密钥不随包分发，真实值由部署机环境变量或 DEPLOY_DIR/config
#      下已有的配置文件提供（见仓库根 .env.example）。
# =====================================================================
set -euo pipefail

ARTIFACT_DIR="${ARTIFACT_DIR:-./xiaohucare-platform/target}"
DEPLOY_DIR="${DEPLOY_DIR:-/opt/xiaohucare}"
BACKUP_KEEP="${BACKUP_KEEP:-5}"
ZIP_NAME='xiaohucare-platform.zip'
SRC_ZIP="${ARTIFACT_DIR}/${ZIP_NAME}"

if [ ! -f "$SRC_ZIP" ]; then
    echo "ERROR: 找不到构建产物 $SRC_ZIP（请先执行 mvn package）"
    exit 1
fi

mkdir -p "$DEPLOY_DIR"

# 发布前备份当前版本，便于快速回滚
if [ -d "$DEPLOY_DIR/xiaohucare-platform" ]; then
    STAMP=$(date +%Y%m%d%H%M%S)
    tar -czf "$DEPLOY_DIR/backup-xiaohucare-platform-$STAMP.tar.gz" \
        -C "$DEPLOY_DIR" xiaohucare-platform
    echo "已备份为 backup-xiaohucare-platform-$STAMP.tar.gz"
    # 仅保留最近 N 份备份
    ls -1t "$DEPLOY_DIR"/backup-xiaohucare-platform-*.tar.gz 2>/dev/null | \
        tail -n +$((BACKUP_KEEP + 1)) | xargs -r rm -f
fi

echo "解压 $SRC_ZIP 到 $DEPLOY_DIR"
unzip -oq "$SRC_ZIP" -d "$DEPLOY_DIR"
chmod +x "$DEPLOY_DIR/xiaohucare-platform/bin/"*.sh

# 保留部署机上已有的外置配置：包内 config/ 会覆盖，若需要自定义请改用环境变量注入
cd "$DEPLOY_DIR/xiaohucare-platform/bin"
./restart.sh
