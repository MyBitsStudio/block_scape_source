package com.rs.game.npc.combat.impl.riseofthesix;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Guthans extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18541, 18542 };
	}

	@Override
	public int attack(NPC npc, Entity target) {

		int damage = getRandomMaxHit(npc, 210, NPCCombatDefinitions.MELEE, target);
		int healDamage = damage;
		if (damage > 0 && Utils.random(3) == 0) {
			target.setNextGraphics(new Graphics(398));
			if (target instanceof Player) {
				Player player = (Player) target;
				if (player.getPrayer().isMeleeProtecting())
					healDamage = damage / 2;
			}
			npc.heal(healDamage);
		}
		npc.setNextAnimation(new Animation(18224));
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return 3;
	}

}
