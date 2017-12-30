package hu.wolfman.deimos.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import static hu.wolfman.deimos.physics.BoxConst.PPM;

/**
 * Fixtúra (testrész) készítésére szolgáló osztály.
 * @author Farkas Péter
 */
public class FixtureBuilder {
    private final FixtureDef fixtureDef;
    private Shape shape;

    public FixtureBuilder() {
        fixtureDef = new FixtureDef();
    }

    /**
     * A fixtúra alakját kör formájúra állítja be.
     * @param radius A kör sugara.
     * @return FixtureBuilder objektum
     */
    public FixtureBuilder setCircleShape(float radius) {
        shape = new CircleShape();
        ((CircleShape) shape).setRadius(radius / PPM);
        fixtureDef.shape = shape;
        return this;
    }

    /**
     * A fixtúra alakját téglalap formájúra állítja be.
     * @param x Szélesség fele
     * @param y Hosszúság fele
     * @param cx Középpont X-koordináta
     * @param cy Középpont Y-koordináta
     * @return FixtureBuilder objektum
     */
    public FixtureBuilder setPolygonShape(float x, float y, float cx, float cy) {
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(x / PPM, y / PPM, new Vector2(cx / PPM, cy / PPM), 0);
        fixtureDef.shape = shape;
        return this;
    }

    /**
     * Beállítja a fixtúra kategória- és maszkbitjeit.
     * @param category Kategóriabit
     * @param mask Maszkbitek
     * @return FixtureBuilder objektum
     */
    public FixtureBuilder setFilter(short category, int mask) {
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = (short)mask;
        return this;
    }

    /**
     * Fixtúra beállítása szenzornak.
     * @return FixtureBuilder objektum
     */
    public FixtureBuilder setAsSensor() {
        fixtureDef.isSensor = true;
        return this;
    }

    /**
     * Fixtúra létrehozása.
     * @return Fixtúradefiníció
     */
    public FixtureDef build() {
        return fixtureDef;
    }
    
    
}
