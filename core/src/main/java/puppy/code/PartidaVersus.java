package puppy.code;

public class PartidaVersus {
    private final Partida uno, dos;
    public PartidaVersus(ControlMovimiento controlUno, ControlMovimiento controlDos, Dificultad dificultad, long semilla) {
        uno = new Partida(controlUno, dificultad, semilla, 480);
        dos = new Partida(controlDos, dificultad, semilla, 480);
    }
    public void actualizar(float delta) {
        if (terminada()) return;
        uno.actualizar(delta); dos.actualizar(delta);
    }
    public boolean terminada() { return uno.terminada() || dos.terminada(); }
    public int getGanador() {
        if (!terminada()) return -1;
        if (uno.gano() && dos.gano() || uno.getPersonaje().getVidas() == 0 && dos.getPersonaje().getVidas() == 0) return 0;
        return uno.gano() || dos.getPersonaje().getVidas() == 0 ? 1 : 2;
    }
    public Partida getUno() { return uno; }
    public Partida getDos() { return dos; }
}
