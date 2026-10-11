package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lluvia {
    private final List<Gota> gotas = new ArrayList<>();
    private final List<Gota> vista = Collections.unmodifiableList(gotas);
    private float tiempo;

    public void actualizar(float delta, Tarro tarro, Nivel nivel) {
        tiempo += delta;
        while (tiempo >= nivel.getIntervalo()) {
            tiempo -= nivel.getIntervalo();
            float x = MathUtils.random(12f, 920f);
            gotas.add(MathUtils.random() < nivel.getProbabilidadMala()
                    ? new GotaMala(x, 495, nivel.getVelocidad())
                    : new GotaBuena(x, 495, nivel.getVelocidad()));
        }
        // Al recorrer al revés, borrar una gota no salta la siguiente.
        for (int i = gotas.size() - 1; i >= 0; i--) {
            if (tarro.getVidas() == 0) break;
            Gota gota = gotas.get(i);
            gota.actualizar(delta, tarro);
            if (gota.estaTerminada()) gotas.remove(i);
        }
    }
    public void limpiar() { gotas.clear(); tiempo = 0; }
    public List<Gota> getGotas() { return vista; }
}
