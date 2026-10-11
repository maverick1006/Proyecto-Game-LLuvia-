package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlTeclado implements ControlMovimiento {
    @Override
    public void actualizar(Tarro tarro, float delta) {
        int direccion = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) direccion--;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) direccion++;
        tarro.mover(direccion * 440 * delta);
    }
}
