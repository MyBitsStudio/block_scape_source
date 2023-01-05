package com.hyperledger.connector.packet;


import com.hyperledger.BlockchainConnector;
import com.hyperledger.BlockchainUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BlockchainPacket {

    protected BlockchainPacketOpcode opcode;
    protected List<String> listData;
    protected String[] data;
    protected BlockchainPacketSecurity security;
    protected BlockchainConnector connect;

    protected String serial = BlockchainUtils.createRandomString(24);

    protected int port;

    protected List<BlockchainPacketProperties> properties = new ArrayList<>();

    public BlockchainPacket(BlockchainPacketOpcode opcode){
        this.opcode = opcode;
    }

    public BlockchainPacket(BlockchainPacketOpcode opcode, BlockchainConnector connect){
        this.opcode = opcode;
        this.connect = connect;
    }

    public BlockchainPacketOpcode getOpcode(){
        return opcode;
    }

    public List<String> getPayload(){
        return listData;
    }

    public BlockchainPacketSecurity getSecurity() {
        return security;
    }

    public void setPayload(List<String> payload) {
        this.listData = payload;
    }

    public int[] getLengths() {
        return opcode.getLengths();
    }

    public void setSecurity(BlockchainPacketSecurity security) {
        this.security = security;
    }
    public boolean isValid(){ return properties.isEmpty(); }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSerial() {return serial;}

    public void setData(String[] data) {this.data = data;}

    public abstract void handlePacket();

    public abstract void onComplete(String... info);

    @Override
    public String toString(){
        return "Packet : \n"
                +"Opcode : "+opcode.getOpcode()+"\n"
                +"Payload : "+listData+"\n"
                +"Data : "+ Arrays.toString(data) +"\n"
                +"Port : "+port+"\n"
                +"Serial : "+serial+"\n"
                +"Security : "+security;
    }
}
