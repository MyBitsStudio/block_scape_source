package com.rs.game.player.content.corrupt.perks;

import com.rs.game.player.Player;

import java.io.Serializable;

public class PlayerPerks implements Serializable {

    private static final long serialVersionUID = -2981269569878645577L;

    private transient Player player;
    private final boolean[] unlocked;

    public PlayerPerks(){
        unlocked = new boolean[120];

        for(int i = 0; i < 120; i++)
            unlocked[i] = false;
    }

    public void setPlayer(Player player) { this.player = player; }
    public boolean isUnlocked(int id){ return this.unlocked[id]; }
    public void unlockPerk(int id){ this.unlocked[id] = true; }

    public void unlockPerk(Perks perk){
        if(isUnlocked(perk.getId())){
            //already unlocked

        }
        if(player.getDonationManager().getCorruptCoins() < perk.getCost()){
            //Not enough coins
        }
        player.getDonationManager().takeCorruptCoins(perk.getCost());
        unlockPerk(perk.getId());
        sendBoughtScreen(perk);
    }

    public void sendBoughtScreen(Perks perk){

    }

    public void handleBoughtScreen(){

    }


}
