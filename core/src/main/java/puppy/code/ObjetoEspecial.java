package puppy.code;

public class ObjetoEspecial extends Gota {
    private final TipoObjeto tipo;
    public ObjetoEspecial(float x, float y, float velocidad, TipoObjeto tipo) {
        super(x, y, velocidad);
        this.tipo = tipo;
    }
    @Override protected void calcularMovimiento(float delta) { caer(delta, tipo.esPositivo() ? 0.9f : 1.05f); }
    @Override protected boolean verificarColision(Personaje personaje) { return toca(personaje, 2); }
    @Override protected void aplicarEfecto(Personaje personaje) { personaje.atrapar(tipo); }
    @Override public boolean esBuena() { return tipo.esPositivo(); }
    public TipoObjeto getTipo() { return tipo; }
}
