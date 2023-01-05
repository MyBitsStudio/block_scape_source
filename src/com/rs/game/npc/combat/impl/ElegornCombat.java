package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class ElegornCombat extends CombatScript{

	
	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
			if(npc.withinDistance(target, npc.getSize())) {
				switch(Utils.random(10)) {
				case 0:
					mageAttack(npc, target);
					break;
				case 1:
					dragonFireAttack(npc, target);
					break;
				case 2:
					rangeAttack(npc, target);
					break;
				case 3:
					poisonAttack(npc, target);
					break;
				case 4:
					meleeAttack2(npc, target);
					break;
				case 5:
					mageAttack2(npc, target);
					break;
				case 6:
					meleeAttack(npc, target);
					break;
				}
			} else {
				switch(Utils.random(10)) {
					case 0:
						rangeAttack(npc, target);
						break;
					case 1:
						dragonFireAttack(npc, target);
						break;
					case 2:
						mageAttack2(npc, target);
						break;
					case 3:
						meleeAttack2(npc, target);
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
	
	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(14244));  //26524 old
		World.sendProjectile(npc, target, 16, 28, 16, 35, 20, 16, 0);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, target);
		damage += Utils.random(100, 150);		
		delayHit(npc, 1, target, getRangeHit(npc, damage));
	}

	public void mageAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(14245));
		for (Entity t : npc.getPossibleTargets()) {
		//World.sendProjectile(npc, target, 16, 28, 16, 35, 20, 16, 0);
		World.sendGraphics(npc, new Graphics(7011), npc);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
		damage += Utils.random(100, 250);		
		delayHit(npc, 1, t, getMagicHit(npc, damage));
		
	}
	}
	public void mageAttack2(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(26543));  
		//World.sendProjectile(npc, target, 16, 28, 16, 35, 20, 16, 0);
		for (Entity t : npc.getPossibleTargets()) {
		World.sendGraphics(npc, new Graphics(7010), t);
		if (target instanceof Player)
			((Player) target).getPackets().sendGameMessage("Your prayer been drain by the Celestial rain.");
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
		damage += Utils.random(50, 100);
		Player Player = (Player) target;
		delayHit(npc, 1, t, getMagicHit(npc, damage));
		int activeLevel = Player.getPrayer().getPrayerpoints();
		if (activeLevel > 0) {
			int level = Player.getSkills().getLevelForXp(Skills.PRAYER) * 225;
			Player.getPrayer().drainPrayer(level / 1);
		}
	}
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
				    player.getPoison().makePoisoned(200);
				    stop();
				}
		   }, 0);
		}
	}
	
	public void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(12252));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(100, 200);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
	}
	
	public void meleeAttack2(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {	
		npc.setNextAnimation(new Animation(26528));
		World.sendGraphics(npc, new Graphics(7048), npc);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, t);
		damage += Utils.random(150, 300);
		delayHit(npc, 0, t, getMeleeHit(npc, damage));
	}
	}
	public void dragonFireAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		int damage = Utils.random(120, 500 + npc.getCombatLevel());
		if (target instanceof Player) {
			String message = Combat.getProtectMessage(player);
			if (message != null) {
				player.sendMessage(message, true);
				if (message.contains("fully"))
					damage *= 0.05;
				else if (message.contains("most"))
					damage *= 0.1;
				else if (message.contains("some"))
					damage *= 0.2;
			}
			if (damage > 0)
				player.sendMessage(
						"You are hit by the dragon's fiery breath!",
						true);
		}
		npc.setNextAnimation(new Animation(26537));
		World.sendProjectile(npc, target, 438, 56, 16, 35, 20, 16, 0);
		delayHit(npc, 1, target, getRegularHit(npc, damage));
	}
	
	@Override
	public Object[] getKeys() {
		return new Object[] {25695};
	}

}
