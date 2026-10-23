# GameLluvia — Guardián de la lluvia

Proyecto de Programación Avanzada desarrollado en **Java y LibGDX**, a partir de GameLluvia2024 entregado por el profesor. Recolecta agua, supera 30 niveles y utiliza el agua acumulada para vencer al OVNI.

**Integrantes:** Matías Santelices S., Rodrigo Escudero y Ariel Fuenzalida.
**Entrega:** 23 de octubre de 2026.

## Documentos para revisar primero

Los dos PDF finales están en esta misma carpeta, junto al README:

1. **[Informe del proyecto](01-Informe-GameLluvia.pdf)**.
2. **[Manual de usuario](02-Manual-de-Usuario-GameLluvia.pdf)**.

Los PDF finales se encuentran en la raíz del repositorio.

## Descargar el trabajo completo

La rama para entregar es **[entrega-final](https://github.com/maverick1006/Proyecto-Game-LLuvia-/tree/entrega-final)**. En GitHub selecciona esa rama y pulsa **Code > Download ZIP**. Descomprime el archivo antes de abrir el proyecto.

El ZIP incluye el código Java, los recursos del juego, las pruebas, la configuración de Gradle y ambos PDF. El informe contiene los diagramas UML y la explicación de los requisitos.

Los ejecutables se generan desde las fuentes siguiendo los pasos de este README.

## Contenido

- [Instalación y requisitos](#instalación-y-requisitos)
- [Abrir y ejecutar en Eclipse](#abrir-y-ejecutar-en-eclipse)
- [Ejecutar por consola](#ejecutar-por-consola)
- [Generar y ejecutar el JAR](#generar-y-ejecutar-el-jar)
- [Juego y controles](#juego-y-controles)
- [Organización y patrones](#organización-y-patrones)
- [Pruebas y plataformas](#pruebas-y-plataformas)
- [Documentos para revisar primero](#documentos-para-revisar-primero)

## Instalación y requisitos

- **JDK 17**, configurado en `JAVA_HOME`. El código se compila con compatibilidad Java 11, para reproducir nuestro entorno usamos JDK 17.
- **Gradle Wrapper 8.10.1**, incluido en el repositorio. No hace falta instalar Gradle por separado.
- **LibGDX 1.12.1**, descargado por Gradle.
- Para Eclipse: **Eclipse IDE for Java Developers**, con **Buildship (Gradle Integration)** y soporte para Java 17. Si la versión de Eclipse requiere otro Java para iniciar el IDE, su JVM de arranque es independiente del JDK 17 configurado para este proyecto y Gradle.
- Conexión a Internet para la primera resolución de dependencias al importar las fuentes Gradle.

Para obtener directamente la rama correcta por Git:

```bash
git clone --branch entrega-final https://github.com/maverick1006/Proyecto-Game-LLuvia-.git GameLluvia
cd GameLluvia
```

## Abrir y ejecutar en Eclipse

1. Descomprime el ZIP completo fuera del workspace de Eclipse.
2. En Eclipse, abre **Window > Preferences > Java > Installed JREs** y agrega o selecciona el **JDK 17**.
3. Selecciona **File > Import > Gradle > Existing Gradle Project**. Si no aparece Gradle, instala **Buildship Gradle Integration** desde Eclipse Marketplace.
4. En **Project root directory**, selecciona la carpeta que contiene `settings.gradle` y `gradlew.bat`.
5. Utiliza **Gradle Wrapper** y configura **Gradle JVM / Java home** con JDK 17. Pulsa **Finish** y espera la descarga de dependencias.
6. Abre **Window > Show View > Other > Gradle > Gradle Tasks**. En el módulo `lwjgl3`, ejecuta **application > run**.

La tarea `run` configura el directorio de recursos automáticamente. Para escritorio no se necesita instalar Android SDK.

### Si Eclipse no abre el proyecto

Comprueba que importaste la raíz como proyecto **Gradle**, que JDK 17 está seleccionado y que tienes conexión a Internet. Después usa **Gradle > Refresh Gradle Project** sobre el proyecto.

También puedes compilar y ejecutar desde PowerShell, ubicado en la raíz descomprimida:

```powershell
.\gradlew.bat -Pandroid=false :core:test :lwjgl3:dist
java -jar .\lwjgl3\build\libs\GameLluvia.jar
```

Para ejecutarlo directamente con Gradle:

```powershell
.\gradlew.bat -Pandroid=false :lwjgl3:run
```

Si aparece un error de recursos, comprueba que descomprimiste también la carpeta `assets`. No importes únicamente `core` o `lwjgl3`.

### Generar un paquete independiente para Eclipse

```powershell
.\gradlew.bat -Pandroid=false exportarEclipse
```

Se genera `build/distributions/GameLluvia-Eclipse.zip` con fuentes, recursos, bibliotecas y configuración Eclipse. Descomprime ese paquete e impórtalo mediante **File > Import > General > Existing Projects into Workspace**. Luego ejecuta la configuración **GameLluvia** desde **Run > Run Configurations > Java Application**. Esta forma de importación corresponde al paquete generado.

## Android Studio

Abre la raíz con **File > Open**, configura SDK Platform 35 y Build-Tools 34.0.0, elige **Gradle JDK 17** y sincroniza. Android Studio escribe `sdk.dir` en `local.properties`, esto habilita el módulo Android automáticamente. No publiques ese archivo personal.

La compilación del APK se comprobó el 23/10/2026 mediante `.\gradlew.bat -Pandroid=true :android:assembleDebug`. El archivo generado queda en `android/build/outputs/apk/debug/android-debug.apk`. La ejecución en teléfono sigue pendiente.

## Ejecutar por consola

En PowerShell, desde la raíz:

```powershell
java -version
.\gradlew.bat -Pandroid=false :lwjgl3:run
```

Linux/macOS:

```bash
chmod +x gradlew
./gradlew -Pandroid=false :lwjgl3:run
```

El módulo de escritorio se llama **`lwjgl3`**: no existen las tareas `desktop:run` o `desktop:dist` en este proyecto. La ejecución Gradle configura los assets automáticamente.

## Generar y ejecutar el JAR

```powershell
.\gradlew.bat -Pandroid=false :core:test :lwjgl3:dist
java -jar .\lwjgl3\build\libs\GameLluvia.jar
```

En Linux/macOS, sustituye el wrapper por `./gradlew` y usa barras `/`. Para ejecutar el JAR en macOS, añade `-XstartOnFirstThread` antes de `-jar`.

**`GameLluvia.jar` contiene las clases, dependencias y assets.** Requiere Java. No incluye una JVM. No confundirlo con `lwjgl3.jar`, que es el JAR simple del módulo y no es el entregable autónomo.

También se puede generar una distribución con bibliotecas separadas:

```powershell
.\gradlew.bat -Pandroid=false :lwjgl3:installDist :lwjgl3:distZip
.\lwjgl3\build\install\lwjgl3\bin\lwjgl3.bat
```

Para esa alternativa se comparte toda la carpeta `lwjgl3/build/install/lwjgl3`, o el ZIP de `lwjgl3/build/distributions`. No se entrega solo el `.bat`.

## Juego y controles

La campaña recorre llanura (1–10), hielo (11–20) y Atacama (21–30). El agua suma puntos y munición. Cada 100 puntos permite avanzar, los peligros pueden restar progreso. Con 3000 puntos el OVNI lleva al jugador al espacio. Para vencerlo se disparan chorros de agua y se siguen recogiendo gotas para recargar.

- **Noob:** 5 vidas. **Pro:** 3. **Hacker:** 1. También cambian velocidad, frecuencia y peligrosidad de la lluvia.
- Tras cuatro segundos sin agua empieza la reducción de velocidad. Recoger agua restaura la hidratación.
- Hay mejoras temporales y perjuicios distintos según la zona, explicados en la guía dentro del juego.
- La tienda ofrece skins cosméticas por monedas y por logros. Completar la campaña y vencer al jefe desbloquea una recompensa exclusiva por dificultad.
- Cada nuevo hito de cinco niveles permite una ronda de monedas de ocho segundos.
- El menú esconde un combate contra el OVNI: ganarlo revela al alien. No modifica dinero, ranking ni niveles de campaña.
- El versus tiene pantalla dividida y teclado compartido. No requiere red ni modifica el perfil de campaña.

| Acción | Campaña | Versus |
|---|---|---|
| Mover | A/D o izquierda/derecha | J1: A/D, J2: izquierda/derecha |
| Disparar al jefe | Espacio o clic en la zona de juego | J1: W, J2: arriba |
| Pausar | P o Escape | P o Escape |
| Iniciar/reintentar | Enter o botones | Enter o botones |
| Sonido | M o botón | M o botón |
| Control alternativo | Botón del menú o T, ratón/táctil | Dos controles de teclado |

Las monedas, skins y puntuaciones se guardan **localmente**, sin cuenta en línea. El launcher usa `../guardado` respecto al directorio de trabajo; si ejecutas desde otra carpeta, puede utilizar otro guardado. No incluyas guardados personales en la entrega.


## Organización y patrones

| Carpeta | Contenido |
|---|---|
| `core` | Reglas, estados, dibujo, controles y perfil compartidos |
| `lwjgl3` | Launcher Desktop y prueba visual |
| `android` | Launcher Android y empaquetado APK, dispositivo pendiente |
| `assets` | Recursos de audio e imágenes de la base |

Los personajes y escenarios actuales se dibujan con formas de LibGDX.

| Requisito | Implementación |
|---|---|
| Abstracta y polimorfismo | `Gota`, `GotaBuena`, `ObjetoEspecial` |
| Interfaz | `ControlMovimiento`, `ControlTeclado`, `ControlTactil` |
| Singleton | `ConfiguracionJuego` |
| Strategy | Movimiento delegado por `Personaje` a `ControlMovimiento` |
| Template Method | Secuencia fija de `Gota.actualizar()` y efecto variable |
| Builder | `Nivel.Builder`, utilizado en `Dificultad.crearNivel()` |

## Pruebas y plataformas

```powershell
.\gradlew.bat -Pandroid=false :core:test
.\gradlew.bat -Pandroid=false :lwjgl3:smoke
.\gradlew.bat -Pandroid=false :lwjgl3:smokeJar
```

Las pruebas de lógica cubren progresión, daño, efectos, compras, recompensas y versus. `smoke` abre una ventana y verifica escenarios con un perfil aislado. `smokeJar` verifica esos escenarios cargando el JAR completo desde una carpeta sin assets externos. No sustituyen una partida manual prolongada ni certifican audio percibido o ejecución Android en dispositivo. La importación y el arranque desde Eclipse se comprobaron aparte.


### Android e IDEs restantes

Android no está verificado en dispositivo. Para habilitar su módulo se necesita SDK Platform 35, JDK 17 y `local.properties` con el `sdk.dir` del equipo:

```powershell
.\gradlew.bat -Pandroid=true :android:assembleDebug
```

El APK esperado es `android/build/outputs/apk/debug/android-debug.apk`. La compilación del APK está comprobada.

Android Studio, VS Code y NetBeans pueden trabajar sobre la misma raíz Gradle.

## Referencias y créditos

Base: GameLluvia2024 entregado por el profesor. Se conservan sus sonidos y recursos de partida.
