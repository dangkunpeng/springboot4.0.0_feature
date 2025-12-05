@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ===========================================
REM Spring Boot 应用启动脚本 (Windows)
REM ===========================================

REM 应用配置
set APP_NAME=@project.artifactId@
set APP_VERSION=@project.version@
set JAR_FILE=%APP_NAME%.jar

REM Java 配置

set JAVA_OPTS=-server -Xms512m -Xmx2048m -XX:MaxMetaspaceSize=512m
set JAVA_OPTS=%JAVA_OPTS% -XX:+UseG1GC -XX:MaxGCPauseMillis=200
set JAVA_OPTS=%JAVA_OPTS% -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=.\logs\heapdump.hprof
set JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai

REM 应用配置
set APP_OPTS=--spring.config.location=.\config\application.yml
set APP_OPTS=%APP_OPTS% --logging.config=.\config\logback-spring.xml

REM 目录设置
set BASE_DIR=%~dp0..
cd /d "%BASE_DIR%"
set LIB_DIR=.\lib
set CONFIG_DIR=.\config
set LOG_DIR=.\logs
set PID_FILE=%APP_NAME%.pid

REM 创建目录
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"
if not exist "%CONFIG_DIR%" mkdir "%CONFIG_DIR%"

REM 构建 classpath
set CLASSPATH=%JAR_FILE%
REM 递归添加 lib 及其子目录下的所有 jar 到 classpath（包含 lib/spring）
for /R "%LIB_DIR%" %%i in ("*.jar") do (
    set CLASSPATH=!CLASSPATH!;%%i
)

:MAIN
if "%1"=="" goto HELP

if "%1"=="start" goto START
if "%1"=="stop" goto STOP
if "%1"=="restart" goto RESTART
if "%1"=="status" goto STATUS
if "%1"=="log" goto LOG
goto HELP

:START
    echo [INFO] 正在启动 %APP_NAME% v%APP_VERSION% ...

    REM 检查是否已运行
    if exist "%PID_FILE%" (
        for /f "tokens=1" %%p in (%PID_FILE%) do (
            tasklist /FI "PID eq %%p" 2>nul | findstr /c:"%%p" >nul
            if !errorlevel! equ 0 (
                echo [WARN] %APP_NAME% 已经在运行 (PID: %%p)
                exit /b 0
            )
            REM 如果上面的 PID 不在运行，则删除 PID 文件
            del "%PID_FILE%"
        )
    )

    echo [INFO] 工作目录: %CD%
    echo [INFO] Java 路径: %JAVA_HOME%
    echo [INFO] JVM 参数: %JAVA_OPTS%
    echo [INFO] 应用参数: %APP_OPTS%

    REM 启动应用
    start /b "" "%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% ^
        -cp "%CLASSPATH%" ^
        -Dloader.path="%LIB_DIR%;%LIB_DIR%\spring" ^
        org.springframework.boot.loader.PropertiesLauncher ^
        %APP_OPTS% ^
        > "%LOG_DIR%\console.log" 2>&1

    echo !time! > "%PID_FILE%"
    timeout /t 2 /nobreak >nul

    echo [INFO] %APP_NAME% 启动成功!
    echo [INFO] 日志文件: %LOG_DIR%\console.log
    goto END

:STOP
    echo [INFO] 正在停止 %APP_NAME% ...

    if not exist "%PID_FILE%" (
        echo [WARN] %APP_NAME% 没有在运行
        exit /b 0
    )

    for /f "tokens=1" %%p in (%PID_FILE%) do (
        echo [INFO] 停止进程: %%p
        taskkill /F /PID %%p >nul 2>&1
    )

    del "%PID_FILE%"
    echo [INFO] %APP_NAME% 已停止
    goto END

:RESTART
    call %0 stop
    timeout /t 2 /nobreak >nul
    call %0 start
    goto END

:STATUS
    if exist "%PID_FILE%" (
        for /f "tokens=1" %%p in (%PID_FILE%) do (
            tasklist /FI "PID eq %%p" 2>nul | findstr /c:"%%p" >nul
            if !errorlevel! equ 0 (
                echo [INFO] %APP_NAME% 正在运行 (PID: %%p)
            )
            if !errorlevel! neq 0 (
                echo [INFO] %APP_NAME% 已停止
                del "%PID_FILE%"
            )
        )
    ) else (
        echo [INFO] %APP_NAME% 已停止
    )
    goto END

:LOG
    if not exist "%LOG_DIR%\console.log" (
        echo [ERROR] 日志文件不存在: %LOG_DIR%\console.log
        exit /b 1
    )

    set LINES=50
    if not "%2"=="" set LINES=%2
    tail -n %LINES% "%LOG_DIR%\console.log"
    goto END

:HELP
    echo 使用方法: %0 {start^|stop^|restart^|status^|log^|help}
    echo.
    echo 命令说明:
    echo   start    启动应用
    echo   stop     停止应用
    echo   restart  重启应用
    echo   status   查看状态
    echo   log [n]  查看日志，n为行数（默认50）
    echo   help     显示帮助
    echo.
    echo 环境变量:
    echo   JAVA_HOME  Java安装路径
    echo   JAVA_OPTS  JVM参数

:END
endlocal