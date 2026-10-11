package puppy.code;

public class GotaMala extends Gota {
    public GotaMala(float x, float y, float velocidad) { super(x, y, velocidad); }
    @Override protected void aplicarEfecto(Tarro tarro) { tarro.recibirDanio(); }
    @Override public boolean esBuena() { return false; }
}
