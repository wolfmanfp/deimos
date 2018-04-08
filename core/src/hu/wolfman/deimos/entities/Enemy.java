package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import hu.wolfman.deimos.physics.BodyBuilder;
import hu.wolfman.deimos.physics.FixtureBuilder;

import static hu.wolfman.deimos.Constants.*;

/**
 * A játékban megjelenő ellenfelet leíró osztály.
 *
 * @author Farkas Péter
 */
public class Enemy extends Entity {
  private final TextureRegion enemyIdle;

  /**
   * Az ellenfél konstruktora.
   *
   * @param world       Box2D világ
   * @param baseTexture Az ellenfél kezdő textúrája.
   * @param rect        Az ellenfél pozícióját leíró négyszög
   */
  public Enemy(World world, TextureRegion baseTexture, Rectangle rect) {
    super(world, baseTexture, rect);
    enemyIdle = baseTexture;
    setRegion(enemyIdle);
    body = createBody();
  }

  @Override
  protected Body createBody() {
    return new BodyBuilder(world).isDynamic()
        .setPosition(rect.getX(), rect.getY())
        .addFixture(
            new FixtureBuilder()
                .setBoxShape(width, height)
                .setFilter(ENEMY_BIT, (PLAYER_BIT | PLATFORM_BIT | BULLET_BIT))
                .build(),
            this
        )
        .build();
  }

  @Override
  public void update(float delta) {
    setPosition(getPosX() - getWidth() / 2, getPosY() - getHeight() / 2);
  }

}
