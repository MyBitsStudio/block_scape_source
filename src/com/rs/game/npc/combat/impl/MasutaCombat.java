package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
//import com.rs.game.player.Player;
//import com.rs.game.player.content.Combat;
//import com.rs.game.tasks.WorldTask;
//import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

// Created by Kingkenobi

public class MasutaCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(5)) {
			case 1:
				meleeAttack2(npc, target);
				break;
			case 2:
				meleeAttack3(npc, target);
				break;
			case 3:
				meleeAttack4(npc, target);
				break;
			case 4:
				meleeAttack(npc, target);
				break;
			default:
				meleeAttack(npc, target);
				break;
			}

		}
		return defs.getAttackDelay();
	}

	public void meleeAttack(NPC npc, Entity target) {
		World.sendGraphics(npc, new Graphics(4585), target);
		npc.setNextAnimation(new Animation(18163));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(10, 20);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));

		/*
		 * Player targetPlayer = (Player) target; int currentLevel =
		 * targetPlayer.getSkills().getLevel(Skills.DEFENCE);
		 * targetPlayer.getSkills().set(Skills.DEFENCE, currentLevel < 2 ? 1 :
		 * currentLevel - 5);
		 */
	}

	public void meleeAttack2(NPC npc, Entity target) {
		World.sendGraphics(npc, new Graphics(3585), target);
		npc.setNextAnimation(new Animation(18177));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(10, 75);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		/* Player targetPlayer = (Player) target; */
	}

	public void meleeAttack3(NPC npc, Entity target) {
		World.sendGraphics(npc, new Graphics(3582), npc);
		npc.setNextAnimation(new Animation(18185));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(10, 75);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		/* Player targetPlayer = (Player) target; */
	}

	public void meleeAttack4(NPC npc, Entity target) {
		World.sendGraphics(npc, new Graphics(3471), npc);
		npc.setNextAnimation(new Animation(18554));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(10, 75);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		delayHit(npc, 1, target, getMeleeHit(npc, damage));
		/* Player targetPlayer = (Player) target; */
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { 25589, 25590, 25591 };
	}

}