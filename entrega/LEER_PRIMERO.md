# Archivos para la entrega

Descarga la rama **entrega-final** completa mediante **Code > Download ZIP** y descomprímela. En esta carpeta encontrarás:

- **GameLluvia-Eclipse.zip**: proyecto Java para Eclipse, con la configuración de apertura comprobada, con fuentes, assets, bibliotecas y configuración compartida. Descomprime este segundo ZIP antes de importarlo.
- **GameLluvia.jar**: juego de escritorio listo para ejecutar con Java 17. Puedes usar `JUGAR_JAR.bat`.
- **GameLluvia-Android-debug.apk**: APK de prueba compilado; ejecución en teléfono pendiente.

El informe y el manual están en las carpetas **informe** y **manual** de la raíz descargada. Ambos tienen PDF y fuentes editables. También se incluyen dentro del ZIP de Eclipse. Sus anexos finales actualizan el estado de validación.

## Apertura en Eclipse

1. Descomprime GameLluvia-Eclipse.zip fuera del workspace.
2. File > Import > General > Existing Projects into Workspace.
3. Selecciona la carpeta GameLluvia, marca el proyecto y finaliza.
4. Abre Lwjgl3Launcher.java y usa Run As > Java Application; elige GameLluvia si pide configuración.

Consulta [la guía completa](../ABRIR_EN_ECLIPSE.md) y [los resultados comprobados](../docs/VALIDACION.md). El ZIP específico para Eclipse incluye dependencias con rutas relativas; requiere Eclipse y Java compatible, pero no SDK Android ni Internet para ejecutar.

Las fuentes de la raíz son el proyecto Gradle original. Puedes modificarlas y regenerar el paquete Eclipse con `gradlew.bat -Pandroid=false exportarEclipse`. La copia preparada para Eclipse se entrega para facilitar la apertura al profesor; los cambios posteriores se realizan en las fuentes originales de la raíz.

[Informe PDF](../informe/Informe-GameLluvia-final.pdf) | [Manual PDF](../manual/Manual-GameLluvia-final.pdf) | [Android Studio](../docs/ANDROID_STUDIO.md)

`SHA256SUMS.txt` permite comprobar que los tres archivos binarios se descargaron completos. Este paquete no realiza el envío al Aula Virtual.
