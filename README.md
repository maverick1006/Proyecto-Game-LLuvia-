# GameLluvia — Guardián de la lluvia

Proyecto de Programación Avanzada desarrollado en **Java y LibGDX**, a partir de GameLluvia2024 entregado por el profesor. Recolecta agua, supera 30 niveles y utiliza el agua acumulada para vencer al OVNI.

**Integrantes:** Matías Santelices S., Rodrigo Escudero y Ariel Fuenzalida.
**Entrega:** 23 de octubre de 2026.

## Contenido

- [Instalación y requisitos](#instalación-y-requisitos)
- [Abrir y ejecutar en Eclipse](#abrir-y-ejecutar-en-eclipse)
- [Ejecutar por consola](#ejecutar-por-consola)
- [Generar y ejecutar el JAR](#generar-y-ejecutar-el-jar)
- [Juego y controles](#juego-y-controles)
- [Organización y patrones](#organización-y-patrones)
- [Pruebas y plataformas](#pruebas-y-plataformas)
- [Documentación de entrega](#documentación-de-entrega)

## Instalación y requisitos

- **JDK 17**, configurado en `JAVA_HOME`. El código se compila con compatibilidad Java 11; para reproducir nuestro entorno usamos JDK 17.
- **Gradle Wrapper 8.10.1**, incluido en el repositorio. No hace falta instalar Gradle por separado.
- **LibGDX 1.12.1**, descargado por Gradle.
- Para Eclipse: **Eclipse IDE for Java Developers**, con **Buildship (Gradle Integration)** y soporte para Java 17. Si la versión de Eclipse requiere otro Java para iniciar el IDE, su JVM de arranque es independiente del JDK 17 configurado para este proyecto y Gradle.
- Conexión a Internet para la primera resolución de dependencias. No copiar cachés ni rutas personales de otro equipo.

Descarga el ZIP de la rama de entrega o clona el repositorio:

```bash
git clone https://github.com/maverick1006/Proyecto-Game-LLuvia-.git
cd Proyecto-Game-LLuvia-
```

La versión jugable anterior está en `cuarta-version` (commit `af75f64`). La preparación final está publicada en `entrega-final` (seis commits verificados hasta `dd95078`):

```bash
git fetch origin
git switch --track origin/entrega-final
```

Si recibiste el ZIP final, ya contiene esa preparación: descomprímelo y utiliza su carpeta raíz, donde están `settings.gradle` y `gradlew.bat`.

## Abrir y ejecutar en Eclipse

1. Descomprime o clona el proyecto en una carpeta fuera del workspace de Eclipse.
2. En `Window > Preferences > Java > Installed JREs`, agrega el **JDK 17** y selecciónalo para el proyecto. Verifica también el Java Home de Gradle en la configuración de importación.
3. Elige `File > Import > Gradle > Existing Gradle Project`.
4. Selecciona la **raíz completa**, no solo `core`, `assets` o `lwjgl3`. Conserva la opción **Gradle Wrapper** y finaliza la importación.
5. Espera a que se descarguen las dependencias. Si es necesario, clic derecho sobre el proyecto > `Gradle > Refresh Gradle Project`.
6. Abre `Window > Show View > Other > Gradle > Gradle Tasks`. Ejecuta la tarea **`:lwjgl3:run`**, en `lwjgl3 > application > run`. Esta tarea ya configura el directorio de trabajo como `assets`.

### Alternativa: Run As > Java Application

1. Busca `lwjgl3/src/main/java/puppy/code/lwjgl3/Lwjgl3Launcher.java`.
2. En `Run > Run Configurations > Java Application`, crea una configuración para el módulo `lwjgl3` (el nombre que Eclipse muestre para ese módulo).
3. Clase principal: **`puppy.code.lwjgl3.Lwjgl3Launcher`**. No es `DesktopLauncher`, ni se ejecuta `GameLluvia` directamente.
4. En `Arguments > Working directory`, selecciona **Other > Workspace** y la carpeta **`assets` de la raíz**. Si Eclipse no la muestra, usa `File System` y elige esa misma carpeta local. Esta ruta se configura en cada equipo; no publiques una ruta `C:/Users/...` propia.
5. En la pestaña JRE, selecciona JDK 17. Aplica los cambios y ejecuta. Esta configuración permite usar después `Run As > Java Application`.

Si aparece `File not found: rain.mp3`, revisa la carpeta `assets`, el directorio de trabajo y la sincronización Gradle. No muevas recursos manualmente dentro de `src` para ocultar el problema.

`Existing Projects into Workspace` es una ruta alternativa para un **export real de Eclipse con sus metadatos**. Un ZIP de GitHub no necesita contener `.project` ni `.classpath`: se importa como proyecto Gradle. Consulta [la guía de exportación](docs/ENTREGA_ECLIPSE.md).

## Ejecutar por consola

En PowerShell, desde la raíz:

```powershell
java -version
.\gradlew.bat :lwjgl3:run
```

Linux/macOS:

```bash
chmod +x gradlew
./gradlew :lwjgl3:run
```

El módulo de escritorio se llama **`lwjgl3`**: no existen las tareas `desktop:run` o `desktop:dist` en este proyecto. La ejecución Gradle configura los assets automáticamente.

## Generar y ejecutar el JAR

```powershell
.\gradlew.bat :core:test :lwjgl3:dist
java -jar .\lwjgl3\build\libs\GameLluvia.jar
```

En Linux/macOS, sustituye el wrapper por `./gradlew` y usa barras `/`. Para ejecutar el JAR en macOS, añade `-XstartOnFirstThread` antes de `-jar`.

**`GameLluvia.jar` contiene las clases, dependencias y assets.** Requiere Java; no incluye una JVM. No confundirlo con `lwjgl3.jar`, que es el JAR simple del módulo y no es el entregable autónomo.

También se puede generar una distribución con bibliotecas separadas:

```powershell
.\gradlew.bat :lwjgl3:installDist :lwjgl3:distZip
.\lwjgl3\build\install\lwjgl3\bin\lwjgl3.bat
```

Para esa alternativa se comparte toda la carpeta `lwjgl3/build/install/lwjgl3`, o el ZIP de `lwjgl3/build/distributions`. No se entrega solo el `.bat`.

## Juego y controles

La campaña recorre llanura (1–10), hielo (11–20) y Atacama (21–30). El agua suma puntos y munición. Cada 100 puntos permite avanzar; los peligros pueden restar progreso. Con 3000 puntos el OVNI lleva al jugador al espacio. Para vencerlo se disparan chorros de agua y se siguen recogiendo gotas para recargar.

- **Noob:** 5 vidas. **Pro:** 3. **Hacker:** 1. También cambian velocidad, frecuencia y peligrosidad de la lluvia.
- Tras cuatro segundos sin agua empieza la reducción de velocidad. Recoger agua restaura la hidratación.
- Hay mejoras temporales y perjuicios distintos según la zona, explicados en la guía dentro del juego.
- La tienda ofrece skins cosméticas por monedas y por logros. Completar la campaña y vencer al jefe desbloquea una recompensa exclusiva por dificultad.
- Cada nuevo hito de cinco niveles permite una ronda de monedas de ocho segundos.
- El menú esconde un combate contra el OVNI: ganarlo revela al alien. No modifica dinero, ranking ni niveles de campaña.
- El versus tiene pantalla dividida y teclado compartido. No requiere red ni modifica el perfil de campaña.

| Acción | Campaña | Versus |
|---|---|---|
| Mover | A/D o izquierda/derecha | J1: A/D; J2: izquierda/derecha |
| Disparar al jefe | Espacio o clic en la zona de juego | J1: W; J2: arriba |
| Pausar | P o Escape | P o Escape |
| Iniciar/reintentar | Enter o botones | Enter o botones |
| Sonido | M o botón | M o botón |
| Control alternativo | Botón del menú o T; ratón/táctil | Dos controles de teclado |

Las monedas, skins y puntuaciones se guardan **localmente**, sin cuenta en línea. El launcher usa `../guardado` respecto al directorio de trabajo; si ejecutas desde otra carpeta, puede utilizar otro guardado. No incluyas guardados personales en la entrega.

![Menú](docs/capturas/01-menu.png)
![Versus](docs/capturas/17-versus.png)

## Organización y patrones

| Carpeta | Contenido |
|---|---|
| `core` | Reglas, estados, dibujo, controles y perfil compartidos |
| `lwjgl3` | Launcher Desktop y prueba visual |
| `android` | Módulo Android opcional, pendiente de validación |
| `assets` | Recursos de audio e imágenes de la base |
| `docs` | Reporte, auditoría, diagramas y capturas |

Los personajes y escenarios actuales se dibujan con formas de LibGDX. Las skins son representaciones sencillas; no son sprites oficiales de las franquicias.

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
.\gradlew.bat :core:test
.\gradlew.bat :lwjgl3:smoke
.\gradlew.bat :lwjgl3:smokeJar
```

Las pruebas de lógica cubren progresión, daño, efectos, compras, recompensas y versus. `smoke` abre una ventana y verifica escenarios con un perfil aislado. `smokeJar` verifica esos escenarios cargando el JAR completo desde una carpeta sin assets externos. No sustituyen una partida manual prolongada ni certifican audio percibido, Eclipse o Android.

Consulta resultados y límites concretos en [VALIDACION.md](docs/VALIDACION.md).

### Android e IDEs restantes

Android no está verificado en dispositivo. Para habilitar su módulo se necesita SDK Platform 35, JDK 17 y `local.properties` con el `sdk.dir` del equipo:

```powershell
.\gradlew.bat -Pandroid=true :android:assembleDebug
```

El APK esperado es `android/build/outputs/apk/debug/android-debug.apk`. La tarea es una instrucción de compilación, no una afirmación de que Android haya sido probado.

Android Studio, VS Code y NetBeans pueden trabajar sobre la misma raíz Gradle. No se ha certificado su importación en todas las versiones de estos IDEs.

## Documentación de entrega

- [Reporte GM-1 a GM-10](docs/REPORTE.md).
- [Informe LaTeX con plantilla institucional](informe/main.tex).
- [Informe final PDF](informe/Informe-GameLluvia-final.pdf).
- [Manual de usuario corregido](manual/main.tex).
- [Manual final PDF](manual/Manual-GameLluvia-final.pdf).
- [Revisión de los aportes del equipo](docs/REVISION_APORTES.md).
- [Auditoría de código y rúbrica](docs/AUDITORIA.md).
- [Exportación Eclipse y Aula Virtual](docs/ENTREGA_ECLIPSE.md).
- [Pruebas y limitaciones](docs/VALIDACION.md).
- [Diagramas UML editables e imágenes](docs/uml/).

## Referencias y créditos

Base: GameLluvia2024 entregado por el profesor. Se conservan sus sonidos y recursos de partida. Los sonidos adicionales del OVNI, disparo y victoria son efectos sintetizados para este proyecto.

La organización de instalación, ejecución, controles y créditos toma como referencia el [README de cars](https://github.com/Schiz0idCat/cars/blob/main/README.md); sus versiones y comandos no se copian porque corresponden a otro proyecto. El flujo de importación se contrastó con la [documentación de LibGDX](https://libgdx.com/wiki/start/import-and-running).
