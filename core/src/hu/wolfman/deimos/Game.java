package hu.wolfman.deimos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import hu.wolfman.deimos.screens.GameScreen;
import hu.wolfman.deimos.utils.Logger;
import hu.wolfman.deimos.utils.ResourceManager;

import static com.badlogic.gdx.Application.ApplicationType.Android;
import static com.badlogic.gdx.Application.ApplicationType.iOS;
import static hu.wolfman.deimos.Constants.DELTA;
import static hu.wolfman.deimos.Constants.MAIN_GAME;

/**
 * A játék fő osztálya, itt fut a game loop.
 *
 * @author Farkas Péter
 */
public class Game extends com.badlogic.gdx.Game {
  public SpriteBatch batch;
  private boolean debugMode = false;

  /**
   * A játék betöltése.
   */
  @Override
  public void create() {
    Logger.log(String.format("Operációs rendszer: %s", System.getProperty("os.name")));

    batch = new SpriteBatch();
    loadAssets();
    setScreen(new GameScreen(this));

    Logger.log("A játék sikeresen elindult.");
  }

  /**
   * A játék által használt képek, hangok, betűtípusok betöltése.
   */
  private void loadAssets() {
    ResourceManager.get().setGame(MAIN_GAME);
    ResourceManager.get().loadSound("jump", "phaserUp1.mp3");
    ResourceManager.get().loadSound("shoot", "FXhome.com Futuristic Gun Sound 01.mp3");
    ResourceManager.get().loadMusic("GameMusic", "Low Level Action A.mp3");
    ResourceManager.get().loadBitmapFont("hudFont", "PressStart2P.fnt");
    ResourceManager.get().loadLanguageBundle("game");
    ResourceManager.get().loadTextureAtlas("player", "player.atlas");
    ResourceManager.get().loadTextureAtlas("controller", "controller.atlas");
    ResourceManager.get().loadTextureAtlas("enemy", "enemy.atlas");
    ResourceManager.get().loadTexture("bullet", "bullet.png");
    ResourceManager.get().finishLoading();
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
    ResourceManager.get().update();
  }

  /**
   * A metódus a memóriát üríti bezáráskor.
   */
  @Override
  public void dispose() {
    super.dispose();
    batch.dispose();
    screen.dispose();
    ResourceManager.get().dispose();
  }

  /**
   * Lekérdezi, hogy mobilon fut-e a játék.
   *
   * @return True, ha mobilon fut a játék
   */
  public boolean isRunningOnPhone() {
    return Gdx.app.getType() == Android || Gdx.app.getType() == iOS;
  }

  public boolean isDebugModeOn() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
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
