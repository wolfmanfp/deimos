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

    @Override
    public void create() {
        batch = new SpriteBatch();
        loadAssets();
        setScreen(new PlayScreen(this));
        Logger.get().log("A játék sikeresen elindult.");
    }
    
    private void loadAssets() {
        Resources.get().setGame(MAINGAME);
        Resources.get().loadMusic("GameMusic", "Germ Factory.mp3");
        Resources.get().loadBitmapFont("hudFont", "PressStart2P.fnt");
        Resources.get().loadTextureAtlas("player", "player.pack");
        Resources.get().finishLoading();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        Resources.get().update();
    }

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
