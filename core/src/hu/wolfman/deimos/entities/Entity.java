package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static hu.wolfman.deimos.Constants.PPM;

/**
 * A játék entitásait leíró absztrakt osztály.
 *
 * @author Farkas Péter
 */
public abstract class Entity extends Sprite {
  protected Body body;
  protected World world;
  protected Rectangle rect;
  protected TextureRegion baseTexture;
  protected float width;
  protected float height;

  /**
   * Az osztály konstruktora.
   *
   * @param world       Box2D világ
   * @param baseTexture Kezdő textúra
   */
  public Entity(World world, TextureRegion baseTexture) {
    this.world = world;
    this.baseTexture = baseTexture;
    this.width = baseTexture.getRegionWidth();
    this.height = baseTexture.getRegionHeight();
    setBounds(0, 0, width, height);
  }

  /**
   * Az osztály konstruktora.
   *
   * @param world       Box2D világ
   * @param baseTexture Kezdő textúra
   * @param rect        Az entitás pozícióját leíró négyszög
   */
  public Entity(World world, TextureRegion baseTexture, Rectangle rect) {
    this(world, baseTexture);
    this.rect = rect;
  }

  /**
   * Box2D test létrehozása az entitás számára.
   *
   * @return Box2D test
   */
  protected abstract Body createBody();

  /**
   * Az entitás X-koordinátájának lekérdezése.
   *
   * @return Az entitás X-koordinátája.
   */
  public float getPosX() {
    return body.getPosition().x;
  }

  /**
   * Az entitás Y-koordinátájának lekérdezése.
   *
   * @return Az entitás Y-koordinátája.
   */
  public float getPosY() {
    return body.getPosition().y;
  }

  /**
   * Az entitás X irányú sebességét kérdezi le.
   *
   * @return Sebesség
   */
  public float getVelocityX() {
    return body.getLinearVelocity().x;
  }

  /**
   * Az entitás Y irányú sebességét kérdezi le.
   *
   * @return Sebesség
   */
  public float getVelocityY() {
    return body.getLinearVelocity().y;
  }

  /**
   * A karakter méretét állítja be.
   *
   * @param x      Kezdő X-koordináta
   * @param y      Kezdő Y-koordináta
   * @param width  Szélesség
   * @param height Magasság
   */
  @Override
  public void setBounds(float x, float y, float width, float height) {
    super.setBounds(x, y, width / PPM, height / PPM);
  }

  /**
   * Entitás állapotának frissítése.
   *
   * @param delta Két frissítés között eltelt idő (általában 1/60 s).
   */
  public abstract void update(float delta);
}
