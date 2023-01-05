package com.rs.game.player.content.commands;

import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.TicketSystem;
import com.rs.game.world.controllers.FightCaves;
import com.rs.game.world.controllers.Wilderness;
import com.rs.utils.*;

import java.io.File;
import java.io.IOException;

public class ModeratorCommands {

    public static boolean processModCommand(final Player player, String[] cmd) {

        StringBuilder name;
        Player target;

        switch(cmd[0]){

            case "hide":
                if (Wilderness.isAtWild(player)) {
                    player.getPackets().sendGameMessage("You can't use ::hide here.");
                    return true;
                }
                player.getGlobalPlayerUpdater().switchHidden();
                player.getPackets().sendGameMessage("Am i hidden? " + player.getGlobalPlayerUpdater().isHidden());
                return true;

            case "teletome":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                if (target == null)
                    return true;
                if ( target.getControlerManager().getControler() instanceof FightCaves) {
                    player.sendMessage("You can't teleport someone from a Fight Caves instance.");
                    return true;
                }
                if (target.getControlerManager().getControler() != null/*
                 * && (target.getControlerManager().getControler()
                 * instanceof InstancedPVPControler)
                 */)
                    return true;
                if (target.getGlobalPlayerUpdater().isHidden())
                    return true;
                Magic.sendCrushTeleportSpell(target, 0, 0, new WorldTile(player));
                target.stopAll();
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

            case "teleto":
                if (player.getControlerManager().getControler() != null
                    /*
                     * && (player.getControlerManager().getControler() instanceof
                     * InstancedPVPControler)
                     */)
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
                if (target.getGlobalPlayerUpdater().isHidden())
                    return true;
                Magic.sendCrushTeleportSpell(player, 0, 0, new WorldTile(target));
                player.stopAll();
                return true;

            case "sz":
                if (player.isAtWild()) {
                    player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
                    return false;
                }
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2495, 2722, 2));
                return true;

            case "ticket":
                TicketSystem.answerTicket(player);
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
                } else {
                    File acc5 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    target = SerializableFilesManager.loadPlayer(name.toString());
                    assert target != null;
                    target.setUsername(name.toString());
                    target.setBanned(Utils.currentTimeMillis() + (60 * 60 * 1000));
                    player.sendMessage("You have banned: " + name + " for 1 hour.");
                    SerializableFilesManager.savePlayer(target);
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc5);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + name + " failed banning " + name + "!");
                    }
                }
                return true;

            case "kick":
            case "forcekick":
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

            case "disconnect":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target == null) {
                    player.sendMessage(Utils.formatString(name.toString()) + " is not logged in.");
                    return true;
                }
                target.getSession().getChannel().close();
                Logger.log("Commands", "Player " + player.getDisplayName() + " has closed connection for "
                        + target.getDisplayName() + "!");
                player.sendMessage("You have closed connection channel for player: " + target.getDisplayName() + ".");
                return true;

            case "jail":
                String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
                target = World.getPlayerByDisplayName(username);
                int amount = Integer.parseInt(cmd[2]);

                if (target != null) {
                    target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
                    target.getControlerManager().startControler("JailController");
                    target.sendMessage(
                            "You've been jailed for for " + amount + " hours by " + player.getDisplayName() + "!");
                    player.sendMessage("You have jailed " + target.getDisplayName() + " for " + amount + " hours!");
                    SerializableFilesManager.savePlayer(target);
                } else {
                    File acc1 = new File("data/playersaves/characters/" + username.replace(" ", "_") + ".p");
                    try {
                        target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
                    } catch (ClassNotFoundException | IOException e) {
                        player.sendMessage("The character you tried to jail does not exist!");
                    }
                    assert target != null;
                    target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000 / 24 * amount);
                    player.sendMessage("You have jailed " + target.getUsername() + " for " + amount + " hours!");
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc1);
                    } catch (IOException e) {
                        player.sendMessage("Failed loading/saving the character, try again or contact Zeus about this!");
                    }
                }
                return true;

            case "mute":
                name = new StringBuilder();
                if (!player.canBan())
                    return true;
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
                    player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
                    SerializableFilesManager.savePlayer(target);
                } else {
                    File acc5 = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    try {
                        target = (Player) SerializableFilesManager.loadSerializedFile(acc5);
                    } catch (ClassNotFoundException | IOException e) {
                        Logger.log("Commands", "Mute, " + name + "'s doesn't exist!");
                    }
                    target = SerializableFilesManager.loadPlayer(name.toString());
                    assert target != null;
                    target.setUsername(name.toString());
                    target.setMuted(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
                    player.sendMessage("You have muted: " + target.getDisplayName() + " for 24 hours!");
                    target.sendMessage("You have been muted for 24 hours by " + player.getDisplayName() + "!");
                    SerializableFilesManager.savePlayer(target);
                    try {
                        SerializableFilesManager.storeSerializableClass(target, acc5);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + player.getUsername() + " failed muting " + name + "!");
                    }
                }
                return true;

            case "unmute":
                StringBuilder name1 = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name1.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                Player target1 = World.getPlayerByDisplayName(name1.toString());
                if (target1 != null) {
                    target1.setMuted(0);
                    IPMute.unmute(target1);
                    target1.setPermMuted(false);
                    target1.sendMessage("You've been unmuted by " + player.getDisplayName() + ".");
                    player.sendMessage("You have unmuted: " + target1.getDisplayName() + ".");
                    SerializableFilesManager.savePlayer(target1);
                } else {
                    File acc1 = new File("data/playersaves/characters/" + name1.toString().replace(" ", "_") + ".p");
                    try {
                        target1 = (Player) SerializableFilesManager.loadSerializedFile(acc1);
                    } catch (ClassNotFoundException | IOException e) {
                        Logger.log("Commands", "UnMute, " + name1 + " doesn't exist!");
                    }
                    if (cmd[1].contains(Utils.formatString(name1.toString()))) {
                        player.sendMessage(Colors.red + "You can't unmute yourself!");
                        return true;
                    }
                    assert target1 != null;
                    target1.setMuted(0);
                    IPMute.unmute(target1);
                    target1.setPermMuted(false);
                    player.sendMessage("You have unmuted: " + target1.getUsername() + ".");
                    try {
                        SerializableFilesManager.storeSerializableClass(target1, acc1);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + player.getUsername() + " failed unmuting " + name1 + "!");
                    }
                }
                return true;


        }
        return false;
    }
}
