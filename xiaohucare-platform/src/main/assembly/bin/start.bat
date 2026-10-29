@echo off
rem =====================================================================
rem xiaohucare-platform Windows 启动脚本（JDK 17+）
rem 目录约定与 assembly.xml 一致：
rem   .\xiaohucare-platform.jar   业务瘦包
rem   .\lib\*                     依赖 jar
rem   .\resources\                模板 / 静态资源 / IP 库
rem   .\config\                   外置配置（application.yml 等）
rem 用法：
rem   start.bat            正常启动
rem   start.bat debug      开启 JDWP（默认仅监听 127.0.0.1:8000）
rem   start.bat jmx        开启 JMX（默认仅本地，端口 9888）
rem =====================================================================

set SERVER_NAME=xiaohucare-platform
set MAIN_CLASS=com.xiaohucare.app.MainApplication

rem 切到部署根目录（bin 的上一级）
cd /d "%~dp0.."

set JAVA_MEM_OPTS= -Xms512m -Xmx1024m -Xss1m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=.\logs
set JAVA_ENV_OPTS= -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Djava.awt.headless=true
set JAVA_ADD_OPENS= --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.math=ALL-UNNAMED
set JAVA_CONF_OPTS= -Dlog.path=.\logs -Dspring.config.additional-location=optional:file:.\config\ -Dlogging.config=.\config\logback.xml
set JAVA_CP= %SERVER_NAME%.jar;lib\*;resources\

set DEBUG_OPTS=
if ""%1"" == ""debug"" (
    if "%JAVA_DEBUG_BIND%"=="" set JAVA_DEBUG_BIND=127.0.0.1
    if "%JAVA_DEBUG_PORT%"=="" set JAVA_DEBUG_PORT=8000
    set DEBUG_OPTS= -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=%JAVA_DEBUG_BIND%:%JAVA_DEBUG_PORT%
    echo [WARN] JDWP 端口 %JAVA_DEBUG_BIND%:%JAVA_DEBUG_PORT% 可执行任意代码，请勿暴露到公网
    goto run
)

set JMX_OPTS=
if ""%1"" == ""jmx"" (
    if "%JAVA_JMX_PORT%"=="" set JAVA_JMX_PORT=9888
    set JMX_OPTS= -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JAVA_JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=true
    goto run
)

echo "Starting the %SERVER_NAME% ..."
:run
java %JAVA_MEM_OPTS% %JAVA_ENV_OPTS% %JAVA_ADD_OPENS% %JAVA_CONF_OPTS% %DEBUG_OPTS% %JMX_OPTS% -cp "%JAVA_CP%" %MAIN_CLASS%
