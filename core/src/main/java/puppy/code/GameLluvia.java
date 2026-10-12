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
    private enum Estado { MENU, DIFICULTAD, TIENDA, PUNTUACIONES, AYUDA, JUGANDO, PAUSA, DERROTA, VICTORIA, RESULTADO_VERSUS }
    private Estado estado = Estado.MENU;
    private final FitViewport viewport = new FitViewport(960, 600, new OrthographicCamera());
    private VistaJuego vista;
    private ConfiguracionJuego configuracion;
    private PerfilJugador perfil;
    private Partida partida;
    private PartidaVersus versus;
    private boolean modoVersus;
    private Dificultad dificultad = Dificultad.NOOB;
    private Music musica;
    private Music musicaJefe;
    private Sound sonidoChorro, sonidoVictoria;
    private Sound recoger, golpe;
    private boolean tactil, resultadoGuardado, nuevoRecord;
    private float reloj, tiempoRecord, tiempoMensaje;
    private String mensaje = "";
    private int paginaTienda;
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
    private void iniciar() {
        if (modoVersus) {
            versus = new PartidaVersus(new ControlTeclado(Input.Keys.A, Input.Keys.D),
                    new ControlTeclado(Input.Keys.LEFT, Input.Keys.RIGHT), dificultad, System.nanoTime());
            partida = versus.getUno();
        } else {
            versus = null;
            partida = new Partida(tactil ? new ControlTactil(viewport) : new ControlTeclado(), dificultad);
        }
        resultadoGuardado = false;
        nuevoRecord = false;
        mensaje = "";
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
                if (aceptar) { modoVersus = false; cambiarEstado(Estado.DIFICULTAD); }
                if (Gdx.input.isKeyJustPressed(Input.Keys.T)) tactil = !tactil;
                break;
            case DIFICULTAD:
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) dificultad = Dificultad.NOOB;
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) dificultad = Dificultad.PRO;
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) dificultad = Dificultad.HACKER;
                if (aceptar) iniciar();
                else if (volver) cambiarEstado(Estado.MENU);
                break;
            case JUGANDO:
                if (volver || Gdx.input.isKeyJustPressed(Input.Keys.P)) cambiarEstado(Estado.PAUSA);
                else if (modoVersus) {
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) disparar(versus.getUno());
                    if (Gdx.input.isKeyPressed(Input.Keys.UP)) disparar(versus.getDos());
                } else {
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
        if (!juego.enJefe()) return;
        int antes = juego.getJefe().getDisparos();
        juego.disparar();
        if (juego.getJefe().getDisparos() > antes && configuracion.tieneSonido()) sonidoChorro.play(0.4f);
    }
    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        reloj += delta;
        tiempoMensaje = Math.max(0, tiempoMensaje - delta);
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
            vista.panel(120, 6, 720, 30, "23354E");
            vista.centrar(mensaje, 120, 720, 27, 1, "FFE1A1");
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
        if (partida.getNumeroNivel() != nivel) {
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
        vista.texto(titulo, 32, 579, 1.55f, "E4EDF5");
        vista.texto(subtitulo, 32, 551, 0.95f, "8FA8BC");
        vista.texto("MONEDAS  " + perfil.getMonedas(), 734, 574, 1.1f, "FFD36A");
    }
    private void menu() {
        cabecera("GAME / LLUVIA", "ARCADE DE SUPERVIVENCIA");
        vista.panel(40, 65, 420, 430, "0C192C");
        vista.texto("UNA GOTA.", 75, 466, 2.2f, "E4EDF5");
        vista.texto("OTRA OPORTUNIDAD.", 75, 426, 1.6f, "67DCCE");
        vista.texto("Recoge. Sobrevive. Desbloquea.", 75, 388, 1, "9BB0C2");
        if (vista.boton("COMENZAR PARTIDA", 75, 308, 350, 54, true)) { modoVersus = false; cambiarEstado(Estado.DIFICULTAD); return; }
        if (vista.boton("TIENDA Y PERSONAJES", 75, 240, 350, 52, false)) { cambiarEstado(Estado.TIENDA); return; }
        if (vista.boton("MEJORES PUNTUACIONES", 75, 174, 350, 52, false)) { cambiarEstado(Estado.PUNTUACIONES); return; }
        if (vista.boton("GUIA DE OBJETOS", 75, 108, 350, 48, false)) { cambiarEstado(Estado.AYUDA); return; }
        if (vista.boton("VERSUS LOCAL / 2 JUGADORES", 500, 107, 420, 40, true)) { modoVersus = true; cambiarEstado(Estado.DIFICULTAD); return; }
        vista.panel(500, 160, 420, 335, "16273C");
        vista.centrar("TU PERSONAJE", 500, 420, 466, 1, "91A9BC");
        vista.personaje(perfil.getEquipado(), 660, 252, 6, reloj, true, false);
        vista.centrar(perfil.getEquipado().getNombre(), 500, 420, 221, 1.3f, "E4EDF5");
        vista.centrar("Aspectos cosmeticos, mismas habilidades", 500, 420, 189, 0.95f, "91A9BC");
        if (vista.boton(tactil ? "CONTROL: TACTIL / RATON" : "CONTROL: TECLADO", 500, 61, 420, 36, false)) tactil = !tactil;
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SI" : "SONIDO: NO", 630, 17, 160, 34, false)) configuracion.alternarSonido();
    }
    private void dificultades() {
        cabecera(modoVersus ? "VERSUS / ELIGE DIFICULTAD" : "ELIGE TU DIFICULTAD", "Cada 100 puntos de agua subes de nivel. Los peligros pueden hacerte retroceder.");
        for (Dificultad opcion : Dificultad.values()) {
            int i = opcion.ordinal(); float x = 42 + i * 300;
            vista.panel(x, 218, 276, 252, opcion == dificultad ? "21473F" : "13253A");
            vista.texto("0" + (i + 1), x + 24, 444, 1.2f, "67DCCE");
            vista.texto(opcion.name(), x + 24, 404, 2, "E4EDF5");
            vista.texto(i == 0 ? "Ritmo suave" : i == 1 ? "Lluvia rapida" : "Peligro constante", x + 24, 359, 1.1f, "A3B8C8");
            vista.texto("Mejor nivel: " + perfil.getNivelMaximo(opcion), x + 24, 321, 1, "A3B8C8");
            if (vista.boton(opcion == dificultad ? "SELECCIONADO" : "ELEGIR", x + 20, 242, 236, 48, opcion == dificultad)) dificultad = opcion;
        }
        vista.centrar(modoVersus ? "Gana quien sobreviva al rival o venza primero al OVNI. J1: A/D + W. J2: flechas + arriba." : "30 niveles / Llanura, hielo y Atacama / Jefe OVNI al completar 3000 puntos.", 0, 960, 184, 0.95f, "A3B8C8");
        if (vista.boton("JUGAR " + dificultad, 330, 103, 300, 54, true)) { iniciar(); return; }
        if (vista.boton("VOLVER", 42, 43, 160, 40, false)) cambiarEstado(Estado.MENU);
    }
    private void tienda() {
        cabecera("TIENDA / VESTUARIO", "Compra con monedas o desbloquea llegando a niveles altos.");
        vista.panel(24, 93, 204, 415, "0C192C");
        vista.centrar("EQUIPADO", 24, 204, 482, 1, "91A9BC");
        vista.personaje(perfil.getEquipado(), 83, 318, 5, reloj, false, false);
        vista.centrar(perfil.getEquipado().getNombre(), 24, 204, 280, 0.95f, "E4EDF5");
        vista.centrar("Solo cambia la apariencia.", 24, 204, 241, 0.85f, "91A9BC");
        vista.centrar("Todos tienen tres vidas.", 24, 204, 219, 0.85f, "91A9BC");
        vista.centrar("Tus compras se guardan", 24, 204, 158, 0.85f, "91A9BC");
        vista.centrar("en este dispositivo.", 24, 204, 138, 0.85f, "91A9BC");
        Aspecto[] catalogo = Aspecto.values();
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
            String accion = perfil.getEquipado() == aspecto ? "EQUIPADO" : tiene ? "EQUIPAR" : aspecto.getDificultad() == null ? "COMPRAR" : "VER OBJETIVO";
            if (vista.boton(accion, x + 12, y + 10, 194, 37, tiene)) {
                if (tiene) { perfil.equipar(aspecto); avisar("Equipado: " + aspecto.getNombre()); }
                else if (aspecto.getDificultad() != null) avisar("Alcanza " + aspecto.getRequisito() + " para desbloquearlo gratis.");
                else if (perfil.comprar(aspecto)) { perfil.equipar(aspecto); avisar("Compra lista. Equipaste " + aspecto.getNombre()); }
                else avisar("Necesitas " + (aspecto.getPrecio() - perfil.getMonedas()) + " monedas mas.");
                return;
            }
        }
        if (vista.boton("VOLVER", 24, 40, 170, 38, false)) { cambiarEstado(Estado.MENU); return; }
        if (vista.boton("<", 440, 40, 70, 36, false)) paginaTienda = Math.max(0, paginaTienda - 1);
        vista.centrar((paginaTienda + 1) + " / 3", 520, 90, 65, 1, "E4EDF5");
        if (vista.boton(">", 625, 40, 70, 36, false)) paginaTienda = Math.min(2, paginaTienda + 1);
    }
    private void puntuaciones() {
        cabecera("MEJORES PUNTUACIONES", "Top 5 local por dificultad. Solo cuentan las partidas terminadas.");
        for (Dificultad opcion : Dificultad.values()) {
            if (vista.boton(opcion.name(), 150 + opcion.ordinal() * 225, 448, 210, 46, dificultad == opcion)) dificultad = opcion;
        }
        vista.panel(150, 125, 660, 300, "0C192C");
        vista.texto("POSICION", 185, 398, 1, "91A9BC");
        vista.texto("PUNTUACION", 602, 398, 1, "91A9BC");
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
        cabecera("GUIA / AGUA Y SUPERVIVENCIA", "Cuatro segundos sin agua: pierdes velocidad. Recoger agua te recupera y carga tu arma.");
        vista.panel(20, 61, 920, 400, "13283C");
        for (Zona zona : new Zona[]{Zona.LLANURA, Zona.HIELO, Zona.DESIERTO, Zona.ESPACIO}) {
            if (vista.boton(zona.getNombre(), 32 + zona.ordinal() * 228, 468, 216, 38, zonaGuia == zona)) zonaGuia = zona;
        }
        vista.texto("Agua: +10 puntos y 1 carga. Cada 100 puntos, otro nivel. Los peligros restan progreso.", 32, 450, 0.95f, "E4EDF5");
        java.util.LinkedHashSet<TipoObjeto> tipos = new java.util.LinkedHashSet<>();
        java.util.Collections.addAll(tipos, zonaGuia.peligros());
        java.util.Collections.addAll(tipos, zonaGuia.mejoras());
        int i = 0;
        for (TipoObjeto tipo : tipos) {
            float y = 401 - i * 34;
            vista.objeto(tipo, 40, y - 22);
            vista.texto(tipo.getNombre(), 95, y, 0.95f, "E4EDF5");
            vista.texto(tipo.getDescripcion(), 310, y, 0.95f, "E4EDF5");
            i++;
        }
        vista.texto("Jefe: espacio o clic para disparar. Versus: J1 A/D y W; J2 izquierda/derecha y arriba.", 32, 72, 0.95f, "E4EDF5");
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
            if (vista.boton("PAUSA", 441, 0, 78, 30, false)) cambiarEstado(Estado.PAUSA);
        } else {
            dibujarMundo(partida, perfil.getEquipado(), "", false);
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
            altura += juego.getAscenso() * 290;
            vista.centrar("EL OVNI TE LLEVA AL ESPACIO", 0, ancho, 370, mini ? 1 : 1.5f, "102334");
        }
        if (juego.enJefe()) {
            JefeOvni jefe = juego.getJefe();
            vista.ovni(jefe.getX(), jefe.getY(), reloj);
            for (ChorroAgua chorro : jefe.getChorros()) {
                vista.panel(chorro.getX(), chorro.getY(), 10, 24, "82EBFF");
                vista.panel(chorro.getX() + 3, chorro.getY() + 4, 3, 18, "E6FAFF");
            }
            vista.panel(20, 493, ancho - 40, 12, "34415E");
            vista.panel(20, 493, (ancho - 40) * jefe.getVida() / jefe.getVidaMaxima(), 12, "C779BE");
            vista.centrar("OVNI " + jefe.getVida() + "/" + jefe.getVidaMaxima() + (jefe.getVida() <= jefe.getVidaMaxima() / 2 ? " / FURIA" : ""), 0, ancho, 485, 0.9f, "E4EDF5");
        }
        vista.personaje(aspecto, jugador.getX(), altura, 3.4f, reloj, jugador.estaMoviendo(), jugador.estaHerido() && (int) (reloj * 10) % 2 == 0);
        vista.panel(0, 528, ancho, 72, "0A1526");
        vista.texto(etiqueta + " " + juego.getDificultad() + " / " + juego.getZona().getNombre(), 16, 583, mini ? 1 : 1.25f, "67DCCE");
        vista.texto("NIVEL " + juego.getNumeroNivel() + "/30    " + jugador.getPuntos() + "/" + juego.getObjetivo() + " pts    VIDAS " + jugador.getVidas(), 16, 559, mini ? 0.94f : 1.05f, "E4EDF5");
        vista.texto("MONEDAS " + jugador.getMonedas() + "    AGUA " + jugador.getAgua() + (jugador.getSinAgua() > 4 ? "    SED: recoge agua!" : ""), 16, 539, mini ? 0.85f : 0.95f, jugador.getSinAgua() > 4 ? "FFB77C" : "FFD36A");
        vista.panel(16, 516, ancho - 32, 5, "23354E");
        vista.panel(16, 516, (ancho - 32) * juego.getProgreso(), 5, "67DCCE");
        if (juego.esSecreto()) vista.centrar("SOLO MONEDAS / " + (int) Math.ceil(juego.getTiempoSecreto()) + " s", 0, ancho, 461, mini ? 1 : 1.3f, "102334");
        else if (juego.getAvisoNivel() > 0 && juego.getFase() == Partida.Fase.CAMPANA)
            vista.centrar("NIVEL " + juego.getNumeroNivel() + " / " + juego.getZona().getNombre(), 0, ancho, 350, mini ? 1.3f : 2, "102334");
        int fila = 0;
        for (TipoObjeto tipo : TipoObjeto.values()) if (jugador.activo(tipo)) {
            vista.panel(10, 437 - fila * 22, mini ? 210 : 240, 22, "20354A");
            vista.texto(tipo.getNombre() + " " + (int) Math.ceil(jugador.tiempoRestante(tipo)) + "s", 17, 453 - fila * 22, 0.9f, "E4EDF5");
            fila++;
        }
        String consejo = mini ? (etiqueta.equals("J1") ? "A/D: mover | W: disparar" : "Flechas: mover | Arriba: disparar")
                : juego.enJefe() ? "ESPACIO o clic: chorro de agua | Recoge gotas para recargar"
                : "A/D o flechas: mover | Recoge agua antes de 4 segundos";
        vista.centrar(consejo, 0, ancho, 22, mini ? 0.8f : 0.95f, "E4EDF5");
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
        vista.centrar("Partida local: no modifica el progreso de campana.", 210, 540, 323, 0.95f, "91A9BC");
        if (vista.boton("REVANCHA", 300, 235, 360, 52, true)) { iniciar(); return; }
        if (vista.boton("VOLVER AL MENU", 300, 165, 360, 48, false)) cambiarEstado(Estado.MENU);
    }
    private void pausa() {
        vista.panel(230, 120, 500, 370, "0C192C");
        vista.centrar("UN RESPIRO", 230, 500, 446, 2, "E4EDF5");
        vista.centrar("El tiempo y los efectos estan detenidos.", 230, 500, 399, 1, "91A9BC");
        if (vista.boton("CONTINUAR", 300, 305, 360, 54, true)) { cambiarEstado(Estado.JUGANDO); return; }
        if (vista.boton(modoVersus ? "ABANDONAR VERSUS" : "GUARDAR MONEDAS Y SALIR", 300, 237, 360, 50, false)) { abandonar(); return; }
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SI" : "SONIDO: NO", 300, 169, 360, 48, false)) configuracion.alternarSonido();
        vista.centrar(modoVersus ? "Versus no modifica monedas ni ranking de campana." : "Salir no registra puntuacion en el ranking.", 230, 500, 145, 0.9f, "91A9BC");
    }
    private void derrota() {
        if (nuevoRecord && tiempoRecord < 6) vista.confeti(tiempoRecord);
        vista.panel(230, 115, 500, 380, "0C192C");
        float escala = nuevoRecord && tiempoRecord < 6 ? 1.8f + 0.1f * (float) Math.sin(tiempoRecord * 6) : 1.8f;
        vista.centrar(partida.gano() ? "VICTORIA / OVNI DERROTADO" : nuevoRecord ? "NUEVO RECORD" : "FIN DE LA PARTIDA", 230, 500, 451, partida.gano() ? 1.5f : escala, nuevoRecord ? "FFD36A" : "E4EDF5");
        Personaje jugador = partida.getPersonaje();
        vista.centrar(partida.getDificultad() + " / NIVEL " + partida.getNumeroNivel(), 230, 500, 405, 1.2f, "67DCCE");
        vista.centrar(jugador.getPuntos() + " puntos", 230, 500, 366, 1.5f, "E4EDF5");
        vista.centrar("+" + partida.getMonedasFinales() + " monedas guardadas", 230, 500, 326, 1.1f, "FFD36A");
        if (partida.gano()) vista.centrar("Bolsa del jefe: " + partida.getPremio() + " monedas", 230, 500, 309, 0.9f, "FFD36A");
        if (vista.boton("OTRA PARTIDA", 300, 240, 360, 52, true)) { iniciar(); return; }
        if (vista.boton("VOLVER AL MENU", 300, 173, 360, 48, false)) cambiarEstado(Estado.MENU);
    }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() { if (estado == Estado.JUGANDO) cambiarEstado(Estado.PAUSA); }
    @Override public void dispose() {
        guardarResultado(false);
        musica.dispose(); musicaJefe.dispose(); sonidoChorro.dispose(); sonidoVictoria.dispose(); recoger.dispose(); golpe.dispose(); vista.dispose();
    }
}
