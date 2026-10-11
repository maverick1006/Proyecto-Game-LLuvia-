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

    public VistaJuego() {
        fuente.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    public void preparar(Viewport viewport) {
        viewport.apply();
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
    public void fondo(float reloj, boolean secreto) {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(Color.valueOf(secreto ? "30273E" : "101E32"));
        formas.rect(0, 0, 960, 600);
        formas.setColor(Color.valueOf(secreto ? "735C46" : "19374C"));
        formas.circle(775, 390, 150);
        formas.setColor(Color.valueOf(secreto ? "E5C56D" : "456C80"));
        formas.circle(780, 415, 48);
        formas.setColor(Color.valueOf("152A40"));
        formas.triangle(0, 40, 180, 340, 480, 40);
        formas.triangle(300, 40, 620, 300, 960, 40);
        if (!secreto) {
            formas.setColor(Color.valueOf("2C465B"));
            for (int i = 0; i < 36; i++) {
                float x = (i * 137) % 960;
                float y = 42 + ((i * 71 + 550 - reloj * 35 % 550) % 550);
                formas.rectLine(x, y, x - 3, y - 12, 1);
            }
        }
        formas.setColor(Color.valueOf("345D65")); formas.rect(0, 38, 960, 4);
        formas.setColor(Color.valueOf("122D37")); formas.rect(0, 0, 960, 38);
        formas.end();
    }
    public void gota(Gota gota) {
        TipoObjeto tipo = gota instanceof ObjetoEspecial ? ((ObjetoEspecial) gota).getTipo() : null;
        String color = tipo != null ? tipo.getColor() : gota.esBuena() ? "61E0E8" : "FF7F91";
        float x = gota.getX(), y = gota.getY();
        formas.begin(ShapeRenderer.ShapeType.Filled);
        formas.setColor(Color.valueOf(color));
        if (tipo == TipoObjeto.MONEDA || tipo == TipoObjeto.MONEDAS_DOBLES) {
            formas.circle(x + 14, y + 18, 14);
            formas.setColor(Color.valueOf("B78537")); formas.circle(x + 14, y + 18, 10);
        } else if (tipo == TipoObjeto.CAFE) {
            formas.rect(x + 2, y + 2, 22, 25);
            formas.rect(x + 23, y + 8, 6, 14);
            formas.setColor(Color.valueOf("F5E6D1")); formas.rect(x + 2, y + 26, 22, 4);
        } else if (tipo == TipoObjeto.PAJARO) {
            formas.circle(x + 14, y + 12, 14);
            formas.circle(x + 14, y + 22, 9);
            formas.circle(x + 14, y + 30, 5);
        } else {
            formas.circle(x + 14, y + 13, 13);
            formas.triangle(x + 1, y + 17, x + 14, y + 38, x + 27, y + 17);
        }
        formas.end();
        if (tipo != null) centrar(tipo.getSimbolo(), x, 28, y + 22, 0.95f, "102234");
        else if (!gota.esBuena()) centrar("X", x, 28, y + 20, 1, "702C4D");
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
