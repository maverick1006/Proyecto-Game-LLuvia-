package puppy.code;

public final class Nivel {
    private final String nombre;
    private final int objetivo;
    private final float velocidad;
    private final float intervalo;
    private final float probabilidadMala;

    private Nivel(Builder b) {
        nombre = b.nombre;
        objetivo = b.objetivo;
        velocidad = b.velocidad;
        intervalo = b.intervalo;
        probabilidadMala = b.probabilidadMala;
    }
    public String getNombre() { return nombre; }
    public int getObjetivo() { return objetivo; }
    public float getVelocidad() { return velocidad; }
    public float getIntervalo() { return intervalo; }
    public float getProbabilidadMala() { return probabilidadMala; }

    public static class Builder {
        private final String nombre;
        private int objetivo = 100;
        private float velocidad = 220;
        private float intervalo = 0.65f;
        private float probabilidadMala = 0.2f;
        public Builder(String nombre) { this.nombre = nombre; }
        public Builder objetivo(int valor) { objetivo = valor; return this; }
        public Builder velocidad(float valor) { velocidad = valor; return this; }
        public Builder intervalo(float valor) { intervalo = valor; return this; }
        public Builder probabilidadMala(float valor) { probabilidadMala = valor; return this; }
        public Nivel construir() {
            if (nombre == null || nombre.trim().isEmpty() || objetivo <= 0
                    || !Float.isFinite(velocidad) || velocidad <= 0
                    || !Float.isFinite(intervalo) || intervalo <= 0
                    || !Float.isFinite(probabilidadMala) || probabilidadMala < 0 || probabilidadMala > 1) {
                throw new IllegalArgumentException("Revisa los datos del nivel");
            }
            return new Nivel(this);
        }
    }
}
