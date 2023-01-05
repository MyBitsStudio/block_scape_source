package com.rs.game.player.dialogue.impl;

import com.rs.game.world.WorldTile;
import com.rs.game.player.dialogue.Dialogue;

public class ClimbNoEmoteStairs extends Dialogue {

    private WorldTile upTile;
    private WorldTile downTile;

    @Override
    public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
		    player.useStairs(-1, upTile, 0, 1);
		} else if (componentId == OPTION_2)
		    player.useStairs(-1, downTile, 0, 1);
		end();
    }

    // uptile, downtile, climbup message, climbdown message, emoteid
    @Override
    public void start() {
		upTile = (WorldTile) parameters[0];
		downTile = (WorldTile) parameters[1];
		sendOptionsDialogue("What would you like to do?", (String) parameters[2], (String) parameters[3]);
    }
    
    @Override
    public void finish() { }
}