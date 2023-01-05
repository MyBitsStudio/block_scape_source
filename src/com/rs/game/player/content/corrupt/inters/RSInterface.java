package com.rs.game.player.content.corrupt.inters;

import com.hyperledger.BlockchainConnector;
import com.rs.game.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RSInterface {

    /**
     * New Interface Class
     * @author Corrupt
     */

    protected BlockchainConnector connect = BlockchainConnector.singleton();
    protected Map<String, Object> properties = new ConcurrentHashMap<>();

    protected int INTER;

    protected Player player;

    public RSInterface(Player player, int INTER){
        this.player = player;
        this.INTER = INTER;
    }

    public abstract void open();
    public abstract void onClose();
    public abstract void handleButtons(int button, int packet, int itemId, int slotId);

    public int getId(){
        return INTER;
    }

    public void refresh(){
        open();
    }

    public void sendHideComponent(int component, boolean hide){
        player.getPackets().sendHideIComponent(INTER, component, hide);
    }

    public void sendText(int component, String text){
        player.getPackets().sendIComponentText(INTER, component, text);
    }

    public void sendItem(int component, int itemId, int amount){
        player.getPackets().sendItemOnIComponent(INTER, component, itemId, amount);
    }

    public void sendItem(int component, int itemId){
        player.getPackets().sendItemOnIComponent(INTER, component, itemId, 1);
    }

    public void sendSprite(int component, int spriteId){
        player.getPackets().sendIComponentSprite(INTER, component, spriteId);
    }


}
