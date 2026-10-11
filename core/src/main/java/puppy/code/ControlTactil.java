package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ControlTactil implements ControlMovimiento {
    private final Viewport viewport;
    private final Vector2 toque = new Vector2();

    public ControlTactil(Viewport viewport) { this.viewport = viewport; }

    @Override
    public void actualizar(Tarro tarro, float delta) {
        if (!Gdx.input.isTouched()) return;
        viewport.unproject(toque.set(Gdx.input.getX(), Gdx.input.getY()));
        // La franja superior queda reservada para los botones.
        if (toque.y >= 0 && toque.y < 480 && toque.x >= 0 && toque.x <= 960) {
            tarro.colocar(toque.x - Tarro.ANCHO / 2);
        }
    }
}
