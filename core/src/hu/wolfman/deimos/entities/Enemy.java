package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import hu.wolfman.deimos.Resources;

/**
 *
 * @author Farkas Péter
 */
public class Enemy extends Entity {
    private final TextureRegion enemyIdle;

    public Enemy(Body body) {
        super(body);
        enemyIdle = new TextureRegion(Resources.get().texture("enemy"));
        
        setBounds(0, 0, 50, 50);
        setRegion(enemyIdle);
    }

    @Override
    public void update(float delta) {
        setPosition(getPosX() - getWidth()/ 2, getPosY() - getWidth() / 2);
    }
    
}
