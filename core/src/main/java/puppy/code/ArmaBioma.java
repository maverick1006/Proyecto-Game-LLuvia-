package puppy.code;

import java.util.List;

public enum ArmaBioma {
    AGUA("Chorro de agua", "82EBFF", 0.20f, null),
    PULSO("Pulso de llanura", "7CE5A6", 0.18f, TipoObjeto.ARMA_LLANURA) {
        @Override int daño() { return 2; }
    },
    LANZA_HIELO("Espada de hielo", "B9DAFF", 0.24f, TipoObjeto.ARMA_HIELO) {
        @Override int daño() { return 2; }
        @Override float frio() { return 1.4f; }
    },
    RAYO_SOLAR("Pistola láser solar", "FFD36A", 0.23f, TipoObjeto.ARMA_SOLAR) {
        @Override int daño() { return 3; }
        @Override int quemadura() { return 2; }
    },
    HIDRO_PLASMA("Cañón hidro-plasma", "EF9FF2", 0.14f, TipoObjeto.ARMA_PLASMA) {
        @Override void disparar(List<ChorroAgua> tiros, float x, float y, boolean cargado) {
            tiros.add(new ChorroAgua(x, y, 0, this, cargado));
            if (cargado) {
                tiros.add(new ChorroAgua(x, y, -120, this, true));
                tiros.add(new ChorroAgua(x, y, 120, this, true));
            }
        }
    };

    private final String nombre, color;
    private final float cadencia;
    private final TipoObjeto icono;

    ArmaBioma(String nombre, String color, float cadencia, TipoObjeto icono) {
        this.nombre = nombre;
        this.color = color;
        this.cadencia = cadencia;
        this.icono = icono;
    }

    public String getNombre() { return nombre; }
    public TipoObjeto getIcono() { return icono; }
    public String getColor() { return color; }
    float getCadencia() { return cadencia; }
    int dañoCargado() { return daño() + 2; }
    int daño() { return 1; }
    float frio() { return 0; }
    int quemadura() { return 0; }

    void disparar(List<ChorroAgua> tiros, float x, float y, boolean cargado) {
        tiros.add(new ChorroAgua(x, y, 0, this, cargado));
    }
}
