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
import com.rs.game.player.content.Combat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class VerakLithCombat extends CombatScript{

	
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
						mageAttack(npc, target);
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
		for (Entity t : npc.getPossibleTargets()) {
		npc.setNextAnimation(new Animation(32109));  //26524 old
		World.sendProjectile(npc, t, 16, 56, 16, 35, 20, 16, 0);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.RANGE, t);
		damage += Utils.random(150, 200);		
		delayHit(npc, 1, t, getRangeHit(npc, damage));
		int delay = 5 + Utils.random(5);
		t.addFreezeDelay(delay * 1000, true);
		((Player) t).getPackets().sendGameMessage("you cant move.");
	}
	}

	public void mageAttack2(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
	    	npc.setNextAnimation(new Animation(32104));
		    final WorldTile center = new WorldTile(t);
		    World.sendGraphics(npc, new Graphics(7027), center);   //5649
		    World.sendGraphics(npc, new Graphics(7013), t);
		    npc.setNextForceTalk(new ForceTalk("Burn them!"));
		    Player Player = (Player) target;
			int activeLevel = Player.getPrayer().getPrayerpoints();
			if (activeLevel > 0) {
				int level = Player.getSkills().getLevelForXp(Skills.PRAYER) * 200;
				Player.getPrayer().drainPrayer(level / 1);
			}
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
					    new Hit(npc, Utils.random(175),
						    HitLook.REGULAR_DAMAGE));
				}
			    }
			    if (count++ == 8) {
				stop();
				return;
			      }
			   
			}
		    }, 0, 0);
	    	}	
	    }	
	public void mageAttack(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {	
		((Player) t).getPackets().sendGameMessage("Your Defence been drain by Verak Lith.");			
		npc.setNextAnimation(new Animation(32112));  
        World.sendProjectile(npc, t, 7015, 56, 16, 35, 20, 16, 0);			
		World.sendGraphics(npc, new Graphics(7016), t);
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MAGE, t);
		damage += Utils.random(100, 300);
		delayHit(npc, 0, t, getMagicHit(npc, damage));
	    Player targetPlayer = (Player) t;
	    int currentLevel = targetPlayer.getSkills().getLevel(
		    Skills.DEFENCE);
	    targetPlayer.getSkills().set(Skills.DEFENCE,
		    currentLevel < 20 ? 0 : currentLevel - 20);	
	   	
	     }
	}
	public void poisonAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		if (player != null) {
			npc.setNextAnimation(new Animation(32111));   //26524 old
		 World.sendProjectile(npc, target, 3436, 56, 16, 35, 20, 16, 0);
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
		npc.setNextAnimation(new Animation(32106));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		damage += Utils.random(150, 200);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
	}
	
	public void meleeAttack2(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {	
		npc.setNextAnimation(new Animation(32107));
		int damage = getRandomMaxHit(npc, npc.getMaxHit(), NPCCombatDefinitions.MELEE, t);
		damage += Utils.random(250, 300);
		delayHit(npc, 0, t, getMeleeHit(npc, damage));
	}
	}
	public void dragonFireAttack(NPC npc, Entity target) {
		final Player player = target instanceof Player ? (Player) target : null;
		int damage = Utils.random(120, 700 + npc.getCombatLevel());
		if (target instanceof Player) {
			String message = Combat.getProtectMessage(player);
			if (message != null) {
				player.sendMessage(message, true);
				if (message.contains("fully"))
					damage *= 0.1;
				else if (message.contains("most"))
					damage *= 0.2;
				else if (message.contains("some"))
					damage *= 0.3;
			}
			if (damage > 0)
				player.sendMessage(
						"You are hit by the dragon's fiery breath!",
						true);
		}
		npc.setNextAnimation(new Animation(32110));
		World.sendGraphics(npc, new Graphics(7023), npc);
		World.sendProjectile(npc, target, 438, 56, 16, 35, 20, 16, 0);
		delayHit(npc, 1, target, getRegularHit(npc, damage));
	}
	
	@Override
	public Object[] getKeys() {
		return new Object[] {25656};
	}

}
