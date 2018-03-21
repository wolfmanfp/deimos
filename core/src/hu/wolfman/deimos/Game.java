package hu.wolfman.deimos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hu.wolfman.deimos.screens.PlayScreen;
import hu.wolfman.deimos.tools.Logger;
import static hu.wolfman.deimos.Constants.*;

/**
 * A játék fő osztálya, itt fut a game loop.
 * @author Farkas Péter
 */
public class Game extends com.badlogic.gdx.Game {
    public SpriteBatch batch;
    
    public boolean debugMode = false;

    /**
     * A játék betöltése.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        loadAssets();
        setScreen(new PlayScreen(this));
        Logger.log("A játék sikeresen elindult.");
    }

    /**
     * A játék erőforrásainak (képek, hangok, betűtípus)
     * betöltése.
     */
    private void loadAssets() {
        Resources.get().setGame(MAIN_GAME);
        Resources.get().loadSound("jump", "phaserUp1.mp3");
        Resources.get().loadMusic("GameMusic", "Low Level Action A.mp3");
        Resources.get().loadBitmapFont("hudFont", "PressStart2P.fnt");
        Resources.get().loadTextureAtlas("player", "player.pack");
        Resources.get().loadTextureAtlas("controller", "controller.pack");
        Resources.get().loadTexture("enemy", "enemy.png");
        Resources.get().finishLoading();
    }

    /**
     * Game loop.
     * Frissíti a játék állapotát, és a DELTA konstans
     * értékének megfelelően rajzolja ki a képernyőre.
     */
    @Override
    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        screen.render(DELTA);
        Resources.get().update();
    }

    /**
     * A metódus a memóriát üríti bezáráskor.
     */
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        Resources.get().dispose();
    }
    
    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
