package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class SolakCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(10)) {
			case 1:
				meleeAttack2(npc, target);
				break;
			case 2:
				rangeAttack(npc, target);
				break;
			case 3:
				poisonAttack(npc, target);
				break; 
			case 4:
				mageAttack(npc, target);
				break;
			case 5:
				aoeAttack(npc, target);
				break;	
			default:
				meleeAttack(npc, target);
				break;
			}
		} else {
			switch (Utils.random(5)) {
			case 0:
			case 1:
				rangeAttack(npc, target);
				break;
			case 2:
				aoeAttack(npc, target);
				break;
			case 3:
				rangeAttack(npc, target);
				break;
			case 4:
				poisonAttack(npc, target);
				break;
			default:
				mageAttack(npc, target);
				break;
			}
		}
		return defs.getAttackDelay();
	}

	public void mageAttack(NPC npc, Entity target) {
		npc.setNextForceTalk(new ForceTalk(""));
		for (Entity t : npc.getPossibleTargets()) {
		npc.setNextAnimation(new Animation(31817));
		World.sendGraphics(npc, new Graphics(6865), t);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
		damage += Utils.random(125, 225);
		delayHit(npc, 0, t, getMagicHit(npc, damage));
	}
	}
//test
	public void aoeAttack(NPC npc, Entity target) {
    	for (Entity t : npc.getPossibleTargets()) {
    	npc.setNextAnimation(new Animation(31766));
	    final WorldTile center = new WorldTile(t);
	    World.sendGraphics(npc, new Graphics(6991), center);   //5649
	    npc.setNextForceTalk(new ForceTalk("Seren POWER!"));
	    WorldTasksManager.schedule(new WorldTask() {
		int count = 0;

		@Override
		public void run() {
		    for (Player player : World.getPlayers()) { // lets just loop
							       // all players
							       // for massive  
							       // moves

			if (player == null || player.isDead()
				|| player.hasFinished())
			    continue;
			if (player.withinDistance(center, 1)) {
			    delayHit(npc, 0, player,
				    new Hit(npc, Utils.random(125),
					    HitLook.REGULAR_DAMAGE));
			}
		    }
		    if (count++ == 10) {
			stop();
			return;
		      }
		   
		}
	    }, 0, 0);
    	}	
    }	
//	
	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextForceTalk(new ForceTalk(""));
		for (Entity t : npc.getPossibleTargets()) {
		npc.setNextAnimation(new Animation(31764));
		World.sendProjectile(npc, t, 6896, 41, 16, 41, 35, 16, 0);//39, 36, 41, 50, 0, 100); 
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, t);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, t, getRangeHit(npc, damage));
	}
	}
	public void poisonAttack(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
		final Player player = t instanceof Player ? (Player) t : null;	
		if (player != null) {
			npc.setNextAnimation(new Animation(31764));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage("", true);
					delayHit(npc, 0, t,
							getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t)
									+ Utils.random(150, 200)));
					player.setNextGraphics(new Graphics(6898, 50, 0));
					player.getPoison().makePoisoned(100);
					stop();
				}
			}, 0);
		}
	}
	}

	public void meleeAttack(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
		npc.setNextAnimation(new Animation(31763));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, t);
		World.sendGraphics(npc, new Graphics(5516), t);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, t, getMeleeHit(npc, damage));
	}
	}
	public void meleeAttack2(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(31815));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		for (Entity t : npc.getPossibleTargets()) {
		World.sendGraphics(npc, new Graphics(5516), t);
		damage += Utils.random(300, 350);
		delayHit(npc, 3, t, getMeleeHit(npc, damage));

	}
	}

	
	@Override
	public Object[] getKeys() {
		return new Object[] { 25513 };
	}

}