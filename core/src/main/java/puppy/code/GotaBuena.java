package puppy.code;

public class GotaBuena extends Gota {
    public GotaBuena(float x, float y, float velocidad) { super(x, y, velocidad); }
    @Override protected void calcularMovimiento(float delta) { caer(delta, 1); }
    @Override protected boolean verificarColision(Personaje personaje) { return toca(personaje, 0); }
    @Override protected void aplicarEfecto(Personaje personaje) { personaje.recogerAgua(10); }
    @Override public boolean esBuena() { return true; }
}
