package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 *
 * @author Farkas Péter
 */
public class Player extends Entity {
    public enum State {STANDING, DEAD, WALKING, JUMPING, SHOOTING, FALLING}
    public State currentState;
    
    public Player(Body body) {
        super(body);
    }
    
    @Override
    public void draw(Batch batch, float alphaModulation) {
        super.draw(batch, alphaModulation);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    
    @Override
    public void update(float delta) {
        
    }

    public void moveLeft() {
        body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }
    
    public void moveRight() {
        body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
    }
    
    public void jump() {
        if (currentState != State.JUMPING) {
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void fire() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
