package hu.wolfman.deimos.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

import hu.wolfman.deimos.entities.Player;
import hu.wolfman.deimos.utils.Logger;

/**
 * A számítógéphez csatlakoztatott játékvezérlő
 * eseményeit kezelő osztály.
 * @author Farkas Péter
 */
public class ControllerInputListener extends ControllerAdapter {
    private Player player;

    public ControllerInputListener(Player player) {
        this.player = player;
    }

    @Override
    public void connected(Controller controller) {
        Logger.log(String.format("%d csatlakoztatva", controller.getName()));
    }

    /**
     * Egy gomb lenyomásakor hívódik meg.
     * @param controller Az eseményt meghívó kontroller
     * @param buttonCode A gomb azonosítója
     * @return true, ha egy másik Listener-nek is átadjuk az eseményt
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == Xbox.A) {
            player.fire();
        }
        if (buttonCode == Xbox.B) {
            player.jump();
        }
        return false;
    }

    /**
     * A joystick mozgatásakor fut le.
     * @param controller Az eseményt meghívó kontroller
     * @param axisCode A megfelelő joystick megfelelő tengelyének azonosítója
     * @param value Az elmozdulás értéke
     * @return true, ha egy másik Listener-nek is átadjuk az eseményt
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) {
            player.setMovingLeft(value < -0.01);
            player.setMovingRight(value > 0.01);
        }
        return false;
    }

    /**
     * A D-pad lenyomásakor fut le.
     * @param controller Az eseményt meghívó kontroller
     * @param povCode A megfelelő gomb azonosítója
     * @param value A megadott irány
     * @return true, ha egy másik Listener-nek is átadjuk az eseményt
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        player.setMovingLeft(value.equals(PovDirection.west));
        player.setMovingRight(value.equals(PovDirection.east));
        return false;
    }
}
