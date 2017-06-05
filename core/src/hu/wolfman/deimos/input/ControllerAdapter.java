/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.wolfman.deimos.input;

import com.badlogic.gdx.controllers.Controller;
import hu.wolfman.deimos.tools.Logger;

/**
 *
 * @author Farkas Péter
 */
public class ControllerAdapter 
        extends com.badlogic.gdx.controllers.ControllerAdapter {

    @Override
    public void disconnected(Controller controller) {
        super.disconnected(controller);
        Logger.get().log(String.format("%s csatlakoztatva", controller.getName()));
    }

    @Override
    public void connected(Controller controller) {
        super.connected(controller);
        Logger.get().log(String.format("%s lecsatlakoztatva", controller.getName()));
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonIndex) {
        return super.buttonDown(controller, buttonIndex);
    }
    
}
