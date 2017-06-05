package hu.wolfman.deimos.physics;

/**
 * A fizikához kapcsolódó konstansokat tároló osztály.
 * @author Farkas Péter
 */
public class BoxConst {
    // pixel per meter
    public static final int PPM = 100;
    
    // collision bits
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short OBJECT_BIT = 4;
    public static final short ENEMY_BIT = 8;
    public static final short BULLET_BIT = 16;
}
