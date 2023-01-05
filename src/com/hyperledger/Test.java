package com.hyperledger;

import com.hyperledger.connector.packet.outgoing.standard.SystemTransactionRequestPacket;


public class Test {

    public static void main(String[] args) throws InterruptedException {
        BlockchainConnector connect = new BlockchainConnector();
        Thread.sleep(5000);
        System.out.println("Lets start");
        String wallet = "0x1saV946BlemlhRGr";

        SystemTransactionRequestPacket packet = new SystemTransactionRequestPacket(wallet, 1.05f, "Block Coin");
        System.out.println("encrypt : "+packet.getPayload());
        connect.sendEncrypted(packet);

//        for(int i =0; i < 10; i++){
//            WalletRequestPacket test = new WalletRequestPacket(WalletType.PLAYER, 1.05f);
//            System.out.println("encrypt : "+test.getPayload());
//            connect.sendEncrypted(test);
//        }

    }
}
