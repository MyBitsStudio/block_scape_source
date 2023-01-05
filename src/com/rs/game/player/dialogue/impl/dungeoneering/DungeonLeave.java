package com.rs.game.player.dialogue.impl.dungeoneering;

import com.rs.game.world.controllers.DungeonController;
import com.rs.game.player.dialogue.Dialogue;

public class DungeonLeave extends Dialogue {

	private DungeonController dungeon;

	@Override
	public void start() {
		dungeon = (DungeonController) parameters[0];
		sendOptionsDialogue("Leave the dungeon permanently?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1)
			dungeon.leaveDungeonPermanently();
		end();
	}

	@Override
	public void finish() {

	}

}