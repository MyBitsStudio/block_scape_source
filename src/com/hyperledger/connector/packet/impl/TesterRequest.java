package com.hyperledger.connector.packet.impl;

import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;

public class TesterRequest extends BlockchainPacket {


    public TesterRequest(BlockchainPacketOpcode opcode) {
        super(opcode);
    }

    @Override
    public void handlePacket() {
    }

    @Override
    public void onComplete(String... info) {

    }
}
