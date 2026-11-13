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

    // Cada tipo decide cómo cae y qué pasa al tocar al jugador.
    public final void actualizar(float delta, Personaje personaje) {
        if (terminada) return;
        calcularMovimiento(delta);
        if (area.y + area.height < 0) {
            terminada = true;
        } else if (verificarColision(personaje)) {
            aplicarEfecto(personaje);
            terminada = true;
        }
    }

    protected abstract void calcularMovimiento(float delta);
    protected abstract boolean verificarColision(Personaje personaje);

    protected final void caer(float delta, float factor) { area.y -= velocidad * delta * factor; }

    protected final boolean toca(Personaje personaje, float margen) {
        return personaje.choca(area.x + margen, area.y + margen,
                area.width - 2 * margen, area.height - 2 * margen);
    }

    protected abstract void aplicarEfecto(Personaje personaje);
    public abstract boolean esBuena();
    public boolean estaTerminada() { return terminada; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
}
