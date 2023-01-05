package com.hyperledger.connector.packet.outgoing.standard;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.hyperledger.wallet.WalletType;
import io.netty.util.concurrent.CompleteFuture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WalletRequestPacket extends BlockchainPacket {

    private final WalletType type;
    private final float amount;

    public WalletRequestPacket(WalletType type, float amount) {
        super(BlockchainPacketOpcode.WALLET_CREATE, BlockchainConnector.singleton());
        this.type = type;
        this.amount = amount;
        handlePacket();
        System.out.println("WalletRequestPacket:ready");
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
        System.out.println("WalletRequestPacket:onComplete data: "+ Arrays.toString(info));
    }
}
