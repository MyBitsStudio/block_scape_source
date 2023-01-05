package com.rs.game.npc.raids.dragonburst;

import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

public class CelestialDrag extends NPC {

    public CelestialDrag(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(85000);
        getCombatDefinitions().setHitpoints(85000);
        setCapDamage(Utils.random(2000));
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
