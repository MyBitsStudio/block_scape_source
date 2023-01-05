package com.hyperledger.connector.packet.impl.tcp;


import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;

import java.util.Arrays;

public class WalletResponse extends BlockchainPacket {

    public WalletResponse(BlockchainConnector connect) {
        super(BlockchainPacketOpcode.WALLET_RESPONSE, connect);
    }

    @Override
    public void handlePacket() {
        System.out.println(this.getPayload());
        String[] info = this.getPayload().get(2).split(":");
        System.out.println("Port : "+this.port +": "+ Arrays.toString(info));

        if(connect.getAwaiting(info[3])){
            BlockchainPacket packet = connect.getAwaitingPacket(info[3]);

            if(packet != null){
                packet.onComplete(info);
            }

        }
    }

    @Override
    public void onComplete(String... info) {

    }

}
