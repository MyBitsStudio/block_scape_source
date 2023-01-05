package com.rs.game.player.content.commands;

import com.rs.Settings;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.SquealOfFortune;
import com.rs.game.player.content.*;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.game.player.content.dropprediction.DropUtils;
import com.rs.game.player.content.grandExchange.GrandExchange;
import com.rs.game.player.content.grandExchange.Offer;
import com.rs.game.world.controllers.Dungeoneering;
import com.rs.game.player.dialogue.Dialogue;
import com.rs.game.player.dialogue.impl.CasinoEntranceD;
import com.rs.utils.Colors;
import com.rs.utils.Encrypt;
import com.rs.utils.Utils;
import mysql.impl.FoxVote;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayerCommands {

    public static boolean processPlayerCommand(final Player player, String[] cmd) {

        String name;
        Player target;

        switch(cmd[0]){
            case "cashbag":
                /*
                 * if (!(player.getControlerManager().getControler() instanceof
                 * InstancedPVPControler)) {
                 */
                int billion = 1000000000;
                if (player.getInventory().getNumberOf(995) >= billion) {
                    player.getInventory().deleteItem(995, billion);
                    player.sendMessage("You turn your coins into a cash bag worth 1b!");
                    if (!player.getInventory().addItem(27155, 1)) {
                        player.getBank().addItem(27155, 1, true);
                        player.sendMessage("Item added to bank due to insuficient inventory space");
                    }
                } else {
                    player.sendMessage("You need atleast 1B to turn your coins into cash bag!");
                }

                break;

            case "geoffers":
                player.getInterfaceManager().sendInterface(1245);
                player.getPackets().sendRunScript(4017, GrandExchange.getOffers().values().size() + 4);
                for (int i = 0; i < 316; i++) {
                    player.getPackets().sendIComponentText(1245, i, "");
                }
                player.getPackets().sendIComponentText(1245, 330, "<u=FFD700>Grand Exchange Offers List</u>");
                player.getPackets().sendIComponentText(1245, 13, "Here is a list of the offers currently on GE:");
                int offersCount = 0;
                for (Offer offer : GrandExchange.getOffers().values()) {
                    if (offer == null)
                        continue;
                    if (offer.isCompleted())
                        continue;
                    offersCount++;
                    player.getPackets().sendIComponentText(1245, 14 + offersCount,
                            offersCount + ") [" + (offer.isBuying() ? "<col=00ff00>BUYING" : "<col=ff0000>SELLING")
                                    + "]<shad=000000> " + (offer.getAmount() - offer.getTotalAmountSoFar()) + " X "
                                    + offer.getName() + " for " + Utils.getFormattedNumber(offer.getPrice())
                                    + " coins.");
                }
                return true;
            case "itemdrop":
                StringBuilder itemName = new StringBuilder(cmd[1]);
                for (int i = 2; i < cmd.length; i++) {
                    itemName.append(" ").append(cmd[i]);
                }
                DropUtils.sendItemDrops(player, itemName.toString());
                return true;

            case "referralrewards":
            case "rr":
                player.getDialogueManager().startDialogue("ReferralDonationD");
                return true;

            case "opengg":
            case "opengearpresets":
                if (!player.canSpawn()) {
                    player.getPackets().sendGameMessage("You can't use that here.");
                    return false;
                }
                if (player.getPerks().isUnlocked(Perks.BANK.getId())) {
                    if (!player.canSpawn()) {
                        player.sendMessage("You cannot open this without bank command perk.");
                        return false;

                    }
                    if (CasinoEntranceD.CasinoArea(player, player)) {
                        player.getPackets().sendGameMessage(Colors.red + "You cannot use preset command in the casino");
                        return false;
                    }
                    if (player.isLocked()) {
                        player.sendMessage("You can't open preset at the moment, please wait.");
                        return false;
                    }
                    if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
                        player.sendMessage("You can't open preset while you're in this area.");
                        return false;
                    }
                    if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
                        player.sendMessage("You can't open preset 15 seconds after combat, please wait.");
                        return true;
                    }

                    player.getDialogueManager().startDialogue("GearPresetsD");
                    return true;
                } else
                    player.sendMessage(
                            "You have to purchase the Bank Command perk in order to use perk preset anywhere this.");

                return true;

            case "timerinter":
                BossTimerManager.sendInterface(player, 0);
                return true;
            case "spin":
                try {
                    int amount = Integer.parseInt(cmd[1]);
                    if (player.getSquealOfFortune().getTotalSpins() < amount) {
                        player.getPackets().sendGameMessage("You don't have enough spins to do that.");
                        return true;
                    }
                    Item[] rewards;
                    int jackpotSlot;
                    for (int j = 0; j < amount; j++) {
                        jackpotSlot = Utils.random(13);
                        rewards = new Item[13];
                        for (int i = 0; i < rewards.length; i++)
                            rewards[i] = SquealOfFortune.generateReward(player.getSquealOfFortune().getNextSpinType(),
                                    player.getSquealOfFortune().getSlotRarity(i, jackpotSlot));
                        if (!player.getBank().hasBankSpace() || !player.getSquealOfFortune().useSpin())
                            return false;
                        int rewardRarity = SquealOfFortune.RARITY_COMMON;
                        double roll = Utils.randomDouble();
                        if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_JACKPOT])
                            rewardRarity = SquealOfFortune.RARITY_JACKPOT;
                        else if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_RARE])
                            rewardRarity = SquealOfFortune.RARITY_RARE;
                        else if (roll <= Settings.SOF_CHANCES[SquealOfFortune.RARITY_UNCOMMON])
                            rewardRarity = SquealOfFortune.RARITY_UNCOMMON;
                        int[] possibleSlots = new int[13];
                        int possibleSlotsCount = 0;
                        for (int i = 0; i < 13; i++) {
                            if (player.getSquealOfFortune().getSlotRarity(i, jackpotSlot) == rewardRarity)
                                possibleSlots[possibleSlotsCount++] = i;
                        }
                        int rewardSlot = possibleSlots[Utils.random(possibleSlotsCount)];
                        Item reward = rewards[rewardSlot];

                        if (rewardRarity >= SquealOfFortune.RARITY_JACKPOT) {
                            String message = "News: " + player.getDisplayName() + " has just won " + "x"
                                    + Utils.getFormattedNumber(reward.getAmount()) + " of " + reward.getName()
                                    + " on Squeal of Fortune";
                            World.sendWorldMessage(Colors.orange + "<img=7>" + message + "!", false);
                        }
                        if (reward.getDefinitions().isNoted())
                            reward.setId(reward.getDefinitions().getCertId());
                        if (reward.getId() == 30372)
                            reward.setAmount(Utils.random(15, 250));
                        player.getBank().addItem(reward.getId(), rewards[rewardSlot].getAmount(), true);
                        player.getPackets()
                                .sendGameMessage("Congratulations, x" + Utils.getFormattedNumber(reward.getAmount())
                                        + " of " + reward.getName() + " has been added to your bank.");
                    }
                } catch (Exception e) {
                    player.getPackets().sendGameMessage("Usage ::spin amount");
                    return true;
                }
                return true;

            case "gaze":
                player.getInterfaceManager().gazeOrbOfOculus();
                return true;

            case "managebanks":
            case "mb":
                player.getDialogueManager().startDialogue("BanksManagerD");
                return true;

            case "petlootmanager":
            case "plm":
                player.getDialogueManager().startDialogue("PetLootManagerD");
                return true;

            case "npcdrop":
                StringBuilder npcNameSB = new StringBuilder(cmd[1]);
                if (cmd.length > 1) {
                    for (int i = 2; i < cmd.length; i++) {
                        npcNameSB.append(" ").append(cmd[i]);
                    }
                }
                DropUtils.sendNPCDrops(player, npcNameSB.toString());
                return true;
            case "reward":

                try {
                    if (Integer.parseInt(cmd[1]) != 1) {
                        player.sendMessage("This id has no reward.");
                        return false;
                    }
                    player.sendMessage(Colors.red + "The command to claim the votes is now ;;voted");
                } catch (Exception e) {
                    player.sendMessage(
                            ("Our API services are currently offline. We are working on bringing it back up."));
                    e.printStackTrace();
                }
                return true;
            case "voted":
                new Thread(new FoxVote(player)).start();
                return true;
            case "train":
                player.getDialogueManager().startDialogue("TrainingTeleport");
                return true;

            case "xplock":
                player.setXpLocked(!player.isXpLocked());
                player.sendMessage("Your experience is now: " + (player.isXpLocked() ? "Locked" : "Unlocked") + ".");
                return true;

            case "cosmetics":
            case "cosmetic":
                player.getDialogueManager().startDialogue("CosmeticsManagersD");
                return true;

            case "bank":
            case "b":

                if (player.getPerks().isUnlocked(Perks.BANK.getId())) {
                    if (!player.canSpawn()) {
                        player.sendMessage("You cannot open your bank account at the moment.");
                        return false;
                    }
                    if (CasinoEntranceD.CasinoArea(player, player)) {
                        player.getPackets().sendGameMessage(Colors.red + "You cannot use bank command in the casino");
                        return false;
                    }
                    if (Dungeoneering.DragonKinArea(player)) {
                        player.sm("You cannot use bank in Dragonkin Lab");
                        return false;
                    }
                    if (player.isLocked()) {
                        player.sendMessage("You can't bank at the moment, please wait.");
                        return true;
                    }
                    if (!player.canSpawn() || player.getControlerManager().getControler() != null) {
                        player.sendMessage("You can't bank while you're in this area.");
                        return true;
                    }
                    if (player.getAttackedByDelay() + 15000 > Utils.currentTimeMillis()) {
                        player.sendMessage("You can't bank 15 seconds after combat, please wait.");
                        return true;
                    }
                    player.getBank().openPlayerBank(player);
                    return true;
                } else
                    player.sendMessage("You have to purchase the Bank Command perk in order to do this.");
                return true;

            case "vote1":
                player.getPackets().sendOpenURL(Settings.VOTE);
                return true;

            case "guides":
            case "guide":
                player.getPackets().sendOpenURL(Settings.GUIDES);
                return true;

            case "update":
            case "updates":
                player.getPackets().sendOpenURL(Settings.UPDATES);
                return true;

            /*
             * case "hiscores": case "highscores": case "hs": if
             * (player.getUsername().equals("alber")) {
             * player.sm("You cannot update you highscore!"); return true; }
             * player.getPackets().sendOpenURL(Settings.HISCORES); new Thread(new
             * Highscores(player)).start(); return true;
             */
            case "discord":
                player.getPackets().sendOpenURL(Settings.DISCORD);
                return true;

            case "rules":
                player.getPackets()
                        .sendOpenURL(Settings.FORUM + "/index.php?app=forums&module=forums&controller=topic&id=355");
                return true;

            case "forum":
            case "forums":
                player.getPackets().sendOpenURL(Settings.FORUM);
                return true;
            case "vote":
                if (!player.getUsername().contains("_")) {
                    player.getPackets().sendOpenURL(Settings.VOTE);
                    return true;
                }
                player.getDialogueManager().startDialogue(new Dialogue() {
                    @Override
                    public void start() {
                        sendNPCDialogue(659, HAPPY_FACE,
                                "Note: Players with a space in there names are advised to use an underscore instead for your vote to work properly");
                        stage = 0;
                    }

                    @Override
                    public void run(int interfaceId, int componentId) {
                        if (stage == 0) {
                            finish();
                            player.getPackets().sendOpenURL(Settings.VOTE);
                        }
                    }

                    @Override
                    public void finish() {
                        player.getInterfaceManager().closeChatBoxInterface();
                    }

                });
                return true;

            case "donate":
            case "store":
                if (!player.getUsername().contains("_")) {
                    player.getPackets().sendOpenURL("https://helwyr3.wikia.com/wiki/Perks_system");// ("https://helwyr3.com/forums/index.php?/topic/112-donator-benefits");
                    player.getPackets().sendOpenURL(Settings.DONATE);
                    return true;
                }
                player.getDialogueManager().startDialogue(new Dialogue() {

                    @Override
                    public void start() {
                        sendNPCDialogue(659, HAPPY_FACE,
                                "Note: Players with a space in there names are advised to use an underscore instead for your donation to work properly");
                        stage = 0;
                    }

                    @Override
                    public void run(int interfaceId, int componentId) {
                        switch (stage) {
                            case 0:
                                finish();
                                player.getPackets()
                                        .sendOpenURL("https://helwyr3.org/forums/index.php?/topic/112-donator-benefits");
                                player.getPackets().sendOpenURL(Settings.DONATE);
                                break;

                        }
                    }

                    @Override
                    public void finish() {
                        player.getInterfaceManager().closeChatBoxInterface();
                    }

                });
                return true;

            case "time":
                DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
                        new Locale("en", "EN"));
                String formattedDate = df.format(new Date());
                player.getDialogueManager().startDialogue("SimpleMessage",
                        Settings.SERVER_NAME + "'s time is now: " + formattedDate);
                return true;

            case "ticket":
                TicketSystem.requestTicket(player);
                return true;

            case "empty":
                player.getDialogueManager().startDialogue("EmptyD");
                return true;

            case "market":
                if (player.isAtWild()) {
                    player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
                    return false;
                }

                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2831, 3860, 3));
                return true;

            /*
             * case "party": Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(5888,
             * 4681, 1)); return true;
             */
            case "togglepouch":
                player.togglePouchMessages = !player.togglePouchMessages;
                player.succeedMessage("Money pouch messages: " + player.togglePouchMessages);
                return true;

            case "skillinter":
                player.isDefskill = !player.isDefskill;
                if (player.isDefskill) {
                    player.sm(Colors.red + "[Skilling Inter]</col> You are now using the Default Skilling Interface!");
                } else {
                    player.sm(Colors.red + "[Skilling Inter]</col> You are now using the New Skilling Interface!");
                }
                return true;

            case "prif":
            case "priff":
            case "prifd":
            case "priffdin":
            case "priffdinas":
            case "prifddinas":
            case "prifddin":
                if (player.isAtWild()) {
                    player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
                    return false;
                }
                if (player.getPerks().isUnlocked(Perks.ELFFIEND.getId()))
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2213, 3361, 1));
                else {
                    player.sendMessage("Buy the 'Elf Fiend' game perk to access Prifddinas through this command.");
                    player.sendMessage(
                            "Optionally you can get a total level of 2250+ and speak to Elf Hermit at ;;home.");
                }
                return true;

            case "players":
            case "player":
                player.sendMessage(
                        "There are currently [" + Colors.red + World.getPlayersOnline() + "</col>] players online.");
                World.playersList(player);
                return true;

            case "settings":
                if (player.getInterfaceManager().containsScreenInter())
                    player.getInterfaceManager().closeScreenInterface();

                if (player.getInterfaceManager().containsChatBoxInter())
                    player.getInterfaceManager().closeChatBoxInterface();

                if (player.getInterfaceManager().containsInventoryInter())
                    player.getInterfaceManager().closeInventoryInterface();

                AccountInterfaceManager.sendInterface(player);
                return true;

            case "titles":
                player.getTitles().openShop();
                return true;

            case "changepass":
                String inputLine = "";
                for (int i = 1; i < cmd.length; i++)
                    inputLine += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                if (inputLine.length() > 15) {
                    player.sendMessage("You cannot set your password with over 15 chars.");
                    return true;
                }
                if (inputLine.length() < 5) {
                    player.sendMessage("You cannot set your password with less than 5 chars.");
                    return true;
                }
                player.setPassword(Encrypt.encryptSHA1(cmd[1]));
                player.sendMessage("You've successfully changed your password! Your new password is " + cmd[1] + ".");
                return true;

            case "yell":
            case "y":
                String inputLine1 = "";
                for (int i = 1; i < cmd.length; i++)
                    inputLine1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
                YellManager.sendYell(player, Utils.fixChatMessage(inputLine1));
                return true;

            case "extras":
            case "perks":
            case "features":
                player.getPerkManager().displayAvailablePerks();
                return true;

        }

        return false;
    }
}
