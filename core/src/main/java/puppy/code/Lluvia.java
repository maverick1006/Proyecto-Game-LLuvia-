package puppy.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Lluvia {
    private final List<Gota> gotas = new ArrayList<>();
    private final List<Gota> vista = Collections.unmodifiableList(gotas);
    private final Random azar;
    private final float ancho;
    private float tiempo;
    public Lluvia() { this(System.nanoTime(), 960); }
    public Lluvia(long semilla, float ancho) { azar = new Random(semilla); this.ancho = ancho; }
    public void actualizar(float delta, Personaje personaje, Nivel nivel) { actualizar(delta, personaje, nivel, false); }
    public void actualizar(float delta, Personaje personaje, Nivel nivel, boolean secreto) {
        actualizar(delta, personaje, nivel, secreto, Zona.LLANURA);
    }
    public void actualizar(float delta, Personaje personaje, Nivel nivel, boolean secreto, Zona zona) {
        tiempo += delta;
        float intervalo = secreto ? 0.13f : nivel.getIntervalo();
        while (tiempo >= intervalo) {
            tiempo -= intervalo;
            gotas.add(crearGota(12 + azar.nextFloat() * (ancho - 52), nivel, secreto, zona));
        }
        mover(delta, personaje);
    }
    public void mover(float delta, Personaje personaje) {
        // Se recorre al reves para no saltar elementos al borrar.
        for (int i = gotas.size() - 1; i >= 0; i--) {
            if (personaje.getVidas() == 0) break;
            Gota gota = gotas.get(i);
            gota.actualizar(delta, personaje);
            if (gota.estaTerminada()) gotas.remove(i);
        }
    }
    public void lanzar(Gota gota) { gotas.add(gota); }
    public void limpiar() { gotas.clear(); tiempo = 0; }
    private Gota crearGota(float x, Nivel nivel, boolean secreto, Zona zona) {
        float velocidad = secreto ? 240 : nivel.getVelocidad();
        if (secreto) return new ObjetoEspecial(x, 495, velocidad, TipoObjeto.MONEDA);
        float valor = azar.nextFloat();
        TipoObjeto tipo;
        if (valor < nivel.getProbabilidadMala()) {
            TipoObjeto[] peligros = zona.peligros();
            tipo = peligros[azar.nextInt(peligros.length)];
        } else if (valor < nivel.getProbabilidadMala() + 0.14f) tipo = TipoObjeto.MONEDA;
        else if (valor < nivel.getProbabilidadMala() + 0.27f) {
            TipoObjeto[] mejoras = zona.mejoras();
            tipo = mejoras[azar.nextInt(mejoras.length)];
        } else return new GotaBuena(x, 495, velocidad);
        return new ObjetoEspecial(x, 495, velocidad, tipo);
    }
    public List<Gota> getGotas() { return vista; }
}
