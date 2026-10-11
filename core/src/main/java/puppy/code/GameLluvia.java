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
    private enum Estado { MENU, DIFICULTAD, TIENDA, PUNTUACIONES, AYUDA, JUGANDO, PAUSA, DERROTA }
    private Estado estado = Estado.MENU;
    private final FitViewport viewport = new FitViewport(960, 600, new OrthographicCamera());
    private VistaJuego vista;
    private ConfiguracionJuego configuracion;
    private PerfilJugador perfil;
    private Partida partida;
    private Dificultad dificultad = Dificultad.NOOB;
    private Music musica;
    private Sound recoger, golpe;
    private boolean tactil, resultadoGuardado, nuevoRecord;
    private float reloj, tiempoRecord, tiempoMensaje;
    private String mensaje = "";
    private int paginaTienda;

    @Override public void create() {
        vista = new VistaJuego();
        configuracion = ConfiguracionJuego.getInstancia();
        perfil = configuracion.getPerfil();
        tactil = Gdx.app.getType() == Application.ApplicationType.Android;
        musica = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        musica.setLooping(true); musica.setVolume(0.25f);
        recoger = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        golpe = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }
    private void iniciar() {
        partida = new Partida(tactil ? new ControlTactil(viewport) : new ControlTeclado(), dificultad);
        resultadoGuardado = false;
        nuevoRecord = false;
        mensaje = "";
        cambiarEstado(Estado.JUGANDO);
    }
    private void cambiarEstado(Estado nuevo) {
        estado = nuevo;
        if (nuevo == Estado.JUGANDO && configuracion.tieneSonido()) musica.play();
        else musica.pause();
    }
    private void avisar(String texto) { mensaje = texto; tiempoMensaje = 4; }
    private void guardarResultado(boolean completa) {
        if (partida == null || resultadoGuardado) return;
        Personaje jugador = partida.getPersonaje();
        perfil.registrarNivel(partida.getDificultad(), partida.getNumeroNivel());
        nuevoRecord = perfil.guardarResultado(partida.getDificultad(), jugador.getPuntos(), jugador.getMonedas(), completa);
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
                if (aceptar) cambiarEstado(Estado.DIFICULTAD);
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
                break;
            case PAUSA:
                if (aceptar || volver || Gdx.input.isKeyJustPressed(Input.Keys.P)) cambiarEstado(Estado.JUGANDO);
                break;
            case DERROTA:
                if (aceptar) iniciar();
                else if (volver) cambiarEstado(Estado.MENU);
                break;
            default:
                if (volver) cambiarEstado(Estado.MENU);
        }
    }
    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        reloj += delta;
        tiempoMensaje = Math.max(0, tiempoMensaje - delta);
        entrada();
        if (estado == Estado.JUGANDO) actualizar(delta);
        if (estado == Estado.DERROTA) tiempoRecord += delta;
        ScreenUtils.clear(0.025f, 0.04f, 0.07f, 1);
        vista.preparar(viewport);
        vista.fondo(reloj, partida != null && partida.esSecreto() && estado == Estado.JUGANDO);
        switch (estado) {
            case MENU: menu(); break;
            case DIFICULTAD: dificultades(); break;
            case TIENDA: tienda(); break;
            case PUNTUACIONES: puntuaciones(); break;
            case AYUDA: ayuda(); break;
            case JUGANDO: dibujarPartida(); break;
            case PAUSA: pausa(); break;
            case DERROTA: derrota(); break;
        }
        if (tiempoMensaje > 0) {
            vista.panel(120, 6, 720, 30, "23354E");
            vista.centrar(mensaje, 120, 720, 27, 1, "FFE1A1");
        }
    }
    private void actualizar(float delta) {
        Personaje jugador = partida.getPersonaje();
        int puntos = jugador.getPuntos(), vidas = jugador.getVidas(), monedas = jugador.getMonedas();
        int nivel = partida.getNumeroNivel();
        partida.actualizar(delta);
        if (configuracion.tieneSonido()) {
            if (jugador.getPuntos() > puntos || jugador.getMonedas() > monedas) recoger.play(0.4f);
            if (jugador.getVidas() < vidas) golpe.play(0.6f);
        }
        if (partida.getNumeroNivel() != nivel) {
            List<Aspecto> nuevos = perfil.registrarNivel(partida.getDificultad(), partida.getNumeroNivel());
            if (!nuevos.isEmpty()) avisar("Personaje desbloqueado: " + nuevos.get(0).getNombre());
        }
        if (partida.terminada()) { guardarResultado(true); cambiarEstado(Estado.DERROTA); }
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
        if (vista.boton("COMENZAR PARTIDA", 75, 308, 350, 54, true)) { cambiarEstado(Estado.DIFICULTAD); return; }
        if (vista.boton("TIENDA Y PERSONAJES", 75, 240, 350, 52, false)) { cambiarEstado(Estado.TIENDA); return; }
        if (vista.boton("MEJORES PUNTUACIONES", 75, 174, 350, 52, false)) { cambiarEstado(Estado.PUNTUACIONES); return; }
        if (vista.boton("GUIA DE OBJETOS", 75, 108, 350, 48, false)) { cambiarEstado(Estado.AYUDA); return; }
        vista.panel(500, 160, 420, 335, "16273C");
        vista.centrar("TU PERSONAJE", 500, 420, 466, 1, "91A9BC");
        vista.personaje(perfil.getEquipado(), 660, 252, 6, reloj, true, false);
        vista.centrar(perfil.getEquipado().getNombre(), 500, 420, 221, 1.3f, "E4EDF5");
        vista.centrar("Aspectos cosmeticos, mismas habilidades", 500, 420, 189, 0.95f, "91A9BC");
        if (vista.boton(tactil ? "CONTROL: TACTIL / RATON" : "CONTROL: TECLADO", 500, 95, 420, 45, false)) tactil = !tactil;
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SI" : "SONIDO: NO", 630, 43, 160, 36, false)) configuracion.alternarSonido();
    }
    private void dificultades() {
        cabecera("ELIGE TU DIFICULTAD", "El nivel aumenta cada 25 segundos de supervivencia.");
        for (Dificultad opcion : Dificultad.values()) {
            int i = opcion.ordinal(); float x = 42 + i * 300;
            vista.panel(x, 218, 276, 252, opcion == dificultad ? "21473F" : "13253A");
            vista.texto("0" + (i + 1), x + 24, 444, 1.2f, "67DCCE");
            vista.texto(opcion.name(), x + 24, 404, 2, "E4EDF5");
            vista.texto(i == 0 ? "Ritmo suave" : i == 1 ? "Lluvia rapida" : "Peligro constante", x + 24, 359, 1.1f, "A3B8C8");
            vista.texto("Mejor nivel: " + perfil.getNivelMaximo(opcion), x + 24, 321, 1, "A3B8C8");
            if (vista.boton(opcion == dificultad ? "SELECCIONADO" : "ELEGIR", x + 20, 242, 236, 48, opcion == dificultad)) dificultad = opcion;
        }
        vista.centrar("Tres vidas. Niveles sin limite. Cada dificultad tiene su propio ranking.", 0, 960, 184, 1.05f, "A3B8C8");
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
        cabecera("GUIA DE SUPERVIVENCIA", "A / D o flechas: mover. P: pausa. Tambien puedes usar raton o pantalla tactil.");
        vista.texto("Agua celeste: +10 puntos. Gota rosa con X: pierde una vida.", 42, 499, 1.05f, "D6E4ED");
        int i = 0;
        for (TipoObjeto tipo : TipoObjeto.values()) {
            float y = 451 - i * 39;
            vista.panel(42, y - 24, 45, 30, tipo.getColor());
            vista.centrar(tipo.getSimbolo(), 42, 45, y - 3, 1, "102234");
            vista.texto(tipo.getNombre(), 108, y, 1.05f, tipo.getColor());
            vista.texto(tipo.getDescripcion(), 288, y, 1, "D6E4ED");
            i++;
        }
        vista.texto("Los efectos iguales renuevan su tiempo. Cafe y veneno se combinan.", 42, 80, 1, "91A9BC");
        if (vista.boton("VOLVER", 42, 30, 170, 36, false)) cambiarEstado(Estado.MENU);
    }
    private void dibujarPartida() {
        Personaje jugador = partida.getPersonaje();
        for (Gota gota : partida.getLluvia().getGotas()) vista.gota(gota);
        vista.personaje(perfil.getEquipado(), jugador.getX(), jugador.getY(), 3.4f, reloj, jugador.estaMoviendo(), jugador.estaHerido() && (int) (reloj * 10) % 2 == 0);
        vista.panel(0, 532, 960, 68, "0A1526");
        vista.texto(partida.getDificultad() + " / NIVEL " + partida.getNumeroNivel(), 24, 578, 1.2f, "67DCCE");
        vista.texto("PUNTOS " + jugador.getPuntos(), 275, 578, 1.15f, "E4EDF5");
        vista.texto("VIDAS " + jugador.getVidas(), 495, 578, 1.15f, "FF9BA6");
        vista.texto("MONEDAS " + jugador.getMonedas(), 650, 578, 1.1f, "FFD36A");
        if (vista.boton("PAUSA", 825, 545, 110, 40, false)) { cambiarEstado(Estado.PAUSA); return; }
        vista.panel(24, 514, 912, 5, "23354E");
        vista.panel(24, 514, 912 * partida.getProgreso(), 5, "67DCCE");
        vista.texto("Record " + perfil.getPuntuaciones(partida.getDificultad())[0], 24, 550, 0.95f, "91A9BC");
        if (partida.esSecreto()) {
            vista.centrar("RONDA SECRETA / SOLO MONEDAS / " + (int) Math.ceil(partida.getTiempoSecreto()) + " s", 0, 960, 466, 1.3f, "FFD36A");
        } else if (partida.getAvisoNivel() > 0) vista.centrar("NIVEL " + partida.getNumeroNivel(), 0, 960, 375, 2, "E4EDF5");
        int fila = 0;
        for (TipoObjeto tipo : TipoObjeto.values()) if (jugador.activo(tipo)) {
            vista.texto(tipo.getNombre() + "  " + (int) Math.ceil(jugador.tiempoRestante(tipo)) + "s", 24, 490 - fila * 22, 1, tipo.getColor());
            fila++;
        }
        vista.centrar(jugador.getAviso().isEmpty() ? "Recoge agua y monedas. Evita los peligros." : jugador.getAviso(), 0, 960, 25, 1, "D6E4ED");
    }
    private void pausa() {
        vista.panel(230, 120, 500, 370, "0C192C");
        vista.centrar("UN RESPIRO", 230, 500, 446, 2, "E4EDF5");
        vista.centrar("El tiempo y los efectos estan detenidos.", 230, 500, 399, 1, "91A9BC");
        if (vista.boton("CONTINUAR", 300, 305, 360, 54, true)) { cambiarEstado(Estado.JUGANDO); return; }
        if (vista.boton("GUARDAR MONEDAS Y SALIR", 300, 237, 360, 50, false)) { abandonar(); return; }
        if (vista.boton(configuracion.tieneSonido() ? "SONIDO: SI" : "SONIDO: NO", 300, 169, 360, 48, false)) configuracion.alternarSonido();
        vista.centrar("Salir no registra puntuacion en el ranking.", 230, 500, 145, 0.9f, "91A9BC");
    }
    private void derrota() {
        if (nuevoRecord && tiempoRecord < 6) vista.confeti(tiempoRecord);
        vista.panel(230, 115, 500, 380, "0C192C");
        float escala = nuevoRecord && tiempoRecord < 6 ? 1.8f + 0.1f * (float) Math.sin(tiempoRecord * 6) : 1.8f;
        vista.centrar(nuevoRecord ? "NUEVO RECORD" : "FIN DE LA PARTIDA", 230, 500, 451, escala, nuevoRecord ? "FFD36A" : "E4EDF5");
        Personaje jugador = partida.getPersonaje();
        vista.centrar(partida.getDificultad() + " / NIVEL " + partida.getNumeroNivel(), 230, 500, 405, 1.2f, "67DCCE");
        vista.centrar(jugador.getPuntos() + " puntos", 230, 500, 366, 1.5f, "E4EDF5");
        vista.centrar("+" + jugador.getMonedas() + " monedas guardadas", 230, 500, 326, 1.1f, "FFD36A");
        if (vista.boton("OTRA PARTIDA", 300, 240, 360, 52, true)) { iniciar(); return; }
        if (vista.boton("VOLVER AL MENU", 300, 173, 360, 48, false)) cambiarEstado(Estado.MENU);
    }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() { if (estado == Estado.JUGANDO) cambiarEstado(Estado.PAUSA); }
    @Override public void dispose() {
        guardarResultado(false);
        musica.dispose(); recoger.dispose(); golpe.dispose(); vista.dispose();
    }
}
