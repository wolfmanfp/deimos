package hu.wolfman.deimos;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import hu.wolfman.deimos.tools.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * A játék textúráit, hangjait, betűtípusait kezelő osztály.
 * @author Farkas Péter
 */
public class Resources {
    private Map<String, String> items;
    private AssetManager manager;
    private String game;
    private static Resources INSTANCE;

    private Resources() {
        manager = new AssetManager();
        items = new HashMap<>();
    }
    
    /**
     * Az osztály statikus példányát visszaadó metódus.
     * Amennyiben a példány nem létezik, ebben a metódusban jön létre.
     * @return Az osztály példánya.
     */
    public static Resources get() {
        if (INSTANCE == null) {
            INSTANCE = new Resources();
        }
        return INSTANCE;
    }

    /**
     * A játék nevét beállító metódus.
     * @param game A játékmappa neve.
     */
    public void setGame(String game) {
        this.game = game;
    }
    
    /**
     * Az AssetManager finishLoading() metódusához nyújt elérést.
     */
    public void finishLoading() {
        manager.finishLoading();
    }
    
    /**
     * Hangeffektus betöltése a játék megfelelő könyvtárából.
     * @param id Azonosító, amivel hivatkozunk betöltésnél a hangeffektusra.
     * @param filename A hangfájl neve.
     */
    public void loadSound(String id, String filename) {
        String path = game + "/sounds/" + filename;
        try {
            manager.load(path, Sound.class);
            items.put(id, path);
        } catch (Exception e) {
            Logger.get().log(path + " nem található!");
        }
    }
    
    /**
     * Egy tárolt hangeffektus megkeresése.
     * Használata: Resources.get().sound("jump");
     * @param id A hangeffektus azonosítója.
     * @return Hangeffektus
     */
    public Sound sound(String id) {
        return manager.get(items.get(id), Sound.class);
    }
    
    /**
     * Zene betöltése a játék megfelelő könyvtárából.
     * @param id Azonosító, amivel hivatkozunk betöltésnél a zenére.
     * @param filename A zenefájl neve.
     */
    public void loadMusic(String id, String filename) {
        String path = game + "/music/" + filename;
        try {
            manager.load(path, Music.class);
            items.put(id, path);
        } catch (Exception e) {
            Logger.get().log(path + " nem található!");
        }
    }
    
    /**
     * Egy tárolt zene megkeresése.
     * @param id A zene azonosítója.
     * @return Zene
     */
    public Music music(String id) {
        return manager.get(items.get(id), Music.class);
    }
    
    /**
     * Textúraatlasz betöltése a játék megfelelő könyvtárából.
     * @param id Azonosító, amivel hivatkozunk betöltésnél az atlaszra.
     * @param filename A gyűjteményfájl neve.
     */
    public void loadTextureAtlas(String id, String filename) {
        String path = game + "/textures/" + filename;
        try {
            manager.load(path, TextureAtlas.class);
            items.put(id, path);
        } catch (Exception e) {
            Logger.get().log(path + " nem található!");
        }
    }
    
    /**
     * Egy tárolt textúrarégió megkeresése.
     * @param id A textúraatlasz azonosítója.
     * @param region A keresett régió.
     * @return Textúrarégió
     */
    public TextureRegion textureRegion(String id, String region) {
        return manager.get(items.get(id), TextureAtlas.class).findRegion(region);
    }
    
    /**
     * Bitmap betűtípus betöltése a játék megfelelő könyvtárából.
     * @param id Azonosító, amivel hivatkozunk betöltésnél a betűtípusra.
     * @param filename A betűtípusfájl neve.
     */
    public void loadBitmapFont(String id, String filename) {
        String path = game + "/fonts/" + filename;
        try {
            manager.load(path, BitmapFont.class);
            items.put(id, path);
        } catch (Exception e) {
            Logger.get().log(path + " nem található!");
        }
    }
    
    /**
     * Egy tárolt betűtípus megkeresése.
     * @param id A betűtípus azonosítója.
     * @return Betűtípus
     */
    public BitmapFont bitmapFont(String id) {
        return manager.get(items.get(id), BitmapFont.class);
    }
    
}
