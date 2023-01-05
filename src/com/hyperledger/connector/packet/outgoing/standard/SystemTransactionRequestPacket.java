package com.hyperledger.connector.packet.outgoing.standard;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.hyperledger.wallet.WalletType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SystemTransactionRequestPacket extends BlockchainPacket {

    private final String address, contractName;
    private final float amount;

    public SystemTransactionRequestPacket(String address, float amount, String contractName) {
        super(BlockchainPacketOpcode.TRANSACTION_REQUEST, BlockchainConnector.singleton());
        this.address = address;
        this.amount = amount;
        this.contractName = contractName;
        handlePacket();
        System.out.println("SystemTransactionRequestPacket:ready");
    }


    @Override
    public void handlePacket() {
        List<String> data = new LinkedList<>();
        data.add("12");
        data.add("5");
        data.add(connect.certified(false, ""+33306));
        data.add("SEND:"+contractName+":"+address+":"+amount+":"+this.serial);
        data.add(connect.certify(""+33306));
        setPayload(data);
        setSecurity(new BlockchainPacketSecurity(this, false, 0));
    }

    @Override
    public void onComplete(String... info) {
        System.out.println("SystemTransactionRequestPacket:onComplete data: "+ Arrays.toString(info));
    }
}
