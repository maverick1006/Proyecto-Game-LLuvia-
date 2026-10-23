# Abrir GameLluvia en Eclipse

## GameLluvia-Eclipse.zip: entrega recomendada para evaluar

Incluye fuentes, assets y bibliotecas de LibGDX. No requiere Gradle, Android SDK ni Internet para abrir y ejecutar. Necesitas Eclipse IDE for Java Developers y JDK 17 o superior. El Java de arranque del IDE puede ser distinto del Java del proyecto.

1. Descomprime el ZIP en una carpeta normal fuera del workspace. No ejecutes archivos dentro del ZIP.
2. Abre Eclipse y elige un workspace nuevo.
3. Ve a **File > Import > General > Existing Projects into Workspace**.
4. En **Select root directory**, selecciona `GameLluvia`, donde están `.project` y `.classpath`.
5. Marca **GameLluvia**, pulsa **Finish** y espera la compilación automática.
6. Abre `lwjgl3/src/main/java/puppy/code/lwjgl3/Lwjgl3Launcher.java`.
7. Clic derecho en el editor > **Run As > Java Application**. Si pide configuración, selecciona **GameLluvia**. También puedes usar **Run > Run Configurations > Java Application > GameLluvia > Run**.

`GameLluvia.launch` configura `assets` como directorio de trabajo con una ruta relativa al workspace. `core` conserva las reglas compartidas y `lwjgl3` el arranque Desktop. Se muestran en un único proyecto Java Eclipse para facilitar su apertura; se conserva la estructura Gradle original para los otros IDEs.

## Si Java aparece en rojo

En **Window > Preferences > Java > Installed JREs > Add > Standard VM**, selecciona tu instalación del JDK. En **Java > Installed JREs > Execution Environments > JavaSE-17**, selecciona ese JDK compatible. Luego usa **Project > Clean**. No sustituyas `lib` por rutas personales.

Si falta `rain.mp3`, revisa **Run Configurations > Arguments > Working directory**: debe ser `${workspace_loc:GameLluvia/assets}`. Los assets también están incluidos como recursos del classpath.

## Si descargaste las fuentes desde GitHub

Importa la raíz con **File > Import > Gradle > Existing Gradle Project**, usa el Wrapper y JDK 17. Requiere Internet la primera vez. Ejecuta **lwjgl3 > application > run** desde Gradle Tasks.

Para regenerar el ZIP con bibliotecas incluidas:

```powershell
.\gradlew.bat -Pandroid=false exportarEclipse
```

Resultado: `build/distributions/GameLluvia-Eclipse.zip`. Este paquete se genera con Gradle; no confundir su generación con el comando Export del IDE. Para exportar desde Eclipse, sigue [la guía](docs/ENTREGA_ECLIPSE.md).

## Informe y manual incluidos

- [Informe ejecutivo PDF](01-Informe-GameLluvia.pdf): requisitos GM-1 a GM-10.
- [Manual de usuario PDF](02-Manual-de-Usuario-GameLluvia.pdf): reglas y controles.
- Fuentes editables: `informe/main.tex` y `manual/main.tex`.
- [Resultados y límites](docs/VALIDACION.md).
- [Android Studio](docs/ANDROID_STUDIO.md).

La entrega conserva Java y LibGDX; no cambia el framework del juego.
