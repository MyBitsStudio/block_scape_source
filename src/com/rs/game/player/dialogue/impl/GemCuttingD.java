package com.rs.game.player.dialogue.impl;

import com.rs.game.player.actions.crafting.GemCutting;
import com.rs.game.player.actions.crafting.GemCutting.Gem;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.dialogue.Dialogue;

public class GemCuttingD extends Dialogue {

    private Gem gem;

    @Override
    public void finish() {

    }

    @Override
    public void run(int interfaceId, int componentId) {
	player.getActionManager().setAction(
		new GemCutting(gem, SkillsDialogue.getQuantity(player)));
	end();
    }

    @Override
    public void start() {
	this.gem = (Gem) parameters[0];
	SkillsDialogue
		.sendSkillsDialogue(
			player,
			SkillsDialogue.CUT,
			"Choose how many you wish to cut,<br>then click on the item to begin.",
			player.getInventory().getItems()
				.getNumberOf(gem.getUncut()),
			new int[] { gem.getUncut() }, null);

    }

}