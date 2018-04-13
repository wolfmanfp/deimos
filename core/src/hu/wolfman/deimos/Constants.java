package hu.wolfman.deimos;

/**
 * A programban használt konstansokat tartalmazó osztály.
 *
 * @author Farkas Péter
 */
public class Constants {
  public static final int WIDTH = 427;
  public static final int HEIGHT = 240;

  public static final int FPS = 60;
  public static final float DELTA = 1f / FPS;

  public static final float BUTTON_SIZE = 50;

  public static final String MAIN_GAME = "main";
  public static final String TITLE = "Deimos";
  public static final String TEST_LEVEL = "/maps/level.tmx";

  public static final int PLAYER_HEALTH = 100;
  public static final int PLAYER_POINTS = 100;
  public static final int PLAYER_DAMAGE = 20;
  public static final int ENEMY_HEALTH = 80;
  public static final int ENEMY_DAMAGE = 40;
  public static final float ENEMY_MASS = 100;
  public static final float ATTACK_DELAY = 1.5f;

  // Fizikához kapcsolódó konstansok
  // Pixel/méter
  public static final int PPM = 100;

  // Ütközési bitek
  public static final short PLATFORM_BIT = 1;
  public static final short PLAYER_BIT = 2;
  public static final short ENEMY_BIT = 4;
  public static final short BULLET_BIT = 8;
  public static final short ENEMY_BULLET_BIT = 16;
  public static final short LEVEL_END_BIT = 32;
}
