package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
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

public class TelosCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(10)) {
			case 1:
				meleeAttack3(npc, target);
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
			default:
				meleeAttack(npc, target);
				break;
			}
		} else {
			switch (Utils.random(5)) {
			case 0:
			case 1:
				meleeAttack3(npc, target);
				break;
			case 2:
				meleeAttack(npc, target);
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
		npc.setNextForceTalk(new ForceTalk("Power from the ground"));
		for (Entity t : npc.getPossibleTargets()) {
			npc.setNextAnimation(new Animation(28959));
			World.sendGraphics(npc, new Graphics(5513), t);
			int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
			damage += Utils.random(100, 175);
			delayHit(npc, 0, t, getMagicHit(npc, damage));
		}
	}

	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextForceTalk(new ForceTalk("Im Telos, The Warden!"));
		for (Entity t : npc.getPossibleTargets()) {
			npc.setNextAnimation(new Animation(29006));
			World.sendProjectile(npc, t, 6000, 41, 16, 41, 35, 16, 0);// 39, 36, 41, 50, 0, 100);
			int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, t);
			damage += Utils.random(100, 175);
			delayHit(npc, 0, t, getRangeHit(npc, damage));
		}
	}

	public void poisonAttack(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
			final Player player = t instanceof Player ? (Player) t : null;
			if (player != null) {
				npc.setNextAnimation(new Animation(28959));
				World.sendGraphics(npc, new Graphics(6245), npc);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getPackets().sendGameMessage("You got curse by telos", true);
						delayHit(npc, 0, t,
								getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t)
										+ Utils.random(150, 175)));
						player.setNextGraphics(new Graphics(6129, 50, 0));
						player.getPoison().makePoisoned(45);
						stop();
					}
				}, 0);
			}
		}
	}

	public void meleeAttack(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
			npc.setNextAnimation(new Animation(28929));
			int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, t);
			World.sendGraphics(npc, new Graphics(5516), t);
			damage += Utils.random(150, 225);
			delayHit(npc, 0, t, getMeleeHit(npc, damage));
		}
	}

	public void meleeAttack3(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
			npc.setNextAnimation(new Animation(28938));
			int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, t);
			// World.sendGraphics(npc, new Graphics(5516), t);
			World.sendGraphics(npc, new Graphics(6246), npc);
			damage += Utils.random(225, 300);
			delayHit(npc, 5, t, getMeleeHit(npc, damage));
		}
	}

	public void meleeAttack2(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(28935));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		for (Entity t : npc.getPossibleTargets()) {
			// World.sendGraphics(npc, new Graphics(5516), target);
			damage += Utils.random(150, 200);
			delayHit(npc, 2, target, getMeleeHit(npc, damage));
			damage += Utils.random(250, 250);
			delayHit(npc, 7, t, getMeleeHit(npc, damage));
		}
	}

	public void dragonFireAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		int damage = Utils.random(80, 250 + npc.getCombatLevel());
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
				player.sendMessage("you you got burn by Telos", true);
		}
		npc.setNextAnimation(new Animation(28959));
		World.sendGraphics(npc, new Graphics(5513), player);
		delayHit(npc, 1, target, getRegularHit(npc, damage));
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { 22891 };
	}

}