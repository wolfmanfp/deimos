package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import static hu.wolfman.deimos.physics.BoxConst.PPM;

/**
 *
 * @author Farkas Péter
 */
public abstract class Entity extends Sprite {
    public Body body;

    public Entity(Body body) {
        this.body = body;
        body.setUserData(this);
    }
    
    @Override
    public void draw(Batch batch) {
        draw(batch, 1.0f);
    }
    
    @Override
    public void draw(Batch batch, float alphaModulation) {
        super.draw(batch, alphaModulation);
    }
    
    public float getPosX() {
        return body.getPosition().x * PPM;
    }
    
    public float getPosY() {
        return body.getPosition().y * PPM;
    }
    
    public float getVelocityX() {
        return body.getLinearVelocity().x;
    }
    
    public float getVelocityY() {
        return body.getLinearVelocity().y;
    }
    
    public abstract void update(float delta);
}
