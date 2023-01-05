package com.hyperledger.connector.packet.outgoing;

import com.alex.store.Store;
import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.world.World;
import com.rs.game.world.shops.MultiShop;
import com.rs.game.world.shops.Shop;
import com.rs.game.world.shops.ShopItem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class StoreTransactionRequestPacket extends BlockchainPacket {

    private final String from, to, contractName, username;
    private final Item item;
    private final float amount;
    private final MultiShop shop;
    private final Player player;

    private final boolean sell;

    public StoreTransactionRequestPacket(Player player, MultiShop store, float amount, String contractName, boolean sell, ShopItem item, int quantity) {
        super(BlockchainPacketOpcode.TRANSACTION_REQUEST, BlockchainConnector.singleton());
        this.sell = sell;
        if(sell){
            from = store.getWallet();
            to = player.getWallet().getAddress();
        } else{
            from = player.getWallet().getAddress();
            to = store.getWallet();
        }
        this.amount = amount;
        this.contractName = contractName;
        this.shop = store;
        this.player = player;
        this.username = player.getUsername();
        this.item = new Item(item.getId(), quantity);
        handlePacket();
        System.out.println("StoreTransactionRequestPacket:ready");
    }

    public StoreTransactionRequestPacket(Player player, MultiShop store, float amount, String contractName, boolean sell, Item item, int quantity) {
        super(BlockchainPacketOpcode.TRANSACTION_REQUEST, BlockchainConnector.singleton());
        this.sell = sell;
        if(sell){
            from = store.getWallet();
            to = player.getWallet().getAddress();
        } else{
            from = player.getWallet().getAddress();
            to = store.getWallet();
        }
        this.amount = amount;
        this.contractName = contractName;
        this.shop = store;
        this.player = player;
        this.username = player.getUsername();
        this.item = item;
        handlePacket();
        System.out.println("StoreTransactionRequestPacket:ready");
    }


    @Override
    public void handlePacket() {
        player.getWallet().setUpdating(true);
        player.getWallet().addPending(this.serial);
        player.sendMessage("Transaction is being sent. Item's will be deposited into your escrow upon completion.");

        List<String> data = new LinkedList<>();
        data.add("12");
        data.add("5");
        data.add(connect.certified(false, ""+33306));
        data.add("SEND:"+from+":"+to+":"+contractName+":"+amount+":"+(this.sell ? 2 : 1)+":"+this.serial);
        data.add(connect.certify(""+33306));
        setPayload(data);
        setSecurity(new BlockchainPacketSecurity(this, false, 0));
    }

    @Override
    public void onComplete(String... info) {
        System.out.println("ShopTransactionRequestPacket:onComplete data: "+ Arrays.toString(info));
            Optional<Player> player = World.getPlayerOption(username);
            if(player.isPresent()){
                Player p = player.get();
                if(p.getWallet().getPending(this.serial)){
                    p.getWallet().removePending(this.serial);

                    if(info[0].equalsIgnoreCase("finish")){
                        p.getWallet().addTransaction(info[1]);
                        p.getWallet().setBalance(contractName, Float.parseFloat(info[2]));
                        p.getInventory().addItem(item);
                    }
                } else {
                    System.out.println("ShopTransactionRequestPacket:falseData "+username+" data: "+ Arrays.toString(info));
                    return;
                }
                p.getWallet().setUpdating(false);
            } else {
                //TODO player isnt online
                System.out.println("ShopTransactionRequestPacket:notOnline username: "+ username);
            }
    }

}
