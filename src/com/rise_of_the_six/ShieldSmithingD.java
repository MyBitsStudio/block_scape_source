package com.rise_of_the_six;

import com.rs.game.Animation;
import com.rs.game.player.Skills;
import com.rs.game.player.content.corrupt.modes.prestige.Prestige;
import com.rs.game.player.dialogue.Dialogue;

public class ShieldSmithingD extends Dialogue {
	
	public static int
	energy = 29940, plate = 29942, malshield = 29935, vengshield = 29937, mercshield = 29939;

	@Override
	public void start() {
		sendOptionsDialogue("Which Shield Do You Wish To Make?", "Malevolent kiteshield", "Vengeful kiteshield", "Merciless kiteshield");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				player.lock(3);
				player.setNextAnimation(new Animation(898));
				player.getInventory().deleteItem(energy, 86);
				player.getInventory().deleteItem(plate, 6);
				player.getInventory().addItem(malshield, 1);
				//if(player.getGameModeManager().getMode(0) != 2)
					player.getSkills().addXp(Skills.SMITHING, 91);
				player.getPackets().sendGameMessage("You carefully combine the energy with the plates...");
				player.getPackets().sendGameMessage("...And you successfully create a Malevolent kiteshield.");
				end();
			}
			if (componentId == OPTION_2) {
				player.lock(3);
				player.setNextAnimation(new Animation(898));
				player.getInventory().deleteItem(energy, 86);
				player.getInventory().deleteItem(plate, 6);
				player.getInventory().addItem(vengshield, 1);
				//if(player.getGameModeManager().getMode(0) != 2)
					player.getSkills().addXp(Skills.SMITHING, 91);
				player.getPackets().sendGameMessage("You carefully combine the energy with the plates...");
				player.getPackets().sendGameMessage("...And you successfully create a Vengeful kiteshield.");
				end();
			}
			if (componentId == OPTION_3) {
				player.lock(3);
				player.setNextAnimation(new Animation(898));
				player.getInventory().deleteItem(energy, 86);
				player.getInventory().deleteItem(plate, 6);
				player.getInventory().addItem(mercshield, 1);
				//if(player.getGameModeManager().getMode(0) != 2)
					player.getSkills().addXp(Skills.SMITHING, 91);
				player.getPackets().sendGameMessage("You carefully combine the energy with the plates...");
				player.getPackets().sendGameMessage("...And you successfully create a Merciless kiteshield.");
				end();
			}
		}
	}

	@Override
	public void finish() {
		
	}
	
}