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
    
    public void setCircleShape(float radius) {
        shape = new CircleShape();
        ((CircleShape) shape).setRadius(radius);
    }
    
    public void setPolygonShape(float x, float y, float cx, float cy) {
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(x, y, new Vector2(cx / PPM, cy / PPM), 0);
    }
    
    public void setFilter(short category, short mask) {
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
    }
    
    public void setAsSensor() {
        fixtureDef.isSensor = true;
    }
    
    public FixtureDef build() {
        return fixtureDef;
    }
    
    
}
