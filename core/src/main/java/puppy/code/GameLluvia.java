package puppy.code;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameLluvia extends ApplicationAdapter {
    private enum Estado { MENU, JUGANDO, PAUSA, DERROTA, VICTORIA }
    private Estado estado = Estado.MENU;
    private final FitViewport viewport = new FitViewport(960, 600, new OrthographicCamera());
    private final Vector2 toque = new Vector2();
    private final GlyphLayout texto = new GlyphLayout();
    private final Lluvia lluvia = new Lluvia();
    private final Nivel[] niveles = {
        new Nivel.Builder("Brisa").objetivo(100).construir(),
        new Nivel.Builder("Aguacero").objetivo(220).velocidad(280).intervalo(0.5f).probabilidadMala(0.28f).construir(),
        new Nivel.Builder("Tormenta").objetivo(360).velocidad(340).intervalo(0.38f).probabilidadMala(0.35f).construir()
    };
    private SpriteBatch batch;
    private ShapeRenderer formas;
    private BitmapFont fuente;
    private Music musica;
    private Sound recoger, golpe;
    private ConfiguracionJuego configuracion;
    private Tarro tarro;
    private int nivelActual;
    private float reloj;
    private float avisoNivel;
    private boolean tactil;

    @Override public void create() {
        batch = new SpriteBatch();
        formas = new ShapeRenderer();
        fuente = new BitmapFont();
        fuente.getRegion().getTexture().setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear,
                com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
        configuracion = ConfiguracionJuego.getInstancia();
        tactil = Gdx.app.getType() == Application.ApplicationType.Android;
        musica = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.25f);
        recoger = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        golpe = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    private void iniciar() {
        tarro = new Tarro(tactil ? new ControlTactil(viewport) : new ControlTeclado());
        lluvia.limpiar();
        nivelActual = 0;
        avisoNivel = 2;
        cambiarEstado(Estado.JUGANDO);
    }

    private void cambiarEstado(Estado nuevo) {
        estado = nuevo;
        if (nuevo == Estado.JUGANDO && configuracion.tieneSonido()) musica.play();
        else musica.pause();
    }

    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
        reloj += delta;
        procesarEntrada();
        if (estado == Estado.JUGANDO) actualizar(delta);
        ScreenUtils.clear(0.025f, 0.045f, 0.09f, 1);
        viewport.apply();
        formas.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        dibujarFondo();
        if (estado != Estado.MENU) dibujarPartida();
        if (estado != Estado.JUGANDO) dibujarPanel();
        dibujarTextos();
    }

    private void procesarEntrada() {
        boolean pulsado = Gdx.input.justTouched();
        viewport.unproject(toque.set(Gdx.input.getX(), Gdx.input.getY()));
        boolean aceptar = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        boolean pausar = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.BACK);
        if (Gdx.input.isKeyJustPressed(Input.Keys.M) || (pulsado && dentro(704, 546, 120, 36))) {
            configuracion.alternarSonido();
            cambiarEstado(estado);
            return;
        }
        if (estado == Estado.JUGANDO) {
            if (pausar || (pulsado && dentro(838, 546, 98, 36))) cambiarEstado(Estado.PAUSA);
        } else if (estado == Estado.PAUSA) {
            if (aceptar || pausar || (pulsado && dentro(330, 235, 300, 52))) cambiarEstado(Estado.JUGANDO);
            else if (pulsado && dentro(330, 167, 300, 48)) cambiarEstado(Estado.MENU);
        } else if (aceptar || (pulsado && dentro(330, 235, 300, 52))) {
            iniciar();
        } else if (estado != Estado.MENU && (pausar || (pulsado && dentro(330, 167, 300, 48)))) {
            cambiarEstado(Estado.MENU);
        } else if (estado == Estado.MENU && (Gdx.input.isKeyJustPressed(Input.Keys.T)
                || (pulsado && dentro(330, 167, 300, 48)))) {
            tactil = !tactil;
        }
    }

    private boolean dentro(float x, float y, float ancho, float alto) {
        return toque.x >= x && toque.x <= x + ancho && toque.y >= y && toque.y <= y + alto;
    }

    private void actualizar(float delta) {
        int puntosAntes = tarro.getPuntos();
        int vidasAntes = tarro.getVidas();
        tarro.actualizar(delta);
        lluvia.actualizar(delta, tarro, niveles[nivelActual]);
        avisoNivel = Math.max(0, avisoNivel - delta);
        if (configuracion.tieneSonido()) {
            if (tarro.getPuntos() > puntosAntes) recoger.play(0.5f);
            if (tarro.getVidas() < vidasAntes) golpe.play(0.6f);
        }
        if (tarro.getVidas() == 0) {
            configuracion.registrarPuntos(tarro.getPuntos());
            cambiarEstado(Estado.DERROTA);
        } else if (tarro.getPuntos() >= niveles[nivelActual].getObjetivo()) {
            if (nivelActual == niveles.length - 1) {
                configuracion.registrarPuntos(tarro.getPuntos());
                cambiarEstado(Estado.VICTORIA);
            } else {
                nivelActual++;
                lluvia.limpiar();
                avisoNivel = 2;
            }
        }
    }

    private void rect(float x, float y, float w, float h, String color) {
        formas.setColor(Color.valueOf(color));
        formas.rect(x, y, w, h);
    }

    private void dibujarFondo() {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        rect(0, 0, 960, 600, "0D1C33");
        formas.setColor(Color.valueOf("17344B"));
        formas.circle(780, 408, 155);
        formas.setColor(Color.valueOf("254B60"));
        formas.circle(785, 435, 53);
        formas.setColor(Color.valueOf("10283E"));
        formas.triangle(0, 70, 215, 370, 475, 70);
        formas.triangle(270, 70, 605, 320, 960, 70);
        formas.setColor(Color.valueOf("31526A"));
        for (int i = 0; i < 45; i++) {
            float x = (i * 137) % 960;
            float y = 80 + ((i * 71 + 520 - reloj * 35 % 520) % 520);
            formas.rectLine(x, y, x - 3, y - 12, 1);
        }
        rect(0, 0, 960, 42, "183E49");
        rect(0, 38, 960, 4, "3C8791");
        rect(0, 532, 960, 68, "091629");
        rect(704, 546, 120, 36, "20394C");
        if (estado == Estado.JUGANDO) rect(838, 546, 98, 36, "20394C");
        formas.end();
    }

    private void dibujarPartida() {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        for (Gota gota : lluvia.getGotas()) {
            formas.setColor(Color.valueOf(gota.esBuena() ? "61E0E8" : "FF7F91"));
            formas.circle(gota.getX() + 14, gota.getY() + 13, 13);
            formas.triangle(gota.getX() + 1, gota.getY() + 17, gota.getX() + 14,
                    gota.getY() + 38, gota.getX() + 27, gota.getY() + 17);
            if (!gota.esBuena()) {
                formas.setColor(Color.valueOf("712F54"));
                formas.rectLine(gota.getX() + 8, gota.getY() + 7, gota.getX() + 20, gota.getY() + 19, 3);
                formas.rectLine(gota.getX() + 8, gota.getY() + 19, gota.getX() + 20, gota.getY() + 7, 3);
            }
        }
        float x = tarro.getX(), y = tarro.getY();
        boolean parpadeo = tarro.estaHerido() && (int) (reloj * 10) % 2 == 0;
        rect(x + 4, y, 56, 44, parpadeo ? "F5A0AC" : "E5BA75");
        rect(x, y + 39, 64, 9, "FFE0A6");
        rect(x + 13, y + 8, 38, 23, "398D9F");
        rect(24, 507, 912, 6, "20394C");
        int anterior = nivelActual == 0 ? 0 : niveles[nivelActual - 1].getObjetivo();
        float avance = (tarro.getPuntos() - anterior) / (float) (niveles[nivelActual].getObjetivo() - anterior);
        rect(24, 507, 912 * Math.min(1, avance), 6, "61E0E8");
        formas.end();
    }

    private void dibujarPanel() {
        formas.begin(ShapeRenderer.ShapeType.Filled);
        rect(193, 100, 574, 392, "081526");
        rect(193, 488, 574, 4, "61E0E8");
        rect(330, 235, 300, 52, "61E0E8");
        rect(330, 167, 300, 48, "20394C");
        formas.end();
    }

    private void escribir(String valor, float x, float y, float escala, Color color) {
        fuente.getData().setScale(escala);
        fuente.setColor(color);
        fuente.draw(batch, valor, x, y);
    }
    private void centrado(String valor, float y, float escala, Color color) {
        fuente.getData().setScale(escala);
        texto.setText(fuente, valor);
        escribir(valor, (960 - texto.width) / 2, y, escala, color);
    }

    private void dibujarTextos() {
        batch.begin();
        escribir("GAME / LLUVIA", 24, 577, 1.4f, Color.WHITE);
        escribir(configuracion.tieneSonido() ? "SONIDO: SI" : "SONIDO: NO", 717, 569, 1, Color.WHITE);
        if (estado != Estado.MENU) {
            escribir("AGUA " + tarro.getPuntos() + " / " + niveles[nivelActual].getObjetivo(), 248, 573, 1.1f, Color.CYAN);
            escribir("VIDAS " + tarro.getVidas(), 480, 573, 1.1f, Color.WHITE);
            escribir("0" + (nivelActual + 1) + " / " + niveles[nivelActual].getNombre(), 24, 492, 1, Color.LIGHT_GRAY);
        }
        if (estado == Estado.JUGANDO) {
            escribir("PAUSA", 855, 569, 1, Color.WHITE);
            if (avisoNivel > 0) centrado(niveles[nivelActual].getNombre(), 375, 2, Color.WHITE);
            centrado(tactil ? "Arrastra en la zona de juego para moverte" : "A / D o flechas: mover    P: pausa    M: sonido", 25, 1, Color.LIGHT_GRAY);
        } else {
            String titulo = estado == Estado.MENU ? "GUARDIAN DE LA LLUVIA"
                    : estado == Estado.PAUSA ? "UN RESPIRO"
                    : estado == Estado.VICTORIA ? "AGUA PARA TODOS" : "FIN DE LA TORMENTA";
            centrado(titulo, 442, 2, Color.WHITE);
            if (estado == Estado.MENU) {
                centrado("El pueblo necesita agua. Cada gota cuenta.", 385, 1.15f, Color.LIGHT_GRAY);
                centrado("Recoge las gotas celestes y evita las rosas con X.", 352, 1, Color.LIGHT_GRAY);
                centrado("Supera 3 niveles y llega a 360 puntos con vida.", 326, 1, Color.LIGHT_GRAY);
            } else if (estado == Estado.PAUSA) {
                centrado("La partida esta detenida.", 371, 1.2f, Color.LIGHT_GRAY);
                centrado("Puedes continuar cuando quieras.", 337, 1, Color.LIGHT_GRAY);
            } else {
                centrado("Agua recolectada: " + tarro.getPuntos() + " puntos", 375, 1.3f, Color.CYAN);
                centrado(estado == Estado.VICTORIA ? "Completaste los tres niveles." : "Evita las gotas contaminadas e intentalo otra vez.", 337, 1, Color.LIGHT_GRAY);
            }
            centrado(estado == Estado.PAUSA ? "CONTINUAR" : estado == Estado.MENU ? "JUGAR" : "VOLVER A JUGAR", 267, 1.35f, Color.valueOf("081526"));
            centrado(estado == Estado.MENU ? "CONTROL: " + (tactil ? "TACTIL / RATON" : "TECLADO") : "VOLVER AL MENU", 197, 1.1f, Color.WHITE);
            centrado("Mejor puntuacion: " + configuracion.getRecord(), 134, 1, Color.LIGHT_GRAY);
        }
        batch.end();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() { if (estado == Estado.JUGANDO) cambiarEstado(Estado.PAUSA); }
    @Override public void dispose() {
        musica.dispose(); recoger.dispose(); golpe.dispose();
        batch.dispose(); fuente.dispose(); formas.dispose();
    }
}
