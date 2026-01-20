@echo off
REM Script para crear un keystore para firmar APKs

set KEYSTORE_PATH=criptoapied.keystore
set KEYSTORE_ALIAS=criptoapied-key
set KEYSTORE_PASSWORD=CriptoAPI2024!
set KEY_PASSWORD=CriptoAPI2024!
set VALIDITY=36500

REM Crear el keystore
keytool -genkey -v -keystore %KEYSTORE_PATH% ^
  -keyalg RSA -keysize 2048 -validity %VALIDITY% ^
  -alias %KEYSTORE_ALIAS% ^
  -storepass %KEYSTORE_PASSWORD% ^
  -keypass %KEY_PASSWORD% ^
  -dname "CN=CriptoAPI, OU=Development, O=CriptoAPI, L=Mexico, ST=Mexico, C=MX"

echo.
echo ============================================
echo Keystore creado exitosamente!
echo ============================================
echo Ubicacion: %KEYSTORE_PATH%
echo Alias: %KEYSTORE_ALIAS%
echo Contrasena del keystore: %KEYSTORE_PASSWORD%
echo Contrasena de la clave: %KEY_PASSWORD%
echo ============================================
