package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class WyvernCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(10)) {
			case 1:
				FrostAttack(npc, target);
				break;
			case 2:
				mageAttack(npc, target);
				break;
			case 3:
				poisonAttack(npc, target);
				break;
			default:
				meleeAttack(npc, target);
				break;
			}
		} else {
			switch (Utils.random(5)) {
			case 0:
				mageAttack(npc, target);
				break;
			case 1:
				FrostAttack(npc, target);
				break;
			case 2:
				FrostAttack(npc, target);
				break;
			case 3:
				mageAttack(npc, target);
				break;
			default:
				poisonAttack(npc, target);
				break;
			}
		}
		return defs.getAttackDelay();
	}

	public void mageAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(27752));//27757
		World.sendGraphics(npc, new Graphics(5913), npc);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		damage += Utils.random(150, 200);
		delayHit(npc, 1, target, getMagicHit(npc, damage));
	}

	public void poisonAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		if (player != null) {
			npc.setNextAnimation(new Animation(27752));
			World.sendGraphics(npc, new Graphics(5910), npc);
			World.sendGraphics(npc, new Graphics(5911), target);
			World.sendProjectile(npc, target, 5912, 28, 16, 35, 20, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage("You are hit by the wyvern poisonous breath!", true);
					delayHit(npc, 0, target,
							getPoisonHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target)
									+ Utils.random(100, 150)));
					player.getPoison().makePoisoned(50);
					stop();
				}
			}, 0);
		}
	}

	public void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(27751));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		//World.sendGraphics(npc, new Graphics(9), npc);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
	}

	
	
	public void FrostAttack(NPC npc, Entity target) {     
		npc.setNextAnimation(new Animation(27757));//27752
		World.sendGraphics(npc, new Graphics(5909), npc);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		damage += Utils.random(100, 250);
		delayHit(npc, 2, target, getMagicHit(npc, damage));
	    int delay = 5 + Utils.random(5);
	    target.addFreezeDelay(delay * 300, true);		
	
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { 21812,21992 };
	}

}
