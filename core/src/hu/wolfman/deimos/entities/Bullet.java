package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;

import static hu.wolfman.deimos.physics.B2DConst.*;

/**
 * Egy töltényt definiáló osztály.
 * @author Farkas Péter
 */
public class Bullet extends Entity {
    private boolean isEnemy;
    private boolean isFlyingRight;
    private boolean removeFlag;
    private Object owner;

    /**
     * A töltény konstruktora.
     * @param world Box2D világ
     * @param baseTexture A töltény textúrája
     * @param x X koordináta
     * @param y Y koordináta
     * @param right Meghatározza, hogy milyen irányba repül
     * @param isEnemy Meghatározza, hogy az ellenség vagy a játékos tölténye
     * @param owner A tulajdonos objektum
     */
    public Bullet(World world, TextureRegion baseTexture, float x, float y, boolean right, boolean isEnemy, Object owner) {
        super(world, baseTexture);
        this.isFlyingRight = right;
        this.isEnemy = isEnemy;
        this.removeFlag = false;
        this.owner = owner;
        setPosition(x, y);
        setRegion(baseTexture);

        this.body = createBody();
        body.setBullet(true);
        body.setLinearVelocity(isFlyingRight ? 12f : -12f, 0);
    }

    @Override
    protected Body createBody() {
        return new BodyBuilder(world).isDynamic()
                .setPosition(getX(), getY())
                .addFixture(
                        new FixtureBuilder()
                                .setCircleShape(3)
                                .setFilter(
                                        isEnemy ? ENEMY_BULLET_BIT : BULLET_BIT,
                                        isEnemy ? PLATFORM_BIT | PLAYER_BIT : PLATFORM_BIT | ENEMY_BIT)
                                .build(),
                        this
                )
                .build();
    }

    @Override
    public void update(float delta) {
        setPosition(getPosX() - getWidth() / 2, getPosY() - getHeight() / 2);
    }

    /**
     * Ütközéskor hívódik meg, beállít egy flag-et,
     * hogy a játékos/ellenség töltényeinek listájából törölni lehessen.
     */
    public void setToRemove() {
        this.removeFlag = true;
    }

    /**
     * Lekérdezi, hogy eltávolítható-e a töltények listájából.
     * @return Flag
     */
    public boolean isRemovable() {
        return removeFlag;
    }

    /**
     * Tulajdonos lekérdezése.
     * @return Tulajdonos objektum
     */
    public Object getOwner() {
        return owner;
    }
}
