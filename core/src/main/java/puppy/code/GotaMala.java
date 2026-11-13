package puppy.code;

public class GotaMala extends Gota {
    private final TipoObjeto tipo;

    public GotaMala(float x, float y, float velocidad) { this(x, y, velocidad, null); }

    GotaMala(float x, float y, float velocidad, TipoObjeto tipo) {
        super(x, y, velocidad);
        this.tipo = tipo;
    }

    public TipoObjeto getTipo() { return tipo == null ? TipoObjeto.SANGRE : tipo; }
    @Override protected void calcularMovimiento(float delta) { caer(delta, 1.15f); }
    @Override protected boolean verificarColision(Personaje personaje) { return toca(personaje, 4); }
    @Override protected void aplicarEfecto(Personaje personaje) {
        if (tipo == null) personaje.recibirDaño();
        else personaje.atrapar(tipo);
    }
    @Override public boolean esBuena() { return false; }
}
