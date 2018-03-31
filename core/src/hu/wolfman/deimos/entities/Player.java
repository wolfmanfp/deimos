package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;
import hu.wolfman.deimos.utils.ResourceManager;

import static hu.wolfman.deimos.physics.B2DConst.*;

/**
 * A játékost (irányítható karaktert) leíró osztály.
 * @author Farkas Péter
 */
public class Player extends Entity {
    public enum State {IDLE, DEAD, RUNNING, JUMPING, FALLING}
    public State previousState;
    public State currentState;
    
    private final TextureRegion playerStanding;
    private final TextureRegion playerJumping;
    private final Animation playerRunning;

    private List<Bullet> bullets;
    
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
     * @param baseTexture A játékos kezdő textúrája
     * @param rect A játékos pozícióját leíró négyszög
     */
    public Player(World world, TextureRegion baseTexture, Rectangle rect) {
        super(world, baseTexture, rect);
        bullets = new CopyOnWriteArrayList<>();

        currentState = previousState = State.IDLE;
        
        playerStanding = this.baseTexture;
        playerJumping = ResourceManager.get().textureRegion("player", "player_jumping");
        playerRunning = createAnimation(
            ResourceManager.get().textureRegion("player", "player_running"), 8, 50
        );

        setRegion(playerStanding);

        this.body = createBody();
    }

    @Override
    protected Body createBody() {
        return new BodyBuilder(world).isDynamic()
                .setPosition(rect.getX(), rect.getY())
                .addFixture(
                        new FixtureBuilder()
                                .setBoxShape(width, height)
                                .setFilter(PLAYER_BIT, (PLATFORM_BIT |ENEMY_BIT|BULLET_BIT))
                                .build(),
                        this
                )
                .build();
    }

    /**
     * A játékos állapotának frissítése.
     * @param delta Két frissítés között eltelt idő (általában 1/60 s).
     */
    @Override
    public void update(float delta) {
        bullets.forEach(bullet -> {
            if (bullet.isRemovable()) bullets.remove(bullet);
            else bullet.update(delta);
        });
        setRegion(getFrame(delta));
        setPosition(getPosX() - getWidth()/ 2, getPosY() - getHeight() / 2);
    }

    @Override
    public void draw(Batch batch) {
        bullets.forEach(bullet -> bullet.draw(batch));
        super.draw(batch);
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
            ResourceManager.get().sound("jump").play();
            body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    /**
     * Lövéskor hívódik meg, hozzáad egy új töltényt
     * a töltények listájához.
     */
    public void fire() {
        ResourceManager.get().sound("shoot").play();
        bullets.add(new Bullet(
                world,
                new TextureRegion(ResourceManager.get().texture("bullet")),
                facingRight ? getX() * PPM + width - 5 : getX() * PPM + 5,
                getY() * PPM + 30,
                facingRight,
                false,
                this
        ));
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
