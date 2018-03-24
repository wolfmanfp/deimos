package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import hu.wolfman.deimos.Resources;
import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;

import static hu.wolfman.deimos.physics.BoxConst.*;

/**
 * A játékban megjelenő ellenfelet leíró osztály.
 * @author Farkas Péter
 */
public class Enemy extends Entity {
    private final TextureRegion enemyIdle;

    /**
     * Az ellenfél konstruktora.
     * @param world Box2D világ
     * @param rect Az ellenfél pozícióját leíró négyszög
     */
    public Enemy(World world, Rectangle rect) {
        super(world, rect);
        enemyIdle = new TextureRegion(Resources.get().texture("enemy"));
        
        setBounds(0, 0, 30, 50);
        setRegion(enemyIdle);
    }

    @Override
    protected Body createBody() {
        return new BodyBuilder(world).isDynamic()
                .setPosition(rect.getX(), rect.getY())
                .addFixture(
                        new FixtureBuilder()
                                .setPolygonShape(12, 12, 0, 0)
                                .setFilter(ENEMY_BIT, (PLAYER_BIT|GROUND_BIT|BULLET_BIT))
                                .build()
                )
                .build();
    }

    @Override
    public void update(float delta) {
        setPosition(getPosX() - getWidth()/ 2, getPosY() - getWidth() / 2);
    }
    
}
