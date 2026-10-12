package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.EnumMap;

public class Personaje {
    public static final float ANCHO = 48;
    private final Rectangle area = new Rectangle(456, 42, ANCHO, 68);
    private final ControlMovimiento control;
    private final EnumMap<TipoObjeto, Float> efectos = new EnumMap<>(TipoObjeto.class);
    private int vidas = 3;
    private int puntos;
    private int monedas;
    private float invulnerabilidad;
    private float desplazamiento;
    private String aviso = "";
    private float tiempoAviso;
    private float sinAgua;
    private int agua;
    private final float anchoMundo;

    public Personaje(ControlMovimiento control) { this(control, 960); }
    public Personaje(ControlMovimiento control, float anchoMundo) {
        this.control = control;
        this.anchoMundo = anchoMundo;
        area.x = (anchoMundo - ANCHO) / 2;
    }

    public void actualizar(float delta) {
        actualizar(delta, true);
    }
    public void actualizar(float delta, boolean necesitaAgua) {
        sinAgua = necesitaAgua ? sinAgua + delta : 0;
        invulnerabilidad = Math.max(0, invulnerabilidad - delta);
        tiempoAviso = Math.max(0, tiempoAviso - delta);
        efectos.replaceAll((tipo, tiempo) -> Math.max(0, tiempo - delta));
        float anterior = area.x;
        if (vidas > 0) control.actualizar(this, delta);
        desplazamiento = area.x - anterior;
    }

    public float getVelocidad() {
        if (activo(TipoObjeto.PAJARO) || activo(TipoObjeto.HIELO)) return 0;
        float sed = 1 - Math.min(0.6f, Math.max(0, sinAgua - 4) * 0.15f);
        return 680 * sed * (activo(TipoObjeto.CAFE) || activo(TipoObjeto.FRUTA) ? 1.5f : 1)
                * (activo(TipoObjeto.VENENO) || activo(TipoObjeto.NIEVE) ? 0.5f : 1);
    }
    public void mover(float distancia) {
        if (!activo(TipoObjeto.PAJARO) && !activo(TipoObjeto.HIELO)) colocar(area.x + distancia);
    }
    public void moverHacia(float destino, float delta) {
        mover(MathUtils.clamp(destino - area.x, -getVelocidad() * delta, getVelocidad() * delta));
    }
    public void colocar(float x) { area.x = MathUtils.clamp(x, 0, anchoMundo - ANCHO); }
    public void recogerAgua(int cantidad) {
        if (vidas == 0) return;
        sumarPuntos(cantidad);
        agua += cantidad / 10;
        sinAgua = 0;
    }
    public void quitarPuntos(int cantidad) { puntos = Math.max(0, puntos - Math.max(0, cantidad)); }
    public boolean gastarAgua() { if (agua == 0 || vidas == 0) return false; agua--; return true; }
    public int getAgua() { return agua; }
    public float getSinAgua() { return sinAgua; }
    public boolean recibirDanio() {
        if (vidas == 0 || invulnerabilidad > 0) return false;
        vidas--;
        invulnerabilidad = 1.2f;
        return true;
    }
    public void sumarPuntos(int cantidad) {
        if (vidas > 0 && cantidad > 0) puntos += cantidad * (activo(TipoObjeto.PUNTOS_DOBLES) ? 2 : 1);
    }
    public void atrapar(TipoObjeto tipo) {
        if (vidas == 0) return;
        switch (tipo) {
            case AGUA_DOBLE: recogerAgua(20); break;
            case OASIS: recogerAgua(30); break;
            case SANGRE: quitarPuntos(20); break;
            case NIEVE:
            case HIELO:
                if (!activo(TipoObjeto.TERMO)) {
                    quitarPuntos(tipo == TipoObjeto.NIEVE ? 20 : 30);
                    efectos.put(tipo, tipo.getDuracion());
                }
                break;
            case TERMO:
                efectos.remove(TipoObjeto.NIEVE); efectos.remove(TipoObjeto.HIELO);
                efectos.put(tipo, tipo.getDuracion());
                break;
            case FUEGO:
            case CACTUS:
                if (recibirDanio()) quitarPuntos(tipo == TipoObjeto.FUEGO ? 40 : 25);
                break;
            case VENENO:
                quitarPuntos(10); efectos.put(tipo, tipo.getDuracion()); break;
            case MONEDA:
                if (!activo(TipoObjeto.BOLSA_ROTA)) monedas += activo(TipoObjeto.MONEDAS_DOBLES) ? 2 : 1;
                break;
            case ACIDO:
                if (recibirDanio()) puntos = Math.max(0, puntos - 30);
                break;
            case TINTA:
                puntos = Math.max(0, puntos - 50);
                break;
            case BOLSA_ROTA:
                monedas = Math.max(0, monedas - 5);
                efectos.put(tipo, tipo.getDuracion());
                break;
            default:
                efectos.put(tipo, tipo.getDuracion());
        }
        if (tipo != TipoObjeto.MONEDA) { aviso = tipo.getDescripcion(); tiempoAviso = 2.5f; }
    }
    public boolean activo(TipoObjeto tipo) { return efectos.getOrDefault(tipo, 0f) > 0; }
    public float tiempoRestante(TipoObjeto tipo) { return efectos.getOrDefault(tipo, 0f); }
    public String getAviso() { return tiempoAviso > 0 ? aviso : ""; }
    public boolean choca(Rectangle otra) { return area.overlaps(otra); }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
    public int getVidas() { return vidas; }
    public int getPuntos() { return puntos; }
    public int getMonedas() { return monedas; }
    public boolean estaHerido() { return invulnerabilidad > 0; }
    public boolean estaMoviendo() { return Math.abs(desplazamiento) > 0.1f; }
}
