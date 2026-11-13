package puppy.code;

import com.badlogic.gdx.math.Rectangle;

public class ChorroAgua {
    private final Rectangle area;
    private final float velocidadX;
    private final ArmaBioma arma;
    public ChorroAgua(float x, float y) { this(x, y, 0, ArmaBioma.AGUA); }
    public ChorroAgua(float x, float y, float velocidadX, ArmaBioma arma) {
        area = new Rectangle(x, y, 10, 24);
        this.velocidadX = velocidadX;
        this.arma = arma;
    }
    public void actualizar(float delta) {
        area.x += velocidadX * delta;
        area.y += 700 * delta;
    }
    public ArmaBioma getArma() { return arma; }
    public boolean choca(Rectangle otra) { return area.overlaps(otra); }
    public boolean fuera() { return area.y > 600; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
}
