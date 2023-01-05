package com.rs.game.npc.combat.impl;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class BallakCombat extends CombatScript {

	@Override
	public Object[] getKeys() { return new Object[] {10140}; }
	int[] attackEmotes = { 19563, 19562 };
	
	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		
		int style = Utils.random(100);
		
		if (style <= 25) {
			for (Entity t : npc.getPossibleTargets()) {
				Player p = (Player) t;
				p.applyHit(new Hit(npc, Utils.random(100) < 50 ? 0 : Utils.random(50, 450), HitLook.MAGIC_DAMAGE));
			}
			npc.setNextAnimation(new Animation(19561));
			startShake(npc, npc.getPossibleTargets());
		} else {
			target.applyHit(new Hit(npc, Utils.random(100) < 25 ? 0 : Utils.random(50, 450), HitLook.MELEE_DAMAGE));
			npc.setNextAnimation(new Animation(attackEmotes[Utils.random(attackEmotes.length - 1)]));
		}
		return defs.getAttackDelay();
	}

	public void startShake(final NPC npc,  final ArrayList<Entity> targets) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;
			@Override
			public void run() {
				if (loop == 0) {
					for (Entity target : targets) {
						Player p = (Player) target;
						p.applyHit(new Hit(npc, Utils.random(100) < 50 ? 0 : Utils.random(50, 450), HitLook.MAGIC_DAMAGE));
						p.getPackets().sendCameraShake(3, 25, 50, 25, 50);
					}
				} else if (loop == 1) {
					for (Entity target : targets) {
						Player p = (Player) target;
						p.getPackets().sendStopCameraShake();
						stop();
					}
				}
				loop++;
			}
		}, 0, 1);
	}
	
}