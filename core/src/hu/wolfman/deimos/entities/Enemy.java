/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.wolfman.deimos.entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 *
 * @author Wolfman
 */
public class Enemy extends Entity {

    public Enemy(Body body) {
        super(body);
    }

    @Override
    public void update(float delta) {
    }
    
}
