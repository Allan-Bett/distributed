@echo off
echo ================================
echo  FRUIT SERVICE ENGINE - SERVER
echo          Using JDK 24
echo ================================
echo.
echo Starting RMI Server on port 1099...
echo Press Ctrl+C to stop the server
echo.

cd /d "%~dp0"

REM Use JDK 24 explicitly
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Using Java version:
"%JAVA_HOME%\bin\java" -version
echo.

if exist "target\classes" (
    echo Starting server with compiled classes...
    "%JAVA_HOME%\bin\java" -cp target\classes engine.FruitComputeEngine
) else (
    echo Error: Classes not found. Please run compile-core.bat first
    pause
) 