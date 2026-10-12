package puppy.code;

import org.junit.Test;
import static org.junit.Assert.*;

public class CampanaTest {
    @Test public void vidasDependenDeLaDificultadEnCampanaYVersus() {
        int[] vidas = {5, 3, 1};
        for (Dificultad dificultad : Dificultad.values()) {
            Partida juego = new Partida((p, dt) -> { }, dificultad);
            assertEquals(vidas[dificultad.ordinal()], juego.getPersonaje().getVidas());
            PartidaVersus versus = new PartidaVersus((p, dt) -> { }, (p, dt) -> { }, dificultad, 1);
            assertEquals(vidas[dificultad.ordinal()], versus.getUno().getPersonaje().getVidas());
            assertEquals(vidas[dificultad.ordinal()], versus.getDos().getPersonaje().getVidas());
            for (int i = 0; i < vidas[dificultad.ordinal()]; i++) {
                juego.getPersonaje().actualizar(1.3f); juego.getPersonaje().recibirDanio();
            }
            assertTrue(juego.terminada());
        }
    }
    private Partida partida() { return new Partida((p, dt) -> { }, Dificultad.NOOB, 12, 960); }
    @Test public void esperarNoSubeDeNivel() {
        Partida juego = partida();
        for (int i = 0; i < 1000; i++) { juego.actualizar(0.1f); juego.getLluvia().limpiar(); }
        assertEquals(1, juego.getNumeroNivel());
        assertEquals(0, juego.getPersonaje().getPuntos());
    }
    @Test public void aguaSubeNivelYPenalizacionesLoBajan() {
        Partida juego = partida();
        juego.getPersonaje().recogerAgua(90); juego.actualizar(0.01f);
        assertEquals(1, juego.getNumeroNivel());
        juego.getPersonaje().recogerAgua(10); juego.actualizar(0.01f);
        assertEquals(2, juego.getNumeroNivel());
        juego.getPersonaje().atrapar(TipoObjeto.SANGRE); juego.actualizar(0.01f);
        assertEquals(1, juego.getNumeroNivel());
        assertEquals(2, juego.getMaximoNivel());
        assertEquals(0.8f, juego.getProgreso(), 0.001f);
    }
    @Test public void sedEmpiezaDespuesDeCuatroSegundosYAguaLaQuita() {
        Personaje p = partida().getPersonaje();
        p.actualizar(4); assertEquals(680, p.getVelocidad(), 0);
        p.actualizar(1); assertTrue(p.getVelocidad() < 680);
        p.actualizar(20); assertEquals(272, p.getVelocidad(), 0.01f);
        p.atrapar(TipoObjeto.MONEDA); assertEquals(272, p.getVelocidad(), 0.01f);
        p.recogerAgua(10); assertEquals(680, p.getVelocidad(), 0);
        assertEquals(1, p.getAgua());
    }
    @Test public void zonasTienenLimitesExactos() {
        assertEquals(Zona.LLANURA, Zona.delNivel(1)); assertEquals(Zona.LLANURA, Zona.delNivel(10));
        assertEquals(Zona.HIELO, Zona.delNivel(11)); assertEquals(Zona.HIELO, Zona.delNivel(20));
        assertEquals(Zona.DESIERTO, Zona.delNivel(21)); assertEquals(Zona.DESIERTO, Zona.delNivel(30));
    }
    @Test public void treintaNivelesSeCompletanConTresMilPuntos() {
        Partida juego = partida();
        for (int nivel = 1; nivel <= 30; nivel++) {
            assertEquals(nivel, juego.getNumeroNivel());
            if (juego.esSecreto()) for (int i = 0; i < 32; i++) { juego.actualizar(0.25f); juego.getLluvia().limpiar(); }
            juego.getPersonaje().recogerAgua(100); juego.actualizar(0.01f);
        }
        assertEquals(Partida.Fase.ASCENSO, juego.getFase());
        assertEquals(30, juego.getNumeroNivel());
        juego.actualizar(3);
        assertTrue(juego.enJefe()); assertEquals(Zona.ESPACIO, juego.getZona());
    }
    @Test public void secretoNoSeRepiteAlBajarYSubirYNoDaSed() {
        Partida juego = partida();
        juego.getPersonaje().recogerAgua(400); juego.actualizar(0.01f);
        assertEquals(8, juego.getTiempoSecreto(), 0);
        for (int i = 0; i < 32; i++) { juego.actualizar(0.25f); juego.getLluvia().limpiar(); }
        assertFalse(juego.esSecreto()); assertEquals(680, juego.getPersonaje().getVelocidad(), 0);
        juego.getPersonaje().quitarPuntos(20); juego.actualizar(0.01f);
        juego.getPersonaje().recogerAgua(20); juego.actualizar(0.01f);
        assertEquals(5, juego.getNumeroNivel()); assertFalse(juego.esSecreto());
    }
    @Test public void termoProtegeDelFrioPeroNoDelFuego() {
        Personaje p = partida().getPersonaje();
        p.recogerAgua(100); p.atrapar(TipoObjeto.TERMO); p.atrapar(TipoObjeto.HIELO);
        assertEquals(100, p.getPuntos()); assertFalse(p.activo(TipoObjeto.HIELO));
        p.atrapar(TipoObjeto.FUEGO); assertEquals(60, p.getPuntos()); assertEquals(4, p.getVidas());
    }
    @Test public void ovniConsumeAguaYRespetaLaCadencia() {
        Personaje p = partida().getPersonaje(); JefeOvni jefe = new JefeOvni(Dificultad.NOOB, 960, 1);
        assertFalse(jefe.disparar(p));
        p.recogerAgua(20); assertTrue(jefe.disparar(p)); assertEquals(1, p.getAgua());
        assertFalse(jefe.disparar(p));
        jefe.actualizar(0.21f, p, new Lluvia()); assertTrue(jefe.disparar(p)); assertEquals(0, p.getAgua());
    }
    @Test public void aguaSigueApareciendoDuranteLaPelea() {
        Personaje p = partida().getPersonaje(); JefeOvni jefe = new JefeOvni(Dificultad.HACKER, 960, 1);
        Lluvia lluvia = new Lluvia(); jefe.actualizar(1.3f, p, lluvia);
        assertTrue(lluvia.getGotas().stream().anyMatch(g -> g instanceof GotaBuena));
        assertTrue(lluvia.getGotas().stream().anyMatch(g -> g instanceof ObjetoEspecial));
    }
    @Test public void jefeSePuedeVencerConChorrosRealesYBolsaSeEntregaUnaVez() {
        Partida juego = partida(); juego.getPersonaje().recogerAgua(3000); juego.actualizar(0.01f); juego.actualizar(3);
        for (int i = 0; i < 20000 && !juego.terminada(); i++) {
            JefeOvni jefe = juego.getJefe();
            juego.getPersonaje().colocar(jefe.getX() + 40);
            juego.disparar(); juego.actualizar(0.01f); juego.getLluvia().limpiar();
        }
        assertTrue(juego.gano()); assertEquals(250, juego.getPremio());
        int monedas = juego.getMonedasFinales(); juego.actualizar(100);
        assertEquals(monedas, juego.getMonedasFinales());
    }
    @Test public void versusTieneMundosSeparadosYLluviaInicialIgual() {
        PartidaVersus versus = new PartidaVersus((p, dt) -> p.mover(-5000), (p, dt) -> p.mover(5000), Dificultad.PRO, 77);
        versus.actualizar(0.5f);
        assertEquals(0, versus.getUno().getPersonaje().getX(), 0);
        assertEquals(432, versus.getDos().getPersonaje().getX(), 0);
        Gota a = versus.getUno().getLluvia().getGotas().get(0), b = versus.getDos().getLluvia().getGotas().get(0);
        assertEquals(a.getClass(), b.getClass()); assertEquals(a.getX(), b.getX(), 0);
        assertFalse(versus.terminada());
        for (int i = 0; i < 3; i++) { versus.getUno().getPersonaje().actualizar(1.3f); versus.getUno().getPersonaje().recibirDanio(); }
        assertEquals(2, versus.getGanador());
    }
    @Test public void ambosMuertosEsEmpate() {
        PartidaVersus versus = new PartidaVersus((p, dt) -> { }, (p, dt) -> { }, Dificultad.NOOB, 1);
        for (Partida p : new Partida[]{versus.getUno(), versus.getDos()})
            for (int i = 0; i < 5; i++) { p.getPersonaje().actualizar(1.3f); p.getPersonaje().recibirDanio(); }
        assertEquals(0, versus.getGanador());
    }
}
