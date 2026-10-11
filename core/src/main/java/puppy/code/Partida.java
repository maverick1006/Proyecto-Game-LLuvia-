package puppy.code;

public class Partida {
    public static final float DURACION_NIVEL = 25;
    public static final float DURACION_SECRETO = 12;
    private final Personaje personaje;
    private final Lluvia lluvia = new Lluvia();
    private final Dificultad dificultad;
    private int numeroNivel = 1;
    private Nivel nivel;
    private float tiempoNivel;
    private float tiempoSecreto;
    private float avisoNivel = 2;
    public Partida(ControlMovimiento control, Dificultad dificultad) {
        personaje = new Personaje(control);
        this.dificultad = dificultad;
        nivel = dificultad.crearNivel(1);
    }
    public void actualizar(float delta) {
        if (terminada()) return;
        personaje.actualizar(delta);
        avisoNivel = Math.max(0, avisoNivel - delta);
        boolean eraSecreto = esSecreto();
        lluvia.actualizar(delta, personaje, nivel, eraSecreto);
        if (terminada()) return;
        if (eraSecreto) {
            tiempoSecreto = Math.max(0, tiempoSecreto - delta);
            if (!esSecreto()) { lluvia.limpiar(); avisoNivel = 2; }
        } else {
            tiempoNivel += delta;
            if (tiempoNivel >= DURACION_NIVEL) {
                tiempoNivel -= DURACION_NIVEL;
                numeroNivel++;
                nivel = dificultad.crearNivel(numeroNivel);
                avisoNivel = 2;
                lluvia.limpiar();
                if (numeroNivel % 5 == 0) tiempoSecreto = DURACION_SECRETO;
            }
        }
    }
    public boolean terminada() { return personaje.getVidas() == 0; }
    public boolean esSecreto() { return tiempoSecreto > 0; }
    public float getTiempoSecreto() { return tiempoSecreto; }
    public float getProgreso() { return tiempoNivel / DURACION_NIVEL; }
    public float getAvisoNivel() { return avisoNivel; }
    public int getNumeroNivel() { return numeroNivel; }
    public Dificultad getDificultad() { return dificultad; }
    public Personaje getPersonaje() { return personaje; }
    public Lluvia getLluvia() { return lluvia; }
}
