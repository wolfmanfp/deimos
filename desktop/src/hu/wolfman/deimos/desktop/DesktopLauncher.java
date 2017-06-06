package hu.wolfman.deimos.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.wolfman.deimos.Constants;
import hu.wolfman.deimos.Game;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = 
                new LwjglApplicationConfiguration();
        config.title = Constants.TITLE;
        config.width = Constants.WIDTH;
        config.height = Constants.HEIGHT;
        config.addIcon("mik_icon.png", Files.FileType.Internal);
        
        Game game = new Game();
        if (args.length != 0 && args[0].equals("-debug")) {
            game.debugMode = true;
        }
        new LwjglApplication(game, config);
    }
}
