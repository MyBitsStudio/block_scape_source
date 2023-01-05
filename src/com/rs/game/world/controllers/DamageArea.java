package com.rs.game.world.controllers;

import com.rs.game.world.WorldTile;
import com.rs.game.player.content.Magic;

/**
 * Handles everything Godwars dungeon related.
 * @author Zeus
 */
public class DamageArea extends Controller {

    @Override
    public void start() {
    	Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(2142, 5532, 3), new int[0]);
    	
    }

    /**
     * Ints containing our killcount.
     */
    private int mummyDamage;

    /**
     * Closing all interfaces upon controller removal.
     */
  

   

    @Override
    public boolean login() {
    	player.setTentMulti(0);
    	return false;
    }

    @Override
    public boolean logout() {
    	player.setTentMulti(0);
    	return false;
    }

    @Override
    public void magicTeleported(int type) {
    	player.setTentMulti(0);
    }
    
    


    /**
     * Resets all kill count.
     */
 

    @Override
    public boolean sendDeath() {
    	player.setTentMulti(0);
    	return true;
    }

    

    
   
}