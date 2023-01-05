package com.rs.game.npc.combat.impl.riseofthesix;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class Ahrims extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18538, 18539 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		int damage = getRandomMaxHit(npc, 86, NPCCombatDefinitions.MAGE, target);
		if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
			target.setNextGraphics(new Graphics(400, 0, 100));
			Player targetPlayer = (Player) target;
			int currentLevel = targetPlayer.getSkills().getLevel(Skills.STRENGTH);
			targetPlayer.getSkills().set(Skills.STRENGTH, currentLevel < 5 ? 0 : currentLevel - 5);
		}
		if (npc.getId() == 18539) {
			npc.setNextAnimation(new Animation(21925));
			delayHit(npc, 2, target, getMagicHit(npc, damage)); 
			World.sendProjectile(npc, target, 374, 50, 18, 50, 50, 0, 0);
			return 3;
		}
		if (Utils.random(7) == 1) {
			Player t = (target instanceof Player ? (Player) target : ((Familiar) npc).getOwner());
			
				return 1;
			
		}
		npc.setNextAnimation(new Animation(18300));
		delayHit(npc, 2, target, getMagicHit(npc, damage)); 
		World.sendProjectile(npc, target, 374, 18, 18, 50, 50, 0, 0);
		return 3;
	}

}
