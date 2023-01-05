package com.rs.game.world.controllers;

import com.rs.game.Animation;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.tasks.WorldTask; 
import com.rs.game.tasks.WorldTasksManager;

public class CorpBeastController extends Controller {

    @Override
    public boolean login() {
    	return false; // so doesnt remove script
    }

    @Override
    public boolean logout() {
    	return false; // so doesnt remove script
    }

    @Override
    public void magicTeleported(int type) {
    	removeControler();
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 37929 || object.getId() == 38811) {
		    removeControler();
		    player.stopAll();
		    player.setNextWorldTile(new WorldTile(2970, 4384, player.getPlane()));
		    return false;
		}
		return true;
    }

    @Override
    public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
		    int loop;
	
		    @Override
		    public void run() {
				if (loop == 0) {
				    player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
				    player.sendMessage("Oh dear, you have died.");
				} else if (loop == 3) {
				    //Player killer = player.getMostDamageReceivedSourcePlayer();
				    //if (killer != null) {
				    //	killer.removeDamage(player);
				    //}
					//player.sendItemsOnDeath(player);
				    //player.getEquipment().init();
				    //player.getInventory().init();
				    player.reset();
				    player.setNextWorldTile(new WorldTile(player.getHomeTile()));
				    player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
				    removeControler();
				    player.getPackets().sendMusicEffect(90);
				    stop();
				}
				loop++;
		    }
		}, 0, 1);
		return false;
    }

    @Override
    public void start() {

    }
}