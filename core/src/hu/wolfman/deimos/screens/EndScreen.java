package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.utils.Logger;
import hu.wolfman.deimos.utils.Resources;

import static hu.wolfman.deimos.Constants.HEIGHT;
import static hu.wolfman.deimos.Constants.WIDTH;

/**
 * A játék végén megjelenítendő képernyő.
 *
 * @author Farkas Péter
 */
public class EndScreen implements Screen {
  private final Game game;
  private String message;
  private Viewport viewport;
  private Stage stage;

  /**
   * Az osztály konstruktora.
   *
   * @param game    A játék fő osztályának objektuma.
   * @param message A megjelenítendő üzenet.
   */
  public EndScreen(Game game, String message) {
    this.game = game;
    this.message = message;

    viewport = new FitViewport(WIDTH, HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport, game.batch);
    stage.addActor(createScreen());
    stage.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        game.setScreen(new GameScreen(game));
        dispose();
        return false;
      }

      @Override
      public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Keys.SPACE) {
          game.setScreen(new GameScreen(game));
          dispose();
        }
        if (keycode == Keys.ESCAPE) {
          Logger.log("Kilépés a játékból");
          Gdx.app.exit();
        }
        return false;
      }
    });
    Gdx.input.setInputProcessor(stage);
  }

  /**
   * A képernyő létrehozása.
   *
   * @return A képernyőt leíró táblázat
   */
  private Table createScreen() {
    LabelStyle font = new LabelStyle(
        Resources.get().bitmapFont("hudFont"),
        Color.WHITE
    );

    String playAgain = game.isRunningOnPhone()
        ? Resources.get().localeString("game", "touchScreen")
        : Resources.get().localeString("game", "pressSpace");
    Label messageLabel = new Label(message, font);
    Label playAgainLabel = new Label(playAgain, font);
    Label infoLabel = new Label(Resources.get().localeString("game", "info"), font);
    messageLabel.setFontScale(0.5f);
    playAgainLabel.setFontScale(0.3f);
    infoLabel.setFontScale(0.2f);

    Table table = new Table();
    table.center();
    table.setFillParent(true);
    table.add(messageLabel).expandX();
    table.row();
    table.add(playAgainLabel).expandX().padTop(10f);
    table.row();
    table.add(infoLabel).expandX().padTop(30f);
    return table;
  }

  @Override
  public void render(float delta) {
    stage.draw();
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void resize(int width, int height) {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void show() {}

  @Override
  public void hide() {}

}
