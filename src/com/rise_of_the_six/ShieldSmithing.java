package com.rise_of_the_six;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class ShieldSmithing {
	
	public static int
	energy = 29940, plate = 29942, malshield = 29935, vengshield = 29937, mercshield = 29939;
	
	public static void shieldMaking(Player player, Item item) {
		if (player.getSkills().getLevel(Skills.SMITHING) >= 91) {
			if (player.getInventory().containsItem(energy, 86) && player.getInventory().containsItem(plate, 6)) {
				player.getDialogueManager().startDialogue("ShieldSmithingD");
			} else {
				player.sm("You must have 86 Malevolent energies and 6 reinforcing plates before attempting this.");
			}
			} else {
				player.sm("You must have at least 91 Smithing before attempting this.");
			}
		}
	}