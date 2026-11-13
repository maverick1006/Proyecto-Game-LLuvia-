package puppy.code;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReglasJuegoTest {
    private Personaje personaje() { return new Personaje((jugador, delta) -> { }); }

    @Test public void unaGotaSoloSeRecogeUnaVez() {
        Personaje jugador = personaje();
        Gota gota = new GotaBuena(jugador.getX(), jugador.getY(), 200);
        gota.actualizar(0, jugador);
        gota.actualizar(0, jugador);
        assertEquals(10, jugador.getPuntos());
        assertTrue(gota.estaTerminada());
    }

    @Test public void variasGotasMalasNoQuitanTodasLasVidasEnUnFotograma() {
        Personaje jugador = personaje();
        new GotaMala(jugador.getX(), jugador.getY(), 200).actualizar(0, jugador);
        new GotaMala(jugador.getX(), jugador.getY(), 200).actualizar(0, jugador);
        assertEquals(2, jugador.getVidas());
    }

    @Test public void elDanioSeRecuperaPorTiempoYLasVidasNoSonNegativas() {
        Personaje jugador = personaje();
        assertTrue(jugador.recibirDaño());
        jugador.actualizar(0.6f);
        assertFalse(jugador.recibirDaño());
        jugador.actualizar(0.61f);
        assertTrue(jugador.recibirDaño());
        jugador.actualizar(1.21f);
        assertTrue(jugador.recibirDaño());
        jugador.actualizar(2);
        assertFalse(jugador.recibirDaño());
        jugador.sumarPuntos(10);
        assertEquals(0, jugador.getVidas());
        assertEquals(0, jugador.getPuntos());
    }

    @Test public void unaGotaFueraDePantallaTerminaSinDarPuntos() {
        Personaje jugador = personaje();
        Gota gota = new GotaBuena(100, -40, 200);
        gota.actualizar(0, jugador);
        assertTrue(gota.estaTerminada());
        assertEquals(0, jugador.getPuntos());
    }

    @Test public void elPersonajeRespetaAmbosBordes() {
        Personaje jugador = personaje();
        jugador.colocar(-100);
        assertEquals(0, jugador.getX(), 0);
        jugador.colocar(2000);
        assertEquals(912, jugador.getX(), 0);
    }

    @Test public void seActualizanTodasLasGotasAunqueSeEliminenVarias() {
        Personaje jugador = personaje();
        jugador.colocar(0);
        Lluvia lluvia = new Lluvia();
        Nivel nivel = new Nivel.Builder("Prueba").intervalo(0.5f).velocidad(10000).construir();
        lluvia.actualizar(2, jugador, nivel);
        assertTrue(lluvia.getGotas().isEmpty());
        assertEquals(3, jugador.getVidas());
    }

    @Test public void unaPartidaCambiaDeControlSinPerderElProgreso() {
        Partida partida = new Partida((p, dt) -> p.mover(100 * dt), Dificultad.NOOB, 12, 960);
        Personaje jugador = partida.getPersonaje();
        jugador.recogerAgua(10);
        partida.actualizar(0.1f);
        float antes = jugador.getX();

        jugador.setControlMovimiento((p, dt) -> p.mover(-100 * dt));
        partida.actualizar(0.1f);

        assertSame(jugador, partida.getPersonaje());
        assertEquals(antes - 10, jugador.getX(), 0.001f);
        assertEquals(10, jugador.getPuntos());
        assertEquals(1, jugador.getAgua());
    }

    @Test public void elBordeDeLaGotaContaminadaNoCuentaComoImpacto() {
        Personaje jugador = personaje();
        float x = jugador.getX() - 26;
        Gota agua = new GotaBuena(x, jugador.getY(), 200);
        Gota acido = new GotaMala(x, jugador.getY(), 200, TipoObjeto.ACIDO);

        agua.actualizar(0, jugador);
        acido.actualizar(0, jugador);

        assertTrue(agua.estaTerminada());
        assertFalse(acido.estaTerminada());
        assertEquals(3, jugador.getVidas());
        assertEquals(10, jugador.getPuntos());
    }

    @Test public void laGotaContaminadaCaeMasRapidoYAplicaElEfectoDeAcido() {
        Personaje jugador = personaje();
        Gota agua = new GotaBuena(jugador.getX(), 130, 200);
        Gota acido = new GotaMala(jugador.getX(), 130, 200, TipoObjeto.ACIDO);
        agua.actualizar(0.1f, jugador);
        acido.actualizar(0.1f, jugador);
        assertTrue(acido.getY() < agua.getY());
        assertFalse(acido.estaTerminada());

        jugador.sumarPuntos(40);
        acido.actualizar(0.25f, jugador);
        assertTrue(acido.estaTerminada());
        assertEquals(2, jugador.getVidas());
        assertEquals(10, jugador.getPuntos());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unNivelNoAceptaIntervaloCero() {
        new Nivel.Builder("Invalido").intervalo(0).construir();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unNivelNoAceptaProbabilidadesInvalidas() {
        new Nivel.Builder("Invalido").probabilidadMala(Float.NaN).construir();
    }

    @Test public void elBuilderNoModificaNivelesYaConstruidos() {
        Nivel.Builder builder = new Nivel.Builder("Brisa").objetivo(100);
        Nivel primero = builder.construir();
        Nivel segundo = builder.objetivo(200).construir();
        assertEquals(100, primero.getObjetivo());
        assertEquals(200, segundo.getObjetivo());
    }
}
