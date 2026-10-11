package puppy.code;

public class ObjetoEspecial extends Gota {
    private final TipoObjeto tipo;
    public ObjetoEspecial(float x, float y, float velocidad, TipoObjeto tipo) {
        super(x, y, velocidad);
        this.tipo = tipo;
    }
    @Override protected void aplicarEfecto(Personaje personaje) { personaje.atrapar(tipo); }
    @Override public boolean esBuena() { return tipo.esPositivo(); }
    public TipoObjeto getTipo() { return tipo; }
}
