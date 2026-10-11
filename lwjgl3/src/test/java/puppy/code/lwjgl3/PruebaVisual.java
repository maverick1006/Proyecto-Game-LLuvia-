package puppy.code.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import java.lang.reflect.Proxy;
import puppy.code.GameLluvia;

// Abre el juego, recorre las pantallas principales y guarda capturas reales.
public class PruebaVisual extends GameLluvia {
    private int cuadro;
    private Input entradaSimulada;
    private final String carpeta;
    public PruebaVisual(String carpeta) { this.carpeta = carpeta; }

    @Override public void create() {
        super.create();
        Input original = Gdx.input;
        entradaSimulada = (Input) Proxy.newProxyInstance(Input.class.getClassLoader(), new Class<?>[]{Input.class},
                (objeto, metodo, argumentos) -> {
                    if (metodo.getName().equals("isKeyJustPressed")) {
                        int tecla = (Integer) argumentos[0];
                        return (cuadro == 30 && tecla == Input.Keys.ENTER)
                                || ((cuadro == 150 || cuadro == 180) && tecla == Input.Keys.P);
                    }
                    return metodo.invoke(original, argumentos);
                });
    }

    @Override public void render() {
        cuadro++;
        Gdx.input = entradaSimulada;
        super.render();
        if (cuadro == 20) { verificarEstado("MENU"); capturar("01-menu"); }
        if (cuadro == 140) { verificarEstado("JUGANDO"); capturar("02-partida"); }
        if (cuadro == 160) { verificarEstado("PAUSA"); capturar("03-pausa"); }
        if (cuadro == 190) Gdx.graphics.setWindowedMode(800, 700);
        if (cuadro == 210) { verificarEstado("JUGANDO"); capturar("04-redimensionado"); }
        if (cuadro == 220) Gdx.app.exit();
    }

    private void verificarEstado(String esperado) {
        try {
            java.lang.reflect.Field campo = GameLluvia.class.getDeclaredField("estado");
            campo.setAccessible(true);
            if (!campo.get(this).toString().equals(esperado)) throw new AssertionError("Estado esperado: " + esperado);
        } catch (ReflectiveOperationException error) {
            throw new AssertionError(error);
        }
    }

    private void capturar(String nombre) {
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        PixmapIO.writePNG(Gdx.files.absolute(carpeta + "/" + nombre + ".png"), pixmap, -1, true);
        pixmap.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GameLluvia - verificacion visual");
        config.setWindowedMode(960, 600);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new PruebaVisual(args[0]), config);
    }
}
