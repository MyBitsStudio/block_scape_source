package com.rs.game.npc.raids.mazcab;

import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

public class RaidsSkeletons extends NPC {

    private static final long serialVersionUID = 1161925690845557210L;

    public RaidsSkeletons(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(3000);
        getCombatDefinitions().setHitpoints(3000);
        setCapDamage(Utils.random(1000));
        setAtMultiArea(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        this.setForceAgressive(true);
        setLureDelay(5000);
        setForceTargetDistance(64);
        setForceFollowClose(false);
        setNoDistanceCheck(false);
        setIntelligentRouteFinder(true);
    }

}
