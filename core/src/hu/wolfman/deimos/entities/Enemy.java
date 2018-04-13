package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;
import hu.wolfman.deimos.utils.ResourceManager;

import static hu.wolfman.deimos.Constants.ATTACK_DELAY;
import static hu.wolfman.deimos.Constants.BULLET_BIT;
import static hu.wolfman.deimos.Constants.ENEMY_BIT;
import static hu.wolfman.deimos.Constants.ENEMY_HEALTH;
import static hu.wolfman.deimos.Constants.ENEMY_MASS;
import static hu.wolfman.deimos.Constants.PLATFORM_BIT;
import static hu.wolfman.deimos.Constants.PLAYER_BIT;
import static hu.wolfman.deimos.Constants.PPM;

/**
 * A játékban megjelenő ellenfelet leíró osztály.
 *
 * @author Farkas Péter
 */
public class Enemy extends Entity {
  public enum State { IDLE, SHOOTING, DEAD }

  public State previousState = State.IDLE;
  public State currentState = State.IDLE;

  private final TextureRegion enemyIdle;
  private final TextureRegion enemyDead;

  private List<Bullet> bullets;

  private int health = ENEMY_HEALTH;
  private boolean isDead = false;
  private boolean shooting = false;
  private float stateTimer = 0;

  /**
   * Az ellenfél konstruktora.
   *
   * @param world       Box2D világ
   * @param baseTexture Az ellenfél kezdő textúrája.
   * @param rect        Az ellenfél pozícióját leíró négyszög
   */
  public Enemy(World world, TextureRegion baseTexture, Rectangle rect) {
    super(world, baseTexture, rect);
    bullets = new CopyOnWriteArrayList<>();

    enemyIdle = baseTexture;
    enemyDead = ResourceManager.get().textureRegion("enemy", "enemy_dead");

    setRegion(enemyIdle);
    body = createBody();
  }

  @Override
  protected Body createBody() {
    return new BodyBuilder(world).isDynamic()
        .setPosition(rect.getX(), rect.getY())
        .addFixture(
            new FixtureBuilder()
                .setBoxShape(width, height)
                .setFilter(ENEMY_BIT, PLAYER_BIT | PLATFORM_BIT | BULLET_BIT)
                .build(),
            this
        )
        .setMass(ENEMY_MASS)
        .build();
  }

  /**
   * Az ellenfél állapotának frissítése.
   *
   * @param delta Két frissítés között eltelt idő (általában 1/60 s).
   */
  @Override
  public void update(float delta) {
    bullets.forEach(bullet -> {
      if (bullet.isRemovable()) {
        bullet.destroyBody();
        bullets.remove(bullet);
      } else {
        bullet.update(delta);
      }
    });

    if (shooting) {
      fire();
      shooting = false;
    }
    if (!shooting && stateTimer > ATTACK_DELAY) {
      shooting = true;
    }
    if (isDead) {
      body.setActive(false);
    }

    setRegion(getFrame(delta));
    setPosition(getPosX() - getWidth() / 2, getPosY() - getHeight() / 2);
  }

  @Override
  public void draw(Batch batch) {
    bullets.forEach(bullet -> bullet.draw(batch));
    super.draw(batch);
  }

  /**
   * Az ellenfél állapotát (alaphelyzet, lövés, halott) kéri le.
   *
   * @return A játékos állapota
   */
  public State getState() {
    if (isDead) {
      return State.DEAD;
    } else if (shooting) {
      return State.SHOOTING;
    } else {
      return State.IDLE;
    }
  }

  /**
   * Az ellenfél sprite-ját állítja be az állapotától függően.
   *
   * @param delta Két frissítés között eltelt idő (általában 1/60 s).
   * @return Textúrarégió
   */
  private TextureRegion getFrame(float delta) {
    currentState = getState();
    TextureRegion region;
    switch (currentState) {
      case DEAD:
        region = enemyDead;
        break;
      case SHOOTING:
      case IDLE:
      default:
        region = enemyIdle;
        break;
    }

    stateTimer = currentState == previousState ? stateTimer + delta : 0;
    previousState = currentState;

    return region;
  }

  /**
   * Lövéskor hívódik meg, hozzáad egy új töltényt
   * a töltények listájához.
   */
  public void fire() {
    if (!isDead) {
      ResourceManager.get().sound("shoot").play();
      bullets.add(new Bullet(
          world,
          new TextureRegion(ResourceManager.get().texture("bullet")),
          getX() * PPM + 5,
          getY() * PPM + 30,
          false,
          true,
          this
      ));
    }
  }

  /**
   * Az ellenfél életpontszámát csökkenti
   * a paraméterben megadott számmal.
   *
   * @param healthPoints Életszázalék
   */
  public void damage(int healthPoints) {
    health -= healthPoints;
    if (health == 0) {
      isDead = true;
    }
  }

  public boolean isDead() {
    return isDead;
  }
}
