package hu.wolfman.deimos.physics;

/**
 * A fizikához kapcsolódó konstansokat tároló osztály.
 * @author Farkas Péter
 */
public class B2DConst {
    // Pixel/méter
    public static final int PPM = 100;
    
    // Ütközési bitek
    public static final short PLATFORM_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short BULLET_BIT = 8;
    public static final short ENEMY_BULLET_BIT = 16;
}
