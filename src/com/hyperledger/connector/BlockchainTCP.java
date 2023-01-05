package com.hyperledger.connector;

import com.hyperledger.BlockchainConnector;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class BlockchainTCP {

    private static ChannelFuture f;
    private BlockchainConnector connector;

    public static BlockchainTCP singleton;
    private int port;

    private Bootstrap b;

    private EventLoopGroup workerGroup;

    public BlockchainTCP(BlockchainConnector connector, int port){
        singleton = this;
        singleton.connector = connector;
        singleton.port = port;

        singleton.service();
    }

    private void service(){
        workerGroup = new NioEventLoopGroup();
        int ports = this.port;

            b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline p = ch.pipeline();

                    p.addLast(new ObjectDecoder(ClassResolvers
                            .cacheDisabled(getClass().getClassLoader())));
                    p.addLast(new ObjectEncoder());
                    p.addLast(new BlockchainTCPObjectHandler(connector, ports));

                }
            });

    }

    public void start(){
        try {
            f = b.connect(connector.HOST, port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void sendEncryptedObject(Object o){
        this.connector.count();
        f.channel().writeAndFlush(o);
    }

    public void sendTCPObject(Object o){
        f.channel().writeAndFlush(o);
    }

    public String channelID(){
        return f.channel().id().asLongText();
    }

    public int getPort(){
        return port;
    }
}
