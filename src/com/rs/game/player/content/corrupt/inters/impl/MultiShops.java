package com.rs.game.player.content.corrupt.inters.impl;

import com.hyperledger.BlockchainUtils;
import com.rs.game.activites.Sawmill;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.inters.RSInterface;
import com.rs.game.world.shops.MultiShop;
import com.rs.game.world.shops.Shop;
import com.rs.game.world.shops.ShopItem;
import org.jetbrains.annotations.NotNull;

public class MultiShops extends RSInterface {

    private MultiShop shop;

    public MultiShops(Player player, int INTER, MultiShop shop) {
        super(player, INTER);
        this.shop = shop;
        properties.put("inter_tab", player.getPlayerVars().getViewingTabOfShop() == -1 ? 0 : player.getPlayerVars().getViewingTabOfShop());
        properties.put("shop_item", -1);
        properties.put("is_buying", false);
        properties.put("quantity", 1);
    }

    public MultiShop getMulti(){return shop;}
    public int getShopId(){return (int) properties.get("inter_tab");}

    public void setQuantity(int quantity) {
        properties.replace("quantity", quantity);
        refresh();
    }

    @Override
    public void open() {
        setUp();
        Shop shops = shop.getShops()[(int) properties.get("inter_tab")];
        sendItems(shops);
        shops.sendInventory(player);
        shops.sendExtraConfigs(player); //?
    }

    private int spriteForEquip(int equip){
        switch(equip){
            case 10 -> { // feet
                return 29675;
            }
            case 3-> { // weapon
                return 29669;
            }
            case 12 -> { //ring
                return 29670;
            }
            case 13 -> { // arrows
                return 29676;
            }
            case 4 -> { // chest
                return 29671;
            }
            case 0 -> { // hat
                return 29666;
            }
            case 5 -> { // shield
                return 29672;
            }
            case 9 ->{ // hands
                return 29674;
            }
            case 1 -> { // cape
                return 29667;
            }
            case 2 -> { // amulet
                return 29668;
            }
            case 7 -> { // legs
                return 29673;
            }
            case 14 -> { // aura
                return 29677;
            }
        }
        return 29678;
    }

    private void sendItems(@NotNull Shop shops){
        player.getPackets().sendInterSetItemsOptionsScript(getId(), 48,  shop.getKey(), 8, 16, "Choose");
        player.getPackets().sendUnlockIComponentOptionSlots(getId(), 48, 0, 128, 0);

        shops.sendItems(player, shop.getKey());
    }

    private void setUp(){
        sendSprite(20, shop.canSell() ? 25862 : 25863);
        sendText(16, shop.getName());

        sendHideComponent(28, true);
        sendHideComponent(33, true);
        sendHideComponent(38, true);
        sendHideComponent(43, true);
        sendHideComponent(68, true);

        for(int i = 0; i < shop.getShops().length; i++){
            sendHideComponent(28 + (i * 5), false);
            sendText(32 + (i * 5), shop.getShops()[i].getShortName());
        }

        if((int) properties.get("shop_item") == -1) {
            sendText( 50, "");
            sendText( 52, "");
            sendText( 54, "");
            sendText( 56, "");
        } else {
            Shop shops = shop.getShops()[(int) properties.get("inter_tab")];
            ShopItem item = shops.getItemForSlot(player, (int) properties.get("shop_item"));
            if(item == null)
                return;
            Item itemx = new Item(item.getId());
            int amount = player.getInventory().getAmountOf(itemx.getId());
            amount += player.getBank().getNumberOf(itemx.getId());
            sendText( 50, ""+itemx.getName());
            sendText( 52, ""+ BlockchainUtils.formatCoins(BlockchainUtils.coinToChain(item.getPrice())));
            sendText( 54, ""+BlockchainUtils.formatCoins(BlockchainUtils.coinToChain(itemx.getDefinitions().getTipitPrice())));
            sendText( 56, ""+amount);
            sendSprite(59, spriteForEquip(itemx.getDefinitions().getEquipSlot()));
        }
        sendText( 61, ""+BlockchainUtils.formatCoins(player.getWallet().getBalance(shop.getCurrency().getName())));

        if((boolean) properties.get("is_buying")){
            Shop shops = shop.getShops()[(int) properties.get("inter_tab")];
            ShopItem item = shops.getItemForSlot(player, (int) properties.get("shop_item"));

            sendHideComponent(68, false);

            sendText( 76, ""+ BlockchainUtils.formatCoins(BlockchainUtils.coinToChain(item.getPrice())));
            sendText( 78, ""+ (int) properties.get("quantity"));
            sendText( 80, ""+ BlockchainUtils.formatCoins(BlockchainUtils.coinToChain(item.getPrice() * (int) properties.get("quantity"))));
        }

    }

    @Override
    public void onClose() {
        properties.clear();
        shop.refresh();
        shop.removePlayer(player);
    }

    @Override
    public void handleButtons(int button, int packet, int itemId, int slotId) {
        System.out.println("Button: "+button + " Packet: "+packet + " ItemId: "+itemId + " SlotId: "+slotId);
        switch(button){
            case 28 -> {
                properties.put("inter_tab", 0);
                refresh();
            }
            case 33 -> {
                properties.put("inter_tab", 1);
                refresh();
            }
            case 38 -> {
                properties.put("inter_tab", 2);
                refresh();
            }
            case 43 -> {
                properties.put("inter_tab", 3);
                refresh();
            }
            case 57 -> {
                if((int) properties.get("shop_item") == -1)
                    return;
                Shop shops = shop.getShops()[(int) properties.get("inter_tab")];
                shops.sendExamine(player, (int) properties.get("shop_item"));
            }
            case 58 -> player.sendMessage("Coming soon!");
            case 62 -> {
                properties.replace("is_buying", true);
                refresh();
            }
            case 77 ->{
                player.getTemporaryAttributtes().put("shop_quantity", Boolean.TRUE);
                player.getPackets().sendInputIntegerScript(true, "Enter quantity:");
            }
            case 81 -> {
                properties.replace("is_buying", false);
                refresh();
            }
            case 86 -> {
                if((int) properties.get("shop_item") == -1)
                    return;
                Shop shops = shop.getShops()[(int) properties.get("inter_tab")];
                shops.buy(player, (int) properties.get("shop_item"), (int) properties.get("quantity"), shop);
            }
        }
        if(slotId != 65535){
            properties.replace("shop_item", slotId);
            refresh();
        }
    }
}
