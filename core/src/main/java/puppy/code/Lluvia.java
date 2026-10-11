package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lluvia {
    private final List<Gota> gotas = new ArrayList<>();
    private final List<Gota> vista = Collections.unmodifiableList(gotas);
    private float tiempo;

    public void actualizar(float delta, Personaje personaje, Nivel nivel) {
        actualizar(delta, personaje, nivel, false);
    }

    public void actualizar(float delta, Personaje personaje, Nivel nivel, boolean secreto) {
        tiempo += delta;
        float intervalo = secreto ? 0.13f : nivel.getIntervalo();
        while (tiempo >= intervalo) {
            tiempo -= intervalo;
            float x = MathUtils.random(12f, 920f);
            gotas.add(crearGota(x, nivel, secreto));
        }
        // Al recorrer al revés, borrar una gota no salta la siguiente.
        for (int i = gotas.size() - 1; i >= 0; i--) {
            if (personaje.getVidas() == 0) break;
            Gota gota = gotas.get(i);
            gota.actualizar(delta, personaje);
            if (gota.estaTerminada()) gotas.remove(i);
        }
    }
    public void limpiar() { gotas.clear(); tiempo = 0; }
    private Gota crearGota(float x, Nivel nivel, boolean secreto) {
        float velocidad = secreto ? 240 : nivel.getVelocidad();
        if (secreto) return new ObjetoEspecial(x, 495, velocidad, TipoObjeto.MONEDA);
        float azar = MathUtils.random();
        if (azar < nivel.getProbabilidadMala()) {
            int peligro = MathUtils.random(5);
            if (peligro == 0) return new GotaMala(x, 495, velocidad);
            TipoObjeto[] malos = {TipoObjeto.ACIDO, TipoObjeto.VENENO, TipoObjeto.PAJARO,
                    TipoObjeto.TINTA, TipoObjeto.BOLSA_ROTA};
            return new ObjetoEspecial(x, 495, velocidad, malos[peligro - 1]);
        }
        if (azar < nivel.getProbabilidadMala() + 0.25f)
            return new ObjetoEspecial(x, 495, velocidad, TipoObjeto.MONEDA);
        if (azar < nivel.getProbabilidadMala() + 0.37f) {
            TipoObjeto[] buenos = {TipoObjeto.CAFE, TipoObjeto.MONEDAS_DOBLES, TipoObjeto.PUNTOS_DOBLES};
            return new ObjetoEspecial(x, 495, velocidad, buenos[MathUtils.random(2)]);
        }
        return new GotaBuena(x, 495, velocidad);
    }
    public List<Gota> getGotas() { return vista; }
}
