package com.rs.game.npc.combat.impl.gwd2.twins;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.NewProjectile;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.gwd2.twinfuries.CeilingCollapse;
import com.rs.game.npc.gwd2.twinfuries.Nymora;
import com.rs.utils.Utils;

/**
 * @ausky Tom
 * @date April 14, 2017
 */
public class NymoraCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22454 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Nymora nymora = (Nymora) npc;
		if (nymora.getInstance().getPhase() == 1 && nymora.getInstance().getSpecialDelay() < Utils.currentTimeMillis()) {
			nymora.getInstance().setSpecialDelay(Utils.currentTimeMillis() + Utils.random(25000, 30000));
			new CeilingCollapse(npc, target).effect();
			nymora.getInstance().nextPhase();
			return 20;
		}

		final NewProjectile projectile = new NewProjectile(new WorldTile(nymora.getCoordFaceX(nymora.getSize()), nymora.getCoordFaceY(nymora.getSize()), nymora.getPlane()), target, 6136, 40, 40, 30, 0, 35, 0);
		nymora.setNextAnimation(new Animation(28250));
		nymora.getInstance().getPlayers().forEach(p -> p.getPackets().sendTestProjectile(projectile));

		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				try {
					delayHit(npc, 0, target, getRangeHit(npc, getRandomMaxHit(npc, Utils.random(50, 88), NPCCombatDefinitions.RANGE, target)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, projectile.getTime(), TimeUnit.MILLISECONDS);

		return 4;
	}

}