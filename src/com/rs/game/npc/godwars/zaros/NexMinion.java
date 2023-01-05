package com.rs.game.npc.godwars.zaros;

import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.world.WorldTile;
import com.rs.game.activites.ZarosGodwars;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class NexMinion extends NPC {

    private boolean hasNoBarrier;

    public NexMinion(int id, WorldTile tile, int mapAreaNameHash,
	    boolean canBeAttackFromOutOfArea, boolean spawned) {
	super(id, tile, mapAreaNameHash, true, true);
	setCantFollowUnderCombat(true);
    }

    public void breakBarrier() {
	setCapDamage(Utils.random(850, 1250));
	hasNoBarrier = true;
    }

    public boolean isBarrierBroken() {
	return hasNoBarrier;
    }

    @Override
    public void handleIngoingHit(Hit hit) {
	if (!hasNoBarrier) {
	    setNextGraphics(new Graphics(1549));
	    if (hit.getSource() instanceof Player)
		((Player) hit.getSource()).getPackets().sendGameMessage(
			"The avatar is not weak enough to damage this minion.");
	    hit.setDamage(0);
	} else
	    super.handleIngoingHit(hit);
    }

    @Override
    public void processNPC() {
	if (isDead() || !hasNoBarrier)
	    return;
	if (!getCombat().process())
	    checkAgressivity();
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
	ZarosGodwars.incrementStage(ZarosGodwars.nex.getCurrentPhase());
    }
}