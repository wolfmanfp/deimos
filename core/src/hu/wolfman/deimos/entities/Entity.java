/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.wolfman.deimos.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 *
 * @author Farkas Péter
 */
public abstract class Entity extends Sprite {
    public Body body;
    
    @Override
    public void draw(Batch batch) {
        draw(batch, 1.0f);
    }
    
    @Override
    public void draw(Batch batch, float alphaModulation) {
        super.draw(batch, alphaModulation);
    }
    
    public abstract void update(float delta);
}
