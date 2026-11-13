package puppy.code;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.EnumMap;

public class Personaje {
    public static final float ANCHO = 48;
    private final Rectangle area = new Rectangle(456, 42, ANCHO, 68);
    private ControlMovimiento control;
    private MovimientoTerreno terreno = MovimientoTerreno.NORMAL;
    private float movimientoPedido, velocidadTerreno;
    private boolean leyendoControl;
    private final EnumMap<TipoObjeto, Float> efectos = new EnumMap<>(TipoObjeto.class);
    private final int vidasMaximas;
    private int vidas;
    private ArmaBioma arma = ArmaBioma.AGUA;
    private int puntos;
    private int monedas;
    private float invulnerabilidad;
    private float desplazamiento;
    private String aviso = "";
    private float tiempoAviso;
    private float sinAgua;
    private float proximoDanioPorSed = 12;
    private Hidratacion hidratacion = Hidratacion.NORMAL;
    public static final int CARGA_MAXIMA = 6;
    private int cargaArma;
    private int agua;
    private final float anchoMundo;

    public Personaje(ControlMovimiento control) { this(control, 960); }
    public Personaje(ControlMovimiento control, float anchoMundo) { this(control, anchoMundo, 3); }
    public Personaje(ControlMovimiento control, float anchoMundo, int vidas) {
        this.vidas = vidas;
        vidasMaximas = vidas;
        setControlMovimiento(control);
        this.anchoMundo = anchoMundo;
        area.x = (anchoMundo - ANCHO) / 2;
    }

    public void setControlMovimiento(ControlMovimiento control) {
        this.control = java.util.Objects.requireNonNull(control, "control");
    }

    public void setMovimientoTerreno(MovimientoTerreno terreno) {
        if (this.terreno == terreno) return;
        this.terreno = terreno;
        velocidadTerreno = 0;
    }

    public MovimientoTerreno getMovimientoTerreno() { return terreno; }

    public void actualizar(float delta) {
        actualizar(delta, true);
    }
    public void actualizar(float delta, boolean necesitaAgua) {
        if (necesitaAgua) sinAgua += delta;
        else hidratar();
        invulnerabilidad = Math.max(0, invulnerabilidad - delta);
        tiempoAviso = Math.max(0, tiempoAviso - delta);
        efectos.replaceAll((tipo, tiempo) -> Math.max(0, tiempo - delta));
        hidratacion = sinAgua > 4 ? Hidratacion.FATIGADA : Hidratacion.NORMAL;
        while (vidas > 0 && sinAgua >= proximoDanioPorSed) {
            if (recibirDaño()) avisar("Deshidratación: recoge agua");
            proximoDanioPorSed += 4;
        }
        float anterior = area.x;
        if (vidas > 0 && delta > 0) {
            movimientoPedido = 0;
            leyendoControl = true;
            try {
                control.actualizar(this, delta);
            } finally {
                leyendoControl = false;
            }
            float distancia = terreno.calcular(movimientoPedido, velocidadTerreno * delta, delta);
            mover(distancia);
            velocidadTerreno = (area.x - anterior) / delta;
        }
        desplazamiento = area.x - anterior;
    }

    public float getVelocidad() {
        if (activo(TipoObjeto.PAJARO) || activo(TipoObjeto.HIELO)) return 0;
        float sed = hidratacion.factor(sinAgua);
        return 680 * sed * (activo(TipoObjeto.CAFE) || activo(TipoObjeto.FRUTA) ? 1.5f : 1)
                * (activo(TipoObjeto.VENENO) || activo(TipoObjeto.NIEVE) ? 0.5f : 1);
    }
    public void mover(float distancia) {
        if (activo(TipoObjeto.PAJARO) || activo(TipoObjeto.HIELO)) return;
        if (leyendoControl) movimientoPedido += distancia;
        else colocar(area.x + distancia);
    }
    public void moverHacia(float destino, float delta) {
        mover(MathUtils.clamp(destino - area.x, -getVelocidad() * delta, getVelocidad() * delta));
    }
    public void colocar(float x) { area.x = MathUtils.clamp(x, 0, anchoMundo - ANCHO); }
    public void recogerAgua(int cantidad) {
        if (vidas == 0) return;
        sumarPuntos(cantidad);
        agua += cantidad / 10;
        hidratar();
        if (arma != ArmaBioma.AGUA) cargaArma = Math.min(CARGA_MAXIMA, cargaArma + cantidad / 10);
    }
    private void hidratar() {
        sinAgua = 0;
        proximoDanioPorSed = 12;
        hidratacion = Hidratacion.NORMAL;
    }
    public Hidratacion getHidratacion() { return hidratacion; }
    public int getCargaArma() { return cargaArma; }
    boolean consumirCargaArma() {
        if (cargaArma < CARGA_MAXIMA) return false;
        cargaArma = 0;
        avisar("Disparo especial");
        return true;
    }
    void avisar(String texto) { aviso = texto; tiempoAviso = 2.5f; }
    public void quitarPuntos(int cantidad) { puntos = Math.max(0, puntos - Math.max(0, cantidad)); }
    public void cargarAgua(int cantidad) { agua += Math.max(0, cantidad); }
    public boolean gastarAgua() { if (agua == 0 || vidas == 0) return false; agua--; return true; }
    public int getAgua() { return agua; }
    public float getSinAgua() { return sinAgua; }
    public boolean recibirDaño() {
        if (vidas == 0 || invulnerabilidad > 0) return false;
        if (activo(TipoObjeto.ESCUDO)) {
            efectos.remove(TipoObjeto.ESCUDO);
            aviso = "El escudo absorbio el golpe";
            tiempoAviso = 2.5f;
            return false;
        }
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
            case ARMA_LLANURA: equiparArma(ArmaBioma.PULSO); break;
            case ARMA_HIELO: equiparArma(ArmaBioma.LANZA_HIELO); break;
            case ARMA_SOLAR: equiparArma(ArmaBioma.RAYO_SOLAR); break;
            case ARMA_PLASMA: equiparArma(ArmaBioma.HIDRO_PLASMA); break;
            case BOTIQUIN: vidas = Math.min(vidasMaximas, vidas + 1); break;
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
                if (recibirDaño()) quitarPuntos(tipo == TipoObjeto.FUEGO ? 40 : 25);
                break;
            case VENENO:
                quitarPuntos(10); efectos.put(tipo, tipo.getDuracion()); break;
            case MONEDA:
                if (!activo(TipoObjeto.BOLSA_ROTA)) monedas += activo(TipoObjeto.MONEDAS_DOBLES) ? 2 : 1;
                break;
            case ACIDO:
                if (recibirDaño()) puntos = Math.max(0, puntos - 30);
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
        if (tipo != TipoObjeto.MONEDA) avisar(tipo.getDescripcion());
    }
    public void equiparArma(ArmaBioma arma) {
        java.util.Objects.requireNonNull(arma);
        if (this.arma != arma) cargaArma = 0;
        this.arma = arma;
    }
    public ArmaBioma getArma() { return arma; }
    void colocarAltura(float y) { area.y = y; }
    public boolean activo(TipoObjeto tipo) { return efectos.getOrDefault(tipo, 0f) > 0; }
    public float tiempoRestante(TipoObjeto tipo) { return efectos.getOrDefault(tipo, 0f); }
    public String getAviso() { return tiempoAviso > 0 ? aviso : ""; }
    public boolean choca(float x, float y, float ancho, float alto) {
        return area.x < x + ancho && area.x + area.width > x
                && area.y < y + alto && area.y + area.height > y;
    }

    public boolean choca(Rectangle otra) { return area.overlaps(otra); }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
    public int getVidas() { return vidas; }
    public int getPuntos() { return puntos; }
    public int getMonedas() { return monedas; }
    public boolean estaHerido() { return invulnerabilidad > 0; }
    public boolean estaMoviendo() { return Math.abs(desplazamiento) > 0.1f; }
}
