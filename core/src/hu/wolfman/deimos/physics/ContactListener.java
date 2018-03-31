package hu.wolfman.deimos.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import hu.wolfman.deimos.entities.Bullet;
import hu.wolfman.deimos.entities.Player;

import static hu.wolfman.deimos.physics.BoxConst.*;

/**
 * Az objektumok ütközéseit kezelő osztály.
 * @author Farkas Péter
 */
public class ContactListener 
        implements com.badlogic.gdx.physics.box2d.ContactListener {

    /**
     * A metódus megadja, mi történjen két objektum ütközésekor.
     * @param contact Az ütköző fixtúrákat magába foglaló objektum
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        int collisionDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        
        switch(collisionDefinition) {
            case BULLET_BIT | ENEMY_BIT:
                Bullet bullet =
                        fixA.getFilterData().categoryBits == BULLET_BIT ?
                                (Bullet)(fixA.getUserData()) :
                                (Bullet)(fixB.getUserData());
                bullet.setToRemove();
                break;
            case ENEMY_BULLET_BIT | PLAYER_BIT:
                break;
            case BULLET_BIT | PLATFORM_BIT:
                if (fixA.getFilterData().categoryBits == BULLET_BIT) {
                    ((Bullet)fixA.getUserData()).setToRemove();
                } else ((Bullet)fixB.getUserData()).setToRemove();
                break;
        }
    }

    /**
     * A metódus megadja, mi történjen két objektum ütközése után.
     * @param contact Az ütköző fixtúrákat magába foglaló objektum
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int collisionDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(collisionDefinition) {
            case BULLET_BIT | ENEMY_BIT:
                Bullet bullet =
                        fixA.getFilterData().categoryBits == BULLET_BIT ?
                                (Bullet) (fixA.getUserData()) :
                                (Bullet) (fixB.getUserData());
                ((Player) bullet.getOwner()).addPoints(100);
                break;
            case ENEMY_BULLET_BIT | PLAYER_BIT:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
}
