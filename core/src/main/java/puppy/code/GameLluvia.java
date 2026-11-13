package puppy.code;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.List;

public class GameLluvia extends ApplicationAdapter {
    public static final String TITULO = "Guardián de la Lluvia";
    private enum Estado { MENU, DIFICULTAD, PROLOGO, TIENDA, PUNTUACIONES, AYUDA, JUGANDO, PAUSA, DERROTA, VICTORIA, RESULTADO_VERSUS }
    private Estado estado = Estado.MENU;
    private final FitViewport viewport = new FitViewport(960, 600, new OrthographicCamera());
    private VistaJuego vista;
    private ConfiguracionJuego configuracion;
    private PerfilJugador perfil;
    private Partida partida;
    private PartidaVersus versus;
    private boolean modoVersus, modoOculto;
    private Dificultad dificultad = Dificultad.NOOB;
    private Music musica;
    private Music musicaJefe;
    private Sound sonidoChorro, sonidoVictoria;
    private Sound recoger, golpe;
    private boolean tactil, resultadoGuardado, nuevoRecord;
    private float reloj, tiempoRecord, tiempoMensaje;
    private String mensaje = "";
    private float avisoControl;
    private int paginaTienda, paginaPrologo;
    private Zona zonaGuia = Zona.LLANURA;

    @Override public void create() {
        vista = new VistaJuego();
        configuracion = ConfiguracionJuego.getInstancia();
        perfil = configuracion.getPerfil();
        tactil = Gdx.app.getType() == Application.ApplicationType.Android;
        musica = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        musica.setLooping(true); musica.setVolume(0.25f);
        recoger = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        golpe = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        musicaJefe = Gdx.audio.newMusic(Gdx.files.internal("ovni.wav"));
        musicaJefe.setLooping(true); musicaJefe.setVolume(0.18f);
        sonidoChorro = Gdx.audio.newSound(Gdx.files.internal("chorro.wav"));
        sonidoVictoria = Gdx.audio.newSound(Gdx.files.internal("victoria.wav"));
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
    private ControlMovimiento crearControl() {
        return tactil ? new ControlTactil(viewport) : new ControlTeclado();
    }

    private void alternarControlEnPartida() {
        tactil = !tactil;
        partida.getPersonaje().setControlMovimiento(crearControl());
        avisoControl = 2;
    }

    private static final String[][] PROLOGO = {
        {"01 / LA LLUVIA CAMBIÓ",
            "Soy Luma, guardiana de la llanura.",
            "La nave nodriza lanzó lluvia tóxica sobre la llanura.",
            "Recoge agua azul: sin hidratarte perderás velocidad y vidas."},
        {"02 / LA RUTA DEL AGUA",
            "Sigue el agua desde la llanura hasta el hielo del sur.",
            "Busca la espada de hielo y el láser solar de Atacama.",
            "Cada 100 puntos de agua avanzas un nivel."},
        {"03 / LA NAVE ESTA CERCA",
            "Si el OVNI te atrapa con su rayo, conserva la calma.",
            "Recupera su hidro-plasma y derriba los drones del escudo.",
            "Dispara con Espacio o toca el campo de juego."}
    };

    private void confirmarDificultad() {
        if (modoVersus) {
            iniciar();
        } else {
            paginaPrologo = 0;
            tiempoMensaje = 0;
            cambiarEstado(Estado.PROLOGO);
        }
    }

    private void avanzarPrologo() {
        if (paginaPrologo == PROLOGO.length - 1) iniciar();
        else paginaPrologo++;
    }

    private void iniciar() {
        if (modoOculto) {
            versus = null;
            partida = Partida.crearDesafioOculto(crearControl());
        } else if (modoVersus) {
            versus = new PartidaVersus(new ControlTeclado(Input.Keys.A, Input.Keys.D),
                    new ControlTeclado(Input.Keys.LEFT, Input.Keys.RIGHT), dificultad, System.nanoTime());
            partida = versus.getUno();
        } else {
            versus = null;
            partida = new Partida(crearControl(), dificultad);
        }
        resultadoGuardado = false;
        nuevoRecord = false;
        mensaje = "";
        avisoControl = 0;
        tiempoMensaje = 0;
        cambiarEstado(Estado.JUGANDO);
    }
    private void cambiarEstado(Estado nuevo) {
        estado = nuevo;
        actualizarMusica();
    }
    private void actualizarMusica() {
        boolean suena = estado == Estado.JUGANDO && configuracion.tieneSonido();
        boolean jefe = partida != null && (partida.enJefe() || partida.getFase() == Partida.Fase.ASCENSO
                || modoVersus && versus != null && versus.getDos().enJefe());
        if (suena && !jefe) { if (!musica.isPlaying()) musica.play(); } else musica.pause();
        if (suena && jefe) { if (!musicaJefe.isPlaying()) musicaJefe.play(); } else musicaJefe.pause();
    }
    private void avisar(String texto) { mensaje = texto; tiempoMensaje = 4; }
    private void guardarResultado(boolean completa) {
        if (partida == null || resultadoGuardado || versus != null) return;
        if (partida.esDesafioOculto()) {
            perfil.registrarDesafioOculto(partida);
            resultadoGuardado = true;
            return;
        }
        if (partida.gano()) {
            Aspecto premio = perfil.registrarVictoria(partida.getDificultad());
            avisar("Recompensa de campaña: " + premio.getNombre());
        }
        Personaje jugador = partida.getPersonaje();
        perfil.registrarNivel(partida.getDificultad(), partida.getMaximoNivel());
        nuevoRecord = perfil.guardarResultado(partida.getDificultad(), jugador.getPuntos(), partida.getMonedasFinales(), completa);
        resultadoGuardado = true;
        tiempoRecord = 0;
    }
    private void abandonar() {
        guardarResultado(false);
        cambiarEstado(Estado.MENU);
    }
    private void entrada() {
        boolean aceptar = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        boolean volver = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK);
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            configuracion.alternarSonido(); cambiarEstado(estado);
        }
        switch (estado) {
            case MENU:
                if (aceptar) { modoOculto = false; modoVersus = false; cambiarEstado(Estado.DIFICULTAD); }
                if (Gdx.input.isKeyJustPressed(Input.Keys.T)) tactil = !tactil;
                break;
            case DIFICULTAD:
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) dificultad = Dificultad.NOOB;
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) dificultad = Dificultad.PRO;
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) dificultad = Dificultad.HACKER;
                if (aceptar) confirmarDificultad();
                else if (volver) cambiarEstado(Estado.MENU);
                break;
            case PROLOGO:
                if (volver) cambiarEstado(Estado.DIFICULTAD);
                else if (aceptar) avanzarPrologo();
                break;
            case JUGANDO:
                if (volver || Gdx.input.isKeyJustPressed(Input.Keys.P)) cambiarEstado(Estado.PAUSA);
                else if (modoVersus) {
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) disparar(versus.getUno());
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) disparar(versus.getDos());
                } else {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.T)) alternarControlEnPartida();
                    com.badlogic.gdx.math.Vector2 puntero = viewport.unproject(new com.badlogic.gdx.math.Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    boolean clickJuego = Gdx.input.isButtonPressed(Input.Buttons.LEFT) && puntero.y > 32 && puntero.y < 510 && puntero.x >= 0 && puntero.x <= 960;
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || clickJuego) disparar(partida);
                }
                break;
            case PAUSA:
                if (aceptar || volver || Gdx.input.isKeyJustPressed(Input.Keys.P)) cambiarEstado(Estado.JUGANDO);
                break;
            case DERROTA:
            case VICTORIA:
            case RESULTADO_VERSUS:
                if (aceptar) iniciar();
                else if (volver) cambiarEstado(Estado.MENU);
                break;
            default:
                if (volver) cambiarEstado(Estado.MENU);
        }
    }
    private void disparar(Partida juego) {
        if (juego.disparar() && configuracion.tieneSonido()) sonidoChorro.play(0.4f);
    }
    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        reloj += delta;
        tiempoMensaje = Math.max(0, tiempoMensaje - delta);
        avisoControl = Math.max(0, avisoControl - delta);
        entrada();
        if (estado == Estado.JUGANDO) actualizar(delta);
        actualizarMusica();
        if (estado == Estado.DERROTA || estado == Estado.VICTORIA) tiempoRecord += delta;
        ScreenUtils.clear(0.025f, 0.04f, 0.07f, 1);
        vista.preparar(viewport);
        vista.fondo(reloj, false, Zona.LLANURA, 960);
        switch (estado) {
            case MENU: menu(); break;
            case DIFICULTAD: dificultades(); break;
            case PROLOGO: prologo(); break;
            case TIENDA: tienda(); break;
            case PUNTUACIONES: puntuaciones(); break;
            case AYUDA: ayuda(); break;
            case JUGANDO: dibujarPartida(); break;
            case PAUSA: pausa(); break;
            case DERROTA: derrota(); break;
            case VICTORIA: derrota(); break;
            case RESULTADO_VERSUS: resultadoVersus(); break;
        }
        if (tiempoMensaje > 0) {
            float y = estado == Estado.JUGANDO ? 46 : 8;
            vista.panel(220, y, 520, 28, "23354E");
            vista.centrar(mensaje, 220, 520, y + 19, 1, "FFE1A1");
        }
    }
    private void actualizar(float delta) {
        if (modoVersus) {
            int vidasAntes = versus.getUno().getPersonaje().getVidas() + versus.getDos().getPersonaje().getVidas();
            versus.actualizar(delta);
            if (configuracion.tieneSonido() && vidasAntes > versus.getUno().getPersonaje().getVidas() + versus.getDos().getPersonaje().getVidas()) golpe.play(0.5f);
            if (versus.terminada()) cambiarEstado(Estado.RESULTADO_VERSUS);
            return;
        }
        Personaje jugador = partida.getPersonaje();
        int puntos = jugador.getPuntos(), vidas = jugador.getVidas(), monedas = jugador.getMonedas();
        int nivel = partida.getNumeroNivel();
        partida.actualizar(delta);
        if (configuracion.tieneSonido()) {
            if (jugador.getPuntos() > puntos || jugador.getMonedas() > monedas) recoger.play(0.4f);
            if (jugador.getVidas() < vidas) golpe.play(0.6f);
        }
        if (!partida.esDesafioOculto() && partida.getNumeroNivel() != nivel) {
            List<Aspecto> nuevos = perfil.registrarNivel(partida.getDificultad(), partida.getMaximoNivel());
            if (!nuevos.isEmpty()) avisar("Personaje desbloqueado: " + nuevos.get(0).getNombre());
        }
        if (partida.terminada()) {
            guardarResultado(true);
            cambiarEstado(partida.gano() ? Estado.VICTORIA : Estado.DERROTA);
            if (partida.gano() && configuracion.tieneSonido()) sonidoVictoria.play(0.5f);
        }
    }
    private void cabecera(String titulo, String subtitulo) {
        vista.panel(0, 530, 960, 70, "0A1526");
        vista.textoAcotado(titulo, 32, 579, 650, 1.4f, "E4EDF5");
        vista.textoAcotado(subtitulo, 32, 551, 650, 0.95f, "8FA8BC");
        vista.centrar("MONEDAS  " + perfil.getMonedas(), 712, 224, 574, 1.1f, "FFD36A");
    }
    private void menu() {
        cabecera(TITULO, "Proyecto de Programación Avanzada");
        if (vista.botonOculto(3, 570)) {
            modoVersus = false; modoOculto = true; iniciar(); return;
        }
        vista.panel(40, 65, 420, 430, "0C192C");
        vista.texto("Una gota, otra oportunidad", 75, 458, 1.35f, "E4EDF5");
        vista.texto("Recoge agua para seguir con vida.", 75, 429, 1.05f, "67DCCE");
        vista.texto("Un OVNI contaminó la lluvia. Rescata el agua.", 75, 403, 0.95f, "9BB0C2");
        if (vista.boton("COMENZAR PARTIDA", 75, 308, 350, 54, true)) { modoOculto = false; modoVersus = false; cambiarEstado(Estado.DIFICULTAD); return; }
        if (vista.boton("TIENDA Y PERSONAJES", 75, 240, 350, 52, false)) { cambiarEstado(Estado.TIENDA); return; }
        if (vista.boton("MEJORES PUNTUACIONES", 75, 174, 350, 52, false)) { cambiarEstado(Estado.PUNTUACIONES); return; }
        if (vista.boton("GUÍA DE OBJETOS", 75, 108, 350, 48, false)) { cambiarEstado(Estado.AYUDA); return; }
        if (vista.boton("VERSUS LOCAL / 2 JUGADORES", 500, 107, 420, 40, true)) { modoOculto = false; modoVersus = true; cambiarEstado(Estado.DIFICULTAD); return; }
        vista.panel(500, 160, 420, 335, "16273C");
        vista.centrar("TU PERSONAJE", 500, 420, 466, 1, "91A9BC");
        vista.personaje(perfil.getEquipado(), 660, 252, 6, reloj, true, false);
        vista.centrar(perfil.getEquipado().getNombre(), 500, 420, 221, 1.3f, "E4EDF5");
        vista.centrar("Aspectos cosméticos, mismas habilidades", 500, 420, 189, 0.95f, "91A9BC");
        if (vista.boton(tactil ? "CONTROL: TÁCTIL / RATÓN" : "CONTROL: TECLADO", 500, 61, 420, 36, false)) tactil = !tactil;
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SÍ" : "SONIDO: NO", 630, 17, 160, 34, false)) configuracion.alternarSonido();
    }
    private void dificultades() {
        cabecera(modoVersus ? "VERSUS / ELIGE DIFICULTAD" : "ELIGE TU DIFICULTAD", "Cada 100 puntos de agua subes de nivel. Los peligros pueden hacerte retroceder.");
        for (Dificultad opcion : Dificultad.values()) {
            int i = opcion.ordinal(); float x = 42 + i * 300;
            vista.panel(x, 218, 276, 252, opcion == dificultad ? "21473F" : "13253A");
            vista.texto("0" + (i + 1), x + 24, 444, 1.2f, "67DCCE");
            vista.texto(opcion.name(), x + 24, 404, 2, "E4EDF5");
            vista.texto(i == 0 ? "Ritmo suave" : i == 1 ? "Lluvia rápida" : "Peligro constante", x + 24, 359, 1.1f, "A3B8C8");
            vista.texto(opcion.getVidas() + " vidas / Mejor nivel: " + perfil.getNivelMaximo(opcion), x + 24, 321, 1, "A3B8C8");
            if (vista.boton(opcion == dificultad ? "SELECCIONADO" : "ELEGIR", x + 20, 242, 236, 48, opcion == dificultad)) dificultad = opcion;
        }
        vista.centrar(modoVersus ? "Gana quien sobreviva al rival o venza primero al OVNI. J1: A/D + W. J2: flechas + arriba." : "30 niveles / Llanura, hielo y Atacama / Jefe OVNI al completar 3000 puntos.", 0, 960, 184, 0.95f, "A3B8C8");
        if (vista.boton("JUGAR " + dificultad, 330, 103, 300, 54, true)) { confirmarDificultad(); return; }
        if (vista.boton("VOLVER", 42, 43, 160, 40, false)) cambiarEstado(Estado.MENU);
    }
    private void prologo() {
        String[] escena = PROLOGO[paginaPrologo];
        cabecera("ANTES DE LA LLUVIA", "PRÓLOGO / " + (paginaPrologo + 1) + " DE " + PROLOGO.length);
        vista.panel(48, 150, 864, 350, "0C192C");
        vista.panel(70, 179, 245, 286, "16273C");
        vista.centrar("LUMA", 70, 245, 439, 1.2f, "67DCCE");
        vista.personaje(Aspecto.LUMA, 146, 252, 6, reloj, false, false);
        vista.texto(escena[0], 344, 437, 1.55f, "67DCCE");
        for (int i = 1; i < escena.length; i++) {
            vista.textoAcotado(escena[i], 344, 385 - (i - 1) * 42, 536, 1.13f, "E4EDF5");
        }
        boolean ultima = paginaPrologo == PROLOGO.length - 1;
        if (vista.boton("VOLVER", 48, 70, 180, 48, false)) cambiarEstado(Estado.DIFICULTAD);
        else if (vista.boton("SALTAR HISTORIA", 390, 70, 236, 48, false)) iniciar();
        else if (vista.boton(ultima ? "EMPEZAR MISIÓN" : "CONTINUAR", 644, 70, 268, 48, true)) avanzarPrologo();
        vista.centrar("Enter: avanzar / Esc: dificultad", 0, 960, 44, 0.9f, "E4EDF5");
    }

    private void tienda() {
        cabecera("TIENDA / VESTUARIO", "Compra con monedas o desbloquea llegando a niveles altos.");
        vista.panel(24, 93, 204, 415, "0C192C");
        vista.centrar("EQUIPADO", 24, 204, 482, 1, "91A9BC");
        vista.personaje(perfil.getEquipado(), 83, 318, 5, reloj, false, false);
        vista.centrar(perfil.getEquipado().getNombre(), 24, 204, 280, 0.95f, "E4EDF5");
        vista.centrar("Solo cambia la apariencia.", 24, 204, 241, 0.85f, "91A9BC");
        vista.centrar("Vidas según dificultad.", 24, 204, 219, 0.85f, "91A9BC");
        vista.centrar("Tus compras se guardan", 24, 204, 158, 0.85f, "91A9BC");
        vista.centrar("en este dispositivo.", 24, 204, 138, 0.85f, "91A9BC");
        Aspecto[] catalogo = java.util.Arrays.stream(Aspecto.values())
                .filter(a -> !a.esOculto() || perfil.tiene(a)).toArray(Aspecto[]::new);
        int paginas = (catalogo.length + 5) / 6;
        for (int casilla = 0; casilla < 6; casilla++) {
            int indice = paginaTienda * 6 + casilla;
            if (indice >= catalogo.length) break;
            Aspecto aspecto = catalogo[indice];
            float x = 246 + (casilla % 3) * 234, y = 302 - (casilla / 3) * 218;
            vista.panel(x, y, 218, 206, perfil.getEquipado() == aspecto ? "21473F" : "172A40");
            vista.centrar(aspecto.getNombre(), x, 218, y + 187, 1, "E4EDF5");
            vista.personaje(aspecto, x + 86, y + 77, 3.6f, reloj, false, false);
            boolean tiene = perfil.tiene(aspecto);
            vista.centrar(tiene ? "EN TU COLECCION" : aspecto.getRequisito(), x, 218, y + 62, 0.95f, "FFD36A");
            String accion = perfil.getEquipado() == aspecto ? "EQUIPADO" : tiene ? "EQUIPAR" : aspecto.esComprable() ? "COMPRAR" : "VER OBJETIVO";
            if (vista.boton(accion, x + 12, y + 10, 194, 37, tiene)) {
                if (tiene) { perfil.equipar(aspecto); avisar("Equipado: " + aspecto.getNombre()); }
                else if (!aspecto.esComprable()) avisar(aspecto.getRequisito() + " para desbloquearlo.");
                else if (perfil.comprar(aspecto)) { perfil.equipar(aspecto); avisar("Compra lista. Equipaste " + aspecto.getNombre()); }
                else avisar("Necesitas " + (aspecto.getPrecio() - perfil.getMonedas()) + " monedas mas.");
                return;
            }
        }
        if (vista.boton("VOLVER", 24, 40, 170, 38, false)) { cambiarEstado(Estado.MENU); return; }
        if (vista.boton("<", 440, 40, 70, 36, false)) paginaTienda = Math.max(0, paginaTienda - 1);
        vista.centrar((paginaTienda + 1) + " / " + paginas, 520, 90, 65, 1, "E4EDF5");
        if (vista.boton(">", 625, 40, 70, 36, false)) paginaTienda = Math.min(paginas - 1, paginaTienda + 1);
    }
    private void puntuaciones() {
        cabecera("MEJORES PUNTUACIONES", "Top 5 local por dificultad. Solo cuentan las partidas terminadas.");
        for (Dificultad opcion : Dificultad.values()) {
            if (vista.boton(opcion.name(), 150 + opcion.ordinal() * 225, 448, 210, 46, dificultad == opcion)) dificultad = opcion;
        }
        vista.panel(150, 125, 660, 300, "0C192C");
        vista.texto("POSICIÓN", 185, 398, 1, "91A9BC");
        vista.texto("PUNTUACIÓN", 602, 398, 1, "91A9BC");
        int[] top = perfil.getPuntuaciones(dificultad);
        for (int i = 0; i < top.length; i++) {
            float y = 357 - i * 45;
            vista.panel(170, y - 28, 620, 37, i == 0 ? "294334" : "172A40");
            vista.texto("0" + (i + 1), 185, y, 1.2f, "FFD36A");
            vista.texto(top[i] == 0 ? "Sin partida registrada" : "Superviviente / " + dificultad, 250, y, 1, "D6E4ED");
            vista.texto(top[i] == 0 ? "---" : String.valueOf(top[i]), 664, y, 1.2f, "E4EDF5");
        }
        vista.centrar("Mayor nivel alcanzado: " + perfil.getNivelMaximo(dificultad), 150, 660, 98, 1.1f, "A3B8C8");
        if (vista.boton("VOLVER", 32, 40, 170, 40, false)) cambiarEstado(Estado.MENU);
    }
    private void ayuda() {
        cabecera("GUÍA / AGUA Y SUPERVIVENCIA", "Tras 4 s pierdes velocidad. A los 12 s empieza el daño por sed.");
        vista.panel(20, 61, 920, 400, "13283C");
        for (Zona zona : new Zona[]{Zona.LLANURA, Zona.HIELO, Zona.DESIERTO, Zona.ESPACIO}) {
            if (vista.boton(zona.getNombre(), 32 + zona.ordinal() * 228, 468, 216, 38, zonaGuia == zona)) zonaGuia = zona;
        }
        vista.texto("Agua doble: quita la fatiga y suma 2 cargas de agua y de arma.", 32, 450, 0.95f, "E4EDF5");
        java.util.LinkedHashSet<TipoObjeto> tipos = new java.util.LinkedHashSet<>();
        java.util.Collections.addAll(tipos, zonaGuia.peligros());
        java.util.Collections.addAll(tipos, zonaGuia.mejoras());
        tipos.add(zonaGuia.arma());
        int i = 0;
        for (TipoObjeto tipo : tipos) {
            float y = 412 - i * 30;
            vista.objeto(tipo, 40, y - 22);
            vista.textoAcotado(tipo.getNombre(), 95, y, 200, 0.95f, "E4EDF5");
            vista.textoAcotado(tipo.getDescripcion(), 310, y, 600, 0.95f, "E4EDF5");
            i++;
        }
        vista.texto("Espacio o clic: dispara a patrullas y al jefe. Versus: J1 A/D y W; J2 izquierda/derecha y arriba.", 32, 72, 0.95f, "E4EDF5");
        if (vista.boton("VOLVER", 32, 20, 170, 36, false)) cambiarEstado(Estado.MENU);
    }
    private void dibujarPartida() {
        if (modoVersus) {
            Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_SCISSOR_TEST);
            int mitad = viewport.getScreenWidth() / 2;
            Gdx.gl.glScissor(viewport.getScreenX(), viewport.getScreenY(), mitad, viewport.getScreenHeight());
            vista.origen(0);
            dibujarMundo(versus.getUno(), perfil.getEquipado(), "J1", true);
            vista.origen(480);
            Gdx.gl.glScissor(viewport.getScreenX() + mitad, viewport.getScreenY(), viewport.getScreenWidth() - mitad, viewport.getScreenHeight());
            dibujarMundo(versus.getDos(), Aspecto.EXPLORADOR, "J2", true);
            vista.origen(0);
            Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_SCISSOR_TEST);
            vista.panel(478, 0, 4, 600, "0A1526");
            if (vista.boton("PAUSA", 430, 12, 100, 28, false)) cambiarEstado(Estado.PAUSA);
        } else {
            dibujarMundo(partida, perfil.getEquipado(), "", false);
            if (vista.boton(tactil ? "TÁCTIL [T]" : "TECLADO [T]", 635, 548, 175, 40, false)) alternarControlEnPartida();
            if (vista.boton("PAUSA", 825, 548, 110, 40, false)) cambiarEstado(Estado.PAUSA);
        }
    }
    private void dibujarMundo(Partida juego, Aspecto aspecto, String etiqueta, boolean mini) {
        Personaje jugador = juego.getPersonaje();
        float ancho = juego.getAncho();
        vista.fondo(reloj, juego.esSecreto(), juego.getZona(), ancho);
        for (Gota gota : juego.getLluvia().getGotas()) vista.gota(gota);
        float altura = jugador.getY();
        if (juego.getFase() == Partida.Fase.ASCENSO) {
            vista.ovni(jugador.getX() - 30, 438, reloj);
            vista.rayo(jugador.getX() + 24, juego.getAscenso());
            vista.centrar("Recupera el hidro-plasma si aparece", 0, ancho, 342, 0.95f, "102334");
            vista.centrar("EL OVNI TE ATRAPÓ. USA EL AGUA RECOLECTADA", 0, ancho, 370, mini ? 1 : 1.5f, "102334");
        }
        SubordinadoOvni patrulla = juego.getPatrulla();
        if (patrulla != null) vista.dron(patrulla.getX(), patrulla.getY(), patrulla.getAtaque());
        for (ChorroAgua chorro : juego.getChorros()) {
            float anchoChorro = chorro.esCargado() ? 14 : 10;
            vista.panel(chorro.getX(), chorro.getY(), anchoChorro, 24, chorro.getArma().getColor());
        }
        if (juego.enJefe()) {
            JefeOvni jefe = juego.getJefe();
            vista.ovni(jefe.getX(), jefe.getY(), reloj);
            for (SubordinadoOvni dron : jefe.getSubordinados()) vista.dron(dron.getX(), dron.getY(), dron.getAtaque());
            vista.panel(20, 493, ancho - 40, 12, "34415E");
            vista.panel(20, 493, (ancho - 40) * jefe.getVida() / jefe.getVidaMaxima(), 12, "C779BE");
            String fase = jefe.tieneEscudo() ? "ESCUDO / DRONES " + jefe.getSubordinados().size()
                    : jefe.getFase() == JefeOvni.Fase.SUBORDINADOS ? "ESCUDO CAIDO" : jefe.getFase().name();
            vista.panel(20, 463, ancho - 40, 24, "20354A");
            vista.centrar("OVNI " + jefe.getVida() + "/" + jefe.getVidaMaxima() + " / " + fase,
                    20, ancho - 40, 480, 0.9f, "E4EDF5");
        }
        vista.personaje(aspecto, jugador.getX(), altura, 3.4f, reloj, jugador.estaMoviendo(), jugador.estaHerido() && (int) (reloj * 10) % 2 == 0);
        vista.panel(0, 528, ancho, 72, "0A1526");
        float anchoHud = mini ? ancho - 32 : 595;
        vista.textoAcotado(etiqueta + " " + juego.getDificultad() + " / " + juego.getZona().getNombre(), 16, 586, anchoHud, mini ? 1 : 1.25f, "67DCCE");
        vista.textoAcotado(juego.esDesafioOculto() ? "DESAFÍO OCULTO / VIDAS " + jugador.getVidas() : "NIVEL " + juego.getNumeroNivel() + "/30    " + jugador.getPuntos() + "/" + juego.getObjetivo() + " pts    VIDAS " + jugador.getVidas(), 16, 570, anchoHud, mini ? 0.94f : 1.05f, "E4EDF5");
        vista.textoAcotado("MONEDAS " + jugador.getMonedas() + "    AGUA " + jugador.getAgua() + (jugador.getSinAgua() > 4 ? "    SED: recoge agua" : ""), 16, 554, anchoHud, mini ? 0.85f : 0.95f, jugador.getSinAgua() > 4 ? "FFB77C" : "FFD36A");
        TipoObjeto iconoArma = jugador.getArma().getIcono();
        if (iconoArma != null) vista.objetoReducido(iconoArma, 16, 530, 0.38f);
        vista.textoAcotado("ARMA: " + jugador.getArma().getNombre(), 34, 540, mini ? 180 : 300, 0.85f, jugador.getArma().getColor());
        if (jugador.getArma() != ArmaBioma.AGUA) {
            float carga = jugador.getCargaArma() / (float) Personaje.CARGA_MAXIMA;
            float xCarga = mini ? 225 : 365;
            vista.panel(xCarga, 532, 65, 6, "34415E");
            if (carga > 0) vista.panel(xCarga, 532, 65 * carga, 6, jugador.getArma().getColor());
            vista.textoAcotado(carga == 1 ? "LISTA" : "CARGA " + jugador.getCargaArma() + "/6",
                    xCarga + 75, 540, 100, 0.85f, "E4EDF5");
        }
        vista.panel(16, 516, ancho - 32, 5, "23354E");
        vista.panel(16, 516, (ancho - 32) * juego.getProgreso(), 5, "67DCCE");
        if (juego.esSecreto()) vista.centrar("SOLO MONEDAS / " + (int) Math.ceil(juego.getTiempoSecreto()) + " s", 0, ancho, 461, mini ? 1 : 1.3f, "102334");
        else if (juego.getAvisoNivel() > 0 && juego.getFase() == Partida.Fase.CAMPANA)
            vista.centrar("NIVEL " + juego.getNumeroNivel() + " / " + juego.getZona().getNombre(), 0, ancho, 350, mini ? 1.3f : 2, "102334");
        if (!mini && !juego.esSecreto() && juego.getAvisoNivel() > 0 && juego.getFase() == Partida.Fase.CAMPANA) {
            String pista;
            switch (juego.getZona()) {
                case HIELO: pista = "La espada de hielo ralentiza a la nave. Cuidado al deslizarte."; break;
                case DESIERTO: pista = "Busca el láser solar del oasis. Compensa el viento."; break;
                default: pista = "Azul: agua limpia. Esquiva la lluvia contaminada.";
            }
            vista.centrar(pista, 0, ancho, 318, 1, "102334");
        }
        int fila = 0;
        for (TipoObjeto tipo : TipoObjeto.values()) if (jugador.activo(tipo)) {
            float x = 16 + (fila % 3) * 152;
            float y = 441 - (fila / 3) * 24;
            vista.panel(x, y, 144, 21, "20354A");
            vista.textoAcotado(tipo.getNombre() + " " + (int) Math.ceil(jugador.tiempoRestante(tipo)) + "s",
                    x + 8, y + 16, 128, 0.9f, "E4EDF5");
            fila++;
        }
        String consejo = mini ? (etiqueta.equals("J1") ? "A/D: mover | W: disparar" : "Flechas: mover | Arriba: disparar")
                : (juego.enJefe() || patrulla != null) ? "Espacio o clic: dispara | Carga llena: próximo disparo especial"
                : tactil ? "Manten pulsado para moverte | Recoge agua antes de 4 segundos"
                : "A/D o flechas: mover | Recoge agua antes de 4 segundos";
        int xPie = mini && etiqueta.equals("J2") ? 60 : 16;
        float anchoPie = ancho - (mini ? 76 : 32);
        vista.panel(xPie, 12, anchoPie, 30, "0A1526");
        vista.centrar(consejo, xPie, anchoPie, 32, mini ? 0.8f : 0.95f, "E4EDF5");
        if (!mini && avisoControl > 0) {
            float y = juego.enJefe() ? 431 : 468;
            vista.panel(635, y, 300, 26, "20354A");
            vista.centrar(tactil ? "Control activo: táctil / ratón" : "Control activo: teclado",
                    635, 300, y + 18, 0.95f, "FFE1A1");
        }
        if (!jugador.getAviso().isEmpty() && !mini) {
            vista.panel(210, 77, 540, 25, "20354A");
            vista.centrar(jugador.getAviso(), 210, 540, 95, 0.95f, "FFE1A1");
        }
    }
    private void resultadoVersus() {
        vista.panel(210, 115, 540, 380, "0C192C");
        String ganador = versus.getGanador() == 0 ? "EMPATE" : "GANA EL JUGADOR " + versus.getGanador();
        vista.centrar(ganador, 210, 540, 452, 1.8f, "FFD36A");
        vista.centrar("J1: " + versus.getUno().getPersonaje().getPuntos() + " puntos / nivel " + versus.getUno().getNumeroNivel(), 210, 540, 401, 1.1f, "E4EDF5");
        vista.centrar("J2: " + versus.getDos().getPersonaje().getPuntos() + " puntos / nivel " + versus.getDos().getNumeroNivel(), 210, 540, 363, 1.1f, "E4EDF5");
        vista.centrar("Partida local: no modifica el progreso de campaña.", 210, 540, 323, 0.95f, "91A9BC");
        if (vista.boton("REVANCHA", 300, 235, 360, 52, true)) { iniciar(); return; }
        if (vista.boton("VOLVER AL MENÚ", 300, 165, 360, 48, false)) cambiarEstado(Estado.MENU);
    }
    private void pausa() {
        vista.panel(210, 120, 540, 370, "0C192C");
        vista.centrar("UN RESPIRO", 210, 540, 446, 2, "E4EDF5");
        vista.centrar("El tiempo y los efectos estan detenidos.", 210, 540, 399, 1, "91A9BC");
        if (vista.boton("CONTINUAR", 300, 305, 360, 54, true)) { cambiarEstado(Estado.JUGANDO); return; }
        if (vista.boton(modoOculto ? "SALIR DEL DESAFÍO" : modoVersus ? "ABANDONAR VERSUS" : "GUARDAR MONEDAS Y SALIR", 300, 237, 360, 50, false)) { abandonar(); return; }
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SÍ" : "SONIDO: NO", 300, 169, 360, 48, false)) configuracion.alternarSonido();
        vista.centrar(modoOculto ? "El desafío solo desbloquea un aspecto al ganar." : modoVersus ? "Versus no modifica monedas ni ranking de campaña." : "Salir no registra puntuación en el ranking.", 210, 540, 145, 0.9f, "91A9BC");
    }
    private void derrota() {
        if (nuevoRecord && tiempoRecord < 6) vista.confeti(tiempoRecord);
        vista.panel(230, 115, 500, 380, "0C192C");
        float escala = nuevoRecord && tiempoRecord < 6 ? 1.8f + 0.1f * (float) Math.sin(tiempoRecord * 6) : 1.8f;
        vista.centrar(partida.gano() ? "VICTORIA / OVNI DERROTADO" : nuevoRecord ? "NUEVO RÉCORD" : "FIN DE LA PARTIDA", 230, 500, 451, partida.gano() ? 1.5f : escala, nuevoRecord ? "FFD36A" : "E4EDF5");
        Personaje jugador = partida.getPersonaje();
        vista.centrar(partida.esDesafioOculto() ? "ENCUENTRO SECRETO" : partida.getDificultad() + " / NIVEL " + partida.getNumeroNivel(), 230, 500, 405, 1.2f, "67DCCE");
        vista.centrar(jugador.getPuntos() + " puntos", 230, 500, 366, 1.5f, "E4EDF5");
        vista.centrar(partida.esDesafioOculto() ? (partida.gano() ? "ALIEN DESBLOQUEADO EN LA TIENDA" : "Vence al OVNI para revelar su secreto") : "+" + partida.getMonedasFinales() + " monedas guardadas", 230, 500, 326, 1.1f, "FFD36A");
        if (partida.gano() && !partida.esDesafioOculto()) vista.centrar("Bolsa del jefe: " + partida.getPremio() + " monedas", 230, 500, 309, 0.9f, "FFD36A");
        if (vista.boton("OTRA PARTIDA", 300, 240, 360, 52, true)) { iniciar(); return; }
        if (vista.boton("VOLVER AL MENÚ", 300, 173, 360, 48, false)) cambiarEstado(Estado.MENU);
    }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() { if (estado == Estado.JUGANDO) cambiarEstado(Estado.PAUSA); }
    @Override public void dispose() {
        guardarResultado(false);
        musica.dispose(); musicaJefe.dispose(); sonidoChorro.dispose(); sonidoVictoria.dispose(); recoger.dispose(); golpe.dispose(); vista.dispose();
    }
}
