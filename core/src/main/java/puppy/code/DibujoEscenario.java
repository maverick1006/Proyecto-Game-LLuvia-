package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DibujoEscenario {
    private final ShapeRenderer formas;
    public DibujoEscenario(ShapeRenderer formas) { this.formas = formas; }
    private void color(String color) { formas.setColor(Color.valueOf(color)); }
    public void fondo(Zona zona, float ancho, float reloj, boolean secreto) {
        color(zona == Zona.LLANURA ? "9ACAC6" : zona == Zona.HIELO ? "93BCCD" : zona == Zona.DESIERTO ? "E7B491" : "080E28");
        formas.rect(0, 0, ancho, 600);
        if (zona == Zona.LLANURA) {
            color("E5ECCC"); formas.circle(ancho * 0.78f, 433, 40);
            color("71AA90"); formas.ellipse(-50, 50, ancho * 0.8f, 350); formas.ellipse(ancho * 0.35f, 40, ancho * 0.8f, 330);
            for (int i = 0; i < 7; i++) {
                float x = ancho * (i + 0.5f) / 7;
                color("745544"); formas.rect(x - 7, 42, 14, 110 + i % 2 * 40);
                color(i % 2 == 0 ? "3D8263" : "4A9470");
                formas.circle(x, 165 + i % 2 * 40, 36); formas.circle(x - 22, 149 + i % 2 * 40, 28); formas.circle(x + 22, 149 + i % 2 * 40, 28);
            }
        } else if (zona == Zona.HIELO) {
            color("EEF9FF"); formas.circle(ancho * 0.78f, 444, 32);
            color("D2E7F0");
            for (int i = 0; i < 4; i++) {
                float x = i * ancho / 3;
                formas.triangle(x - 80, 65, x + 30, 330 + i % 2 * 90, x + 120, 65);
            }
            color("A8D4E4"); formas.rect(0, 42, ancho, 85);
            for (int i = 0; i < 2; i++) {
                float x = ancho * (i == 0 ? 0.2f : 0.78f);
                color("EDF8FF"); formas.circle(x, 72, 62);
                color("BEDAE8");
                for (int fila = 0; fila < 3; fila++) formas.rect(x - 44, 80 + fila * 15, 88, 2);
                formas.rect(x - 17, 75, 2, 47); formas.rect(x + 17, 75, 2, 47);
                color("2E526D"); formas.ellipse(x - 16, 56, 32, 43);
            }
        } else if (zona == Zona.DESIERTO) {
            color("F8DD90"); formas.circle(ancho * 0.8f, 430, 43);
            color("B87F6E"); formas.triangle(0, 80, ancho * 0.3f, 335, ancho * 0.65f, 80);
            color("C78E72"); formas.triangle(ancho * 0.25f, 70, ancho * 0.72f, 280, ancho, 70);
            color("DCAE82"); formas.ellipse(-80, -45, ancho + 160, 290);
            color("709075");
            for (int i = 0; i < 4; i++) {
                float x = 35 + i * (ancho - 70) / 3;
                formas.rect(x, 42, 12, 84); formas.rect(x - 18, 77, 18, 10); formas.rect(x - 18, 77, 8, 27);
                formas.rect(x + 12, 91, 18, 9); formas.rect(x + 22, 91, 8, 29);
            }
        } else {
            for (int i = 0; i < 70; i++) {
                color(i % 4 == 0 ? "D9BAFF" : "BDDDF4");
                formas.circle((i * 79 + 11) % ancho, 45 + (i * 97 + reloj * 8) % 490, i % 3 == 0 ? 1.6f : 0.8f);
            }
            color("254E7B"); formas.circle(ancho * 0.77f, 322, 67);
            color("398177"); formas.ellipse(ancho * 0.77f - 52, 321, 84, 26);
        }
        if (!secreto && zona != Zona.DESIERTO && zona != Zona.ESPACIO) {
            color(zona == Zona.HIELO ? "F2FBFF" : "5F98B1");
            for (int i = 0; i < 30; i++) {
                float x = (i * 137) % ancho, y = 42 + (i * 71 + 490 - reloj * 35 % 490) % 490;
                if (zona == Zona.HIELO) formas.circle(x, y, 1.5f);
                else formas.rectLine(x, y, x - 3, y - 10, 1);
            }
        }
        color(secreto ? "A38243" : zona == Zona.LLANURA ? "456D4A" : zona == Zona.HIELO ? "D6EBF0" : zona == Zona.DESIERTO ? "AA795D" : "405378");
        formas.rect(0, 0, ancho, 42);
        color("1A293C"); formas.rect(0, 0, ancho, 31);
    }
    public void ovni(float x, float y, float reloj) {
        color("2D657C"); formas.ellipse(x + 26, y + 16, 58, 38);
        color("99E3E8"); formas.ellipse(x + 32, y + 23, 44, 28);
        color("9DACC4"); formas.ellipse(x, y, 110, 34);
        color("556481"); formas.ellipse(x + 12, y - 2, 86, 17);
        for (int i = 0; i < 5; i++) { color((i + (int) (reloj * 5)) % 2 == 0 ? "FFE197" : "EC83AE"); formas.circle(x + 17 + i * 19, y + 10, 3); }
    }
}
