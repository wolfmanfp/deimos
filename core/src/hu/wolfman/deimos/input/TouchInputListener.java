package hu.wolfman.deimos.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import hu.wolfman.deimos.entities.Player;

/**
 * Az érintőképernyő gombjain történő bevitelt
 * kezelő osztály.
 * @author Farkas Péter
 */
public class TouchInputListener extends InputListener {
    private String buttonName;
    private Player player;

    /**
     * Az osztály konstruktora.
     * @param buttonName A gomb azonosítója, ez alapján dől el, hogy milyen művelet fut le
     * @param player Játékos
     */
    public TouchInputListener(String buttonName, Player player) {
        this.buttonName = buttonName;
        this.player = player;
    }

    /**
     * Egy gomb lenyomásakor fut le.
     */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (buttonName.equals("button_left")) player.setMovingLeft(true);
        if (buttonName.equals("button_right")) player.setMovingRight(true);
        if (buttonName.equals("button_shoot")) player.fire();
        if (buttonName.equals("button_jump")) player.jump();
        return true;
    }

    /**
     * Egy gomb felengedésekor fut le.
     */
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (buttonName.equals("button_left")) player.setMovingLeft(false);
        if (buttonName.equals("button_right")) player.setMovingRight(false);
    }
}
