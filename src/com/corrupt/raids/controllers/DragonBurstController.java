package com.corrupt.raids.controllers;

import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.ForceTalk;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.raids.dragonburst.*;
import com.rs.game.player.dialogue.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class DragonBurstController extends RaidsController {

    public static int davinaStage, davinaSubStage;
    public int KBD = 50, ELITE = 21138, DRAGON_HANDLER = 20433; //BOSSES
    public int DAVINA = 25042, RAPTOR = 25859; //Mob Bosses
    public static int HYDRIX = 25684, ONYX = 25683, CELESTIAL = 25681; // MOBS
    public int REVENANT = 23608, SPIRIT = 23029;// PETS

    public boolean kbDragonDone, eliteDone, handlerDone, kbdEnrage, kbdEnraged, eliteEnrage, eliteEnraged, handlerEnrage, handlerEnraged, devinaSpawned;


    private NPC[] firstMob, secondMob, thirdMob;
    private WorldTile[] firstMobTiles, secondMobTiles, thirdMobTiles;
    private WorldTile bossTile;
    private NPC kbd, elite, handler, davina, raptor;
    private RaidsSettings raid;

    @Override
    public void start(){
        raid = (RaidsSettings) getArguments()[0];
        buildMap();
        begin();
    }

    private void buildMap(){
        firstMob = new NPC[10];
        secondMob = new NPC[10];
        thirdMob = new NPC[10];
        firstMobTiles = new WorldTile[10];
        secondMobTiles = new WorldTile[10];
        thirdMobTiles = new WorldTile[10];

        buildTiles();

    }

    private void buildTiles(){
        firstMobTiles[0] = raid.getTile(29, 23);
        firstMobTiles[1] = raid.getTile(26, 29);
        firstMobTiles[2] = raid.getTile(28, 29);
        firstMobTiles[3] = raid.getTile(26, 30);
        firstMobTiles[4] = raid.getTile(26, 27);
        firstMobTiles[5] = raid.getTile(21, 24);
        firstMobTiles[6] = raid.getTile(24, 22);
        firstMobTiles[7] = raid.getTile(23, 23);
        firstMobTiles[8] = raid.getTile(31, 31);
        firstMobTiles[9] = raid.getTile(20, 25);

        secondMobTiles[0] = raid.getTile(29, 23);
        secondMobTiles[1] = raid.getTile(26, 29);
        secondMobTiles[2] = raid.getTile(28, 29);
        secondMobTiles[3] = raid.getTile(26, 30);
        secondMobTiles[4] = raid.getTile(26, 27);
        secondMobTiles[5] = raid.getTile(21, 24);
        secondMobTiles[6] = raid.getTile(24, 22);
        secondMobTiles[7] = raid.getTile(23, 23);
        secondMobTiles[8] = raid.getTile(31, 31);
        secondMobTiles[9] = raid.getTile(20, 25);

        thirdMobTiles[0] = raid.getTile(29, 23);
        thirdMobTiles[1] = raid.getTile(26, 29);
        thirdMobTiles[2] = raid.getTile(28, 29);
        thirdMobTiles[3] = raid.getTile(26, 30);
        thirdMobTiles[4] = raid.getTile(26, 27);
        thirdMobTiles[5] = raid.getTile(21, 24);
        thirdMobTiles[6] = raid.getTile(24, 22);
        thirdMobTiles[7] = raid.getTile(23, 23);
        thirdMobTiles[8] = raid.getTile(31, 31);
        thirdMobTiles[9] = raid.getTile(20, 25);

        bossTile = raid.getTile(21, 28);
    }

    private void begin(){
        sendWelcomeMessage();
    }

    private void sendWelcomeMessage(){ //24,21
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                if(loop == 1)
                    player.setNextWorldTile(bossTile);
                if (loop == 3) {
                    raid.setStage(1);
                    raid.setSubStage(1);
                    checkStage();
                }
                if(loop == 5){
                    Dialogue.sendNPCDialogueNoContinue(player, DRAGON_HANDLER, Dialogue.ANGRY_FACE, "Go my Hydrix Mob!");
                }
                if (loop == 7) {
                    Dialogue.closeNoContinueDialogue(player);
                    player.unlock();
                    stop();
                }
                loop++;
            }
            int loop;
        }, 0, 1);


    }

    public void checkStage(){
        switch(raid.getStage()){

            case 1:
                switch(raid.getSubStage()){
                    case 1:
                        stage1();
                        break;
                    case 2:
                        stage1sub();
                        break;
                }
                break;

            case 2:
                switch(raid.getSubStage()){
                    case 1:
                        stage2();
                        break;
                    case 2:
                        stage2sub();
                        break;
                }
                break;

            case 3:
                switch(raid.getSubStage()){
                    case 1:
                        stage3();
                        break;
                    case 2:
                        stage3final();
                        break;
                }
                break;
        }
    }

    private void stage1(){

        for(int i = 0; i < firstMob.length; i++){
            firstMob[i] = new HydrixMob(HYDRIX, firstMobTiles[i], -1, true, true, raid);
        }
    }

    private void stage1sub(){
        firstMob = null;
        davina = new Davina(DAVINA, bossTile, -1, true, true, raid);
        devinaSpawned = true;
        raid.setNPCStage(0);
        raid.setNPCSubStage(1);
        raid.sendMessage(player, "Davina, take the reign. Eliminate this trespasser.");
        davina.setNextForceTalk(new ForceTalk("Time for this trash to get taken out."));
    }

    private void stage2(){
        for(int i = 0; i < secondMob.length; i++){
            secondMob[i] = new OnyxMob(ONYX, secondMobTiles[i], -1, true, true, raid);
        }

        raid.sendMessage(player, "Feel the wrath of Onyx Dragons!");
    }

    private void stage2sub(){
        raptor = new Raptor(RAPTOR, bossTile, -1, false, true, raid);
        raid.sendMessage(player, "Raptor, Devina failed us. You better not fail!");
        raid.setNPCStage(0);
        raid.setNPCSubStage(1);
    }

    private void stage3(){

        for(int i = 0; i < thirdMob.length; i++){
            thirdMob[i] = new CelestialMob(CELESTIAL, thirdMobTiles[i], -1, false, true, raid);
        }
        raid.sendMessage(player, "Time to send the Celestials out.");
    }

    private void stage3final(){

        player.lock(10);
        WorldTasksManager.schedule(new WorldTask() {
            long count = 0;
            @Override
            public void run() {
                if(count == 0) {
                    raid.sendMessage(player, "You fools! You let this amateur walk all over you.");
                }
                if(count == 1){
                    raid.sendMessage(player, "I guess I must do this myself...");
                }
                if(count == 2){
                    raid.sendMessage(player, "Call out the pets!");
                }
                if(count == 3){
                    spawnPets();
                }
                if(count == 4){
                    raid.sendMessage(player, "Time for this pest to be squashed.");
                }
                if(count == 5) {
                    raid.sendMessage(player, "Prepare to be devoured by my beautiful dragons.");
                }
                if(count == 6){
                   spawnHandler();
                   player.unlock();
                   stop();
                }
                count++;
            }
        }, 0, 1);
    }

    public void spawnPets(){
       kbd = new KBDragon(KBD, raid.getTile(30,26), -1, false, true);
       kbd.setRaidInstance(raid);

       elite = new CelestialDrag(ELITE, raid.getTile(14, 27), -1, false, true);
       elite.setRaidInstance(raid);
    }

    public void spawnHandler(){
        handler = new DragonHandler(DRAGON_HANDLER, raid.getTile(23, 33), -1, false, true);
        handler.setRaidInstance(raid);
    }

    public void finishRaid(){
        player.sm("You have finished the raid!");
    }

    public void enrageKBD(){
        kbd.setCapDamage(kbdEnraged ? (int) (kbdEnrage ? kbd.getCapDamage() * .75 : kbd.getCapDamage() * .40) : kbd.getCapDamage());
//            kbd.setBonuses(new int[]{
//
//            });
        kbd.setNextForceTalk(new ForceTalk("You fool! You shall pay!"));
    }

    public void enrageElite(){
        elite.setCapDamage(eliteEnraged ? (int) (eliteEnrage ? elite.getCapDamage() * .75 : elite.getCapDamage() * .40) : elite.getCapDamage());
//            kbd.setBonuses(new int[]{
//
//            });
        elite.setNextForceTalk(new ForceTalk("I will feast on your flesh!"));
    }

    public void enrageHandler(){
        handler.setCapDamage(handlerEnraged ? (int) (handlerEnrage ? handler.getCapDamage() * .75 : handler.getCapDamage() * .40) : handler.getCapDamage());
//            kbd.setBonuses(new int[]{
//
//            });
        handler.setNextForceTalk(new ForceTalk("My Pet!! You will PAY!"));
    }


}
