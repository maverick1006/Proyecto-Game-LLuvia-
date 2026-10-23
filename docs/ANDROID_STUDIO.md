# Android Studio

1. Abre la raíz completa con **File > Open**: debe contener `settings.gradle`.
2. En **Tools > SDK Manager**, instala **Android SDK Platform 35**, **Build-Tools 34.0.0** y **Platform-Tools**, aceptando sus licencias.
3. Selecciona el SDK del equipo. Android Studio crea `local.properties` con `sdk.dir`. No publiques ese archivo personal. Su existencia habilita automáticamente el módulo Android.
4. En **Settings > Build, Execution, Deployment > Build Tools > Gradle**, elige **Gradle JDK 17** y el Wrapper. El Java interno del IDE es independiente del JDK de Gradle.
5. Pulsa **Sync Project with Gradle Files**.
6. Selecciona el módulo **android**, un teléfono con depuración USB o un emulador API 24 o superior y pulsa **Run**. Si el teléfono pide autorizar la conexión, hazlo en su pantalla.

`platform-tools-latest-windows.zip` contiene ADB, pero no la plataforma ni las herramientas necesarias para compilar.

## Compilar el APK

Desde la raíz, con JDK 17 y SDK configurado:

```powershell
.\gradlew.bat -Pandroid=true :android:assembleDebug
```

Resultado: `android/build/outputs/apk/debug/android-debug.apk`. La entrega incluye `GameLluvia-Android-debug.apk`, una versión de prueba, no publicada en Google Play.

LibGDX 1.12.1; Android Gradle Plugin 8.7.3; Gradle 8.10.1; compile/target SDK 35; min SDK 24. Pantalla horizontal. Desktop y Android comparten `core` y `assets`.

## Controles y estado real

Android activa `ControlTactil`: mantener o arrastrar el dedo en la zona de juego mueve al personaje hacia ese punto. La franja superior se reserva a botones. En el jefe, tocar la zona de juego también dispara. Versus está diseñado para teclado físico compartido y se valida en Desktop.

El 23/10/2026 se compiló el APK con JDK 17 y SDK 35: **BUILD SUCCESSFUL**. No había dispositivo conectado: gestos, sonido y rendimiento en teléfono siguen pendientes. Compilar con Gradle no certifica la sincronización visual en todas las versiones de Android Studio.

Antes de declarar Android validado, probar en teléfono: campaña, movimiento, agua, pausa, tienda, guardado, jefe, menú y reinicio de la aplicación.

Para trabajar únicamente en Desktop aunque exista SDK configurado: `-Pandroid=false`.
