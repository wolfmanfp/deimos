package hu.wolfman.deimos.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import static hu.wolfman.deimos.physics.BoxConst.PPM;

/**
 * Box2D testeket létrehozó osztály.
 * @author Farkas Péter
 */
public class BodyBuilder {
    private final World world;
    private final BodyDef bodyDef;
    private final List<FixtureDef> fixtures;

    public BodyBuilder(World world) {
        this.world = world;
        this.bodyDef = new BodyDef();
        this.fixtures = new ArrayList<>();
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
        fixtures.add(fixture);
        return this;
    }

    /**
     * Létrehozza a testet, hozzáadja a listában
     * tárolt fixtúradefiníciókat, és hozzáadja a világhoz.
     * @return Box2D test
     */
    public Body build() {
        Body body = world.createBody(bodyDef);
        for (FixtureDef fixture : fixtures) {
            body.createFixture(fixture);
        }
        return body;
    }
    
}
