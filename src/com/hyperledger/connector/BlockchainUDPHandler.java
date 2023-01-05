package com.hyperledger.connector;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketOpcode;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.hyperledger.connector.packet.impl.udp.UDPVerify;

import java.net.DatagramPacket;

import static com.hyperledger.connector.packet.BlockchainPacketSecurity.byteToString;

public class BlockchainUDPHandler {

    public static void handle(DatagramPacket packet, int port) {
        BlockchainConnector connect = BlockchainConnector.singleton();
        connect.getThreads().addToFactory(() -> {
            System.out.println("UDP - " + port + " : " + new String(packet.getData()));
            String[] data = byteToString(packet.getData()).toString().split(":");

            BlockchainPacketOpcode opcode = BlockchainPacketOpcode.getOpcode(Integer.parseInt(data[0]));
            BlockchainPacket bPacket = null;
            if (opcode != null) {
                switch (opcode.getOpcode()) {
                    case 4 -> bPacket = new UDPVerify(connect, port);
                }
            }
            if(bPacket == null) return;
            bPacket.setPort(port);
            bPacket.setData(data);
            bPacket.setSecurity(new BlockchainPacketSecurity(bPacket, true, 1));
            if(bPacket.isValid()) bPacket.handlePacket();
            else System.out.println("Invalid Packet : "+bPacket);
        });


    }
}
