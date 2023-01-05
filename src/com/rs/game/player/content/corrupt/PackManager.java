package com.rs.game.player.content.corrupt;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.player.Player;
import com.rs.utils.Colors;
import com.rs.utils.Utils;


public class PackManager  {

    /**
     * Packs
     *  43414 - Variety
     *  43399 - Small Currency
     *  43400 - Medium Currency
     *  43401 - Large Currency
     *  43398 - Powerpack
     *  37322 - Resource Pack
     *  36813 - Mini Protean
     *  34023 - Small Protean
     *  34024 - Medium Protean
     *  34025 - Large Protean
     *  34026 - Portable Skilling Pack
     *
     *  Chests
     *  44238 - Smouldering
     *  44237 - Genie
     *  44236 - Rainbow
     *  44235 - Double Dragon God
     *  44234 - Double Dragon Dragon
     *  44233 - Double Dragon Rune
     *  44232 - Double Dragon addy
     *  44231 - God Chest
     *  44230 - Dragon Chest
     *  44229 - Rune Chest
     *  44228 - Addy Chest
     *  44224 - Experience Chest
     *  43261 - Huge Lamp Chest
     *  43278 - Umbral chest
     *  40191 - Antique Chest
     *  40190 - Skilling Chest
     *  40189 - Protean Chest
     *  40188 - Experience Chest (Quad Exp)
     *
     *
     */

    public static int[] packs = { 34026 };
    public static int[] chests = {39596, 39588, 39592, 39590, 40188 };

    public static boolean isHandled(int packId){
        for(Integer pack : packs)
            if(pack == packId)
                return true;

        for(Integer chest : chests)
            if(chest == packId)
                return true;

        return false;
    }

    public static void openPacksAndChests(Player player, int packId){
        switch(packId){
            case 39596:
                player.getGlobalPlayerUpdater().transformIntoNPC(24011);
                player.setNextForceTalk(new ForceTalk("For the glory of Zaros!"));
                World.sendGraphics(player, new Graphics(5028), player);
                player.setNextAnimation(new Animation(26494));
                break;
            case 39588:
                player.getGlobalPlayerUpdater().transformIntoNPC(24010);
                player.setNextForceTalk(new ForceTalk("For the glory of Zaros!"));
                World.sendGraphics(player, new Graphics(5028), player);
                player.setNextAnimation(new Animation(26494));
                break;
            case 39592:
                player.getGlobalPlayerUpdater().transformIntoNPC(24012);
                player.setNextForceTalk(new ForceTalk("For the glory of Zaros!"));
                World.sendGraphics(player, new Graphics(5028), player);
                player.setNextAnimation(new Animation(26494));
                break;
            case 39590:
                player.getGlobalPlayerUpdater().transformIntoNPC(24013);
                player.setNextForceTalk(new ForceTalk("For the glory of Zaros!"));
                World.sendGraphics(player, new Graphics(5028), player);
                player.setNextAnimation(new Animation(26494));
                break;
            case 40188:
                player.getDialogueManager().startDialogue("QuadExp");
                break;
            case 34026:
                player.sm("Coming soon!");
                break;
        }
    }

    public static void openChest(Player player, int packId){

    }

    public static void openDonatorChest(Player player){

    }

    public static void openChristmasPresent(Player player){
        int[] Coal = { 453 };

        int[] Common = { 41517, 41518, 41519, 41520, 41521, 41524, 41525, 39273, 39275, 33593, 33594, 33595, 26493,
                15422, 15423, 15425 };

        int[] Rare = { 26518, 14600, 14602, 14604, 33625, 39265, 39269 };

        int[] Pets = { 41526, 41538, 41559 };

        int[] Legendary = { 36079, 36080 };

        int[] Legendary1 = { 30412 };

        player.getInventory().deleteItem(33610, 1);
        int rarity = Utils.getRandom(1000);

        if (rarity > 0 && rarity <= 250) {
            int length = Coal.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Coal[reward], 1);
            World.sendWorldMessage(
                    "<col=fff000>" + player.getDisplayName() + "Was a Naughty kid and received coal! ", false);
        }

        if (rarity > 250 && rarity <= 750) {
            int length = Common.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Common[reward], 1);
            World.sendWorldMessage(
                    "<col=fff000>" + player.getDisplayName() + " recieved a Common Item from the christmas gift!",
                    false);
        }
        if (rarity > 750 && rarity <= 920) {
            int length = Rare.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Rare[reward], 1);
            World.sendWorldMessage(
                    "<col=ff00ff>" + player.getDisplayName() + " recieved a Rare Item from the christmas gift!",
                    false);
        }
        if (rarity > 920 && rarity <= 980) {
            int length = Pets.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Pets[reward], 1);
            World.sendWorldMessage(
                    "<col=ff00ff>" + player.getDisplayName() + " recieved a PET from the christmas gift!", false);

        }
        if (rarity > 980 && rarity <= 995) {
            int length = Legendary.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Legendary[reward], 1);
            World.sendWorldMessage("<col=ff00ff><shad=ff0000>" + player.getDisplayName()
                    + " recieved a Legendary item from the christmas gift!", false);
        }
        if (rarity > 995 && rarity <= 1000) {
            int length = Legendary1.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Legendary[reward], 1);
            World.sendWorldMessage("<col=ff00ff><shad=ff0000>" + player.getDisplayName()
                    + " recieved a black santa hat from the christmas gift!", false);
        }
    }

    public static void openHallowPack2(Player player){
        int[] Common = { 29441, 29441, 29441, 29441, 29442, 35886, 34836, 28566, 24154, 24154, 24154, 24154, 24154,
                14529, 14531, 11842, 11844, 29441, 29441, 29441 };
        int[] Rare = { 6739, 15259, 13661, 11846, 11848, 11850, 11852, 11854, 11856, 25758 };
        int[] Legendary = { 1053, 1055, 1057, 1506, 2631, 3057, 4164, 6188, 6335, 7003 };
        player.setNextGraphics(new Graphics(184));
        player.getInventory().deleteItem(34354, 1);
        player.sm(Colors.red + "You Opened 1 Mystery Gift and received 1 Halloween Points. ");
        player.setHweenPoints(player.getHweenPoints() + 1);
        int rarity = Utils.getRandom(1000);
        if (rarity > 0 && rarity <= 800) {
            int length = Common.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Common[reward], 1);
            World.sendWorldMessage("<col=fff000>" + player.getDisplayName()
                    + " recieved a Common Item from the Mystery Gift from Halloween Event!", false);
        }
        if (rarity > 800 && rarity <= 990) {
            int length = Rare.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Rare[reward], 1);
            World.sendWorldMessage("<col=ff00ff>" + player.getDisplayName()
                    + " recieved a Rare Item from the Mystery Gift from Halloween Event!", false);
        }
        if (rarity > 990 && rarity <= 1000) {
            int length = Legendary.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Legendary[reward], 1);
            World.sendWorldMessage("<col=ff00ff><shad=ff0000>" + player.getDisplayName()
                    + " recieved a Legendary Item from the Mystery Gift!", false);
        }

    }

    public static void openLoyaltyBox(Player player){
        int[] Common = {1149, 1187, 1215, 1305, 1434, 2513, 3204, 4087, 4585, 4587, 6739, 15332, 15328,
                23713, 23717, 23721, 23725, 23729, 23737, 23733, 23741,23745, 23749, 23753, 23757, 23761,
                23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790, 23802, 23798,23810, 23806, 23814,
                23714, 23718, 23722, 23726, 23730, 23738, 23734, 23742, 23746, 23750, 23754, 23758, 23762,
                23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791, 23803, 23799, 23811, 23807, 23815,
                23715, 23719, 23723, 23727, 23731, 23739, 23735, 23743, 23747, 23751, 23755, 23759, 23763,
                23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792, 23804, 23800, 23812, 23808, 23816



        };

        int[] Rare = {43416, 43418, 43420, 43422, 43424, 43426, 43428, 43430, 43432, 43434, 43436, 34896,
                42706, 42707, 42708, 42709, 42710, 42711, 42712, 42713, 42714, 42715, 42716, 42717, 42718, 42719,
                42720, 42721,
                31041, 23691, 23684, 23685, 23686, 23687, 23688, 23689, 23690, 23683, 23692, 23693, 23694,
                23675, 23676, 23677, 23678, 23673 };

        int[] Pets = {42722, 42725, 43293, 43292, 42743};

        int[] Legendary = {29494 };

        int[] Legendary1 = {29492 };

        player.getInventory().deleteItem(43410, 1);
        int rarity = Utils.getRandom(1000);
        if (rarity > 0 && rarity <= 800) {
            int length = Common.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Common[reward], 1);
            World.sendWorldMessage(
                    "<col=fff000>" + player.getDisplayName() + " recieved a Common Item from the Loyality Box!",
                    false);
        }
        if (rarity > 800 && rarity <= 950) {
            int length = Rare.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Rare[reward], 1);
            World.sendWorldMessage(
                    "<col=ff00ff>" + player.getDisplayName() + " recieved a Rare Item from the Loyality Box!",
                    false);
        }
        if (rarity > 950 && rarity <= 990) {
            int length = Pets.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Pets[reward], 1);
            World.sendWorldMessage(
                    "<col=ff00ff>" + player.getDisplayName() + " recieved a PET from the Loyality Box!",
                    false);

        }
        if (rarity > 990 && rarity <= 998) {
            int length = Legendary.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Legendary[reward], 1);
            World.sendWorldMessage("<col=ff00ff><shad=ff0000>" + player.getDisplayName()
                    + " recieved a untradeable Bond from the Loyality Box!", false);
        }
        if (rarity > 998 && rarity <= 1000) {
            int length = Legendary1.length;
            length--;
            int reward = Utils.getRandom(length);
            player.getInventory().addItem(Legendary[reward], 1);
            World.sendWorldMessage("<col=ff00ff><shad=ff0000>" + player.getDisplayName()
                    + " recieved a Bond from the Loyality Box!", false);
        }
    }

}
