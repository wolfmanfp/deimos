package hu.wolfman.deimos.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import static hu.wolfman.deimos.Constants.*;
import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.tools.Logger;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = 
                new LwjglApplicationConfiguration();
        config.title = TITLE;
        config.width = WIDTH * 2;
        config.height = HEIGHT * 2;
        config.addIcon("mik_icon.png", Files.FileType.Internal);
        
        Game game = new Game();
        if (args.length != 0 && args[0].equals("-debug")) {
            game.debugMode = true;
            Logger.get().log("Debug módban indítva.");
        }
        new LwjglApplication(game, config);
    }
}
