package com.rs.game.npc.combat.impl.riseofthesix;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class VeracsCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18545 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		
		int damage = getRandomMaxHit(npc,210, NPCCombatDefinitions.MELEE, target);
		if (Utils.random(3) == 0) {
			if (damage == 0)
				damage =  Utils.random(130);
		}
		
		npc.setNextAnimation(new Animation(18222));
		delayHit(npc, 0, target, getMeleeHit(npc, damage)); 
		return 5;
	}

}
