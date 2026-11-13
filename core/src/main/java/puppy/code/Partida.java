package puppy.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Partida {
    public static final int TOTAL_NIVELES = 30;
    public static final int PUNTOS_POR_NIVEL = 100;
    public static final float DURACION_SECRETO = 8;
    public enum Fase { CAMPANA, ASCENSO, JEFE, VICTORIA, DERROTA }
    private final Personaje personaje;
    private final Lluvia lluvia;
    private final Dificultad dificultad;
    private final float ancho;
    private final long semilla;
    private int numeroNivel = 1;
    private int maximoNivel = 1;
    private int ultimoSecreto;
    private Nivel nivel;
    private float tiempoSecreto, avisoNivel = 2, tiempoAscenso;
    private Fase fase = Fase.CAMPANA;
    private JefeOvni jefe;
    private final List<ChorroAgua> chorros = new ArrayList<>();
    private SubordinadoOvni patrulla;
    private Zona zonaPatrulla;
    private float esperaPatrulla = 6, esperaDisparo;
    private boolean desafioOculto;
    public Partida(ControlMovimiento control, Dificultad dificultad) { this(control, dificultad, System.nanoTime(), 960); }
    public Partida(ControlMovimiento control, Dificultad dificultad, long semilla, float ancho) {
        personaje = new Personaje(control, ancho, dificultad.getVidas()); lluvia = new Lluvia(semilla, ancho);
        this.dificultad = dificultad; this.semilla = semilla; this.ancho = ancho;
        nivel = dificultad.crearNivel(1);
    }
    public static Partida crearDesafioOculto(ControlMovimiento control) {
        Partida juego = new Partida(control, Dificultad.PRO);
        juego.desafioOculto = true;
        juego.fase = Fase.JEFE;
        juego.jefe = new JefeOvni(Dificultad.PRO, juego.ancho, juego.semilla);
        juego.personaje.cargarAgua(40);
        juego.actualizarMovimientoTerreno();
        return juego;
    }
    public boolean esDesafioOculto() { return desafioOculto; }
    public void actualizar(float delta) {
        if (terminada()) return;
        esperaDisparo = Math.max(0, esperaDisparo - delta);
        avisoNivel = Math.max(0, avisoNivel - delta);
        if (fase == Fase.ASCENSO) {
            tiempoAscenso += delta;
            personaje.actualizar(delta, false);
            personaje.colocarAltura(42 + getAscenso() * 290);
            actualizarPatrulla(delta, Zona.ESPACIO);
            lluvia.mover(delta, personaje);
            if (tiempoAscenso >= 3) {
                personaje.colocarAltura(42);
                lluvia.limpiar();
                fase = Fase.JEFE;
                patrulla = null;
                chorros.clear();
                jefe = new JefeOvni(dificultad, ancho, semilla);
                actualizarMovimientoTerreno();
            }
            return;
        }
        personaje.actualizar(delta, !esSecreto());
        if (fase == Fase.JEFE) {
            jefe.actualizar(delta, personaje, lluvia);
            lluvia.mover(delta, personaje);
            if (personaje.getVidas() == 0) fase = Fase.DERROTA;
            else if (jefe.derrotado()) { fase = Fase.VICTORIA; lluvia.limpiar(); }
            return;
        }
        boolean secreto = esSecreto();
        if (!secreto) actualizarPatrulla(delta, getZona());
        lluvia.actualizar(delta, personaje, nivel, secreto, getZona());
        if (personaje.getVidas() == 0) { fase = Fase.DERROTA; return; }
        if (secreto) {
            tiempoSecreto = Math.max(0, tiempoSecreto - delta);
            if (!esSecreto()) { lluvia.limpiar(); avisoNivel = 2; actualizarMovimientoTerreno(); }
            return;
        }
        if (personaje.getPuntos() >= TOTAL_NIVELES * PUNTOS_POR_NIVEL) {
            numeroNivel = TOTAL_NIVELES; maximoNivel = TOTAL_NIVELES;
            fase = Fase.ASCENSO;
            patrulla = new SubordinadoOvni(ancho, true, TipoObjeto.ACIDO, 4);
            zonaPatrulla = Zona.ESPACIO;
            chorros.clear();
            lluvia.limpiar();
            java.util.Random suerte = new java.util.Random(semilla ^ 0xA5CE50L);
            if (suerte.nextFloat() < 0.65f) {
                float x = ancho * (0.25f + suerte.nextFloat() * 0.5f);
                lluvia.lanzar(new ObjetoEspecial(x, 495, 175, Zona.ESPACIO.arma()));
            }
            actualizarMovimientoTerreno();
            return;
        }
        int siguiente = Math.min(TOTAL_NIVELES, 1 + personaje.getPuntos() / PUNTOS_POR_NIVEL);
        if (siguiente != numeroNivel) {
            numeroNivel = siguiente; maximoNivel = Math.max(maximoNivel, siguiente);
            nivel = dificultad.crearNivel(numeroNivel); lluvia.limpiar(); avisoNivel = 2;
            int hito = numeroNivel / 5;
            if (hito > ultimoSecreto) { ultimoSecreto = hito; tiempoSecreto = DURACION_SECRETO; }
            if (esSecreto()) {
                patrulla = null;
                chorros.clear();
            }
            actualizarMovimientoTerreno();
        }
    }
    private void actualizarMovimientoTerreno() {
        MovimientoTerreno terreno = MovimientoTerreno.NORMAL;
        if (fase == Fase.JEFE) terreno = MovimientoTerreno.ESPACIO;
        else if (fase == Fase.CAMPANA && !esSecreto()) {
            if (getZona() == Zona.HIELO) terreno = MovimientoTerreno.HIELO;
            if (getZona() == Zona.DESIERTO) terreno = MovimientoTerreno.ATACAMA;
        }
        personaje.setMovimientoTerreno(terreno);
    }

    private void actualizarPatrulla(float delta, Zona zona) {
        if (zonaPatrulla != zona) {
            zonaPatrulla = zona;
            patrulla = null;
            chorros.clear();
            esperaPatrulla = 6;
        }
        if (patrulla == null) {
            esperaPatrulla -= delta;
            if (esperaPatrulla <= 0) {
                TipoObjeto ataque = zona == Zona.HIELO ? TipoObjeto.NIEVE
                        : zona == Zona.DESIERTO ? TipoObjeto.FUEGO : TipoObjeto.ACIDO;
                patrulla = new SubordinadoOvni(ancho, numeroNivel % 2 == 1, ataque, 4);
                esperaPatrulla = 10;
            }
        }
        if (patrulla != null) patrulla.actualizar(delta, ancho, lluvia);
        for (int i = chorros.size() - 1; i >= 0; i--) {
            ChorroAgua chorro = chorros.get(i);
            chorro.actualizar(delta);
            if (patrulla != null && patrulla.recibir(chorro)) {
                chorros.remove(i);
                if (patrulla.derrotado()) {
                    patrulla = null;
                    personaje.avisar("Patrulla derribada");
                }
            } else if (chorro.fuera()) chorros.remove(i);
        }
    }

    public boolean disparar() {
        if (terminada() || esSecreto()) return false;
        if (enJefe()) return jefe.disparar(personaje);
        if (patrulla == null || esperaDisparo > 0 || !personaje.gastarAgua()) return false;
        ArmaBioma arma = personaje.getArma();
        arma.disparar(chorros, personaje.getX() + Personaje.ANCHO / 2 - 5, personaje.getY() + 68,
                personaje.consumirCargaArma());
        esperaDisparo = arma.getCadencia();
        return true;
    }
    public SubordinadoOvni getPatrulla() { return patrulla; }
    public List<ChorroAgua> getChorros() {
        return enJefe() ? jefe.getChorros() : Collections.unmodifiableList(chorros);
    }
    public boolean terminada() { return fase == Fase.VICTORIA || fase == Fase.DERROTA || personaje.getVidas() == 0; }
    public boolean gano() { return fase == Fase.VICTORIA; }
    public int getPremio() { return gano() && !desafioOculto ? 250 + dificultad.ordinal() * 150 : 0; }
    public int getMonedasFinales() { return desafioOculto ? 0 : personaje.getMonedas() + getPremio(); }
    public boolean esSecreto() { return tiempoSecreto > 0 && fase == Fase.CAMPANA; }
    public boolean enJefe() { return fase == Fase.JEFE; }
    public float getTiempoSecreto() { return tiempoSecreto; }
    public float getProgreso() { return Math.max(0, Math.min(1, (personaje.getPuntos() - (numeroNivel - 1) * 100) / 100f)); }
    public int getObjetivo() { return numeroNivel * PUNTOS_POR_NIVEL; }
    public float getAvisoNivel() { return avisoNivel; }
    public float getAscenso() { return Math.min(1, tiempoAscenso / 3); }
    public int getNumeroNivel() { return numeroNivel; }
    public int getMaximoNivel() { return maximoNivel; }
    public float getAncho() { return ancho; }
    public Fase getFase() { return fase; }
    public Zona getZona() { return fase == Fase.JEFE || fase == Fase.VICTORIA ? Zona.ESPACIO : Zona.delNivel(numeroNivel); }
    public JefeOvni getJefe() { return jefe; }
    public Dificultad getDificultad() { return dificultad; }
    public Personaje getPersonaje() { return personaje; }
    public Lluvia getLluvia() { return lluvia; }
}
