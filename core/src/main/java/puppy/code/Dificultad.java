package puppy.code;

public enum Dificultad {
    NOOB(5, 200, 0.62f, 0.16f, "Para aprender y juntar monedas"),
    PRO(3, 290, 0.48f, 0.25f, "Mas velocidad y mas peligros"),
    HACKER(1, 370, 0.36f, 0.34f, "Reflejos rapidos, lluvia intensa");
    private final float velocidad, intervalo, peligro;
    private final String descripcion;
    private final int vidas;
    Dificultad(int vidas, float velocidad, float intervalo, float peligro, String descripcion) {
        this.velocidad = velocidad; this.intervalo = intervalo; this.peligro = peligro;
        this.descripcion = descripcion; this.vidas = vidas;
    }
    public int getVidas() { return vidas; }
    public String getDescripcion() { return descripcion; }
    public Nivel crearNivel(int numero) {
        return new Nivel.Builder(name() + " / " + numero).objetivo(numero * 100)
                .velocidad(Math.min(620, velocidad + (numero - 1) * 15))
                .intervalo(Math.max(0.2f, intervalo - (numero - 1) * 0.015f))
                .probabilidadMala(Math.min(0.48f, peligro + (numero - 1) * 0.01f)).construir();
    }
}
