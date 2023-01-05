package com.hyperledger.connector.packet.impl.udp;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;

public class UDPVerify extends BlockchainPacket {

    private final int port;

    public UDPVerify(BlockchainConnector connect, int port) {
        super(BlockchainPacketOpcode.REQUEST_UDP_VERIFIED, connect);
        this.port = port;
    }

    @Override
    public void handlePacket() {
        this.connect.setSerial(String.valueOf(this.port), this.data[0]);
    }

    @Override
    public void onComplete(String... info) {}
}
