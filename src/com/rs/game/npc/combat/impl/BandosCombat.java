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

public class BandosCombat extends CombatScript {

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(10)) {
			case 1:
				meleekickAttack(npc, target);
				break;
			case 2:
				meleeAttack(npc, target);
				break;
			case 3:
				meleeAttack(npc, target);
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
				//dragonFireAttack(npc, target);
				break;
			case 2:
				//meleeAttack(npc, target);
				//break;
			case 3:
				//rangeAttack(npc, target);
				break;
			case 4:
				meleeAttack(npc, target);
				break;
			default:
				mageAttack(npc, target);
				break;
			}
		}
		return defs.getAttackDelay();
	}

	public void mageAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(18364));
		World.sendGraphics(npc, new Graphics(3564), npc);
		World.sendProjectile(npc, target, 3565, 41, 16, 41, 35, 16, 0);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, target, getMagicHit(npc, damage));
	}

	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(29006));
		World.sendProjectile(npc, target, 6000, 41, 16, 41, 35, 16, 0);//39, 36, 41, 50, 0, 100); 
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, target);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, target, getRangeHit(npc, damage));
	}

	public void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(10961));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		World.sendGraphics(npc, new Graphics(373), target);
		damage += Utils.random(25, 50);
		delayHit(npc, 1, target, getMeleeHit(npc, damage));
		delayHit(npc, 2, target, getMeleeHit(npc, damage));
		//delayHit(npc, 2, target, getMeleeHit(npc, damage));
	}
	public void meleekickAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(27250));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		World.sendGraphics(npc, new Graphics(373), target);
		damage += Utils.random(150, 250);
		delayHit(npc, 1, target, getMeleeHit(npc, damage));
	}
    
	@Override
	public Object[] getKeys() {
		return new Object[] { 25125, 18506 };
	}

}