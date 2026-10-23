# Validación de entrega

Revisión: 23 de octubre de 2026. Entorno: Windows, JDK 17, LibGDX 1.12.1 y Gradle Wrapper 8.10.1.

## Ejecutado

Se ejecutó con repetición de tareas:

```powershell
.\gradlew.bat :core:test :lwjgl3:dist :lwjgl3:smokeJar :lwjgl3:installDist --rerun-tasks
```

La resolución de dependencias utilizó la caché disponible (`--offline` en esta máquina). Resultado: **BUILD SUCCESSFUL**.

| Suite | Pruebas | Fallos | Errores |
|---|---:|---:|---:|
| puppy.code.CampanaTest | 13 | 0 | 0 |
| puppy.code.ProgresoTest | 13 | 0 | 0 |
| puppy.code.ReglasJuegoTest | 10 | 0 | 0 |

Total: **36 pruebas de lógica**, sin fallos ni errores. `smokeJar` abrió una ventana y verificó escenarios de compra, movimiento, pausa, zonas, jefe, victoria, ranking, versus, acceso secreto y desbloqueo de alien con perfil aislado. Su classpath utiliza el JAR completo y las clases de la prueba; el directorio de trabajo no contiene los assets del proyecto. Esto verifica recursos y dependencias empaquetados.

`installDist` también se generó correctamente. `dist` produce `lwjgl3/build/libs/GameLluvia.jar`, con manifiesto `Main-Class`, dependencias y recursos.

## Evidencia y límites

- Resultados de reglas: `core/build/test-results/test/TEST-*.xml`, regenerables mediante Gradle.
- Capturas actuales: `docs/capturas`. Se generan con un perfil de prueba, no con la cartera real del jugador.
- La prueba visual prepara algunos escenarios mediante reflexión. La prueba de lógica de jefe recorre impactos reales de proyectiles; la preparación visual no demuestra una campaña completa jugada a mano.
- El audio se carga sin error; no se certifica su calidad subjetiva ni su reproducción en todos los equipos.
- Eclipse 2024-06 (4.32): ZIP descomprimido e importado en workspace nuevo; Problems sin errores; menú y partida ejecutados desde el IDE con Java 21 integrado. Configuración compartida y bibliotecas relativas.
- Android: APK debug compilado correctamente con JDK 17, SDK Platform 35 y Build-Tools 34.0.0. Dispositivo pendiente; ADB no detectó teléfonos conectados. No certifica gestos, sonido ni rendimiento móvil.
- Otros sistemas operativos e IDEs: no certificados por estas pruebas.
- GitHub: se verificó publicada `cuarta-version` en `af75f64`. Se confirmó publicada la rama `entrega-final` en `dd95078`, con seis commits. Los ajustes documentales posteriores deben distinguirse de esa verificación. El historial local queda en `HISTORIAL.txt`.
- Informe: el documento independiente de `Informe_GameLluvia` compiló correctamente en el editor LaTeX. La plantilla institucional y el manual corregidos también compilaron correctamente en el editor LaTeX. La compilación no sustituye la revisión visual final del PDF exportado desde Prism.

## Exportación final de documentos

Se importaron copias corregidas en Prism y se descargó el informe PDF final. Se revisaron las páginas renderizadas del informe y del manual; se corrigió el desborde de una ruta larga del JAR en el manual antes de su exportación final.

Informe Prism: https://prism.openai.com/?u=7d8354d9-4f9f-4736-b841-d3b6fb137117&pg=1&m=main.tex

Manual Prism: https://prism.openai.com/?u=207e1ac9-64ff-41bf-85aa-501e48b756a3&pg=1&m=main.tex

El historial remoto de entrega se verificó con seis commits hasta dd95078, en la rama entrega-final. Los ajustes posteriores de documentación se registran aparte.

## Apertura y compilación para la entrega (23/10/2026)

- `gradlew.bat -Pandroid=false exportarEclipse`: correcto; dependencias copiadas dentro de `lib`.
- `gradlew.bat -Pandroid=true :android:assembleDebug`: BUILD SUCCESSFUL; APK con assets y bibliotecas nativas arm64-v8a, armeabi-v7a y x86_64.
- SDK configurado únicamente en `local.properties`, ignorado por Git.
- Guías: `ABRIR_EN_ECLIPSE.md` y `docs/ANDROID_STUDIO.md`.
- Informe PDF y manual PDF incluidos únicamente en la raíz, junto al README; las carpetas `informe` y `manual` conservan las fuentes.

## Archivo completo de la rama

La carpeta `entrega` incluye el ZIP exportado desde Eclipse, el JAR Desktop, el APK debug y sus SHA-256. Estos archivos no se regeneran al descargar GitHub: están incluidos en la rama para facilitar la evaluación. El código editable principal está en la raíz Gradle; los documentos PDF finales están junto al README y sus fuentes en `informe` y `manual`. La exportación real desde Eclipse se comparó archivo por archivo con el paquete portátil: coincidencia completa.

## Revisión del informe y UML

Se contrastaron las clases y miembros mostrados con los archivos Java. Los diagramas presentan atributos, operaciones y visibilidad; se dividió el dominio en dos vistas y cada patrón conserva su explicación y evidencia. Se revisaron las copias originales compartidas del informe y manual; se conservaron la portada institucional y el diseño del manual. Las 36 pruebas se volvieron a ejecutar y finalizaron sin fallos ni errores. La revisión completa por GM está en REVISION_FINAL.md.

El paquete Eclipse se regeneró después de la revisión de UML y documentos. Las fuentes Java del juego no cambiaron respecto de la apertura comprobada en el IDE. Los PDF incluidos corresponden a las fuentes actuales y a las copias revisadas en Prism.
