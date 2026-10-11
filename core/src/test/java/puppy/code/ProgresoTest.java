package puppy.code;

import com.badlogic.gdx.Preferences;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProgresoTest {
    private Personaje jugador() { return new Personaje((p, delta) -> { }); }
    private Preferences memoria() {
        Map<String, Object> valores = new HashMap<>();
        return (Preferences) Proxy.newProxyInstance(Preferences.class.getClassLoader(), new Class<?>[]{Preferences.class},
                (objeto, metodo, argumentos) -> {
                    String nombre = metodo.getName();
                    if (nombre.startsWith("put")) { valores.put((String) argumentos[0], argumentos[1]); return objeto; }
                    if (nombre.startsWith("get")) return valores.getOrDefault((String) argumentos[0], argumentos[1]);
                    return null;
                });
    }
    @Test public void cafeYVenenoSeCombinanYExpiran() {
        Personaje p = jugador();
        assertEquals(680, p.getVelocidad(), 0);
        p.atrapar(TipoObjeto.CAFE);
        assertEquals(1020, p.getVelocidad(), 0);
        p.atrapar(TipoObjeto.VENENO);
        assertEquals(510, p.getVelocidad(), 0);
        p.actualizar(5.1f);
        assertEquals(1020, p.getVelocidad(), 0);
        p.actualizar(2);
        assertEquals(680, p.getVelocidad(), 0);
    }
    @Test public void inmovilizacionTambienBloqueaControlTactil() {
        Personaje p = jugador();
        p.atrapar(TipoObjeto.PAJARO);
        p.moverHacia(900, 1);
        assertEquals(456, p.getX(), 0);
        p.actualizar(1.6f);
        p.moverHacia(900, 0.1f);
        assertEquals(524, p.getX(), 0);
    }
    @Test public void losMultiplicadoresNoSeAcumulanYLaBolsaSeRepara() {
        Personaje p = jugador();
        p.atrapar(TipoObjeto.MONEDAS_DOBLES);
        p.atrapar(TipoObjeto.MONEDAS_DOBLES);
        for (int i = 0; i < 4; i++) p.atrapar(TipoObjeto.MONEDA);
        assertEquals(8, p.getMonedas());
        p.atrapar(TipoObjeto.BOLSA_ROTA);
        p.atrapar(TipoObjeto.MONEDA);
        assertEquals(3, p.getMonedas());
        p.actualizar(4.1f);
        p.atrapar(TipoObjeto.MONEDA);
        assertEquals(5, p.getMonedas());
        p.atrapar(TipoObjeto.PUNTOS_DOBLES);
        p.sumarPuntos(10);
        assertEquals(20, p.getPuntos());
    }
    @Test public void peligrosNoDejanValoresNegativos() {
        Personaje p = jugador();
        p.atrapar(TipoObjeto.ACIDO);
        p.atrapar(TipoObjeto.ACIDO);
        assertEquals(2, p.getVidas());
        p.atrapar(TipoObjeto.TINTA);
        p.atrapar(TipoObjeto.BOLSA_ROTA);
        assertEquals(0, p.getPuntos());
        assertEquals(0, p.getMonedas());
    }
    @Test public void comprarDescuentaUnaVezYGuardaElEquipamiento() {
        Preferences datos = memoria();
        PerfilJugador perfil = new PerfilJugador(datos);
        assertFalse(perfil.comprar(Aspecto.FONTANERO));
        perfil.guardarResultado(Dificultad.NOOB, 0, 40, false);
        assertTrue(perfil.comprar(Aspecto.FONTANERO));
        assertFalse(perfil.comprar(Aspecto.FONTANERO));
        assertEquals(20, perfil.getMonedas());
        assertTrue(perfil.equipar(Aspecto.FONTANERO));
        PerfilJugador recargado = new PerfilJugador(datos);
        assertEquals(Aspecto.FONTANERO, recargado.getEquipado());
        assertEquals(20, recargado.getMonedas());
        assertFalse(perfil.equipar(Aspecto.MARINE));
    }
    @Test public void desbloqueosExigenDificultadYNivelCorrectos() {
        PerfilJugador perfil = new PerfilJugador(memoria());
        perfil.registrarNivel(Dificultad.NOOB, 3);
        assertFalse(perfil.tiene(Aspecto.NINJA));
        assertEquals(1, perfil.registrarNivel(Dificultad.NOOB, 4).size());
        assertTrue(perfil.tiene(Aspecto.NINJA));
        assertFalse(perfil.tiene(Aspecto.MARINE));
        assertTrue(perfil.registrarNivel(Dificultad.NOOB, 4).isEmpty());
        perfil.registrarNivel(Dificultad.NOOB, 2);
        assertEquals(4, perfil.getNivelMaximo(Dificultad.NOOB));
        perfil.guardarResultado(Dificultad.NOOB, 0, 500, false);
        assertFalse(perfil.comprar(Aspecto.MARINE));
    }
    @Test public void rankingOrdenaCincoYNoMezclaDificultadesNiAbandonos() {
        PerfilJugador perfil = new PerfilJugador(memoria());
        assertTrue(perfil.guardarResultado(Dificultad.NOOB, 100, 1, true));
        assertFalse(perfil.guardarResultado(Dificultad.NOOB, 100, 1, true));
        perfil.guardarResultado(Dificultad.NOOB, 20, 0, true);
        perfil.guardarResultado(Dificultad.NOOB, 90, 0, true);
        perfil.guardarResultado(Dificultad.NOOB, 200, 0, true);
        perfil.guardarResultado(Dificultad.NOOB, 300, 0, true);
        assertArrayEquals(new int[]{300, 200, 100, 100, 90}, perfil.getPuntuaciones(Dificultad.NOOB));
        assertFalse(perfil.guardarResultado(Dificultad.PRO, 9000, 5, false));
        assertEquals(0, perfil.getPuntuaciones(Dificultad.PRO)[0]);
        assertEquals(7, perfil.getMonedas());
    }
    @Test public void dificultadesIncrementanElRiesgo() {
        Nivel noob = Dificultad.NOOB.crearNivel(1), hacker = Dificultad.HACKER.crearNivel(1);
        assertTrue(hacker.getVelocidad() > noob.getVelocidad());
        assertTrue(hacker.getIntervalo() < noob.getIntervalo());
        assertTrue(hacker.getProbabilidadMala() > noob.getProbabilidadMala());
        assertTrue(Dificultad.NOOB.crearNivel(10).getVelocidad() > noob.getVelocidad());
    }
    @Test public void rondaSecretaSoloGeneraMonedas() {
        Lluvia lluvia = new Lluvia();
        lluvia.actualizar(0.5f, jugador(), Dificultad.HACKER.crearNivel(5), true);
        assertFalse(lluvia.getGotas().isEmpty());
        for (Gota gota : lluvia.getGotas()) {
            assertTrue(gota instanceof ObjetoEspecial);
            assertEquals(TipoObjeto.MONEDA, ((ObjetoEspecial) gota).getTipo());
        }
    }
    @Test public void nivelSecretoSeActivaYVuelveSinSaltarseUnNivel() {
        Partida partida = new Partida((p, delta) -> { }, Dificultad.NOOB);
        // Se retiran las gotas para aislar el reloj de la progresion.
        for (int i = 0; i < 1000; i++) { partida.actualizar(0.1f); partida.getLluvia().limpiar(); }
        assertEquals(5, partida.getNumeroNivel());
        assertTrue(partida.esSecreto());
        for (int i = 0; i < 121; i++) { partida.actualizar(0.1f); partida.getLluvia().limpiar(); }
        assertFalse(partida.esSecreto());
        assertEquals(5, partida.getNumeroNivel());
        assertFalse(partida.terminada());
    }
}
