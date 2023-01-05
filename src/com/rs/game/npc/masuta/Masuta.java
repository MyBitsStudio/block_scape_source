package com.rs.game.npc.masuta;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

/**
 * 
 * @author Kingkenobi
 *
 */

@SuppressWarnings("serial")
public class Masuta extends NPC {

	public Masuta(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		this.setHitpoints(5000);
		this.getCombatDefinitions().setHitpoints(5000);
		setLureDelay(0);
		setCapDamage(1750);
		setRun(true);
		setForceFollowClose(true);

	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		checkReset();
	}

	public void checkReset() {
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && !isUnderCombat() && getPossibleTargets().isEmpty())
			setHitpoints(maxhp);

	}

	public void handleIngoingHit(Hit hit) {
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.90;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.25;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.25;
	}

	@Override
	public void sendDeath(Entity source) {
		// World.sendGraphics(Masuta, new Graphics(432), Masuta);
		super.sendDeath(source);
	}

	// test
	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning()
							|| !player.withinDistance(this, 5)
							|| ((!isAtMultiArea() || !player.isAtMultiArea()) && player.getAttackedBy() != this
									&& player.getAttackedByDelay() > System.currentTimeMillis())
							|| !clipedProjectile(player, false))
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}
	// test

}
