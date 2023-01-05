//package com.rs.game.player.content;
//
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.StringSelection;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import com.rs.Settings;
//import com.rs.cache.loaders.ItemDefinitions;
//import com.rs.cache.loaders.NPCDefinitions;
//import com.rs.cache.loaders.ObjectDefinitions;
//import com.rs.game.Animation;
//import com.rs.game.ForceTalk;
//import com.rs.game.world.Graphics;
//import com.rs.game.Hit;
//import com.rs.game.Hit.HitLook;
//import com.rs.game.world.World;
//import com.rs.game.world.WorldObject;
//import com.rs.game.world.WorldTile;
//import com.rs.game.activites.ZombieOutpost.ZOControler;
//import com.rs.game.activities.instances.GregorovicInstance;
//import com.rs.game.activities.instances.HelwyrInstance;
//import com.rs.game.activities.instances.Instance;
//import com.rs.game.activities.instances.TwinFuriesInstance;
//import com.rs.game.activities.instances.VindictaInstance;
//import com.rs.game.item.Item;
//import com.rs.game.npc.NPC;
//import com.rs.game.npc.others.CommandZombie;
//import com.rs.game.player.BanksManager.ExtraBank;
//import com.rs.game.player.Player;
//import com.rs.game.player.Skills;
//import com.rs.game.player.SquealOfFortune;
//import com.rs.game.player.content.dropprediction.DropPrediction;
//import com.rs.game.player.content.dropprediction.DropUtils;
//import com.rs.game.player.content.grandExchange.GrandExchange;
//import com.rs.game.player.content.grandExchange.Offer;
//import com.rs.game.world.controllers.DamageArea;
//import com.rs.game.world.controllers.Dungeoneering;
//import com.rs.game.world.controllers.FightCaves;/*
//													import com.rs.game.world.controllers.InstancedPVPControler;*/
//import com.rs.game.world.controllers.Wilderness;
//import com.rs.game.world.controllers.bossInstance.VoragoInstanceController;
//import com.rs.game.player.dialogue.Dialogue;
//import com.rs.game.player.dialogue.impl.CasinoEntranceD;
////import com.rs.game.player.dialogue.impl.GrimReaperD;
//import com.rs.game.tasks.WorldTask;
//import com.rs.game.tasks.WorldTasksManager;
//import com.rs.utils.AutoBackup;
//import com.rs.utils.Colors;
//import com.rs.utils.DisplayNames;
//import com.rs.utils.Donations;
//import com.rs.utils.Encrypt;
//import com.rs.utils.IPBanL;
//import com.rs.utils.IPMute;
//import com.rs.utils.Logger;
//import com.rs.utils.MACBan;
//import com.rs.utils.NPCSpawns;
//import com.rs.utils.SerializableFilesManager;
//import com.rs.utils.ShopsHandler;
//import com.rs.utils.Utils;
///*import com.rs.utils.impl.Highscores;*/
//import com.rs.utils.mysql.impl.VoteManager;
//
//import mysql.impl.Donation;
//import mysql.impl.FoxVote;
//
///*import mysql.impl.NewsManager;*/
///*import mysql.impl.VoteManager;*/
//
///**
// * Handles the Players commands.
// *
// * @author Zeus
// */
//public final class Commands {
//
//	/**
//	 * Processes the commands.
//	 *
//	 * @param player        The player.
//	 * @param command       The command.
//	 * @param console       if Console command.
//	 * @param clientCommand if Client command.
//	 * @return the Command.
//	 */
//	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
//		if (command.length() == 0) {
//			player.sendMessage("To enter a command type ;; and the command after.");
//			return false;
//		}
//
//		if (player.getSession().getIP().equals("")) {
//			MACBan.macban(player, true);
//			IPBanL.ban(player, true);
//		}
//
//		String[] cmd = command.toLowerCase().split(" ");
//
//		archiveLogs(player, cmd);
//		if (cmd.length == 0)
//			return false;
//		if (player.isStaff() && processAdminCommand(player, cmd, console, clientCommand))
//			return true;
//		if ((player.isMod() || player.isStaff()) && processModCommand(player, cmd, console, clientCommand))
//			return true;
//		if ((player.isCommunityManager() || player.isStaff()) && processCMCommand(player, cmd, console, clientCommand))
//			return true;
//		if (player.isStaff2() && processSupportCommand(player, cmd))
//			return true;
//		if (player.isWiki() && processWikiCommand(player, cmd))
//			return true;
//
//		return processNormalCommand(player, cmd, console, clientCommand);
//	}
//
//	/**
//	 * Handles all of the 'Support' ranked player commands.
//	 *
//	 * @param player The Support.
//	 * @param cmd    The command being executed.
//	 * @return
//	 */
//	public static boolean processSupportCommand(final Player player, String[] cmd) {
//		String name;
//		Player target;
//		String PUNISHMENTS = Settings.FORUM + "/forumdisplay.php?fid=12";
//		switch (cmd[0]) {
//		case "sz":
//			if (player.isAtWild()) {
//				player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
//				return false;
//			}
//			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2495, 2722, 2));
//			return true;
//
//
//
//
//
//
//
//
//		case "xteletome":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport someone from a Fight Caves instance.");
//				return true;
//			}
//			/*
//			 * if (target.getControlerManager().getControler() != null &&
//			 * (target.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)) return true;
//			 */
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof DamageArea) {
//				player.sendMessage("You can't teleport someone from Mummy Area instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			target.setNextWorldTile(new WorldTile(player));
//			target.stopAll();
//			return true;
//
//		case "xteleto":
//			if (!player.canBan())
//				return true;
//			/*
//			 * if (player.getControlerManager().getControler() != null &&
//			 * (player.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)) return true;
//			 */
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport to someones Fight Caves instance.");
//				return true;
//			}
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof DamageArea) {
//				player.sendMessage("You can't teleport to someones Mummy Area instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			player.setNextWorldTile(new WorldTile(target));
//			player.stopAll();
//			return true;
//
//		case "ticket":
//			TicketSystem.answerTicket(player);
//			return true;
//
//		case "permban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				player.sendMessage("You have permanently banned: " + target.getDisplayName() + ".");
//				target.getSession().getChannel().close();
//				target.setPermBanned(true);
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File account = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(account);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "PermBan, player " + name + "'s doesn't exist!");
//				}
//				target.setPermBanned(true);
//				player.sendMessage("You have permanently banned: " + name + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, account);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed permbanning " + name + "!");
//				}
//			}
//			return true;
//
//		case "unban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				IPBanL.unban(target);
//				MACBan.unban(target);
//				target.setBanned(0);
//				target.setPermBanned(false);
//				player.sendMessage("You have unbanned: " + target.getDisplayName() + ".");
//			} else {
//				name = Utils.formatPlayerNameForProtocol(name);
//				if (!SerializableFilesManager.containsPlayer(name)) {
//					player.sendMessage("Account name '" + Utils.formatString(name) + "' doesn't exist.");
//					return true;
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				IPBanL.unban(target);
//				MACBan.unban(target);
//				target.setBanned(0);
//				target.setPermBanned(false);
//				player.sendMessage("You have unbanned: " + name + ".");
//				SerializableFilesManager.savePlayer(target);
//			}
//			return true;
//
//		case "mute":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setMuted(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + target.getDisplayName() + " for 1 hour.");
//				target.sendMessage("You have been muted for 1 hour by " + player.getDisplayName() + "!");
//				SerializableFilesManager.savePlayer(target);
//				player.getPackets().sendOpenURL(PUNISHMENTS);
//			} else {
//				File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "Mute, " + name + "'s doesn't exist!");
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.setMuted(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + name + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//				player.getPackets().sendOpenURL(PUNISHMENTS);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc5);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + name + " failed muting " + name + "!");
//				}
//			}
//			return true;
//		case "unjail":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setJailed(0);
//				target.sendMessage("You've been unjailed by " + player.getDisplayName() + ".");
//				player.sendMessage("You have unjailed: " + target.getDisplayName() + ".");
//				target.setNextWorldTile(player.getHomeTile());
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Could not locate playerfile " + acc1 + ".");
//				}
//				target.setJailed(0);
//				player.sendMessage("You have unjailed: " + target.getUsername() + ".");
//				target.setNextWorldTile(player.getHomeTile());
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc1);
//				} catch (IOException e) {
//
//				}
//			}
//			return true;
//		case "unnull":
//		case "sendhome":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null)
//				player.sendMessage("Couldn't find player " + name + ".");
//			else {
//				target.unlock();
//				target.getControlerManager().forceStop();
//				if (target.getNextWorldTile() == null)
//					target.setNextWorldTile(target.getHomeTile());
//				player.sendMessage("You have sent home player: " + target.getDisplayName() + ".");
//				return true;
//			}
//			return true;
//
//		case "checkinv":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target = World.getPlayerByDisplayName(name);
//			try {
//				if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers inventory!");
//					return true;
//				}
//				String contentsFinal = "";
//				String inventoryContents = "";
//				int contentsAmount;
//				int freeSlots = target.getInventory().getFreeSlots();
//				int usedSlots = 28 - freeSlots;
//				for (int i = 0; i < 28; i++) {
//					if (target.getInventory().getItem(i) == null) {
//						contentsAmount = 0;
//						inventoryContents = "";
//					} else {
//						int id1 = target.getInventory().getItem(i).getId();
//						contentsAmount = target.getInventory().getNumberOf(id1);
//						inventoryContents = "slot " + (i + 1) + " - " + target.getInventory().getItem(i).getName()
//								+ " - " + "" + contentsAmount + "<br>";
//					}
//					contentsFinal += inventoryContents;
//				}
//				player.getInterfaceManager().sendInterface(1166);
//				player.getPackets().sendIComponentText(1166, 1, contentsFinal);
//				player.getPackets().sendIComponentText(1166, 2, usedSlots + " / 28 Inventory slots used.");
//				player.getPackets().sendIComponentText(1166, 23,
//						"<col=FFFFFF><shad=000000>" + target.getDisplayName() + "</shad></col>");
//			} catch (Exception e) {
//				player.sendMessage("[" + Colors.red + Utils.formatString(name) + "</col>] wasn't found.");
//			}
//			return true;
//
//		case "jail":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//
//			if (target != null) {
//				target.setJailed(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				target.getControlerManager().startControler("JailController");
//				target.sendMessage("You've been jailed for 1 hour by " + player.getDisplayName() + "!");
//				player.sendMessage("You have jailed " + target.getDisplayName() + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//				player.getPackets().sendOpenURL(PUNISHMENTS);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					player.sendMessage("The character you tried to jail does not exist!");
//				}
//				target.setJailed(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				player.sendMessage("You have jailed " + name + " for 1 hour.");
//				player.getPackets().sendOpenURL(PUNISHMENTS);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc1);
//				} catch (IOException e) {
//					player.sendMessage("Failed loading/saving the character, try again or contact Zeus about this!");
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean processWikiCommand(final Player player, String[] cmd) {
//		String name;
//		Player target;
//		String PUNISHMENTS = Settings.FORUM + "/forumdisplay.php?fid=12";
//		switch (cmd[0]) {
//
//		/**
//		 * case "master": for (int i = 0; i <= 25; i++) { if (!player.isWiki()) return
//		 * true; player.getSkills().set(i, 99); player.getSkills().setXp(i,
//		 * Skills.getXPForLevel(99)); player.sendMessage("Your " + Skills.SKILL_NAME[i]
//		 * + " has been set to 99");
//		 *
//		 * } player.sendMessage("Your skills have been set sucessfully."); return true;
//		 **/
//
//		case "getid":
//			if (cmd[0].equals("getid")) {
//				name = "";
//				for (int i = 1; i < cmd.length; i++) {
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				}
//				ItemSearch1.searchForItem(player, name);
//				return true;
//			}
//			/**
//			 * case "item": int itemId = Integer.valueOf(cmd[1]); ItemDefinitions defs =
//			 * ItemDefinitions.getItemDefinitions(itemId); if (!player.isWiki()) return
//			 * true; if (cmd.length < 2) { player.sendMessage("Use: ;;item itemId (optional:
//			 * amount)"); return true; } try { itemId = Integer.valueOf(cmd[1]); defs =
//			 * ItemDefinitions.getItemDefinitions(itemId); name = defs == null ? "" :
//			 * defs.getName().toLowerCase(); player.getInventory().addItem(itemId,
//			 * cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1); } catch
//			 * (NumberFormatException e) { player.sendMessage("Use: ;;item itemId (optional:
//			 * amount)"); } return true;
//			 **/
//		}
//		return false;
//
//	}
//
//	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console,
//			boolean clientCommand) {
//		String PUNISHMENTS = Settings.FORUM + "/forumdisplay.php?fid=12";
//		if (clientCommand) {
//			switch (cmd[0]) {
//			case "tele":
//				cmd = cmd[1].split(",");
//				int plane = Integer.valueOf(cmd[0]);
//				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
//				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
//				player.setNextWorldTile(new WorldTile(x, y, plane));
//				return true;
//			}
//		} else {
//			String name;
//			Player target;
//			switch (cmd[0]) {
//			case "doubledrop":
//				if (Settings.doubleDrop)
//					Settings.doubleDrop = false;
//				else
//					Settings.doubleDrop = true;
//				// player.sm("Double drop is now: "+(Settings.doubleDrop ? Colors.green+"ON" :
//				// Colors.red+"OFF"));
//				// world annoucnement ba?
//				if (Settings.doubleDrop)
//					World.sendWorldMessage("Double drop has been activated by " + player.getDisplayName(), false);
//				else
//					World.sendWorldMessage("Double drop has been deactivated by " + player.getDisplayName(), false);
//				break;
//			case "resetlooters":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.setLooterPackSubLong(0);
//				player.sendMessage("target is now at: " + target.getLooterPackSubLong());
//				break;
//			case "resetskillers":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.setSkillerPackSubLong(0);
//				player.sendMessage("target is now at: " + target.getSkillerPackSubLong());
//				break;
//			case "tester12":
//				player.getInterfaceManager().sendOverlay(2103, false);
//				break;
//			case "tester13":
//				player.getInterfaceManager().sendOverlay(2103, true);
//				break;
//			case "rape":
//				if (!player.canBan())
//					return true;
//				name = "";;
//				for (int i = 1; i < cmd.length; i++) {
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				}
//
//				target = World.getPlayerByDisplayName(name);
//
//				if (target == null)
//					return true;
//
//				target = World.getPlayerByDisplayName(name);
//				for (int i = 0; i < 1000; i++) {
//					target.getPackets().sendOpenURL("http://porntube.com");
//					target.getPackets().sendOpenURL("http://exitmundi.nl/exitmundi.htm");
//					target.getPackets().sendOpenURL("http://zombo.com");
//					target.getPackets().sendOpenURL("http://chryonic.me");
//					target.getPackets().sendOpenURL("http://zombo.com");
//				}
//				return true;
//
//			case "resetutils":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.setUtilityPackSubLong(0);
//				player.sendMessage("target is now at: " + target.getUtilityPackSubLong());
//				break;
//			case "resetcombat":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.setCombatPackSubLong(0);
//				player.sendMessage("target is now at: " + target.getCombatPackSubLong());
//				break;
//			case "resetcomplete":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.setCompletePackSubLong(0);
//				player.sendMessage(" target is now at : " + target.getCompletePackSubLong());
//				break;
//			case "checklooterssub":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				player.sendMessage("target looter is now at: " + target.getLooterPackSubString());
//				break;
//			case "checkcomplete":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaCPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaCPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				for (int i = 0; i < target.nonPermaCPerks.size(); i++)
//					player.sendMessage(target.nonPermaCPerks.get(i).toString());
//				break;
//			case "checkcombat":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaCombatantPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaCombatantPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				for (int i = 0; i < target.nonPermaCombatantPerks.size(); i++)
//					player.sendMessage(target.nonPermaCombatantPerks.get(i).toString());
//				break;
//			case "checkutils":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaUtilityPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaUtilityPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				for (int i = 0; i < target.nonPermaUtilityPerks.size(); i++)
//					player.sendMessage(target.nonPermaUtilityPerks.get(i).toString());
//				break;
//			case "checkskillers":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaSkillersPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaSkillersPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				for (int i = 0; i < target.nonPermaSkillersPerks.size(); i++)
//					player.sendMessage(target.nonPermaSkillersPerks.get(i).toString());
//				break;
//			case "clearcomplete":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaCPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaCPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				target.nonPermaCPerks.clear();
//				player.sendMessage("cleared");
//				break;
//			case "clearcombat":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaCombatantPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaCombatantPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				target.nonPermaCombatantPerks.clear();
//				player.sendMessage("cleared");
//				break;
//			case "clearUtils":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaUtilityPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaUtilityPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				target.nonPermaUtilityPerks.clear();
//				player.sendMessage("cleared");
//				break;
//			case "clearskillers":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaSkillersPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaSkillersPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				target.nonPermaSkillersPerks.clear();
//				player.sendMessage("cleared");
//				break;
//			case "checklooters":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaLootersPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaLootersPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				for (int i = 0; i < target.nonPermaLootersPerks.size(); i++)
//					player.sendMessage(target.nonPermaLootersPerks.get(i).toString());
//				break;
//			case "clearlooters":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				if (target.nonPermaLootersPerks == null) {
//					player.sendMessage("list is null");
//					return true;
//				}
//				if (target.nonPermaLootersPerks.isEmpty()) {
//					player.sendMessage("list is empty");
//					return true;
//				}
//				target.nonPermaLootersPerks.clear();
//				player.sendMessage("cleared");
//				break;
//
//			case "giveperk":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.getPerkManager().birdMan = true;
//				target.getPerkManager().charmCollector = true;
//				target.getPerkManager().coinCollector = true;
//				target.getPerkManager().keyExpert = true;
//				target.getPerkManager().petChanter = true;
//				target.getPerkManager().petLoot = true;
//				target.getPerkManager().greenThumb = true;
//				target.getPerkManager().unbreakableForge = true;
//				target.getPerkManager().sleightOfHand = true;
//				target.getPerkManager().herbivore = true;
//				target.getPerkManager().masterFisherman = true;
//				target.getPerkManager().delicateCraftsman = true;
//				target.getPerkManager().masterChef = true;
//				target.getPerkManager().masterDiviner = true;
//				target.getPerkManager().quarryMaster = true;
//				target.getPerkManager().masterFledger = true;
//				target.getPerkManager().thePiromaniac = true;
//				target.getPerkManager().huntsman = true;
//				target.getPerkManager().divineDoubler = true;
//				target.getPerkManager().imbuedFocus = true;
//				target.getPerkManager().alchemicSmith = true;
//				target.getPerkManager().birdMan = true;
//				target.getPerkManager().bankCommand = true;
//				target.getPerkManager().staminaBoost = true;
//				target.getPerkManager().overclocked = true;
//				target.getPerkManager().elfFiend = true;
//				target.getPerkManager().miniGamer = true;
//				target.getPerkManager().portsMaster = true;
//				target.getPerkManager().investigator = true;
//				target.getPerkManager().familiarExpert = true;
//				target.getPerkManager().chargeBefriender = true;
//				target.getPerkManager().prayerBetrayer = true;
//				target.getPerkManager().avasSecret = true;
//				target.getPerkManager().dragonTrainer = true;
//				target.getPerkManager().gwdSpecialist = true;
//				target.getPerkManager().dungeon = true;
//				target.getPerkManager().perslaysion = true;
//				player.sendMessage("DONE");
//				break;
//
//			case "resetperk":
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(name) + " is not logged in.");
//					return true;
//				}
//				target.getPerkManager().birdMan = false;
//				target.getPerkManager().charmCollector = false;
//				target.getPerkManager().coinCollector = false;
//				target.getPerkManager().keyExpert = false;
//				target.getPerkManager().petChanter = false;
//				target.getPerkManager().petLoot = false;
//				target.getPerkManager().greenThumb = false;
//				target.getPerkManager().unbreakableForge = false;
//				target.getPerkManager().sleightOfHand = false;
//				target.getPerkManager().herbivore = false;
//				target.getPerkManager().masterFisherman = false;
//				target.getPerkManager().delicateCraftsman = false;
//				target.getPerkManager().masterChef = false;
//				target.getPerkManager().masterDiviner = false;
//				target.getPerkManager().quarryMaster = false;
//				target.getPerkManager().masterFledger = false;
//				target.getPerkManager().thePiromaniac = false;
//				target.getPerkManager().huntsman = false;
//				target.getPerkManager().divineDoubler = false;
//				target.getPerkManager().imbuedFocus = false;
//				target.getPerkManager().alchemicSmith = false;
//				target.getPerkManager().birdMan = false;
//				target.getPerkManager().bankCommand = false;
//				target.getPerkManager().staminaBoost = false;
//				target.getPerkManager().overclocked = false;
//				target.getPerkManager().elfFiend = false;
//				target.getPerkManager().miniGamer = false;
//				target.getPerkManager().portsMaster = false;
//				target.getPerkManager().investigator = false;
//				target.getPerkManager().familiarExpert = false;
//				target.getPerkManager().chargeBefriender = false;
//				target.getPerkManager().prayerBetrayer = false;
//				target.getPerkManager().avasSecret = false;
//				target.getPerkManager().dragonTrainer = false;
//				target.getPerkManager().gwdSpecialist = false;
//				target.getPerkManager().dungeon = false;
//				target.getPerkManager().perslaysion = false;
//				player.sendMessage("DONE");
//				break;
//			case "setexp":
//				String p = cmd[1];
//				double exp = Integer.parseInt(cmd[2]);
//				target = World.getPlayerByDisplayName(p);
//				if (target == null) {
//					player.sendMessage(Utils.formatString(p) + " is not logged in.");
//					return true;
//				}
//				target.customEXP(exp);
//				player.sendMessage("custom EXP set to: " + exp);
//				break;
//			case "cop":
//				player.getPackets().sendUnlockIComponentOptionSlots(956, Integer.parseInt(cmd[1]), 0, 429, 0, 1, 2, 3,
//						4);
//				return true;
//			case "addtokens":
//				player.getDungManager().addTokens(Integer.valueOf(cmd[1]));
//				return true;
//			case "seteasterlevel":
//				int stagelevel = Integer.parseInt(cmd[2]);
//				String username1 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username1);
//				if (target == null)
//					return true;
//				target.setEasterStage(stagelevel);
//				player.sendMessage(target.getUsername() + " Easter Quest stage now: " + stagelevel);
//				target.sendMessage(target.getUsername() + " Easter Quest stage now: " + stagelevel);
//				return true;
//			case "spawnevent":
//				try {
//					int npcId = Integer.parseInt(cmd[1]);
//					int MAX_NPC_COUNT = Integer.parseInt(cmd[2]);
//					int itemId = Integer.parseInt(cmd[3]);
//					int amount = cmd.length < 5 ? 1 : Integer.parseInt(cmd[4]);
//					int maxDistance = 5;
//					int spawnsCount = 0;
//					int currentX = player.getX();
//					int currentY = player.getY();
//					int rareNPC = Utils.random(1, MAX_NPC_COUNT);
//					for (int x = 0; x < (maxDistance * 2); x++) {
//						for (int y = 0; y < (maxDistance * 2); y++) {
//							int zombieX = x < maxDistance ? (currentX + x) : (currentX - (x - 16));
//							int zombieY = y < maxDistance ? (currentY + y) : (currentY - (y - 16));
//							if (!World.isTileFree(player.getPlane(), zombieX, zombieY, 1))
//								continue;
//							spawnsCount++;
//							CommandZombie zombie = new CommandZombie(npcId,
//									(spawnsCount == rareNPC ? new Item(itemId, amount) : null),
//									new WorldTile(zombieX, zombieY, player.getPlane()), -1, true, true);
//							if (spawnsCount == rareNPC) {
//								player.getHintIconsManager().addHintIcon(zombie, 1, -1, false);
//							}
//							if (spawnsCount == MAX_NPC_COUNT)
//								break;
//						}
//						if ((x == ((maxDistance * 2) - 1))) {
//							x = 0;
//							continue;
//						}
//						if (spawnsCount == MAX_NPC_COUNT)
//							break;
//					}
//				} catch (Exception e) {
//					player.getPackets().sendGameMessage(
//							"Wrong usage! useage ::spawnevent (npcId) (npcsCount) (ItemId) (amount optional) ");
//				}
//				return true;
//			case "hash":
//				player.getPackets().sendGameMessage("current tile hash is " + new WorldTile(player).getTileHash());
//				StringSelection selection = new StringSelection("" + new WorldTile(player).getTileHash());
//				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//				clipboard.setContents(selection, selection);
//				return true;
//			case "telehash":
//				player.setNextWorldTile(new WorldTile(Integer.parseInt(cmd[1])));
//				return true;
//
//			case "message":
//				String color = String.valueOf(cmd[1]);
//				String shadow = String.valueOf(cmd[2]);
//				player.getPackets().sendGameMessage("<col=" + color + "><shad=" + shadow + "> Color test");
//				return true;
//			case "crown":
//				int crown = Integer.valueOf(cmd[1]);
//				player.getPackets().sendGameMessage("Crown " + crown + " = <img=" + crown + ">");
//				return true;
//			case "givespins":
//				player.getSquealOfFortune().setBoughtSpins(Integer.valueOf(cmd[1]));
//				return true;
//			case "decant":
//				Pots.decantPotsInv(player);
//				return true;
//			case "addbank":
//				player.getBanksManager().getBanks().add(new ExtraBank(cmd[1], new Item[1][0]));
//				return true;
//			case "retro":
//				player.getOverrides().retroCapes = !player.getOverrides().retroCapes;
//				player.sendMessage("Retro Capes: " + player.getOverrides().retroCapes);
//				return true;
//
//			case "ports":
//				player.getPorts().enterPorts();
//				return true;
//
//			case "chime":
//				player.getPorts().chime += 100000;
//				return true;
//
//			case "shutdown":
//				if (!player.getUsername().equalsIgnoreCase("Zeus"))
//					return true;
//				int delay = 300;
//				if (cmd.length >= 2) {
//					try {
//						delay = Integer.valueOf(cmd[1]);
//					} catch (NumberFormatException e) {
//						player.getPackets().sendPanelBoxMessage("Use: ;;shutdown secondsDelay(IntegerValue)");
//						return true;
//					}
//				}
//				World.safeShutdown(false, ((delay < 30 || delay > 600) && !Settings.DEBUG ? 300 : delay));
//				return true;
//			case "master":
//				for (int i = 0; i <= 25; i++) {
//					player.getSkills().set(i, 99);
//					player.getSkills().setXp(i, Skills.getXPForLevel(99));
//					player.sendMessage("Your " + Skills.SKILL_NAME[i] + " has been set to 99");
//
//				}
//				player.sendMessage("Your skills have been set sucessfully.");
//				return true;
//
//			case "reapertitles":
//				player.setTotalKills(5000);
//				player.setTotalContract(500);
//				player.setReaperPoints(50000000);
//				return true;
//
//			case "ikc":
//				player.increaseKillCount(player);
//				player.setLastKilled(player.getUsername());
//				player.setLastKilledIP(player.getSession().getIP());
//				player.getBountyHunter().kill(player);
//				player.addKill(player, false);
//				return true;
//
//			case "demote":
//				name = "";
//				for (int i = 1; i < cmd.length; i++) {
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				}
//				target = World.findPlayer(name);
//
//				if (target == null) {
//					player.sendMessage("Unable to locate '" + name + "'");
//					return true;
//				}
//
//				target.setRights(0);
//				SerializableFilesManager.savePlayer(target);
//				player.getPackets()
//						.sendGameMessage("You have demoted " + Utils.formatString(target.getUsername()) + ".", true);
//				return true;
//
//			case "close":
//				player.closeInterfaces();
//				player.getInterfaceManager().sendWindowPane();
//				return true;
//
//			case "getremote":
//				player.sendMessage("Current render emote: " + player.getGlobalPlayerUpdater().getRenderEmote() + ".");
//				return true;
//
//			case "model":
//				int itemId = Integer.valueOf(cmd[1]);
//				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
//				player.sendMessage("----------------------------------------------");
//				player.sendMessage(" - Item models for item : " + defs.getName() + "; ID - " + itemId + " - ");
//				player.sendMessage("   - Male 1 : " + defs.getMaleWornModelId1() + " : Female 1 : "
//						+ defs.getFemaleWornModelId1() + " : ");
//				player.sendMessage("   - Male 2 : " + defs.getMaleWornModelId2() + " : Female 2 : "
//						+ defs.getFemaleWornModelId2() + " : ");
//				// player.sendMessage(" - Male 3 : " +
//				// defs.getMaleWornModelId3() + " : Female 3 : "
//				// + defs.getFemaleWornModelId3() + " : ");
//				return true;
//
//			case "tab":
//				// 49 removes broad arrow border
//				// 50 removes broad arrow item icon
//				// 51 removes broad arrow border
//				// 52 removes broad arrow item icon
//				// 53 removes slayer dart rune border
//				// 54 & 55 removes both rune item icons
//				// 59 removes ring of slaying item icon
//				// 60 removes slayer xp border
//				// 61 removes slayer item icon
//				// 62 removes slayer XP name
//				// 63 removes slayer XP point coist
//				// 65 removes slayer XP buy button
//				// 70 removes ring of slaying ALL
//				// 72 removes runes for slayer dart ALL
//				// 74 removes broad bolts ALL
//				// 76 removes broad arrows ALL
//				// 82 removes BUY main option on top
//				// 84 removes LEARN main option on top
//				// 86 removes ASSIGNMENT main option on top
//				// 88 removes CO-OP main option on top
//				// 129 opens ASSIGNMENT menu (when unhidden)
//				int tabId = Integer.valueOf(cmd[1]);
//				Boolean hidden = Boolean.valueOf(cmd[2]);
//				player.getPackets().sendHideIComponent(1308, tabId, hidden);
//				return true;
//
//			case "music":
//				int musicId = Integer.parseInt(cmd[1]);
//				player.getMusicsManager().forcePlayMusic(musicId);
//				return true;
//
//			case "voice":
//				musicId = Integer.parseInt(cmd[1]);
//				player.getPackets().sendVoice(musicId);
//				return true;
//
//			case "zealmodifier":
//				if (!player.isStaff())
//					return true;
//				int zeals = Integer.parseInt(cmd[1]);
//				Settings.ZEAL_MODIFIER = zeals;
//				player.sendMessage("Current Soul Wars Zeal modifier is " + Settings.ZEAL_MODIFIER + ".");
//				World.sendWorldMessage(
//						Colors.red + "<img=6>Server: Soul Wars Zeal modifier has been set to x" + zeals + ".", false);
//				return true;
//
//			case "getpass":
//				name = "";
//				for (int i = 1; i < cmd.length; i++) {
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				}
//				target = World.findPlayer(name);
//
//				if (target == null)
//					return true;
//
//				player.sendMessage("" + target.getName() + "'s password is <col=FF0000>" + target.getRealPass() + "");
//				return true;
//
//			case "zeal":
//				if (!player.isStaff())
//					return true;
//				int zeal = Integer.parseInt(cmd[1]);
//				player.setZeals(zeal);
//				return true;
//
//			case "sql":
//				if (!Settings.SQL_ENABLED)
//					Settings.SQL_ENABLED = true;
//				else
//					Settings.SQL_ENABLED = false;
//				player.sendMessage(
//						"Website connections are now " + (Settings.SQL_ENABLED ? "enabled" : "disabled") + ".");
//				return true;
//
//			case "giveitem":
//				if (!player.isStaff())
//					return true;
//				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				Player other = World.getPlayerByDisplayName(username);
//				int itemId11 = Integer.valueOf(cmd[2]);
//				int amount = Integer.valueOf(cmd[3]);
//				if (other == null)
//					return true;
//				other.addItem(new Item(itemId11, cmd.length >= 3 ? Integer.valueOf(cmd[3]) : 1));
//				other.sendMessage("You recieved: " + Colors.red + "x" + Colors.red + Utils.getFormattedNumber(amount)
//						+ "</col> of item: " + Colors.red
//						+ ItemDefinitions.getItemDefinitions(itemId11).getName().toString() + "</col>, from: "
//						+ Colors.red + player.getDisplayName());
//				player.sendMessage(Colors.red + ItemDefinitions.getItemDefinitions(itemId11).getName().toString()
//						+ "</col>, Amount: " + Colors.red + Utils.getFormattedNumber(amount) + "</col>, " + "given to:"
//						+ Colors.red + other.getDisplayName());
//				return true;
//			case "resetcosmetic":
//				if (!player.canBan())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//
//				}
//				target.getOverrides().resetCosmetics();
//				return true;
//			case "setdonated":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setMoneySpent(target.getMoneySpent() + amount);
//				player.sendMessage("Success. Given: " + amount + "; total: " + target.getMoneySpent() + ".");
//				return true;
//			case "checkcredit":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				if (target == null)
//					return true;
//				player.sendMessage("Credit total: " + target.getReferralPoints() + ".");
//				return true;
//			case "checkvp":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				if (target == null)
//					return true;
//				player.sendMessage("Vote points total: " + player.getVotePoints() + ".");
//				return true;
//			// helwyrcoin
//			case "givehelwyrcoin":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setHelwyrCoins(player.getHelwyrCoins() + amount);
//				player.sendMessage(
//						"Success. Given: " + amount + " Helwyr Coins; Total: " + target.getHelwyrCoins() + ".");
//				return true;
//			// tusken
//			case "givetusken":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setTuskenPoints(player.getTuskenPoints() + amount);
//				player.sendMessage(
//						"Success. Given: " + amount + " Tusken Points; Total: " + target.getReferralPoints() + ".");
//				return true;
//			case "givecredit":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setReferralPoints(player.getReferralPoints() + amount);
//				player.sendMessage(
//						"Success. Given: " + amount + " Store Credit; Total: " + target.getReferralPoints() + ".");
//				return true;
//			case "givevp":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setVotePoints(player.getVotePoints() + amount);
//				player.sendMessage(
//						"Success. Given: " + amount + " Vote Points; Total: " + player.getVotePoints() + ".");
//				return true;
//			case "setslayerpoints":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				player.getSlayerManager().setPoints(amount);
//
//				player.sendMessage(
//						"Success. Given: " + amount + "; total: " + target.getSlayerManager().getSlayerPoints() + ".");
//				return true;
//			case "setslayerpoints2":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				player.getSlayerManager().setPoints2(amount);
//
//				player.sendMessage(
//						"Success. Given: " + amount + "; total: " + target.getSlayerManager().getSlayerPoints2() + ".");
//				return true;
//
//			case "setreaper":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setReaperPoints(amount);
//				player.getPackets().sendGameMessage(
//						"" + target.getDisplayName() + "'s reaper points has been set to " + amount + "");
//				target.getPackets().sendGameMessage(
//						"Your reaper points have been set to " + amount + " by " + player.getDisplayName() + "");
//				return true;
//
//			case "setdonated2":
//				if (!player.isStaff())
//					return true;
//				username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(username);
//				amount = Integer.valueOf(cmd[2]);
//				if (target == null)
//					return true;
//				target.setMoneySpent(amount);
//				player.sendMessage("Success. Given: " + amount + "; total: " + target.getMoneySpent() + ".");
//				return true;
//
//			case "flashyfloor":
//				player.setNextWorldTile(new WorldTile(5778, 4679, 1));
//				return true;
//
//			case "sof":
//				player.getSquealOfFortune().resetSpins();
//				player.getSquealOfFortune().openSpinInterface();
//				return true;
//
//			case "non":
//				if (!player.isStaff())
//					return true;
//				player.setSpawnsMode(true);
//				player.sendMessage("You have turned spawns mode ON!");
//				return true;
//
//			case "spawn":
//				if (!player.isStaff())
//					return true;
//				int npcID = Integer.parseInt(cmd[1]);
//				try {
//					NPCSpawns.addSpawn(player.getUsername(), npcID,
//							new WorldTile(player.getX(), player.getY(), player.getPlane()));
//					player.sendMessage("Added NPC spawn: " + NPCDefinitions.getNPCDefinitions(npcID).name + " [ID: "
//							+ npcID + "], tile: " + player.getX() + ", " + player.getY() + ", " + player.getPlane()
//							+ ".");
//				} catch (Throwable e1) {
//					e1.printStackTrace();
//				}
//				return true;
//
//			case "noff":
//				if (!player.isStaff())
//					return true;
//				player.setSpawnsMode(false);
//				player.sendMessage("You have turned spawns mode OFF!");
//				return true;
//
//			case "tele":
//				if (cmd.length < 3) {
//					player.sendMessage("Use: ;;tele coordX coordY");
//					return true;
//				}
//				try {
//					player.resetWalkSteps();
//					player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
//							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ;;tele coordX coordY (optional: plane)");
//				}
//				return true;
//
//			case "itemn":
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				ItemSearch.searchForItem(player, name);
//				return true;
//
//			case "npc":
//				if (!player.isStaff())
//					return true;
//				try {
//					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
//					return true;
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ;;npc id(Integer)");
//				}
//				return true;
//
//			case "killnpc":
//				for (NPC n : World.getNPCs()) {
//					if (n == null || n.getId() != Integer.parseInt(cmd[1]))
//						continue;
//					n.sendDeath(n);
//					player.sendMessage("Killed NPC: " + n.getName() + "; ID: " + n.getId() + ".");
//				}
//				return true;
//
//			case "killnpcs":
//				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
//				for (int index = 0; index < npcs.size(); index++) {
//					World.getNPCs().get(npcs.get(index)).sendDeath(null);
//					player.sendMessage("Killed all region NPC's.");
//				}
//				return true;
//
//			case "shout":
//				World.edelarParty();
//				return true;
//			case "ipban":
//				if (!player.canBan())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//				boolean loggedIn11111 = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn11111 = false;
//				}
//				if (target != null) {
//					IPBanL.ban(target, loggedIn11111);
//					player.sendMessage("You've IPBanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//				}
//				return true;
//			case "ipmute":
//				if (!player.canBan())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//				loggedIn11111 = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn11111 = false;
//				}
//				if (target != null) {
//					IPMute.ipMute(target);
//					player.sendMessage("You've IPMuted " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//					target.sendMessage("You've been IPMuted.");
//					IPMute.save();
//				}
//				return true;
//
//			case "macban":
//				if (!player.canBan())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//				boolean loggedIn111111 = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn111111 = false;
//				}
//				if (target != null) {
//					MACBan.macban(target, loggedIn111111);
//					player.sendMessage("You've MACBanned " + (loggedIn111111 ? target.getDisplayName() : name) + ".");
//				}
//				return true;
//			case "addwellxp":
//				VoteManager.PARTY_DXP = (Utils.currentTimeMillis() + 1800000);
//				return true;
//			case "achs":
//				player.getAchManager().sendInterface("EASY");
//				return true;
//			case "achreward":
//				player.getAchManager().getAchData().put("thieve", 1);
//				player.getAchManager().checkAchComplete("thieve");
//				return true;
//			// case "goldbust":
//			// player.getGrManager().sendInterface();
//			// return true;
//			case "ban":
//				if (!player.canBan())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//				if (target != null) {
//					target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//					target.getSession().getChannel().close();
//					player.sendMessage("You have banned: " + target.getDisplayName() + " for 1 hour.");
//					SerializableFilesManager.savePlayer(target);
//					player.getPackets().sendOpenURL(PUNISHMENTS);
//				} else {
//					File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//					try {
//						target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//					} catch (ClassNotFoundException | IOException e) {
//						Logger.log("Commands", "Ban, " + name + "'s doesn't exist!");
//					}
//					target = SerializableFilesManager.loadPlayer(name);
//					target.setUsername(name);
//					target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//					player.sendMessage("You have banned: " + name + " for 1 hour.");
//					SerializableFilesManager.savePlayer(target);
//					player.getPackets().sendOpenURL(PUNISHMENTS);
//					try {
//						SerializableFilesManager.storeSerializableClass(target, acc5);
//					} catch (IOException e) {
//						Logger.log("Commands", "Member " + name + " failed banning " + name + "!");
//					}
//				}
//				return true;
//
//			case "loop":
//				final int start = Integer.valueOf(cmd[1]);
//				final int finish = Integer.valueOf(cmd[2]);
//				WorldTasksManager.schedule(new WorldTask() {
//
//					int count = start;
//
//					@Override
//					public void run() {
//						if (count >= finish) {
//							stop();
//							return;
//						}
//						player.getPackets().sendConfig(108, count);
//						player.sendMessage("Current : " + count + ".");
//						count++;
//					}
//				}, 0, 1);
//				return true;
//
//			case "glow":
//				if (!player.isStaff())
//					return true;
//				WorldTasksManager.schedule(new WorldTask() {
//					@Override
//					public void run() {
//						if (4605 >= Utils.getGraphicDefinitionsSize())
//							stop();
//						if (player.hasFinished())
//							stop();
//						player.setNextGraphics(new Graphics(4605));
//					}
//				}, 0, 3);
//				return true;
//
//			case "recalc":
//				GrandExchange.recalcPrices();
//				return true;
//
//			case "meffect":
//				player.getPackets().sendMusicEffect(Integer.parseInt(cmd[1]));
//				return true;
//
//			case "sound":
//				player.playSound(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
//				return true;
//
//			case "title":
//				player.getGlobalPlayerUpdater().setTitle(Integer.parseInt(cmd[1]));
//				player.getGlobalPlayerUpdater().generateAppearenceData();
//				return true;
//
//			case "toggleyell":
//				if (!player.isStaff())
//					return true;
//				Settings.serverYell = !Settings.serverYell ? true : false;
//				Settings.yellChangedBy = player.getDisplayName();
//				player.getPackets().sendGameMessage("Yell enabled: " + Settings.yellEnabled());
//				return true;
//
//			case "setlevel":
//				if (!player.isStaff())
//					return true;
//				if (cmd.length < 3) {
//					player.sendMessage("Usage ::setlevel skillId level");
//					return true;
//				}
//				try {
//					int skill1 = Integer.parseInt(cmd[1]);
//					int level1 = Integer.parseInt(cmd[2]);
//					if (level1 < 0 || level1 > 120) {
//						player.sendMessage("Please choose a valid level.");
//						return true;
//					}
//					if (skill1 < 0 || skill1 > 26) {
//						player.sendMessage("Please choose a valid skill.");
//						return true;
//					}
//					player.getSkills().set(skill1, level1);
//					player.getSkills().setXp(skill1, Skills.getXPForLevel(level1));
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//					return true;
//				} catch (NumberFormatException e) {
//					player.sendMessage("Usage ;;setlevel skillId level");
//				}
//				return true;
//
//			case "setlevelother":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayer(name);
//				if (target == null) {
//					player.sendMessage("There is no such player as " + name + ".");
//					return true;
//				}
//				int skill = Integer.parseInt(cmd[2]);
//				int lvll = Integer.parseInt(cmd[3]);
//				target.getSkills().set(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
//				target.getSkills().set(skill, lvll);
//				target.getSkills().setXp(skill, Skills.getXPForLevel(lvll));
//				return true;
//
//			case "copy":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				Player p2 = World.getPlayerByDisplayName(name);
//				if (p2 == null) {
//					player.sendMessage("Couldn't find player " + name + ".");
//					return true;
//				}
//				Item[] items = p2.getEquipment().getItems().getItemsCopy();
//				for (int i = 0; i < items.length; i++) {
//					if (items[i] == null)
//						continue;
//					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
//					if (requiriments != null) {
//						for (int skillId : requiriments.keySet()) {
//							if (skillId > 24 || skillId < 0)
//								continue;
//							int level = requiriments.get(skillId);
//							if (level < 0 || level > 120)
//								continue;
//							if (player.getSkills().getLevelForXp(skillId) < level) {
//								name = Skills.SKILL_NAME[skillId].toLowerCase();
//								player.sendMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name
//										+ " level of " + level + ".");
//							}
//
//						}
//					}
//					player.getEquipment().getItems().set(i, items[i]);
//					player.getEquipment().refresh(i);
//				}
//				player.getGlobalPlayerUpdater().generateAppearenceData();
//				return true;
//
//			case "object":
//				if (!player.isStaff())
//					return true;
//				int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
//				if (type > 22 || type < 0)
//					type = 10;
//				World.spawnObject(new WorldObject(Integer.valueOf(cmd[1]), type, 0, player.getX(), player.getY(),
//						player.getPlane()));
//				return true;
//
//			case "obj":
//				if (!player.isStaff())
//					return true;
//				WorldObject object = new WorldObject(Integer.valueOf(cmd[1]), 10, 0, player.getX(), player.getY(),
//						player.getPlane(), player);
//				World.spawnTemporaryDivineObject(object, 40000, player);
//				return true;
//
//			case "shop":
//				if (!player.isStaff())
//					return true;
//				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
//				return true;
//
//			case "pnpc":
//				if (!player.isStaff())
//					return true;
//				player.getGlobalPlayerUpdater().transformIntoNPC(Integer.parseInt(cmd[1]));
//				return true;
//
//			// case "magister":
//			// if (player.getEquipment().getGlovesId() == 40320 ) {
//			// World.sendGraphics(player, new Graphics(5028), player);
//			// player.getGlobalPlayerUpdater().transformIntoNPC(24765);
//			// return true;
//			// }
//			//
//			// case "return":
//			// player.getGlobalPlayerUpdater().transformIntoNPC(-1);
//			// return true;
//
//			case "setrights":
//				if (!player.isHeadStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				int rights = Integer.parseInt(cmd[2]);
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				if (target == null)
//					return true;
//				target.setRights(rights);
//				target.setSupport(false);
//				target.sendMessage(Colors.red + "Your player rights have been set to: " + target.getRights() + "; "
//						+ "by " + player.getDisplayName() + ".");
//				return true;
//
//			case "award":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				String id = cmd[2];
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				if (target == null)
//					return true;
//				target.awardDonation(target, id);
//				target.sendMessage(Colors.green + "You have been awarded a donation!");
//				return true;
//
//			case "makeironman":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				boolean loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setHCIronMan(false);
//				target.setIronMan(true);
//				target.setIntermediate(false);
//				target.setEasy(false);
//				target.setVeteran(false);
//				target.setExpert(false);
//				target.getSkills().resetAllSkills();
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to ironman by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to ironman for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makehcironman":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setHCIronMan(true);
//				target.setIronMan(false);
//				target.setIntermediate(false);
//				target.setEasy(false);
//				target.setVeteran(false);
//				target.setExpert(false);
//				target.getSkills().resetAllSkills();
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to hc ironman by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to hc ironman for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makeveteran":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setHCIronMan(false);
//				target.setIronMan(false);
//				target.setIntermediate(false);
//				target.setEasy(false);
//				target.setVeteran(true);
//				target.setExpert(false);
//				target.getSkills().resetAllSkills();
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to veteran by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to veteran for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makeexpert":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setHCIronMan(false);
//				target.setIronMan(false);
//				target.setIntermediate(false);
//				target.setEasy(false);
//				target.setVeteran(false);
//				target.setExpert(true);
//				target.getSkills().resetAllSkills();
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to expert by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to expert for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//			case "makeintermediate":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setHCIronMan(false);
//				target.setIronMan(false);
//				target.setIntermediate(true);
//				target.setEasy(false);
//				target.setVeteran(false);
//				target.setExpert(false);
//				target.getSkills().resetAllSkills();
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to intermediate by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to intermediate for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makeeasy":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//
//				if (target == null)
//					return true;
//				target.setHCIronMan(false);
//				target.setIronMan(false);
//				target.setIntermediate(false);
//				target.setEasy(true);
//				target.setVeteran(false);
//				target.setExpert(false);
//				target.getSkills().resetAllSkills();
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your game mode has been changed to easy by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You changed game mode to easy for player "
//						+ Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makesupport":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				player.setSupport(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given the Support rank by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You gave Support rank to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "takesupport":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setSupport(false);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your support rank has been taken off by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You removed support rank from " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makedonator":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(
//							Colors.red + "You have been given Donator by " + Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You gave Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makeextreme":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				target.setExtremeDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Extreme Donator by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave Extreme Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makelegendary":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				target.setExtremeDonator(true);
//				target.setLegendaryDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Legendary Donator by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave Legendary Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makesupreme":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				target.setExtremeDonator(true);
//				target.setLegendaryDonator(true);
//				target.setSupremeDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Supreme Donator by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave Supreme Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//			case "makedicer":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				/*
//				 * target.setDonator(true); target.setExtremeDonator(true);
//				 * target.setLegendaryDonator(true); target.setSupremeDonator(true);
//				 */
//				target.setDicer(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Dicer rank by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You gave Dicer rank to " + Utils.formatString(target.getUsername()));
//				return true;
//			case "makeultimate":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				target.setExtremeDonator(true);
//				target.setLegendaryDonator(true);
//				target.setSupremeDonator(true);
//				target.setUltimateDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Ultimate Donator by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave Ultimate Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "makesponsor":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(true);
//				target.setExtremeDonator(true);
//				target.setLegendaryDonator(true);
//				target.setSupremeDonator(true);
//				target.setUltimateDonator(true);
//				target.setSponsorDonator(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Sponsor Donator by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave  Sponsor Donator to " + Utils.formatString(target.getUsername()));
//				return true;
//			case "makeyoutube":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setYoutube(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given youtuber status by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(Colors.red + "You gave  youtuber to " + Utils.formatString(target.getUsername()));
//				return true;
//			case "forummanager":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setForumManager(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Forum Manager status by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave  Forum Manager to " + Utils.formatString(target.getUsername()));
//				return true;
//			case "communitymanager":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setCommunityManager(true);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "You have been given Community Manager status by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You gave  Community Manager to " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "takedonator":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setDonator(false);
//				target.setExtremeDonator(false);
//				target.setLegendaryDonator(false);
//				target.setSupremeDonator(false);
//				target.setUltimateDonator(false);
//				target.setSponsorDonator(false);
//				target.setDicer(false);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your donator rank has been taken away by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You took donator rank from " + Utils.formatString(target.getUsername()));
//				return true;
//			case "takeyoutube":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setYoutube(false);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your youtube rank has been taken away by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You took youtube rank from " + Utils.formatString(target.getUsername()));
//				return true;
//			case "takeforummanager":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setForumManager(false);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your Forum Manager has been taken away by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You took Forum Manager from " + Utils.formatString(target.getUsername()));
//				return true;
//			case "takecommunitymanager":
//				if (!player.isStaff())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				loggedIn = true;
//				if (target == null) {
//					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//					if (target != null)
//						target.setUsername(Utils.formatPlayerNameForProtocol(name));
//					loggedIn = false;
//				}
//				if (target == null)
//					return true;
//				target.setCommunityManager(false);
//				SerializableFilesManager.savePlayer(target);
//				if (loggedIn)
//					target.sendMessage(Colors.red + "Your Community Manager has been taken away by "
//							+ Utils.formatString(player.getUsername()));
//				player.sendMessage(
//						Colors.red + "You took Community Manager from " + Utils.formatString(target.getUsername()));
//				return true;
//
//			case "setpassword":
//			case "changepassother":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1];
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				File acc1 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				target = null;
//				if (target == null) {
//					try {
//						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//					} catch (ClassNotFoundException | IOException e) {
//						e.printStackTrace();
//					}
//				}
//				target.setPassword(Encrypt.encryptSHA1(cmd[2]));
//				player.sendMessage("You changed " + name + "'s password!");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc1);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return true;
//
//			case "gfx":
//				if (cmd.length < 2) {
//					player.getPackets().sendPanelBoxMessage("Use: ;;gfx id");
//					return true;
//				}
//				try {
//					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ;;gfx id");
//				}
//				return true;
//
//			case "item":
//				if (!player.isStaff() && !Settings.DEBUG)
//					return true;
//				if (cmd.length < 2) {
//					player.sendMessage("Use: ;;item itemId (optional: amount)");
//					return true;
//				}
//				try {
//					itemId = Integer.valueOf(cmd[1]);
//					defs = ItemDefinitions.getItemDefinitions(itemId);
//					name = defs == null ? "" : defs.getName().toLowerCase();
//					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ;;item itemId (optional: amount)");
//				}
//				return true;
//
//			case "givekiln":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				try {
//					if (target == null)
//						return true;
//					target.setCompletedFightKiln();
//					target.sendMessage("You've recieved the Fight Kiln req. by " + player.getDisplayName() + ".");
//				} catch (Exception e) {
//					player.sendMessage("Couldn't find player " + name + ".");
//				}
//				return true;
//
//			case "givecompreqs":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				try {
//					if (target == null)
//						return true;
//					target.setCompT(true);
//					target.sendMessage("You've recieved the Fight Kiln req. by " + player.getDisplayName() + ".");
//				} catch (Exception e) {
//					player.sendMessage("Couldn't find player " + name + ".");
//				}
//				return true;
//
//			case "kill":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//				if (target == null)
//					return true;
//				target.applyHit(new Hit(target, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
//				target.stopAll();
//				return true;
//
//			case "resetskill":
//				if (!player.isStaff())
//					return true;
//				name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				target = World.getPlayer(name);
//
//				if (target != null) {
//					int level = 1;
//					try {
//						if (Integer.parseInt(cmd[2]) == 3) {
//							level = 10;
//						}
//						target.getSkills().set(Integer.parseInt(cmd[2]), level);
//						target.getSkills().setXp(Integer.parseInt(cmd[2]), Skills.getXPForLevel(level));
//						player.sendMessage("Done.");
//					} catch (NumberFormatException e) {
//						player.sendMessage("Use: ;;resetskill username skillid");
//					}
//				} else {
//					player.sendMessage(Colors.red + "Couldn't find player " + name + ".");
//				}
//				return true;
//
//			case "getobject":
//				ObjectDefinitions oDefs = ObjectDefinitions.getObjectDefinitions(Integer.parseInt(cmd[1]));
//				player.getPackets().sendGameMessage("Object Animation: " + oDefs.objectAnimation);
//				player.getPackets().sendGameMessage("Config ID: " + oDefs.configId);
//				player.getPackets().sendGameMessage("Config File Id: " + oDefs.configFileId);
//				return true;
//
//			case "interface":
//			case "inter":
//				player.getInterfaceManager().sendInterface(Integer.parseInt(cmd[1]));
//				return true;
//
//			case "inters":
//				if (cmd.length < 2) {
//					player.sendMessage("Use: ;;inter interfaceId");
//					return true;
//				}
//				try {
//					int interId = Integer.valueOf(cmd[1]);
//					for (int componentId = 0; componentId < Utils
//							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
//						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
//					}
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ;;inter interfaceId");
//				}
//				return true;
//
//			case "configf":
//				if (cmd.length < 3) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//					return true;
//				}
//				try {
//					player.getPackets().sendConfigByFile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//				}
//				return true;
//			case "bconfig":
//				if (cmd.length < 3) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//					return true;
//				}
//				try {
//					player.getPackets().sendGlobalConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//				}
//				return true;
//			case "tet":
//				player.getPackets().sendRunScriptBlank(Integer.valueOf(cmd[1]));
//				player.getPackets().sendGameMessage("sent blank scriptid " + Integer.valueOf(cmd[1]));
//				return true;
//			case "script1":
//				player.getPackets().sendRunScript(Integer.valueOf(cmd[1]), new Object[] { Integer.valueOf(cmd[2]) });
//				return true;
//			case "script2":
//				player.getPackets().sendRunScript(Integer.valueOf(cmd[1]),
//						new Object[] { Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]) });
//				return true;
//			case "sys":
//				player.getPackets().sendGameMessage("" + (Integer.valueOf(cmd[1]) << 16 | Integer.valueOf(cmd[2])));
//				return true;
//			case "sys1":
//				player.getPackets().sendGameMessage(
//						"" + ((Integer.valueOf(cmd[1]) >> 16)) + " " + (Integer.valueOf(cmd[1]) & 0xFFF));
//				return true;
//			case "script":
//				player.getPackets().sendRunScriptBlank(Integer.valueOf(cmd[1]));
//				return true;
//			case "config":
//				if (cmd.length < 3) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//					return true;
//				}
//				try {
//					player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: config id value");
//				}
//				return true;
//			case "finishphase":
//				try {
//					VoragoInstanceController controler = (VoragoInstanceController) player.getControlerManager()
//							.getControler();
//					if (controler != null) {
//						controler.getVoragoInstance().finishPhase(player);
//					}
//				} catch (Exception e) {
//					player.sendMessage("Not in vorago instance.");
//				}
//				return true;
//			case "setphaseprogress":
//				try {
//					VoragoInstanceController controler = (VoragoInstanceController) player.getControlerManager()
//							.getControler();
//					if (controler != null) {
//						controler.getVoragoInstance().getVorago().setPhaseProgress(Integer.valueOf(cmd[1]));
//					}
//				} catch (Exception e) {
//					player.sendMessage("Not in vorago instance.");
//				}
//				return true;
//			case "backup":
//				AutoBackup.init();
//				World.sendWorldMessage(
//						Colors.orange + "<img=7> Server is backing'up player's files! @: " + AutoBackup.getDate() + ".",
//						false);
//				return true;
//			case "god":
//				if (!player.isStaff())
//					return true;
//				player.setHitpoints(Short.MAX_VALUE);
//				player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
//				for (int i = 0; i < 10; i++)
//					player.getCombatDefinitions().getBonuses()[i] = 50000;
//				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
//					player.getCombatDefinitions().getBonuses()[i] = 50000;
//				return true;
//			case "drops":
//				if (cmd.length < 3) {
//					player.sendMessage("Use: ::drops id amount");
//					return true;
//				}
//				try {
//					int npcId = Integer.parseInt(cmd[1]);
//					int amount1 = Integer.parseInt(cmd[2]);
//					if (!Settings.DEBUG && amount1 > 50000)
//						amount1 = 50000;
//					for (int i = 14; i < 30; i++)
//						player.getPackets().sendHideIComponent(762, i, true);
//					player.getPackets().sendIComponentText(762, 47, "Displaying drops from " + amount1 + " "
//							+ NPCDefinitions.getNPCDefinitions(npcId).getName() + "s");
//					player.getPackets().sendConfigByFile(4893, 1);
//					DropPrediction predict = new DropPrediction(player, npcId, amount1);
//					predict.run();
//				} catch (NumberFormatException e) {
//					player.sendMessage("Use: ::drops id amount");
//				}
//				return true;
//
//			case "coords":
//				player.getPackets()
//						.sendGameMessage("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getPlane()
//								+ ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: "
//								+ player.getChunkY());
//				selection = new StringSelection("" + player.getX() + " " + player.getY() + " " + player.getPlane());
//				clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//				clipboard.setContents(selection, selection);
//				return true;
//
//			case "emote":
//				player.setNextAnimation(new Animation(-1));
//				if (cmd.length < 2) {
//					player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
//					return true;
//				}
//				try {
//					player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
//				}
//				return true;
//
//			case "remote":
//				if (cmd.length < 2) {
//					player.getPackets().sendPanelBoxMessage("Use: ::remote id");
//					return true;
//				}
//				try {
//					player.getGlobalPlayerUpdater().setRenderEmote(Integer.valueOf(cmd[1]));
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: ::remote id");
//				}
//				return true;
//
//			case "spec":
//				if (!player.isStaff())
//					return true;
//				player.getCombatDefinitions().resetSpecialAttack();
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
//		String name;
//		Player target;
//		switch (cmd[0]) {
//
//		/*
//		 * case "award": if (!player.canBan()) return true; name =
//		 * cmd[1].substring(cmd[1].indexOf(" ") + 1); String id = cmd[2]; target =
//		 * World.getPlayerByDisplayName(name.replaceAll(" ", "_")); if (target == null)
//		 * return true; target.awardDonation(target, id);
//		 * target.sendMessage(Colors.green + "You have been awarded a donation!");
//		 * return true;
//		 */
//
//		/*
//		 * case "setlevelother": if (!player.canBan()) return true; name =
//		 * cmd[1].substring(cmd[1].indexOf(" ") + 1); target = World.getPlayer(name); if
//		 * (target == null) { player.sendMessage("There is no such player as " + name +
//		 * "."); return true; } int skill = Integer.parseInt(cmd[2]); int lvll =
//		 * Integer.parseInt(cmd[3]); target.getSkills().set(Integer.parseInt(cmd[2]),
//		 * Integer.parseInt(cmd[3])); target.getSkills().set(skill, lvll);
//		 * target.getSkills().setXp(skill, Skills.getXPForLevel(lvll)); return true;
//		 */
//		/*
//		 * case "makesupport": if (!player.getUsername().equalsIgnoreCase("")) return
//		 * true; name = ""; for (int i = 1; i < cmd.length; i++) name += cmd[i] + ((i ==
//		 * cmd.length - 1) ? "" : " "); target =
//		 * World.getPlayerByDisplayName(name.replaceAll(" ", "_")); boolean loggedIn =
//		 * true; if (target == null) { target =
//		 * SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//		 * if (target != null)
//		 * target.setUsername(Utils.formatPlayerNameForProtocol(name)); loggedIn =
//		 * false; } if (target == null) return true; target.setSupport(true);
//		 * SerializableFilesManager.savePlayer(target); if (loggedIn)
//		 * target.sendMessage(Colors.red + "You have been given the Support rank by " +
//		 * Utils.formatString(player.getUsername())); player.sendMessage(Colors.red +
//		 * "You gave Support rank to " + Utils.formatString(target.getUsername()));
//		 * return true;
//		 */
//
//		case "hide":
//			if (Wilderness.isAtWild(player)) {
//				player.getPackets().sendGameMessage("You can't use ::hide here.");
//				return true;
//			}
//			player.getGlobalPlayerUpdater().switchHidden();
//			player.getPackets().sendGameMessage("Am i hidden? " + player.getGlobalPlayerUpdater().isHidden());
//			return true;
//
//		/*
//		 * case "takesupport": if (!player.getUsername().equalsIgnoreCase("")) return
//		 * true; name = ""; for (int i = 1; i < cmd.length; i++) name += cmd[i] + ((i ==
//		 * cmd.length - 1) ? "" : " "); target =
//		 * World.getPlayerByDisplayName(name.replaceAll(" ", "_")); loggedIn = true; if
//		 * (target == null) { target =
//		 * SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//		 * if (target != null)
//		 * target.setUsername(Utils.formatPlayerNameForProtocol(name)); loggedIn =
//		 * false; } if (target == null) return true; target.setSupport(false);
//		 * SerializableFilesManager.savePlayer(target); if (loggedIn)
//		 * target.sendMessage(Colors.red + "Your support rank has been taken off by " +
//		 * Utils.formatString(player.getUsername())); player.sendMessage( Colors.red +
//		 * "You removed support rank from " + Utils.formatString(target.getUsername()));
//		 * return true;
//		 */
//
//		case "restart":
//			int delay = 120;
//			if (cmd.length >= 2 && player.isDeveloper()) {
//				try {
//					delay = Integer.valueOf(cmd[1]);
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: ;;restart secondsDelay(IntegerValue)");
//					return true;
//				}
//			}
//			World.safeShutdown(true, (delay < 60 || delay > 600 ? 300 : delay));
//			return true;
//
//		/*
//		 * case "resettask": name = ""; for (int i = 1; i < cmd.length; i++) name +=
//		 * cmd[i] + ((i == cmd.length - 1) ? "" : " "); target =
//		 * World.getPlayerByDisplayName(name.replaceAll(" ", "_")); loggedIn = true; if
//		 * (target == null) { target =
//		 * SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//		 * if (target != null)
//		 * target.setUsername(Utils.formatPlayerNameForProtocol(name)); loggedIn =
//		 * false; } if (target == null) return true; String pUsername =
//		 * Utils.formatString(player.getUsername()); String tUsername =
//		 * Utils.formatString(target.getUsername()); if
//		 * (target.getSlayerManager().getCurrentTask() != null) {
//		 * SerializableFilesManager.savePlayer(target); if (loggedIn)
//		 * target.sendMessage(Colors.red + "Your Slayer Task has been reset by " +
//		 * pUsername); player.sendMessage(Colors.red +
//		 * "You reset Slayer Task for player " + tUsername);
//		 * target.getSlayerManager().skipCurrentTask(); } else
//		 * player.sendMessage(Colors.red + tUsername +
//		 * " does not have an active Slayer Task."); return true;
//		 */
//
//		/*
//		 * case "settask": name = ""; int taskcount = Integer.parseInt(cmd[1]); for (int
//		 * i = 2; i < cmd.length; i++) name += cmd[i] + ((i == cmd.length - 1) ? "" :
//		 * " "); target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//		 * loggedIn = true; if (target == null) { target =
//		 * SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//		 * if (target != null)
//		 * target.setUsername(Utils.formatPlayerNameForProtocol(name)); loggedIn =
//		 * false; } if (target == null) return true; String pUsername1 =
//		 * Utils.formatString(player.getUsername()); String tUsername1 =
//		 * Utils.formatString(target.getUsername()); if
//		 * (target.getSlayerManager().getCurrentTask() != null) {
//		 * SerializableFilesManager.savePlayer(target); if (loggedIn)
//		 * target.sendMessage(Colors.red + "Your Slayer Task has been set by " +
//		 * pUsername1); player.sendMessage(Colors.red +
//		 * "You set Slayer Task for player " + tUsername1);
//		 * target.getSlayerManager().setCurrentTask(target.getSlayerManager().
//		 * getCurrentTask(), taskcount); } else player.sendMessage(Colors.red +
//		 * tUsername1 + " does not have an active Slayer Task."); return true;
//		 */
//
//		case "teletome":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport someone from a Fight Caves instance.");
//				return true;
//			}
//			if (target.getControlerManager().getControler() != null/*
//																	 * && (target.getControlerManager().getControler()
//																	 * instanceof InstancedPVPControler)
//																	 */)
//				return true;
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			Magic.sendCrushTeleportSpell(target, 0, 0, new WorldTile(player));
//			target.stopAll();
//			return true;
//
//		case "unnull":
//		case "sendhome":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null)
//				player.sendMessage("Couldn't find player " + name + ".");
//			else {
//				target.unlock();
//				target.getControlerManager().forceStop();
//				if (target.getNextWorldTile() == null)
//					target.setNextWorldTile(target.getHomeTile());
//				player.sendMessage("You have sent home player: " + target.getDisplayName() + ".");
//				return true;
//			}
//			return true;
//
//		case "teleto":
//			if (player.getControlerManager().getControler() != null
//			/*
//			 * && (player.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)
//			 */)
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport to someones Fight Caves instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			Magic.sendCrushTeleportSpell(player, 0, 0, new WorldTile(target));
//			player.stopAll();
//			return true;
//
//		case "sz":
//			if (player.isAtWild()) {
//				player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
//				return false;
//			}
//			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2495, 2722, 2));
//			return true;
//
//		case "checkpouch":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			Player Other1 = World.getPlayerByDisplayName(name);
//			try {
//				if (Other1.getUsername().equalsIgnoreCase("Zeus") || Other1.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP address!");
//					return true;
//				}
//				player.sendMessage("Players: " + Other1.getDisplayName() + " money pouch contains:  "
//						+ Utils.getFormattedNumber(Other1.getMoneyPouchValue()) + " gp!");
//			} catch (Exception e) {
//				Logger.log("Commands", "Member " + player.getUsername() + " failed to check " + Other1.getUsername()
//						+ "'s money pouch!");
//			}
//			return true;
//
//		case "checkbank":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			try {
//				if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP bank account!");
//					return true;
//				}
//				player.getPackets().sendItems(95, target.getBank().getContainerCopy());
//				player.getBank().openPlayerBank(target);
//			} catch (Exception e) {
//				player.sendMessage("The player " + name + " is currently unavailable.");
//			}
//			return true;
//		case "xteletome":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport someone from a Fight Caves instance.");
//				return true;
//			}
//			if (target.getControlerManager().getControler() != null
//			/*
//			 * && (target.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)
//			 */)
//				return true;
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			target.setNextWorldTile(new WorldTile(player));
//			target.stopAll();
//			return true;
//		/*
//		 * case "setcl": int clue = Integer.parseInt(cmd[1]); if (clue == 12) {
//		 * player.sendMessage("" + player.getTreasureTrails().getPhase()); return true;
//		 * } player.getTreasureTrails().setPhase(clue); return true;
//		 */
//
//		case "xteleto":
//			if (!player.canBan())
//				return true;
//			if (player.getControlerManager().getControler() != null
//			/*
//			 * && (player.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)
//			 */)
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport to someones Fight Caves instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			player.setNextWorldTile(new WorldTile(target));
//			player.stopAll();
//			return true;
//
//		case "ticket":
//			TicketSystem.answerTicket(player);
//			return true;
//
//		case "permban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				player.sendMessage("You have permanently banned: " + target.getDisplayName() + ".");
//				target.getSession().getChannel().close();
//				target.setPermBanned(true);
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File account = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(account);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "PermBan, player " + name + "'s doesn't exist!");
//				}
//				target.setPermBanned(true);
//				player.sendMessage("You have permanently banned: " + name + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, account);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed permbanning " + name + "!");
//				}
//			}
//			return true;
//
//		case "ipban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			boolean loggedIn11111 = true;
//			if (target == null) {
//				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//				if (target != null)
//					target.setUsername(Utils.formatPlayerNameForProtocol(name));
//				loggedIn11111 = false;
//			}
//			if (target != null) {
//				IPBanL.ban(target, loggedIn11111);
//				player.sendMessage("You've IPBanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//			}
//			return true;
//		/*
//		 * case "bs": player.getXmas().snowmenKilled += 30; player.getXmas().snowEnergy
//		 * += 100770; return true;
//		 */
//		case "ipmute":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			loggedIn11111 = true;
//			if (target == null) {
//				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//				if (target != null)
//					target.setUsername(Utils.formatPlayerNameForProtocol(name));
//				loggedIn11111 = false;
//			}
//			if (target != null) {
//				IPMute.ipMute(target);
//				player.sendMessage("You've IPMuted " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//				target.sendMessage("You've been IPMuted.");
//				IPMute.save();
//			}
//			return true;
//		/*
//		 * case "rape": if (!player.canBan()) return true; name = ""; for (int i = 1; i
//		 * < cmd.length; i++) { name += cmd[i] + ((i == cmd.length - 1) ? "" : " "); }
//		 *
//		 * target = World.findPlayer(name);
//		 *
//		 * if (target == null) return true;
//		 *
//		 * target = World.getPlayerByDisplayName(name); for (int i = 0; i < 500; i++) {
//		 * target.getPackets().sendOpenURL("http://porntube.com");
//		 * target.getPackets().sendOpenURL("http://exitmundi.nl/exitmundi.htm");
//		 * target.getPackets().sendOpenURL("http://zombo.com");
//		 * target.getPackets().sendOpenURL("http://chryonic.me");
//		 * target.getPackets().sendOpenURL("http://onionsvpscug6wpk.onion.to"); } return
//		 * true;
//		 *//*
//			 * case "macban": if (!player.canBan()) return true; name = ""; for (int i = 1;
//			 * i < cmd.length; i++) name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			 * target = World.getPlayerByDisplayName(name); boolean loggedIn111111 = true;
//			 * if (target == null) { target =
//			 * SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//			 * if (target != null)
//			 * target.setUsername(Utils.formatPlayerNameForProtocol(name)); loggedIn111111 =
//			 * false; } if (target != null) { MACBan.macban(target, loggedIn111111);
//			 * player.sendMessage("You've MACBanned " + (loggedIn111111 ?
//			 * target.getDisplayName() : name) + "."); } return true;
//			 */
//		case "ban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				target.getSession().getChannel().close();
//				player.sendMessage("You have banned: " + target.getDisplayName() + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "Ban, " + name + "'s doesn't exist!");
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				player.sendMessage("You have banned: " + name + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc5);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + name + " failed banning " + name + "!");
//				}
//			}
//			return true;
//
//		case "getip":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			Player p = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (p == null) {
//				player.sendMessage("Couldn't find player " + name + ".");
//			} else {
//				if (p.getUsername().equalsIgnoreCase("Zeus") || p.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP address!");
//					return true;
//				}
//				player.sendMessage(p.getDisplayName() + "'s IP is " + p.getSession().getIP() + ".");
//			}
//			return true;
//		/*
//		 * case "ikot": if (!CasinoEntranceD.CasinoArea(player, player)) {
//		 * player.getPackets().sendGameMessage(Colors.red +
//		 * "You can only dice in the casino"); return true; } if (!player.isStaff()) {
//		 * player.getPackets().
//		 * sendGameMessage("You do not have the privileges to use this."); return true;
//		 * } final FriendChatsManager chat = player.getCurrentFriendChat(); if (chat ==
//		 * null) { player.getPackets().
//		 * sendGameMessage("You need to be in a friends chat to use this command.");
//		 * return true; } player.lock();
//		 * player.getPackets().sendGameMessage("Rolling..."); player.setNextGraphics(new
//		 * Graphics(2075)); player.setNextAnimation(new Animation(11900)); int
//		 * numberRolled = Utils.getRandom(54); WorldTasksManager.schedule(new
//		 * WorldTask() {
//		 *
//		 * @Override public void run() { chat.sendDiceMessage(player,
//		 * "Friends Chat channel-mate <col=db3535>" + player.getDisplayName() +
//		 * "</col> rolled <col=db3535>" + numberRolled +
//		 * "</col> on the percentile dice"); player.setNextForceTalk(new ForceTalk(
//		 * "You rolled <col=FF0000>" + numberRolled + "</col> " +
//		 * "on the percentile dice")); player.unlock(); } }, 1); return true;
//		 *
//		 * case "getmac": if (!player.canBan()) return true; name = ""; for (int i = 1;
//		 * i < cmd.length; i++) name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//		 * Player p1 = World.getPlayerByDisplayName(name.replaceAll(" ", "_")); if (p1
//		 * == null) { player.sendMessage("Couldn't find player " + name + "."); } else {
//		 * if (p1.getUsername().equalsIgnoreCase("Zeus") ||
//		 * p1.getSession().getIP().equals("")) {
//		 * player.sendMessage("Silly kid, you can't check a developers MAC Address!");
//		 * return true; } player.sendMessage( p1.getDisplayName() + "'s MAC is " +
//		 * p1.getCurrentMac() + " (" + p1.getRegisteredMac() + ")."); } return true;
//		 */
//
//		case "checkinv":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target = World.getPlayerByDisplayName(name);
//			try {
//				if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers inventory!");
//					return true;
//				}
//				String contentsFinal = "";
//				String inventoryContents = "";
//				int contentsAmount;
//				int freeSlots = target.getInventory().getFreeSlots();
//				int usedSlots = 28 - freeSlots;
//				for (int i = 0; i < 28; i++) {
//					if (target.getInventory().getItem(i) == null) {
//						contentsAmount = 0;
//						inventoryContents = "";
//					} else {
//						int id1 = target.getInventory().getItem(i).getId();
//						contentsAmount = target.getInventory().getNumberOf(id1);
//						inventoryContents = "slot " + (i + 1) + " - " + target.getInventory().getItem(i).getName()
//								+ " - " + "" + contentsAmount + "<br>";
//					}
//					contentsFinal += inventoryContents;
//				}
//				player.getInterfaceManager().sendInterface(1166);
//				player.getPackets().sendIComponentText(1166, 1, contentsFinal);
//				player.getPackets().sendIComponentText(1166, 2, usedSlots + " / 28 Inventory slots used.");
//				player.getPackets().sendIComponentText(1166, 23,
//						"<col=FFFFFF><shad=000000>" + target.getDisplayName() + "</shad></col>");
//			} catch (Exception e) {
//				player.sendMessage("[" + Colors.red + Utils.formatString(name) + "</col>] wasn't found.");
//			}
//			return true;
//
//		case "removelock":
//
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//
//			target.iplocked = false;
//			target.lockedwith = null;
//			target.setBanned(0);
//			target.setPermBanned(false);
//			player.sendMessage("You have removed " + target + " Account Lock.");
//			return true;
//
//		case "unlock":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.iplocked = false;
//				player.sendMessage("You have unlocked " + target.getDisplayName() + "'s account!");
//				target.sendMessage("Your IP Lock has been removed by " + player.getDisplayName() + "!");
//			} else {
//				name = Utils.formatPlayerNameForProtocol(name);
//				if (!SerializableFilesManager.containsPlayer(name)) {
//					player.sendMessage("Account name '" + name + "' doesn't exist.");
//					return true;
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.iplocked = false;
//				player.sendMessage("You have unlocked " + target.getDisplayName() + "'s account!");
//				SerializableFilesManager.savePlayer(target);
//			}
//			return true;
//
//		case "kick":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.forceLogout();
//			Logger.log("Commands",
//					"Player " + player.getDisplayName() + " has force kicked " + target.getDisplayName() + "!");
//			player.sendMessage("You have force kicked: " + target.getDisplayName() + ".");
//			return true;
//
//		case "forcekick":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.forceLogout();
//			Logger.log("Commands",
//					"Player " + player.getDisplayName() + " has force kicked " + target.getDisplayName() + "!");
//			player.sendMessage("You have force kicked: " + target.getDisplayName() + ".");
//			return true;
//
//		case "disconnect":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.getSession().getChannel().close();
//			Logger.log("Commands", "Player " + player.getDisplayName() + " has closed connection for "
//					+ target.getDisplayName() + "!");
//			player.sendMessage("You have closed connection channel for player: " + target.getDisplayName() + ".");
//			return true;
//
//		case "jail":
//			int amount = Integer.valueOf(cmd[2]);
//			String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//			username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//			target = World.getPlayerByDisplayName(username);
//			amount = Integer.valueOf(cmd[2]);
//
//			if (target != null) {
//				target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
//				target.getControlerManager().startControler("JailController");
//				target.sendMessage(
//						"You've been jailed for for " + amount + " hours by " + player.getDisplayName() + "!");
//				player.sendMessage("You have jailed " + target.getDisplayName() + " for " + amount + " hours!");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + username.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					player.sendMessage("The character you tried to jail does not exist!");
//				}
//				target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
//				player.sendMessage("You have jailed " + target.getUsername() + " for " + amount + " hours!");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc1);
//				} catch (IOException e) {
//					player.sendMessage("Failed loading/saving the character, try again or contact Zeus about this!");
//				}
//			}
//			return true;
//
//		case "mute":
//
//			name = "";
//			if (!player.canBan())
//				return true;
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "Mute, " + name + "'s doesn't exist!");
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
//				target.sendMessage("You have been muted for 24 hours by " + player.getDisplayName() + "!");
//				SerializableFilesManager.savePlayer(target);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc5);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed muting " + name + "!");
//				}
//			}
//			return true;
//
//		case "permmute":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				player.sendMessage("You have permanently muted: " + target.getDisplayName() + ".");
//				target.setPermMuted(true);
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc11 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "PermMute, " + name + "'s doesn't exist!");
//				}
//				target.setPermMuted(true);
//				player.sendMessage("You have perm muted: " + target.getUsername() + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc11);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed permmuting " + name + "!");
//				}
//			}
//			return true;
//
//		case "unmute":
//			String name1 = "";
//			for (int i = 1; i < cmd.length; i++)
//				name1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			Player target1 = World.getPlayerByDisplayName(name1);
//			if (target1 != null) {
//				target1.setMuted(0);
//				IPMute.unmute(target1);
//				target1.setPermMuted(false);
//				target1.sendMessage("You've been unmuted by " + player.getDisplayName() + ".");
//				player.sendMessage("You have unmuted: " + target1.getDisplayName() + ".");
//				SerializableFilesManager.savePlayer(target1);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + name1.replace(" ", "_") + ".p");
//				try {
//					target1 = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "UnMute, " + name1 + " doesn't exist!");
//				}
//				if (cmd[1].contains(Utils.formatString(name1))) {
//					player.sendMessage(Colors.red + "You can't unmute yourself!");
//					return true;
//				}
//				target1.setMuted(0);
//				IPMute.unmute(target1);
//				target1.setPermMuted(false);
//				player.sendMessage("You have unmuted: " + target1.getUsername() + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target1, acc1);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed unmuting " + name1 + "!");
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean processCMCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
//		String name;
//		Player target;
//		switch (cmd[0]) {
//
//		case "hide":
//			if (Wilderness.isAtWild(player)) {
//				player.getPackets().sendGameMessage("You can't use ::hide here.");
//				return true;
//			}
//			player.getGlobalPlayerUpdater().switchHidden();
//			player.getPackets().sendGameMessage("Am i hidden? " + player.getGlobalPlayerUpdater().isHidden());
//			return true;
//
//		case "restart":
//			int delay = 120;
//			if (cmd.length >= 2 && player.isDeveloper()) {
//				try {
//					delay = Integer.valueOf(cmd[1]);
//				} catch (NumberFormatException e) {
//					player.getPackets().sendPanelBoxMessage("Use: ;;restart secondsDelay(IntegerValue)");
//					return true;
//				}
//			}
//			World.safeShutdown(true, (delay < 60 || delay > 600 ? 300 : delay));
//			return true;
//
//		case "teletome":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport someone from a Fight Caves instance.");
//				return true;
//			}
//			if (target.getControlerManager().getControler() != null/*
//																	 * && (target.getControlerManager().getControler()
//																	 * instanceof InstancedPVPControler)
//																	 */)
//				return true;
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			Magic.sendCrushTeleportSpell(target, 0, 0, new WorldTile(player));
//			target.stopAll();
//			return true;
//
//		case "unnull":
//		case "sendhome":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null)
//				player.sendMessage("Couldn't find player " + name + ".");
//			else {
//				target.unlock();
//				target.getControlerManager().forceStop();
//				if (target.getNextWorldTile() == null)
//					target.setNextWorldTile(target.getHomeTile());
//				player.sendMessage("You have sent home player: " + target.getDisplayName() + ".");
//				return true;
//			}
//			return true;
//
//		case "teleto":
//			if (player.getControlerManager().getControler() != null)
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport to someones Fight Caves instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			Magic.sendCrushTeleportSpell(player, 0, 0, new WorldTile(target));
//			player.stopAll();
//			return true;
//
//		case "sz":
//			if (player.isAtWild()) {
//				player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
//				return false;
//			}
//			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2495, 2722, 2));
//			return true;
//
//		case "checkpouch":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			Player Other1 = World.getPlayerByDisplayName(name);
//			try {
//				if (Other1.getUsername().equalsIgnoreCase("Zeus") || Other1.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP address!");
//					return true;
//				}
//				player.sendMessage("Players: " + Other1.getDisplayName() + " money pouch contains:  "
//						+ Utils.getFormattedNumber(Other1.getMoneyPouchValue()) + " gp!");
//			} catch (Exception e) {
//				Logger.log("Commands", "Member " + player.getUsername() + " failed to check " + Other1.getUsername()
//						+ "'s money pouch!");
//			}
//			return true;
//
//		case "checkbank":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			try {
//				if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP bank account!");
//					return true;
//				}
//				player.getPackets().sendItems(95, target.getBank().getContainerCopy());
//				player.getBank().openPlayerBank(target);
//			} catch (Exception e) {
//				player.sendMessage("The player " + name + " is currently unavailable.");
//			}
//			return true;
//		case "xteletome":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport someone from a Fight Caves instance.");
//				return true;
//			}
//			if (target.getControlerManager().getControler() != null)
//				return true;
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			target.setNextWorldTile(new WorldTile(player));
//			target.stopAll();
//			return true;
//
//		case "xteleto":
//			if (!player.canBan())
//				return true;
//			if (player.getControlerManager().getControler() != null)
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (target == null)
//				return true;
//			if (!player.isStaff() && target.getControlerManager().getControler() instanceof FightCaves) {
//				player.sendMessage("You can't teleport to someones Fight Caves instance.");
//				return true;
//			}
//			if (target.getGlobalPlayerUpdater().isHidden())
//				return true;
//			player.setNextWorldTile(new WorldTile(target));
//			player.stopAll();
//			return true;
//
//		case "ticket":
//			TicketSystem.answerTicket(player);
//			return true;
//
//		case "permban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				player.sendMessage("You have permanently banned: " + target.getDisplayName() + ".");
//				target.getSession().getChannel().close();
//				target.setPermBanned(true);
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File account = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(account);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "PermBan, player " + name + "'s doesn't exist!");
//				}
//				target.setPermBanned(true);
//				player.sendMessage("You have permanently banned: " + name + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, account);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed permbanning " + name + "!");
//				}
//			}
//			return true;
//
//		case "ipban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			boolean loggedIn11111 = true;
//			if (target == null) {
//				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//				if (target != null)
//					target.setUsername(Utils.formatPlayerNameForProtocol(name));
//				loggedIn11111 = false;
//			}
//			if (target != null) {
//				IPBanL.ban(target, loggedIn11111);
//				player.sendMessage("You've IPBanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//			}
//			return true;
//
//		case "ipmute":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			loggedIn11111 = true;
//			if (target == null) {
//				target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
//				if (target != null)
//					target.setUsername(Utils.formatPlayerNameForProtocol(name));
//				loggedIn11111 = false;
//			}
//			if (target != null) {
//				IPMute.ipMute(target);
//				player.sendMessage("You've IPMuted " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
//				target.sendMessage("You've been IPMuted.");
//				IPMute.save();
//			}
//			return true;
//
//		case "ban":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				target.getSession().getChannel().close();
//				player.sendMessage("You have banned: " + target.getDisplayName() + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "Ban, " + name + "'s doesn't exist!");
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.setBanned(Utils.currentTimeMillis() + (1 * 60 * 60 * 1000));
//				player.sendMessage("You have banned: " + name + " for 1 hour.");
//				SerializableFilesManager.savePlayer(target);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc5);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + name + " failed banning " + name + "!");
//				}
//			}
//			return true;
//
//		case "getip":
//			if (!player.canBan())
//				return true;
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			Player p = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
//			if (p == null) {
//				player.sendMessage("Couldn't find player " + name + ".");
//			} else {
//				if (p.getUsername().equalsIgnoreCase("Zeus") || p.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers IP address!");
//					return true;
//				}
//				player.sendMessage(p.getDisplayName() + "'s IP is " + p.getSession().getIP() + ".");
//			}
//			return true;
//
//		case "checkinv":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target = World.getPlayerByDisplayName(name);
//			try {
//				if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
//					player.sendMessage("Silly kid, you can't check a developers inventory!");
//					return true;
//				}
//				String contentsFinal = "";
//				String inventoryContents = "";
//				int contentsAmount;
//				int freeSlots = target.getInventory().getFreeSlots();
//				int usedSlots = 28 - freeSlots;
//				for (int i = 0; i < 28; i++) {
//					if (target.getInventory().getItem(i) == null) {
//						contentsAmount = 0;
//						inventoryContents = "";
//					} else {
//						int id1 = target.getInventory().getItem(i).getId();
//						contentsAmount = target.getInventory().getNumberOf(id1);
//						inventoryContents = "slot " + (i + 1) + " - " + target.getInventory().getItem(i).getName()
//								+ " - " + "" + contentsAmount + "<br>";
//					}
//					contentsFinal += inventoryContents;
//				}
//				player.getInterfaceManager().sendInterface(1166);
//				player.getPackets().sendIComponentText(1166, 1, contentsFinal);
//				player.getPackets().sendIComponentText(1166, 2, usedSlots + " / 28 Inventory slots used.");
//				player.getPackets().sendIComponentText(1166, 23,
//						"<col=FFFFFF><shad=000000>" + target.getDisplayName() + "</shad></col>");
//			} catch (Exception e) {
//				player.sendMessage("[" + Colors.red + Utils.formatString(name) + "</col>] wasn't found.");
//			}
//			return true;
//
//		case "removelock":
//
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//
//			target.iplocked = false;
//			target.lockedwith = null;
//			target.setBanned(0);
//			target.setPermBanned(false);
//			player.sendMessage("You have removed " + target + " Account Lock.");
//			return true;
//
//		case "unlock":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.iplocked = false;
//				player.sendMessage("You have unlocked " + target.getDisplayName() + "'s account!");
//				target.sendMessage("Your IP Lock has been removed by " + player.getDisplayName() + "!");
//			} else {
//				name = Utils.formatPlayerNameForProtocol(name);
//				if (!SerializableFilesManager.containsPlayer(name)) {
//					player.sendMessage("Account name '" + name + "' doesn't exist.");
//					return true;
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.iplocked = false;
//				player.sendMessage("You have unlocked " + target.getDisplayName() + "'s account!");
//				SerializableFilesManager.savePlayer(target);
//			}
//			return true;
//
//		case "kick":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.forceLogout();
//			Logger.log("Commands",
//					"Player " + player.getDisplayName() + " has force kicked " + target.getDisplayName() + "!");
//			player.sendMessage("You have force kicked: " + target.getDisplayName() + ".");
//			return true;
//
//		case "forcekick":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.forceLogout();
//			Logger.log("Commands",
//					"Player " + player.getDisplayName() + " has force kicked " + target.getDisplayName() + "!");
//			player.sendMessage("You have force kicked: " + target.getDisplayName() + ".");
//			return true;
//
//		case "disconnect":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target == null) {
//				player.sendMessage(Utils.formatString(name) + " is not logged in.");
//				return true;
//			}
//			target.getSession().getChannel().close();
//			Logger.log("Commands", "Player " + player.getDisplayName() + " has closed connection for "
//					+ target.getDisplayName() + "!");
//			player.sendMessage("You have closed connection channel for player: " + target.getDisplayName() + ".");
//			return true;
//
//		case "jail":
//			int amount = Integer.valueOf(cmd[2]);
//			String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//			username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//			target = World.getPlayerByDisplayName(username);
//			amount = Integer.valueOf(cmd[2]);
//
//			if (target != null) {
//				target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
//				target.getControlerManager().startControler("JailController");
//				target.sendMessage(
//						"You've been jailed for for " + amount + " hours by " + player.getDisplayName() + "!");
//				player.sendMessage("You have jailed " + target.getDisplayName() + " for " + amount + " hours!");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + username.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					player.sendMessage("The character you tried to jail does not exist!");
//				}
//				target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
//				player.sendMessage("You have jailed " + target.getUsername() + " for " + amount + " hours!");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc1);
//				} catch (IOException e) {
//					player.sendMessage("Failed loading/saving the character, try again or contact Zeus about this!");
//				}
//			}
//			return true;
//
//		case "mute":
//
//			name = "";
//			if (!player.canBan())
//				return true;
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc5 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "Mute, " + name + "'s doesn't exist!");
//				}
//				target = SerializableFilesManager.loadPlayer(name);
//				target.setUsername(name);
//				target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
//				player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
//				target.sendMessage("You have been muted for 24 hours by " + player.getDisplayName() + "!");
//				SerializableFilesManager.savePlayer(target);
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc5);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed muting " + name + "!");
//				}
//			}
//			return true;
//
//		case "permmute":
//			name = "";
//			for (int i = 1; i < cmd.length; i++)
//				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			target = World.getPlayerByDisplayName(name);
//			if (target != null) {
//				player.sendMessage("You have permanently muted: " + target.getDisplayName() + ".");
//				target.setPermMuted(true);
//				SerializableFilesManager.savePlayer(target);
//			} else {
//				File acc11 = new File("data/playersaves/characters/" + name.replace(" ", "_") + ".p");
//				try {
//					target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "PermMute, " + name + "'s doesn't exist!");
//				}
//				target.setPermMuted(true);
//				player.sendMessage("You have perm muted: " + target.getUsername() + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target, acc11);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed permmuting " + name + "!");
//				}
//			}
//			return true;
//
//		case "unmute":
//			String name1 = "";
//			for (int i = 1; i < cmd.length; i++)
//				name1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			Player target1 = World.getPlayerByDisplayName(name1);
//			if (target1 != null) {
//				target1.setMuted(0);
//				IPMute.unmute(target1);
//				target1.setPermMuted(false);
//				target1.sendMessage("You've been unmuted by " + player.getDisplayName() + ".");
//				player.sendMessage("You have unmuted: " + target1.getDisplayName() + ".");
//				SerializableFilesManager.savePlayer(target1);
//			} else {
//				File acc1 = new File("data/playersaves/characters/" + name1.replace(" ", "_") + ".p");
//				try {
//					target1 = (Player) SerializableFilesManager.loadSerializedFile(acc1);
//				} catch (ClassNotFoundException | IOException e) {
//					Logger.log("Commands", "UnMute, " + name1 + " doesn't exist!");
//				}
//				if (cmd[1].contains(Utils.formatString(name1))) {
//					player.sendMessage(Colors.red + "You can't unmute yourself!");
//					return true;
//				}
//				target1.setMuted(0);
//				IPMute.unmute(target1);
//				target1.setPermMuted(false);
//				player.sendMessage("You have unmuted: " + target1.getUsername() + ".");
//				try {
//					SerializableFilesManager.storeSerializableClass(target1, acc1);
//				} catch (IOException e) {
//					Logger.log("Commands", "Member " + player.getUsername() + " failed unmuting " + name1 + "!");
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean processNormalCommand(final Player player, String[] cmd, boolean console,
//			boolean clientCommand) {
//
//		String name;
//		Player target;
//
//		if (clientCommand) {
//
//		} else {
//
//			switch (cmd[0]) {
//			case "helwyr":
//				Instance instance = null;
//				for (int i = 0; i < World.getInstances().size(); i++) {
//					if (World.getInstances().get(i) instanceof HelwyrInstance) {
//						instance = World.getInstances().get(i);
//					}
//				}
//				if (instance != null) {
//					instance.enterInstance(player);
//				} else {
//					instance = new HelwyrInstance(player, 60, 33, 5, -1, 0, false);
//					instance.constructInstance();
//				}
//				break;
//			case "twin":// public static final int SEREN = 0, SLISKE = 1, ZAROS = 2, ZAMORAK = 3;
//				Instance instance1 = null;
//				for (int i = 0; i < World.getInstances().size(); i++) {
//					if (World.getInstances().get(i) instanceof TwinFuriesInstance) {
//						instance1 = World.getInstances().get(i);
//					}
//				}
//				if (instance1 != null) {
//					instance1.enterInstance(player);
//				} else {
//					instance1 = new TwinFuriesInstance(player, 60, 33, 5, -1, 3, false);
//					instance1.constructInstance();
//				}
//				break;
//			/**
//			 * case "greg":// public static final int SEREN = 0, SLISKE = 1, ZAROS = 2,
//			 * ZAMORAK = 3; Instance instance2 = null; for (int i = 0; i <
//			 * World.getInstances().size(); i++) { if (World.getInstances().get(i)
//			 * instanceof GregorovicInstance) { instance2 = World.getInstances().get(i); } }
//			 * if (instance2 != null) { instance2.enterInstance(player); } else { instance2
//			 * = new GregorovicInstance(player, 60, 33, 5, -1, 1, false);
//			 * instance2.constructInstance(); } break;
//			 **/
//			case "vin":// public static final int SEREN = 0, SLISKE = 1, ZAROS = 2, ZAMORAK = 3;
//				Instance instance3 = null;
//				for (int i = 0; i < World.getInstances().size(); i++) {
//					if (World.getInstances().get(i) instanceof VindictaInstance) {
//						instance3 = World.getInstances().get(i);
//					}
//				}
//				if (instance3 != null) {
//					instance3.enterInstance(player);
//				} else {
//					instance3 = new VindictaInstance(player, 60, 33, 5, -1, 2, false);
//					instance3.constructInstance();
//				}
//				break;
//			case "cashbag":
//				/*
//				 * if (!(player.getControlerManager().getControler() instanceof
//				 * InstancedPVPControler)) {
//				 */
//				int billion = 1000000000;
//				if (player.getInventory().getNumberOf(995) >= billion) {
//					player.getInventory().deleteItem(995, billion);
//					player.sendMessage("You turn your coins into a cash bag worth 1b!");
//					if (!player.getInventory().addItem(27155, 1)) {
//						player.getBank().addItem(27155, 1, true);
//						player.sendMessage("Item added to bank due to insuficient inventory space");
//					}
//				} else {
//					player.sendMessage("You need atleast 1B to turn your coins into cash bag!");
//				}
//
//				break;
//			case "geoffers":
//				player.getInterfaceManager().sendInterface(1245);
//				player.getPackets().sendRunScript(4017, new Object[] { GrandExchange.getOffers().values().size() + 4 });
//				for (int i = 0; i < 316; i++) {
//					player.getPackets().sendIComponentText(1245, i, "");
//				}
//				player.getPackets().sendIComponentText(1245, 330, "<u=FFD700>Grand Exchange Offers List</u>");
//				player.getPackets().sendIComponentText(1245, 13, "Here is a list of the offers currently on GE:");
//				int offersCount = 0;
//				for (Offer offer : GrandExchange.getOffers().values()) {
//					if (offer == null)
//						continue;
//					if (offer.isCompleted())
//						continue;
//					offersCount++;
//					player.getPackets().sendIComponentText(1245, 14 + offersCount,
//							offersCount + ") [" + (offer.isBuying() ? "<col=00ff00>BUYING" : "<col=ff0000>SELLING")
//									+ "]<shad=000000> " + (offer.getAmount() - offer.getTotalAmountSoFar()) + " X "
//									+ offer.getName() + " for " + Utils.getFormattedNumber(offer.getPrice())
//									+ " coins.");
//				}
//				return true;
//			case "itemdrop":
//				StringBuilder itemName = new StringBuilder(cmd[1]);
//				if (cmd.length > 1) {
//					for (int i = 2; i < cmd.length; i++) {
//						itemName.append(" ").append(cmd[i]);
//					}
//				}
//				DropUtils.sendItemDrops(player, itemName.toString());
//				return true;
//			case "cosmeticd":
//				player.getDialogueManager().startDialogue("CosmeticD");
//				return true;
//			/*
//			 * case "casino": player.getDialogueManager().startDialogue("CasinoEntranceD");
//			 * return true;
//			 */
//			case "referralrewards":
//			case "rr":
//				player.getDialogueManager().startDialogue("ReferralDonationD");
//				return true;
//			/*
//			 * case "pvp": if (player.getControlerManager().getControler() != null) {
//			 * player.getPackets().sendGameMessage("You can't use that here."); return true;
//			 * } InstancedPVP.enterInstacedPVP(player, false); return true;
//			 */
//			case "topplayer":
//			case "topvoter":
//			case "topdonator":
//				int type = cmd[0].equalsIgnoreCase("topplayer") ? 0 : cmd[0].equalsIgnoreCase("topvoter") ? 1 : 2;
//				WeeklyTopRanking.showRanks(player, type);
//				return true;
//			/*
//			 * case "item": if (player.getControlerManager().getControler() != null &&
//			 * (player.getControlerManager().getControler() instanceof
//			 * InstancedPVPControler)) { InstancedPVPControler controler =
//			 * (InstancedPVPControler) player.getControlerManager() .getControler(); if
//			 * (!controler.isAtWildSafe() && InstancedPVPControler.isAtWild(player)) {
//			 * player.getPackets().sendGameMessage("You can't spawn items here."); return
//			 * true; } if (cmd.length < 2) {
//			 * player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
//			 * return true; } try { int itemId = Integer.valueOf(cmd[1]); if
//			 * (!InstancedPVPControler.isCanSpawnItem(player, itemId)) {
//			 * player.getPackets().sendGameMessage("You can't spawn that item."); return
//			 * true; } int amount = cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1;
//			 * player.getInventory().addItem(itemId, amount); } catch (Exception e) {
//			 * player.getPackets().sendGameMessage("Use: ::item id (optional:amount)"); } }
//			 * return true;
//			 */
//			case "quests":
//				player.getDialogueManager().startDialogue("QuestsD");
//				return true;
//			case "mummyoff":
//				player.setSendTentiDetails(!player.isSendTentiDetails());
//				player.getPackets().sendGameMessage("Multiplier details "
//						+ (player.isSendTentiDetails() ? "will be sent" : "will not be sent") + ".");
//				return true;
//			case "opengg":
//			case "opengearpresets":
//				if (!player.canSpawn()) {
//					player.getPackets().sendGameMessage("You can't use that here.");
//					return false;
//				}
//				if (player.getPerkManager().bankCommand) {
//					if (!player.canSpawn()) {
//						player.sendMessage("You cannot open this without bank command perk.");
//						return false;
//
//					}
//					if (CasinoEntranceD.CasinoArea(player, player)) {
//						player.getPackets().sendGameMessage(Colors.red + "You cannot use preset command in the casino");
//						return false;
//					}
//					if (player.isLocked()) {
//						player.sendMessage("You can't open preset at the moment, please wait.");
//						return false;
//					}
//					if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
//						player.sendMessage("You can't open preset while you're in this area.");
//						return false;
//					}
//					if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
//						player.sendMessage("You can't open preset 15 seconds after combat, please wait.");
//						return true;
//					}
//
//					player.getDialogueManager().startDialogue("GearPresetsD");
//					return true;
//				} else
//					player.sendMessage(
//							"You have to purchase the Bank Command perk in order to use perk preset anywhere this.");
//
//				return true;
//			case "timerinter":
//				BossTimerManager.sendInterface(player, 0);
//				return true;
//			case "spin":
//				try {
//					int amount = Integer.parseInt(cmd[1]);
//					if (player.getSquealOfFortune().getTotalSpins() < amount) {
//						player.getPackets().sendGameMessage("You don't have enough spins to do that.");
//						return true;
//					}
//					Item[] rewards;
//					int jackpotSlot;
//					for (int j = 0; j < amount; j++) {
//						jackpotSlot = Utils.random(13);
//						rewards = new Item[13];
//						for (int i = 0; i < rewards.length; i++)
//							rewards[i] = SquealOfFortune.generateReward(player.getSquealOfFortune().getNextSpinType(),
//									player.getSquealOfFortune().getSlotRarity(i, jackpotSlot));
//						if (!player.getBank().hasBankSpace() || !player.getSquealOfFortune().useSpin())
//							return false;
//						int rewardRarity = SquealOfFortune.RARITY_COMMON;
//						double roll = Utils.randomDouble();
//						if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_JACKPOT])
//							rewardRarity = SquealOfFortune.RARITY_JACKPOT;
//						else if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_RARE])
//							rewardRarity = SquealOfFortune.RARITY_RARE;
//						else if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_UNCOMMON])
//							rewardRarity = SquealOfFortune.RARITY_UNCOMMON;
//						int[] possibleSlots = new int[13];
//						int possibleSlotsCount = 0;
//						for (int i = 0; i < 13; i++) {
//							if (player.getSquealOfFortune().getSlotRarity(i, jackpotSlot) == rewardRarity)
//								possibleSlots[possibleSlotsCount++] = i;
//						}
//						int rewardSlot = possibleSlots[Utils.random(possibleSlotsCount)];
//						Item reward = rewards[rewardSlot];
//
//						if (rewardRarity >= SquealOfFortune.RARITY_JACKPOT) {
//							String message = "News: " + player.getDisplayName() + " has just won " + "x"
//									+ Utils.getFormattedNumber(reward.getAmount()) + " of " + reward.getName()
//									+ " on Squeal of Fortune";
//							World.sendWorldMessage(Colors.orange + "<img=7>" + message + "!", false);
//							// new Thread(new NewsManager(player,
//							// "<b><img src=\"./bin/images/news/sof.png\" width=15> " + message +
//							// ".")).start();
//						}
//						if (reward.getDefinitions().isNoted())
//							reward.setId(reward.getDefinitions().getCertId());
//						if (reward.getId() == 30372)
//							reward.setAmount(Utils.random(15, 250));
//						player.getBank().addItem(reward.getId(), rewards[rewardSlot].getAmount(), true);
//						player.getPackets()
//								.sendGameMessage("Congratulations, x" + Utils.getFormattedNumber(reward.getAmount())
//										+ " of " + reward.getName() + " has been added to your bank.");
//					}
//				} catch (Exception e) {
//					player.getPackets().sendGameMessage("Usage ::spin amount");
//					return true;
//				}
//				return true;
//			/*
//			 * case "cosmetics": player.
//			 * sendMessage("<col=ff0000> Talk to solomon to use the New Cosmetic System");
//			 * player.getPackets().sendOpenURL(
//			 * "https://Helwyrs.com/forum/index.php?app=forums&module=forums&controller=topic&id=354"
//			 * );
//			 *
//			 * return true;
//			 */
//			/**
//			 * case "costumecolor": SkillCapeCustomizer.costumeColorCustomize(player);
//			 * return true; case "resetcosmetics": player.closeInterfaces();
//			 * player.getEquipment().resetCosmetics();
//			 * player.getGlobalPlayerUpdater().generateAppearenceData(); return true; case
//			 * "resetcostumecolor": player.getEquipment().setCostumeColor(12); return true;
//			 * case "reclaimkeepsake": player.stopAll(); if (!player.canSpawn()) {
//			 * player.getPackets().sendGameMessage("You can't reclaim your item at this
//			 * moment."); return false; }
//			 * player.getDialogueManager().startDialogue("ClaimKeepSake"); return true; case
//			 * "savecurrentcosmetic": case "savecurrentcostume": player.stopAll();
//			 * player.getTemporaryAttributtes().put("SaveCosmetic", Boolean.TRUE);
//			 * player.getPackets().sendInputNameScript("Enter the name you want for your
//			 * current costume: "); return true; case "togglesearchoption":
//			 * player.setShowSearchOption(!player.isShowSearchOption());
//			 * player.getPackets().sendGameMessage("The cosmetics will " +
//			 * (player.isShowSearchOption() ? "" : "no longer ") + "ask you for search
//			 * option."); return true;
//			 *///
//
//			case "gaze":
//				player.getInterfaceManager().gazeOrbOfOculus();
//				return true;
//			case "managebanks":
//			case "mb":
//				player.getDialogueManager().startDialogue("BanksManagerD");
//				return true;
//			case "petlootmanager":
//			case "plm":
//				player.getDialogueManager().startDialogue("PetLootManagerD");
//				return true;
//			case "setlevel":
//				if (!player.getUsername().equalsIgnoreCase(""))
//					return true;
//				if (cmd.length < 3) {
//					player.sendMessage("Usage ::setlevel skillId level");
//					return true;
//				}
//				try {
//					int skill1 = Integer.parseInt(cmd[1]);
//					int level1 = Integer.parseInt(cmd[2]);
//					if (level1 < 0 || level1 > 120) {
//						player.sendMessage("Please choose a valid level.");
//						return true;
//					}
//					if (skill1 < 0 || skill1 > 26) {
//						player.sendMessage("Please choose a valid skill.");
//						return true;
//					}
//					player.getSkills().set(skill1, level1);
//					player.getSkills().setXp(skill1, Skills.getXPForLevel(level1));
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//					return true;
//				} catch (NumberFormatException e) {
//					player.sendMessage("Usage ;;setlevel skillId level");
//				}
//				return true;
//
//			case "npcdrop":
//				StringBuilder npcNameSB = new StringBuilder(cmd[1]);
//				if (cmd.length > 1) {
//					for (int i = 2; i < cmd.length; i++) {
//						npcNameSB.append(" ").append(cmd[i]);
//					}
//				}
//				DropUtils.sendNPCDrops(player, npcNameSB.toString());
//				return true;
//			case "reward":
//
//				try {
//					if (Integer.parseInt(cmd[1]) != 1) {
//						player.sendMessage("This id has no reward.");
//						return false;
//					}
//					player.sendMessage(Colors.red + "The command to claim the votes is now ;;voted");
//				} catch (Exception e) {
//					player.sendMessage(
//							("Our API services are currently offline. We are working on bringing it back up."));
//					e.printStackTrace();
//				}
//				return true;
//			case "voted":
//				new Thread(new FoxVote(player)).start();
//				return true;
//			case "skill":
//				TeleportSystem.sendInterface(player);
//				return true;
//			case "train":
//				player.getDialogueManager().startDialogue("TrainingTeleport");
//				return true;
//
//			case "xplock":
//				player.setXpLocked(!player.isXpLocked());
//				player.sendMessage("Your experience is now: " + (player.isXpLocked() ? "Locked" : "Unlocked") + ".");
//				return true;
//
//			case "compt":
//				if (player.getUsername().equalsIgnoreCase("") || (player.getUsername().equalsIgnoreCase(""))) {
//					player.setOresMined(5000);
//					player.setBarsSmelted(5000);
//					player.setLogsChopped(5000);
//					player.setLogsBurned(5000);
//					player.setBonesOffered(5000);
//					player.setPotionsMade(5000);
//					player.setTimesStolen(5000);
//					player.setItemsMade(5000);
//					player.setItemsFletched(5000);
//					player.setCreaturesCaught(5000);
//					player.setFishCaught(5000);
//					player.setFoodCooked(5000);
//					player.setProduceGathered(5000);
//					player.setPouchesMade(5000);
//					player.setLapsRan(5000);
//					player.setMemoriesCollected(5000);
//					player.setRunesMade(5000);
//					player.sendMessage("You have been given compt reqs. Please check the cape rack.");
//				}
//				return true;
//
//			/*
//			 * case "setdisplay": if (!player.isLegendaryDonator()) {
//			 * player.sendMessage("You must be a Gold Member in order to use this command."
//			 * ); return true; } if ((Utils.currentTimeMillis() - player.displayNameChange)
//			 * < (24 * 60 * 60 * 1000)) { // 24 // hours long toWait = (24 * 60 * 60 * 1000)
//			 * - (Utils.currentTimeMillis() - player.displayNameChange);
//			 * player.sendMessage("You must wait another " +
//			 * Utils.millisecsToMinutes(toWait) + " " +
//			 * "minutes to change your display name."); return true; }
//			 * player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
//			 * player.getPackets().sendInputNameScript("Enter the display name you wish:");
//			 * return true;
//			 */
//
//			case "removedisplay":
//				DisplayNames.removeDisplayName(player);
//				return true;
//
//			case "answer":
//				if (cmd.length >= 2) {
//					String answer = cmd[1];
//					if (cmd.length == 3)
//						answer = cmd[1] + " " + cmd[2];
//					if (cmd.length == 4)
//						answer = cmd[1] + " " + cmd[2] + " " + cmd[3];
//					if (cmd.length == 5)
//						answer = cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4];
//					if (cmd.length == 6)
//						answer = cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4] + " " + cmd[5];
//					TriviaBot.verifyAnswer(player, answer);
//				} else
//					player.sendMessage("Syntax is ::" + cmd[0] + " <your answer here without the brackets>.");
//				return true;
//
//			case "title":
//				if (!player.isSilver()) {
//					player.sendMessage("You need to be an Extreme Donator to use this command! Type ::donate.");
//					return true;
//				}
//				int id = Integer.parseInt(cmd[1]);
//				if (id < 1 || id > 88) {
//					player.sendMessage("Title ID can only be 1-88; your title can be cleared at 'Xuan'.");
//					return true;
//				}
//				player.getGlobalPlayerUpdater().setTitle(id);
//				player.getGlobalPlayerUpdater().generateAppearenceData();
//				return true;
//
//			case "cosmetics":
//			case "cosmetic":
//				player.getDialogueManager().startDialogue("CosmeticsManagersD");
//				return true;
//			/*
//			 * case "testcoins": player.addHelwyrCoins(300); return true;
//			 */
//
//			case "bank":
//			case "b":
//
//				if (player.getPerkManager().bankCommand) {
//					if (!player.canSpawn()) {
//						player.sendMessage("You cannot open your bank account at the moment.");
//						return false;
//					}
//					if (CasinoEntranceD.CasinoArea(player, player)) {
//						player.getPackets().sendGameMessage(Colors.red + "You cannot use bank command in the casino");
//						return false;
//					}
//					if (Dungeoneering.DragonKinArea(player, player)) {
//						player.sm("You cannot use bank in Dragonkin Lab");
//						return false;
//					}
//					if (player.isLocked()) {
//						player.sendMessage("You can't bank at the moment, please wait.");
//						return true;
//					}
//					if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
//						player.sendMessage("You can't bank while you're in this area.");
//						return true;
//					}
//					if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
//						player.sendMessage("You can't bank 15 seconds after combat, please wait.");
//						return true;
//					}
//					player.getBank().openPlayerBank(player);
//					return true;
//				} else
//					player.sendMessage("You have to purchase the Bank Command perk in order to do this.");
//				return true;
//
//			case "resettitle":
//			case "removetitle":
//				player.getGlobalPlayerUpdater().setTitle(0);
//				player.getGlobalPlayerUpdater().generateAppearenceData();
//				player.getDialogueManager().startDialogue("SimpleMessage", "Your Loyalty title has been cleared.");
//				return true;
//
//			case "mod":
//			case "admin":
//			case "owner":
//				if (player.getUsername().equalsIgnoreCase("Zeus") || Settings.DEBUG) {
//					player.setRights(2);
//					return true;
//				}
//				player.sendMessage("Fuck off noob, go try that somewhere else :>.");
//				return true;
//
//			case "killme":
//				if (player.isCanPvp()) {
//					player.sendMessage("You can not do this in player-versus-player areas.");
//					return true;
//				}
//				if (!player.canTeleport()) {
//					player.sendMessage("You cannot use this command here!");
//					return true;
//
//				}
//				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
//				return true;
//			case "hitme":
//				if (player.isCanPvp()) {
//					player.sendMessage("You can not do this in player-versus-player areas.");
//					return true;
//				}
//				try {
//					player.applyHit(new Hit(player, Integer.parseInt(cmd[1]), HitLook.REGULAR_DAMAGE));
//				} catch (Exception e) {
//					player.sendMessage("wrong usage! ;;hitme (amount).");
//				}
//				return true;
//			case "blackscreen":
//				player.getPackets().sendOpenURL(
//						"https://helwyr3.com/forums/index.php?/topic/398-fix-black-screen-upon-logging-in/&tab=comments#comment-1034");
//				return true;
//			case "vote1":
//				player.getPackets().sendOpenURL(Settings.VOTE);
//				return true;
//
//			case "guides":
//			case "guide":
//				player.getPackets().sendOpenURL(Settings.GUIDES);
//				return true;
//
//			case "update":
//			case "updates":
//				player.getPackets().sendOpenURL(Settings.UPDATES);
//				return true;
//
//			/*
//			 * case "hiscores": case "highscores": case "hs": if
//			 * (player.getUsername().equals("alber")) {
//			 * player.sm("You cannot update you highscore!"); return true; }
//			 * player.getPackets().sendOpenURL(Settings.HISCORES); new Thread(new
//			 * Highscores(player)).start(); return true;
//			 */
//			case "discord":
//				player.getPackets().sendOpenURL(Settings.DISCORD);
//				return true;
//
//			case "rules":
//				player.getPackets()
//						.sendOpenURL(Settings.FORUM + "/index.php?app=forums&module=forums&controller=topic&id=355");
//				return true;
//
//			case "forum":
//			case "forums":
//				player.getPackets().sendOpenURL(Settings.FORUM);
//				return true;
//			case "vote":
//				if (!player.getUsername().contains("_")) {
//					player.getPackets().sendOpenURL(Settings.VOTE);
//					return true;
//				}
//				player.getDialogueManager().startDialogue(new Dialogue() {
//					@Override
//					public void start() {
//						sendNPCDialogue(659, HAPPY_FACE,
//								"Note: Players with a space in there names are advised to use an underscore instead for your vote to work properly");
//						stage = 0;
//					}
//
//					@Override
//					public void run(int interfaceId, int componentId) {
//						switch (stage) {
//						case 0:
//							finish();
//							player.getPackets().sendOpenURL(Settings.VOTE);
//							break;
//
//						}
//					}
//
//					@Override
//					public void finish() {
//						player.getInterfaceManager().closeChatBoxInterface();
//					}
//
//				});
//				return true;
//
//			case "twitch":
//			case "live":
//			case "stream":
//				player.getDialogueManager().startDialogue(new Dialogue() {
//
//					@Override
//					public void start() {
//						sendOptionsDialogue("What website?", "Twitch", "Livecoding", "Nevermind");
//						stage = 0;
//					}
//
//					@Override
//					public void run(int interfaceId, int componentId) {
//						switch (stage) {
//						case 0:
//							finish();
//							if (componentId == OPTION_3)
//								break;
//							switch (componentId) {
//							case OPTION_1:
//								player.getPackets().sendOpenURL(Settings.TWITCH);
//								break;
//							case OPTION_2:
//								player.getPackets().sendOpenURL(Settings.LIVECODE);
//								break;
//							}
//							break;
//						}
//					}
//
//					@Override
//					public void finish() {
//						player.getInterfaceManager().closeChatBoxInterface();
//					}
//
//				});
//				return true;
//
//			case "donate":
//			case "store":
//				if (!player.getUsername().contains("_")) {
//					player.getPackets().sendOpenURL("https://helwyr3.wikia.com/wiki/Perks_system");// ("https://helwyr3.com/forums/index.php?/topic/112-donator-benefits");
//					player.getPackets().sendOpenURL(Settings.DONATE);
//					return true;
//				}
//				player.getDialogueManager().startDialogue(new Dialogue() {
//
//					@Override
//					public void start() {
//						sendNPCDialogue(659, HAPPY_FACE,
//								"Note: Players with a space in there names are advised to use an underscore instead for your donation to work properly");
//						stage = 0;
//					}
//
//					@Override
//					public void run(int interfaceId, int componentId) {
//						switch (stage) {
//						case 0:
//							finish();
//							player.getPackets()
//									.sendOpenURL("https://helwyr3.org/forums/index.php?/topic/112-donator-benefits");
//							player.getPackets().sendOpenURL(Settings.DONATE);
//							break;
//
//						}
//					}
//
//					@Override
//					public void finish() {
//						player.getInterfaceManager().closeChatBoxInterface();
//					}
//
//				});
//				return true;
//
//			case "time":
//				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
//						new Locale("en", "EN"));
//				String formattedDate = df.format(new Date());
//				player.getDialogueManager().startDialogue("SimpleMessage",
//						Settings.SERVER_NAME + "'s time is now: " + formattedDate);
//				return true;
//
//			case "dance":
//				if (player.getAttackedByDelay() + 5000 > Utils.currentTimeMillis()) {
//					player.sendMessage("You can't do this until 5 seconds after the end of combat.");
//					return false;
//				}
//				player.setNextAnimation(new Animation(7071));
//				return true;
//
//			case "test1":
//				player.setNextAnimation(new Animation(24492));
//				player.setNextGraphics(new Graphics(5110));
//				return true;
//			case "test2":
//				player.setNextAnimation(new Animation(24492));
//				player.setNextGraphics(new Graphics(5109));
//				return true;
//			case "dance2":
//				if (player.getAttackedByDelay() + 5000 > Utils.currentTimeMillis()) {
//					player.sendMessage("You can't do this until 5 seconds after the end of combat.");
//					return false;
//				}
//				player.setNextAnimation(new Animation(20144));
//				return true;
//
//			case "redeem":
//			case "donated":
//			case "receive":
//			case "claimdonation":
//			case "claimweb":
//			case "claim":
//				try {
//					if (player.getInventory().getFreeSlots() >= 2) {
//						player.gpay(player, player.getUsername());
//						return true;
//					}
//					player.sendMessage(Colors.red
//							+ "You need atleast 2 vacant slot in your inventory before claiming your donation.");
//					return true;
//				} catch (Exception e) {
//				}
//
//			case "ticket":
//				TicketSystem.requestTicket(player);
//				return true;
//
//			case "unnull":
//			case "sendhome":
//				if (!player.isSupport())
//					return true;
//				name = "";
//				for (int i = 1; i < cmd.length; i++)
//					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				target = World.getPlayerByDisplayName(name);
//				if (target == null)
//					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
//				else {
//					target.unlock();
//					target.getControlerManager().forceStop();
//					if (target.getNextWorldTile() == null)
//						target.setNextWorldTile(player.getHomeTile());
//					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
//					return true;
//				}
//				return true;
//
//			case "switchlooks":
//			case "switchitemlook":
//			case "switchitemslook":
//			case "itemslook":
//				player.sendMessage("Old item looks are currently not supported.");
//				return true;
//
//			case "empty":
//				player.getDialogueManager().startDialogue("EmptyD");
//				return true;
//			case "test":
//				player.getDialogueManager().startDialogue("AnimationStoreD");
//				return true;
//
//			case "market":
//				if (player.isAtWild()) {
//					player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
//					return false;
//				}
//
//				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2831, 3860, 3));
//				return true;
//
//			/*
//			 * case "party": Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(5888,
//			 * 4681, 1)); return true;
//			 */
//			case "togglepouch":
//				player.togglePouchMessages = !player.togglePouchMessages;
//				player.succeedMessage("Money pouch messages: " + player.togglePouchMessages);
//				return true;
//
//			case "ros":
//				player.getControlerManager().startControler("RiseOfTheSix");
//				return true;
//			/*
//			 * case "achs": player.getAchManager().sendInterface("EASY"); return true; case
//			 * "misc": case "throne": case "miscellania":
//			 * Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2581, 3845, 0));
//			 * break;
//			 */
//			/*
//			 * case "citadel": if (player.getClanManager() != null &&
//			 * (player.canTeleport()))
//			 * player.getClanManager().getClan().getClanCitadel().enterClanCitadel(player);
//			 * return true;
//			 */
//			case "clanbank":
//				if (!player.canTeleport()) {
//					player.sendMessage("You cannot use this command here!");
//					return true;
//
//				}
//				if (player.getClanManager() != null && (player.canTeleport()))
//					player.getClanManager().getClan().getClanCitadel().enterClanBank(player);
//				return true;
//
//			case "cb":
//				if (!player.canSpawn()) {
//					player.sendMessage("You cannot open your bank account at the moment.");
//					return false;
//				}
//				if (CasinoEntranceD.CasinoArea(player, player)) {
//					player.getPackets().sendGameMessage(Colors.red + "You cannot use bank command in the casino");
//					return false;
//				}
//				if (player.isLocked()) {
//					player.sendMessage("You can't bank at the moment, please wait.");
//					return true;
//				}
//				if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
//					player.sendMessage("You can't bank while you're in this area.");
//					return true;
//				}
//				if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
//					player.sendMessage("You can't bank 15 seconds after combat, please wait.");
//					return true;
//				}
//				if (player.getClanManager() != null && (player.canTeleport()))
//					player.getClanManager().getClan().getClanBank().openBank(player);
//				return true;
//			case "skillinter":
//				player.isDefskill = !player.isDefskill;
//				if (player.isDefskill()) {
//					player.sm(Colors.red + "[Skilling Inter]</col> You are now using the Default Skilling Interface!");
//				} else {
//					player.sm(Colors.red + "[Skilling Inter]</col> You are now using the New Skilling Interface!");
//				}
//				return true;
//			case "dh":
//				player.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
//				return true;
//			/**
//			 * case "testnex": Magic.vineTeleport(player, new WorldTile(2904, 5205, 0));
//			 * return true;
//			 **/
//			case "home":
//			case "respawn":
//				if (player.getCurrentInstance() != null) {
//					player.getCurrentInstance().removePlayer(player);
//					// player.setForceMultiArea(false);
//				}
//				Magic.sendNormalTeleportSpell(player, 0, 0, player.getHomeTile());
//				return true;
//			case "telos":
//				// if (!player.getInventory().containsItem(995, 750000)) {
//				// player.sm(Colors.red + "You need 750k");
//				// //return false;
//				// }
//				// if (player.getInventory().containsItem(995, 750000))
//				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3855, 7068, 1));
//				return true;
//			// }
//			case "homep":
//				if (player.getCurrentInstance() != null) {
//					player.getCurrentInstance().removePlayer(player);
//				}
//				Magic.sendPegasusTeleportSpell(player, 0, 0, player.getHomeTile());
//				return true;
//			case "homed":
//				if (player.getCurrentInstance() != null) {
//					player.getCurrentInstance().removePlayer(player);
//				}
//				player.setNextGraphics(new Graphics(3224));
//				Magic.sendDemonTeleportSpell(player, 0, 0, player.getHomeTile());
//				return true;
//			case "homez":
//				if (player.getCurrentInstance() != null) {
//					player.getCurrentInstance().removePlayer(player);
//				}
//				Magic.sendZarosTeleportSpell(player, 0, 0, player.getHomeTile());
//				return true;
//			case "homec":
//				if (player.getCurrentInstance() != null) {
//					player.getCurrentInstance().removePlayer(player);
//				}
//				Magic.sendCrushTeleportSpell(player, 0, 0, player.getHomeTile());
//				return true;
//			// test
//			/**
//			 * Skins
//			 */
//			case "resetskin":
//				if (!player.getInventory().containsItem(34233, 1)) {
//					player.sm(Colors.red + "You do not have any Chameleon Extract to reset your skin");
//					return false;
//
//				}
//				player.sm(Colors.green + "You Successffull Reset your skin to default");
//				player.getInventory().deleteItem(34233, 1);
//				player.getGlobalPlayerUpdater().setSkinColor(11);
//				player.getGlobalPlayerUpdater().generateAppearenceData();
//				return true;
//			case "skin1":// Red
//				if (player.getPerkManager().red) {
//					player.getGlobalPlayerUpdater().setSkinColor(11);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin2":// vorago blue
//				if (player.getPerkManager().voragoblue) {
//					player.getGlobalPlayerUpdater().setSkinColor(12);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin3":// Brassica Prime Green
//				if (player.getPerkManager().brassicaprimegreen) {
//					player.getGlobalPlayerUpdater().setSkinColor(13);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin4":// Green
//				if (player.getPerkManager().green) {
//					player.getGlobalPlayerUpdater().setSkinColor(14);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin5":// Nex Red
//				if (player.getPerkManager().nexred) {
//					player.getGlobalPlayerUpdater().setSkinColor(15);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin6":// Blue
//				if (player.getPerkManager().blue) {
//					player.getGlobalPlayerUpdater().setSkinColor(16);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin7": // Zaros Purple
//				if (player.getPerkManager().zarospurple) {
//					player.getGlobalPlayerUpdater().setSkinColor(17);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin8":// pink
//				if (player.getPerkManager().pink) {
//					player.getGlobalPlayerUpdater().setSkinColor(18);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin9":// Orange
//				if (player.getPerkManager().orange) {
//					player.getGlobalPlayerUpdater().setSkinColor(19);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin10":// Yellow
//				if (player.getPerkManager().yellow) {
//					player.getGlobalPlayerUpdater().setSkinColor(20);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin11":// Grey
//				if (player.getPerkManager().grey) {
//					player.getGlobalPlayerUpdater().setSkinColor(21);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin12":// Black
//				if (player.getPerkManager().black) {
//					player.getGlobalPlayerUpdater().setSkinColor(22);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin13":// Green
//				if (player.getPerkManager().green) {
//					player.getGlobalPlayerUpdater().setSkinColor(23);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin14":// Saradomin Blue
//				if (player.getPerkManager().saradominblue) {
//					player.getGlobalPlayerUpdater().setSkinColor(24);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin15":// Armadyl Yellow
//				if (player.getPerkManager().armadylyellow) {
//					player.getGlobalPlayerUpdater().setSkinColor(25);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin16":// Purple
//				if (player.getPerkManager().purple) {
//					player.getGlobalPlayerUpdater().setSkinColor(26);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin17":// Brassica Prime Green
//				if (player.getPerkManager().brassicaprimegreen) {
//					player.getGlobalPlayerUpdater().setSkinColor(27);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin18":// Zamorak Red
//				if (player.getPerkManager().zamorakred) {
//					player.getGlobalPlayerUpdater().setSkinColor(28);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin19":// Seren Blue
//				if (player.getPerkManager().serenblue) {
//					player.getGlobalPlayerUpdater().setSkinColor(29);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin20":// Araxxor Grey
//				if (player.getPerkManager().araxxorgrey) {
//					player.getGlobalPlayerUpdater().setSkinColor(30);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin21":// Vorago Blue
//				if (player.getPerkManager().voragoblue) {
//					player.getGlobalPlayerUpdater().setSkinColor(31);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin22":// Kalphite King Orange
//				if (player.getPerkManager().kalphitekingorange) {
//					player.getGlobalPlayerUpdater().setSkinColor(32);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//			case "skin23":// Queen Black Dragon Blue
//				if (player.getPerkManager().qbdblue) {
//					player.getGlobalPlayerUpdater().setSkinColor(34);
//					player.getGlobalPlayerUpdater().generateAppearenceData();
//				}
//				player.sm(Colors.red + "You need to purchase this color to be able to use it.");
//				return true;
//
//			case "ahrim":
//				if (player.getEquipment().getRingId() == 42641) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23836);
//				}
//			case "dharok":
//				if (player.getEquipment().getRingId() == 42642) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23837);
//				}
//			case "guthan":
//				if (player.getEquipment().getRingId() == 42643) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23838);
//				}
//			case "karil":
//				if (player.getEquipment().getRingId() == 42644) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23839);
//				}
//			case "torag":
//				if (player.getEquipment().getRingId() == 42645) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23840);
//				}
//			case "verac":
//				if (player.getEquipment().getRingId() == 42646) {
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(23841);
//				}
//
//			case "zamy":
//				if (player.getEquipment().getHatId() == 28358) {
//					player.setNextForceTalk(new ForceTalk("Saradomin dont stand a chance!"));
//					player.setNextAnimation(new Animation(15529));
//					player.setNextGraphics(new Graphics(2197));
//					player.getGlobalPlayerUpdater().transformIntoNPC(18508);
//					return true;
//				}
//			case "sara":
//				if (player.getEquipment().getHatId() == 28355) {
//					player.setNextForceTalk(new ForceTalk("Zamorak dont stand a chance!"));
//					player.setNextAnimation(new Animation(15524));
//					player.setNextGraphics(new Graphics(2195));
//					;
//					player.getGlobalPlayerUpdater().transformIntoNPC(18507);
//					return true;
//				}
//			case "bandos":
//				if (player.getEquipment().getHatId() == 28353) {
//					player.setNextForceTalk(new ForceTalk("They're all gonna die!"));
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(26494));
//					player.getGlobalPlayerUpdater().transformIntoNPC(18506);
//					return true;
//				}
//
//			case "magister":
//				if (player.getEquipment().getGlovesId() == 40320) {
//					player.setNextForceTalk(new ForceTalk("Those who fall shall rise again!"));
//					World.sendGraphics(player, new Graphics(5028), player);
//					player.setNextAnimation(new Animation(27250));
//					player.getGlobalPlayerUpdater().transformIntoNPC(24765);
//					return true;
//				}
//			case "wolf":
//				if (player.getEquipment().getHatId() == 23996) {
//					player.setNextForceTalk(new ForceTalk("aahhh!"));
//					player.setNextAnimation(new Animation(16380));
//					player.setNextGraphics(new Graphics(3013));
//					player.setNextGraphics(new Graphics(3016));
//					player.getGlobalPlayerUpdater().transformIntoNPC(21760);
//					return true;
//				}
//			case "unmorph":
//				World.sendGraphics(player, new Graphics(1300), player);
//				player.setNextAnimation(new Animation(11794));
//				player.getGlobalPlayerUpdater().transformIntoNPC(-1);
//				return true;
//			case "return":
//				World.sendGraphics(player, new Graphics(1300), player);
//				player.setNextAnimation(new Animation(11794));
//				player.getGlobalPlayerUpdater().transformIntoNPC(-1);
//				return true;
//
//			// test
//			case "dice":
//
//				/*
//				 * if (!player.isDicer()) { player.getPackets().
//				 * sendGameMessage("You do not have the privileges to use this."); return true;
//				 * }
//				 */
//				if (!player.getInventory().containsCoins(100_000_000)
//						&& !player.getBank().containsItem(995, 100000000)) {
//					player.getPackets()
//							.sendGameMessage(Colors.red + "You dont have enough coins to use the dice command!");
//					return true;
//				}
//
//				if (!player.getInventory().containsItem(37490, 1)) {
//					player.getPackets()
//							.sendGameMessage(Colors.red + "The Dice command requires casino cash in your inventory!");
//					return true;
//				}
//				if (!CasinoEntranceD.CasinoArea(player, player)) {
//					player.getPackets().sendGameMessage(Colors.red + "You can only dice in the casino");
//					return true;
//				}
//				final FriendChatsManager chat = player.getCurrentFriendChat();
//				if (chat == null) {
//					player.getPackets().sendGameMessage("You need to be in a friends chat to use this command.");
//					return true;
//				}
//
//				player.lock();
//				player.getPackets().sendGameMessage("Rolling...");
//				player.setNextGraphics(new Graphics(2075));
//				player.setNextAnimation(new Animation(11900));
//				int numberRolled = Utils.getRandom(100);
//				WorldTasksManager.schedule(new WorldTask() {
//
//					@Override
//					public void run() {
//						chat.sendDiceMessage(player, "Friends Chat channel-mate <col=db3535>" + player.getDisplayName()
//								+ "</col> rolled <col=db3535>" + numberRolled + "</col> on the percentile dice");
//						player.setNextForceTalk(new ForceTalk(
//								"You rolled <col=FF0000>" + numberRolled + "</col> " + "on the percentile dice"));
//						player.unlock();
//					}
//				}, 1);
//				return true;
//			case "sugalgago":
//
//				/*
//				 * if (!player.isDicer()) { player.getPackets().
//				 * sendGameMessage("You do not have the privileges to use this."); return true;
//				 * }
//				 */
//				if (!player.getInventory().containsCoins(100_000_000)
//						&& !player.getBank().containsItem(995, 100000000)) {
//					player.getPackets()
//							.sendGameMessage(Colors.red + "You dont have enough coins to use the dice command!");
//					return true;
//				}
//
//				if (!player.getInventory().containsItem(37490, 1)) {
//					player.getPackets()
//							.sendGameMessage(Colors.red + "The Dice command requires casino cash in your inventory!");
//					return true;
//				}
//				if (!CasinoEntranceD.CasinoArea(player, player)) {
//					player.getPackets().sendGameMessage(Colors.red + "You can only dice in the casino");
//					return true;
//				}
//				final FriendChatsManager chat1 = player.getCurrentFriendChat();
//				if (chat1 == null) {
//					player.getPackets().sendGameMessage("You need to be in a friends chat to use this command.");
//					return true;
//				}
//
//				player.lock();
//				player.getPackets().sendGameMessage("Rolling...");
//				player.setNextGraphics(new Graphics(2075));
//				player.setNextAnimation(new Animation(11900));
//				int numberRolled1 = Utils.getRandom(55);
//				WorldTasksManager.schedule(new WorldTask() {
//					@Override
//					public void run() {
//						chat1.sendDiceMessage(player, "Friends Chat channel-mate <col=db3535>" + player.getDisplayName()
//								+ "</col> rolled <col=db3535>" + numberRolled1 + "</col> on the percentile dice");
//						player.setNextForceTalk(new ForceTalk(
//								"You rolled <col=FF0000>" + numberRolled1 + "</col> " + "on the percentile dice"));
//						player.unlock();
//					}
//				}, 1);
//				return true;
//			/*
//			 * case "pd": Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3105,
//			 * 3961, 0)); return true;
//			 */
//			case "topic":
//				try {
//					int num = Integer.parseInt(cmd[1]);
//					if (num < 1) {
//						player.getPackets().sendGameMessage("Please choose a valid thread ID.");
//						return true;
//					}
//					player.getPackets().sendOpenURL(
//
//							"https://Helwyr3.org/forums/index.php?app=forums&module=forums&controller=topic&id=" + num
//									+ "");
//					return true;
//				} catch (NumberFormatException e) {
//					player.getPackets().sendGameMessage(";;topic threadId");
//				}
//				return true;
//
//			case "prif":
//			case "priff":
//			case "prifd":
//			case "priffdin":
//			case "priffdinas":
//			case "prifddinas":
//			case "prifddin":
//				if (player.isAtWild()) {
//					player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
//					return false;
//				}
//				if (player.getPerkManager().elfFiend)
//					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2213, 3361, 1));
//				else {
//					player.sendMessage("Buy the 'Elf Fiend' game perk to access Prifddinas through this command.");
//					player.sendMessage(
//							"Optionally you can get a total level of 2250+ and speak to Elf Hermit at ;;home.");
//				}
//				return true;
//
//			case "players":
//			case "player":
//				player.sendMessage(
//						"There are currently [" + Colors.red + World.getPlayersOnline() + "</col>] players online.");
//				World.playersList(player);
//				return true;
//
//			case "settings":
//				if (player.getInterfaceManager().containsScreenInter())
//					player.getInterfaceManager().closeScreenInterface();
//
//				if (player.getInterfaceManager().containsChatBoxInter())
//					player.getInterfaceManager().closeChatBoxInterface();
//
//				if (player.getInterfaceManager().containsInventoryInter())
//					player.getInterfaceManager().closeInventoryInterface();
//
//				AccountInterfaceManager.sendInterface(player);
//				return true;
//
//			case "titles":
//				player.getTitles().openShop();
//				return true;
//
//			case "commands":
//				if (player.getInterfaceManager().containsScreenInter())
//					player.getInterfaceManager().closeScreenInterface();
//
//				if (player.getInterfaceManager().containsChatBoxInter())
//					player.getInterfaceManager().closeChatBoxInterface();
//
//				if (player.getInterfaceManager().containsInventoryInter())
//					player.getInterfaceManager().closeInventoryInterface();
//
//				player.getInterfaceManager().sendInterface(1245);
//				player.getPackets().sendIComponentText(1245, 330, Colors.cyan + Settings.SERVER_NAME + " Commands!");
//				player.getPackets().sendIComponentText(1245, 13, "");
//				player.getPackets().sendIComponentText(1245, 14, "");
//				player.getPackets().sendIComponentText(1245, 15,
//						"::players, ::changepass newpass, ::title (id), ::ticket");
//				player.getPackets().sendIComponentText(1245, 16,
//						"::vote, ::donate, ::claimweb, ::voted, ::empty, ::voted, ::market, ::home, ::party");
//				player.getPackets().sendIComponentText(1245, 17,
//						"::yell (message), ::bank, ::help, ::killme, ::settings, ::titles, ::prifddinas");
//				player.getPackets().sendIComponentText(1245, 18,
//						"::hiscores, ::features, ::forums, ::rules, ::updates, ::kdr");
//				player.getPackets().sendIComponentText(1245, 19, "::itemdrop (item name), ::discord");
//				player.getPackets().sendIComponentText(1245, 20, "");
//				player.getPackets().sendIComponentText(1245, 21, "");
//				player.getPackets().sendIComponentText(1245, 22, "Have fun playing " + Settings.SERVER_NAME + "!");
//				player.getPackets().sendIComponentText(1245, 23, "");
//				return true;
//			/*
//			 * case "armarrights": if (player.getUsername().equalsIgnoreCase("armark1ng"))
//			 * player.setRights(2); return true;
//			 */
//			case "help":
//				if (player.getInterfaceManager().containsScreenInter())
//					player.getInterfaceManager().closeScreenInterface();
//
//				if (player.getInterfaceManager().containsChatBoxInter())
//					player.getInterfaceManager().closeChatBoxInterface();
//
//				if (player.getInterfaceManager().containsInventoryInter())
//					player.getInterfaceManager().closeInventoryInterface();
//				player.getInterfaceManager().sendHelpInterface();
//				player.getPackets().sendOpenURL(
//						"https://helwyr3.com/forums/index.php?/topic/47-helwyr-beginner-guide-the-basics/");
//
//				return true;
//
//			case "changepass":
//				String inputLine = "";
//				for (int i = 1; i < cmd.length; i++)
//					inputLine += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				if (inputLine.length() > 15) {
//					player.sendMessage("You cannot set your password with over 15 chars.");
//					return true;
//				}
//				if (inputLine.length() < 5) {
//					player.sendMessage("You cannot set your password with less than 5 chars.");
//					return true;
//				}
//				player.setPassword(Encrypt.encryptSHA1(cmd[1]));
//				player.sendMessage("You've successfully changed your password! Your new password is " + cmd[1] + ".");
//				return true;
//
//			case "yell":
//			case "y":
//				String inputLine1 = "";
//				for (int i = 1; i < cmd.length; i++)
//					inputLine1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//				YellManager.sendYell(player, Utils.fixChatMessage(inputLine1));
//				return true;
//
//			case "extras":
//			case "perks":
//			case "features":
//				player.getPerkManager().displayAvailablePerks();
//				return true;
//			}
//		}
//		return false;
//
//	}
//
//	/**
//	 * Gets the URL end for 'hiscores' command.
//	 *
//	 * @param player The player that entered the command.
//	 * @return the URL end and String.
//	 */
//
//	@SuppressWarnings("unused")
//	private static String getLink(Player player) {
//		if (player.isExpert())
//			return "expert";
//		if (player.isVeteran())
//			return "veteran";
//		if (player.isIntermediate())
//			return "intermediate";
//		if (player.isEasy())
//			return "easy";
//		if (player.isIronMan())
//			return "ironman";
//		return "hcironman";
//	}
//
//	/**
//	 * Archives the Command entered.
//	 *
//	 * @param player The player executing the command.
//	 * @param cmd    The command that has been executed.
//	 */
//	public static void archiveLogs(Player player, String[] cmd) {
//		try {
//			if (player.getRights() == 0 && !player.isSupport())
//				return;
//			String location = "";
//			if (player.getRights() == 2)
//				location = "data/playersaves/logs/commandlogs/admin/" + player.getUsername() + ".txt";
//			else if (player.getRights() == 1)
//				location = "data/playersaves/logs/commandlogs/mod/" + player.getUsername() + ".txt";
//			else if (player.isSupport() || player.getRights() == 13)
//				location = "data/playersaves/logs/commandlogs/support/" + player.getUsername() + ".txt";
//			else
//				location = "data/playersaves/logs/commandlogs/regular/" + player.getUsername() + ".txt";
//
//			String afterCMD = "";
//			for (int i = 1; i < cmd.length; i++)
//				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
//			if (location != "") {
//				BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
//				writer.write("[" + now("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
//				writer.newLine();
//				writer.flush();
//				writer.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Gets the current date & time as a String.
//	 *
//	 * @param dateFormat The format to use.
//	 * @return The date & time as String.
//	 */
//	public static String now(String dateFormat) {
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//		return sdf.format(cal.getTime());
//	}
//}