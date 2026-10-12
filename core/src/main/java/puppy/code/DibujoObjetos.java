package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DibujoObjetos {
    private final ShapeRenderer formas;
    public DibujoObjetos(ShapeRenderer formas) { this.formas = formas; }
    private void color(String color) { formas.setColor(Color.valueOf(color)); }
    private void gota(float x, float y, String color) {
        color(color); formas.circle(x + 14, y + 12, 12);
        formas.triangle(x + 2, y + 16, x + 14, y + 37, x + 26, y + 16);
        color("FFFFFF"); formas.ellipse(x + 7, y + 14, 4, 9);
    }
    public void dibujar(float x, float y, TipoObjeto tipo) {
        if (tipo == null || tipo == TipoObjeto.AGUA_DOBLE || tipo == TipoObjeto.SANGRE || tipo == TipoObjeto.PUNTOS_DOBLES) {
            gota(x, y, tipo == TipoObjeto.SANGRE ? "B5394D" : tipo == TipoObjeto.PUNTOS_DOBLES ? "8099EF" : "218FCB"); return;
        }
        color(tipo.getColor());
        switch (tipo) {
            case FUEGO:
                formas.circle(x + 14, y + 11, 13);
                formas.triangle(x, y + 12, x + 9, y + 34, x + 17, y + 10);
                formas.triangle(x + 8, y + 9, x + 19, y + 39, x + 28, y + 10);
                color("FFE394"); formas.triangle(x + 7, y + 8, x + 14, y + 25, x + 22, y + 8); break;
            case CACTUS:
                formas.rect(x + 10, y + 2, 9, 33); formas.circle(x + 14.5f, y + 34, 4.5f);
                formas.rect(x + 1, y + 12, 10, 6); formas.rect(x + 1, y + 12, 5, 16);
                formas.rect(x + 18, y + 20, 10, 5); formas.rect(x + 23, y + 20, 5, 14);
                color("EAE5B9"); formas.rect(x + 12, y + 7, 1, 23); break;
            case NIEVE:
                for (int i = 0; i < 3; i++) {
                    float a = i * (float) Math.PI / 3, dx = (float) Math.cos(a) * 15, dy = (float) Math.sin(a) * 15;
                    formas.rectLine(x + 14 - dx, y + 18 - dy, x + 14 + dx, y + 18 + dy, 3);
                }
                color("568CAF"); formas.circle(x + 14, y + 18, 3); break;
            case HIELO:
                formas.rect(x + 1, y + 3, 27, 30);
                color("E7FAFF"); formas.triangle(x + 2, y + 32, x + 27, y + 32, x + 2, y + 4);
                color("75BAD9"); formas.rectLine(x + 4, y + 8, x + 20, y + 28, 2); break;
            case VENENO:
            case ACIDO:
                formas.circle(x + 14, y + 12, 13); formas.rect(x + 8, y + 22, 12, 12);
                color("344654"); formas.rect(x + 7, y + 31, 14, 5);
                color("ECF7DF"); formas.circle(x + 14, y + 13, 7);
                color("24362C"); formas.circle(x + 11, y + 14, 2); formas.circle(x + 17, y + 14, 2);
                formas.rect(x + 11, y + 7, 6, 2); break;
            case CAFE:
                formas.rect(x + 2, y + 3, 20, 22); formas.rect(x + 22, y + 8, 7, 13);
                color("F9EEE1"); formas.rect(x + 2, y + 24, 20, 4);
                formas.rectLine(x + 7, y + 31, x + 10, y + 38, 2); formas.rectLine(x + 16, y + 30, x + 19, y + 37, 2); break;
            case TERMO:
                formas.rect(x + 5, y + 3, 18, 29); color("DEEBED"); formas.rect(x + 7, y + 31, 14, 6);
                formas.rect(x + 10, y + 10, 8, 12); break;
            case OASIS:
                formas.rect(x + 5, y + 3, 18, 25); formas.rect(x + 9, y + 26, 10, 7);
                color("E1EDDE"); formas.rect(x + 8, y + 32, 12, 4); formas.rect(x + 6, y + 12, 16, 9); break;
            case FRUTA:
                formas.ellipse(x + 2, y + 3, 25, 29); color("5B9674"); formas.triangle(x + 8, y + 29, x + 14, y + 39, x + 20, y + 29);
                color("FFF0CE"); formas.circle(x + 11, y + 13, 2); formas.circle(x + 19, y + 21, 2); break;
            case MONEDA:
            case MONEDAS_DOBLES:
                formas.circle(x + 14, y + 18, 14); color("B78537"); formas.circle(x + 14, y + 18, 10);
                color("FFE7A0"); formas.rect(x + 12, y + 10, 4, 16); break;
            case PAJARO:
                formas.circle(x + 14, y + 10, 14); formas.circle(x + 14, y + 21, 9); formas.circle(x + 14, y + 30, 5);
                color("F8EDE0"); formas.ellipse(x + 4, y + 4, 21, 8); break;
            case BOLSA_ROTA:
                formas.ellipse(x + 1, y + 1, 27, 29); formas.triangle(x + 5, y + 35, x + 24, y + 35, x + 14, y + 24);
                color("633B49"); formas.rectLine(x + 7, y + 7, x + 20, y + 21, 4); break;
            default:
                color("35485E"); formas.circle(x + 14, y + 15, 13); formas.circle(x + 3, y + 4, 5); formas.circle(x + 25, y + 29, 5);
        }
    }
}
