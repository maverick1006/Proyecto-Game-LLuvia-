package puppy.code;

import com.badlogic.gdx.math.Rectangle;

public abstract class Gota {
    private final Rectangle area;
    private final float velocidad;
    private boolean terminada;

    protected Gota(float x, float y, float velocidad) {
        area = new Rectangle(x, y, 28, 38);
        this.velocidad = velocidad;
    }

    // Todas siguen estos pasos; cada hija decide qué pasa al recogerla.
    public final void actualizar(float delta, Personaje personaje) {
        if (terminada) return;
        area.y -= velocidad * delta;
        if (area.y + area.height < 0) {
            terminada = true;
        } else if (personaje.choca(area)) {
            aplicarEfecto(personaje);
            terminada = true;
        }
    }

    protected abstract void aplicarEfecto(Personaje personaje);
    public abstract boolean esBuena();
    public boolean estaTerminada() { return terminada; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
}
