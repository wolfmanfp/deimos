package hu.wolfman.deimos.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.entities.Player;
import hu.wolfman.deimos.input.TouchInputListener;
import hu.wolfman.deimos.utils.ResourceManager;

import static hu.wolfman.deimos.Constants.*;

/**
 * A mobilos felületen megjelenő vezérlőgombokat definiáló osztály.
 * @author Farkas Péter
 */
public class TouchController implements Disposable {
    private Viewport viewport;
    private Stage stage;
    private Player player;

    public TouchController(Game game, Player player) {
        viewport = new FitViewport(WIDTH, HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        this.player = player;

        stage.addActor(createController());
    }

    /**
     * Gombok létrehozása, és hozzáadása a vezérlő felületéhez.
     * @return A kontroller struktúráját leíró táblázat
     */
    private Table createController() {
        Table table = new Table();

        Image jumpButton = createButton("button_jump");
        Image shootButton = createButton("button_shoot");
        Image leftButton = createButton("button_left");
        Image rightButton = createButton("button_right");

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
        image.addListener(new TouchInputListener(textureRegion, player));
        return image;
    }

    public void draw() {
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
