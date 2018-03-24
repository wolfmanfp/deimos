package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import hu.wolfman.deimos.utils.ResourceManager;
import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;

import static hu.wolfman.deimos.physics.BoxConst.*;

/**
 * A játékost (irányítható karaktert) leíró osztály.
 * @author Farkas Péter
 */
public class Player extends Entity {
    public enum State {IDLE, DEAD, RUNNING, JUMPING, SHOOTING, FALLING}
    public State previousState;
    public State currentState;
    
    private final TextureRegion playerStanding;
    private final TextureRegion playerJumping;
    private final Animation playerRunning;
    
    private boolean isDead = false;
    private boolean facingRight = true;
    private int points = 0;
    private int health = 100;
    private float stateTimer = 0;

    /**
     * A játékos osztály konstruktora.
     * Itt történik meg az állapot és a pozíció beállítása,
     * a textúrarégiók inicializálása.
     * @param world Box2D világ
     * @param rect A játékos pozícióját leíró négyszög
     */
    public Player(World world, Rectangle rect) {
        super(world, rect);
        
        currentState = previousState = State.IDLE;
        
        playerStanding = ResourceManager.get().textureRegion("player", "player_standing");
        playerJumping = ResourceManager.get().textureRegion("player", "player_jumping");
        playerRunning = createAnimation(
            ResourceManager.get().textureRegion("player", "player_running"), 8, 50
        );
        
        setBounds(0, 0, 50, 50);
        setRegion(playerStanding);
    }

    @Override
    protected Body createBody() {
        return new BodyBuilder(world).isDynamic()
                .setPosition(rect.getX(), rect.getY())
                .addFixture(
                        new FixtureBuilder()
                                .setPolygonShape(20, 23, 0, 0)
                                .setFilter(PLAYER_BIT, (GROUND_BIT|ENEMY_BIT|BULLET_BIT))
                                .build()
                )
                .build();
    }

    /**
     * A játékos állapotának frissítése.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     */
    @Override
    public void update(float delta) {
        setRegion(getFrame(delta));
        setPosition(getPosX() - getWidth()/ 2, getPosY() - getWidth() / 2);
    }

    /**
     * A játékos sprite-ját állítja be az állapotától függően.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     * @return Textúrarégió
     */
    private TextureRegion getFrame(float delta) {
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
            case IDLE:
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
        
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        
        return region;
    }

    /**
     * A játékos állapotát (alaphelyzet, mozgás, ugrás, zuhanás) kéri le.
     * @return A játékos állapota
     */
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
        else return State.IDLE;
    }

    /**
     * A játékos balra mozgásakor hívódik meg.
     */
    public void moveLeft() {
        body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }

    /**
     * A játékos jobbra mozgásakor hívódik meg.
      */
    public void moveRight() {
        body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
    }

    /**
     * A játékos ugrásakor hívódik meg.
     */
    public void jump() {
        if (currentState != State.JUMPING) {
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    /**
     * A játékos pontszámának lekérdezése.
     * @return A játékos pontszáma
     */
    public int getPoints() {
        return points;
    }

    /**
     * A paraméterben megadott pont hozzáadása
     * a játékos pontszámához
     * @param points Szerzett pontok
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * A játékos életpontját kérdezi le.
     * @return A játékos életpontja.
     */
    public int getHealth() {
        return health;
    }

    /**
     * A játékos életpontját növeli a
     * paraméterben megadott számmal (maximum 100-ig).
     * @param healthPoints
     */
    public void addHealth(int healthPoints) {
        this.health += health;
        if (health > 100) health = 100;
    }

    /**
     * A játékos életpontszámát csökkenti
     * a paraméterben megadott számmal.
     * @param healthPoints
     */
    public void damage(int healthPoints) {
        this.health -= health;
        if (health < 0) health = 0;
    }
    
}
