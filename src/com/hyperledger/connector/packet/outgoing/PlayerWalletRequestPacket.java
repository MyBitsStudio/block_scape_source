package com.hyperledger.connector.packet.outgoing;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.hyperledger.wallet.WalletType;
import com.rs.game.world.World;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerWallet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlayerWalletRequestPacket extends BlockchainPacket {

    private final WalletType type;
    private final Player player;
    private final float amount;

    public PlayerWalletRequestPacket(Player player, float amount) {
        super(BlockchainPacketOpcode.WALLET_CREATE, BlockchainConnector.singleton());
        this.type = WalletType.PLAYER;
        this.player = player;
        this.amount = amount;
        handlePacket();
        System.out.println("PlayerWalletRequestPacket:ready");
    }


    @Override
    public void handlePacket() {
        List<String> data = new LinkedList<>();
        data.add("7");
        data.add("5");
        data.add(connect.certified(false, ""+33306));
        data.add("REQUEST:"+type.ordinal()+":"+amount+":"+this.getSerial());
        data.add(connect.certify(""+33306));
        setPayload(data);
        setSecurity(new BlockchainPacketSecurity(this, false, 0));
    }

    @Override
    public void onComplete(String... info) {
        System.out.println("PlayerWalletRequestPacket:onComplete data: "+ Arrays.toString(info));
        if(player == null) {
            System.out.println("PlayerWalletRequestPacket:onComplete player is null");
        } else if (World.getPlayers().contains(player)) {
            player.setWallet(new PlayerWallet(info[0], Float.parseFloat(info[1])));
            player.getDialogueManager().startDialogue("SimpleMessage", "Your wallet has been created! Balance : "+player.getWallet().getBalance("Block Coin"));
        } else {
            System.out.println("PlayerWalletRequestPacket:onComplete player is offline");
        }
    }
}
