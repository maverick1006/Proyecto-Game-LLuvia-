# GameLluvia — Guardián de la lluvia

Primera versión jugable basada en GameLluvia2024, entregado por el profesor. Proyecto universitario Java + LibGDX para tres integrantes. Entrega: 23 de octubre de 2026.

Recoge agua para un pueblo durante una tormenta. Las gotas celestes suman 10 puntos; las rosas con X quitan una vida. Hay tres vidas y tres niveles con metas acumuladas de 100, 220 y 360 puntos. Las gotas que llegan al suelo desaparecen sin penalización. Tras un golpe tienes 1,2 segundos de protección. Al terminar se guarda el récord local.

## Ejecutar en Windows

Necesitas un JDK 17 y acceso a Internet la primera vez. Desde la raíz del proyecto:

```powershell
.\gradlew.bat :lwjgl3:run
```

Si Java no se encuentra, configura `JAVA_HOME` con la carpeta de tu JDK 17. No copies rutas de otros compañeros.

```powershell
.\gradlew.bat :core:test
.\gradlew.bat :lwjgl3:installDist
```

La distribución queda en `lwjgl3/build/install/lwjgl3`. Se comparte la carpeta completa y se inicia con `bin/lwjgl3.bat`; necesita Java instalado. No es un ejecutable autónomo.

## Controles

| Acción | Control |
|---|---|
| Mover el tarro | Flechas o A/D |
| Iniciar / continuar | Enter o botón |
| Pausar | P, Escape o botón |
| Activar / desactivar sonido | M o botón superior |
| Cambiar control en el menú | T o botón de control |
| Control táctil / ratón | Arrastrar en la zona de juego |

El teclado es predeterminado en escritorio; el táctil, en Android. La lluvia se congela al pausar. Salir al menú abandona la partida; volver a jugar restablece vidas, puntos y nivel.

## Android

El módulo existe, pero su compilación y ejecución en dispositivo están pendientes. Se conserva LibGDX 1.12.1 de la base del profesor. Esta entrega no está validada para publicación en tiendas ni para dispositivos con requisitos especiales de bibliotecas nativas.

Instala Android SDK Platform 35 con Android Studio y configura `local.properties` con tu `sdk.dir`. Para habilitar el módulo al sincronizar, agrega `android=true` a tu configuración local de Gradle, o usa:

```powershell
.\gradlew.bat -Pandroid=true :android:assembleDebug
```

El APK esperado queda en `android/build/outputs/apk/debug/android-debug.apk`. Se usa AGP 8.7.3 y JDK 17. El módulo es opcional para que quien trabaje solamente en escritorio no necesite el SDK.

## IDEs

Usen el mismo Gradle Wrapper, JDK y código compartido; no suban carpetas de caché ni configuraciones personales.

- Android Studio: abrir la carpeta raíz como proyecto Gradle; habilitar el módulo Android según lo indicado arriba.
- Eclipse: importar como proyecto Gradle existente con Buildship.
- VS Code: abrir la raíz con soporte de Java y Gradle; ejecutar los comandos de arriba desde la terminal.
- NetBeans: abrir como proyecto Gradle y ejecutar las mismas tareas. La integración exacta depende de la versión instalada; todavía no se ha probado este IDE.

## Organización

- `core`: reglas y vista compartidas.
- `lwjgl3`: inicio de escritorio y prueba visual.
- `android`: inicio de Android y configuración de empaquetado.
- `assets`: recursos heredados del profesor; se reutilizan sus sonidos. Verificar permisos antes de distribuirlos públicamente.
- `docs/REPORTE.md`: explicación y evidencias de la pauta.
- `docs/PLAN_EQUIPO.md`: propuesta de reparto y calendario.

La vista se dibuja con formas de LibGDX, sin descargar ilustraciones. `GameLluvia` concentra por ahora la presentación y coordinación: se puede dividir en pantallas cuando los integrantes comprendan este primer avance.

## Verificación visual

```powershell
.\gradlew.bat :lwjgl3:smoke
```

Abre una ventana durante unos segundos y guarda capturas reales de menú, partida, pausa y redimensionado en `docs/capturas`. No sustituye una partida manual completa ni las pruebas en Android.

Referencias: [importación de proyectos LibGDX](https://libgdx.com/wiki/start/import-and-running), [entorno de desarrollo](https://libgdx.com/wiki/start/setup), [compatibilidad de AGP](https://developer.android.com/build/releases/about-agp).
