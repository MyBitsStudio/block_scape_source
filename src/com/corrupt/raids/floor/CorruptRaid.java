package com.corrupt.raids.floor;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class CorruptRaid extends RaidsSettings {

    private static final long serialVersionUID = 7550798689053146148L;

    public CorruptRaid(Player player, Player[] fcPlayers, Raid raid) {
        super(player, fcPlayers, raid, null);
        start();
    }

    private void start(){
        buildMap();
        buildConstant();
    }

    public void buildMap() {

    }

    private void buildConstant(){

    }
}
