# Auditoría de código frente a la pauta

Revisión de cierre: 23 de octubre de 2026. Base de juego: `af75f64`; cambios finales de empaquetado y documentación en `codex/entrega-final`.

| Ítem | Diagnóstico del código | Evidencia principal | Acción de cierre |
|---|---|---|---|
| GM-4 abstracta | Cumple la estructura y uso | `Gota`, `GotaBuena`, `ObjetoEspecial`; fábrica y recorrido en `Lluvia` | Reporte corregido para citar las hijas activas |
| GM-4 interfaz | Cumple estructura y uso | `ControlMovimiento`, `ControlTeclado`, `ControlTactil`; selección en `GameLluvia`, delegación en `Personaje` | Explicar ambos controles en defensa |
| GM-5 | Aplicación identificable, con límites | Estado privado, compra controlada, responsabilidades por clase, dependencia de interfaz | Documentar ejemplos sin afirmar SOLID absoluto |
| GM-6 | Singleton real | Constructor privado, instancia estática, `getInstancia()` y llamada desde `create()` | Problema/contexto/solución/UML incluidos |
| GM-7 | Strategy real | Interfaz de control y delegación de `Personaje` | No confundir contexto con selector de estrategia |
| GM-8 | Template Method real | `Gota.actualizar()` final llama a paso abstracto | Mostrar secuencia común, no solo herencia |
| GM-9 | Builder real | `Nivel.Builder` valida y crea; cliente `Dificultad.crearNivel()` | UML y evidencia actualizados |

No se detectó un requisito GM-4 a GM-9 que obligara a reescribir la lógica. No se añadió herencia artificial ni patrones extra. Las correcciones de cierre se concentran en evidencia, documentación y empaquetado: se agregó `:lwjgl3:dist` para producir el JAR con librerías/assets y `:lwjgl3:smokeJar` para probar ese empaquetado.

## Límites que deben defender con honestidad

- `GameLluvia` reúne coordinación y presentación; SRP se aplica parcialmente.
- Nuevos efectos requieren cambios en el `switch` de `Personaje`; no hay OCP absoluto.
- Los objetos de la lista no modificable de `Lluvia` siguen siendo mutables.
- `GotaMala` y el récord antiguo de `ConfiguracionJuego` permanecen como legado; el ranking vigente pertenece a `PerfilJugador`.
- Los diagramas son una vista de las relaciones principales, no una lista exhaustiva de miembros.
- Android y la importación real en Eclipse no están certificados por las pruebas Desktop.
- GM-10 exige publicación real. Consultar `VALIDACION.md` y `HISTORIAL.txt`.

## Evidencia detallada

`REPORTE.md` incluye enlaces con líneas comprobadas para cada ítem y fuentes PlantUML junto a imágenes. Revisar esas referencias si el equipo modifica el código después de este cierre.
