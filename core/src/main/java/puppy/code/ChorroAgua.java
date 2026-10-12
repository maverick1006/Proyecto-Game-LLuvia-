package puppy.code;

import com.badlogic.gdx.math.Rectangle;

public class ChorroAgua {
    private final Rectangle area;
    public ChorroAgua(float x, float y) { area = new Rectangle(x, y, 10, 24); }
    public void actualizar(float delta) { area.y += 700 * delta; }
    public boolean choca(Rectangle otra) { return area.overlaps(otra); }
    public boolean fuera() { return area.y > 600; }
    public float getX() { return area.x; }
    public float getY() { return area.y; }
}
