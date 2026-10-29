# 安全说明（Security Policy）

## 一、报告安全问题

请勿在公开 Issue / PR 中直接贴出可利用的漏洞细节。

- 报告渠道：`security@xiaohucare.example`（请替换为项目实际维护邮箱），或私下联系维护者
- 期望响应：72 小时内确认收到，7 天内给出修复评估
- 报告内容建议：影响版本、复现步骤、漏洞成因、PoC（脱敏后）、修复建议

请在报告中避免包含任何真实个人信息（姓名、身份证号、手机号、住址、医疗记录等）。

## 二、运行环境安全基线

本项目面向养老照护机构，处理长者个人敏感信息，部署时**必须**满足以下基线：

| 项 | 要求 |
| --- | --- |
| JWT 密钥 | `XIAOHUCARE_TOKEN_SECRET` 必须由环境变量注入，长度 ≥ 32 字节；缺失或过短将启动失败 |
| 数据库账号 | 禁止使用 `root`；按库/表最小权限授权；开启 `DB_USE_SSL=true`（跨主机部署时） |
| Redis | 必须设置 `requirepass`，禁止裸端口暴露于公网；`REDIS_KEY_PREFIX` 按环境隔离 |
| Druid 监控 | 生产环境 `DRUID_MONITOR_ENABLED=false`；确需开启时账号口令必须配置，否则启动失败 |
| 接口文档 | 生产环境 `KNIFE4J_ENABLE=false`；开启时同时启用 `KNIFE4J_BASIC_ENABLE` 并配置强口令 |
| 默认口令 | 首次部署后立即修改 `admin` 口令（种子数据口令为 `Admin@123456`），删除演示账号 |
| 文件上传 | `XIAOHUCARE_PROFILE` 目录独立挂载并禁止脚本执行；上传白名单见 `MimeTypeUtils`/`FileTypeUtils` |
| 传输 | 对外入口必须启用 HTTPS，并在反向代理层限制 `/doc.html`、`/druid/**`、`/actuator/**` 来源 |
| 日志 | 禁止打印令牌、密码、身份证号、手机号；`logback` 输出目录不得提交到仓库（`.gitignore` 已拦截 `logs/`） |
| 三方凭据 | 云 AK/SK、小程序密钥、大华 ICC 凭据一律经环境变量/密钥管理服务注入，不入库、不入仓 |

## 三、代码层约束

新增或修改配置项时请遵循：

1. `application*.yml` 中只允许出现 `${ENV_NAME:非敏感默认值}`，敏感项默认值必须为空
2. 敏感项为空时优先采用 **fail-fast**（抛 `IllegalStateException` 阻断启动），而不是内置兜底口令
3. 不得在 `log.info` / `log.debug` 中输出请求体、token、密码、身份证等字段
4. 不得硬编码内网 IP、开发者本机路径（`/Users/...`、`C:\...`）、测试账号口令
5. 涉及个人信息的表字段新增时同步考虑脱敏注解与导出限制

## 四、依赖与漏洞管理

- 版本统一由根 `pom.xml` 的 `<properties>` + `spring-boot-starter-parent` 管理，
  **不再手工覆盖** Spring Framework / Security / Logback / Tomcat 版本，升级 parent 即可获得补丁
- 已知受控组件：`commons-fileupload ≥ 1.6.0`（CVE-2025-48976）、`zxing ≥ 3.5.4`、
  `gson ≥ 2.11.0`、`guava ≥ 33.x`、`fastjson2 ≥ 2.0.66`、`poi ≥ 5.4.1`
- 建议在 CI 中加入 OWASP dependency-check（`mvn org.owasp:dependency-check-maven:check`）
  一类的依赖漏洞扫描任务，发现 HIGH 及以上漏洞时阻断合并

## 五、开源发布前检查清单

发布或对外同步代码前，确认：

- [ ] `sql/` 目录只有 `schema.sql` 与脱敏 `data.sql`，无生产 dump
- [ ] `git status --ignored` 中不存在待提交的 `.env`、`logs/`、`uploadPath/`、`.local-backup/`
- [ ] 全仓搜索无云 AK/SK（`LTAI`）、私钥（`-----BEGIN`）、内网 IP、真实手机号/身份证号
- [ ] `LICENSE`、`NOTICE`、`README.md`、`SECURITY.md` 均随产物分发
- [ ] 历史提交中不含敏感信息（若曾泄露，必须先在对应云平台吊销/轮换密钥，再考虑重写历史）
