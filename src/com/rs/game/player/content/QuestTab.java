package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.game.world.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.game.player.dialogue.impl.CasinoEntranceD;
import com.rs.utils.Colors;
import com.rs.utils.Utils;
import com.rs.game.player.dialogue.Dialogue;

/**
 * Handles the custom Quest Tab.
 * 
 * @author Zeus
 */
public class QuestTab {

	/**
	 * Sends the tab.
	 * 
	 * @param player
	 *            The player.
	 */
	public static void sendTab(Player player) {

	}

	/**
	 * Handles the custom Quest Tabs buttons.
	 * 
	 * @param player
	 *            The player using the tab.
	 * @param componentId
	 *            The interfaces childId's.
	 */
	public static void handleTab(Player player, int componentId) {
		switch (componentId) {
		case 1: /** Bank Button **/
			player.closeInterfaces();
			if (player.getPerks().isUnlocked(Perks.BANK.getId())) {
				if (!player.canSpawn()) {
					player.sendMessage("You cannot open your bank account at the moment.");
					return;
				}
				if (CasinoEntranceD.CasinoArea(player, player)) {
					player.getPackets().sendGameMessage(Colors.red + "You cannot use bank command in the casino");
					return;
				}
				if (player.isLocked()) {
					player.sendMessage("You can't bank at the moment, please wait.");
					return;
				}
				if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
					player.sendMessage("You can't bank while you're in this area.");
					return;
				}
				if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
					player.sendMessage("You can't bank 15 seconds after combat, please wait.");
					return;
				}
				player.getBank().openPlayerBank(player);
				return;
			} else
				player.sendMessage("You have to purchase the Bank Command perk in order to do this.");
			break;
		case 2: // home tele
			player.closeInterfaces();
			if (player.getCurrentInstance() != null) {
				player.getCurrentInstance().removePlayer(player);
			}
			Magic.sendNormalTeleportSpell(player, 0, 0, player.getHomeTile());
			break;
		case 3: // prayer toggle
			player.closeInterfaces();
			if (!player.getPrayer().isAncientCurses()) {
				if (player.getSkills().getLevel(Skills.PRAYER) < 50) {
					player.sm("Your Prayer level is not high enough to do this.");
					return;
				}
				player.sm("The altar fills your head with dark thoughts, purging the "
						+ "prayers from your memory and leaving only Curses in their place.");
				player.getPrayer().setPrayerBook(true);
			} else {
				player.sm("The altar eases its grip on your mind. The Curses slip from "
						+ "your memory and you recall the prayers you used to know.");
				player.getPrayer().setPrayerBook(false);
			}
			break;
		case 4:
			player.closeInterfaces();
			player.getDialogueManager().startDialogue("MagicButton");
			break;

		case 5: /** Training **/
			player.closeInterfaces();
			// player.getDialogueManager().startDialogue("TrainingTeleport");
			InterfaceManager.setPlayerInterfaceSelected(3);
			TrainingTeleports.sendInterface(player);
			break;
		case 6: /** Bosses **/
			player.closeInterfaces();
			// player.getInterfaceManager().sendTeleportsbInterface();
			// player.getDialogueManager().startDialogue("BossTeleports");
			InterfaceManager.setPlayerInterfaceSelected(1);
			BossTeleports.sendInterface(player);
			break;
		case 7: /** Minigames **/
			player.closeInterfaces();
			// player.getInterfaceManager().sendMinigamesInterface();
			InterfaceManager.setPlayerInterfaceSelected(2);
			MinigameTeleports.sendInterface(player);
			// player.getDialogueManager().startDialogue("MinigameTeleport");
			break;
		case 8: /** PvP **/
			player.closeInterfaces();
			// player.getDialogueManager().startDialogue("PvpTele");
			InterfaceManager.setPlayerInterfaceSelected(4);
			PvPTeleports.sendInterface(player);
			// player.getDialogueManager().startDialogue("PkingTeleports"); TODO delete
			break;
		case 9: /** Skilling **/
			player.closeInterfaces();
			player.getDialogueManager().startDialogue("SkillTeleport");

			// player.getDialogueManager().startDialogue("SkillTeleport");
			// InterfaceManager.setPlayerInterfaceSelected(5);
			// SkillingTeleports.sendInterface(player);
			// player.getDialogueManager().startDialogue("SkillingTeleports"); TODO delete
			break;
		case 10: /** Donator Zone **/

			player.closeInterfaces();
			player.getDialogueManager().startDialogue("MembersTeleport");
			/*
			 * if (!player.isDonator()) {
			 * player.getDialogueManager().startDialogue("SimpleMessage",
			 * "If you'd like to visit the Donator Zone, you'll have to ;;donate at least 20$ first!"
			 * ); return; } Magic.sendAncientTeleportSpell(player, 1, 0, new WorldTile(4382,
			 * 5919, 0));
			 */
			break;

		case 11:
			player.closeInterfaces();
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					sendOptionsDialogue("Helwyr Links", "Forums", "Vote", "Donate", "Discord", "Highscores");
				}

				@Override
				public void run(int interfaceId, int componentId) {
					switch (stage) {
					case -1:
						switch (componentId) {

						case OPTION_1:
							player.getPackets().sendOpenURL(Settings.FORUM);
							sendNPCDialogue(18808, HAPPY_FACE, "Opening forums on your default browser");
							stage = 99;
							break;
						case OPTION_2:
							player.getPackets().sendOpenURL(Settings.VOTE);
							sendNPCDialogue(18808, HAPPY_FACE, "Opening Voting page on your default browser");
							stage = 99;
							break;
						case OPTION_3:
							player.getPackets().sendOpenURL(Settings.DONATE);
							sendNPCDialogue(18808, HAPPY_FACE, "Opening Donation page on your default browser");
							stage = 99;
							break;
						case OPTION_4:
							player.getPackets().sendOpenURL(Settings.DISCORD);
							sendNPCDialogue(18808, HAPPY_FACE, "Opening Discord on your default browser");
							stage = 99;
							break;
						case OPTION_5:
							player.getPackets().sendOpenURL(Settings.HISCORES);
							sendNPCDialogue(18808, HAPPY_FACE, "Opening highscores on your default browser");
							stage = 99;
							break;
						}
						stage++;
						break;
					case 99:
					default:
						finish();
						break;
					}
				}

				@Override
				public void finish() {
					player.getInterfaceManager().closeChatBoxInterface();
				}
			});
			break;

		case 12: /** Account Manager **/
			player.closeInterfaces();
			AccountInterfaceManager.sendInterface(player);
			break;
		case 22: /** Prifddinas **/
			player.closeInterfaces();
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					sendOptionsDialogue("Prifddinas", "Prifddinas Main", "Hefin Herald", "Amlodd Herald", "Ithell Herald",
							Colors.red + "More");
				}

				@Override
				public void run(int interfaceId, int componentId) {
					switch (stage) {
					case -1:
						switch (componentId) {

						case OPTION_1:
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2213, 3361, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}

							break;
						case OPTION_2: // heffin
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2186, 3409, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_3: // amlodd
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2155, 3381, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_4: // ithell
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2153, 3339, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_5:
							sendOptionsDialogue("Prifddinas", "Iorwerth Herald", "Trahaearn Herald", "Cadarn Herald",
									"Crwys Herald", Colors.red + "More");
							stage = 98;
							break;
						}
						break;
					case 98:
						switch (componentId) {
						case OPTION_1:
							if (player.isAtWild()) { // Iorwerth Herald
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2185, 3312, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}

							break;
						case OPTION_2: // Trahaearn Herald
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2232, 3311, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_3: // Cadarn
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2260, 3338, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_4: // Crwys
							if (player.isAtWild()) {
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2260, 3383, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}
							break;
						case OPTION_5:
							sendOptionsDialogue("Prifddinas", "Meilyr Herald", Colors.red + "Back");
							stage = 97;
							break;
						}
						break;
					case 97:
						switch (componentId) {
						case OPTION_1:
							if (player.isAtWild()) { // Meilyr Herald
								player.getPackets()
										.sendGameMessage("A magical force is blocking you from teleporting.");
								return;
							}
							if (player.hasAccessToPrifddinas())
								Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2231, 3411, 1));
							else {
								player.sendMessage(Colors.red + "[Notice] </col>Buy the" + Colors.red
										+ " 'Elf Fiend'</col> game perk to access Prifddinas in this button.");
								player.sendMessage("Optionally you can get a total level of" + Colors.red + " 2250+");
							}

							break;

						case OPTION_2:
							sendOptionsDialogue("Prifddinas", "Prifddinas Main", "Hefin Herald", "Amlodd Herald",
									"Ithell Herald", Colors.red + "More");
							stage = -1;
							break;
						}
						break;
					case 99:
					default:
						finish();
						break;
					}
				}

				@Override
				public void finish() {
					player.getInterfaceManager().closeChatBoxInterface();
				}
			});

			break;

		default:
			player.sendMessage("Unhandled interface button: " + componentId + "; report this to an Administrator!");
			break;
		}
	}

}