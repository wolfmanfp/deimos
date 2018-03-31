package hu.wolfman.deimos.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;
import java.util.Map;

import static hu.wolfman.deimos.physics.B2DConst.PPM;

/**
 * Box2D testeket létrehozó osztály.
 * @author Farkas Péter
 */
public class BodyBuilder {
    private final World world;
    private final BodyDef bodyDef;
    private Map<FixtureDef, Object> fixtures;

    public BodyBuilder(World world) {
        this.world = world;
        this.bodyDef = new BodyDef();
        this.fixtures = new HashMap<>();
    }

    /**
     * A testet dinamikusra állítja.
     * @return BodyBuilder objektum
     */
    public BodyBuilder isDynamic() {
        bodyDef.type = BodyType.DynamicBody;
        return this;
    }

    /**
     * A testet statikusra állítja.
     * @return BodyBuilder objektum
     */
    public BodyBuilder isStatic() {
        bodyDef.type = BodyType.StaticBody;
        return this;
    }

    /**
     * A test pozícióját állítja be.
     * @param x X-koordináta
     * @param y Y-koordináta
     * @return BodyBuilder objektum
     */
    public BodyBuilder setPosition(float x, float y) {
        bodyDef.position.set(x / PPM, y / PPM);
        return this;
    }

    /**
     * A paraméterként megadott fixtúradefiníció
     * hozzáadása a BodyBuilder objektum listájához.
     * @param fixture Fixtúra definíció
     * @return BodyBuilder objektum
     */
    public BodyBuilder addFixture(FixtureDef fixture) {
        fixtures.put(fixture, null);
        return this;
    }

    /**
     * A paraméterként megadott fixtúradefiníció
     * hozzáadása a BodyBuilder objektum listájához.
     * @param fixture Fixtúra definíció
     * @param userData Fixtúrához tartozó adat
     * @return BodyBuilder objektum
     */
    public BodyBuilder addFixture(FixtureDef fixture, Object userData) {
        fixtures.put(fixture, userData);
        return this;
    }

    /**
     * Létrehozza a testet, hozzáadja a listában
     * tárolt fixtúradefiníciókat, és hozzáadja a világhoz.
     * @return Box2D test
     */
    public Body build() {
        Body body = world.createBody(bodyDef);
        fixtures.forEach((fixtureDef, userData) ->
                body.createFixture(fixtureDef).setUserData(userData)
        );
        return body;
    }
    
}
