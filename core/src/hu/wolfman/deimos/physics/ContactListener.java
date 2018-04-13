package hu.wolfman.deimos.physics;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import hu.wolfman.deimos.entities.Bullet;
import hu.wolfman.deimos.entities.Enemy;
import hu.wolfman.deimos.entities.Player;

import static hu.wolfman.deimos.Constants.*;

/**
 * Az objektumok ütközéseit kezelő osztály.
 *
 * @author Farkas Péter
 */
public class ContactListener
    implements com.badlogic.gdx.physics.box2d.ContactListener {
  private Bullet bullet;
  private Player player;
  private Enemy enemy;

  /**
   * A metódus megadja, mi történjen két objektum ütközésekor.
   *
   * @param contact Az ütköző fixtúrákat magába foglaló objektum
   */
  @Override
  public void beginContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    int collisionDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    switch (collisionDefinition) {
      case BULLET_BIT | ENEMY_BIT:
      case BULLET_BIT | PLATFORM_BIT:
      case ENEMY_BULLET_BIT | PLAYER_BIT:
      case ENEMY_BULLET_BIT | PLATFORM_BIT:
        if (fixA.getFilterData().categoryBits == BULLET_BIT
            || fixA.getFilterData().categoryBits == ENEMY_BULLET_BIT) {
          bullet = (Bullet) fixA.getUserData();
        } else {
          bullet = (Bullet) fixB.getUserData();
        }

        bullet.setToRemove();
        break;
      case PLAYER_BIT | LEVEL_END_BIT:
        if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
          player = (Player) fixA.getUserData();
        } else {
          player = (Player) fixB.getUserData();
        }

        player.setHasReachedEndOfLevel(true);
        break;
      default:
        break;
    }
  }

  /**
   * A metódus megadja, mi történjen két objektum ütközése után.
   *
   * @param contact Az ütköző fixtúrákat magába foglaló objektum
   */
  @Override
  public void endContact(Contact contact) {
    Fixture fixA = contact.getFixtureA();
    Fixture fixB = contact.getFixtureB();

    int collisionDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

    switch (collisionDefinition) {
      case BULLET_BIT | ENEMY_BIT:
        if (fixA.getFilterData().categoryBits == BULLET_BIT) {
          bullet = (Bullet) fixA.getUserData();
          enemy = (Enemy) fixB.getUserData();
        } else {
          bullet = (Bullet) fixB.getUserData();
          enemy = (Enemy) fixA.getUserData();
        }

        player = (Player) bullet.getOwner();
        player.addPoints(PLAYER_POINTS);
        enemy.damage(ENEMY_DAMAGE);
        break;
      case ENEMY_BULLET_BIT | PLAYER_BIT:
        if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
          player = (Player) fixA.getUserData();
        } else {
          player = (Player) fixB.getUserData();
        }

        player.damage(PLAYER_DAMAGE);
        break;
      default:
        break;
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}

}
