package hu.wolfman.deimos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import hu.wolfman.deimos.Game;
import hu.wolfman.deimos.Resources;
import hu.wolfman.deimos.entities.Player;
import static hu.wolfman.deimos.Constants.*;

/**
 *
 * @author Farkas Péter
 */
public class HeadsUpDisplay implements Disposable {
    public Stage stage;
    private boolean debugMode;
    private Viewport viewport;
    private Player player;
    private Label scoreLabel, healthLabel, fpsLabel;

    public HeadsUpDisplay(Game game, Player player) {
        this.player = player;
        this.debugMode = game.debugMode;

        viewport = new FitViewport(WIDTH, HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        
        createHud();
    }
    
    private void createHud() {
        LabelStyle style = new LabelStyle(
                Resources.get().bitmapFont("hudFont"),
                Color.WHITE
        );
        
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        
        scoreLabel = new Label("", style);
        healthLabel = new Label("", style);
        fpsLabel = new Label("", style);
        
        table.add(scoreLabel).expandX().padTop(10).align(Align.left);
        table.add(healthLabel).expandX().padTop(10).align(Align.right);
        if (debugMode) table.add(fpsLabel).expandX().padBottom(10).align(Align.left);

        stage.addActor(table);
    }
    
    public void update() {
        scoreLabel.setText(
                String.format("%06d", player.getPoints())
        );
        healthLabel.setText(
                String.format("%d", player.getHealth())
        );
        if (debugMode)
            fpsLabel.setText(
                    String.format("%d fps", Gdx.graphics.getFramesPerSecond())
            );
    }
    
    public Camera getCamera() {
        return stage.getCamera();
    }
    
    public void draw() {
        stage.draw();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }

}
