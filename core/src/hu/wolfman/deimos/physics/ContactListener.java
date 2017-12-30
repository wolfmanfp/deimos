package hu.wolfman.deimos.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import static hu.wolfman.deimos.physics.BoxConst.*;

/**
 * Az objektumok ütközéseit kezelő osztály.
 * @author Farkas Péter
 */
public class ContactListener 
        implements com.badlogic.gdx.physics.box2d.ContactListener {

    /**
     * A metódus megadja, mi történjen két objektum ütközésekor.
     * @param contact Az ütköző fixtúrákat magába foglaló objektum.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        int collisionDefinition = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        
        switch(collisionDefinition) {
            case PLAYER_BIT | ENEMY_BIT:
                break;
            case BULLET_BIT | ENEMY_BIT:
                break;
            case BULLET_BIT | PLAYER_BIT:
                break;
            case BULLET_BIT | GROUND_BIT:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
}
