package com.rs.game.player;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.corrupt.raids.floor.*;

import java.io.Serializable;
import java.util.Arrays;

public class RaidsManager implements Serializable {

    private static final long serialVersionUID = 5575040759047599289L;

    private transient Player player;
    private RaidsSettings raid;
    private int raidPoints, lives, raidSkill, skelCount;
    private boolean[] unlockedRaids;

    public RaidsManager(){
        this.unlockedRaids = new boolean[124];
        this.raidPoints = 0;


        Arrays.fill(unlockedRaids, false);
        unlockedRaids[0] = true;

    }

    public int TUSKEN_RAID = 0, MAZCAB_RAID = 1;

    public void setPlayer(Player player) { this.player = player; }

    public RaidsSettings getLastRaidSettings(){ return raid; }
    public void setRaidSettings(RaidsSettings raid) { this.raid = raid; }
    public void resetRaidSettings(){ this.raid = null; }
    public int getLives(){ return lives;}
    public void setLives(int set){this.lives = set;}
    public void takeLife(){this.lives--;}
    public int getRaidSkill(){ return raidSkill;}
    public void setRaidSkill(int set){ this.raidSkill = set;}
    public int getSkelCount(){ return skelCount;}
    public void setSkelCount(int set){ this.skelCount = set;}
    public boolean isUnlocked(int raid){ return this.unlockedRaids[raid];}
    public void setUnlocked(int raid, boolean set){this.unlockedRaids[raid] = set;}

    public void beginRaid(String name){
        Raid raid = null;
        for(Raid raids : Raid.values()){
            if(raids.getRaidName().contentEquals(name))
                raid = raids;
        }
        if(raid != null){
            if(player.getControlerManager().getControler() != null){
                if(player.getControlerManager().getControler() instanceof RaidsController){
                    //Already in a raid
                }
            }
            if(!isUnlocked(raid.getId())){
                //Not unlocked
            }
            if(player.getInventory().getCoinsAmount() < raid.getInitialCost()){
                //Not enough coins to start
            }
            player.getInventory().deleteCoins(raid.getInitialCost());

//            if(raid.isMulti()){
//                switch(raid.getId()){
//                    case 1:
//                        new RaidLobby(player, new Player[]{player}, Raid.LOBBY, new MazcabRaid(player, new Player[]{player}, Raid.MAZCAB_RAID));
//                        break;
//                }
//            } else {
            startRaid(raid.getId(), new Player[]{player});

        }
    }

    public void startRaid(int id, Player[] players){
        switch(id){
            case 0:
                new TuskenRaid(player, players, Raid.TUSKEN_RAID);
                break;
            case 1:
                new MazcabRaid(player, players, Raid.MAZCAB_RAID);
                break;
            case 2:
                new DragonBurst(player, players, Raid.DRAGON_BURST);
                break;
            case 3:
                new CorruptedCrystal(player, players, Raid.CORRUPTED_CRYSTALS);
                break;
            default:
                player.sm("cant find raid");
                break;
        }

    }

}
