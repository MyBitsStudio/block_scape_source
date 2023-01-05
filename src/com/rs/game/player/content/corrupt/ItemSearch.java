package com.rs.game.player.content.corrupt;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.network.protocol.codec.decode.WorldPacketsDecoder;
import com.rs.utils.Utils;

public class ItemSearch {

    public static int inter = 3020;
    public static ItemsContainer<Item> items = new ItemsContainer<Item>(126, false);

    public static void sendInterface(Player player, String item){
        int amouns = 0;

        items.clear();

        for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
            Item itema = new Item(i);
            if (itema.getDefinitions().getName().toLowerCase()
                    .contains(item.toLowerCase())) {
                if(items.getUsedSlots() >= items.getSize()) {
                    amouns++;
                    continue;
                }
                items.add(itema);
                amouns++;
            }
        }

        player.getInterfaceManager().sendInterface(inter);
        player.getPackets().sendIComponentText(inter, 15, "Searching : "+item);

        player.getPackets().sendInterSetItemsOptionsScript (inter, 18, 540, 6, 21, "Info", "Take");
        player.getPackets().sendUnlockIComponentOptionSlots(inter, 18, 0, 126, 0, 1);
        player.getPackets().sendItems(540, items);
    }

    public static void handleButtons(Player player, int componentId, int slotId, int packetId){
        if (componentId == 18) {
            switch (packetId) {

                case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
                    Item item = items.get(slotId);
                    player.getPackets().sendGameMessage("Item Name : " + item.getName() + "<br> " +
                            "Item Id : " + item.getId() + "<br>" +
                            "Value : " + item.getDefinitions().getValue());
                    break;

                case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
                    Item itema = items.get(slotId);
                    if (!player.getInventory().hasFreeSlots()) {
                        player.getPackets().sendGameMessage("Your inventory is full. Make room.");
                        return;
                    }
                    player.getInventory().addItem(itema);
                    break;
            }
        }
    }
}
