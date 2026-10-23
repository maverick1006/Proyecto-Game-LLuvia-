# Entrega para Eclipse

La guía de apertura está en [ABRIR_EN_ECLIPSE.md](../ABRIR_EN_ECLIPSE.md). El ZIP específico contiene `.project`, `.classpath`, ajustes de Java, configuración compartida de ejecución y dependencias dentro de `lib`, mediante rutas relativas. No incluye el workspace ni rutas a cachés personales.

## Generar el paquete portátil

```powershell
.\gradlew.bat -Pandroid=false exportarEclipse
```

Resultado: `build/distributions/GameLluvia-Eclipse.zip`. Se genera con Gradle a partir de las mismas fuentes de Desktop. La compilación Eclipse usa `core/src/main/java`, `lwjgl3/src/main/java`, recursos `assets` y bibliotecas `lib` en un solo proyecto Java. Gradle conserva sus módulos originales.

## Exportar desde Eclipse

1. Importa y ejecuta el proyecto según la guía de apertura.
2. Selecciona el proyecto y usa **File > Export > General > Archive File**.
3. Selecciona las fuentes, `assets`, `lib`, documentación, wrapper, scripts y metadatos `.project`, `.classpath`, `.settings` y `GameLluvia.launch`.
4. Excluye `bin`, `build`, `.gradle`, guardados, logs, `local.properties` y `.metadata` del workspace.
5. Elige ZIP y un destino fuera del proyecto. Conserva la carpeta raíz `GameLluvia`.
6. Reimporta el ZIP en otro workspace y comprueba el arranque antes de entregar.

No confundir el ZIP de fuentes de GitHub con la versión Eclipse que lleva bibliotecas incluidas. Los PDF finales están en la raíz, junto al README. Las carpetas `informe` y `manual` conservan las fuentes LaTeX.

## Comprobación realizada

El 23/10/2026 se descomprimió el paquete generado en una carpeta de prueba, se importó con **Existing Projects into Workspace** en un workspace nuevo de Eclipse 2024-06 (4.32), se comprobó la vista Problems sin errores y se ejecutó `Lwjgl3Launcher` usando la configuración compartida. El IDE y la ejecución utilizaron su Java 21 integrado; los builds Gradle Desktop y Android se comprobaron con JDK 17. La compatibilidad de compilación del código sigue siendo Java 11.

Evidencia: `docs/capturas/18-eclipse-menu.png`. Los resultados restantes y límites se registran en [VALIDACION.md](VALIDACION.md).
