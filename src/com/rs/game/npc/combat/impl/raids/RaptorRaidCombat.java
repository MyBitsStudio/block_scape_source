package com.rs.game.npc.combat.impl.raids;

import com.corrupt.raids.RaidsSettings;
import com.rs.game.*;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.raids.dragonburst.Raptor;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.world.Graphics;
import com.rs.utils.Utils;

public class RaptorRaidCombat extends CombatScript {

    RaidsSettings raid;
    Player player;

    @Override
    public int attack(NPC npc, Entity target) {
        final Raptor dav = (Raptor) npc;
        raid = dav.getRaid();
        player = (Player) target;
        switch(raid.getNPCStage()){
            case 0:
            case 1:
                int block = Utils.random(2);
                switch(raid.getNPCSubStage()){
                    case 1:
                        switch(block){
                            case 0:
                                destroy(dav);
                                break;

                            case 1:
                            case 2:
                                slaughter(dav);
                                break;
                        }
                        break;

                    case 2:
                    case 3:
                        switch(block){
                            case 0:
                                slaughter(dav);
                                break;

                            case 1:
                                destroy(dav);
                                break;

                            case 2:
                                heal(dav);
                                break;
                        }
                        break;
                }
                break;


            case 2:
            case 3:
                int stop = Utils.random(3);
                switch(raid.getNPCSubStage()){
                    case 1:
                        switch(stop){
                            case 0:
                                destroy(dav);
                                break;

                            case 1:
                                breakHell(dav);
                                break;

                            case 2:
                            case 3:
                                slaughter(dav);
                                break;
                        }
                        break;

                    case 2:
                        switch(stop){

                            case 1:
                                breakHell(dav);
                                break;

                            case 2:
                                heal(dav);
                                break;

                            case 3:
                            case 0:
                                destroy(dav);
                                break;
                        }
                        break;
                }
                break;
        }

        return 5;
    }

    private void breakHell(Raptor dav) {
        dav.setNextForceTalk(new ForceTalk("Time to die!"));
        player.stopAll();
        player.lock(5);
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Bring me power!"));
                        dav.setNextAnimation(new Animation(18098));
                        dav.setNextGraphics(new Graphics(3616));
                        break;


                    case 2:
                        player.getActionManager().setActionDelay(10);
                        player.resetWalkSteps();
                        player.setNextAnimation(new Animation(-1));
                        player.setNextGraphics(new Graphics(2767));
                        player.setNextForceMovement(new ForceMovement(dav, 2, Utils.getMoveDirection(
                                dav.getCoordFaceX(player.getSize()) - player.getX(),
                                dav.getCoordFaceY(player.getSize()) - player.getY())));
                        dav.setTarget(player);
                        break;

                    case 4:
                        dav.setNextAnimation(new Animation(18175));
                        dav.setNextGraphics(new Graphics(3583));
                        delayHit(dav, 2, player, new Hit(dav, Utils.random(500,800)+player.getPlayerVars().getPAdamage(), Hit.HitLook.MELEE_DAMAGE));
                        break;

                    case 5:
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }

    private void slaughter(Raptor dav) {
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Slaughter the house."));
                        dav.setNextAnimation(new Animation(18162));
                        break;

                    case 1:
                        delayHit(dav, 2, player, new Hit(dav, Utils.random(320,400)+player.getPlayerVars().getPAdamage(), Hit.HitLook.MELEE_DAMAGE));
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }

    private void destroy(Raptor dav) {
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Well, time to destroy"));
                        dav.setNextAnimation(new Animation(18591));
                        break;

                    case 1:
                        delayHit(dav, 2, player, new Hit(dav, Utils.random(250,530)+player.getPlayerVars().getPAdamage(), Hit.HitLook.MELEE_DAMAGE));
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);

    }


    @Override
    public Object[] getKeys() {
        return new Object[]{25859};
    }

    private void heal(Raptor dav){
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("I need to heal!"));
                        dav.setNextAnimation(new Animation(18362));
                        dav.setNextGraphics(new Graphics(3538));
                        break;

                    case 1:
                        dav.heal(Utils.random(500, 2500));
                        break;

                    case 3:
                        dav.heal(Utils.random(1000, 5000));
                        break;

                    case 5:
                        dav.heal(Utils.random(2500, 7500));
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }




}
