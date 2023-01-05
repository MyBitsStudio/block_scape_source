package com.rs.game.npc.raids.dragonburst;

import com.corrupt.raids.RaidsSettings;
import com.corrupt.raids.controllers.DragonBurstController;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class CelestialMob extends NPC {

    private static final long serialVersionUID = 5470684104458520872L;

    public CelestialMob(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, RaidsSettings raid) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(21500);
        getCombatDefinitions().setHitpoints(21500);
        setCapDamage(1000);
        setAtMultiArea(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        setRaidInstance(raid);
        this.setForceAgressive(true);
        setLureDelay(5000);
        setForceTargetDistance(64);
        setForceFollowClose(false);
        setNoDistanceCheck(false);
        setIntelligentRouteFinder(true);
    }

    @Override
    public void sendDeath(Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        Player killer = getMostDamageReceivedSourcePlayer();
        DragonBurstController cont = (DragonBurstController) killer.getControlerManager().getControler();
        RaidsSettings raid = getRaid();
        setCantInteract(true);
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        raid.setKillCount(raid.getKillCount() + 1);
        if(raid.getKillCount() >= 10){
            raid.setStage(3);
            raid.setSubStage(2);
            raid.setKillCount(0);
            cont.checkStage();
        }
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                if (loop == 0)
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                if (loop >= defs.getDeathDelay() + 1) {
                    finish();
                    stop();
                }
                loop++;
            }
            int loop;
        }, 0, 1);
    }

}
