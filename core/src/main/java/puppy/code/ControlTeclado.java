package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlTeclado implements ControlMovimiento {
    @Override
    public void actualizar(Personaje personaje, float delta) {
        int direccion = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) direccion--;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) direccion++;
        personaje.mover(direccion * personaje.getVelocidad() * delta);
    }
}
