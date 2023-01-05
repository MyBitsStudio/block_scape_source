package com.rs.game.player.content.corrupt.modes;

import com.rs.game.player.Player;

import java.io.Serializable;

public class NewSkilling implements Serializable {

    private static final long serialVersionUID = -3640538093241913878L;

    private transient Player player;

    public void setPlayer(Player player){ this.player = player;}
}
