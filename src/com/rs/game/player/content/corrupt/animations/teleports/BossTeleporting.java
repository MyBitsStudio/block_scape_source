package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.world.World;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.npc.Drop;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class BossTeleporting {

    public static int inter = 3010;

    public static void sendInterface(Player player){
        int tab = player.getPlayerVars().getInterface3010Tab();
        int boss = player.getPlayerVars().getInterface3010Boss();
        ItemsContainer<Item> items = new ItemsContainer<>(24, true);

        player.getInterfaceManager().sendInterface(inter);

        for(int i = 37; i < 82; i+=3){
            player.getPackets().sendHideIComponent(inter, i, true);
        }

        int l;
        NPC bosses;
        switch(tab){
            case 1://Tier 1
                l = 37;
                player.getPackets().sendIComponentText(inter, 36, "Tier I");
                for(BossTeleports teleports : player.getTeleports().getTier1Bosses()){
                   player.getPackets().sendHideIComponent(inter, l, false);
                   player.getPackets().sendIComponentSprite(inter, l+1, 22159);
                   player.getPackets().sendIComponentText(inter, l+2, teleports.getName());
                   l+=3;
                }
                switch(boss){
                    case 0://Sunfreet
                        items.clear();
                        bosses = World.getNPCByName("Sunfreet");
                        assert bosses != null;
                        player.getPackets().sendIComponentText(inter, 83, "Sunfreet");
                        for(Drop drop : NPCDrops.getDrops(bosses.getId())){
                            Item item = new Item(drop.getItemId(), drop.getMaxAmount());
                            items.add(item);
                        }
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Sunfreet");
                        break;
                    case 1://Bork
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Bork");

                        items.add(new Item(532, 1)); //big bones
                        items.add(new Item(995, 4000 + Utils.random(20000))); //coins
                        items.add(new Item(12163, 5)); //blue charm
                        items.add(new Item(12160, 6)); //crimson charm
                        items.add(new Item(12159, 7)); //green charm
                        items.add(new Item(12158, 8)); //gold charm
                        items.add(new Item(1618, 1)); //uncut diamond
                        items.add(new Item(1620, 3)); //uncut ruby
                        items.add(new Item(1622, 6)); //uncut emerald
                        items.add(new Item(1624, 9)); //uncut sapphire

                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Bork");
                        break;
                    case 2://Barrel Chest
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Barrel Chest");
                        items.add(new Item(10887));
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Barrel Chest");
                        break;
                    case 3://Wyvern
                        items.clear();
                        bosses = World.getNPCByName("Wyvern");
                        assert bosses != null;
                        player.getPackets().sendIComponentText(inter, 83, "Wyvern");
                        for(Drop drop : NPCDrops.getDrops(bosses.getId())){
                            Item item = new Item(drop.getItemId(), drop.getMaxAmount());
                            items.add(item);
                        }
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Wyvern");
                        break;
                    case 4://Dagganoth Kings
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Dagganoth Kings");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Dagganoth Kings (Need to do)");
                        break;
                    case 5://KBD
                        items.clear();
                        bosses = World.getNPCByName("King Black Dragon");
                        assert bosses != null;
                        player.getPackets().sendIComponentText(inter, 83, "King Black Dragon");
                        for(Drop drop : NPCDrops.getDrops(bosses.getId())){
                            Item item = new Item(drop.getItemId(), drop.getMaxAmount());
                            items.add(item);
                        }
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>King Black Dragon");
                        break;
                    case 6://Giant Mole
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Giant Mole");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Giant Mole");
                        break;
                    case 7://Blink
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Blink");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Blink");
                        break;
                    case 8://Chaos Elemental
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Chaos Elemental");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Chaos Elemental");
                        break;
                }
                break;

            case 2://Tier 2
                l = 37;
                player.getPackets().sendIComponentText(inter, 36, "Tier II");
                for(BossTeleports teleports : player.getTeleports().getTier2Bosses()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 22158);
                    player.getPackets().sendIComponentText(inter, l+2, teleports.getName());
                    l+=3;
                }
                switch(boss){
                    case 0://Dark Lord
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Dark Lord");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Dark Lord");
                        break;
                    case 1://God Wars
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "God Wars");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>God Wars");
                        break;
                    case 2://QBD
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Queen Black Dragon");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Queen Black Dragon");
                        break;
                    case 3://Kalphite Queen
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Kalphite Queen");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Kalphite Queen");
                        break;
                    case 4://Helwyr
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Helwyr");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Helwyr");
                        break;
                    case 5://Vindicta
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Vindicta");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Vindicta");
                        break;
                    case 6://Twin Furies
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Twin Furies");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Twin Furies");
                        break;
                    case 7://Gregorvic
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Gregorvic");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Gregorvic");
                        break;
                    case 8://Wildywyrm
                        items.clear();
                        bosses = World.getNPCByName("Wildywyrm");
                        assert bosses != null;
                        player.getPackets().sendIComponentText(inter, 83, "Wildywyrm");
                        for(Drop drop : NPCDrops.getDrops(bosses.getId())){
                            Item item = new Item(drop.getItemId(), drop.getMaxAmount());
                            items.add(item);
                        }
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Wildywyrm");
                        break;
                    case 9://Tormented Demon
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Tormented Demon");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Tormented Demon");
                        break;
                }
                break;

            case 3://Tier 3
                l = 37;
                player.getPackets().sendIComponentText(inter, 36, "Tier III");
                for(BossTeleports teleports : player.getTeleports().getTier3Bosses()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 22157);
                    player.getPackets().sendIComponentText(inter, l+2, teleports.getName());
                    l+=3;
                }
                switch(boss){
                    case 0://Mercenary Mage
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Mercenary Mage");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Mercenary Mage");
                        break;
                    case 1://Kalphite King
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Kalphite King");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Kalphite King");
                        break;
                    case 2://Araxxor
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Araxxor");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Araxxor");
                        break;
                    case 3://Vorago
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Vorago");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Vorago");
                        break;
                    case 4://Corporeal Beast
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Corporeal Beast");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Corporeal Beast");
                        break;
                    case 5://Party Demon
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Party Demon");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Party Demon");
                        break;
                }
                break;

            case 4://Tier 4
                l = 37;
                player.getPackets().sendIComponentText(inter, 36, "Hardcore");
                for(BossTeleports teleports : player.getTeleports().getTier4Bosses()){
                    for(int k = l; k < l+3; k++){
                        player.getPackets().sendHideIComponent(inter, k, false);
                    }
                    player.getPackets().sendIComponentSprite(inter, l+1, 30404);
                    player.getPackets().sendIComponentText(inter, l+2, teleports.getName());
                    l+=3;
                }
                switch(boss){
                    case 0://Solak
                        items.clear();
                        bosses = World.getNPCByName("Solak");
                        assert bosses != null;
                        player.getPackets().sendIComponentText(inter, 83, "Solak");
                        for(Drop drop : NPCDrops.getDrops(bosses.getId())){
                            Item item = new Item(drop.getItemId(), drop.getMaxAmount());
                            items.add(item);
                        }
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Solak");
                        break;
                    case 1://Telos
                        items.clear();
                        player.getPackets().sendIComponentText(inter, 83, "Telos");
                        //To Do
                        items.add(new Item(995, 100000));
                        player.getPackets().sendInterSetItemsOptionsScript(inter, 84, 531, 3, 8, "Examine");
                        player.getPackets().sendUnlockIComponentOptionSlots(inter, 84, 0, 24, 0);
                        player.getPackets().sendItems(531, items);

                        player.getPackets().sendIComponentText(inter, 89, "Stats shall go here <br>Telos");
                        break;
                }
                break;
        }


    }

    public static void handleButtons(Player player, int componentId){
        switch(componentId){

            case 21:
                player.getPlayerVars().setInterface3010Tab(1);
                sendInterface(player);
                break;

            case 22:
                player.getPlayerVars().setInterface3010Tab(2);
                sendInterface(player);
                break;

            case 23:
                player.getPlayerVars().setInterface3010Tab(3);
                sendInterface(player);
                break;

            case 24:
                player.getPlayerVars().setInterface3010Tab(4);
                sendInterface(player);
                break;

            case 25:
                if(player.getPlayerVars().getInterface3010Boss() != -1 && player.getPlayerVars().getInterface3010Tab() != -1)
                    player.getTeleports().teleportToBoss();
                break;

            case 30://simulator
                player.sm("Coming soon!");
                break;
        }

        if(componentId >= 37 && componentId < 82){
            int id = (componentId - 37) / 3;
            player.getPlayerVars().setInterface3010Boss(id);
            sendInterface(player);
        }
    }
}
