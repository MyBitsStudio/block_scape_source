package com.hyperledger.connector.packet.outgoing;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.world.World;
import com.rs.game.world.shops.MultiShop;
import com.rs.game.world.shops.ShopItem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ServerTransactionRequestPacket extends BlockchainPacket {

    private final String from, to, contractName, username;
    private final float amount;
    private final Player player;


    public ServerTransactionRequestPacket(Player player, float amount, String contractName) {
        super(BlockchainPacketOpcode.TRANSACTION_REQUEST, BlockchainConnector.singleton());
        this.from = "0x0";
        this.to = player.getWallet().getAddress();
        this.amount = amount;
        this.contractName = contractName;
        this.player = player;
        this.username = player.getUsername();
        handlePacket();
        System.out.println("ServerTransactionRequestPacket:ready");
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
        data.add("SEND:"+from+":"+to+":"+contractName+":"+amount+":"+0+":"+this.serial);
        data.add(connect.certify(""+33306));
        setPayload(data);
        setSecurity(new BlockchainPacketSecurity(this, false, 0));
    }

    @Override
    public void onComplete(String... info) {
        System.out.println("ServerTransactionRequestPacket:onComplete data: "+ Arrays.toString(info));
            Optional<Player> player = World.getPlayerOption(username);
            if(player.isPresent()){
                Player p = player.get();
                if(p.getWallet().getPending(this.serial)){
                    p.getWallet().removePending(this.serial);

                    if(info[0].equalsIgnoreCase("finish")){
                        p.getWallet().addTransaction(info[1]);
                        p.getWallet().setBalance(contractName, Float.parseFloat(info[2]));
                    }
                } else {
                    System.out.println("ServerTransactionRequestPacket:falseData "+username+" data: "+ Arrays.toString(info));
                    return;
                }
                p.getWallet().setUpdating(false);
            } else {
                //TODO player isnt online
                System.out.println("ServerTransactionRequestPacket:notOnline username: "+ username);
            }
    }

}
