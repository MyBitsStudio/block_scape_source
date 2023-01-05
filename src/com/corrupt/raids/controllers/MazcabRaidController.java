package com.corrupt.raids.controllers;

import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.rs.Settings;
import com.rs.game.*;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.maczabinvasion.Durzag;
import com.rs.game.npc.raids.mazcab.Jenny;
import com.rs.game.npc.raids.mazcab.RaidsSkeletons;
import com.rs.game.npc.raids.mazcab.RaidsZombieChamp;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Fishing;
import com.rs.game.player.actions.Woodcutting;
import com.rs.game.player.actions.mining.Mining;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.world.map.MapBuilder;
import com.rs.utils.Colors;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MazcabRaidController extends RaidsController {

    private RaidsSettings raid;
    public static final int exitPortal = 16150, FIRSTGATE1 = 16123, FIRSTGATE2 = 16124;
    public Item key = new Item(85);
    public WorldTile gate1, bossTile, chestTile;
    public WorldObject gate1O, chest;
    public WorldTile[] stage1, stage1r;
    public NPC[] jenny, fish, rFish;
    public NPC skelBoss;
    public boolean skellBossDone, jennyDone, skelBossStart;
    public int skill;
    public WorldObject[] tree, rTree, mine, rMine, gate1a;
    public Durzag boss;
    public int skelMinions;

    @Override
    public void start(){
        raid = (RaidsSettings) getArguments()[0];
        buildArrays();
        buildConstants();
        buildMap();
        checkStage();
    }

    private void buildArrays(){
        stage1 = new WorldTile[10];
        stage1r = new WorldTile[10];
        jenny = new NPC[10];
        gate1a = new WorldObject[10];
    }

    private void buildMap() {
        gate1O = new WorldObject(110337, 10, 2, raid.getX(24,18), raid.getY(24,18), 0);
        World.spawnObject(gate1O, true);
    }

    private void buildConstants() {
       gate1 = raid.getTile(27,31);
       bossTile = raid.getTile(28,37);
       chestTile = raid.getTile(27,33);

        stage1[0] = raid.getTile(13, 43);
        stage1[1] = raid.getTile(15, 34);
        stage1[2] = raid.getTile(5, 24);
        stage1[3] = raid.getTile(24,55);
        stage1[4] = raid.getTile(22,44);
        //stage1b2 = getTile(19,19);
        stage1[5] = raid.getTile(4,10);
        stage1[6] = raid.getTile(34,29);
        stage1[7] = raid.getTile(34,52);
        stage1[8] = raid.getTile(34,59);
        //RANDOM TILES
        stage1r[0] = raid.getTile(22, 27);//zombies stage 1
        stage1r[1] = raid.getTile(22,34);
        stage1r[2] = raid.getTile(28,36);
        stage1r[3] = raid.getTile(34,32);
        stage1r[4] = raid.getTile(29,25);
    }


    private void checkStage(){
        switch(raid.getStage()){
            case 0:
            case 1:
                startStage1();
                break;
            case 2:
                switch(player.getRaidsManager().getRaidSkill()){
                    case 1:
                        startStage1a();
                        break;
                    case 2:
                        startStage1b();
                        break;
                    case 3:
                        startStage1c();
                        break;
                }
                break;
            case 3:
                startJenny();
                break;
            case 4:
                startBossPhase1a();
                break;
            case 5:
                chestStage();
                break;
        }
    }

    @Override
    public boolean processNPCClick2(NPC npc) {
        if(npc == rFish[0] || npc == rFish[1] || npc == rFish[2] || npc == rFish[3] || npc == rFish[4] ||
                npc == rFish[5] || npc == rFish[6] || npc == rFish[7] || npc == rFish[8] || npc == rFish[9]) {
            if (!player.getInventory().containsOneItem(311)) {
                raid.sendMessage(player, "You need to have harpoon in your inventory to fish this.");
                return false;
            }
            player.getActionManager().setAction(new Fishing(Fishing.FishingSpots.RAID_HARPOON,npc));
        }
        return true;
    }

    private void chestStage() {
        player.setNextWorldTile(gate1);
        raid.sendMessage(player, "Congratulations! you have entered the chest room!");
        spawnChest();
        raid.setStage(5);
    }

    public void spawnSkelMinions() {
        WorldTile targetTile;
        for(int i = 0;i<10;i++) {
            targetTile = new WorldTile(gate1, 8);
            World.spawnNPC(21345, targetTile, -1, true, true); //fix airut minions
        }
    }

    public void startJenny1() {
        jenny[0] = new Jenny(17427, bossTile, -1, true, true);
        jenny[0].setAtMultiArea(true);
        jenny[0].setForceMultiArea(true);
        jenny[0].setForceMultiAttacked(true);
    }

    public void skelBoss() {
        skelBossStart = true;
        skelBoss = new RaidsZombieChamp(23758, bossTile, -1, true,true);
        skelBoss.setNextGraphics(new Graphics(120));
        skelBoss.setNextGraphics(new Graphics(3428));
        skelBoss.setAtMultiArea(true);
        skelBoss.setForceMultiArea(true);
        skelBoss.setForceMultiAttacked(true);
        skelBoss.setLureDelay(5000);
        skelBoss.setForceTargetDistance(64);
        skelBoss.setForceFollowClose(false);
        skelBoss.setNoDistanceCheck(true);
        skelBoss.setIntelligentRouteFinder(true);
    }

    private void clearRoom() {
        if(skill == 1)
            cleanStage1a();
        else if(skill == 2)
            cleanStage1b();
        else
            cleanStage1c();
        List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
        for (Integer npc : npcs) {
            World.getNPCs().get(npc).sendDeath(null);
        }
    }


    private void cleanStage1a(){
        for(WorldObject tree : rTree){
            if(tree != null)
                World.removeObject(tree, true);
        }
    }

    private void cleanStage1b(){
        for(WorldObject object : rMine){
            if(object != null)
                World.removeObject(object, true);
        }
    }

    private void cleanStage1c(){
        for(NPC npc : rFish)
            if(npc != null)
                npc.sendDeath(null);
    }

    /**
     * Stages here
     */

    private void startJenny(){
        player.getInventory().deleteItem(key);
        clearRoom();
        player.setNextWorldTile(gate1);
        jennyDone = false;
        startJenny1();
        raid.sendMessage(player, "Kill this boss to proceed to the next gate!");
        raid.setStage(3);
    }

    private void startStage1() {
        raid.sendMessage(player, "Kill all these Demonic Ghosts to proceed to the next Stage.");
        player.setNextWorldTile(gate1);
        raid.setStage(1);
        skellBossDone = false;
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                try {
                    if(count == 0) {
                        WorldTile randomTile = new WorldTile(gate1, 8);
                        WorldTile randomTile2 = new WorldTile(gate1, 8);
                        new RaidsSkeletons(20174, randomTile, -1, true,true).setNextGraphics(new Graphics(3428));
                        new RaidsSkeletons(20174, randomTile2, -1, true,true).setNextGraphics(new Graphics(3428));
                    }
                    if(count == 1) {
                        WorldTile randomTile = new WorldTile(gate1, 8);
                        WorldTile randomTile2 = new WorldTile(gate1, 8);
                        new RaidsSkeletons(20174, randomTile, -1, true,true).setNextGraphics(new Graphics(3428));
                        new RaidsSkeletons(20174, randomTile2, -1, true,true).setNextGraphics(new Graphics(3428));
                    }
                    if(count == 3) {
                        WorldTile randomTile = new WorldTile(gate1, 8);
                        WorldTile randomTile2 = new WorldTile(gate1, 8);
                        new RaidsSkeletons(20174, randomTile, -1, true,true).setNextGraphics(new Graphics(3428));
                        new RaidsSkeletons(20174, randomTile2, -1, true,true).setNextGraphics(new Graphics(3428));
                        stop();
                    }
                    count++;
                } catch (Throwable e) {
                    Logger.handle(e);
                }
            }
        }, 5, 1);
    }

    public void startStage1a() {
        tree = new WorldObject[10];
        rTree = new WorldObject[10];
        World.addGroundItem(new Item(1351), gate1);//bronze hatchet
        for(int i = 0; i < 10; i++)
            tree[i] = new WorldObject(112521, 10, 0, new WorldTile(gate1, 5));

        List<WorldObject> randomtreeArray = new ArrayList<>(Arrays.asList(rTree).subList(0, tree.length));
        Collections.shuffle(randomtreeArray);

        for(int i = 0; i < randomtreeArray.size(); i++) {
            rTree[i] = randomtreeArray.get(i);
        }

        for(WorldObject object : rTree) {
            System.out.println(object.getId());
            World.spawnObject(object, true);
        }
    }

    public void startStage1b() {
        mine = new WorldObject[10];
        rMine = new WorldObject[10];

        World.addGroundItem(new Item(1265), gate1);//bronze pickaxe
        for(int i = 0; i < 9; i++)
            mine[i] = new WorldObject(112493, 10, 0, new WorldTile(gate1, 5));

        mine[9] = new WorldObject(112493, 10, 0, raid.getTile(18,33));
        List<WorldObject> solution = new ArrayList<>(Arrays.asList(mine));
        Collections.shuffle(solution);

        for(int i = 0; i < solution.size(); i++)
            rMine[i] = solution.get(i);

        for(WorldObject r : rMine) {
            if(r != null) {
                System.out.println(r.getId());
                World.spawnObject(r, true);
            }
        }
    }

    public void startStage1c() {
        fish = new NPC[10];
        rFish = new NPC[10];

        World.addGroundItem(new Item(311), gate1);//harpoon
        for(int i = 0; i < 10; i++)
            fish[i] = new NPC(322, new WorldTile(gate1, 5), -1, true,true);

        List<NPC> randomfishArray = new ArrayList<>(Arrays.asList(fish));
        Collections.shuffle(randomfishArray);

        for(int i = 0; i < 10; i++)
            rFish[i] = randomfishArray.get(i);

    }

    //objects

    public void startObject1(WorldObject rock) {
        World.addGroundItem(new Item(85), rock);//key
        player.sm(Colors.green+"[RAIDS] Congratulations! you have found the key on this rock! you can now proceed to the next gate.");
    }
    public void startObject2(WorldObject rock) {
        World.sendGraphics(player, new Graphics(1005),rock);
        player.setNextAnimation(new Animation(11051));
        player.applyHit(new Hit(player, Utils.random(4000, 5000), Hit.HitLook.CANNON_DAMAGE));
        player.setNextForceTalk(new ForceTalk("Fuck that hurts!"));
    }
    public void startObject3() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("Fuck!!"));
    }
    public void startObject4(WorldObject rock) {
        World.spawnNPC(20174, rock, - 1, true, true);
    }
    public void startObject5(WorldObject rock) {
        World.addGroundItem(new Item(26313), rock);
    }
    public void startObject6(WorldObject rock) {
        World.addGroundItem(new Item(26313), rock);
    }
    public void startObject7(WorldObject rock) {
        World.spawnNPC(20174, rock, - 1, true, true); //rock crab
    }
    public void startObject8() {
        player.applyHit(new Hit(null,300, Hit.HitLook.HEALED_DAMAGE));
    }
    public void startObject9() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("ouch!"));
    }
    public void startObject10(WorldObject rock) {
        World.spawnNPC(20174, rock, - 1, true, true); //rock crab
    }

    //Trees

    public void startTree1(WorldObject tree) {
        World.addGroundItem(new Item(85), tree);//key
        player.sm(Colors.green+"[RAIDS] Congratulations! you have found the key on this Tree! you can now proceed to the next gate.");
    }
    public void startTree2(WorldObject tree) {
        World.sendGraphics(player, new Graphics(1005),tree);
        player.setNextAnimation(new Animation(11051));
        player.applyHit(new Hit(player, Utils.random(4000, 5000), Hit.HitLook.CANNON_DAMAGE));
        player.setNextForceTalk(new ForceTalk("Fuck, that hurts..."));
    }
    public void startTree3() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("Fuck!!"));
    }
    public void startTree4( WorldObject tree) {
        World.spawnNPC(20174, tree, - 1, true, true);
    }
    public void startTree5( WorldObject tree) {
        World.addGroundItem(new Item(26313), tree);
    }
    public void startTree6(WorldObject tree) {
        World.addGroundItem(new Item(26313), tree);
    }
    public void startTree7(WorldObject tree) {
        World.spawnNPC(20174, tree, - 1, true, true); //tree crab
    }
    public void startTree8() {
        player.applyHit(new Hit(null,300, Hit.HitLook.HEALED_DAMAGE));
    }
    public void startTree9() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("ouch!"));
    }
    public void startTree10(WorldObject tree) {
        World.spawnNPC(20174, tree, - 1, true, true); //rock crab
    }

    //fish

    public void startFish1(NPC spot) {
        World.addGroundItem(new Item(85), spot);//key
        player.sm(Colors.green+"[RAIDS] Congratulations! you have found the key on this Fishing spot! you can now proceed to the next gate.");
    }
    public void startFish2(NPC spot) {
        World.sendGraphics(player, new Graphics(1005),spot);
        player.setNextAnimation(new Animation(11051));
        player.applyHit(new Hit(player, Utils.random(4000, 5000), Hit.HitLook.CANNON_DAMAGE));
        player.setNextForceTalk(new ForceTalk("that hurts!"));
    }
    public void startFish3() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("ahhhh!"));
    }
    public void startFish4( NPC spot) {
        World.spawnNPC(15222, spot, - 1, true, true);
    }
    public void startFish5(NPC spot) {
        World.addGroundItem(new Item(26313), spot);
    }
    public void startFish6(NPC spot) {
        World.addGroundItem(new Item(26313), spot);
    }
    public void startFish7(NPC spot) {
        World.spawnNPC(20174, spot, - 1, true, true);
    }
    public void startFish8() {
        player.applyHit(new Hit(null,300, Hit.HitLook.HEALED_DAMAGE));
    }
    public void startFish9() {
        player.getPoison().makePoisoned(400);
        player.setNextForceTalk(new ForceTalk("ouch!"));
    }
    public void startFish10(NPC spot) {
        World.spawnNPC(20174, spot, - 1, true, true);
    }

    public void camShake() {
        WorldTasksManager.schedule(new WorldTask() {
            int loop = 0;
            public void run() {
                if (loop == 0) {
                    player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
                } else if (loop == 1) {
                    player.getPackets().sendStopCameraShake();
                }
                loop++;
            }
        }, 0, 0);
    }

    public void camShakeEvent(NPC npc) {
        final int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
        final WorldTile center = new WorldTile(npc.getX()
                + Utils.DIRECTION_DELTA_X[dir] * 5, npc.getY()
                + Utils.DIRECTION_DELTA_Y[dir] * 5, 0);
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;
            @Override
            public void run() {
                for (Player player : World.getPlayers()) { // lets just loop
                    // all players
                    // for massive
                    // moves
                    if (Utils.DIRECTION_DELTA_X[dir] == 0) {
                        if (player.getX() != center.getX())
                            continue;
                    }
                    if (Utils.DIRECTION_DELTA_Y[dir] == 0) {
                        if (player.getY() != center.getY())
                            continue;
                    }
                    if (Utils.DIRECTION_DELTA_X[dir] != 0) {
                        if (Math.abs(player.getX() - center.getX()) > 5)
                            continue;
                    }
                    if (Utils.DIRECTION_DELTA_Y[dir] != 0) {
                        if (Math.abs(player.getY() - center.getY()) > 5)
                            continue;
                    }
                    player.applyHit(new Hit(npc, Utils.random(50),
                            Hit.HitLook.REGULAR_DAMAGE));
                }
                if (count++ == 5) {
                    stop();
                }
            }
        }, 0, 0);
        World.sendProjectile(npc, center, 2788, 0, 0, 5, 35, 0, 0);
    }

    public void sendBoss(Player player) {//14816
        boss = new Durzag(21335, bossTile, - 1, true, true);
        boss.setAtMultiArea(true);
        boss.setForceMultiAttacked(true);
        boss.setForceMultiArea(true);
        player.sm("The boss has risen from its minions death bodies!");
    }

    private void startBossPhase1a() {
        player.setNextWorldTile(gate1);
        raid.sendMessage(player, "Challenge Boss phase 1 started! kill all this minions to proceed to the next phase!");
        spawnSkelMinions();
        raid.setStage(4);
    }

    public void endRaidsEvent(WorldObject box,Player p) {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if(p.getRaidsManager() == null)
                    stop();
                WorldTile targetTile = chest;
                targetTile = new WorldTile(box, 4);
                World.sendGraphics(p, new Graphics(438), targetTile);
                WorldTile target = targetTile;
                WorldTasksManager.schedule(new WorldTask(){
                    @Override
                    public void run() {
                        World.sendGraphics(p, new Graphics(416), target);
                        if(p.getX() == target.getX() && p.getY() == target.getY()) {
                            randomDamage(p);
                            p.getPackets().sendCameraShake(3, 25, 50, 25, 50);
                        }
                        stop();
                    }
                }, 0, 8);
            }
        }, 0, 1);
    }

    public void endRaidsEvent2(WorldObject box,Player p) {
        WorldTile chest = raid.getTile(51,38);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if(p.getRaidsManager() == null)
                    stop();
                WorldTile targetTile = chest;
                targetTile = new WorldTile(box, 8);
                World.sendGraphics(p, new Graphics(438), targetTile);
                WorldTile target = targetTile;
                WorldTasksManager.schedule(new WorldTask(){
                    @Override
                    public void run() {
                        World.sendGraphics(p, new Graphics(416), target);
                        if(p.getX() == target.getX() && p.getY() == target.getY()) {
                            randomDamage(p);
                            p.getPackets().sendCameraShake(3, 25, 50, 25, 50);
                        }
                        stop();
                    }
                }, 0, 8);
            }
        }, 19, 2);
    }

    public void endRaidsEvent3(WorldObject box,Player p) {
        WorldTile chest = raid.getTile(51,38);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if(p.getRaidsManager() == null)
                    stop();
                WorldTile targetTile = chest;
                targetTile = new WorldTile(box, 12);
                World.sendGraphics(p, new Graphics(438), targetTile);
                WorldTile target = targetTile;
                WorldTasksManager.schedule(new WorldTask(){
                    @Override
                    public void run() {
                        World.sendGraphics(p, new Graphics(416), target);
                        if(p.getX() == target.getX() && p.getY() == target.getY()) {
                            randomDamage(p);
                            p.getPackets().sendCameraShake(3, 25, 50, 25, 50);
                        }
                        stop();
                    }
                }, 0, 8);
            }
        }, 48, 3);
    }

    private void randomDamage(Player p) {
        WorldTasksManager.schedule(new WorldTask(){
            @Override public void run() {
                int randomhits = Utils.random(6);
                if(randomhits == 0)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.MELEE_DAMAGE));
                else if(randomhits == 1)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.RANGE_DAMAGE));
                else if(randomhits == 2)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.MAGIC_DAMAGE));
                else if(randomhits == 3)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.CANNON_DAMAGE));
                else if(randomhits == 4)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.POISON_DAMAGE));
                else if(randomhits == 5)
                    p.applyHit(new Hit(null, Utils.random(200,500), Hit.HitLook.REGULAR_DAMAGE));
                p.getPackets().sendStopCameraShake();
                stop();
            }
        }, 0, 0);
    }

    public void cameraShake(Player p) {
        WorldTasksManager.schedule(new WorldTask() {
            int loop = 0;
            public void run() {
                if (loop == 0) {
                    p.getPackets().sendCameraShake(3, 25, 50, 25, 50);

                } else if (loop == 1) {

                }
                loop++;
            }
        }, 0, 0);
    }

    public void senddrop(WorldObject chest, Player player) {

    }

    public void setDirection(int direction) {
        player.getRaidsManager().setRaidSkill(direction);
    }

    public void spawnChest(NPC npc) {
        chest = new WorldObject(16135, 10, 0, npc);
        World.spawnObject(chest,true);
    }

    public void spawnChest() {
        chest = new WorldObject(16135, 10, 0, chestTile);
        World.spawnObject(chest,true);
    }

    private boolean compare(WorldObject object, WorldObject object2) {
        return object.getX() == object2.getX() && object.getY() == object2.getY();
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if(object.getId() == exitPortal) {
            destroyRaids(true);
            return true;
        }
        //first GATE FROM START ROOM ENTERING ZOMBIES
        if((object.getId() == FIRSTGATE1 || object.getId() == FIRSTGATE2) && raid.getStage() == 0) {
            startStage1();
            return true;
        }
        //ENTERING STAGE 2 WOODCUTTING // FIRST STAGE
        if(object.getId() == gate1O.getId() && skellBossDone && raid.getStage() == 1) {
            randomizer();
            return true;
        }

        //1A1 JENNY
        if(compare(object,gate1O) && player.getInventory().containsItem(key) && raid.getStage() == 2) {
            startJenny();
            return true;
        }

        if(compare(object,gate1O) && jennyDone && raid.getStage() == 3) {
            startBossPhase1a();
            return true;
        }
        if(raid.getStage() == 5 && object.getId() == 16135) {
            //raids.senddrop(object,player);
            endRaidsEvent(object,player);
            endRaidsEvent2(object,player);
            endRaidsEvent3(object,player);
            raid.setStage(6);
            return true;
        }

        if(compare(object,gate1O) && raid.getStage() == 6) {
            destroyRaids(true);
            return true;
        }
        //GEM ROCKS MINING
        if(raid.getStage() == 2 &&
                (object == rMine[0] || object == rMine[1] || object == rMine[2] || object == rMine[3]
                        || object == rMine[4] || object == rMine[5] || object == rMine[6] || object == rMine[7]
                        || object == rMine[8] || object == rMine[9])) {
            if (!player.getInventory().containsOneItem(15259, 1275, 1271, 1273, 1269, 1267, 1265, 13661)) {
                raid.sendMessage(player, "You need to have pickaxe in your inventory to mine this.");
                return false;
            }
            System.out.println("Mine working?");
            player.getActionManager().setAction(new Mining(object, Mining.RockDefinitions.RAID_ROCKS));
            return true;
        }
        //WOODCUTTING
        if(raid.getStage() == 2 &&
                (object == rTree[0] || object == rTree[1] || object == rTree[2] || object == rTree[3]
                        || object == rTree[4] || object == rTree[5] || object == rTree[6] || object == rTree[7]
                        || object == rTree[8] || object == rTree[9] )) {
            if (!player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661)) {
                raid.sendMessage(player, "You need to have hatchet in your inventory to cut this.");
                return false;
            }
            System.out.println("Wood working?");
            player.getActionManager().setAction(new Woodcutting(object, Woodcutting.TreeDefinitions.RAID_TREE));
            return true;
        }
        return false;
    }

    private void randomizer() {
        int randomizer = Utils.getRandom(2);
        if(randomizer == 1) {
            startStage1a();
            raid.sendMessage(player,"Illegal Logger! find the key on one of these trees!");
            raid.setStage(2);
            setDirection(1);
        }else if(randomizer == 2){
            startStage1b();
            raid.sendMessage(player,"Minesweeper! find the key on one of these rocks!");
            raid.setStage(2);
            setDirection(2);
        }else {
            startStage1c();
            raid.sendMessage(player,"Smells fishy! find the key on one of these fishing spot!");
            raid.setStage(2);
            setDirection(3);
        }


    }

    public void destroyRaids(boolean portal) {
        clean();
        exit(player,portal);
        removeControler();
    }

    private void clean() {
        player.setAtMultiArea(false);
        player.setForceMultiArea(false);
        player.getRaidsManager().setLives(0);
        removeControler();
    }


    private void exit(Player player, boolean portal) {
        if(portal) {
            player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
            removeControler();
        }
        if(raid.boundChunks != null)
            MapBuilder.destroyMap(raid.boundChunks[0], raid.boundChunks[1], 8, 8);
        player.getRaidsManager().setRaidSettings(null);
    }
}
