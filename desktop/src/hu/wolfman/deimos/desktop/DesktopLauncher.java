package hu.wolfman.deimos.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import static hu.wolfman.deimos.Constants.*;
import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.utils.Logger;

public class DesktopLauncher {
    public static void main (String[] args) {
        Lwjgl3ApplicationConfiguration config = 
                new Lwjgl3ApplicationConfiguration();
        config.setTitle(TITLE);
        config.setWindowedMode(WIDTH * 2, HEIGHT * 2);
        config.setWindowIcon("icon.png");
        
        Game game = new Game();
        if (args.length != 0 && args[0].equals("-debug")) {
            game.debugMode = true;
            config.setTitle(TITLE + " [debug]");
            Logger.log("Debug módban indítva.");
        }

        new Lwjgl3Application(game, config);
    }
}
