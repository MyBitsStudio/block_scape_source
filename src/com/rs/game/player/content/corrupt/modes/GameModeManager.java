package com.rs.game.player.content.corrupt.modes;

import com.rs.game.player.Player;

import java.io.Serial;
import java.io.Serializable;

public class GameModeManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 6360120281177395241L;

    private transient Player player;

    private GameMode gameMode;
    private ModeDifficulty difficulty;
    private boolean beta;

    public GameModeManager(){
    }

    public GameMode getGameMode(){ return gameMode;}
    public ModeDifficulty getDifficulty(){ return difficulty;}
    public void setGameMode(GameMode gameMode){ this.gameMode = gameMode;}
    public void setDifficulty(ModeDifficulty difficulty){ this.difficulty = difficulty;}
    public boolean isBeta(){return beta;}
    public void setPlayer(Player player) {this.player = player;}
    public void setBeta(boolean set) {this.beta = set;}

}
