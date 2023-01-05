package com.rs.game.npc.raids.mazcab;

import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

public class RaidsZombieChamp extends NPC {

    private static final long serialVersionUID = -7521908470938636049L;

    public RaidsZombieChamp(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,true);
        setHitpoints(35000);
        getCombatDefinitions().setHitpoints(35000);
        setCapDamage(Utils.random(4000, 6000));
        setAtMultiArea(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        setLureDelay(5000);
        setForceTargetDistance(64);
        setForceFollowClose(false);
        setNoDistanceCheck(true);
        setIntelligentRouteFinder(true);
        this.setForceAgressive(true);
    }

}
