package puppy.code;

public enum Zona {
    LLANURA("Llanura"), HIELO("Hielo"), DESIERTO("Atacama"), ESPACIO("Espacio exterior");
    private final String nombre;
    Zona(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
    public static Zona delNivel(int nivel) { return nivel <= 10 ? LLANURA : nivel <= 20 ? HIELO : DESIERTO; }
    public TipoObjeto[] peligros() {
        switch (this) {
            case LLANURA: return new TipoObjeto[]{TipoObjeto.SANGRE, TipoObjeto.VENENO, TipoObjeto.ACIDO, TipoObjeto.PAJARO};
            case HIELO: return new TipoObjeto[]{TipoObjeto.NIEVE, TipoObjeto.HIELO, TipoObjeto.ACIDO};
            case DESIERTO: return new TipoObjeto[]{TipoObjeto.FUEGO, TipoObjeto.CACTUS, TipoObjeto.TINTA};
            default: return new TipoObjeto[]{TipoObjeto.SANGRE, TipoObjeto.VENENO, TipoObjeto.ACIDO, TipoObjeto.HIELO, TipoObjeto.FUEGO, TipoObjeto.CACTUS, TipoObjeto.BOLSA_ROTA};
        }
    }
    public TipoObjeto[] mejoras() {
        switch (this) {
            case HIELO: return new TipoObjeto[]{TipoObjeto.CAFE, TipoObjeto.TERMO, TipoObjeto.AGUA_DOBLE, TipoObjeto.PUNTOS_DOBLES};
            case DESIERTO: return new TipoObjeto[]{TipoObjeto.OASIS, TipoObjeto.FRUTA, TipoObjeto.AGUA_DOBLE, TipoObjeto.MONEDAS_DOBLES};
            default: return new TipoObjeto[]{TipoObjeto.AGUA_DOBLE, TipoObjeto.PUNTOS_DOBLES, TipoObjeto.MONEDAS_DOBLES};
        }
    }
}
