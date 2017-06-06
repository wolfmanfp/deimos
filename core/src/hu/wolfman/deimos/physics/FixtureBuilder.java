package hu.wolfman.deimos.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import static hu.wolfman.deimos.physics.BoxConst.PPM;

/**
 * Alkotórész készítésére szolgáló osztály.
 * @author Farkas Péter
 */
public class FixtureBuilder {
    private final FixtureDef fixtureDef;
    private Shape shape;

    public FixtureBuilder() {
        fixtureDef = new FixtureDef();
    }
    
    public FixtureBuilder setCircleShape(float radius) {
        shape = new CircleShape();
        ((CircleShape) shape).setRadius(radius / PPM);
        fixtureDef.shape = shape;
        return this;
    }
    
    public FixtureBuilder setPolygonShape(float x, float y, float cx, float cy) {
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(x / PPM, y / PPM, new Vector2(cx / PPM, cy / PPM), 0);
        fixtureDef.shape = shape;
        return this;
    }
    
    public FixtureBuilder setFilter(short category, short mask) {
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return this;
    }
    
    public FixtureBuilder setAsSensor() {
        fixtureDef.isSensor = true;
        return this;
    }
    
    public FixtureDef build() {
        return fixtureDef;
    }
    
    
}
