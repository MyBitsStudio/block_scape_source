package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.content.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class GemDragonsCombat extends CombatScript{

	
	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
			if(npc.withinDistance(target, npc.getSize())) {
				switch(Utils.random(10)) {
				case 1:
					dragonFireAttack(npc, target);
					break;
				case 2:
					rangeAttack(npc, target);
					break;
				case 3:
					poisonAttack(npc, target);
					break;
					default:
						meleeAttack(npc, target);
						break;
				}
			} else {
				switch(Utils.random(5)) {
					case 0:
						rangeAttack(npc, target);
						break;
					case 1:
						dragonFireAttack(npc, target);
						break;
					case 2:
						
					case 3:
						
					default:
						poisonAttack(npc, target);
						break;
						
				}
			}
		return defs.getAttackDelay();
	}
	
	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(14244));  //26524 old
		World.sendProjectile(npc, target, 16, 28, 16, 35, 20, 16, 0);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, target);
		damage += Utils.random(130, 180);		
		delayHit(npc, 1, target, getRangeHit(npc, damage));
	}

	public void poisonAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		if (player != null) {
			npc.setNextAnimation(new Animation(14244));   //26524 old
		 World.sendProjectile(npc, target, 3436, 60, 16, 65, 35, 16, 0);
		   WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
				    player.getPackets().sendGameMessage("You are hit by the dragon's poisonous breath!", true);
					delayHit(npc, 0, target, getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target)+Utils.random(150, 200)));
				    player.setNextGraphics(new Graphics(3437, 50, 0));
				    player.getPoison().makePoisoned(25);
				    stop();
				}
		   }, 0);
		}
	}
	
	public void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(12252));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(130, 180);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
	}
	
	public void dragonFireAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		int damage = Utils.random(80, 550 + npc.getCombatLevel());
		if (target instanceof Player) {
			String message = Combat.getProtectMessage(player);
			if (message != null) {
				player.sendMessage(message, true);
				if (message.contains("fully"))
					damage *= 0;
				else if (message.contains("most"))
					damage *= 0.05;
				else if (message.contains("some"))
					damage *= 0.1;
			}
			if (damage > 0)
				player.sendMessage(
						"You are hit by the dragon's fiery breath!",
						true);
		}
		npc.setNextAnimation(new Animation(14245));
		World.sendProjectile(npc, target, 438, 28, 16, 35, 20, 16, 0);
		delayHit(npc, 1, target, getRegularHit(npc, damage));
	}
	
	@Override
	public Object[] getKeys() {
		return new Object[] {24170,24171,24172,3372,1830};
	}

}
