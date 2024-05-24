@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  mirlab-switch startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and MIRLAB_SWITCH_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\mirlab-switch-0.0.2-SNAPSHOT.jar;%APP_HOME%\lib\jgoodies-forms-1.9.0.jar;%APP_HOME%\lib\async-http-client-2.12.3.jar;%APP_HOME%\lib\httpasyncclient-4.1.4.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\jaxb-core-2.3.0.1.jar;%APP_HOME%\lib\jaxb-impl-2.3.1.jar;%APP_HOME%\lib\httpclient-4.5.6.jar;%APP_HOME%\lib\netty-all-4.1.109.Final.jar;%APP_HOME%\lib\json-20231013.jar;%APP_HOME%\lib\openflowj-0.9.7.onos.jar;%APP_HOME%\lib\async-http-client-1.9.40.jar;%APP_HOME%\lib\async-http-client-netty-utils-2.12.3.jar;%APP_HOME%\lib\weblaf-ui-2.2.1.jar;%APP_HOME%\lib\weblaf-core-2.2.1.jar;%APP_HOME%\lib\slf4j-simple-1.7.28.jar;%APP_HOME%\lib\slf4j-api-2.0.9.jar;%APP_HOME%\lib\logback-core-1.5.3.jar;%APP_HOME%\lib\log4j-core-2.20.0.jar;%APP_HOME%\lib\jgoodies-common-1.8.1.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.109.Final.jar;%APP_HOME%\lib\netty-reactive-streams-2.0.4.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.109.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.109.Final-osx-aarch_64.jar;%APP_HOME%\lib\netty-resolver-dns-classes-macos-4.1.109.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.109.Final.jar;%APP_HOME%\lib\netty-handler-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.109.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.109.Final-linux-aarch_64.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.109.Final-linux-riscv64.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.109.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.109.Final-osx-aarch_64.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\jakarta.activation-1.2.2.jar;%APP_HOME%\lib\httpcore-nio-4.4.10.jar;%APP_HOME%\lib\httpcore-4.4.10.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\netty-codec-dns-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-4.1.109.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-haproxy-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-memcache-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-mqtt-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-redis-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-smtp-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-stomp-4.1.109.Final.jar;%APP_HOME%\lib\netty-codec-xml-4.1.109.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.109.Final.jar;%APP_HOME%\lib\netty-common-4.1.109.Final.jar;%APP_HOME%\lib\netty-handler-ssl-ocsp-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-rxtx-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-sctp-4.1.109.Final.jar;%APP_HOME%\lib\netty-transport-udt-4.1.109.Final.jar;%APP_HOME%\lib\netty-3.10.6.Final.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\log4j-api-2.20.0.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\xstream-1.4.11.1.jar;%APP_HOME%\lib\jericho-html-3.4.jar;%APP_HOME%\lib\java-image-scaling-0.8.6.jar;%APP_HOME%\lib\svg-salamander-1.0.jar;%APP_HOME%\lib\filters-2.0.235.jar


@rem Execute mirlab-switch
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %MIRLAB_SWITCH_OPTS%  -classpath "%CLASSPATH%" com.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable MIRLAB_SWITCH_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%MIRLAB_SWITCH_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
