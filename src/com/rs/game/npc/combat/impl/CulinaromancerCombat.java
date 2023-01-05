package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;

public class CulinaromancerCombat extends CombatScript {

    @Override
    public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		// Magic
		int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		npc.setNextGraphics(new Graphics(2728));
		World.sendProjectile(npc, target, 2735, 18, 18, 50, 50, 3, 0);
		World.sendProjectile(npc, target, 2736, 18, 18, 50, 50, 20, 0);
		World.sendProjectile(npc, target, 2736, 18, 18, 50, 50, 110, 0);
		delayHit(npc, 2, target, getMagicHit(npc, damage));
		return defs.getAttackDelay();
    }

    @Override
    public Object[] getKeys() {
    	return new Object[] { "Culinaromancer" };
    }
}