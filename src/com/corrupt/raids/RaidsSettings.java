package com.corrupt.raids;

import com.rs.game.world.map.MapBuilder;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;

import java.io.Serializable;
import java.util.Arrays;

public class RaidsSettings implements Serializable {

    private static final long serialVersionUID = -2883408709210898008L;

    private Player[] team;
    private Player leader;
    private final Raid raid;
    private int killCount;

    private int stage, subStage, npcStage, npcSubStage;
    private boolean started, spawned;

    public int[] boundChunks;
    public WorldObject chest;
    public boolean[] stageCheck;
    private int[] common, rare, ultra, legend;


    public RaidsSettings(Player player, Player[] fcPlayers, Raid raid, RaidsSettings other) {
        boundChunks = MapBuilder.findEmptyChunkBound(raid.getChunks()[0], raid.getChunks()[1]);
        MapBuilder.copyAllPlanesMap(raid.getMapCoords()[0], raid.getMapCoords()[1], boundChunks[0], boundChunks[1], raid.getChunks()[0]);
        team = fcPlayers;
        leader = player;
        this.raid = raid;
        killCount = 0;
        WorldTasksManager.schedule(new WorldTask() {
            long count = 0;
            @Override
            public void run() {
                if(count == 0) {
                    for (Player p : fcPlayers) {
                        sendMessage(p, "Loading in 5...");
                    }
                }
                if(count == 1){
                    for (Player p : fcPlayers) {
                        sendMessage(p, "4...");
                    }
                }
                if(count == 2){
                    for (Player p : fcPlayers) {
                        sendMessage(p, "3...");
                    }
                }
                if(count == 3){
                    for (Player p : fcPlayers) {
                        sendMessage(p, "2...");
                    }
                }
                if(count == 4){
                    for (Player p : fcPlayers) {
                        sendMessage(p, "1...");
                    }
                }
                if(count == 5) {
                    for(Player p : fcPlayers) {
                        setPlayer(p);
                    }
                }
                if(count == 6){
                    for (Player p : fcPlayers) {
                        sendMessage(p, "Begin!");
                    }
                    stop();
                }
                count++;
            }
        }, 0, 1);
    }

    public WorldTile getTile(int mapX, int mapY) {
        return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
    }
    public WorldTile getXTile(int mapX, int mapY){
        return getTile(mapX, mapY).getWorldTile();
    }

    public void setPlayer(Player player){
        setStage(0);
        player.getRaidsManager().setRaidSettings(this);
        player.getControlerManager().startControler(raid.getControllerName(), this);
        player.setNextWorldTile(getTile(raid.getInsideTile()[0], raid.getInsideTile()[1]));
        player.setForceMultiArea(true);
        player.getRaidsManager().setLives(3);
    }

    public void setLobby(Player player, RaidsSettings raids){
        setStage(0);
        team = new Player[10];
        player.getRaidsManager().setRaidSettings(raids);
        player.getControlerManager().startControler(raid.getControllerName(), this, raids);
        player.setNextWorldTile(getTile(raid.getInsideTile()[0], raid.getInsideTile()[1]));
        player.setForceMultiArea(true);
        player.getRaidsManager().setLives(3);
    }

    public void addToTeam(Player player){
       team[(int) Arrays.stream(team).count()] = player;

    }

    public int getX(int X, int Y) {
        return getTile(X,Y).getX();
    }

    public int getY(int X, int Y) {
        return getTile(X,Y).getY();
    }

    public void sendMessage(Player player, String message) {
        player.sm(Colors.red+"[RAIDS] "+message);
    }

    public Raid getRaid() { return raid; }

    public int getStage(){ return this.stage;}
    public void setStage(int stage){ this.stage = stage;}
    public int getSubStage(){ return this.subStage; }
    public void setSubStage(int set){ this.subStage = set; }
    public int getKillCount() { return this.killCount; }
    public void setKillCount(int set){ this.killCount = set;}
    public int getNPCStage(){ return this.npcStage;}
    public void setNPCStage(int set){this.npcStage = set;}
    public int getNPCSubStage(){ return npcSubStage;}
    public void setNPCSubStage(int set){this.npcSubStage = set;}

    public boolean hasStarted(){ return started; }
    public void setStarted(boolean set){ this.started = set;}

    public boolean isSpawned() {return spawned;}
    public void setSpawned(boolean set){ this.spawned = set;}

    public Player[] getTeam(){ return team;}


}
