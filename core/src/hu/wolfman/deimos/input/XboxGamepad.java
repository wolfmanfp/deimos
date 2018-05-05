package hu.wolfman.deimos.input;

import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.utils.SharedLibraryLoader;

/**
 * Wrapper osztály a beépített Xbox osztályhoz.
 * Windows alatt inicializálja az értékeket, más operációs rendszerek esetén
 * visszaáll a másik osztályt hívja meg.
 * @see com.badlogic.gdx.controllers.mappings.Xbox
 */
public class XboxGamepad {
  public static final int A;
  public static final int B;
  public static final int L_STICK_HORIZONTAL_AXIS;

  static {
    if (SharedLibraryLoader.isWindows) {
      A = 0;
      B = 1;
      L_STICK_HORIZONTAL_AXIS = 0;
    } else {
      A = Xbox.A;
      B = Xbox.B;
      L_STICK_HORIZONTAL_AXIS = Xbox.L_STICK_HORIZONTAL_AXIS;
    }
  }
}
