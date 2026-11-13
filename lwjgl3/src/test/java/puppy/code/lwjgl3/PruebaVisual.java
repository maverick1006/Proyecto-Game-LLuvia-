package puppy.code.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import puppy.code.*;

// Prepara escenarios de prueba sin tocar el perfil real ni incluir atajos en el juego.
public class PruebaVisual extends GameLluvia {
    private int cuadro;
    private Input entradaSimulada;
    private final String carpeta;
    private float sedPausada;
    private Personaje jugadorAntesDelCambio;
    private float xAntesDelToque;
    private int monedasAntesSecreto, puntosAntesSecreto, nivelAntesSecreto;
    public PruebaVisual(String carpeta) { this.carpeta = carpeta; }
    @Override public void create() {
        Preferences datos = Gdx.app.getPreferences(System.getProperty("gamelluvia.perfil"));
        datos.clear(); datos.putInteger("monedas", 1000).putBoolean("sonido", false).flush();
        super.create();
        Input original = Gdx.input;
        entradaSimulada = (Input) Proxy.newProxyInstance(Input.class.getClassLoader(), new Class<?>[]{Input.class},
                (objeto, metodo, argumentos) -> {
                    String nombre = metodo.getName();
                    if (nombre.equals("isKeyJustPressed")) {
                        int tecla = (Integer) argumentos[0];
                        return ((cuadro == 210 || cuadro == 230) && tecla == Input.Keys.P)
                                || ((cuadro == 460 || cuadro == 490 || cuadro == 570 || cuadro == 640 || cuadro == 823 || cuadro == 826) && tecla == Input.Keys.ESCAPE)
                                || (cuadro == 172 && tecla == Input.Keys.T)
                                || ((cuadro == 132 || cuadro == 134 || cuadro == 136 || cuadro == 510 || cuadro == 730 || cuadro == 824 || cuadro == 825 || cuadro == 827) && tecla == Input.Keys.ENTER);
                    }
                    if (nombre.equals("isKeyPressed")) {
                        int tecla = (Integer) argumentos[0];
                        return (cuadro >= 140 && cuadro < 170 && tecla == Input.Keys.RIGHT)
                                || (cuadro >= 365 && cuadro < 430 && tecla == Input.Keys.SPACE)
                                || (cuadro >= 520 && cuadro < 545 && (tecla == Input.Keys.A || tecla == Input.Keys.RIGHT));
                    }
                    if (nombre.equals("justTouched") || nombre.equals("isTouched")) return click() != null;
                    if (nombre.equals("isButtonPressed")) return false;
                    if ((nombre.equals("getX") || nombre.equals("getY")) && click() != null) {
                        float escala = Math.min(Gdx.graphics.getWidth() / 960f, Gdx.graphics.getHeight() / 600f);
                        return nombre.equals("getX") ? (int) ((Gdx.graphics.getWidth() - 960 * escala) / 2 + click()[0] * escala)
                                : (int) ((Gdx.graphics.getHeight() - 600 * escala) / 2 + (600 - click()[1]) * escala);
                    }
                    return metodo.invoke(original, argumentos);
                });
    }
    private float[] click() {
        switch (cuadro) {
            case 30: return new float[]{200, 265};
            case 50: case 55: return new float[]{590, 330};
            case 70: return new float[]{655, 58};
            case 90: return new float[]{100, 60};
            case 100: return new float[]{200, 335};
            case 120: return new float[]{470, 265};
            case 130: return new float[]{480, 130};
            case 176: return new float[]{100, 220};
            case 184: return new float[]{720, 568};
            case 470: return new float[]{200, 200};
            case 500: return new float[]{700, 125};
            case 580: return new float[]{200, 130};
            case 600: return new float[]{350, 485};
            case 620: return new float[]{590, 485};
            case 680: return new float[]{15, 580};
            case 775: return new float[]{480, 190};
            case 780: return new float[]{200, 265};
            case 790: case 800: case 810: return new float[]{655, 58};
            case 828: return new float[]{500, 92};
            default: return null;
        }
    }
    @Override public void render() {
        cuadro++; Gdx.input = entradaSimulada;
        if (cuadro == 172) {
            jugadorAntesDelCambio = partida().getPersonaje();
            xAntesDelToque = jugadorAntesDelCambio.getX();
        }
        if (cuadro == 240) puntos(400);
        if (cuadro == 270) poner(partida(), "tiempoSecreto", 0.001f);
        if (cuadro == 280) { poner(partida(), "ultimoSecreto", 6); puntos(1000); }
        if (cuadro == 300) puntos(2000);
        if (cuadro == 320) puntos(3000);
        if (cuadro == 360) poner(partida(), "tiempoAscenso", 3f);
        if (cuadro == 440) {
            JefeOvni jefe = partida().getJefe();
            poner(jefe, "fase", JefeOvni.Fase.SUBORDINADOS); poner(jefe, "vida", 1); poner(jefe, "reloj", 0f);
            @SuppressWarnings("unchecked") List<ChorroAgua> tiros = (List<ChorroAgua>) leer(jefe, "chorros");
            tiros.add(new ChorroAgua(465, jefe.getY()));
            partida().getLluvia().limpiar();
        }
        if (cuadro == 550) {
            Personaje p = versus().getUno().getPersonaje();
            for (int i = 0; i < 3; i++) { p.actualizar(1.3f); p.recibirDaño(); }
        }
        if (cuadro == 680) {
            monedasAntesSecreto = perfil().getMonedas();
            puntosAntesSecreto = perfil().getPuntuaciones(Dificultad.PRO)[0];
            nivelAntesSecreto = perfil().getNivelMaximo(Dificultad.PRO);
        }
        if (cuadro == 710) {
            Personaje p = partida().getPersonaje();
            for (int i = 0; i < 3; i++) { p.actualizar(1.3f); p.recibirDaño(); }
        }
        if (cuadro == 750) {
            JefeOvni jefe = partida().getJefe(); poner(jefe, "fase", JefeOvni.Fase.SUBORDINADOS); poner(jefe, "vida", 1); poner(jefe, "reloj", 0f);
            @SuppressWarnings("unchecked") List<ChorroAgua> tiros = (List<ChorroAgua>) leer(jefe, "chorros");
            tiros.add(new ChorroAgua(465, jefe.getY()));
        }
        super.render();
        if (cuadro == 20) capturar("01-menu", "MENU");
        if (cuadro == 40) capturar("05-tienda", "TIENDA");
        if (cuadro == 60) comprobar(perfil().getMonedas() == 820 && perfil().getEquipado() == Aspecto.FONTANERO, "Compra y equipamiento");
        if (cuadro == 80) capturar("06-desbloqueos", "TIENDA");
        if (cuadro == 110) capturar("07-dificultad", "DIFICULTAD");
        if (cuadro == 130) {
            comprobar(partida() == null, "El prologo no inicia la simulacion");
        }
        if (cuadro == 131) capturar("27-prologo-luma", "PROLOGO");
        if (cuadro == 132) comprobar((Integer) leer(this, "paginaPrologo") == 1, "Avanza al segundo dialogo");
        if (cuadro == 134) comprobar((Integer) leer(this, "paginaPrologo") == 2, "Avanza al tercer dialogo");
        if (cuadro == 136) capturar("28-inicio-campana", "JUGANDO");
        if (cuadro == 170) comprobar(partida().getPersonaje().getX() > 456, "Movimiento con teclado");
        if (cuadro == 172) comprobar(leer(jugadorAntesDelCambio, "control") instanceof ControlTactil,
                "T cambia al control tactil en plena partida");
        if (cuadro == 176) comprobar(jugadorAntesDelCambio.getX() < xAntesDelToque, "Toque mueve al jugador");
        if (cuadro == 184) {
            comprobar(partida().getPersonaje() == jugadorAntesDelCambio, "Se conserva el jugador");
            comprobar(leer(jugadorAntesDelCambio, "control") instanceof ControlTeclado,
                    "El boton del HUD vuelve al teclado");
            capturar("26-control-en-partida", "JUGANDO");
        }
        if (cuadro == 200) capturar("02-partida", "JUGANDO");
        if (cuadro == 210) sedPausada = partida().getPersonaje().getSinAgua();
        if (cuadro == 220) { comprobar(partida().getPersonaje().getSinAgua() == sedPausada, "Pausa congela la sed"); capturar("03-pausa", "PAUSA"); }
        if (cuadro == 260) { comprobar(partida().esSecreto(), "Ronda secreta"); capturar("08-secreto", "JUGANDO"); }
        if (cuadro == 290) { comprobar(partida().getZona() == Zona.HIELO, "Zona hielo"); capturar("12-hielo", "JUGANDO"); }
        if (cuadro == 310) { comprobar(partida().getZona() == Zona.DESIERTO, "Zona desierto"); capturar("13-atacama", "JUGANDO"); }
        if (cuadro == 350) capturar("14-ascenso", "JUGANDO");
        if (cuadro == 400) { comprobar(partida().getJefe().getDisparos() > 0, "Espacio dispara agua"); capturar("15-jefe", "JUGANDO"); }
        if (cuadro == 450) {
            comprobar(partida().gano(), "Jefe vencido"); comprobar(perfil().getMonedas() == 820 + partida().getMonedasFinales(), "Premio de victoria");
            capturar("16-victoria", "VICTORIA");
        }
        if (cuadro == 480) { comprobar(perfil().getMonedas() == 820 + partida().getMonedasFinales(), "Deposito unico"); capturar("10-puntuaciones", "PUNTUACIONES"); }
        if (cuadro == 540) {
            comprobar(versus().getUno().getPersonaje().getX() < 216 && versus().getDos().getPersonaje().getX() > 216, "Controles independientes");
            capturar("17-versus", "JUGANDO");
        }
        if (cuadro == 560) { comprobar(versus().getGanador() == 2, "Victoria J2"); capturar("18-resultado-versus", "RESULTADO_VERSUS"); }
        if (cuadro == 590) capturar("11-guia", "AYUDA");
        if (cuadro == 610) capturar("19-guia-hielo", "AYUDA");
        if (cuadro == 630) capturar("20-guia-desierto", "AYUDA");
        if (cuadro == 650) Gdx.graphics.setWindowedMode(800, 700);
        if (cuadro == 670) capturar("04-redimensionado", "MENU");
        if (cuadro == 690) {
            comprobar(partida().esDesafioOculto() && partida().enJefe(), "Boton oculto con ventana redimensionada");
            capturar("21-desafio-oculto", "JUGANDO");
        }
        if (cuadro == 720) {
            comprobar(!perfil().tiene(Aspecto.ALIEN), "Perder no entrega alien");
            capturar("22-secreto-derrota", "DERROTA");
        }
        if (cuadro == 760) {
            comprobar(partida().gano() && perfil().tiene(Aspecto.ALIEN), "Victoria secreta entrega alien");
            comprobar(perfil().getMonedas() == monedasAntesSecreto, "Secreto no altera dinero");
            comprobar(perfil().getPuntuaciones(Dificultad.PRO)[0] == puntosAntesSecreto, "Secreto no altera ranking");
            comprobar(perfil().getNivelMaximo(Dificultad.PRO) == nivelAntesSecreto, "Secreto no altera niveles");
            capturar("23-alien-desbloqueado", "VICTORIA");
        }
        if (cuadro == 805) capturar("24-personajes-nuevos", "TIENDA");
        if (cuadro == 820) capturar("25-recompensas-alien", "TIENDA");
        if (cuadro == 825) comprobar(leer(this, "estado").toString().equals("PROLOGO"), "Campana abre la historia");
        if (cuadro == 826) comprobar(leer(this, "estado").toString().equals("DIFICULTAD"), "Esc vuelve a dificultad");
        if (cuadro == 828) comprobar(leer(this, "estado").toString().equals("JUGANDO") && !partida().esDesafioOculto(),
                "Saltar inicia la campana");
        if (cuadro == 830) Gdx.app.exit();
    }
    private void puntos(int puntos) {
        Personaje p = partida().getPersonaje(); p.actualizar(10, false); p.quitarPuntos(p.getPuntos()); p.recogerAgua(puntos);
        partida().getLluvia().limpiar();
    }
    private PerfilJugador perfil() { return ConfiguracionJuego.getInstancia().getPerfil(); }
    private Partida partida() { return (Partida) leer(this, "partida"); }
    private PartidaVersus versus() { return (PartidaVersus) leer(this, "versus"); }
    private Object leer(Object objeto, String nombre) {
        try { Field campo = (objeto == this ? GameLluvia.class : objeto.getClass()).getDeclaredField(nombre); campo.setAccessible(true); return campo.get(objeto); }
        catch (ReflectiveOperationException error) { throw new AssertionError(error); }
    }
    private void poner(Object objeto, String nombre, Object valor) {
        try { Field campo = objeto.getClass().getDeclaredField(nombre); campo.setAccessible(true); campo.set(objeto, valor); }
        catch (ReflectiveOperationException error) { throw new AssertionError(error); }
    }
    private void comprobar(boolean condicion, String descripcion) { if (!condicion) throw new AssertionError(descripcion); }
    private void capturar(String nombre, String esperado) {
        comprobar(leer(this, "estado").toString().equals(esperado), "Estado esperado: " + esperado);
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        PixmapIO.writePNG(Gdx.files.absolute(carpeta + "/" + nombre + ".png"), pixmap, -1, true); pixmap.dispose();
    }
    @Override public void dispose() {
        super.dispose(); Preferences datos = Gdx.app.getPreferences(System.getProperty("gamelluvia.perfil")); datos.clear(); datos.flush();
    }
    public static void main(String[] args) {
        System.setProperty("gamelluvia.perfil", "gamelluvia-prueba-visual");
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setPreferencesConfig(java.nio.file.Paths.get(args[0]).getParent().getParent().resolve("build/perfil-prueba").toString(), com.badlogic.gdx.Files.FileType.Absolute);
        config.setTitle("GameLluvia - verificacion visual"); config.setWindowedMode(960, 600); config.setForegroundFPS(60);
        new Lwjgl3Application(new PruebaVisual(args[0]), config);
    }
}
