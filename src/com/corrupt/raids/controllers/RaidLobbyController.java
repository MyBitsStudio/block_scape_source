package com.corrupt.raids.controllers;

import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.player.Player;

public class RaidLobbyController extends RaidsController {

    private Player leader;
    private Player[] team;
    private RaidsSettings lobby, raid;

    @Override
    public void start(){
        lobby = (RaidsSettings) getArguments()[0];
        raid = (RaidsSettings) getArguments()[1];
        leader = player;
        team = new Player[12];

    }




}
