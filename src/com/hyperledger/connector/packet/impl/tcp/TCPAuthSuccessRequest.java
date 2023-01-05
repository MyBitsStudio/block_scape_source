package com.hyperledger.connector.packet.impl.tcp;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;

import java.math.BigInteger;
import java.util.Arrays;

public class TCPAuthSuccessRequest extends BlockchainPacket {

    public TCPAuthSuccessRequest(BlockchainConnector connect) {
        super(BlockchainPacketOpcode.REQUEST_TCP_VERIFIED, connect);
    }

    @Override
    public void handlePacket() {
      System.out.println(this.getPayload());
      String[] info = this.getPayload().get(2).split(":");
      System.out.println("Count : "+info[1] +": Count Radix : "+new BigInteger(info[1], 6).intValue()+" data : "+Arrays.toString(info));
      connect.setSerial(""+this.getPort(), info[0]);
      connect.setSuperSerial(""+this.getPort(), info[2]);
      connect.setCount(new BigInteger(info[1], 6).intValue());

    }

    @Override
    public void onComplete(String... info) {

    }

}
