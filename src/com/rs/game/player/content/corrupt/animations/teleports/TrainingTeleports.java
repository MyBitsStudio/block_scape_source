package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.world.WorldTile;

public enum TrainingTeleports {

    //LOW
    EAST_ROCKCRABS("Rockcrabs", new WorldTile(2710, 3710, 0)),
    GLACOR_CAVE("Glacor Cave", new WorldTile(4181, 5726, 0)),
    DWARF_BATTLE("Dwarf Battlefield", new WorldTile(1519, 4704, 0)),
    POLYPORE_DUNGEON("Polypore Dungeon", new WorldTile(4625, 5457, 3)),
    ANCIENT_CAVERN("Ancient Cavern", new WorldTile(1763, 5365, 1)),
    OTHER_CRABS("Other Crabs", new WorldTile(2672, 3710, 0)),

    //High
    FREMMENIK_DUNGEON("Fremennik Dungeon", new WorldTile(2808, 10002, 0)),
    TAVERLY_DUNGEON("Taverley Dungeon", new WorldTile(2884, 9799, 0)),
    ASCENSION_DUNGEON("Ascension Dungeon", new WorldTile(2508, 2886, 0)),
    RUNE_DRAGON("Rune Dragons", new WorldTile(2367, 3356, 0)),
    CELESTIAN_DRAGS("Celestian Dragons", new WorldTile(2285, 5972, 0)),
    FROST_DRAGS("Frost Dragons", new WorldTile(1298, 4510, 0)),

    //Slayer
    KURADEL_DUNGEON("Kuradel Dungeon", new WorldTile(1690, 5286, 1)),
    SLAYER_TOWER("Slayer Tower", new WorldTile(3423, 3543, 0)),
    BRIMHAVEN_DUNGEON("Brimhaven Dungeon", new WorldTile(2699, 9564, 0)),
    JUNGLE_STRYKEWYRM("Jungle Strykewyrms", new WorldTile(2452, 2911, 0)),
    JADINKO_LAIR("Jadinko Lair", new WorldTile(3012, 9274, 0)),
    DESERT_STRYKEWYRM("Desert Strykewyrms", new WorldTile(3356, 3160, 0)),
    ICE_STRYKEWYRM("Ice Strykewyrms", new WorldTile(3435, 5648, 0)),
    AIRUT("Airut", new WorldTile(1641, 5317, 0)),
    RIPPER_DEMON("Ripper Demon", new WorldTile(5152, 7584, 0)),
    ARCHERON_MAMMOTH("Archeron Mammoth", new WorldTile(3424, 4378, 0)),
    WYVERN("Wyverns", new WorldTile(5158, 7536, 0)),


    ;

    private String name;
    private WorldTile tile;

    TrainingTeleports(String name, WorldTile tile){
        this.name = name;
        this.tile = tile;
    }

    public String getName(){ return name; }
    public WorldTile getWorldTile(){ return tile; }


}
