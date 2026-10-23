# Revisión de aportes del equipo

23 de octubre de 2026. Se revisaron el manual `manual_usuario` y el informe técnico V2 (29 páginas PDF). Se conservaron la estructura GM-1 a GM-10, la portada institucional, el diseño del manual y las explicaciones compatibles con el código. Los originales remotos no se modificaron.

Fuentes: [manual](https://prism.openai.com/?u=c4b37500-1d80-4174-8390-60e74c8c7490&pg=1&m=main.tex) e [informe V2](https://prism.openai.com/?u=311e5d13-3831-4a00-a1f6-698674a2c97f&pg=1&m=main.tex).

## Correcciones prioritarias del informe

| Apartado | Diferencia | Corrección contra el código |
|---|---|---|
| GM-1, tabla página 6 | Café/fruta 5 s y termo 6 s | Son 7 y 8 segundos. |
| GM-1, páginas 7–8 | Bonus sin hito 30, nivel 31 y 30 skins | Hay bonus al alcanzar 30, jefe como fase posterior al nivel 30 y 29 aspectos en total. |
| GM-2, páginas 9–11 | Separación estricta y estabilidad total | Explicar límites: GameLluvia coordina y presenta; dominio usa Rectangle. Las pruebas no garantizan cualquier entorno. |
| GM-3, figura página 14 | Faltan nodos LibGDX y relaciones; composiciones invertidas y firma abreviada de Lluvia inexistente | UML nuevo con clases propias y LibGDX; diamante en el contenedor y firmas verificadas. |
| GM-4 y GM-8 | No distingue GotaMala heredada de las hijas activas | Usar GotaBuena y ObjetoEspecial como prueba de hijas instanciadas y utilizadas. |
| GM-5, páginas 17–18 | OCP absoluto y lista inmutable | Personaje.atrapar contiene un switch que cambia con nuevos efectos. La lista no modificable no vuelve inmutables sus objetos. |
| GM-6, páginas 19–20 | Atribuye preferencias duplicadas y carreras a la base; afirma atomicidad | Esos problemas no se comprobaron en la base. Singleton comparte configuración en el hilo principal y no está sincronizado. |
| GM-7, páginas 21–22 | Sin Strategy sería imposible añadir controles | Sería posible con condicionales; el patrón permite separar e intercambiar algoritmos. La selección ocurre al iniciar. |
| GM-8, páginas 23–24 | Nuevas gotas sin modificar generación y menciona curar | La plantilla centraliza caída/colisión, pero la fábrica y efectos pueden cambiar. No hay curación de vidas implementada. |
| GM-9, páginas 25–27 | Un constructor largo se llama telescópico e ISP por métodos fluidos | Un constructor largo aislado no es una familia telescópica. Builder da legibilidad y validación; eso no demuestra ISP. Dificultad es cliente del Builder. |
| GM-10, página 28 | Solo enlaza GitHub | Agregar historial y evidencia de seis commits publicados. |
| Evidencias | Algunas referencias de código son generales | Añadir archivo, clase, método y líneas comprobadas. |

## Correcciones del manual

| Tema | Corrección |
|---|---|
| Java | JDK 17 es el entorno probado. Compatibilidad de clases Java 11; Java 8 no funciona. |
| Hardware/sistemas | Retirar mínimos no medidos y garantías universales de 60 FPS. |
| Distribución | installDist necesita Java. La preparación final añade dist para un JAR con dependencias y assets. |
| Furia del OVNI | Factor 1,4 en frecuencia de ataques y oscilación, no duplicación. No hay transformación carmesí ni explosión implementadas. |
| Campaña | Jefe como fase posterior a 30, bonus también en 30, 29 skins. |
| Bolsa rota | Bloquea monedas durante cuatro segundos, no todos los recursos. |
| Ranking | Local por dificultad. Pro no tiene clasificación oficial. Abandonar guarda monedas pero no registra puntaje. |
| Agua | Recoger restaura hidratación; disparar consume munición sin alterar sed. |
| Control T | Cambia la selección desde el menú para la siguiente partida. |
| Llanura | El café es una mejora de la zona hielo. |
| Secreto | Coordenadas lógicas; no altera economía, ranking ni niveles guardados. |
| Persistencia | Compras y logros se guardan al registrarse; monedas al terminar/salir guardando. |

## Resultado preparado

El reporte completo está en REPORTE.md. Se prepararon cinco UML con fuentes PlantUML y PNG. Junto a la carpeta del juego están Informe_GameLluvia (documento independiente), Informe_Institucional_GameLluvia (plantilla recibida y capítulos corregidos) y Manual_GameLluvia (diseño conservado y reglas corregidas).

Consultar VALIDACION.md para distinguir lo ejecutado de lo pendiente. La publicación del historial, el export real desde Eclipse y el PDF final integrado en Prism se deben verificar antes del envío.
