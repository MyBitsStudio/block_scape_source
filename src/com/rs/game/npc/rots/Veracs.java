package com.rs.game.npc.rots;

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
public class Veracs extends NPC{
	
	public Veracs(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setCapDamage(1500);
		setForceTargetDistance(64);

	}
	
	@Override
	public void processNPC() {
		super.processNPC();
	}
	
	public void handleIngoingHit(Hit hit) {
	}
	
	@Override
    public double getMeleePrayerMultiplier() {
		return 0.9;
    }
		
	@Override
	public void sendDeath(Entity source) {
	super.sendDeath(source);	
	}

}
