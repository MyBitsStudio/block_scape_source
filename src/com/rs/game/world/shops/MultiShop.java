package com.rs.game.world.shops;

import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.inters.impl.BlockExplorer;
import com.rs.game.player.content.corrupt.inters.impl.MultiShops;
import com.rs.game.world.WorldCurrency;
import com.rs.utils.ItemSetsKeyGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiShop {

    private final Shop[] shops;
    private final int id;

    private final WorldCurrency currency;

    private final String wallet, name;

    private final boolean canSell, isIronman;

    private final List<Player> viewingPlayers;

    private static final int ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

    public static int INTERFACE = 3022;

    public MultiShop(int id, WorldCurrency currency, String wallet, String name, boolean canSell, boolean isIronman, Shop... shops) {
        viewingPlayers = new CopyOnWriteArrayList<>();
        this.id = id;
        this.currency = currency;
        this.wallet = wallet;
        this.name = name;
        this.canSell = canSell;
        this.isIronman = isIronman;
        this.shops = shops;
    }

    public Shop[] getShops() {
        return shops;
    }

    public int getId() {
        return id;
    }

    public WorldCurrency getCurrency() {
        return currency;
    }

    public String getWallet() {
        return wallet;
    }

    public boolean canSell() {
        return canSell;
    }

    public boolean isIronman() {
        return isIronman;
    }

    public String getName() {
        return name;
    }

    public int getKey(){
        return ITEMS_KEY;
    }

    public void addPlayer(Player player) {
        viewingPlayers.add(player);
        player.getPackets().sendConfig(118, ITEMS_KEY);
        player.getInterfaceManager().sendInterface(new MultiShops(player, INTERFACE, this));
    }

    public void removePlayer(Player player){
        viewingPlayers.remove(player);
        player.getInterfaceManager().clearRSInterface();
    }

    public void restoreItems(){
        for (Shop shop : shops) {
            shop.restoreItems();
        }
    }

    public void refresh(){
        for(Player player : viewingPlayers){
            //player.getInterfaceManager().sendInterface(new MultiShops(player, INTERFACE, this));
        }
    }

}
