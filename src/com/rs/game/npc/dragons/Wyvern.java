package com.rs.game.npc.dragons;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;

/**
 * 
 * @author Kingkenobi
 *
 */

@SuppressWarnings("serial")
public class Wyvern extends NPC{
	
	public Wyvern(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		this.setHitpoints(15000);
		this.getCombatDefinitions().setHitpoints(15000);
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
	}
	
	public void handleIngoingHit(Hit hit) {
	}
	
	@Override
    public double getMeleePrayerMultiplier() {
		return 0.3;
    }
	
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.3;
	}
	
	@Override
    public double getRangePrayerMultiplier() {
		return 0.3;
    }
	
	
	@Override
	public void sendDeath(Entity source) {
	super.sendDeath(source);	
	}

}
