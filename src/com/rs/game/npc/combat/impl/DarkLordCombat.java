package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;
/**
 * 
 * @author paolo
 *
 */
public class DarkLordCombat extends CombatScript {

	/**
	 * npcs with this script
	 */
    @Override
    public Object[] getKeys() {
	return new Object[] {20374, 19553};
    }
	/**
	 * returns true if the player is at the water
	 * @param player
	 * @return
	 */
	public boolean isAtWater(Player player) {
		return (player.getX() >= 3801 && player.getX() <= 3818
				&& player.getY() <= 4698 && player.getY() >= 4696);
	}
    
    /**
     * attack script
     */
    @Override
    public int attack(final NPC npc, final Entity target) {
	NPCCombatDefinitions def = npc.getCombatDefinitions();
	final Player player = (Player) target;
	 final WorldObject fire = new WorldObject(95033, 10, 0, player.getX(), player.getY() +1, 0);
	 final WorldObject fire2 = new WorldObject(95033, 10, 0, player.getX()+1, player.getY(), 0);
	 final WorldObject fire3 = new WorldObject(95033, 10, 0, player.getTile().getX() -1, player.getY(), 0);
	 final WorldObject fire4 = new WorldObject(95033, 10, 0, player.getTile().getX() , player.getY() -1, 0);	
	int random = Utils.random(24);
	if (random == 1) { 
	npc.setNextAnimation(new Animation(24224));
	//delayHit(npc, 1, target, getMeleeHit(npc, Utils.random(20,460) + player.getPAdamage()));
	delayHit(npc, 0, player, new Hit(npc, Utils.random(200,430)+ player.getPlayerVars().getPAdamage(), HitLook.REGULAR_DAMAGE));
	}else if (random == 2) {
	npc.setNextAnimation(new Animation(24224));
	//delayHit(npc, 1, target, getMeleeHit(npc, Utils.random(20,460)+ player.getPAdamage()));
	delayHit(npc, 0, player, new Hit(npc, Utils.random(200,430)+ player.getPlayerVars().getPAdamage(), HitLook.REGULAR_DAMAGE));
	} else if (random == 3) {
	npc.setNextAnimation(new Animation(24224));
	//delayHit(npc, 1, target, getMeleeHit(npc, Utils.random(20,460)+ player.getPAdamage()));
	delayHit(npc, 0, player, new Hit(npc, Utils.random(200,430)+ player.getPlayerVars().getPAdamage(), HitLook.REGULAR_DAMAGE));
	}else  if  (random == 4){
	 npc.setNextAnimation(new Animation(24237));
	WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					World.sendProjectile(npc, target, 5102, 80, 30, 40, 20, 5, 0);
					delayHit(npc, 1, target, getRangeHit(npc, Utils.random(520)+ player.getPlayerVars().getPAdamage()));
				}
			}, 2);
	}
	/**
	 * player has to run into the water to avoid damage
	 */
	else if  (random == 5){  
	npc.setNextAnimation(new Animation(24232));	
	npc.setNextForceTalk(new ForceTalk("Find some water my friend!"));
	player.sm("<col=FF000>Quick find some water to absorb the heat!");
	WorldTasksManager.schedule(new WorldTask() {
		int count = 0;
		@Override
		public void run() {			
			if (!isAtWater(player)){
				player.setNextGraphics(new Graphics(4148));
				delayHit(npc, 0, player, new Hit(npc, Utils.random(50,100), HitLook.MAGIC_DAMAGE));	
				}
			if(isAtWater(player)) {
				player.setNextGraphics(new Graphics(4153));
				player.sm("Standing in the water prevented you from taking damage.");
				stop();
				return;
			}
			if(count++ == 10) {
				if (!isAtWater(player)){
					player.setNextGraphics(new Graphics(4148));
					delayHit(npc, 0, player, new Hit(npc, Utils.random(200,400), HitLook.MAGIC_DAMAGE));	
				}
				stop();
				return;
			}
		}
	}, 0, 0);
	
	}
	/**
	 * if the player stands in the water he will receive damage
	 */
	else if  (random == 6){  
	npc.setNextAnimation(new Animation(24232));	
	npc.setNextForceTalk(new ForceTalk("Don't stand in my water!"));
	player.sm("<col=FF000>Standing in the water will heal the dark lord incredible.");
	WorldTasksManager.schedule(new WorldTask() {
		int count = 0;
		@Override
		public void run() {			
			if (isAtWater(player)){
			  npc.applyHit(new Hit(player, Utils.random(100,320), HitLook.HEALED_DAMAGE));
				}
			if(!isAtWater(player)) {
				stop();
				return;
			}
			if(count++ == 10) {
				stop();
				return;
			}
		}
	}, 0, 0);
	
	}
	/**
	 * flame wall
	 */
	else if  (random == 7){
	npc.setNextForceTalk(new ForceTalk("Try to escape my flames!"));
	World.spawnTemporaryObject(fire, 6000, true);
	World.spawnTemporaryObject(fire2, 6000, true);
	World.spawnTemporaryObject(fire3, 6000, true);
	World.spawnTemporaryObject(fire4, 6000, true);
	final WorldTile center = new WorldTile(target);
	WorldTasksManager.schedule(new WorldTask() {
		int count = 0;
		@Override
		public void run() {
			Player player = (Player) target;
			if(count++ == 10) {
				if(player.withinDistance(center, 1) ) {
				delayHit(npc, 0, player, new Hit(npc, Utils.random(520,850), HitLook.REGULAR_DAMAGE));
				}
				stop();
				return;
			}
		}
	}, 0, 0);
	} else {
	npc.setNextAnimation(new Animation(24224));
	delayHit(npc, 0, player, new Hit(npc, Utils.random(50)+ player.getPlayerVars().getPAdamage(), HitLook.REGULAR_DAMAGE));
	//delayHit(npc, 1, target, getMeleeHit(npc, Utils.random(20,460) + player.getPAdamage()));
	}
	return def.getAttackDelay();
    }
}
