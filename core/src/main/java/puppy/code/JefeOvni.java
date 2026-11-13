package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JefeOvni {
    public enum Fase {
        NORMAL(1, 1),
        FURIOSA(1.65f, 1.5f) {
            @Override float trayectoria(float reloj) {
                return 0.8f * super.trayectoria(reloj) + 0.2f * MathUtils.sin(reloj * 4);
            }
        },
        SUBORDINADOS(0.65f, 0) {
            @Override void actuar(JefeOvni jefe, float delta, Personaje jugador, Lluvia lluvia) {
                if (jefe.subordinados.isEmpty()) {
                    jefe.atacar(delta, jugador, lluvia, 1.5f);
                    return;
                }
                for (SubordinadoOvni dron : jefe.subordinados) dron.actualizar(delta, jefe.ancho, lluvia);
                jefe.curacion += delta;
                while (jefe.curacion >= 1) {
                    jefe.curacion--;
                    jefe.vida = Math.min(jefe.vidaMaxima / 2, jefe.vida + 1);
                }
            }
        };

        private final float velocidad, cadencia;
        Fase(float velocidad, float cadencia) { this.velocidad = velocidad; this.cadencia = cadencia; }
        float trayectoria(float reloj) { return MathUtils.sin(reloj * velocidad); }
        void actuar(JefeOvni jefe, float delta, Personaje jugador, Lluvia lluvia) {
            jefe.atacar(delta, jugador, lluvia, cadencia);
        }
    }

    private final Rectangle area = new Rectangle(0, 422, 110, 44);
    private final List<ChorroAgua> chorros = new ArrayList<>();
    private final List<SubordinadoOvni> subordinados = new ArrayList<>();
    private final Random azar;
    private final float ancho;
    private final int vidaMaxima;
    private final Dificultad dificultad;
    private Fase fase = Fase.NORMAL;
    private int vida, ataques, disparos, pulsos;
    private float reloj, ataque, agua, esperaDisparo, curacion, frio, calor;

    public JefeOvni(Dificultad dificultad, float ancho, long semilla) {
        this.dificultad = dificultad; this.ancho = ancho; azar = new Random(semilla);
        vidaMaxima = 24 + dificultad.ordinal() * 8; vida = vidaMaxima;
        area.x = (ancho - area.width) / 2;
    }

    public void actualizar(float delta, Personaje jugador, Lluvia lluvia) {
        if (derrotado()) return;
        reloj += delta * (frio > 0 ? 0.45f : 1);
        frio = Math.max(0, frio - delta);
        esperaDisparo = Math.max(0, esperaDisparo - delta);
        area.x = (ancho - area.width) / 2 + fase.trayectoria(reloj) * (ancho / 2 - 85);
        fase.actuar(this, delta, jugador, lluvia);
        agua += delta;
        if (agua >= 1.2f) {
            agua -= 1.2f;
            lluvia.lanzar(new GotaBuena(12 + azar.nextFloat() * (ancho - 52), 495, 240));
            if (azar.nextFloat() < 0.22f) {
                TipoObjeto mejora = azar.nextBoolean() ? TipoObjeto.ESCUDO : TipoObjeto.BOTIQUIN;
                lluvia.lanzar(new ObjetoEspecial(12 + azar.nextFloat() * (ancho - 52), 495, 225, mejora));
            }
        }
        if (pulsos > 0) {
            calor += delta;
            while (pulsos > 0 && calor >= 0.5f) {
                calor -= 0.5f;
                pulsos--;
                recibirDanio(1);
            }
        }
        for (int i = chorros.size() - 1; i >= 0 && !derrotado(); i--) {
            ChorroAgua chorro = chorros.get(i);
            chorro.actualizar(delta);
            if (golpeaDron(chorro)) chorros.remove(i);
            else if (chorro.choca(area)) {
                if (recibirDanio(chorro.getArma().danio()) && !tieneEscudo()) {
                    frio = Math.max(frio, chorro.getArma().frio());
                    if (chorro.getArma().quemadura() > 0) {
                        pulsos = Math.min(6, pulsos + chorro.getArma().quemadura());
                    }
                }
                chorros.remove(i);
            } else if (chorro.fuera()) chorros.remove(i);
        }
    }

    private void atacar(float delta, Personaje jugador, Lluvia lluvia, float cadencia) {
        ataque += delta;
        float intervalo = (0.85f - dificultad.ordinal() * 0.15f) / cadencia;
        if (ataque < intervalo) return;
        ataque -= intervalo;
        ataques++;
        float x = ataques % 3 == 0 ? jugador.getX() : area.x + azar.nextFloat() * area.width;
        x = MathUtils.clamp(x, 0, ancho - 28);
        float velocidad = 300 + dificultad.ordinal() * 60;
        if (fase == Fase.NORMAL) {
            lluvia.lanzar(new GotaMala(x, area.y - 38, velocidad, TipoObjeto.ACIDO));
        } else {
            TipoObjeto[] peligros = Zona.ESPACIO.peligros();
            lluvia.lanzar(new ObjetoEspecial(x, area.y - 38, velocidad + 50, peligros[azar.nextInt(peligros.length)]));
        }
    }

    private boolean golpeaDron(ChorroAgua chorro) {
        for (int i = subordinados.size() - 1; i >= 0; i--) {
            SubordinadoOvni dron = subordinados.get(i);
            if (dron.recibir(chorro)) {
                if (dron.derrotado()) subordinados.remove(i);
                return true;
            }
        }
        return false;
    }

    boolean recibirDanio(int danio) {
        if (derrotado() || tieneEscudo() || danio <= 0) return false;
        vida = Math.max(0, vida - danio);
        if (fase == Fase.NORMAL && vida <= vidaMaxima * 0.7f) {
            vida = Math.max(vida, 1);
            fase = Fase.FURIOSA;
        } else if (fase == Fase.FURIOSA && vida <= vidaMaxima * 0.3f) {
            vida = Math.max(vida, 1);
            fase = Fase.SUBORDINADOS;
            subordinados.add(new SubordinadoOvni(ancho, true));
            subordinados.add(new SubordinadoOvni(ancho, false));
            pulsos = 0;
            calor = 0;
        }
        return true;
    }

    public boolean disparar(Personaje jugador) {
        if (derrotado() || esperaDisparo > 0 || !jugador.gastarAgua()) return false;
        ArmaBioma arma = jugador.getArma();
        arma.disparar(chorros, jugador.getX() + Personaje.ANCHO / 2 - 5, jugador.getY() + 68);
        esperaDisparo = arma.getCadencia();
        disparos++;
        return true;
    }

    public boolean derrotado() { return vida == 0; }
    public boolean tieneEscudo() { return !subordinados.isEmpty(); }
    public Fase getFase() { return fase; }
    public List<SubordinadoOvni> getSubordinados() { return Collections.unmodifiableList(subordinados); }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaques() { return ataques; }
    public int getDisparos() { return disparos; }
    public List<ChorroAgua> getChorros() { return Collections.unmodifiableList(chorros); }
}
