package com.hyperledger.connector;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.connector.packet.BlockchainPacketSecurity;
import com.hyperledger.connector.packet.impl.tcp.ServerAuthRequest;
import com.hyperledger.connector.packet.impl.tcp.TCPAuthSuccessRequest;
import com.hyperledger.connector.packet.impl.tcp.TransactionResponse;
import com.hyperledger.connector.packet.impl.tcp.WalletResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlockchainTCPObjectHandler extends SimpleChannelInboundHandler<Object> {

    private final BlockchainConnector connect;
    final int port;
    public BlockchainTCPObjectHandler(BlockchainConnector connect, int ports) {
        super();
        this.connect = connect;
        this.port = ports;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) {
            if(o instanceof ArrayList){
                try {
                    ArrayList<String> info = (ArrayList<String>) o;
                    System.out.println("Received: " + info);
                    connect.getThreads().addToFactory(() -> {
                        BlockchainPacket packet = null;
                        switch(Integer.parseInt(info.get(0))){
                            case 0 -> packet = new ServerAuthRequest(connect);
                            case 3 -> packet = new TCPAuthSuccessRequest(connect);
                            case 8 -> packet = new WalletResponse(connect);
                            case 13 -> packet = new TransactionResponse(connect);
                        }
                        if(packet != null){
                            packet.setPort(this.port);
                            packet.setPayload(info);
                            packet.setSecurity(new BlockchainPacketSecurity(packet, true, 0));
                            if(packet.isValid()){
                                packet.handlePacket();
                            } else {
                                System.out.println("Packet is not valid "+packet.getOpcode().getOpcode());
                            }
                        }

                    });
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, @NotNull Throwable cause) {

    }
}
