package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.utils.ResourceManager;

import static hu.wolfman.deimos.Constants.*;

/**
 * A mobilos felületen megjelenő kontrollert
 * definiáló osztály.
 * @author Farkas Péter
 */
public class OnScreenController implements Disposable {
    private Viewport viewport;
    private Stage stage;
    private boolean isLeftPressed, isRightPressed, isJumpPressed, isShootPressed;

    public OnScreenController(Game game) {
        viewport = new FitViewport(WIDTH, HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(createController());
    }

    /**
     * Gombok létrehozása, és hozzáadása a
     * kontroller felületéhez.
     * @return A kontroller struktúráját leíró táblázat
     */
    private Table createController() {
        Table table = new Table();

        Image jumpButton = createButton("button_jump");
        jumpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isJumpPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isJumpPressed = false;
            }
        });

        Image shootButton = createButton("button_shoot");
        shootButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isShootPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isShootPressed = false;
            }
        });

        Image leftButton = createButton("button_left");
        leftButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isLeftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isLeftPressed = false;
            }
        });

        Image rightButton = createButton("button_right");
        rightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isRightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isRightPressed = false;
            }
        });

        table.bottom().left();
        table.add(leftButton).size(jumpButton.getWidth(), jumpButton.getHeight()).pad(5);
        table.add(rightButton).size(leftButton.getWidth(), leftButton.getHeight()).pad(5);
        table.add(jumpButton).size(rightButton.getWidth(), rightButton.getHeight()).pad(5, WIDTH - 235, 5, 5);
        table.add(shootButton).size(shootButton.getWidth(), shootButton.getHeight()).pad(5);

        return table;
    }

    /**
     * Gomb létrehozása.
     * @param textureRegion A megfelelő textúrarégió neve.
     * @return A kontrolleren elhelyezendő kép.
     */
    private Image createButton(String textureRegion) {
        Image image = new Image(ResourceManager.get().textureRegion("controller", textureRegion));
        image.setSize(BUTTON_SIZE, BUTTON_SIZE);
        return image;
    }

    public void draw() {
        stage.draw();
    }

    public boolean isLeftPressed() {
        return isLeftPressed;
    }

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public boolean isJumpPressed() {
        return isJumpPressed;
    }

    public boolean isShootPressed() {
        return isShootPressed;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
