# xiaohucare-platform 启动与部署说明

本模块是整个后台服务的可运行入口（`com.xiaohucare.app.MainApplication`）。
仓库级别的介绍、依赖清单、开源合规说明见根目录 [README.md](../README.md)。

## 一、运行环境

- JDK 17+（Spring Boot 3.3 / Spring 6 的最低要求，**不支持 JDK 8**；JDK 21 亦可）
- Maven 3.8+
- MySQL 8.x、Redis 6.x 已就绪，库表通过根目录 `sql/schema.sql` + `sql/data.sql` 初始化

切换 JDK（macOS 示例，请按本机实际路径调整）：

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
java -version
```

## 二、配置注入

`src/main/resources/application*.yml` 与 `src/main/assembly/conf/application*.yml`（打包后落在 `config/`）中
所有密钥、账号、外部地址均写成 `${ENV_NAME:默认值}`，请复制根目录 `.env.example` 为 `.env`
后填写，并在启动前导出：

```bash
cp ../.env.example .env
set -a; source .env; set +a
```

关键项：`XIAOHUCARE_TOKEN_SECRET`（≥ 32 字节，缺失会启动失败）、`DB_USERNAME`、`DB_PASSWORD`、`REDIS_PASSWORD`。

## 三、开发环境启动

```bash
mvn -DskipTests clean install          # 仓库根目录执行
mvn -pl xiaohucare-platform spring-boot:run
```

默认端口 `8187`，上下文路径 `/xiaohucare-api`。

### JDK 17+ 反射开放参数

Spring / MyBatis / 序列化框架在 JDK 17+ 需要开放内部包，否则会抛 `InaccessibleObjectException`：

```
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.io=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.util.concurrent=ALL-UNNAMED
--add-opens java.base/sun.nio.ch=ALL-UNNAMED
--add-opens java.base/java.net=ALL-UNNAMED
--add-opens java.base/java.math=ALL-UNNAMED
```

- `mvn spring-boot:run` 已由 `spring-boot-maven-plugin` 的 `<jvmArguments>` 内置
- IDE 启动：Run/Debug Configuration → VM options 中粘贴上述参数
- 生产脚本：`bin/start.sh` 已内置

## 四、生产打包与启动

```bash
mvn -pl xiaohucare-platform -am -DskipTests clean package
# 产物：target/xiaohucare-platform.zip
```

打包结构（由 `src/main/assembly/assembly.xml` 定义）：

| 目录/文件 | 内容 |
| --- | --- |
| `xiaohucare-platform.jar` | 仅业务类（`com/xiaohucare/**`），含 MANIFEST 的 `Class-Path` |
| `lib/` | 全部依赖 jar |
| `resources/` | 模板、静态资源、IP 库 |
| `config/` | 外置配置（`application.yml`、`application-druid.yml`、`logback.xml` 等） |
| `bin/` | `start.sh` / `stop.sh` / `restart.sh` / `server.sh` / `dump.sh` / `jenkins.sh` / `start.bat` |

部署：

```bash
unzip xiaohucare-platform.zip -d /opt/xiaohucare && cd /opt/xiaohucare/xiaohucare-platform
chmod +x bin/*.sh
./bin/start.sh            # 正常启动
./bin/start.sh debug      # 开启远程调试（默认仅监听 127.0.0.1:8000，可用 JAVA_DEBUG_BIND/JAVA_DEBUG_PORT 调整）
./bin/start.sh jmx        # 开启 JMX（默认仅本地，端口 1099）
./bin/server.sh restart   # 重启
./bin/dump.sh             # 生成堆转储，便于排查 OOM
```

手工命令行启动（不走脚本时）：

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
     --add-opens java.base/java.io=ALL-UNNAMED \
     --add-opens java.base/java.util=ALL-UNNAMED \
     --add-opens java.base/java.util.concurrent=ALL-UNNAMED \
     --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
     --add-opens java.base/java.net=ALL-UNNAMED \
     --add-opens java.base/java.math=ALL-UNNAMED \
     -Dspring.config.additional-location=optional:file:./config/ \
     -Dlogging.config=./config/logback.xml \
     -cp "xiaohucare-platform.jar:lib/*:resources/" \
     com.xiaohucare.app.MainApplication
```

## 五、上线检查清单

- [ ] `KNIFE4J_ENABLE=false`、`DRUID_MONITOR_ENABLED=false`
- [ ] 已修改 `admin` 默认口令，删除演示账号/角色
- [ ] `XIAOHUCARE_PROFILE` 指向独立数据盘目录，目录无执行权限
- [ ] 反向代理仅放行业务路径，`/doc.html`、`/druid/**` 受限
- [ ] 日志目录（`logs/`）与 `xxl-job/` 不纳入版本管理
- [ ] 外部依赖（Redis / MySQL / InfluxDB / 大华 ICC / OSS）凭据经密钥管理服务注入且已轮换

## 六、代码格式

```bash
mvn spotless:apply    # 格式化
mvn spotless:check    # 校验（CI 推荐）
mvn spotless:apply -pl xiaohucare-common   # 单模块
```
