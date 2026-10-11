package puppy.code;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Cada traje usa la misma base de pixeles y cambia ropa y accesorios.
public class DibujoPersonaje {
    private final ShapeRenderer formas;
    public DibujoPersonaje(ShapeRenderer formas) { this.formas = formas; }
    private void bloque(float x, float y, float escala, float px, float py, float w, float h, String color) {
        formas.setColor(Color.valueOf(color));
        formas.rect(x + px * escala, y + py * escala, w * escala, h * escala);
    }
    public void dibujar(Aspecto aspecto, float x, float y, float escala, float reloj, boolean moviendo, boolean herido) {
        String ropa = herido ? "FFFFFF" : aspecto.getColor();
        String detalle = aspecto.getSecundario();
        int estilo = aspecto.getEstilo();
        float paso = moviendo ? (float) Math.sin(reloj * 20) * 1.2f : 0;
        bloque(x, y, escala, 2, 0 + paso, 4, 5, detalle);
        bloque(x, y, escala, 8, 0 - paso, 4, 5, detalle);
        bloque(x, y, escala, 1, 0 + paso, 5, 2, "152237");
        bloque(x, y, escala, 8, 0 - paso, 5, 2, "152237");
        bloque(x, y, escala, 2, 5, 10, 7, ropa);
        bloque(x, y, escala, 0, 5 - paso, 2, 6, ropa);
        bloque(x, y, escala, 12, 5 + paso, 2, 6, ropa);
        bloque(x, y, escala, 0, 4 - paso, 2, 2, "EFC19C");
        bloque(x, y, escala, 12, 4 + paso, 2, 2, "EFC19C");
        bloque(x, y, escala, 3, 12, 8, 7, "EFC19C");
        bloque(x, y, escala, 3, 18, 8, 2, "47394D");
        bloque(x, y, escala, 4, 15, 2, 2, "172338");
        bloque(x, y, escala, 8, 15, 2, 2, "172338");
        bloque(x, y, escala, 6, 12, 2, 1, "BC7B70");
        bloque(x, y, escala, 2, 5, 10, 1, detalle);
        if (estilo == 1) {
            bloque(x, y, escala, 2, 18, 10, 3, ropa);
            bloque(x, y, escala, 2, 18, 13, 1, ropa);
            bloque(x, y, escala, 4, 6, 2, 6, detalle);
            bloque(x, y, escala, 8, 6, 2, 6, detalle);
        } else if (estilo == 2 || estilo == 4) {
            for (int i = 0; i < 3; i++) bloque(x, y, escala, 2 + i * 4, 18, 3, 3 + i % 2 * 2, estilo == 2 ? ropa : "F5D879");
            if (estilo == 2) bloque(x, y, escala, 0, 13, 3, 5, ropa);
        } else if (estilo == 3) {
            bloque(x, y, escala, 2, 18, 10, 3, "755342");
            bloque(x, y, escala, 4, 12, 6, 2, "755342");
        } else if (estilo == 5 || estilo == 12) {
            bloque(x, y, escala, 2, estilo == 5 ? 17 : 15, 10, 2, detalle);
            bloque(x, y, escala, 6, 17, 3, 1, "DAE7EE");
            if (estilo == 12) bloque(x, y, escala, 2, 19, 10, 3, "D8E6EE");
            else bloque(x, y, escala, 11, 15, 4, 2, detalle);
        } else if (estilo == 6) {
            bloque(x, y, escala, 0, 18, 14, 2, "E9C274");
            bloque(x, y, escala, 3, 20, 8, 2, "E9C274");
            bloque(x, y, escala, 3, 19, 8, 1, ropa);
        } else if (estilo == 7 || estilo == 8 || estilo == 11) {
            bloque(x, y, escala, 2, 12, 10, 8, ropa);
            bloque(x, y, escala, 3, 15, 8, 2, estilo == 7 ? "B2F6BC" : "D8F5FF");
            bloque(x, y, escala, 6, 7, 3, 3, estilo == 11 ? "BEF8FA" : detalle);
            if (estilo == 8) bloque(x, y, escala, 6, 12, 2, 8, detalle);
        } else if (estilo == 9) {
            bloque(x, y, escala, -2, 5, 5, 7, detalle);
            bloque(x, y, escala, -1, 6, 3, 5, "E4E9EE");
            bloque(x, y, escala, 0, 7, 1, 3, ropa);
        } else if (estilo == 10) {
            bloque(x, y, escala, 1, 11, 2, 9, "E4C880");
            bloque(x, y, escala, 11, 11, 2, 9, "E4C880");
            bloque(x, y, escala, 15, 3, 1, 6, "8B6B5D");
            bloque(x, y, escala, 13, 8, 5, 3, "C7D9E0");
        }
    }
}
