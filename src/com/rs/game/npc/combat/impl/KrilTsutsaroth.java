package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class KrilTsutsaroth extends CombatScript {

	private int hitcount;
	private boolean spec;

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(8)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("Attack them, you dogs!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Forward!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Death to Saradomin's dogs!"));
				break;
			case 3:
				npc.setNextForceTalk(new ForceTalk("Kill them, you cowards!"));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("The Dark One will have their souls!"));
				npc.playSound(3229, 2);
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("Zamorak, curse them!"));
				break;
			case 6:
				npc.setNextForceTalk(new ForceTalk("Rend them limb from limb!"));
				break;
			case 7:
				npc.setNextForceTalk(new ForceTalk("No retreat!"));
				break;
			case 8:
				npc.setNextForceTalk(new ForceTalk("Flay them all!"));
				break;
			}
		}
		int attackStyle = Utils.getRandom(2);
		switch (attackStyle) {
		case 0:// magic flame attack
			npc.setNextAnimation(new Animation(14962));
			npc.setNextGraphics(new Graphics(1210));
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 250, NPCCombatDefinitions.MAGE, t)));
				World.sendProjectile(npc, t, 1211, 41, 16, 41, 35, 16, 0);
				if (Utils.getRandom(4) == 0)
					t.getPoison().makePoisoned(168);
			}
			break;
		case 1:// main attack
		case 2:// melee attack
			int damage = 350;// normal
			for (Entity e : npc.getPossibleTargets()) {
				if (e instanceof Player && hitcount >= 2 && (((Player) e).getPrayer().usingPrayer(0, 19)
						|| ((Player) e).getPrayer().usingPrayer(1, 9))) {
					Player player = (Player) e;
					hitcount = -1;
					spec = false;
					damage = 420;
					npc.setNextForceTalk(new ForceTalk("YARRRRRRR!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.setPrayerDelay(Utils.getRandom(5) + 5);
					player.getPackets().sendGameMessage(
							"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
				}
				npc.setNextAnimation(new Animation(spec ? 14963 : 14968));
				delayHit(npc, 0, e, getMeleeHit(npc, getRandomMaxHit(npc, damage, NPCCombatDefinitions.MELEE, e)));
				spec = true;
			}
			break;
		}
		hitcount += 1;
		return defs.getAttackDelay();
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { 6203 };
	}
}