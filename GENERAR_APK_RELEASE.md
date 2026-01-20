# 📱 Guía para Generar APK Release Firmado

## Opción 1: Generar APK Firmado desde Android Studio (RECOMENDADO)

### Paso 1: Generar Signed APK
1. En Android Studio, ve a: **Build** → **Generate Signed APK...**
2. En la ventana que se abre:
   - Selecciona **APK** (no Bundle)
   - Haz clic en **Create new...**

### Paso 2: Crear Keystore (Solo primera vez)
En el diálogo "Create new Key Store":
- **Key store path**: Selecciona una carpeta segura (ej: `C:\Users\APP MOVILES\AndroidStudioProjects\keystores\criptoapied.keystore`)
- **Key store password**: CriptoAPI2024!
- **Confirm password**: CriptoAPI2024!
- **Key alias**: criptoapied-key
- **Key password**: CriptoAPI2024!
- **Confirm password**: CriptoAPI2024!
- **Validity (years)**: 25
- **Certificate information**:
  - First and Last Name: CriptoAPI
  - Organizational Unit: Development
  - Organization: CriptoAPI
  - City or Locality: Mexico
  - State or Province: Mexico
  - Country Code (XX): MX
- Haz clic en **Create**

### Paso 3: Completar la Firma
1. En la ventana "Generate Signed APK":
   - Selecciona el keystore que acabas de crear
   - Ingresa las contraseñas
   - En **Destination Folder**, selecciona una carpeta de destino
   - En **Build Variants**, selecciona **release**
   - Haz clic en **Finish**

### Paso 4: Esperar Compilación
- Espera a que Android Studio compile el proyecto
- El APK firmado se generará en la carpeta seleccionada
- El archivo se llamará: `app-release.apk`

---

## Opción 2: Desde Terminal (PowerShell)

### Paso 1: Crear Keystore
```powershell
$keystorePath = "C:\Users\APP MOVILES\keystores\criptoapied.keystore"
New-Item -Path "C:\Users\APP MOVILES\keystores" -ItemType Directory -Force

keytool -genkey -v -keystore $keystorePath `
  -keyalg RSA -keysize 2048 -validity 9125 `
  -alias criptoapied-key `
  -storepass CriptoAPI2024! `
  -keypass CriptoAPI2024! `
  -dname "CN=CriptoAPI,OU=Development,O=CriptoAPI,L=Mexico,ST=Mexico,C=MX"
```

### Paso 2: Compilar en Release
```powershell
cd "C:\Users\APP MOVILES\AndroidStudioProjects\CriptoApiED2"
.\gradlew.bat assembleRelease
```

### Paso 3: Firmar el APK
```powershell
$apk = "app\build\outputs\apk\release\app-release-unsigned.apk"
$keystore = "C:\Users\APP MOVILES\keystores\criptoapied.keystore"
$output = "app\build\outputs\apk\release\app-release.apk"

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 `
  -keystore $keystore `
  -storepass CriptoAPI2024! `
  -keypass CriptoAPI2024! `
  $apk criptoapied-key

Move-Item -Path $apk -Destination $output -Force
```

---

## ✅ Archivo Final
El APK firmado se encontrará en:
```
C:\Users\APP MOVILES\AndroidStudioProjects\CriptoApiED2\app\build\outputs\apk\release\app-release.apk
```

Este archivo está listo para:
- ✅ Instalar en dispositivos Android
- ✅ Distribuir en Google Play Store
- ✅ Compartir con usuarios

---

## 📝 Datos de la Firma
- **Keystore**: criptoapied.keystore
- **Alias**: criptoapied-key
- **Contraseña Keystore**: CriptoAPI2024!
- **Contraseña Clave**: CriptoAPI2024!
- **Validez**: 25 años

¡Guarda esta información en un lugar seguro!
