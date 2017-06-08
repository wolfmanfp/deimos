package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import hu.wolfman.deimos.Resources;

/**
 *
 * @author Farkas Péter
 */
public class Player extends Entity {
    public enum State {STANDING, DEAD, RUNNING, JUMPING, SHOOTING, FALLING}
    public State previousState;
    public State currentState;
    
    private TextureRegion playerStanding;
    private TextureRegion playerJumping;
    private Animation playerRunning;
    
    private boolean isDead = false;
    private boolean facingRight = true;
    private int points = 0;
    private int health = 100;
    private float stateTimer = 0;
    
    public Player(Body body) {
        super(body);
        
        currentState = previousState = State.STANDING;
        
        playerStanding = Resources.get().textureRegion("player", "player_standing");
        playerJumping = Resources.get().textureRegion("player", "player_jumping");
        playerRunning = createAnimation(
                Resources.get().textureRegion("player", "player_running"), 8, 50
        );
        
        setBounds(0, 0, 50, 50);
        setRegion(playerStanding);
    }   
    
    @Override
    public void update(float delta) {
        setRegion(getFrame(delta));
        setPosition(getPosX() - getWidth()/ 2, getPosY() - getWidth() / 2);
    }
    
    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                region = playerJumping;
                break;
            case RUNNING:
                region = (TextureRegion) playerRunning.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStanding;
                break;
        }
        
        if ((getVelocityX() < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }
        else if ((getVelocityX() > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }
        
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        
        return region;
    }
    
    public State getState() {
        if (isDead) {
            return State.DEAD;
        }
        else if (getVelocityY() > 0) {
            return State.JUMPING;
        }
        else if (getVelocityY() < 0) {
            return State.FALLING;
        }
        else if (getVelocityX() != 0) {
            return State.RUNNING;
        }
        else return State.STANDING;
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
        
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int healthPoints) {
        this.health += health;
    }
    
    public void damage(int healthPoints) {
        this.health -= health;
    }
    
}
