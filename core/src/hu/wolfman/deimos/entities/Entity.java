package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
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

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width / PPM, height / PPM);
    }

    protected Animation createAnimation(TextureRegion region, int numOfFrames, int size) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < numOfFrames; i++) {
            frames.add(new TextureRegion(region, i*size, 0, size, size));
        }
        return new Animation(0.1f, frames);
    }
    
    public abstract void update(float delta);
}
