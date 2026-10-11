package puppy.code;

public enum Aspecto {
    EXPLORADOR("Explorador", "61D7D4", "243F61", 0, null, 0, 0),
    FONTANERO("Salta-tuberias", "EF6869", "435EB7", 20, null, 0, 1),
    VELOCISTA("Rayo azul", "568EFA", "E85F64", 35, null, 0, 2),
    CONSTRUCTOR("Minero cubico", "67CABB", "64719B", 35, null, 0, 3),
    ARACNIDO("Centinela arana", "D95170", "454DA0", 50, null, 0, 8),
    ACERO("Armadura solar", "C66A5B", "F0BE69", 70, null, 0, 11),
    NINJA("Ninja del alba", "F4A353", "353D60", 0, Dificultad.NOOB, 4, 5),
    PIRATA("Capitan del viento", "DB6860", "436CB1", 0, Dificultad.NOOB, 8, 6),
    GUERRERO("Guerrero astral", "F5A04F", "587DE0", 0, Dificultad.NOOB, 12, 4),
    HECHICERO("Hechicero nocturno", "8F84E7", "333E66", 0, Dificultad.PRO, 4, 12),
    ESCUDO("Guardian del escudo", "568DCB", "D06B77", 0, Dificultad.PRO, 8, 9),
    TRUENO("Forjador del trueno", "8CAFC6", "C36778", 0, Dificultad.PRO, 12, 10),
    MARINE("Marine infernal", "90A964", "4D685B", 0, Dificultad.HACKER, 4, 7),
    SOMBRA("Sombra digital", "5CF0C4", "3B4A60", 0, Dificultad.HACKER, 8, 12),
    COSMICO("Campeon cosmico", "EBD283", "9981D6", 0, Dificultad.HACKER, 12, 4);

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
