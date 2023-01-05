package com.corrupt.raids.floor;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class RaidLobby extends RaidsSettings {

    private static final long serialVersionUID = 7550798689053146148L;

    public RaidLobby(Player player, Player[] fcPlayers, Raid raid, RaidsSettings raids) {
        super(player, fcPlayers, raid, raids);
    }

}
