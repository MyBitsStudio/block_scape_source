package com.rs.game.player.cutscenes;

import com.rs.game.player.Player;
import com.rs.game.player.cutscenes.actions.*;

import java.util.ArrayList;

public class StartingCutscene extends Cutscene {
    @Override
    public CutsceneAction[] getActions(Player player) {
        ArrayList<CutsceneAction> actionsList = new ArrayList<>();
        actionsList.add(new ConstructMapAction(403, 347, 3, 3));
        actionsList
                .add(new MovePlayerAction(3, 3, 0, Player.TELE_MOVE_TYPE, -1));
        actionsList.add(new LookCameraAction(1, 1, 2000, -1));
        actionsList.add(new PosCameraAction(7, 4, 2000, 3));
        actionsList.add(new PlayerFaceTileAction(3, 2, 1));

        actionsList.add(new PlayerFaceTileAction(2, 3, 1));

        actionsList.add(new PlayerFaceTileAction(3, 2, 1));

        actionsList.add(new PlayerFaceTileAction(4, 3, 1));

        actionsList.add(new PlayerForceTalkAction("Where am I?", 1));

        actionsList.add(new PlayerForceTalkAction("Why can't I remember anything?", 1));

        actionsList.add(new LookCameraAction(5, 5, 1000, -1));
        actionsList.add(new PosCameraAction(5, 4, 2000, 3));

        actionsList.add(new InterfaceAction(115, 2));
        return actionsList.toArray(new CutsceneAction[actionsList.size()]);
    }

    @Override
    public boolean hiddenMinimap() {
        return true;
    }
}
