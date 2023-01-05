package com.rs.game.npc.combat.impl.riseofthesix;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;

import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Torags extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18544 };
	}

	@Override
	public int attack(NPC npc, Entity target) {

		int damage = getRandomMaxHit(npc, 290, NPCCombatDefinitions.MELEE, target);
		if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
			target.setNextGraphics(new Graphics(399));
			Player targetPlayer = (Player) target;
			targetPlayer.setRunEnergy(targetPlayer.getRunEnergy() > 4 ? targetPlayer.getRunEnergy() - 4 : 0);
		}
		npc.setNextAnimation(new Animation(18222));
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return 5;
	}

}
