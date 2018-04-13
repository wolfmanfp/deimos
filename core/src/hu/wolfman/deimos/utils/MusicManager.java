package hu.wolfman.deimos.utils;

import com.badlogic.gdx.audio.Music;

/**
 * A játék háttérzenéjét kezelő osztály.
 */
public class MusicManager {
  private Music music;
  private static MusicManager INSTANCE;

  private MusicManager() {}

  /**
   * Az osztály statikus példányát visszaadó metódus.
   * Amennyiben a példány nem létezik, ebben a metódusban jön létre.
   *
   * @return Az osztály példánya.
   */
  public static MusicManager get() {
    if (INSTANCE == null) {
      INSTANCE = new MusicManager();
    }
    return INSTANCE;
  }

  /**
   * Egy megadott azonosítójú zene betöltése és lejátszása.
   *
   * @param name A zene azonosítója
   */
  public void play(String name) {
    music = ResourceManager.get().music(name);
    music.setLooping(true);
    music.play();
  }

  /**
   * A zene ki- és bekapcsolása.
   */
  public void toggleMusic() {
    float volume = music.getVolume();
    if (volume > 0.0f) {
      music.setVolume(0.0f);
    } else {
      music.setVolume(1.0f);
    }
  }

  /**
   * A zene hangosítása.
   */
  public void increaseVolume() {
    float volume = music.getVolume();
    if (volume < 1.0f) {
      music.setVolume(volume + 0.1f);
    }
  }

  /**
   * A zene halkítása.
   */
  public void decreaseVolume() {
    float volume = music.getVolume();
    if (volume > 0.0f) {
      music.setVolume(volume - 0.1f);
    } else {
      music.setVolume(0.0f);
    }
  }

  /**
   * A zene leállítása.
   */
  public void stop() {
    music.stop();
  }
}
