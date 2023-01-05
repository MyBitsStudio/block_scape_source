package com.rs.game.world;

public enum WorldCurrency {

    COINS(1, "Block Coin"),
    DIAMONDS(2, "Block Diamond")

    ;

    private final int id;
    private final String name;

    WorldCurrency(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
