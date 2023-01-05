package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.world.WorldTile;

public enum BossTeleports {

    //Tier 1
    //MASUTA(),
    SUNFREET("Sunfreet", new WorldTile(3535, 5189, 0), null),
    BORK("Bork", new WorldTile(3143, 5545, 0), null),
    BARREL_CHEST("BarrelChest", new WorldTile(3873, 4700, 0), null),
    WYVERN("Wyvern", new WorldTile(5158, 7536, 0), null),
    DAGGANOTH_KINGS("Dagganoth Kings", new WorldTile(2900, 4449, 0), null),
    KBD("King Black Dragon", new WorldTile(2273, 4681, 0), null),
    GIANT_MOLE("Giant Mole", new WorldTile(1750, 5243, 0), null),
    BLINK("Blink", new WorldTile(3064, 3951, 0), null),
    CHAOS_ELEMENTAL("Chaos Elemental", new WorldTile(3143, 3823, 0), null),

    //Tier 2
    DARK_LORD("Dark Lord", new WorldTile(3809, 4727, 0), null),
    GOD_WARS("God Wars", new WorldTile(1750, 5243, 0), "GodWars"),
    QBD("Queen Black Dragon", new WorldTile(1195, 6499, 0), null),
    KALPHITE_QUEEN("Kalphite Queen", new WorldTile(3479, 9488, 0), null),
    HELWYR("Helwyr", null, null),
    VINDICTA("Vindicta", null, null),
    TWIN_FURIES("Twin Furies", null, null),
    GREGOROVIC("Gregorvic", null, null),
    WILDYWYRM("Wildywyrm", new WorldTile(3158, 3953, 0), null),
    TORMENTED_DEMON("Tormented Demon", new WorldTile(2571, 5735, 0), null),


    //Tier 3
    //MAGISTER(),
    MERCENARY_MAGE("Mercenary Mage", new WorldTile(3210, 5477, 0), null),
    KALPHITE_KING("Kalphite King", new WorldTile(2974, 1654, 0), null ),
    ARAXXOR("Araxxor", new WorldTile(4512, 6289, 1), null),
    VORAGO("Vorago", new WorldTile(2972, 3430, 0), null),
    CORPOREAL_BEAST("Corporeal Beast",  new WorldTile(2966, 4383, 2), null),
    PARTY_DEMON("Party Demon", new WorldTile(3105, 3961, 0), null),


    //Tier 4
    TELOS("Telos", new WorldTile(3851, 7051, 0), null),
    SOLAK("Solak", new WorldTile(4128, 3242, 0), null),

    ;

    private String name, controller;
    private WorldTile tile;
    private int id;

    BossTeleports(String name, WorldTile tile, String controller){
        this.name = name;
        this.tile = tile;   
        this.controller = controller;
        this.id = id;
    }

    public String getName() { return name; }
    public WorldTile getTile() { return tile; }
    public int getId(){ return id; }



}
