@echo off
echo ================================
echo  COMPILING CORE RMI SYSTEM
echo ================================
echo.

REM Create target directories
if not exist "target" mkdir target
if not exist "target\classes" mkdir target\classes

echo Compiling core Java files (without servlet)...
echo.

REM Compile all Java files except the servlet
javac -d target\classes src\interfaces\*.java src\model\*.java src\data\*.java src\engine\*.java src\client\*.java src\tasks\*.java

if %errorlevel% equ 0 (
    echo.
    echo ================================
    echo  CORE COMPILATION SUCCESSFUL!
    echo ================================
    echo.
    echo All 7 required classes compiled:
    echo ✅ FruitComputeEngine
    echo ✅ FruitComputeTaskRegistry  
    echo ✅ AddFruitPrice
    echo ✅ UpdateFruitPrice
    echo ✅ DeleteFruitPrice
    echo ✅ CalFruitCost
    echo ✅ CalculateCost
    echo.
    echo Ready to run:
    echo 1. Server: java -cp target\classes engine.FruitComputeEngine
    echo 2. Client: java -cp target\classes client.FruitServiceClient
) else (
    echo.
    echo ================================
    echo  COMPILATION FAILED!
    echo ================================
    echo Please check the error messages above.
)

echo.
pause 