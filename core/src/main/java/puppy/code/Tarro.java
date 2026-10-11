package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Tarro {
    public static final float ANCHO = 64;
    private final Rectangle area = new Rectangle(448, 42, ANCHO, 48);
    private final ControlMovimiento control;
    private int vidas = 3;
    private int puntos;
    private float invulnerabilidad;

    public Tarro(ControlMovimiento control) { this.control = control; }

    public void actualizar(float delta) {
        invulnerabilidad = Math.max(0, invulnerabilidad - delta);
        if (vidas > 0) control.actualizar(this, delta);
    }

    public void mover(float distancia) { colocar(area.x + distancia); }
    public void colocar(float x) { area.x = MathUtils.clamp(x, 0, 960 - ANCHO); }
    public boolean recibirDanio() {
        if (vidas == 0 || invulnerabilidad > 0) return false;
        vidas--;
        invulnerabilidad = 1.2f;
        return true;
    }
    public void sumarPuntos(int cantidad) { if (vidas > 0 && cantidad > 0) puntos += cantidad; }
    public boolean choca(Rectangle otra) { return area.overlaps(otra); }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
    public int getVidas() { return vidas; }
    public int getPuntos() { return puntos; }
    public boolean estaHerido() { return invulnerabilidad > 0; }
}
