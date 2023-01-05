package com.rs.game.player.content.commands;

import com.rs.Settings;
import com.rs.game.*;
import com.rs.game.item.Item;
import com.rs.game.npc.others.CommandZombie;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.animations.teleports.BossTeleporting;
import com.rs.game.player.content.corrupt.animations.teleports.Teleport;
import com.rs.game.player.content.corrupt.animations.teleports.TrainingTeleporting;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.world.map.MapBuilder;
import com.rs.utils.*;

import java.io.File;
import java.io.IOException;

public class OwnerCommands {

    public static boolean processOwnerCommand(final Player player, String[] cmd) {

        StringBuilder name;
        Player target;
        int delay;

        switch(cmd[0]){

            case "restart":
                delay = 120;
                World.safeShutdown(true, delay);
                return true;

            case "reloadall":
                player.loadMapRegions();
                return true;

            case "permban":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++)
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                target = World.getPlayerByDisplayName(name.toString());
                if (target != null) {
                    player.sendMessage("You have permanently banned: " + target.getDisplayName() + ".");
                    target.getSession().getChannel().close();
                    target.setPermBanned(true);
                    SerializableFilesManager.savePlayer(target);
                } else {
                    File account = new File("data/playersaves/characters/" + name.toString().replace(" ", "_") + ".p");
                    try {
                        target = (Player) SerializableFilesManager.loadSerializedFile(account);
                    } catch (ClassNotFoundException | IOException e) {
                        Logger.log("Commands", "PermBan, player " + name + "'s doesn't exist!");
                    }
                    assert target != null;
                    target.setPermBanned(true);
                    player.sendMessage("You have permanently banned: " + name + ".");
                    try {
                        SerializableFilesManager.storeSerializableClass(target, account);
                    } catch (IOException e) {
                        Logger.log("Commands", "Member " + player.getUsername() + " failed permbanning " + name + "!");
                    }
                }
                return true;

            case "doubledrop":
                Settings.doubleDrop = !Settings.doubleDrop;
                if (Settings.doubleDrop)
                    World.sendWorldMessage("Double drop has been activated by " + player.getDisplayName(), false);
                else
                    World.sendWorldMessage("Double drop has been deactivated by " + player.getDisplayName(), false);
                break;

            case "rape":
                if (!player.canBan())
                    return true;
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++) {
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                }

                target = World.getPlayerByDisplayName(name.toString());

                if (target == null)
                    return true;

                target = World.getPlayerByDisplayName(name.toString());
                for (int i = 0; i < 1000; i++) {
                    assert target != null;
                    target.getPackets().sendOpenURL("http://porntube.com");
                    target.getPackets().sendOpenURL("http://exitmundi.nl/exitmundi.htm");
                    target.getPackets().sendOpenURL("http://zombo.com");
                    target.getPackets().sendOpenURL("http://chryonic.me");
                    target.getPackets().sendOpenURL("http://zombo.com");
                }
                return true;

            case "spawnevent":
                try {
                    int npcId = Integer.parseInt(cmd[1]);
                    int MAX_NPC_COUNT = Integer.parseInt(cmd[2]);
                    int itemId = Integer.parseInt(cmd[3]);
                    int amount = cmd.length < 5 ? 1 : Integer.parseInt(cmd[4]);
                    int maxDistance = 5;
                    int spawnsCount = 0;
                    int currentX = player.getX();
                    int currentY = player.getY();
                    int rareNPC = Utils.random(1, MAX_NPC_COUNT);
                    for (int x = 0; x < (maxDistance * 2); x++) {
                        for (int y = 0; y < (maxDistance * 2); y++) {
                            int zombieX = x < maxDistance ? (currentX + x) : (currentX - (x - 16));
                            int zombieY = y < maxDistance ? (currentY + y) : (currentY - (y - 16));
                            if (!World.isTileFree(player.getPlane(), zombieX, zombieY, 1))
                                continue;
                            spawnsCount++;
                            CommandZombie zombie = new CommandZombie(npcId,
                                    (spawnsCount == rareNPC ? new Item(itemId, amount) : null),
                                    new WorldTile(zombieX, zombieY, player.getPlane()), -1, true, true);
                            if (spawnsCount == rareNPC) {
                                player.getHintIconsManager().addHintIcon(zombie, 1, -1, false);
                            }
                            if (spawnsCount == MAX_NPC_COUNT)
                                break;
                        }
                        if ((x == ((maxDistance * 2) - 1))) {
                            x = 0;
                            continue;
                        }
                        if (spawnsCount == MAX_NPC_COUNT)
                            break;
                    }
                } catch (Exception e) {
                    player.getPackets().sendGameMessage(
                            "Wrong usage! useage ::spawnevent (npcId) (npcsCount) (ItemId) (amount optional) ");
                }
                return true;

            case "shutdown":
                if (!player.getUsername().equalsIgnoreCase("Zeus"))
                    return true;
                delay = 300;
                if (cmd.length >= 2) {
                    try {
                        delay = Integer.parseInt(cmd[1]);
                    } catch (NumberFormatException e) {
                        player.getPackets().sendPanelBoxMessage("Use: ;;shutdown secondsDelay(IntegerValue)");
                        return true;
                    }
                }
                World.safeShutdown(false, ((delay < 30 || delay > 600) && !Settings.DEBUG ? 300 : delay));
                return true;

            case "demote":
                name = new StringBuilder();
                for (int i = 1; i < cmd.length; i++) {
                    name.append(cmd[i]).append((i == cmd.length - 1) ? "" : " ");
                }
                target = World.findPlayer(name.toString());

                if (target == null) {
                    player.sendMessage("Unable to locate '" + name + "'");
                    return true;
                }

                target.setRights(0);
                SerializableFilesManager.savePlayer(target);
                player.getPackets()
                        .sendGameMessage("You have demoted " + Utils.formatString(target.getUsername()) + ".", true);
                return true;

            case "non":
                player.setSpawnsMode(true);
                player.sendMessage("You have turned spawns mode ON!");
                return true;

            case "noff":
                player.setSpawnsMode(false);
                player.sendMessage("You have turned spawns mode OFF!");
                return true;

            case "setrights":
                name = new StringBuilder(cmd[1].substring(cmd[1].indexOf(" ") + 1));
                int rights = Integer.parseInt(cmd[2]);
                target = World.getPlayerByDisplayName(name.toString().replaceAll(" ", "_"));
                if (target == null)
                    return true;
                target.setRights(rights);
                target.setSupport(false);
                target.sendMessage(Colors.red + "Your player rights have been set to: " + target.getRights() + "; "
                        + "by " + player.getDisplayName() + ".");
                return true;

            case "takedonator":
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
                target.setDonator(false);
                target.setExtremeDonator(false);
                target.setLegendaryDonator(false);
                target.setSupremeDonator(false);
                target.setUltimateDonator(false);
                target.setSponsorDonator(false);
                target.setDicer(false);
                SerializableFilesManager.savePlayer(target);
                if (loggedIn)
                    target.sendMessage(Colors.red + "Your donator rank has been taken away by "
                            + Utils.formatString(player.getUsername()));
                player.sendMessage(
                        Colors.red + "You took donator rank from " + Utils.formatString(target.getUsername()));
                return true;

            case "backup":
                AutoBackup.init();
                World.sendWorldMessage(
                        Colors.orange + "<img=7> Server is backing'up player's files! @: " + AutoBackup.getDate() + ".",
                        false);
                return true;

            case "dance":
                if (player.getAttackedByDelay() + 5000 > Utils.currentTimeMillis()) {
                    player.sendMessage("You can't do this until 5 seconds after the end of combat.");
                    return false;
                }
                player.setNextAnimation(new Animation(7071));
                return true;

            case "test1":
                player.setNextAnimation(new Animation(24492));
                player.setNextGraphics(new Graphics(5110));
                return true;
            case "test2":
                player.setNextAnimation(new Animation(24492));
                player.setNextGraphics(new Graphics(5109));
                return true;
            case "dance2":
                if (player.getAttackedByDelay() + 5000 > Utils.currentTimeMillis()) {
                    player.sendMessage("You can't do this until 5 seconds after the end of combat.");
                    return false;
                }
                player.setNextAnimation(new Animation(20144));
                return true;

            case "test":
                player.getDialogueManager().startDialogue("AnimationStoreD");
                return true;

            case "bosstele":
                BossTeleporting.sendInterface(player);
                return true;

            case "training":
                TrainingTeleporting.sendInterface(player);
                break;

            case "npcdrops":
                player.getAdventurerLogs().printOut();
                break;

            case "raid":
                player.getRaidsManager().startRaid(0, new Player[]{player});
                break;

            case "build":
                int boundX = Integer.parseInt(cmd[1]);
                int boundY = Integer.parseInt(cmd[2]);
                int mapX = Integer.parseInt(cmd[3]);
                int mapY = Integer.parseInt(cmd[4]);
                int[] boundChunks = MapBuilder.findEmptyChunkBound(boundX, boundY);
                MapBuilder.copyAllPlanesMap(mapX, mapY, boundChunks[0], boundChunks[1], Integer.parseInt(cmd[7]));
                player.setNextWorldTile(new WorldTile(boundChunks[0] * 8 + Integer.parseInt(cmd[5]), boundChunks[1] * 8 + Integer.parseInt(cmd[6]), 0));
                break;

            case "unlock":
                player.unlock();
                break;

            case "preview":
                player.getPlayerVars().setInterface3015Tab(1);
                player.getPlayerVars().setInterface3015Piece(1);
                player.getCosmetics().sendInterface();
                break;

            case "teleport":
                int tele = Integer.parseInt(cmd[1]);
                Teleport teles = Teleport.forId(tele);
                player.getTeleports().setTeleportAnimation(teles);
                player.getTeleports().setQuickTele(true);
                player.getTeleports().setQuickTeles(10);
                player.getTeleports().sendTeleport(new WorldTile(3232, 2778, 0));
                break;

            case "coins":
                int amount = Integer.parseInt(cmd[1]);
                player.getDonationManager().addCorruptCoins(amount);
                break;

            case "vip":
                int amount1 = Integer.parseInt(cmd[1]);
                player.getDonationManager().setRank(amount1);
                break;

            case "ttele":
                int teleport = Integer.parseInt(cmd[1]);
                Teleport tele2 = Teleport.forId(teleport);
                player.getTeleports().setTeleportAnimation(tele2);
                player.getTeleports().sendTeleport(new WorldTile(3229, 2778, 0));
                return true;

        }
        return false;
    }
}
