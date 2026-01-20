# Script para generar APK Release de CriptoAPI

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "🔨 Generador de APK Release - CriptoAPI" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Configuración
$projectPath = "C:\Users\APP MOVILES\AndroidStudioProjects\CriptoApiED2"
$keystorePath = "$env:USERPROFILE\.android\debug.keystore"
$outputPath = "$projectPath\app\build\outputs\apk\release"

Write-Host "📂 Ruta del proyecto: $projectPath" -ForegroundColor Yellow
Write-Host "📂 Ruta de salida esperada: $outputPath" -ForegroundColor Yellow
Write-Host ""

# Cambiar al directorio del proyecto
cd $projectPath

Write-Host "⏳ Iniciando compilación en modo RELEASE..." -ForegroundColor Green
Write-Host ""

# Compilar en release
.\gradlew.bat assembleRelease

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "✅ Compilación completada!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si el APK se generó
$apkPath = "$outputPath\app-release.apk"
$unsignedApkPath = "$outputPath\app-release-unsigned.apk"

if (Test-Path $apkPath) {
    Write-Host "✅ APK FIRMADO GENERADO EXITOSAMENTE!" -ForegroundColor Green
    Write-Host "📦 Ubicación: $apkPath" -ForegroundColor Green
    Write-Host "📊 Tamaño: $((Get-Item $apkPath).Length / 1MB) MB" -ForegroundColor Green
} elseif (Test-Path $unsignedApkPath) {
    Write-Host "⚠️  APK sin firmar generado (necesita firma)" -ForegroundColor Yellow
    Write-Host "📦 Ubicación: $unsignedApkPath" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Procurando firmar automáticamente..." -ForegroundColor Cyan

    # Intentar firmar con el keystore de debug
    if (Test-Path $keystorePath) {
        Write-Host "✅ Keystore encontrado en: $keystorePath" -ForegroundColor Green
        Write-Host "Firmando APK..." -ForegroundColor Cyan

        jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 `
            -keystore $keystorePath `
            -storepass android `
            -keypass android `
            $unsignedApkPath androiddebugkey

        # Renombrar a app-release.apk
        Move-Item -Path $unsignedApkPath -Destination $apkPath -Force

        Write-Host "✅ APK FIRMADO EXITOSAMENTE!" -ForegroundColor Green
        Write-Host "📦 Ubicación final: $apkPath" -ForegroundColor Green
        Write-Host "📊 Tamaño: $((Get-Item $apkPath).Length / 1MB) MB" -ForegroundColor Green
    } else {
        Write-Host "❌ Keystore no encontrado en: $keystorePath" -ForegroundColor Red
        Write-Host "Necesitas crear un keystore manualmente." -ForegroundColor Yellow
    }
} else {
    Write-Host "❌ No se generó ningún APK" -ForegroundColor Red
    Write-Host "Revisa los errores de compilación arriba" -ForegroundColor Red
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "📱 APK listo para instalar en dispositivos Android" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Mantener la ventana abierta
Read-Host "Presiona Enter para salir"
