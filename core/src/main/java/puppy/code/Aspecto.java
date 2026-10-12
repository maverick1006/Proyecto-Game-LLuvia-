package puppy.code;

public enum Aspecto {
    EXPLORADOR("Explorador", "61D7D4", "243F61", 0, null, 0, 0),
    FONTANERO("Mario", "EF6869", "435EB7", 150, null, 0, 1),
    VELOCISTA("Sonic", "568EFA", "E85F64", 220, null, 0, 2),
    CONSTRUCTOR("Steve", "67CABB", "64719B", 220, null, 0, 3),
    ARACNIDO("Spiderman", "D95170", "454DA0", 320, null, 0, 8),
    ACERO("Iron Man", "C66A5B", "F0BE69", 450, null, 0, 11),
    NINJA("Naruto", "F4A353", "353D60", 0, Dificultad.NOOB, 10, 5),
    PIRATA("Monkey D. Luffy", "DB6860", "436CB1", 0, Dificultad.NOOB, 20, 6),
    GUERRERO("Goku", "F5A04F", "587DE0", 0, Dificultad.NOOB, 30, 4),
    HECHICERO("Satoru Gojo", "8F84E7", "333E66", 0, Dificultad.PRO, 10, 12),
    ESCUDO("Capitan America", "568DCB", "D06B77", 0, Dificultad.PRO, 20, 9),
    TRUENO("Thor", "8CAFC6", "C36778", 0, Dificultad.PRO, 30, 10),
    MARINE("Doom Slayer", "90A964", "4D685B", 0, Dificultad.HACKER, 10, 7),
    SOMBRA("Megumi Fushiguro", "5CF0C4", "3B4A60", 0, Dificultad.HACKER, 20, 12),
    COSMICO("Vegeta", "EBD283", "9981D6", 0, Dificultad.HACKER, 30, 4);

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
    public String getRequisito() { return dificultad == null ? precio + " monedas" : dificultad + " / nivel " + nivel; }
}
