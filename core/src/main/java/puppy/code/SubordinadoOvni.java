package puppy.code;

import com.badlogic.gdx.math.Rectangle;

public class SubordinadoOvni {
    private final Rectangle area;
    private final float centro, direccion;
    private int vida = 8;
    private float reloj, espera = 1;

    SubordinadoOvni(float ancho, boolean izquierdo) {
        centro = ancho * (izquierdo ? 0.27f : 0.73f);
        direccion = izquierdo ? 1 : -1;
        area = new Rectangle(centro - 24, 410, 48, 24);
    }

    void actualizar(float delta, float ancho, Lluvia lluvia) {
        reloj += delta;
        area.x = centro - 24 + direccion * (float) Math.sin(reloj * 2) * ancho * 0.12f;
        area.y = Math.max(260, 410 - reloj * 90) + (float) Math.sin(reloj * 3) * 16;
        espera -= delta;
        if (espera <= 0) {
            espera += 1.65f;
            lluvia.lanzar(new GotaMala(area.x + 10, area.y - 38, 265, TipoObjeto.ACIDO));
        }
    }

    boolean recibir(ChorroAgua chorro) {
        if (!chorro.choca(area)) return false;
        vida -= chorro.daño() + chorro.getArma().quemadura();
        return true;
    }

    boolean derrotado() { return vida <= 0; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
}
