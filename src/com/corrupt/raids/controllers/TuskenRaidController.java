package com.corrupt.raids.controllers;

import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.pet.Pets;

import java.util.List;

public class TuskenRaidController extends RaidsController {

    public RaidsSettings raid;
    public NPC[] bossNPCs;

    public int Durzag = 21335, Tuz = 21336, Krar = 21337, Airut = 18621, Barrier = 97765, Chest = 100221;

    private static final Object THHAAR_MEJ_JAL = 13633;


    @Override
    public void start() {
        raid = (RaidsSettings) getArguments()[0];
        bossNPCs = new NPC[3];
        raid.setStarted(false);
        raid.setStage(0);
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if (object.getId() == Chest) {
            lootChest();
        }
        if (object.getId() == Barrier && !raid.hasStarted()) {
            passBarrier();
        }
        return false;
    }

    @Override
    public void process() {
        if (raid.isSpawned()) {
            List<Integer> npcsInArea = World.getRegion(player.getRegionId()).getNPCsIndexes();
            if (npcsInArea == null || npcsInArea.isEmpty()) {
                raid.setSpawned(false);
                raid.setStage(raid.getStage() + 1);
                nextWave(raid.getStage());
            }
        }

    }

    public void nextWave(int waveId){
        if(waveId == 1){
            player.getPackets().sendGameMessage("Congratulations! You've defeated the Tusken beast.");
            player.getPackets().sendGameMessage("You now have access to the chest.");
            World.spawnObject(new WorldObject(Chest, 10, 3, raid.getTile(19, 40)), true);
        }
    }

    public void lootChest() {

        player.sm("YAY!");
    }

    private void passBarrier() {
        if (player.getFamiliar() != null || player.getPet() != null || Pets.hasPet(player)) {
            player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL,
                    "Pets and Familiars are not allowed in Minigames or your account will Reset to 0!"
                            + " Please Dismiss your pet/familiar and remove your pet/pouches in your inventory");
            return;
        }
        player.setForceMultiArea(true);
        player.lock(2);
        tuskenBros1();
        tuskenBros2();
        tuskenBros3();
        player.setNextWorldTile(raid.getTile(19, 33));  //14, 21  17, 50
        raid.setStarted(true);
    }

    public void tuskenBros1() {
        bossNPCs[0] = new NPC(Durzag, raid.getTile(19, 38), -1, true, true);   //26, 52
        bossNPCs[0].setForceMultiArea(true);
        bossNPCs[0].setForceAgressive(true);
        bossNPCs[0].setForceTargetDistance(64);
        raid.setSpawned(true);
    }

    public void tuskenBros2() {
        bossNPCs[1] = new NPC(Tuz, raid.getTile(15, 36), -1, true, true);
        bossNPCs[1].setForceMultiArea(true);
        bossNPCs[1].setForceAgressive(true);
        bossNPCs[1].setForceTargetDistance(64);
        raid.setSpawned(true);
    }

    public void tuskenBros3() {
        bossNPCs[2] = new NPC(Krar, raid.getTile(23, 36), -1, true, true);  //26, 48
        bossNPCs[2].setForceMultiArea(true);
        bossNPCs[2].setForceAgressive(true);
        bossNPCs[2].setForceTargetDistance(64);
        raid.setSpawned(true);
    }

    public void exitCave(int type) {
        if (type == 1) {
            raid.setStarted(false);
            player.setForceMultiArea(false);
            player.setNextWorldTile(new WorldTile(player.getHomeTile()));
        }
        removeControler();
    }

    @Override
    public void magicTeleported(int type) {
        player.setForceMultiArea(false);
        raid.setStarted(false);
        removeControler();
    }
}
