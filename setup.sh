#!/bin/bash
echo "============================================"
echo " Student Registry Service - Setup Script"
echo "============================================"
echo ""
echo "Descargando Gradle Wrapper..."
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.11.1/gradle/wrapper/gradle-wrapper.jar

if [ $? -eq 0 ]; then
    echo "Gradle Wrapper descargado!"
    echo "Levantando la aplicacion..."
    chmod +x gradlew
    ./gradlew bootRun
else
    echo "ERROR: Sin acceso a internet."
    echo "Abre el proyecto en IntelliJ IDEA:"
    echo "  File > Open > selecciona esta carpeta"
fi
