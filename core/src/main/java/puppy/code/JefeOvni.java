package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JefeOvni {
    private final Rectangle area = new Rectangle(0, 422, 110, 44);
    private final List<ChorroAgua> chorros = new ArrayList<>();
    private final Random azar;
    private final float ancho;
    private final int vidaMaxima;
    private final Dificultad dificultad;
    private int vida, ataques, disparos;
    private float reloj, ataque, agua, esperaDisparo;
    public JefeOvni(Dificultad dificultad, float ancho, long semilla) {
        this.dificultad = dificultad; this.ancho = ancho; azar = new Random(semilla);
        vidaMaxima = 24 + dificultad.ordinal() * 8; vida = vidaMaxima;
        area.x = (ancho - area.width) / 2;
    }
    public void actualizar(float delta, Personaje jugador, Lluvia lluvia) {
        if (derrotado()) return;
        reloj += delta;
        esperaDisparo = Math.max(0, esperaDisparo - delta);
        float furia = vida <= vidaMaxima / 2 ? 1.4f : 1;
        area.x = (ancho - area.width) / 2 + (float) Math.sin(reloj * furia) * (ancho / 2 - 85);
        ataque += delta; agua += delta;
        float intervalo = (0.85f - dificultad.ordinal() * 0.15f) / furia;
        if (ataque >= intervalo) {
            ataque -= intervalo; ataques++;
            TipoObjeto[] peligros = Zona.ESPACIO.peligros();
            float x = ataques % 3 == 0 ? jugador.getX() : area.x + azar.nextFloat() * area.width;
            lluvia.lanzar(new ObjetoEspecial(MathUtils.clamp(x, 0, ancho - 28), area.y - 35,
                    300 + dificultad.ordinal() * 60 + (furia - 1) * 100, peligros[azar.nextInt(peligros.length)]));
        }
        // Sigue cayendo agua para poder recuperarse si se agota la municion.
        if (agua >= 1.2f) {
            agua -= 1.2f;
            lluvia.lanzar(new GotaBuena(12 + azar.nextFloat() * (ancho - 52), 495, 240));
        }
        for (int i = chorros.size() - 1; i >= 0; i--) {
            ChorroAgua chorro = chorros.get(i);
            chorro.actualizar(delta);
            if (chorro.choca(area)) { vida = Math.max(0, vida - 1); chorros.remove(i); }
            else if (chorro.fuera()) chorros.remove(i);
        }
    }
    public boolean disparar(Personaje jugador) {
        if (derrotado() || esperaDisparo > 0 || !jugador.gastarAgua()) return false;
        chorros.add(new ChorroAgua(jugador.getX() + Personaje.ANCHO / 2 - 5, jugador.getY() + 68));
        esperaDisparo = 0.2f; disparos++;
        return true;
    }
    public boolean derrotado() { return vida == 0; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaques() { return ataques; }
    public int getDisparos() { return disparos; }
    public List<ChorroAgua> getChorros() { return Collections.unmodifiableList(chorros); }
}
