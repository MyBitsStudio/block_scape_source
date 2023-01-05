package com.rs.game.npc.combat.impl.riseofthesix;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;


public class Dharoks extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18540 };
	}

	@Override
	public int attack(NPC npc, Entity target) {

		
		npc.setNextAnimation(new Animation(18236));
		int damage = 250;
		if (npc.getHitpoints() < 50000)
			damage *= (npc.getMaxHitpoints() - npc.getHitpoints()) / 1600;
		delayHit(npc, 0, target, getMeleeHit(npc, damage));

		return 5;
	}

}
