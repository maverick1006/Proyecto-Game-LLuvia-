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
- Eclipse: instrucciones revisadas, pero sin ejecución comprobada desde el IDE. Falta que el equipo realice y verifique el export solicitado.
- Android: módulo y control presentes; compilación y dispositivo pendientes.
- Otros sistemas operativos e IDEs: no certificados por estas pruebas.
- GitHub: se verificó publicada `cuarta-version` en `af75f64`. La rama `codex/entrega-final` debe publicarse y comprobarse antes de entregar. El historial local queda en `HISTORIAL.txt`.
- Informe: el documento independiente de `Informe_GameLluvia` compiló correctamente en el editor LaTeX. La plantilla institucional y el manual corregidos también compilaron correctamente en el editor LaTeX. La compilación no sustituye la revisión visual final del PDF exportado desde Prism.
