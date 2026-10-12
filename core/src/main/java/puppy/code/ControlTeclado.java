package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ControlTeclado implements ControlMovimiento {
    private final int izquierda, derecha;
    public ControlTeclado() { izquierda = -1; derecha = -1; }
    public ControlTeclado(int izquierda, int derecha) { this.izquierda = izquierda; this.derecha = derecha; }
    @Override
    public void actualizar(Personaje personaje, float delta) {
        int direccion = 0;
        if (izquierda == -1) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) direccion--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) direccion++;
        } else {
            if (Gdx.input.isKeyPressed(izquierda)) direccion--;
            if (Gdx.input.isKeyPressed(derecha)) direccion++;
        }
        personaje.mover(direccion * personaje.getVelocidad() * delta);
    }
}
