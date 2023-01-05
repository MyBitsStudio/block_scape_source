package com.rs.game.npc.raids.dragonburst;

import com.corrupt.raids.controllers.DragonBurstController;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class KBDragon extends NPC {

    private static final long serialVersionUID = -845933257296910036L;

    public KBDragon(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,spawned);
        setHitpoints(85000);
        getCombatDefinitions().setHitpoints(85000);
        setCapDamage(5000);
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

    @Override
    public void sendDeath(Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        Player killer = getMostDamageReceivedSourcePlayer();
        DragonBurstController cont = (DragonBurstController) killer.getControlerManager().getControler();
        setCantInteract(true);
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        cont.kbDragonDone = true;
        if(cont.eliteDone && cont.handlerDone){
            cont.finishRaid();
        } else if(!cont.eliteDone && cont.handlerDone){
            cont.enrageElite();
        } else {
            cont.enrageElite();
            cont.enrageHandler();
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
