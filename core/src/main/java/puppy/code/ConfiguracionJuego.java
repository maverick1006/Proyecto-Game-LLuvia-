package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class ConfiguracionJuego {
    private static ConfiguracionJuego instancia;
    private final Preferences preferencias;
    private boolean sonido;
    private int record;
    private final PerfilJugador perfil;

    private ConfiguracionJuego() {
        preferencias = Gdx.app.getPreferences(System.getProperty("gamelluvia.perfil", "gamelluvia"));
        perfil = new PerfilJugador(preferencias);
        sonido = preferencias.getBoolean("sonido", true);
        record = preferencias.getInteger("record", 0);
    }
    public static ConfiguracionJuego getInstancia() {
        if (instancia == null) instancia = new ConfiguracionJuego();
        return instancia;
    }
    public boolean tieneSonido() { return sonido; }
    public PerfilJugador getPerfil() { return perfil; }
    public int getRecord() { return record; }
    public void alternarSonido() {
        sonido = !sonido;
        preferencias.putBoolean("sonido", sonido).flush();
    }
    public void registrarPuntos(int puntos) {
        if (puntos > record) {
            record = puntos;
            preferencias.putInteger("record", record).flush();
        }
    }
}
