package puppy.code;

public enum Aspecto {
    EXPLORADOR("Explorador", "61D7D4", "243F61", 0, null, 0, 0),
    FONTANERO("Mario", "EF6869", "435EB7", 180, null, 0, 1),
    VELOCISTA("Sonic", "568EFA", "E85F64", 264, null, 0, 2),
    CONSTRUCTOR("Steve", "67CABB", "64719B", 264, null, 0, 3),
    ARACNIDO("Spiderman", "D95170", "454DA0", 384, null, 0, 8),
    ACERO("Iron Man", "C66A5B", "F0BE69", 540, null, 0, 11),
    NINJA("Naruto", "F4A353", "353D60", 0, Dificultad.NOOB, 10, 5),
    PIRATA("Monkey D. Luffy", "DB6860", "436CB1", 0, Dificultad.NOOB, 20, 6),
    GUERRERO("Goku", "F5A04F", "587DE0", 0, Dificultad.NOOB, 30, 4),
    HECHICERO("Satoru Gojo", "8F84E7", "333E66", 0, Dificultad.PRO, 10, 12),
    ESCUDO("Capitan America", "568DCB", "D06B77", 0, Dificultad.PRO, 20, 9),
    TRUENO("Thor", "8CAFC6", "C36778", 0, Dificultad.PRO, 30, 10),
    MARINE("Doom Slayer", "90A964", "4D685B", 0, Dificultad.HACKER, 10, 7),
    SOMBRA("Megumi Fushiguro", "5CF0C4", "3B4A60", 0, Dificultad.HACKER, 20, 12),
    COSMICO("Vegeta", "EBD283", "9981D6", 0, Dificultad.HACKER, 30, 4),
    LUIGI("Luigi", "54B95D", "435EB7", 650, null, 0, 1),
    SHADOW("Shadow", "303440", "D95150", 800, null, 0, 2),
    ALEX("Alex", "7AAC65", "835846", 650, null, 0, 3),
    SASUKE("Sasuke", "727AA8", "403753", 850, null, 0, 5),
    ZORO("Roronoa Zoro", "588C60", "334B43", 900, null, 0, 17),
    YUJI("Yuji Itadori", "38445E", "BA5664", 850, null, 0, 18),
    GOHAN("Gohan", "8759AF", "CF5857", 1000, null, 0, 4),
    ARTHUR("Arthur Morgan", "65839B", "735340", 1200, null, 0, 13),
    PIKACHU("Pikachu", "F3D75E", "805B38", 1400, null, 0, 14),
    TRIPULANTE("Among Us", "CF4B5E", "852C48", 1100, null, 0, 15),
    GOKU_ULTRA("Goku Ultra Instinto", "E69B54", "5075BC", 0, Dificultad.NOOB, 31, 19),
    GOJO_DESPERTADO("Gojo Despertado", "F1F1EA", "313849", 0, Dificultad.PRO, 31, 12),
    SONIC_SUPER("Super Sonic", "F3D957", "D95654", 0, Dificultad.HACKER, 31, 2),
    ALIEN("Alien", "94D588", "536095", 0, null, 0, 16);

    private final String nombre, color, secundario;
    private final int precio, nivel, estilo;
    private final Dificultad dificultad;
    Aspecto(String nombre, String color, String secundario, int precio, Dificultad dificultad, int nivel, int estilo) {
        this.nombre = nombre; this.color = color; this.secundario = secundario;
        this.precio = precio; this.dificultad = dificultad; this.nivel = nivel; this.estilo = estilo;
    }
    public String getNombre() { return nombre; }
    public String getColor() { return color; }
    public String getSecundario() { return secundario; }
    public int getPrecio() { return precio; }
    public Dificultad getDificultad() { return dificultad; }
    public int getNivel() { return nivel; }
    public int getEstilo() { return estilo; }
    public boolean esOculto() { return this == ALIEN; }
    public boolean requiereVictoria() { return nivel == 31; }
    public boolean esComprable() { return precio > 0; }
    public String getRequisito() {
        if (esOculto()) return "Encuentra el secreto";
        if (requiereVictoria()) return "Vence al OVNI / " + dificultad;
        return dificultad == null ? precio + " monedas" : dificultad + " / nivel " + nivel;
    }
}
