package com.hyperledger;


import com.hyperledger.connector.BlockchainTCP;
import com.hyperledger.connector.BlockchainUDP;
import com.hyperledger.connector.packet.BlockchainPacket;
import com.hyperledger.threads.HyperledgerThreads;
import com.rs.utils.Utils;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockchainConnector {

    private BlockchainTCP[] tcp;
    private BlockchainUDP[] udp;

    private HyperledgerThreads threads;

    private final Map<String, Map<String, String>> properties = new ConcurrentHashMap<>();

    private final Map<String, BlockchainPacket> awaitingResponse = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> outgoingPackets = new ConcurrentHashMap<>();

    private int count;

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static BlockchainConnector singleton;

    private AtomicBoolean booting = new AtomicBoolean(false);

    public static BlockchainConnector singleton(){
        if(singleton == null)
            singleton = new BlockchainConnector();
        return singleton;
    }

    public final String HOST = "localhost";

    public static final int[] ports = {
            33322, //Dev Port
            33324, //Dev Port Callback
    };

    public static final int[] fastPorts = {
            33306, //main
    };

    public static final int[] clientPorts = {
            33307, //server
            33311, //server
            33315, //server
            33319, //server
    };

    public static final int[] openClientPorts = {
            33309,
            33313,
            33317,
            33321,
    };

    public BlockchainConnector(){
        booting.set(true);
        //properties.put("serial", CryptoHelper.createRandomString(12));
        singleton = this;
        singleton.generate();
        singleton.startNetwork();
    }

    public HyperledgerThreads getThreads(){
        return threads;
    }
    public PublicKey getPublic(){ return publicKey;}
    public PrivateKey getPrivate(){ return privateKey;}

    private void generate(){
        threads = new HyperledgerThreads();
        tcp = new BlockchainTCP[fastPorts.length];
        udp = new BlockchainUDP[clientPorts.length];
        buildKeys();
    }

    private void buildKeys(){
        publicKey = BlockchainUtils.getPublicKey(BlockchainUtils.mod, "65537");
        privateKey = BlockchainUtils.getPrivateKey(BlockchainUtils.mod, BlockchainUtils.privateExp);
    }

    private void startNetwork(){

        for(int i = 0; i < openClientPorts.length; i++){

            int finalI = i;
            properties.put(""+openClientPorts[finalI], new ConcurrentHashMap<>());
            threads.addToFactory(() -> {
                try {
                    udp[finalI] = new BlockchainUDP(openClientPorts[finalI], this);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        }

        properties.put(""+fastPorts[0], new ConcurrentHashMap<>());
        threads.addToFactory(() -> { this.tcp[0] = new BlockchainTCP(this, fastPorts[0]); this.tcp[0].start(); });
        booting.set(false);
    }

    public String getSerial(String port){ return this.properties.get(port).get("serial");}
    public String getSuperSerial(String port){ return this.properties.get(port).get("superSerial");}
    public int getCount(){ return this.count;}

    public void setSerial(String port, String serial){ this.properties.get(port).put("serial", serial);}
    public void setSuperSerial(String port, String superSerial){ this.properties.get(port).put("superSerial", superSerial);}
    public void setCount(int count){ this.count = count;}

    public void count(){this.count++;}

    public String certified(boolean start, String port){
        return start ? this.properties.get(port).get("serial") + ":0:0" : getSuperSerial(port) + ":" + new BigInteger(""+count).toString(6) + ":" + getSerial(port);
    }

    public String certify(String port){
        return getSerial(port) + ":" + new BigInteger(""+count).toString(6) + ":" + getSuperSerial(port);
    }

    public BlockchainTCP getTCP(int port){
        return this.tcp[(port - 33306) / 4];
    }

    public BlockchainUDP getUDP(){
        return this.udp[Utils.random(1, 3)];
    }

    public void sendEncrypted(BlockchainPacket packet){
        awaitingResponse.put(packet.getSerial(), packet);
        outgoingPackets.put(packet.getSerial(), new ConcurrentHashMap<>());
        outgoingPackets.get(packet.getSerial()).put("time", ""+System.currentTimeMillis());
        outgoingPackets.get(packet.getSerial()).put("radix", ""+(count + 1));
        System.out.println("Sending Encrypted Packet: " + awaitingResponse.get(packet.getSerial()));
        this.tcp[0].sendEncryptedObject(packet.getPayload());
    }

    public boolean getAwaiting(String key){
        return awaitingResponse.containsKey(key);
    }

    public Optional<Map<String, String>> getOutgoing(String key){
        return Optional.ofNullable(outgoingPackets.get(key));
    }

    public BlockchainPacket getAwaitingPacket(String key){
        return awaitingResponse.get(key);
    }

    public boolean isBooting(){
        boolean ready = true;
        for(int port : openClientPorts){
            if (!properties.containsKey("" + port)) {
                System.out.println("Port " + port + " not ready");
                ready = false;
                break;
            }
        }
        if(!properties.containsKey(""+fastPorts[0])) {
            System.out.println("Port " + fastPorts[0] + " not ready");
            ready = false;
        }

        return ready;
    }

}
