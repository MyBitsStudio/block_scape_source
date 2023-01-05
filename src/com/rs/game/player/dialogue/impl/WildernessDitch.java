package com.rs.game.player.dialogue.impl;

import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.LendingManager;
import com.rs.game.player.Player;
import com.rs.game.player.dialogue.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Lend;

public class WildernessDitch extends Dialogue {

    private WorldObject ditch;

    @Override
    public void finish() {

    }

    @Override
    public void run(int interfaceId, int componentId) {

    }

    @Override
    public void start() {
	ditch = (WorldObject) parameters[0];
	Lend lend = LendingManager.getLend(player);
	if (lend != null) {
	    Player lender = World.getPlayer(lend.getLendee());
	    if (lender
		    .getEquipment()
		    .getItemsContainer()
		    .containsOne(
			    new Item(lend.getItem().getDefinitions()
				    .getLendId()))
		    || lender.getInventory().containsItem(
			    lend.getItem().getDefinitions().getLendId(), 1)) {
		player.getPackets().sendGameMessage(
			"You can't bring lendable items into the Wilderness.");
		end();
		return;
	    }
	}
	player.stopAll();
	player.lock(4);
	player.setNextAnimation(new Animation(6132));
	final WorldTile toTile = new WorldTile(ditch.getRotation() == 3
		|| ditch.getRotation() == 1 ? ditch.getX() - 1 : player.getX(),
		ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ditch
			.getY() + 2 : player.getY(), ditch.getPlane());
	player.setNextForceMovement(new ForceMovement(
		new WorldTile(player),
		1,
		toTile,
		2,
		ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ForceMovement.NORTH
			: ForceMovement.WEST));
	WorldTasksManager.schedule(new WorldTask() {
	    @Override
	    public void run() {
		player.setNextWorldTile(toTile);
		player.faceObject(ditch);
		player.getControlerManager().startControler("Wilderness");
		player.resetReceivedDamage();
	    }
	}, 2);
	end();
    }
}