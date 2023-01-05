package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.Animation;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.activites.clanwars.FfaZone;
import com.rs.game.activites.clanwars.RequestController;
import com.rs.game.activities.instances.HelwyrInstance;
import com.rs.game.activities.instances.Instance;
import com.rs.game.activities.instances.TwinFuriesInstance;
import com.rs.game.activities.instances.VindictaInstance;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.world.controllers.Wilderness;
import com.rs.game.player.dialogue.Dialogue;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

import java.io.Serializable;

public class TeleportManager implements Serializable {

    private static final long serialVersionUID = 1174715551464984067L;

    private transient Player player;
    private int quickTeles;
    private boolean quickTele;
    private Teleport playerTeleport;

    public TeleportManager(){
        playerTeleport = Teleport.NORMAL;
    }

    public void setPlayer(Player player){ this.player = player;}
    public Teleport getPlayerTeleport(){ return playerTeleport;}
    public void setTeleportAnimation(Teleport tele) {this.playerTeleport = tele; }
    public boolean quickTele() { return this.quickTele;}
    public void setQuickTele(boolean set) { this.quickTele = set;}
    public int getQuickTeles(){ return this.quickTeles;}
    public void setQuickTeles(int set){ this.quickTeles = set;}

    public boolean canTeleport(String name){
        if (player.isAtWild()) {
            player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
            return false;
        }

        switch(name){
            case "Frost Dragons":
                return player.getSkills().getLevel(Skills.DUNGEONEERING) >= 85;
            case "Celestian Dragons":
                return player.getSkills().getLevel(Skills.DUNGEONEERING) >= 95;
            case "Airut":
                return player.getSkills().getLevel(Skills.SLAYER) >= 92;
            case "Jadinko Lair":
                return player.getSkills().getLevel(Skills.SLAYER) >= 80;
            case "Jungle Strykewyrms":
                return player.getSkills().getLevel(Skills.SLAYER) >= 73;
            case "Desert Strykewyrms":
                return player.getSkills().getLevel(Skills.SLAYER) >= 77;
            case "Ripper Demon":
            case "Archeron Mammoth":
            case "Wyverns":
                return player.getSkills().getLevel(Skills.SLAYER) >= 96;
            case "Mercenary Mage":
                return player.getSkills().getCombatLevel() >= 126;
            default:
                return true;
        }
    }

    public boolean startInstance(String name){
        Instance instance = null;
        switch(name){

            case "Helwyr":
                for (int i = 0; i < World.getInstances().size(); i++) {
                    if (World.getInstances().get(i) instanceof HelwyrInstance) {
                        instance = World.getInstances().get(i);
                    }
                }
                if (instance != null) {
                    instance.enterInstance(player);
                } else {
                    instance = new HelwyrInstance(player, 60, 33, 5, -1, 0, false);
                    instance.constructInstance();

                }
                return true;
            case "Vindicta":
                for (int i = 0; i < World.getInstances().size(); i++) {
                    if (World.getInstances().get(i) instanceof VindictaInstance) {
                        instance = World.getInstances().get(i);
                    }
                }
                if (instance != null) {
                    instance.enterInstance(player);
                } else {
                    instance = new VindictaInstance(player, 60, 33, 5, -1, 2, false);
                    instance.constructInstance();

                }
                return true;
            case "Twin Furies":
                for (int i = 0; i < World.getInstances().size(); i++) {
                    if (World.getInstances().get(i) instanceof TwinFuriesInstance) {
                        instance = World.getInstances().get(i);
                    }
                }
                if (instance != null) {
                    instance.enterInstance(player);
                } else {
                    instance = new TwinFuriesInstance(player, 60, 33, 5, -1, 3, false);
                    instance.constructInstance();
                }
                return true;
            case "Gregorvic":
                player.sm(Colors.red + "Gregorvic is currently disabled");
                player.getDialogueManager().finishDialogue();
                return true;
            default:
                return false;
        }
    }

    public void teleportToSkilling(SkillingTeleports teleport){
        if(!canTeleport(teleport.getName()))
            return;
        sendTeleport(teleport.getTile());
    }

    public void teleportToTraining(TrainingTeleports teleport){
        if(!canTeleport(teleport.getName()))
            return;
        sendTeleport(teleport.getWorldTile());
    }

    public void teleportToBoss(BossTeleports teleport){
        if(!canTeleport(teleport.getName()))
            return;
        for(BossTeleports teleports : wildernessBosses){
            if(teleports == teleport) {
                sendWildernessTeleport(teleport);
                return;
            }
        }
        if(startInstance(teleport.getName()))
            return;

        sendTeleport(teleport.getTile());
    }

    private void sendWildernessTeleport(BossTeleports teleport){
        player.getDialogueManager().startDialogue(new Dialogue() {
            @Override
            public void start() {
                sendOptionsDialogue(Colors.red + "You lose items in this area!", "Proceed.", "Cancel.");
            }

            @Override
            public void run(int interfaceId, int componentId) {
                if (componentId == OPTION_1) {
                    sendTeleport(teleport.getTile());
                    player.getControlerManager().startControler("Wilderness");
                }
                end();
            }

            @Override
            public void finish() {
            }
        });
    }

    public void sendTeleport(WorldTile tile){
        player.stopAll();
        if(quickTele && quickTeles > 0 && playerTeleport.getFastAnimUp() != -1){
            teleportSpell(playerTeleport.getFastAnimUp(), playerTeleport.getFastAnimDown(), playerTeleport.getFastGrapUp(), playerTeleport.getFastGrapDown(),
                    tile, 1, true);
        } else {
            teleportSpell(playerTeleport.getAnimationUp(), playerTeleport.getAnimationDown(), playerTeleport.getGraphicUp(), playerTeleport.getGraphicDown(),
                    tile, playerTeleport.getDelay(), false);
        }

    }

    private void teleportSpell(int animationIDup, int animationIDdown, int[] graphicIdup, int[] graphicIddown, WorldTile tile, int delay, boolean fast){
        long currentTime = Utils.currentTimeMillis();
        if (player.getLockDelay() > currentTime)
            return;
        if (player.isCanPvp() && player.getAttackedBy() != null
                && player.getTemporaryAttributtes().remove("obelisktele") == null
                || player.getX() >= 2956 && player.getX() <= 3067 && player.getY() >= 5512 && player.getY() <= 5630
                || (player.getX() >= 2756 && player.getX() <= 2875 && player.getY() >= 5512 && player.getY() <= 5627)) {
            player.getPackets().sendGameMessage("A magical force is blocking you from teleporting.");
            return;
        }
        if (!player.getControlerManager().processMagicTeleport(tile))
            return;

        player.stopAll();

        if(fast){
            if(quickTeles < 0)
                return;
            setQuickTeles(quickTeles - 1);
            player.lock(delay / 2);
            WorldTasksManager.schedule(new WorldTask() {

                boolean removeDamage;

                @Override
                public void run() {
                    if (!removeDamage) {
                        player.setNextWorldTile(tile);
                        player.getControlerManager().magicTeleported(1);
                        if (player.getControlerManager().getControler() == null)
                            teleControlersCheck(player, tile);
                        if (animationIDdown != -1)
                            player.setNextAnimation(new Animation(animationIDdown == -2 ? -1 : animationIDdown));
                        for(Integer graphic : graphicIddown)
                            player.setNextGraphics(new Graphics(graphic));

                        player.setNextFaceWorldTile(
                                new WorldTile(tile.getX(), tile.getY() - 1, tile.getPlane()));
                        player.setDirection(6);
                        removeDamage = true;
                    } else {
                        player.resetReceivedDamage();
                        stop();
                    }
                }
            }, delay / 2, 0);
        } else {
            if (animationIDup != -1)
                player.setNextAnimation(new Animation(animationIDup));
            for(Integer graphic : graphicIdup)
                if(graphic != -1)
                    player.setNextGraphics(new Graphics(graphic));

            player.lock(3 + delay);
            WorldTasksManager.schedule(new WorldTask() {

                boolean removeDamage;

                @Override
                public void run() {
                    if (!removeDamage) {
                        player.setNextWorldTile(tile);
                        player.getControlerManager().magicTeleported(1);
                        if (player.getControlerManager().getControler() == null)
                            teleControlersCheck(player, tile);
                        if (animationIDdown != -1)
                            player.setNextAnimation(new Animation(animationIDdown == -2 ? -1 : animationIDdown));
                        for(Integer graphic : graphicIddown)
                            player.setNextGraphics(new Graphics(graphic));
                        player.setNextFaceWorldTile(
                                new WorldTile(tile.getX(), tile.getY() - 1, tile.getPlane()));
                        player.setDirection(6);
                        removeDamage = true;
                    } else {
                        player.resetReceivedDamage();
                        stop();
                    }
                }
            }, delay, 0);

        }

    }

    public static void teleControlersCheck(Player player, WorldTile teleTile) {
        if (player.getRegionId() == 11601)
            player.getControlerManager().startControler("GodWars");
        else if (player.getRegionId() == 13626 || player.getRegionId() == 13625)
            player.getControlerManager().startControler("DungeoneeringLobby");
        else if (Wilderness.isAtWild(teleTile) || Wilderness.isAtDitch(teleTile))
            player.getControlerManager().startControler("Wilderness");
        else if (RequestController.inWarRequest(player))
            player.getControlerManager().startControler("clan_wars_request");
        else if (FfaZone.inArea(player))
            player.getControlerManager().startControler("clan_wars_ffa");
    }

    public TrainingTeleports[] slayerTeleports () {
        return new TrainingTeleports[]{
                TrainingTeleports.KURADEL_DUNGEON,
                TrainingTeleports.SLAYER_TOWER,
                TrainingTeleports.BRIMHAVEN_DUNGEON,
                TrainingTeleports.JUNGLE_STRYKEWYRM,
                TrainingTeleports.JADINKO_LAIR,
                TrainingTeleports.DESERT_STRYKEWYRM,
                TrainingTeleports.ICE_STRYKEWYRM,
                TrainingTeleports.AIRUT,
                TrainingTeleports.RIPPER_DEMON,
                TrainingTeleports.ARCHERON_MAMMOTH,
                TrainingTeleports.WYVERN
        };
    }

    public TrainingTeleports[] highTraining() {
        return new TrainingTeleports[]{
                TrainingTeleports.FREMMENIK_DUNGEON,
                TrainingTeleports.TAVERLY_DUNGEON,
                TrainingTeleports.ASCENSION_DUNGEON,
                TrainingTeleports.RUNE_DRAGON,
                TrainingTeleports.CELESTIAN_DRAGS,
                TrainingTeleports.FROST_DRAGS
        };
    }

    public TrainingTeleports[] lowTraining() {
        return new TrainingTeleports[]{
            TrainingTeleports.EAST_ROCKCRABS,
            TrainingTeleports.GLACOR_CAVE,
            TrainingTeleports.DWARF_BATTLE,
            TrainingTeleports.POLYPORE_DUNGEON,
            TrainingTeleports.ANCIENT_CAVERN,
            TrainingTeleports.OTHER_CRABS
        };
    }

    public BossTeleports[] wildernessBosses = new BossTeleports[] {
            BossTeleports.BLINK,
            BossTeleports.PARTY_DEMON,
            BossTeleports.CHAOS_ELEMENTAL,
            BossTeleports.WILDYWYRM
    };

    public BossTeleports[] getTier1Bosses(){
        return new BossTeleports[]{
                BossTeleports.SUNFREET,
                BossTeleports.BORK,
                BossTeleports.BARREL_CHEST,
                BossTeleports.WYVERN,
                BossTeleports.DAGGANOTH_KINGS,
                BossTeleports.KBD,
                BossTeleports.GIANT_MOLE,
                BossTeleports.BLINK,
                BossTeleports.CHAOS_ELEMENTAL
        };
    }

    public BossTeleports[] getTier2Bosses(){
        return new BossTeleports[]{
                BossTeleports.DARK_LORD,
                BossTeleports.GOD_WARS,
                BossTeleports.QBD,
                BossTeleports.KALPHITE_QUEEN,
                BossTeleports.HELWYR,
                BossTeleports.VINDICTA,
                BossTeleports.TWIN_FURIES,
                BossTeleports.GREGOROVIC,
                BossTeleports.WILDYWYRM,
                BossTeleports.TORMENTED_DEMON
        };
    }

    public BossTeleports[] getTier3Bosses(){
        return new BossTeleports[]{
                BossTeleports.MERCENARY_MAGE,
                BossTeleports.KALPHITE_KING,
                BossTeleports.ARAXXOR,
                BossTeleports.VORAGO,
                BossTeleports.CORPOREAL_BEAST,
                BossTeleports.PARTY_DEMON
        };
    }

    public BossTeleports[] getTier4Bosses(){
        return new BossTeleports[]{
                BossTeleports.SOLAK,
                BossTeleports.TELOS
        };
    }

    public void teleportToBoss(){
        int tab = player.getPlayerVars().getInterface3010Tab();
        int boss = player.getPlayerVars().getInterface3010Boss();
        switch(tab){
            case 1:
                teleportToBoss(getTier1Bosses()[boss]);
                break;
            case 2:
                teleportToBoss(getTier2Bosses()[boss]);
                break;
            case 3:
                teleportToBoss(getTier3Bosses()[boss]);
                break;
            case 4:
                teleportToBoss(getTier4Bosses()[boss]);
                break;
        }
    }

    public void teleportToTraining(){
        int tab = player.getPlayerVars().getInterface3009Tab();
        int boss = player.getPlayerVars().getInterface3009Boss();
        switch(tab){
            case 1:
                teleportToTraining(lowTraining()[boss]);
                break;
            case 2:
                teleportToTraining(highTraining()[boss]);
                break;
            case 3:
                teleportToTraining(slayerTeleports()[boss]);
                break;
        }
    }

}
