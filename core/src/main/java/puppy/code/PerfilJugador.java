package puppy.code;

import com.badlogic.gdx.Preferences;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class PerfilJugador {
    private final Preferences datos;
    public PerfilJugador(Preferences datos) { this.datos = datos; }
    public int getMonedas() { return Math.max(0, datos.getInteger("monedas", 0)); }
    public int getNivelMaximo(Dificultad dificultad) { return datos.getInteger("nivel." + dificultad, 0); }
    public int[] getPuntuaciones(Dificultad dificultad) {
        int[] resultado = new int[5];
        for (int i = 0; i < resultado.length; i++) resultado[i] = datos.getInteger("top." + dificultad + "." + i, 0);
        return resultado;
    }
    public boolean tiene(Aspecto aspecto) {
        return aspecto == Aspecto.EXPLORADOR || datos.getBoolean("aspecto." + aspecto, false);
    }
    public Aspecto getEquipado() {
        try {
            Aspecto aspecto = Aspecto.valueOf(datos.getString("equipado", "EXPLORADOR"));
            return tiene(aspecto) ? aspecto : Aspecto.EXPLORADOR;
        } catch (IllegalArgumentException error) { return Aspecto.EXPLORADOR; }
    }
    public boolean comprar(Aspecto aspecto) {
        if (tiene(aspecto) || aspecto.getDificultad() != null || getMonedas() < aspecto.getPrecio()) return false;
        datos.putInteger("monedas", getMonedas() - aspecto.getPrecio());
        datos.putBoolean("aspecto." + aspecto, true);
        datos.flush();
        return true;
    }
    public boolean equipar(Aspecto aspecto) {
        if (!tiene(aspecto)) return false;
        datos.putString("equipado", aspecto.name());
        datos.flush();
        return true;
    }
    public List<Aspecto> registrarNivel(Dificultad dificultad, int nivel) {
        datos.putInteger("nivel." + dificultad, Math.max(nivel, getNivelMaximo(dificultad)));
        List<Aspecto> nuevos = new ArrayList<>();
        for (Aspecto aspecto : Aspecto.values()) {
            if (aspecto.getDificultad() == dificultad && aspecto.getNivel() <= nivel && !tiene(aspecto)) {
                datos.putBoolean("aspecto." + aspecto, true);
                nuevos.add(aspecto);
            }
        }
        datos.flush();
        return nuevos;
    }
    public boolean guardarResultado(Dificultad dificultad, int puntos, int monedas, boolean completa) {
        datos.putInteger("monedas", getMonedas() + Math.max(0, monedas));
        boolean record = completa && puntos > getPuntuaciones(dificultad)[0];
        if (completa && puntos > 0) {
            int[] top = Arrays.copyOf(getPuntuaciones(dificultad), 6);
            top[5] = puntos;
            Arrays.sort(top);
            for (int i = 0; i < 5; i++) datos.putInteger("top." + dificultad + "." + i, top[5 - i]);
        }
        datos.flush();
        return record;
    }
}
