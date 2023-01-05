package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
//import com.rs.game.player.Player;
//import com.rs.game.player.content.Combat;
//import com.rs.game.tasks.WorldTask;
//import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

// Created by Kingkenobi

public class NexAngelCombat extends CombatScript {
	
	
	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (npc.withinDistance(target, npc.getSize())) {
			switch (Utils.random(10)) {
			case 1:
				mageAttack(npc, target);
				break;
			case 2:
				rangeAttack(npc, target);
				break;
			case 3:
				mageAttack2(npc, target);
				break; 
			case 4:
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
				mageAttack2(npc, target);
				break;
			case 3:
				aoeAttack(npc, target);
				break;
			case 4:
				mageAttack(npc, target);
				break;
			default:
				mageAttack(npc, target);
				break;
			}
		}
		return defs.getAttackDelay();
	}

	public void mageAttack(NPC npc, Entity target) {
		
		for (Entity t : npc.getPossibleTargets()) {
		npc.setNextForceTalk(new ForceTalk("Taste the true power of a blood sacrifice"));
		npc.setNextAnimation(new Animation(17414));   //14224
		World.sendGraphics(npc, new Graphics(5028), t);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
		damage += Utils.random(50, 125);
		delayHit(npc, 0, t, getMagicHit(npc, damage));
		target.setNextGraphics(new Graphics(376));
		npc.heal(damage / 10);
		  if (Utils.getRandom(8) == 0) {
			  npc.setNextForceTalk(new ForceTalk("There is NO ESCAPE!"));
		      WorldTile teleTile = npc;
		      for (int trycount = 0; trycount < 2; trycount++) {
			  teleTile = new WorldTile(target, 2);
			  if (World.canMoveNPC(target.getPlane(), teleTile.getX(),
				teleTile.getY(), target.getSize()))
			    continue;
		    }
		    if (World.canMoveNPC(npc.getPlane(), teleTile.getX(),
			    teleTile.getY(), npc.getSize())) {
			npc.setNextGraphics(new Graphics(5019));
			npc.setNextWorldTile(teleTile);
		        }
		    }
		}
	}

	public void mageAttack2(NPC npc, Entity target) {      //(final NPC npc, Entity target, int n)
		npc.setNextForceTalk(new ForceTalk("WITNESS THE RAW POWER"));
		npc.setNextAnimation(new Animation(17414));
		for (Entity t : npc.getPossibleTargets()) {
		World.sendGraphics(npc, new Graphics(369), t);  //5516
		World.sendGraphics(npc, new Graphics(3375), npc); //5022
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		damage += Utils.random(200, 350);
		delayHit(npc, 2, t, getMagicHit(npc, damage));
	    Player targetPlayer = (Player) t;
	    int currentLevel = targetPlayer.getSkills().getLevel(
		    Skills.MAGIC);
	    targetPlayer.getSkills().set(Skills.MAGIC,
		    currentLevel < 5 ? 0 : currentLevel - 5);
	    int delay = 5 + Utils.random(5);
	    t.addFreezeDelay(delay * 300, true);
	    
		if (Utils.getRandom(5) == 0) {
			npc.setNextForceTalk(new ForceTalk("There is NO ESCAPE!"));
		    WorldTile teleTile = npc;
		    for (int trycount = 0; trycount < 3; trycount++) {
			teleTile = new WorldTile(target, 3);
			if (World.canMoveNPC(target.getPlane(), teleTile.getX(),
				teleTile.getY(), target.getSize()))
			    continue;
		    }
		    if (World.canMoveNPC(npc.getPlane(), teleTile.getX(),
			    teleTile.getY(), npc.getSize())) {
			npc.setNextGraphics(new Graphics(3607));
			npc.setNextWorldTile(teleTile);
		        }
		    }
		}
    }
		
	public void rangeAttack(NPC npc, Entity target) {
		npc.setNextForceTalk(new ForceTalk("For the glory of ZAROS!"));
		npc.setNextAnimation(new Animation(17413));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, target);
		World.sendProjectile(npc, target, 5326, 28, 16, 35, 20, 16, 0);
		//World.sendGraphics(npc, new Graphics(5030), npc);
		//World.sendGraphics(npc, new Graphics(5106), target);
		damage += Utils.random(50, 150);
	    delayHit(npc, 1, target, getRangeHit(npc, damage));
	    if (Utils.getRandom(8) == 0) {
			  npc.setNextForceTalk(new ForceTalk("There is NO ESCAPE!"));
		      WorldTile teleTile = npc;
		      for (int trycount = 0; trycount < 3; trycount++) {
			  teleTile = new WorldTile(target, 3);
			  if (World.canMoveNPC(target.getPlane(), teleTile.getX(),
				teleTile.getY(), target.getSize()))
			    continue;
		    }
		    if (World.canMoveNPC(npc.getPlane(), teleTile.getX(),
			    teleTile.getY(), npc.getSize())) {
			npc.setNextGraphics(new Graphics(5019));
			npc.setNextWorldTile(teleTile);
		        }
		    }
		}
		
	public void meleeAttack(NPC npc, Entity target) {
		World.sendGraphics(npc, new Graphics(5014), target);
		npc.setNextAnimation(new Animation(17453));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(125, 175);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		Player targetPlayer = (Player) target;
	    int currentLevel = targetPlayer.getSkills().getLevel(
		    Skills.ATTACK);
	    targetPlayer.getSkills().set(Skills.ATTACK,
		    currentLevel < 7 ? 0 : currentLevel - 7);
	}
	//test
    public void aoeAttack(NPC npc, Entity target) {
    	for (Entity t : npc.getPossibleTargets()) {
    	npc.setNextAnimation(new Animation(17407));
    	npc.setNextGraphics(new Graphics(3362));
	    final WorldTile center = new WorldTile(t);
	    World.sendGraphics(npc, new Graphics(383), center);   //5649
	    npc.setNextForceTalk(new ForceTalk("Fear the shadow!"));
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
    
    //test
    
	@Override
	public Object[] getKeys() {
		return new Object[] { 24004 };
	}

}