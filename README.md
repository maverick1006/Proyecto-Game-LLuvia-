# Guardián de la Lluvia

Proyecto de Programación Avanzada en Java y LibGDX, basado en el juego entregado por el profesor.

**Integrantes:** Matías Santelices S., Rodrigo Escudero y Ariel Fuenzalida.

![Menú principal de Guardián de la Lluvia](docs/capturas/01-menu.png)

## Descargar y jugar

1. Abre la rama para entregar es entrega-final. En GitHub selecciona esa rama y pulsa Code > Download ZIP. Descomprime el archivo antes de abrir el proyecto
2. Descomprime el ZIP completo. La carpeta que debes seleccionar en Eclipse es la que contiene juntos `settings.gradle`, `gradlew.bat`, `core`, `lwjgl3` y `assets`.
3. Ten instalado **JDK 17** y **Eclipse IDE for Java Developers** con **Buildship (Gradle Integration)**. La primera importación requiere Internet para descargar Gradle y LibGDX. Para jugar en escritorio no necesitas Android SDK.

### Importar y jugar en Eclipse

1. Abre Eclipse. Si ya tienes otro proyecto con el mismo nombre, usa **File > Switch Workspace > Other…** y elige un espacio de trabajo nuevo.
2. Ve a **Window > Preferences > Gradle** y elige la carpeta del **JDK 17** en **Java home**. Selecciona la carpeta del JDK que contiene `bin`, por ejemplo `C:\Program Files\Java\jdk-17`. Si Eclipse solicita registrar el JDK, puedes hacerlo en **Window > Preferences > Java > Installed JREs > Add… > Standard VM**.
3. Ve a **File > Import… > Gradle > Existing Gradle Project**. Si falta esa opción, busca **Buildship Gradle Integration** en **Help > Eclipse Marketplace**, instálalo y reinicia Eclipse.
4. En **Project root directory**, selecciona la carpeta descomprimida donde está `settings.gradle`. Pulsa **Next**, usa **Gradle Wrapper** como distribución y verifica **Java home** si el asistente muestra esa opción. Deja desactivado el modo sin conexión durante la primera importación. Pulsa **Finish** y espera a que termine la sincronización.
5. Abre **Window > Show View > Other… > Gradle > Gradle Tasks**. Despliega el proyecto **GameLluvia**, entra al módulo **lwjgl3** y haz doble clic en **application > run** (tarea `:lwjgl3:run`). Gradle prepara la carpeta de recursos automáticamente.
6. Cuando se abra **Guardián de la Lluvia**, elige **Comenzar partida**, selecciona una dificultad y avanza el prólogo con **Enter** o **Continuar**. Puedes cerrar la ventana del juego y volver a ejecutar la misma tarea desde Eclipse.

Si falta la tarea o hay errores de dependencias, usa **clic derecho en el proyecto > Gradle > Refresh Gradle Project** y revisa **Console**. Comprueba el JDK de Gradle y que la carpeta seleccionada contiene todo el proyecto. Para ejecutar mientras revisas la importación, usa la alternativa por PowerShell.

### Revisar el código en Eclipse

En **Project Explorer**, abre **core > src/main/java > puppy.code**. Ahí están las reglas del juego. **lwjgl3** contiene el lanzador de escritorio.

| Qué revisar | Dónde empezar |
| --- | --- |
| Arranque en escritorio | `Lwjgl3Launcher.main()` crea `GameLluvia`. |
| Menú y ciclo del juego | `GameLluvia.render()` coordina entrada, actualización y dibujo. |
| Niveles y patrullas | `Partida.actualizar()`, `actualizarPatrulla()` y `disparar()`. |
| Strategy | `GameLluvia.alternarControlEnPartida()`, `Personaje.setControlMovimiento()` y `Personaje.actualizar()`. |
| Template Method | `Gota.actualizar()` y sus variantes `GotaBuena`, `GotaMala` y `ObjetoEspecial`. |
| Hidratación, armas y jefe | `Personaje.recogerAgua()`, `ArmaBioma` y `JefeOvni.recibirDaño()`. |
| Singleton y Builder | `ConfiguracionJuego.getInstancia()` y `Dificultad.crearNivel()`, que usa `Nivel.Builder`. |

### PowerShell

Abre PowerShell en la carpeta que contiene `gradlew.bat`. Comprueba que `java -version` muestre Java 17. Si tienes varias versiones, configura `JAVA_HOME` con la ruta de tu JDK 17 antes de ejecutar:

```powershell
.\gradlew.bat -Pandroid=false :lwjgl3:run
```

En Linux o macOS, usa `./gradlew` con las mismas opciones.

### Android Studio

Abre la carpeta raíz, selecciona JDK 17 para Gradle y configura SDK Platform 35. Después de sincronizar, puedes compilar con `.\gradlew.bat -Pandroid=true :android:assembleDebug`.

## Controles

| Acción | Campaña | Versus |
| --- | --- | --- |
| Mover | A/D, flechas o toque | J1 A/D · J2 flechas |
| Disparar | Espacio o clic/toque en el campo | J1 W · J2 flecha arriba |
| Pausa | P o Esc | P o Esc |

En campaña, **T** o el botón del HUD cambian entre teclado y control táctil en plena partida.

## Documentos

- [Informe del proyecto (PDF)](01-Informe-GameLluvia.pdf), con los diagramas UML.
- [Manual de usuario (PDF)](02-Manual-de-Usuario-GameLluvia.pdf).
