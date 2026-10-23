# Exportación para Eclipse y Aula Virtual

## Qué se entrega

1. Fuentes completas con Gradle Wrapper, módulos `core` y `lwjgl3`, carpeta `assets`, documentación y pruebas. Mantener también el módulo Android si se entrega el repositorio completo, aclarando su estado de validación.
2. `GameLluvia.jar`, construido con `:lwjgl3:dist`; contiene dependencias y assets, pero requiere Java.
3. Informe ejecutivo final en PDF, obtenido del proyecto Prism, con autores, diagramas y evidencias actualizadas.
4. El **export realizado desde Eclipse** que solicita la pauta. Un ZIP preparado desde Git no demuestra por sí mismo que se haya realizado esa exportación ni que se haya probado el IDE.

## Importar y preparar Eclipse

1. Descomprimir las fuentes en una carpeta nueva fuera del workspace.
2. Instalar/configurar JDK 17 para el proyecto y Gradle. Usar Eclipse con Buildship.
3. `File > Import > Gradle > Existing Gradle Project`: seleccionar la raíz con `settings.gradle`, usar el Wrapper y finalizar.
4. Esperar la sincronización. Si hace falta, `Gradle > Refresh Gradle Project`.
5. Ejecutar `:core:test` y `:lwjgl3:run` desde Gradle Tasks. Deben pasar las pruebas y abrirse el juego.
6. Si se ejecuta como Java Application, la clase es `puppy.code.lwjgl3.Lwjgl3Launcher`; en `Run Configurations > Arguments > Working directory` seleccionar la carpeta raíz `assets`. Elegir JDK 17 en JRE.

## Metadatos: qué conservar y qué no

| Archivo | Repositorio Gradle | Export real desde Eclipse |
|---|---|---|
| `settings.gradle`, `build.gradle`, `gradle.properties` y scripts de cada módulo | Obligatorios | Incluir |
| `gradlew`, `gradlew.bat`, `gradle/wrapper/*` | Obligatorios, incluido `gradle-wrapper.jar` | Incluir |
| `.project` | Puede generarlo Eclipse/Buildship; está ignorado por Git | Incluir si el IDE lo generó para los proyectos exportados |
| `.classpath` | No copiar el de otro computador como solución universal | Incluir el generado por el IDE; inspeccionar que no dependa de `C:/Users/...` o de bibliotecas fuera del paquete |
| `.settings/` | Configuración local, normalmente ignorada | Conservar los ajustes necesarios para el proyecto; revisar rutas de Java/Gradle y datos específicos del equipo |
| `.metadata/` del workspace | No incluir | No incluir |
| `.gradle/`, cachés, `bin/`, `build/` | No incluir como fuentes | Excluir; entregar el JAR compilado aparte |
| `local.properties` | No incluir: contiene SDK personal | No incluir; cada equipo configura Android si lo necesita |
| `guardado/` y logs | No incluir | No incluir |

No se deben subir metadatos con rutas absolutas de otro equipo para aparentar portabilidad. Buildship reconstruye la configuración a partir de Gradle. La tarea `gradlew.bat eclipse` existe en los módulos, pero su salida puede contener rutas a dependencias de la caché local; no sustituye una prueba de importación en otro equipo.

## Exportar desde Eclipse a ZIP

1. Con el proyecto importado y funcionando, usar `File > Export > General > Archive File`.
2. Seleccionar los proyectos de la raíz y los módulos `core` y `lwjgl3`, además de `assets`, wrapper y documentación. Revisar la previsualización del árbol: el ZIP debe conservar la raíz y los módulos como carpetas hermanas, sin perder `settings.gradle` ni duplicar árboles completos dentro de otro módulo.
3. Excluir cachés, salidas de compilación, guardados y archivos personales indicados arriba. Conservar los metadatos Eclipse necesarios.
4. Elegir ZIP y un destino **fuera del proyecto**, por ejemplo `GameLluvia-Eclipse.zip`. Exportar.
5. Abrir el ZIP y comprobar que están `assets/rain.mp3`, las fuentes de ambos módulos, `settings.gradle`, `gradlew.bat` y `gradle/wrapper/gradle-wrapper.jar`.
6. Descomprimir el ZIP en otra carpeta y abrir **un workspace nuevo**. Importarlo y ejecutarlo allí antes de subirlo.

### Importación del profesor

La ruta reproducible es `Existing Gradle Project` sobre la raíz descomprimida. Si se utiliza `General > Existing Projects into Workspace`, seleccionar la raíz con los `.project`, habilitar búsqueda de proyectos anidados si aparece y seleccionar ambos módulos. Después sincronizar con Gradle/Buildship y resolver el JDK. Si el export no contiene metadatos adecuados, volver a la importación Gradle; no seleccionar esta opción sobre un ZIP de fuentes sin `.project`.

Para evitar problemas con recursos, ejecutar preferentemente `:lwjgl3:run`, que ya apunta a `assets`. Para una ejecución Java directa, configurar el directorio de trabajo en el nuevo equipo; una configuración de lanzamiento con rutas personales no es portable.

## Comprobación de entrega

- [ ] Los tres autores aparecen con sus nombres correctos.
- [ ] Fuentes y documentación corresponden a la misma versión.
- [ ] Export Eclipse probado desde un workspace nuevo; registrar versión del IDE y resultado.
- [ ] JAR ejecutado con `java -jar GameLluvia.jar`.
- [ ] Informe PDF compilado sin imágenes faltantes ni referencias rotas.
- [ ] Historial publicado con al menos seis commits reales.
- [ ] Enlace de GitHub apunta a la rama de entrega, no a una versión antigua.
- [ ] Android se declara según la evidencia disponible; no anunciarlo como probado si no lo está.
- [ ] Archivo del Aula Virtual comprobado después de subirlo; envío a cargo del equipo.

Las rutas y etiquetas de menús pueden variar entre versiones de Eclipse. Estas instrucciones no afirman que la importación haya sido ejecutada desde Eclipse en esta sesión. Referencia: [importación y ejecución en LibGDX](https://libgdx.com/wiki/start/import-and-running).
