package com.rs.game.player.content.corrupt.animations;

import com.rs.game.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class AnimationManager implements Serializable {

    private static final long serialVersionUID = -4777307301644190803L;

    private transient Player player;
    private boolean[] unlockTeleports, unlockedWalks, unlockedSkills;

    public AnimationManager(){
        unlockTeleports = new boolean[120];
        unlockedWalks = new boolean[120];
        unlockedSkills = new boolean[120];
        Arrays.fill(unlockTeleports, false);
        Arrays.fill(unlockedWalks, false);
        Arrays.fill(unlockedSkills, false);

        unlockTeleports[0] = true;
        unlockedWalks[0] = true;
        unlockedSkills[0] = true;
    }

    public void setPlayer(Player player){ this.player = player; }

    public boolean teleportUnlocked(int id){ return this.unlockTeleports[id];}
    public boolean walksUnlocked(int id){ return this.unlockedWalks[id];}
    public boolean skillsUnlocked(int id){ return this.unlockedSkills[id];}
    public void unlockTeleport(int id){ this.unlockTeleports[id] = true;}
    public void unlockWalk(int id){ this.unlockedWalks[id] = true;}
    public void unlockSkill(int id){ this.unlockedSkills[id] = true;}
}
