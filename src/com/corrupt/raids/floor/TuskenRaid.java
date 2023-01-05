package com.corrupt.raids.floor;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class TuskenRaid extends RaidsSettings {

    private static final long serialVersionUID = -2085132022181132748L;

    private Player[] fcPlayers;

    public TuskenRaid(Player player, Player[] fcPlayers, Raid raid) {
        super(player, fcPlayers, raid, null);
        this.fcPlayers = fcPlayers;
        start();
    }

    private void start(){
        buildMap();
    }

    public void buildMap() {
        for (Player p : fcPlayers) {
            sendMessage(p,"As you enter the Tusken Beast sense your hostility");
            sendMessage(p,"Enter the barrier to begin.");
        }
    }
}
