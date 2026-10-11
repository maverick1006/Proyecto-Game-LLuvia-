package puppy.code;

public enum TipoObjeto {
    MONEDA("$", "FFD36A", "Moneda", "Moneda para la tienda", 0, true),
    CAFE("C", "D79B65", "Cafe", "Velocidad x1.5 durante 7 s", 7, true),
    MONEDAS_DOBLES("$2", "FFE993", "Monedas x2", "Monedas dobles durante 8 s", 8, true),
    PUNTOS_DOBLES("P2", "9DACFF", "Puntos x2", "Puntos dobles durante 8 s", 8, true),
    ACIDO("A", "B2F26B", "Acido", "Acido: una vida y 30 puntos menos", 0, false),
    VENENO("V", "BD87E8", "Veneno", "Veneno: velocidad a la mitad por 5 s", 5, false),
    PAJARO("X", "C7AF91", "Pajaro", "Sorpresa de pajaro: inmovil durante 1.5 s", 1.5f, false),
    TINTA("-P", "8398AA", "Tinta", "Mancha de tinta: pierdes 50 puntos", 0, false),
    BOLSA_ROTA("-$", "FF9A86", "Bolsa rota", "Pierdes 5 monedas y no recoges durante 4 s", 4, false);

    private final String simbolo, color, nombre, descripcion;
    private final float duracion;
    private final boolean positivo;
    TipoObjeto(String simbolo, String color, String nombre, String descripcion, float duracion, boolean positivo) {
        this.simbolo = simbolo; this.color = color; this.nombre = nombre;
        this.descripcion = descripcion; this.duracion = duracion; this.positivo = positivo;
    }
    public String getSimbolo() { return simbolo; }
    public String getColor() { return color; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public float getDuracion() { return duracion; }
    public boolean esPositivo() { return positivo; }
}
