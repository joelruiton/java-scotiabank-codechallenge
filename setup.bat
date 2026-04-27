@echo off
echo ============================================
echo  Student Registry Service - Setup Script
echo ============================================
echo.
echo Descargando Gradle Wrapper...
curl -L -o gradle\wrapper\gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.11.1/gradle/wrapper/gradle-wrapper.jar
if %ERRORLEVEL% equ 0 (
    echo Gradle Wrapper descargado correctamente!
    echo.
    echo Levantando la aplicacion...
    gradlew.bat bootRun
) else (
    echo ERROR: No se pudo descargar el wrapper.
    echo Por favor abre el proyecto en IntelliJ IDEA y usa:
    echo   File > Open > selecciona esta carpeta
    echo   IntelliJ descargara automaticamente todo lo necesario.
)
pause
