package hu.wolfman.deimos.screens;

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
import hu.wolfman.deimos.Resources;
import hu.wolfman.deimos.entities.Player;
import static hu.wolfman.deimos.Constants.*;

/**
 *
 * @author Farkas Péter
 */
public class HeadsUpDisplay implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Player player;
    private Label scoreLabel, healthLabel;

    public HeadsUpDisplay(SpriteBatch batch, Player player) {
        this.player = player;
        
        viewport = new FitViewport(WIDTH, HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        
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
        
        scoreLabel = new Label(
                String.format("%06d", player.getPoints()), 
                style
        );
        healthLabel = new Label(
                String.format("%d", player.getHealth()), 
                style
        );
        
        table.add(scoreLabel).expandX().padTop(10).align(Align.left);
        table.add(healthLabel).expandX().padTop(10).align(Align.right);
        
        stage.addActor(table);
    }
    
    public void update() {
        scoreLabel.setText(String.format("%06d", player.getPoints()));
        healthLabel.setText(String.format("%d", player.getHealth()));
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
