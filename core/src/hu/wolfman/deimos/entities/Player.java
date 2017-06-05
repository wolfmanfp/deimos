/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 *
 * @author Farkas Péter
 */
public class Player extends Entity {
    public enum State {STANDING, DEAD, WALKING, JUMPING, SHOOTING, FALLING}
    public State currentState;
    
    @Override
    public void draw(Batch batch, float alphaModulation) {
        super.draw(batch, alphaModulation);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    
    @Override
    public void update(float delta) {
        
    }

    public void jump() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void fire() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
