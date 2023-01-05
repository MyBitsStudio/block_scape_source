package com.corrupt.raids.floor;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class DragonBurst extends RaidsSettings {

    private static final long serialVersionUID = 7724303034430064813L;

    public DragonBurst(Player player, Player[] fcPlayers, Raid raid) {
        super(player, fcPlayers, raid, null);
    }

}
