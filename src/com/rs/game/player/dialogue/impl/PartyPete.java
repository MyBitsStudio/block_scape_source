package com.rs.game.player.dialogue.impl;

import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.game.player.dialogue.Dialogue;
import com.rs.utils.Colors;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;

/**
 * Handles the Party Pete dialogue.
 * 
 * @author Zeus
 */
public class PartyPete extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendOptionsDialogue("Select an Option", "Store Credit Shop", "Quests", "Vote Store", Colors.red+"[2 Coins] </col>Animation Store </col>",
				"Special Perks" + Colors.green + " [500hrs Playtime]");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			switch (componentId) {
				case OPTION_1:
					player.getDialogueManager().startDialogue("ReferralDonationD");
					break;
				case OPTION_2:
					player.getDialogueManager().startDialogue("QuestsD");
					break;
				case OPTION_3:
					finish();
					ShopsHandler.openShop(player, 47);
					break;
				case OPTION_4:
					player.getDialogueManager().startDialogue("AnimationStoreD");
					break;
			}
		}

	}

	@Override
	public void finish() {
		player.getInterfaceManager().closeChatBoxInterface();

	}
}