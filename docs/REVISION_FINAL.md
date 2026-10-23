# Revisión de la entrega

23 de octubre de 2026. Se revisaron el código Java, el informe, el manual y las instrucciones de apertura. La plantilla institucional y el diseño del manual proceden de los documentos compartidos por el equipo. Los originales de los compañeros se mantienen aparte de la versión corregida.

| Requisito | Evidencia revisada | Resultado |
|---|---|---|
| GM-1: explicación para el jugador | Informe, apartado GM-1; manual con controles, objetivos, campaña y capturas | Documentado |
| GM-2: análisis de la base y modificaciones | Informe, apartado GM-2, comparación de responsabilidades y reglas | Documentado |
| GM-3: UML de clases propias y LibGDX | Dos vistas del dominio y cuatro diagramas de patrones en `docs/uml`; imágenes en el informe | Actualizado con atributos, métodos, visibilidad y relaciones |
| GM-4: abstracta y dos hijas usadas | `Gota`, `GotaBuena`, `ObjetoEspecial`; creación y recorrido en `Lluvia` | Implementado y usado |
| GM-4: interfaz y dos implementaciones usadas | `ControlMovimiento`, `ControlTeclado`, `ControlTactil`; selección en `GameLluvia` y delegación en `Personaje` | Implementado y usado |
| GM-5: encapsulamiento, GRASP y SOLID | Atributos privados, compras validadas, clases con responsabilidades y dependencia de interfaz | Explicado con ejemplos y límites, sin afirmar cumplimiento absoluto |
| GM-6: Singleton | `ConfiguracionJuego`, constructor privado y `getInstancia()` | Código, problema, contexto, solución y UML |
| GM-7: Strategy | `Personaje` delega en `ControlMovimiento` | Código, problema, contexto, solución y UML |
| GM-8: Template Method | `Gota.actualizar()` final y `aplicarEfecto()` protegido abstracto | Código, problema, contexto, solución y UML |
| GM-9: Builder | `Nivel.Builder`, utilizado en `Dificultad.crearNivel()` | Código, problema, contexto, solución y UML |
| GM-10: seis commits como mínimo | Rama `entrega-final`, nueve commits publicados hasta `bb9b721`, antes de esta revisión | Mínimo cumplido; esta revisión añade otro avance |

## Comprobaciones y límites

- Las 36 pruebas de lógica se volvieron a ejecutar: sin fallos ni errores.
- Se comprobó la apertura del paquete Eclipse en un workspace nuevo y el inicio de una partida.
- El JAR incluye recursos y dependencias; las instrucciones ofrecen ejecución directa y compilación Gradle como alternativas.
- El APK Android compila. Falta probar la ejecución, los gestos, el audio y el rendimiento en teléfono. La sincronización visual en Android Studio no se presenta como comprobada.
- El informe y el manual conservan los tres nombres del equipo. Las explicaciones citan clases y métodos que existen en el código.
- El Singleton se usa en el hilo principal: no se afirma que esté sincronizado. `GameLluvia` todavía reúne coordinación y presentación; nuevos efectos pueden exigir cambios en el switch de `Personaje`.
- El dominio UML selecciona miembros para facilitar la lectura: no pretende listar todos los métodos auxiliares. `GotaMala` permanece como clase de la base y no es una de las hijas activas usadas como evidencia de GM-4.

## Para la defensa

Cada integrante debe poder seguir este recorrido: `Lwjgl3Launcher` inicia LibGDX; `GameLluvia` recibe entradas y dibuja; `Partida` aplica las reglas; `Lluvia` actualiza gotas; `Personaje` recoge sus efectos; `PerfilJugador` guarda compras y resultados mediante `Preferences`.

La evidencia del informe respalda la pauta compartida. La calificación también depende de la evaluación del profesor, de la defensa y de entregar los archivos correctos dentro del plazo.
