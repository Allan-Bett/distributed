@echo off
echo ================================
echo  FRUIT SERVICE ENGINE - CLIENT
echo          Using JDK 24
echo ================================
echo.
echo Connecting to RMI Server at localhost:1099...
echo Make sure the server is running first!
echo.

cd /d "%~dp0"

REM Use JDK 24 explicitly
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Using Java version:
"%JAVA_HOME%\bin\java" -version
echo.

if exist "target\classes" (
    echo Starting client...
    "%JAVA_HOME%\bin\java" -cp target\classes client.FruitServiceClient
) else (
    echo Error: Classes not found. Please run compile-core.bat first
    pause
) 