package puppy.code;

public enum TipoObjeto {
    AGUA_DOBLE("2", "6BDBF2", "Agua doble", "+20 puntos y dos cargas de agua", 0, true),
    SANGRE("", "B5394D", "Sangre", "Pierdes 20 puntos", 0, false),
    NIEVE("", "EDF9FF", "Nieve", "-20 puntos y lentitud por 4 s", 4, false),
    HIELO("", "9DDEEF", "Hielo", "-30 puntos e inmovil por 1.5 s", 1.5f, false),
    FUEGO("", "FF8C40", "Fuego", "Una vida y 40 puntos menos", 0, false),
    CACTUS("", "56A875", "Cactus", "Una vida y 25 puntos menos", 0, false),
    TERMO("", "DC6E57", "Termo caliente", "Proteccion contra nieve y hielo: 8 s", 8, true),
    OASIS("", "68E1DC", "Agua de oasis", "+30 puntos y tres cargas de agua", 0, true),
    FRUTA("", "EC799F", "Fruta del cactus", "Velocidad x1.5 durante 7 s", 7, true),
    MONEDA("$", "FFD36A", "Moneda", "Moneda para la tienda", 0, true),
    CAFE("C", "D79B65", "Cafe", "Velocidad x1.5 durante 7 s", 7, true),
    MONEDAS_DOBLES("$2", "FFE993", "Monedas x2", "Monedas dobles durante 8 s", 8, true),
    PUNTOS_DOBLES("P2", "9DACFF", "Puntos x2", "Puntos dobles durante 8 s", 8, true),
    ACIDO("A", "B2F26B", "Acido", "Acido: una vida y 30 puntos menos", 0, false),
    VENENO("V", "BD87E8", "Veneno", "-10 puntos y lentitud durante 5 s", 5, false),
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
