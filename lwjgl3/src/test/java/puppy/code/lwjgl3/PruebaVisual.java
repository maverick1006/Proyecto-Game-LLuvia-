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
import puppy.code.*;

// Usa un perfil separado: las compras de prueba no tocan la partida del usuario.
public class PruebaVisual extends GameLluvia {
    private int cuadro;
    private Input entradaSimulada;
    private final String carpeta;
    private float progresoPausado;
    public PruebaVisual(String carpeta) { this.carpeta = carpeta; }

    @Override public void create() {
        Preferences datos = Gdx.app.getPreferences(System.getProperty("gamelluvia.perfil"));
        datos.clear(); datos.putInteger("monedas", 100).putBoolean("sonido", false).flush();
        super.create();
        Input original = Gdx.input;
        entradaSimulada = (Input) Proxy.newProxyInstance(Input.class.getClassLoader(), new Class<?>[]{Input.class},
                (objeto, metodo, argumentos) -> {
                    String nombre = metodo.getName();
                    if (nombre.equals("isKeyJustPressed")) {
                        int tecla = (Integer) argumentos[0];
                        return ((cuadro == 210 || cuadro == 230) && tecla == Input.Keys.P)
                                || ((cuadro == 320 || cuadro == 350 || cuadro == 380) && tecla == Input.Keys.ESCAPE);
                    }
                    if (nombre.equals("isKeyPressed")) return cuadro >= 140 && cuadro < 170 && (Integer) argumentos[0] == Input.Keys.RIGHT;
                    if (nombre.equals("justTouched") || nombre.equals("isTouched")) return click() != null;
                    if ((nombre.equals("getX") || nombre.equals("getY")) && click() != null) {
                        float escala = Math.min(Gdx.graphics.getWidth() / 960f, Gdx.graphics.getHeight() / 600f);
                        if (nombre.equals("getX")) return (int) ((Gdx.graphics.getWidth() - 960 * escala) / 2 + click()[0] * escala);
                        return (int) ((Gdx.graphics.getHeight() - 600 * escala) / 2 + (600 - click()[1]) * escala);
                    }
                    return metodo.invoke(original, argumentos);
                });
    }
    private float[] click() {
        switch (cuadro) {
            case 30: return new float[]{200, 265};
            case 50:
            case 55: return new float[]{590, 330};
            case 70: return new float[]{655, 58};
            case 90: return new float[]{100, 60};
            case 100: return new float[]{200, 335};
            case 120: return new float[]{470, 265};
            case 130: return new float[]{480, 130};
            case 330: return new float[]{200, 200};
            case 360: return new float[]{200, 130};
            default: return null;
        }
    }
    @Override public void render() {
        cuadro++;
        Gdx.input = entradaSimulada;
        if (cuadro == 175) {
            partida().getPersonaje().atrapar(TipoObjeto.CAFE);
            partida().getPersonaje().atrapar(TipoObjeto.PUNTOS_DOBLES);
        }
        if (cuadro == 240) {
            poner(partida(), "numeroNivel", 4);
            poner(partida(), "tiempoNivel", 25f);
        }
        if (cuadro == 300) {
            Personaje p = partida().getPersonaje();
            p.sumarPuntos(100);
            for (int i = 0; i < 5; i++) p.atrapar(TipoObjeto.MONEDA);
            for (int i = 0; i < 3; i++) { p.actualizar(1.3f); p.recibirDanio(); }
        }
        super.render();
        if (cuadro == 20) capturar("01-menu", "MENU");
        if (cuadro == 40) capturar("05-tienda", "TIENDA");
        if (cuadro == 60) {
            PerfilJugador perfil = ConfiguracionJuego.getInstancia().getPerfil();
            comprobar(perfil.getMonedas() == 80 && perfil.getEquipado() == Aspecto.FONTANERO, "Compra y equipamiento");
        }
        if (cuadro == 80) capturar("06-desbloqueos", "TIENDA");
        if (cuadro == 110) capturar("07-dificultad", "DIFICULTAD");
        if (cuadro == 170) comprobar(partida().getPersonaje().getX() > 456, "Movimiento con teclado");
        if (cuadro == 200) capturar("02-partida", "JUGANDO");
        if (cuadro == 210) progresoPausado = partida().getProgreso();
        if (cuadro == 220) {
            comprobar(partida().getProgreso() == progresoPausado, "Pausa detiene el tiempo");
            capturar("03-pausa", "PAUSA");
        }
        if (cuadro == 260) {
            comprobar(partida().esSecreto(), "Entrada a ronda secreta");
            capturar("08-secreto", "JUGANDO");
        }
        if (cuadro == 310) {
            comprobar((Boolean) leer(this, "nuevoRecord"), "Record nuevo al morir");
            comprobar(ConfiguracionJuego.getInstancia().getPerfil().getMonedas() == 80 + partida().getPersonaje().getMonedas(), "Deposito al morir");
            capturar("09-record", "DERROTA");
        }
        if (cuadro == 340) {
            comprobar(ConfiguracionJuego.getInstancia().getPerfil().getMonedas() == 80 + partida().getPersonaje().getMonedas(), "Sin depositos duplicados");
            capturar("10-puntuaciones", "PUNTUACIONES");
        }
        if (cuadro == 370) capturar("11-guia", "AYUDA");
        if (cuadro == 390) Gdx.graphics.setWindowedMode(800, 700);
        if (cuadro == 410) capturar("04-redimensionado", "MENU");
        if (cuadro == 420) Gdx.app.exit();
    }
    private Partida partida() { return (Partida) leer(this, "partida"); }
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
        PixmapIO.writePNG(Gdx.files.absolute(carpeta + "/" + nombre + ".png"), pixmap, -1, true);
        pixmap.dispose();
    }
    @Override public void dispose() {
        super.dispose();
        Preferences datos = Gdx.app.getPreferences(System.getProperty("gamelluvia.perfil"));
        datos.clear();
        datos.flush();
    }
    public static void main(String[] args) {
        System.setProperty("gamelluvia.perfil", "gamelluvia-prueba-visual");
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setPreferencesConfig(java.nio.file.Paths.get(args[0]).getParent().getParent()
                .resolve("build/perfil-prueba").toString(), com.badlogic.gdx.Files.FileType.Absolute);
        config.setTitle("GameLluvia - verificacion visual");
        config.setWindowedMode(960, 600); config.setForegroundFPS(60);
        new Lwjgl3Application(new PruebaVisual(args[0]), config);
    }
}
