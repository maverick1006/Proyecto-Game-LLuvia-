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
        assertTrue(jugador.recibirDanio());
        jugador.actualizar(0.6f);
        assertFalse(jugador.recibirDanio());
        jugador.actualizar(0.61f);
        assertTrue(jugador.recibirDanio());
        jugador.actualizar(1.21f);
        assertTrue(jugador.recibirDanio());
        jugador.actualizar(2);
        assertFalse(jugador.recibirDanio());
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

    @Test public void elControlSePuedeSustituirSinModificarElPersonaje() {
        Personaje jugador = new Personaje((t, delta) -> t.mover(100 * delta));
        jugador.actualizar(0.5f);
        assertEquals(506, jugador.getX(), 0.001f);
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
