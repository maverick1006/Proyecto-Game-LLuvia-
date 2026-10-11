package puppy.code.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import puppy.code.GameLluvia;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GameLluvia | Guardian de la lluvia");
        config.setWindowedMode(960, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setPreferencesConfig("../guardado", com.badlogic.gdx.Files.FileType.Local);
        new Lwjgl3Application(new GameLluvia(), config);
    }
}
