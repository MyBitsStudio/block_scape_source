package com.corrupt.raids;

import com.corrupt.raids.floor.*;

public enum Raid {
    //TIER 1
    LOBBY(-1,"", RaidLobby.class, 0, 0, null, "RaidLobbyController", new int[]{8,8}, new int[]{281, 584}, false, 8),
    TUSKEN_RAID(0,"Tusken Raid", TuskenRaid.class, 0, 100, new int[]{19, 2}, "TuskenRaidController", new int[]{ 8, 8}, new int[]{537, 103}, false, 8),

    //TIER 2
    MAZCAB_RAID(1,"Mazcab Raid", MazcabRaid.class, 0, 100, new int[] {27,31}, "MazcabRaidController", new int[]{8,8}, new int[]{242, 750}, true, 8),

    //TIER 3


    //HAZARD
    DRAGON_BURST(2,"Dragon Burst", DragonBurst.class, 0, 100, new int[] {25, 9}, "DragonBurstController", new int[]{8,8}, new int[]{281, 584}, true, 8),
    CORRUPTED_CRYSTALS(3,"Corrupted Crystals", CorruptedCrystal.class, 0, 100, new int[] {13, 12}, "CorruptedCrystalController", new int[]{32 , 32}, new int[]{407, 535}, true, 32),
    ;

    private final Class<? extends RaidsSettings> raid;
    private final int initialCost, raidPoints, id, boundChunk;
    private final String controllerName, raidName;
    private final int[] chunks, mapCoords, insideTile;
    private final boolean multi;


    Raid(int id, String raidName, Class<? extends RaidsSettings> raid, int initalCost, int raidPoints, int[] insideTile, String controllerName, int[] chunks, int[] mapCoords, boolean multi, int boundChunk){
        this.id = id;
        this.raid = raid;
        this.initialCost = initalCost;
        this.raidPoints = raidPoints;
        this.insideTile = insideTile;
        this.controllerName = controllerName;
        this.chunks = chunks;
        this.mapCoords = mapCoords;
        this.raidName = raidName;
        this.multi = multi;
        this.boundChunk = boundChunk;
    }

    public Class<? extends RaidsSettings> getRaid(){ return raid; }

    public String getControllerName() {
        return controllerName;
    }

    public int[] getInsideTile() {
        return insideTile;
    }

    public int getInitialCost() {return initialCost;}

    public int[] getChunks(){ return chunks; }

    public int[] getMapCoords(){ return mapCoords;}

    public int getRaidPoints(){ return raidPoints; }

    public String getRaidName(){ return raidName; }

    public boolean isMulti(){ return multi; }

    public int getId() { return id;}

    public int getBoundChunk(){ return boundChunk; }

}
