package com.corrupt.raids.floor;

import com.corrupt.raids.Raid;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class CorruptedCrystal extends RaidsSettings {

    private static final long serialVersionUID = -6712616075881982700L;

    public CorruptedCrystal(Player player, Player[] fcPlayers, Raid raid) {
        super(player, fcPlayers, raid, null);
    }
}
