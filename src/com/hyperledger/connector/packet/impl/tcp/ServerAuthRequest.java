package com.hyperledger.connector.packet.impl.tcp;


import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;

import java.util.ArrayList;
import java.util.List;

public class ServerAuthRequest extends BlockchainPacket {

    public ServerAuthRequest(BlockchainConnector connect) {
        super(BlockchainPacketOpcode.VERIFY_REQUEST, connect);
    }

    @Override
    public void handlePacket() {
        connect.setSerial(""+this.getPort(), getPayload().get(1));
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add(this.connect.certified(true, ""+this.getPort()));
        this.connect.getTCP(this.port).sendTCPObject(list);
    }

    @Override
    public void onComplete(String... info) {

    }

}
