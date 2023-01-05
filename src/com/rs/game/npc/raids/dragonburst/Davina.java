package com.rs.game.npc.raids.dragonburst;

import com.corrupt.raids.RaidsSettings;
import com.corrupt.raids.controllers.DragonBurstController;
import com.rs.game.*;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.world.WorldTile;

public class Davina extends NPC {

    private static final long serialVersionUID = 2913698792161712754L;

    public Davina(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, RaidsSettings raid) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(55000);
        getCombatDefinitions().setHitpoints(55000);
        setCapDamage(1200);
        setAtMultiArea(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        this.setForceAgressive(true);
        setLureDelay(5000);
        setForceTargetDistance(64);
        setForceFollowClose(false);
        setNoDistanceCheck(false);
        setIntelligentRouteFinder(true);
        setRaidInstance(raid);
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
        raid.setStage(2);
        raid.setSubStage(1);
        setNextForceTalk(new ForceTalk("How...."));
        setNextForceTalk(new ForceTalk("How could I lose...."));
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                if (loop == 0)
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                if (loop >= defs.getDeathDelay() + 1) {
                    finish();
                    cont.checkStage();
                    stop();
                }
                loop++;
            }
            int loop;
        }, 0, 1);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        RaidsSettings raid = getRaid();
        if (isDead())
            return;
        if (getHitpoints() < 40000 && raid.getNPCStage() == 0) {
            raid.setNPCStage(1);
            raid.setNPCSubStage(1);
        } else if(getHitpoints() < 25000 && raid.getNPCStage() == 1){
            raid.setNPCStage(2);
            raid.setNPCSubStage(1);
        } else if(getHitpoints() < 10000 && raid.getNPCStage() == 2){
            raid.setNPCStage(3);
            raid.setNPCSubStage(1);
        }
    }

}
