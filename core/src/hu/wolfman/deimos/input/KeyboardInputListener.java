package hu.wolfman.deimos.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import hu.wolfman.deimos.entities.Player;
import hu.wolfman.deimos.screens.GameScreen;
import hu.wolfman.deimos.utils.Logger;
import hu.wolfman.deimos.utils.MusicManager;

/**
 * A billentyűzet beviteli eseményeit kezelő osztály.
 * @author Farkas Péter
 */
public class KeyboardInputListener extends InputAdapter {
    private Player player;
    private GameScreen screen;

    public KeyboardInputListener(Player player, GameScreen screen) {
        this.player = player;
        this.screen = screen;
    }

    /**
     * Egy billentyű lenyomásakor hívódik meg.
     * @param keycode A billentyű azonosítója
     * @return true, ha nem küldjük tovább más InputProcessor-nak.
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT) player.setMovingLeft(true);
        if (keycode == Keys.RIGHT) player.setMovingRight(true);
        if (keycode == Keys.UP) player.jump();
        if (keycode == Keys.CONTROL_LEFT) player.fire();

        if (keycode == Keys.M) MusicManager.get().toggleMusic();
        if (keycode == Keys.PLUS) MusicManager.get().increaseVolume();
        if (keycode == Keys.MINUS) MusicManager.get().decreaseVolume();

        if (keycode == Keys.D) screen.toggleDebugRenderer();
        if (keycode == Keys.ESCAPE) {
            Logger.log("Kilépés a játékból");
            Gdx.app.exit();
        }
        return false;
    }

    /**
     * Egy billentyű elengedésekor hívódik meg.
     * @param keycode A billentyű azonosítója
     * @return true, ha nem küldjük tovább más InputProcessor-nak.
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT) player.setMovingLeft(false);
        if (keycode == Keys.RIGHT) player.setMovingRight(false);
        return false;
    }
}
