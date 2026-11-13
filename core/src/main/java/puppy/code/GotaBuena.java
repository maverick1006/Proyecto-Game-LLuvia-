package puppy.code;

public class GotaBuena extends Gota {
    private final boolean doble;
    public GotaBuena(float x, float y, float velocidad) { this(x, y, velocidad, false); }
    public GotaBuena(float x, float y, float velocidad, boolean doble) {
        super(x, y, velocidad);
        this.doble = doble;
    }
    public boolean esDoble() { return doble; }
    @Override protected void calcularMovimiento(float delta) { caer(delta, doble ? 0.9f : 1); }
    @Override protected boolean verificarColision(Personaje personaje) { return toca(personaje, doble ? 2 : 0); }
    @Override protected void aplicarEfecto(Personaje personaje) { personaje.recogerAgua(doble ? 20 : 10); }
    @Override protected void despuesDeAtrapar(Personaje personaje) {
        if (doble) personaje.avisar("Agua doble: hidratado y +2 cargas");
    }
    @Override public boolean esBuena() { return true; }
}
