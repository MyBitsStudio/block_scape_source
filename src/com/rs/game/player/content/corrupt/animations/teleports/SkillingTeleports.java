package com.rs.game.player.content.corrupt.animations.teleports;

import com.rs.game.world.WorldTile;

public enum SkillingTeleports {

    //Agility
    GNOME_AGILITY("Gnome Agility", new WorldTile(2468,3437,0)),
    BARBARIAN_AGILITY("Barbarian Agility", new WorldTile(2552,3557,0)),
    WILDERNESS_AGILITY("Wilderness Agility", new WorldTile(2998,3933,0)),
    PYRAMID_AGILITY("Pyramid Agility", new WorldTile(3358, 2828, 0)),

    //Fishing
    LOW_FISHING("Low Level Fishing", new WorldTile(3087,3232,0)),
    CATHERBY("Catherby Fishing", new WorldTile(2841,3432,0)),
    FISHING_GUILD("Fishing Guild",new WorldTile(2591,3420,0)),
    LIVING_ROCKS("Living Rock Caverns", new WorldTile(3655,5114,0)),

    //Hunter
    FIELDIP_HILLS("Fieldip Hills",new WorldTile(2526, 2916, 0)),
    FALCONRY("Falconry",new WorldTile(2362, 3623, 0)),

    //Farming
    FARM_SITE("Farm Site",new WorldTile(3052, 3304, 0)),

    //Flecthing
    FLETCHING("Fletching Shop",new WorldTile(4304, 873, 0)),

    //Woodcutting
    WOODCUTTING("Woodcuting Area",new WorldTile(2726, 3477, 0)),
    JUNGLE("Jungle",new WorldTile(2817, 3083, 0)),
    SEERS_VILLAGE("Seer's Village",new WorldTile(2726, 3477, 0)),

    //Runecrafting


    //Smithing
    EDGEVILLE_SMITH("Edgeville",new WorldTile(3107,3499,0)),

    //Summoning
    SUMMON_AREA("Summoning area",new WorldTile(2923, 3449, 0)),

    //Thieving


    //Firemaking


    //Dungeon
    REGULAR_DUNGEON("RS Dungeoneering",new WorldTile(3449, 3727, 0)),
    CUSTOM_DUNGEON("Custom Dungeon",new WorldTile(3972, 5561, 0)),

    //Crafting


    //Cooking

    //Construction
    HOUSE_PORTAL("House Portal",new WorldTile(4326,865,0)),

    //Prayer
    PRAYER_ALTAR("Altar",new WorldTile(4316,853,0)),

    //Mining
    STARTER_MINING("Starter Mining",new WorldTile(3300,3304,0)),
    GRANITE_MINING("Granite mine",new WorldTile(3170,2913,0)),
    LRC("Lrc",new WorldTile(3655,5114,0)),
    RED_SANDSTONE("Red Sandstones",new WorldTile(2590, 2880, 0)),
    PURE_ESSENCE("Pure Essence",new WorldTile(2932, 4821, 0))

    ;

    private String name;
    private WorldTile tile;

    SkillingTeleports(String name, WorldTile tile){
        this.name = name;
        this.tile = tile;
    }

    public String getName(){ return name;}
    public WorldTile getTile() { return tile;}

}
