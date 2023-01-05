package com.rs.game.player.content.commands;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.*;
import com.rs.game.world.Graphics;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.BanksManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.*;
import com.rs.game.player.content.corrupt.ItemSearch;
import com.rs.game.player.content.corrupt.animations.teleports.Teleport;
import com.rs.game.player.content.dropprediction.DropPrediction;
import com.rs.game.player.content.grandExchange.GrandExchange;
import com.rs.game.world.controllers.DamageArea;
import com.rs.game.world.controllers.FightCaves;
import com.rs.game.world.controllers.Wilderness;
import com.rs.game.world.controllers.bossInstance.VoragoInstanceController;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.utils.*;
import com.rs.utils.mysql.impl.VoteManager;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AdminCommands {

    public static boolean processAdminCommand(final Player player, String[] cmd, boolean clientCommand) {

        StringBuilder name;
        Player target;
        String PUNISHMENTS = Settings.FORUM + "/forumdisplay.php?fid=12";

        switch(cmd[0]){
            case "maxdung":

                player.getDungManager().setMaxComplexity(6);
                player.getDungManager().setMaxFloor(60);
                return true;

            case "newtask":
                player.getDailyTaskManager().getNewTask(true);
                return true;

            case "newtaskother":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target == null) {
                    player.sendMessage(Utils.formatString(name.toString()) + " is not logged in.");
                    return true;
                }
                target.getDailyTaskManager().getNewTask(true);
                player.getPackets().sendGameMessage("You have given him a new task. His new task is :"
                        + target.getDailyTaskManager().getCurrentTask().getTaskMessage(target));
                return true;

            case "kick":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target == null) {
                    player.sendMessage(Utils.formatString(name.toString()) + " is not logged in.");
                    return true;
                }
                target.forceLogout();
                Logger.log("Commands",
                        "Player " + player.getDisplayName() + " has force kicked " + target.getDisplayName() + "!");
                player.sendMessage("You have force kicked: " + target.getDisplayName() + ".");
                return true;

            case "hide":
                if (Wilderness.isAtWild(player)) {
                    player.getPackets().sendGameMessage("You can't use ::hide here.");
                    return true;
                }
                player.getGlobalPlayerUpdater().switchHidden();
                player.getPackets().sendGameMessage("Am i hidden? " + player.getGlobalPlayerUpdater().isHidden());
                return true;


            case "getid":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++) {
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                }
                ItemSearch1.searchForItem(player, name.toString());
                return true;

            case "xteletome":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                if (target == null)
                    return true;
                if (target.getControlerManager().getControler() instanceof FightCaves) {
                    player.sendMessage("You can't teleport someone from a Fight Caves instance.");
                    return true;
                }
                if (target.getControlerManager().getControler() instanceof DamageArea) {
                    player.sendMessage("You can't teleport someone from Mummy Area instance.");
                    return true;
                }
                if (target.getGlobalPlayerUpdater().isHidden())
                    return true;
                target.setNextWorldTile(new WorldTile(player));
                target.stopAll();
                return true;

            case "xteleto":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                if (target == null)
                    return true;
                if (target.getControlerManager().getControler() instanceof FightCaves) {
                    player.sendMessage("You can't teleport to someones Fight Caves instance.");
                    return true;
                }
                if (target.getControlerManager().getControler() instanceof DamageArea) {
                    player.sendMessage("You can't teleport to someones Mummy Area instance.");
                    return true;
                }
                if (target.getGlobalPlayerUpdater().isHidden())
                    return true;
                player.setNextWorldTile(new WorldTile(target));
                player.stopAll();
                return true;

            case "ticket":
                TicketSystem.answerTicket(player);
                return true;

            case "unban":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    IPBanL.unban(target);
                    MACBan.unban(target);
                    target.setBanned(0);
                    target.setPermBanned(false);
                    player.sendMessage("You have unbanned: " + target.getDisplayName() + ".");
                } else {
                    name = new StringBuilder(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (!SerializableFilesManager.containsPlayer(name.toString())) {
                        player.sendMessage("Account name '" + Utils.formatString(name.toString()) + "' doesn't exist.");
                        return true;
                    }
                    target = SerializableFilesManager.loadPlayer(name.toString());
                    assert target != null;
                    target.setUsername(name.toString());
                    IPBanL.unban(target);
                    MACBan.unban(target);
                    target.setBanned(0);
                    target.setPermBanned(false);
                    player.sendMessage("You have unbanned: " + name + ".");
                    SerializableFilesManager.savePlayer(target);
                }
                return true;

            case "mute":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    target.setMuted(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    player.sendMessage("You have muted: " + target.getDisplayName() + " for 1 hour.");
                    target.sendMessage("You have been muted for 1 hour by " + player.getDisplayName() + "!");
                    SerializableFilesManager.savePlayer(target);
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                } else {
                    File acc5 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    target = SerializableFilesManager.loadPlayer(name.toString());
                    assert target != null;
                    target.setUsername(name.toString());
                    target.setMuted(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    player.sendMessage("You have muted: " + name + " for 1 hour.");
                    SerializableFilesManager.savePlayer(target);
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc5);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + name + " failed muting " + name + "!");
                    }
                }
                return true;
            case "unjail":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    target.setJailed(0);
                    target.sendMessage("You've been unjailed by " + player.getDisplayName() + ".");
                    player.sendMessage("You have unjailed: " + target.getDisplayName() + ".");
                    target.setNextWorldTile(player.getHomeTile());
                    SerializableFilesManager.savePlayer(target);
                } else {
                    File acc1 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    try {
                        target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
                    } catch (ClassNotFoundException | IOException e) {
                        Logger.log("Could not locate playerfile " + acc1 + ".");
                    }
                    assert target != null;
                    target.setJailed(0);
                    player.sendMessage("You have unjailed: " + target.getUsername() + ".");
                    target.setNextWorldTile(player.getHomeTile());
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc1);
                    } catch (IOException ignored) {

                    }
                }
                return true;
            case "unnull":
            case "sendhome":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target == null)
                    player.sendMessage("Couldn't find player " + name + ".");
                else {
                    target.unlock();
                    target.getControlerManager().forceStop();
                    if (target.getNextWorldTile() == null)
                        target.setNextWorldTile(target.getHomeTile());
                    player.sendMessage("You have sent home player: " + target.getDisplayName() + ".");
                    return true;
                }
                return true;

            case "checkinv":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target == null) {
                    player.sendMessage(Utils.formatString(name.toString()) + " is not logged in.");
                    return true;
                }
                target = World.getPlayerByDisplayName(name.toString());
                try {
                    assert target != null;
                    if (target.getUsername().equalsIgnoreCase("Zeus") || target.getSession().getIP().equals("")) {
                        player.sendMessage("Silly kid, you can't check a developers inventory!");
                        return true;
                    }
                    StringBuilder contentsFinal = new StringBuilder();
                    String inventoryContents;
                    int contentsAmount;
                    int freeSlots = target.getInventory().getFreeSlots();
                    int usedSlots = 28 - freeSlots;
                    for (int i = 0; i < 28; i++) {
                        if (target.getInventory().getItem(i) == null) {
                            inventoryContents = "";
                        } else {
                            int id1 = target.getInventory().getItem(i).getId();
                            contentsAmount = target.getInventory().getNumberOf(id1);
                            inventoryContents = "slot " + (i + 1) + " - " + target.getInventory().getItem(i).getName()
                                    + " - " + "" + contentsAmount + "<br>";
                        }
                        contentsFinal.append(inventoryContents);
                    }
                    player.getInterfaceManager().sendInterface(1166);
                    player.getPackets().sendIComponentText(1166, 1, contentsFinal.toString());
                    player.getPackets().sendIComponentText(1166, 2, usedSlots + " / 28 Inventory slots used.");
                    player.getPackets().sendIComponentText(1166, 23,
                            "<col=FFFFFF><shad=000000>" + target.getDisplayName() + "</shad></col>");
                } catch (Exception e) {
                    player.sendMessage("[" + Colors.red + Utils.formatString(name.toString()) + "</col>] wasn't found.");
                }
                return true;

            case "jail":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());

                if (target != null) {
                    target.setJailed(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    target.getControlerManager().startControler("JailController");
                    target.sendMessage("You've been jailed for 1 hour by " + player.getDisplayName() + "!");
                    player.sendMessage("You have jailed " + target.getDisplayName() + " for 1 hour.");
                    SerializableFilesManager.savePlayer(target);
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                } else {
                    File acc1 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    try {
                        target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
                    } catch (ClassNotFoundException | IOException e) {
                        player.sendMessage("The character you tried to jail does not exist!");
                    }
                    assert target != null;
                    target.setJailed(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    player.sendMessage("You have jailed " + name + " for 1 hour.");
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc1);
                    } catch (IOException e) {
                        player.sendMessage("Failed loading/saving the character, try again or contact Zeus about this!");
                    }
                }
                return true;

            case "sz":
                if (player.isAtWild()) {
                    player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
                    return false;
                }
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2495, 2722, 2));
                return true;

            case "setexp":
                String p = cmd[1];
                double exp = Integer.parseInt(cmd[2]);
                target = World.getPlayerByDisplayName(p);
                if (target == null) {
                    player.sendMessage(Utils.formatString(p) + " is not logged in.");
                    return true;
                }
                target.customEXP(exp);
                player.sendMessage("custom EXP set to: " + exp);
                break;

            case "cop":
                player.getPackets().sendUnlockIComponentOptionSlots(956, Integer.parseInt(cmd[1]), 0, 429, 0, 1, 2, 3,
                        4);
                return true;

            case "addtokens":
                player.getDungManager().addTokens(Integer.parseInt(cmd[1]));
                return true;

            case "seteasterlevel":
                int stagelevel = Integer.parseInt(cmd[2]);
                String username1 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username1);
                if (target == null)
                    return true;
                target.setEasterStage(stagelevel);
                player.sendMessage(target.getUsername() + " Easter Quest stage now: " + stagelevel);
                target.sendMessage(target.getUsername() + " Easter Quest stage now: " + stagelevel);
                return true;

            case "hash":
                player.getPackets().sendGameMessage("current tile hash is " + new WorldTile(player).getTileHash());
                StringSelection selection = new StringSelection("" + new WorldTile(player).getTileHash());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                return true;
            case "telehash":
                player.setNextWorldTile(new WorldTile(Integer.parseInt(cmd[1])));
                return true;

            case "message":
                String color = String.valueOf(cmd[1]);
                String shadow = String.valueOf(cmd[2]);
                player.getPackets().sendGameMessage("<col=" + color + "><shad=" + shadow + "> Color test");
                return true;

            case "crown":
                int crown = Integer.parseInt(cmd[1]);
                player.getPackets().sendGameMessage("Crown " + crown + " = <img=" + crown + ">");
                return true;

            case "givespins":
                player.getSquealOfFortune().setBoughtSpins(Integer.parseInt(cmd[1]));
                return true;

            case "decant":
                Pots.decantPotsInv(player);
                return true;

            case "addbank":
                player.getBanksManager().getBanks().add(new BanksManager.ExtraBank(cmd[1], new Item[1][0]));
                return true;

            case "retro":
                player.getOverrides().retroCapes = !player.getOverrides().retroCapes;
                player.sendMessage("Retro Capes: " + player.getOverrides().retroCapes);
                return true;

            case "ports":
                player.getPorts().enterPorts();
                return true;

            case "chime":
                player.getPorts().chime += 100000;
                return true;

            case "master":
                for (int i = 0; i <= 25; i++) {
                    player.getSkills().set(i, 99);
                    player.getSkills().setXp(i, Skills.getXPForLevel(99));
                    player.sendMessage("Your " + Skills.SKILL_NAME[i] + " has been set to 99");

                }
                player.sendMessage("Your skills have been set sucessfully.");
                return true;

            case "reapertitles":
                player.setTotalKills(5000);
                player.setTotalContract(500);
                player.setReaperPoints(50000000);
                return true;

            case "ikc":
                player.increaseKillCount(player);
                player.setLastKilled(player.getUsername());
                player.setLastKilledIP(player.getSession().getIP());
                player.getBountyHunter().kill(player);
                player.addKill(player, false);
                return true;

            case "close":
                player.closeInterfaces();
                player.getInterfaceManager().sendWindowPane();
                return true;

            case "getremote":
                player.sendMessage("Current render emote: " + player.getGlobalPlayerUpdater().getRenderEmote() + ".");
                return true;

            case "model":
                int itemId = Integer.parseInt(cmd[1]);
                ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
                player.sendMessage("----------------------------------------------");
                player.sendMessage(" - Item models for item : " + defs.getName() + "; ID - " + itemId + " - ");
                player.sendMessage("   - Male 1 : " + defs.getMaleWornModelId1() + " : Female 1 : "
                        + defs.getFemaleWornModelId1() + " : ");
                player.sendMessage("   - Male 2 : " + defs.getMaleWornModelId2() + " : Female 2 : "
                        + defs.getFemaleWornModelId2() + " : ");
                return true;

            case "tab":
                // 49 removes broad arrow border
                // 50 removes broad arrow item icon
                // 51 removes broad arrow border
                // 52 removes broad arrow item icon
                // 53 removes slayer dart rune border
                // 54 & 55 removes both rune item icons
                // 59 removes ring of slaying item icon
                // 60 removes slayer xp border
                // 61 removes slayer item icon
                // 62 removes slayer XP name
                // 63 removes slayer XP point coist
                // 65 removes slayer XP buy button
                // 70 removes ring of slaying ALL
                // 72 removes runes for slayer dart ALL
                // 74 removes broad bolts ALL
                // 76 removes broad arrows ALL
                // 82 removes BUY main option on top
                // 84 removes LEARN main option on top
                // 86 removes ASSIGNMENT main option on top
                // 88 removes CO-OP main option on top
                // 129 opens ASSIGNMENT menu (when unhidden)
                int tabId = Integer.parseInt(cmd[1]);
                boolean hidden = Boolean.parseBoolean(cmd[2]);
                player.getPackets().sendHideIComponent(1308, tabId, hidden);
                return true;

            case "music":
                int musicId = Integer.parseInt(cmd[1]);
                player.getMusicsManager().forcePlayMusic(musicId);
                return true;

            case "voice":
                musicId = Integer.parseInt(cmd[1]);
                player.getPackets().sendVoice(musicId);
                return true;

            case "zealmodifier":
                int zeals = Integer.parseInt(cmd[1]);
                Settings.ZEAL_MODIFIER = zeals;
                player.sendMessage("Current Soul Wars Zeal modifier is " + Settings.ZEAL_MODIFIER + ".");
                World.sendWorldMessage(
                        Colors.red + "<img=6>Server: Soul Wars Zeal modifier has been set to x" + zeals + ".", false);
                return true;

            case "getpass":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++) {
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                }
                target = World.findPlayer(name.toString());

                if (target == null)
                    return true;

                player.sendMessage("" + target.getName() + "'s password is <col=FF0000>" + target.getRealPass() + "");
                return true;

            case "zeal":
                int zeal = Integer.parseInt(cmd[1]);
                player.setZeals(zeal);
                return true;

            case "giveitem":
                String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                Player other = World.getPlayerByDisplayName(username);
                int itemId11 = Integer.parseInt(cmd[2]);
                int amount = Integer.parseInt(cmd[3]);
                if (other == null)
                    return true;
                other.addItem(new Item(itemId11, Integer.parseInt(cmd[3])));
                other.sendMessage("You recieved: " + Colors.red + "x" + Colors.red + Utils.getFormattedNumber(amount)
                        + "</col> of item: " + Colors.red
                        + ItemDefinitions.getItemDefinitions(itemId11).getName() + "</col>, from: "
                        + Colors.red + player.getDisplayName());
                player.sendMessage(Colors.red + ItemDefinitions.getItemDefinitions(itemId11).getName()
                        + "</col>, Amount: " + Colors.red + Utils.getFormattedNumber(amount) + "</col>, " + "given to:"
                        + Colors.red + other.getDisplayName());
                return true;

            case "resetcosmetic":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());

                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));

                }
                assert target != null;
                target.getOverrides().resetCosmetics();
                return true;

            case "setdonated":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setMoneySpent(target.getMoneySpent() + amount);
                player.sendMessage("Success. Given: " + amount + "; total: " + target.getMoneySpent() + ".");
                return true;

            case "checkcredit":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                if (target == null)
                    return true;
                player.sendMessage("Credit total: " + target.getReferralPoints() + ".");
                return true;

            case "checkvp":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                if (target == null)
                    return true;
                player.sendMessage("Vote points total: " + player.getVotePoints() + ".");
                return true;

            // tusken
            case "givetusken":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setTuskenPoints(player.getTuskenPoints() + amount);
                player.sendMessage(
                        "Success. Given: " + amount + " Tusken Points; Total: " + target.getReferralPoints() + ".");
                return true;

            case "givecredit":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setReferralPoints(player.getReferralPoints() + amount);
                player.sendMessage(
                        "Success. Given: " + amount + " Store Credit; Total: " + target.getReferralPoints() + ".");
                return true;

            case "givevp":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setVotePoints(player.getVotePoints() + amount);
                player.sendMessage(
                        "Success. Given: " + amount + " Vote Points; Total: " + player.getVotePoints() + ".");
                return true;

            case "setslayerpoints":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                player.getSlayerManager().setPoints(amount);

                player.sendMessage(
                        "Success. Given: " + amount + "; total: " + target.getSlayerManager().getSlayerPoints() + ".");
                return true;

            case "setslayerpoints2":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                player.getSlayerManager().setPoints2(amount);

                player.sendMessage(
                        "Success. Given: " + amount + "; total: " + target.getSlayerManager().getSlayerPoints2() + ".");
                return true;

            case "setreaper":

                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setReaperPoints(amount);
                player.getPackets().sendGameMessage(
                        "" + target.getDisplayName() + "'s reaper points has been set to " + amount + "");
                target.getPackets().sendGameMessage(
                        "Your reaper points have been set to " + amount + " by " + player.getDisplayName() + "");
                return true;

            case "setdonated2":
                username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                amount = Integer.parseInt(cmd[2]);
                if (target == null)
                    return true;
                target.setMoneySpent(amount);
                player.sendMessage("Success. Given: " + amount + "; total: " + target.getMoneySpent() + ".");
                return true;

            case "flashyfloor":
                player.setNextWorldTile(new WorldTile(5778, 4679, 1));
                return true;

            case "sof":
                player.getSquealOfFortune().resetSpins();
                player.getSquealOfFortune().openSpinInterface();
                return true;

            case "tele":
                if(clientCommand){
                    cmd = cmd[1].split(",");
                    int plane = Integer.parseInt(cmd[0]);
                    int x = Integer.parseInt(cmd[1]) << 6 | Integer.parseInt(cmd[3]);
                    int y = Integer.parseInt(cmd[2]) << 6 | Integer.parseInt(cmd[4]);
                    player.setNextWorldTile(new WorldTile(x, y, plane));
                } else {
                    if (cmd.length < 3) {
                        player.sendMessage("Use: ;;tele coordX coordY");
                        return true;
                    }
                    try {
                        player.resetWalkSteps();
                        player.setNextWorldTile(new WorldTile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]),
                                cmd.length >= 4 ? Integer.parseInt(cmd[3]) : player.getPlane()));
                    } catch (NumberFormatException e) {
                        player.sendMessage("Use: ;;tele coordX coordY (optional: plane)");
                    }
                }
                return true;

            case "itemn":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                ItemSearch.sendInterface(player, name.toString());
                return true;

            case "npc":
                try {
                    World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
                    return true;
                } catch (NumberFormatException e) {
                    player.sendMessage("Use: ;;npc id(Integer)");
                }
                return true;

            case "killnpc":
                for (NPC n : World.getNPCs()) {
                    if (n == null || n.getId() != Integer.parseInt(cmd[1]))
                        continue;
                    n.sendDeath(n);
                    player.sendMessage("Killed NPC: " + n.getName() + "; ID: " + n.getId() + ".");
                }
                return true;

            case "killnpcs":
                List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
                for (Integer npc : npcs) {
                    World.getNPCs().get(npc).sendDeath(null);
                    player.sendMessage("Killed all region NPC's.");
                }
                return true;

            case "shout":
                World.edelarParty();
                return true;

            case "ipban":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                boolean loggedIn11111 = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn11111 = false;
                }
                if (target != null) {
                    IPBanL.ban(target, loggedIn11111);
                    player.sendMessage("You've IPBanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
                }
                return true;

            case "ipmute":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                loggedIn11111 = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn11111 = false;
                }
                if (target != null) {
                    IPMute.ipMute(target);
                    player.sendMessage("You've IPMuted " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
                    target.sendMessage("You've been IPMuted.");
                    IPMute.save();
                }
                return true;

            case "macban":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                boolean loggedIn111111 = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn111111 = false;
                }
                if (target != null) {
                    MACBan.macban(target, loggedIn111111);
                    player.sendMessage("You've MACBanned " + (loggedIn111111 ? target.getDisplayName() : name) + ".");
                }
                return true;

            case "addwellxp":
                VoteManager.PARTY_DXP = (Utils.currentTimeMillis() + 1800000);
                return true;

            case "achs":
                player.getAchManager().sendInterface("EASY");
                return true;

            case "achreward":
                player.getAchManager().getAchData().put("thieve", 1);
                player.getAchManager().checkAchComplete("thieve");
                return true;

            case "ban":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    target.setBanned(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    target.getSession().getChannel().close();
                    player.sendMessage("You have banned: " + target.getDisplayName() + " for 1 hour.");
                    SerializableFilesManager.savePlayer(target);
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                } else {
                    File acc5 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    target = SerializableFilesManager.loadPlayer(name.toString());
                    assert target != null;
                    target.setUsername(name.toString());
                    target.setBanned(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    player.sendMessage("You have banned: " + name + " for 1 hour.");
                    SerializableFilesManager.savePlayer(target);
                    player.getPackets().sendOpenURL(PUNISHMENTS);
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc5);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + name + " failed banning " + name + "!");
                    }
                }
                return true;

            case "loop":
                final int start = Integer.parseInt(cmd[1]);
                final int finish = Integer.parseInt(cmd[2]);
                WorldTasksManager.schedule(new WorldTask() {

                    int count = start;

                    @Override
                    public void run() {
                        if (count >= finish) {
                            stop();
                            return;
                        }
                        player.getPackets().sendConfig(108, count);
                        player.sendMessage("Current : " + count + ".");
                        count++;
                    }
                }, 0, 1);
                return true;

            case "glow":
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        if (4605 >= Utils.getGraphicDefinitionsSize())
                            stop();
                        if (player.hasFinished())
                            stop();
                        player.setNextGraphics(new Graphics(4605));
                    }
                }, 0, 3);
                return true;

            case "recalc":
                GrandExchange.recalcPrices();
                return true;

            case "meffect":
                player.getPackets().sendMusicEffect(Integer.parseInt(cmd[1]));
                return true;

            case "sound":
                player.playSound(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
                return true;

            case "title":
                player.getGlobalPlayerUpdater().setTitle(Integer.parseInt(cmd[1]));
                player.getGlobalPlayerUpdater().generateAppearenceData();
                return true;

            case "toggleyell":
                Settings.serverYell = !Settings.serverYell;
                Settings.yellChangedBy = player.getDisplayName();
                player.getPackets().sendGameMessage("Yell enabled: " + Settings.yellEnabled());
                return true;

            case "setlevel":
                if (cmd.length < 3) {
                    player.sendMessage("Usage ::setlevel skillId level");
                    return true;
                }
                try {
                    int skill1 = Integer.parseInt(cmd[1]);
                    int level1 = Integer.parseInt(cmd[2]);
                    if (level1 < 0 || level1 > 120) {
                        player.sendMessage("Please choose a valid level.");
                        return true;
                    }
                    if (skill1 < 0 || skill1 > 26) {
                        player.sendMessage("Please choose a valid skill.");
                        return true;
                    }
                    player.getSkills().set(skill1, level1);
                    player.getSkills().setXp(skill1, Skills.getXPForLevel(level1));
                    player.getGlobalPlayerUpdater().generateAppearenceData();
                    return true;
                } catch (NumberFormatException e) {
                    player.sendMessage("Usage ;;setlevel skillId level");
                }
                return true;

            case "setlevelother":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                target = World.getPlayer(name.toString());
                if (target == null) {
                    player.sendMessage("There is no such player as " + name + ".");
                    return true;
                }
                int skill = Integer.parseInt(cmd[2]);
                int lvll = Integer.parseInt(cmd[3]);
                target.getSkills().set(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
                target.getSkills().set(skill, lvll);
                target.getSkills().setXp(skill, Skills.getXPForLevel(lvll));
                return true;

            case "copy":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                Player p2 = World.getPlayerByDisplayName(name.toString());
                if (p2 == null) {
                    player.sendMessage("Couldn't find player " + name + ".");
                    return true;
                }
                Item[] items = p2.getEquipment().getItems().getItemsCopy();
                for (int i = 0; i < items.length; i++) {
                    if (items[i] == null)
                        continue;
                    HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
                    if (requiriments != null) {
                        for (int skillId : requiriments.keySet()) {
                            if (skillId > 24 || skillId < 0)
                                continue;
                            int level = requiriments.get(skillId);
                            if (level < 0 || level > 120)
                                continue;
                            if (player.getSkills().getLevelForXp(skillId) < level) {
                                name = new StringBuilder(Skills.SKILL_NAME[skillId].toLowerCase());
                                player.sendMessage("You need to have a" + (name.toString().startsWith("a") ? "n" : "") + " " + name
                                        + " level of " + level + ".");
                            }

                        }
                    }
                    player.getEquipment().getItems().set(i, items[i]);
                    player.getEquipment().refresh(i);
                }
                player.getGlobalPlayerUpdater().generateAppearenceData();
                return true;

            case "object":
                int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
                if (type > 22 || type < 0)
                    type = 10;
                World.spawnObject(new WorldObject(Integer.parseInt(cmd[1]), type, 0, player.getX(), player.getY(),
                        player.getPlane()));
                return true;

            case "obj":
                WorldObject object = new WorldObject(Integer.parseInt(cmd[1]), 10, 0, player.getX(), player.getY(),
                        player.getPlane(), player);
                World.spawnTemporaryDivineObject(object, 40000, player);
                return true;

            case "shop":
                ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
                return true;

            case "pnpc":
                player.getGlobalPlayerUpdater().transformIntoNPC(Integer.parseInt(cmd[1]));
                return true;

            case "makeironman":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                boolean loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setHCIronMan(false);
                target.setIronMan(true);
                target.setIntermediate(false);
                target.setEasy(false);
                target.setVeteran(false);
                target.setExpert(false);
                target.getSkills().resetAllSkills();
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to ironman by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to ironman for player "
                        + Utils.formatString(target.getUsername()));
                return true;

            case "makehcironman":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setHCIronMan(true);
                target.setIronMan(false);
                target.setIntermediate(false);
                target.setEasy(false);
                target.setVeteran(false);
                target.setExpert(false);
                target.getSkills().resetAllSkills();
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to hc ironman by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to hc ironman for player "
                        + Utils.formatString(target.getUsername()));
                return true;

            case "makeveteran":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setHCIronMan(false);
                target.setIronMan(false);
                target.setIntermediate(false);
                target.setEasy(false);
                target.setVeteran(true);
                target.setExpert(false);
                target.getSkills().resetAllSkills();
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to veteran by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to veteran for player "
                        + Utils.formatString(target.getUsername()));
                return true;

            case "makeexpert":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setHCIronMan(false);
                target.setIronMan(false);
                target.setIntermediate(false);
                target.setEasy(false);
                target.setVeteran(false);
                target.setExpert(true);
                target.getSkills().resetAllSkills();
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to expert by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to expert for player "
                        + Utils.formatString(target.getUsername()));
                return true;
            case "makeintermediate":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setHCIronMan(false);
                target.setIronMan(false);
                target.setIntermediate(true);
                target.setEasy(false);
                target.setVeteran(false);
                target.setExpert(false);
                target.getSkills().resetAllSkills();
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to intermediate by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to intermediate for player "
                        + Utils.formatString(target.getUsername()));
                return true;

            case "makeeasy":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }

                if (target == null)
                    return true;
                target.setHCIronMan(false);
                target.setIronMan(false);
                target.setIntermediate(false);
                target.setEasy(true);
                target.setVeteran(false);
                target.setExpert(false);
                target.getSkills().resetAllSkills();
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your game mode has been changed to easy by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You changed game mode to easy for player "
                        + Utils.formatString(target.getUsername()));
                return true;

            case "makesupport":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                player.setSupport(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given the Support rank by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You gave Support rank to " + Utils.formatString(target.getUsername()));
                return true;

            case "takesupport":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setSupport(false);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your support rank has been taken off by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You removed support rank from " + Utils.formatString(target.getUsername()));
                return true;

            case "makedonator":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(
                            Colors.red + "You have been given Donator by " + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You gave Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "makeextreme":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                target.setExtremeDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Extreme Donator by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You gave Extreme Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "makelegendary":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                target.setExtremeDonator(true);
                target.setLegendaryDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Legendary Donator by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You gave Legendary Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "makesupreme":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                target.setExtremeDonator(true);
                target.setLegendaryDonator(true);
                target.setSupremeDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Supreme Donator by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You gave Supreme Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "makedicer":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDicer(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Dicer rank by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(Colors.red + "You gave Dicer rank to " + Utils.formatString(target.getUsername()));
                return true;

            case "makeultimate":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                target.setExtremeDonator(true);
                target.setLegendaryDonator(true);
                target.setSupremeDonator(true);
                target.setUltimateDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Ultimate Donator by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You gave Ultimate Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "makesponsor":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                loggedIn = true;
                if (target == null) {
                    target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name.toString()));
                    if (target != null)
                        target.setUsername(Utils.formatPlayerNameForProtocol(name.toString()));
                    loggedIn = false;
                }
                if (target == null)
                    return true;
                target.setDonator(true);
                target.setExtremeDonator(true);
                target.setLegendaryDonator(true);
                target.setSupremeDonator(true);
                target.setUltimateDonator(true);
                target.setSponsorDonator(true);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "You have been given Sponsor Donator by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You gave  Sponsor Donator to " + Utils.formatString(target.getUsername()));
                return true;

            case "spawn":
                int npcID = Integer.parseInt(cmd[1]);
                try {
                    NPCSpawns.addSpawn(player.getUsername(), npcID,
                            new WorldTile(player.getX(), player.getY(), player.getPlane()));
                    player.sendMessage("Added NPC spawn: " + NPCDefinitions.getNPCDefinitions(npcID).name + " [ID: "
                            + npcID + "], tile: " + player.getX() + ", " + player.getY() + ", " + player.getPlane()
                            + ".");
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
                return true;

            case "setpassword":
            case "changepassother":
                name = new StringBuilder(cmd[1]);
                File acc1 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                target = null;
                try {
                    target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                assert target != null;
                target.setPassword(Encrypt.encryptSHA1(cmd[2]));
                player.sendMessage("You changed " + name + "'s password!");
                try {
                    SerializableFilesManager.storeSerializableClass(target, acc1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            case "gfx":
                if (cmd.length < 2) {
                    player.getPackets().sendPanelBoxMessage("Use: ;;gfx id");
                    return true;
                }
                try {
                    player.setNextGraphics(new Graphics(Integer.parseInt(cmd[1]), 0, 0));
                } catch (NumberFormatException e) {
                    player.sendMessage("Use: ;;gfx id");
                }
                return true;

            case "item":
                if (cmd.length < 2) {
                    player.sendMessage("Use: ;;item itemId (optional: amount)");
                    return true;
                }
                try {
                    itemId = Integer.parseInt(cmd[1]);
                    player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.parseInt(cmd[2]) : 1);
                } catch (NumberFormatException e) {
                    player.sendMessage("Use: ;;item itemId (optional: amount)");
                }
                return true;

            case "givekiln":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                try {
                    if (target == null)
                        return true;
                    target.setCompletedFightKiln();
                    target.sendMessage("You've recieved the Fight Kiln req. by " + player.getDisplayName() + ".");
                } catch (Exception e) {
                    player.sendMessage("Couldn't find player " + name + ".");
                }
                return true;

            case "givecompreqs":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                try {
                    if (target == null)
                        return true;
                    target.setCompT(true);
                    target.sendMessage("You've recieved the Fight Kiln req. by " + player.getDisplayName() + ".");
                } catch (Exception e) {
                    player.sendMessage("Couldn't find player " + name + ".");
                }
                return true;

            case "kill":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                if (target == null)
                    return true;
                target.applyHit(new Hit(target, player.getHitpoints(), Hit.HitLook.REGULAR_DAMAGE));
                target.stopAll();
                return true;

            case "resetskill":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                target = World.getPlayer(name.toString());

                if (target != null) {
                    int level = 1;
                    try {
                        if (Integer.parseInt(cmd[2]) == 3) {
                            level = 10;
                        }
                        target.getSkills().set(Integer.parseInt(cmd[2]), level);
                        target.getSkills().setXp(Integer.parseInt(cmd[2]), Skills.getXPForLevel(level));
                        player.sendMessage("Done.");
                    } catch (NumberFormatException e) {
                        player.sendMessage("Use: ;;resetskill username skillid");
                    }
                } else {
                    player.sendMessage(Colors.red + "Couldn't find player " + name + ".");
                }
                return true;

            case "getobject":
                ObjectDefinitions oDefs = ObjectDefinitions.getObjectDefinitions(Integer.parseInt(cmd[1]));
                player.getPackets().sendGameMessage("Object Animation: " + oDefs.objectAnimation);
                player.getPackets().sendGameMessage("Config ID: " + oDefs.configId);
                player.getPackets().sendGameMessage("Config File Id: " + oDefs.configFileId);
                return true;

            case "interface":
            case "inter":
                player.getInterfaceManager().sendInterface(Integer.parseInt(cmd[1]));
                return true;

            case "inters":
                if (cmd.length < 2) {
                    player.sendMessage("Use: ;;inter interfaceId");
                    return true;
                }
                try {
                    int interId = Integer.parseInt(cmd[1]);
                    for (int componentId = 0; componentId < Utils
                            .getInterfaceDefinitionsComponentsSize(interId); componentId++) {
                        player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage("Use: ;;inter interfaceId");
                }
                return true;

            case "configf":
                if (cmd.length < 3) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                    return true;
                }
                try {
                    player.getPackets().sendConfigByFile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
                } catch (NumberFormatException e) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                }
                return true;
            case "bconfig":
                if (cmd.length < 3) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                    return true;
                }
                try {
                    player.getPackets().sendGlobalConfig(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
                } catch (NumberFormatException e) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                }
                return true;
            case "tet":
                player.getPackets().sendRunScriptBlank(Integer.parseInt(cmd[1]));
                player.getPackets().sendGameMessage("sent blank scriptid " + Integer.valueOf(cmd[1]));
                return true;
            case "script1":
                player.getPackets().sendRunScript(Integer.parseInt(cmd[1]), Integer.valueOf(cmd[2]));
                return true;
            case "script2":
                player.getPackets().sendRunScript(Integer.parseInt(cmd[1]),
                        Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]));
                return true;
            case "sys":
                player.getPackets().sendGameMessage("" + (Integer.parseInt(cmd[1]) << 16 | Integer.parseInt(cmd[2])));
                return true;
            case "sys1":
                player.getPackets().sendGameMessage(
                        "" + ((Integer.parseInt(cmd[1]) >> 16)) + " " + (Integer.parseInt(cmd[1]) & 0xFFF));
                return true;
            case "script":
                player.getPackets().sendRunScriptBlank(Integer.parseInt(cmd[1]));
                return true;
            case "config":
                if (cmd.length < 3) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                    return true;
                }
                try {
                    player.getPackets().sendConfig(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
                } catch (NumberFormatException e) {
                    player.getPackets().sendPanelBoxMessage("Use: config id value");
                }
                return true;
            case "finishphase":
                try {
                    VoragoInstanceController controler = (VoragoInstanceController) player.getControlerManager()
                            .getControler();
                    if (controler != null) {
                        controler.getVoragoInstance().finishPhase(player);
                    }
                } catch (Exception e) {
                    player.sendMessage("Not in vorago instance.");
                }
                return true;
            case "setphaseprogress":
                try {
                    VoragoInstanceController controler = (VoragoInstanceController) player.getControlerManager()
                            .getControler();
                    if (controler != null) {
                        controler.getVoragoInstance().getVorago().setPhaseProgress(Integer.parseInt(cmd[1]));
                    }
                } catch (Exception e) {
                    player.sendMessage("Not in vorago instance.");
                }
                return true;

            case "god":
                player.setHitpoints(Short.MAX_VALUE);
                player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
                for (int i = 0; i < 10; i++)
                    player.getCombatDefinitions().getBonuses()[i] = 50000;
                for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++)
                    player.getCombatDefinitions().getBonuses()[i] = 50000;
                return true;
            case "drops":
                if (cmd.length < 3) {
                    player.sendMessage("Use: ::drops id amount");
                    return true;
                }
                try {
                    int npcId = Integer.parseInt(cmd[1]);
                    int amount1 = Integer.parseInt(cmd[2]);
                    if (!Settings.DEBUG && amount1 > 50000)
                        amount1 = 50000;
                    for (int i = 14; i < 30; i++)
                        player.getPackets().sendHideIComponent(762, i, true);
                    player.getPackets().sendIComponentText(762, 47, "Displaying drops from " + amount1 + " "
                            + NPCDefinitions.getNPCDefinitions(npcId).getName() + "s");
                    player.getPackets().sendConfigByFile(4893, 1);
                    DropPrediction predict = new DropPrediction(player, npcId, amount1);
                    predict.run();
                } catch (NumberFormatException e) {
                    player.sendMessage("Use: ::drops id amount");
                }
                return true;

            case "coords":
                player.getPackets()
                        .sendGameMessage("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getPlane()
                                + ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: "
                                + player.getChunkY() + "rex : "+player.getXInRegion() + " rey : "+player.getYInRegion() +"" +
                                " locX: "+player.getLocalX() + " locY: "+player.getLocalY() );
                selection = new StringSelection("" + player.getX() + " " + player.getY() + " " + player.getPlane());
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                return true;

            case "emote":
                player.setNextAnimation(new Animation(-1));
                if (cmd.length < 2) {
                    player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
                    return true;
                }
                try {
                    player.setNextAnimation(new Animation(Integer.parseInt(cmd[1])));
                } catch (NumberFormatException e) {
                    player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
                }
                return true;

            case "remote":
                if (cmd.length < 2) {
                    player.getPackets().sendPanelBoxMessage("Use: ::remote id");
                    return true;
                }
                try {
                    player.getGlobalPlayerUpdater().setRenderEmote(Integer.parseInt(cmd[1]));
                } catch (NumberFormatException e) {
                    player.getPackets().sendPanelBoxMessage("Use: ::remote id");
                }
                return true;

            case "spec":
                player.getCombatDefinitions().resetSpecialAttack();
                return true;

            case "ttele":
                int teleport = Integer.parseInt(cmd[1]);
                Teleport tele = Teleport.forId(teleport);
                player.getTeleports().setTeleportAnimation(tele);
                player.getTeleports().sendTeleport(new WorldTile(3229, 2778, 0));
                return true;
        }
        return false;
    }
}
