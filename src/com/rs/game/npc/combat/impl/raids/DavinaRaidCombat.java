package com.rs.game.npc.combat.impl.raids;

import com.corrupt.raids.RaidsSettings;
import com.corrupt.raids.controllers.DragonBurstController;
import com.rs.game.*;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.raids.dragonburst.Davina;
import com.rs.game.npc.raids.dragonburst.HydrixMob;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.utils.Utils;

public class DavinaRaidCombat extends CombatScript {

    RaidsSettings raid;
    Player player;
    boolean spawned;

    @Override
    public int attack(NPC npc, Entity target) {
        final Davina dav = (Davina) npc;
        raid = dav.getRaid();
        player = (Player) target;
        switch(raid.getNPCStage()){
            case 0:
                int block = Utils.random(2);
                switch(raid.getNPCSubStage()){
                    case 1:
                        switch(block){
                            case 0:
                                spawnDragons(dav);
                                break;

                            case 1:
                                fireChain(dav);
                                break;

                            case 2:
                                phantomBreak(dav);
                                break;
                        }
                        break;

                    case 2:
                    case 3:
                        switch(block){
                            case 0:
                                phantomBreak(dav);
                                break;

                            case 1:
                                fireChain(dav);
                                break;

                            case 2:
                                heal(dav);
                                break;
                        }
                        break;
                }
                break;

            case 1:
            case 2:
            case 3:
                int stop = Utils.random(3);
                switch(raid.getNPCSubStage()){
                    case 1:
                        switch(stop){
                            case 0:
                                breakTheCycle(dav);
                                break;

                            case 1:
                            case 3:
                                fireChain(dav);
                                break;

                            case 2:
                                phantomBreak(dav);
                                break;
                        }
                        break;

                    case 2:
                        switch(stop){
                            case 0:
                                destroy(dav);
                                break;

                            case 1:
                                fireChain(dav);
                                break;

                            case 2:
                                spawnDragons(dav);
                                break;

                            case 3:
                                phantomBreak(dav);
                                break;
                        }
                        break;
                }
                break;
        }

        return 10;
    }


    @Override
    public Object[] getKeys() {
        return new Object[]{25042};
    }

    private void spawnDragons(Davina dav){
        if(!spawned){
            NPC dragon1 = new HydrixMob(DragonBurstController.HYDRIX, raid.getTile(28, 29), -1, true, true, raid);
            NPC dragon2 = new HydrixMob(DragonBurstController.HYDRIX, raid.getTile(26, 27), -1, true, true, raid);
            dav.setNextForceTalk(new ForceTalk("Come my dragons, feast on his flesh."));
            spawned = true;
        } else {
            fireChain(dav);
        }
    }

    private void fireChain(Davina dav){
        WorldTile targetTile = player.getWorldTile();
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("You all will BURN!"));
                        dav.setNextAnimation(new Animation(25967));
                        dav.setNextGraphics(new Graphics(5408));
                        break;

                    case 2:
                        for(int x = targetTile.getX(); x < targetTile.getX() + 5; x++){
                            for (int y = targetTile.getY(); y < targetTile.getY() + 5; y++){
                                World.spawnObjectTemporary(new WorldObject(95033, 10, 0, x, y, 0), 2500);
                            }
                        }
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(200,430)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        break;

                    case 4:
                        if(Utils.random(20) < 5){
                            World.spawnObjectTemporary(new WorldObject(95033, 10, 0, player.getWorldTile().getX(), player.getWorldTile().getY(), 0), 2500);
                            delayHit(dav, 0, player, new Hit(dav, Utils.random(200,430)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        }
                        dav.setNextAnimation(new Animation(-1));
                        dav.setNextGraphics(new Graphics(-1));
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);

    }

    private void breakTheCycle(Davina dav){
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Finish this fool!"));
                        dav.setNextAnimation(new Animation(15529));
                        player.lock();
                        break;

                    case 2:
                        player.setNextAnimation(new Animation(15530));
                        player.setNextGraphics(new Graphics(2196));
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        break;

                    case 3:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        break;

                    case 4:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(101,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        break;

                    case 5:
                        player.unlock();
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }

    private void phantomBreak(Davina dav){
        dav.setNextForceTalk(new ForceTalk("This is where you die!"));

        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Feel the heat!"));
                        dav.setNextAnimation(new Animation(18362));
                        dav.setNextGraphics(new Graphics(3538));
                        break;

                    case 1:
                        player.lock();
                        player.getActionManager().setActionDelay(10);
                        player.resetWalkSteps();
                        player.setNextAnimation(new Animation(-1));
                        player.setNextGraphics(new Graphics(2767));
                        player.setNextForceMovement(new ForceMovement(dav, 2, Utils.getMoveDirection(
                                dav.getCoordFaceX(player.getSize()) - player.getX(),
                                dav.getCoordFaceY(player.getSize()) - player.getY())));
                        dav.setTarget(player);
                        break;

                    case 3:
                        player.setNextWorldTile(dav);
                        break;

                    case 4:
                        dav.setNextAnimation(new Animation(18585));
                        dav.setNextGraphics(new Graphics(3485));
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        break;

                    case 5:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.REGULAR_DAMAGE));
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(300,530)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.CRITICAL_DAMAGE));
                        break;

                    case 6:
                        player.unlock();
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }

    private void heal(Davina dav){
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

    private void destroy(Davina dav){
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                switch(count){
                    case 0:
                        dav.setNextForceTalk(new ForceTalk("Destroy these fools!"));
                        dav.setNextAnimation(new Animation(18449));
                        break;

                    case 1:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.MAGIC_DAMAGE));
                        break;

                    case 3:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,220)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.MAGIC_DAMAGE));
                        break;

                    case 5:
                        delayHit(dav, 0, player, new Hit(dav, Utils.random(100,230)+ player.getPlayerVars().getPAdamage(), Hit.HitLook.MAGIC_DAMAGE));
                        stop();
                        break;
                }
                count++;
            }
        }, 0, 0);
    }
}
