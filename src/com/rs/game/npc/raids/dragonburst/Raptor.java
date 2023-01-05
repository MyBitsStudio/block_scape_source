package com.rs.game.npc.raids.dragonburst;

import com.corrupt.raids.RaidsSettings;
import com.corrupt.raids.controllers.DragonBurstController;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class Raptor extends NPC {

    private static final long serialVersionUID = -1398486381615977674L;

    public Raptor(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, RaidsSettings raid) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(95000);
        getCombatDefinitions().setHitpoints(95000);
        setCapDamage(Utils.random(1000, 2200));
        setAtMultiArea(true);
        setForceMultiArea(true);
        setForceMultiAttacked(true);
        this.setForceAgressive(true);
        setLureDelay(5000);
        setRaidInstance(raid);
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
        raid.setStage(2);
        raid.setSubStage(1);
        setNextForceTalk(new ForceTalk("Forgive me ... "));
        setNextForceTalk(new ForceTalk("I could not win this one ... "));
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
        if (getHitpoints() < 50000 && raid.getNPCStage() == 0) {
            raid.setNPCStage(1);
            raid.setNPCSubStage(1);
        } else if(getHitpoints() < 30000 && raid.getNPCStage() == 1){
            raid.setNPCStage(2);
            raid.setNPCSubStage(1);
        } else if(getHitpoints() < 15000 && raid.getNPCStage() == 2){
            raid.setNPCStage(3);
            raid.setNPCSubStage(1);
        }
    }

}
