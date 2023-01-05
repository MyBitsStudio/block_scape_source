package com.hyperledger.connector;

import com.hyperledger.BlockchainConnector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BlockchainUDP {

    private final DatagramSocket socket;
    private final InetAddress address;
    private byte[] receive;
    private DatagramPacket incomingPacket;
    private final BlockchainConnector connector;

    private final int port;

    public BlockchainUDP(int port, @NotNull BlockchainConnector connector) throws Exception{
        this.socket = new DatagramSocket(this.port = port);
        this.address = InetAddress.getByName(connector.HOST);
        this.connector = connector;
        start();
    }

    private void start(){
        sendMessage("2:START");
        connector.getThreads().addToFactory(() -> {
            try {
                while (true) {
                    synchronized (this) {
                        receive = new byte[1000];

                        incomingPacket = new DatagramPacket(receive, receive.length);
                        socket.receive(incomingPacket);

                        System.out.println("Received packet from " + incomingPacket.getAddress().getHostAddress() + " : " + new String(incomingPacket.getData()));
                        //UDPHandler.handlePackets(incommingPacket, client);
                        BlockchainUDPHandler.handle(incomingPacket, port);

                        receive = new byte[65535];
                        incomingPacket = null;

                    }
                }
            } catch (IOException e) {
                System.out.println("Exception occured");
            }

        });
    }

    public void sendMessage(@NotNull String message){
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port - 2);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
