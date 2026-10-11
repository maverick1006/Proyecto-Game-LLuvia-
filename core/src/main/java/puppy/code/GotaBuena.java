package puppy.code;

public class GotaBuena extends Gota {
    public GotaBuena(float x, float y, float velocidad) { super(x, y, velocidad); }
    @Override protected void aplicarEfecto(Personaje personaje) { personaje.sumarPuntos(10); }
    @Override public boolean esBuena() { return true; }
}
