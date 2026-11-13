package puppy.code;

import java.util.List;

public enum ArmaBioma {
    AGUA("Chorro de agua", "82EBFF", 0.20f),
    PULSO("Pulso de llanura", "7CE5A6", 0.18f) {
        @Override int danio() { return 2; }
    },
    LANZA_HIELO("Lanza de hielo", "B9DAFF", 0.24f) {
        @Override int danio() { return 2; }
        @Override float frio() { return 1.4f; }
    },
    RAYO_SOLAR("Rayo solar", "FFD36A", 0.23f) {
        @Override int danio() { return 3; }
        @Override int quemadura() { return 2; }
    },
    HIDRO_PLASMA("Canon hidro-plasma", "EF9FF2", 0.14f) {
        @Override void disparar(List<ChorroAgua> tiros, float x, float y) {
            tiros.add(new ChorroAgua(x, y, 0, this));
            tiros.add(new ChorroAgua(x, y, -120, this));
            tiros.add(new ChorroAgua(x, y, 120, this));
        }
    };

    private final String nombre, color;
    private final float cadencia;

    ArmaBioma(String nombre, String color, float cadencia) {
        this.nombre = nombre;
        this.color = color;
        this.cadencia = cadencia;
    }

    public String getNombre() { return nombre; }
    public String getColor() { return color; }
    float getCadencia() { return cadencia; }
    int danio() { return 1; }
    float frio() { return 0; }
    int quemadura() { return 0; }

    void disparar(List<ChorroAgua> tiros, float x, float y) {
        tiros.add(new ChorroAgua(x, y, 0, this));
    }
}
