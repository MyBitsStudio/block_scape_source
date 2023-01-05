package com.rs.game.npc.raids.mazcab;

import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;

public class Jenny extends NPC {

    private static final long serialVersionUID = 5615720119785081566L;

    public Jenny(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,true);
        setHitpoints(35000);
        getCombatDefinitions().setHitpoints(35000);
        setCapDamage(Utils.random(200, 500));
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
