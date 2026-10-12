package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VistaJuego {
    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer formas = new ShapeRenderer();
    private final BitmapFont fuente = new BitmapFont();
    private final GlyphLayout medida = new GlyphLayout();
    private final DibujoPersonaje dibujo = new DibujoPersonaje(formas);
    private final Vector2 puntero = new Vector2();
    private boolean click;
    private final com.badlogic.gdx.math.Matrix4 proyeccion = new com.badlogic.gdx.math.Matrix4();

    public VistaJuego() {
        fuente.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    public void preparar(Viewport viewport) {
        viewport.apply();
        proyeccion.set(viewport.getCamera().combined);
        formas.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        viewport.unproject(puntero.set(Gdx.input.getX(), Gdx.input.getY()));
        click = Gdx.input.justTouched();
    }
    public void panel(float x, float y, float w, float h, String color) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(Color.valueOf(color));
        formas.rect(x, y, w, h);
        formas.end();
    }
    public void texto(String valor, float x, float y, float escala, String color) {
        fuente.getData().setScale(escala);
        fuente.setColor(Color.valueOf(color));
        batch.begin(); fuente.draw(batch, valor, x, y); batch.end();
    }
    public void centrar(String valor, float x, float w, float y, float escala, String color) {
        fuente.getData().setScale(escala);
        medida.setText(fuente, valor);
        texto(valor, x + (w - medida.width) / 2, y, escala, color);
    }
    public boolean boton(String titulo, float x, float y, float w, float h, boolean destacado) {
        boolean encima = puntero.x >= x && puntero.x <= x + w && puntero.y >= y && puntero.y <= y + h;
        panel(x, y, w, h, destacado ? (encima ? "9AEEE0" : "67DCCE") : (encima ? "344B68" : "23354E"));
        centrar(titulo, x, w, y + h / 2 + 6, 1.1f, destacado ? "102334" : "E2EAF2");
        if (click && encima) { click = false; return true; }
        return false;
    }
    public void personaje(Aspecto aspecto, float x, float y, float escala, float reloj, boolean moviendo, boolean herido) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        dibujo.dibujar(aspecto, x, y, escala, reloj, moviendo, herido);
        formas.end();
    }
    public void fondo(float reloj, boolean secreto) { fondo(reloj, secreto, Zona.LLANURA, 960); }
    public void fondo(float reloj, boolean secreto, Zona zona, float ancho) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        new DibujoEscenario(formas).fondo(zona, ancho, reloj, secreto);
        formas.end();
    }
    public void gota(Gota gota) {
        TipoObjeto tipo = gota instanceof ObjetoEspecial ? ((ObjetoEspecial) gota).getTipo() : gota.esBuena() ? null : TipoObjeto.SANGRE;
        objeto(tipo, gota.getX(), gota.getY());
    }
    public void objeto(TipoObjeto tipo, float x, float y) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        new DibujoObjetos(formas).dibujar(x, y, tipo);
        formas.end();
        if (tipo == TipoObjeto.AGUA_DOBLE || tipo == TipoObjeto.PUNTOS_DOBLES || tipo == TipoObjeto.MONEDAS_DOBLES)
            centrar("x2", x, 28, y + 20, 0.85f, "102234");
    }
    public void ovni(float x, float y, float reloj) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        new DibujoEscenario(formas).ovni(x, y, reloj);
        formas.end();
    }
    public void rayo(float centro, float avance) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(Color.valueOf("A2DCD3"));
        formas.triangle(centro, 437, centro - 42, 42 + avance * 300, centro + 42, 42 + avance * 300);
        formas.end();
    }
    public void origen(float x) {
        com.badlogic.gdx.math.Matrix4 matriz = new com.badlogic.gdx.math.Matrix4(proyeccion).translate(x, 0, 0);
        formas.setProjectionMatrix(matriz); batch.setProjectionMatrix(matriz);
    }
    public void confeti(float reloj) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 42; i++) {
            formas.setColor(Color.valueOf(i % 2 == 0 ? "FFD36A" : "67DCCE"));
            float x = 80 + (i * 151) % 800;
            float y = 100 + (i * 37 + 400 - reloj * 65 % 400) % 400;
            formas.rect(x, y, 5, 8);
        }
        formas.end();
    }
    public void dispose() { batch.dispose(); formas.dispose(); fuente.dispose(); }
}
