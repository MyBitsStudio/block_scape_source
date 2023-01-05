package com.hyperledger.connector.packet.outgoing;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.rs.game.player.Player;
import com.rs.game.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PlayerToPlayerTransactionRequestPacket extends BlockchainPacket {

    private final String from, to, contractName, username, toUsername;
    private final float amount;
    private final Player player, toPlayer;


    public PlayerToPlayerTransactionRequestPacket(@NotNull Player player, @NotNull Player to, float amount, String contractName) {
        super(BlockchainPacketOpcode.TRANSACTION_REQUEST, BlockchainConnector.singleton());
        this.from = player.getWallet().getAddress();
        this.to = to.getWallet().getAddress();
        this.amount = amount;
        this.contractName = contractName;
        this.player = player;
        this.toPlayer = to;
        this.username = player.getUsername();
        this.toUsername = to.getUsername();
        handlePacket();
        System.out.println("PlayerToPlayerTransactionRequestPacket:ready");
    }

    @Override
    public void handlePacket() {
        player.getWallet().setUpdating(true);
        player.getWallet().addPending(this.serial);
        player.sendMessage("Transaction is being sent.");
        toPlayer.getWallet().setUpdating(true);
        toPlayer.getWallet().addPending(this.serial);
        toPlayer.sendMessage("Transaction is being sent.");

        List<String> data = new LinkedList<>();
        data.add("12");
        data.add("5");
        data.add(connect.certified(false, ""+33306));
        data.add("SEND:"+from+":"+to+":"+contractName+":"+amount+":"+3+":"+this.serial);
        data.add(connect.certify(""+33306));
        setPayload(data);
        setSecurity(new BlockchainPacketSecurity(this, false, 0));
    }

    @Override
    public void onComplete(String... info) {
        System.out.println("PlayerToPlayerTransactionRequestPacket:onComplete data: "+ Arrays.toString(info));
            Optional<Player> player = World.getPlayerOption(username);
            Optional<Player> to = World.getPlayerOption(toUsername);
            if(player.isPresent()){
                Player p = player.get();
                if(p.getWallet().getPending(this.serial)){
                    p.getWallet().removePending(this.serial);

                    if(info[0].equalsIgnoreCase("finish")){
                        p.getWallet().addTransaction(info[1]);
                        p.getWallet().setBalance(contractName, Float.parseFloat(info[2]));
                    }
                } else {
                    System.out.println("PlayerToPlayerTransactionRequestPacket:falseData "+username+" data: "+ Arrays.toString(info));
                    return;
                }
                p.getWallet().setUpdating(false);
            } else {
                //TODO player isnt online
                System.out.println("PlayerToPlayerTransactionRequestPacket:notOnline username: "+ username);
            }

            if(to.isPresent()){
                Player p = to.get();
                if(p.getWallet().getPending(this.serial)){
                    p.getWallet().removePending(this.serial);

                    if(info[0].equalsIgnoreCase("finish")){
                        p.getWallet().addTransaction(info[1]);
                        p.getWallet().setBalance(contractName, Float.parseFloat(info[2]));
                    }
                } else {
                    System.out.println("PlayerToPlayerTransactionRequestPacket:falseData "+toUsername+" data: "+ Arrays.toString(info));
                    return;
                }
                p.getWallet().setUpdating(false);
            } else {
                //TODO player isnt online
                System.out.println("PlayerToPlayerTransactionRequestPacket:notOnline username: "+ toUsername);
            }
    }

}
