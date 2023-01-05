package com.rs.game.player.content.corrupt.tutorial;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.world.Graphics;
import com.rs.game.world.World;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.content.PlayerDesign;
import com.rs.game.player.content.corrupt.modes.GameMode;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.StarterMap;

import java.io.Serial;
import java.io.Serializable;

import static com.rs.game.player.content.corrupt.tutorial.TutorialStages.*;

public class BlockTutorial implements Serializable {

    @Serial
    private static final long serialVersionUID = -4476814214433795012L;
    private transient Player player;
    private TutorialStages stage;


    public BlockTutorial(){
        stage = NULL;
    }

    public void setPlayer(Player player){this.player = player;}
    public boolean hasFinished(){return stage == END;}

    public void nextStage(){
        switch(stage){
            case NULL -> stage = END;
            case START -> stage = END;
            case END -> {

            }
        }
        begin();
    }

    public void begin(){
        if(stage == NULL)
            teleport();
        if(stage == END)
            finishTutorial();
    }

    private void finishTutorial(){
        player.setCompleted();
        //Temp
        player.createPlayerWallet();
        player.getGameModeManager().setBeta(true);//BETA MODE
        //player.getPackets().closeInterface(inter);
        player.getHintIconsManager().removeUnsavedHintIcon();
        player.getPlayerVars().setDoubleXpTimer(3600000);
        player.getGlobalPlayerUpdater().generateAppearenceData();
        player.getInterfaceManager().sendTaskSystem();
        player.getInterfaceManager().openGameTab(1);
        player.getEquipment().refresh(Equipment.SLOT_AMULET, Equipment.SLOT_FEET, Equipment.SLOT_HANDS,
                Equipment.SLOT_AMULET, Equipment.SLOT_RING, Equipment.SLOT_AURA, Equipment.SLOT_CAPE,
                Equipment.SLOT_ARROWS);

        World.sendWorldMessage(Colors.red + "<img=7>News:</col> Welcome: [" + player.getDisplayName() + "] - "
                + "mode: [" + " game mode here" + "] - to " + Settings.SERVER_NAME + "!", false);

        StarterMap.getSingleton().addIP(player.getSession().getIP());
        sendReward();
    }

    private void sendReward(){
        GameMode mode = player.getGameModeManager().getGameMode();
        if(!canAddReward()){
            player.sendMessage(
                    Colors.red + "You did not receive your starter kit, you've already received it more than the maximum allowed times.");
            return;
        }

        switch(mode){

        }
    }

    private boolean canAddReward() {
        int count = StarterMap.getSingleton().getCount(player.getSession().getIP());
        return count <= Settings.MAX_STARTER_COUNT;
    }

    public void teleport() {
        player.setNextWorldTile(Settings.START_PLAYER_LOCATION);
        player.lock(5);
        WorldTasksManager.schedule(new WorldTask() {
            int tick;

            @Override
            public void run() {
                tick++;
                if (tick == 1) {
                    player.setNextGraphics(new Graphics(3018));
                    player.setNextAnimation(new Animation(16386));
                } else if (tick == 3)
                    player.setNextAnimation(new Animation(16393));
                else if (tick == 4) {
                    player.setNextWorldTile(Settings.START_PLAYER_LOCATION);
                    player.setNextAnimation(new Animation(-1));
                    PlayerDesign.open(player);
                    player.setLogedIn();
                    player.unlock();
                    stop();
                }
            }
        }, 0, 1);
    }

}

enum TutorialStages{
    NULL, START, END
}
