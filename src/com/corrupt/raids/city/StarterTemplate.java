package com.corrupt.raids.city;

import com.rs.game.world.map.MapBuilder;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.world.controllers.Controller;

public class StarterTemplate extends Controller {

    private int[] regionBase;
    public WorldTile base;

    public boolean spawned;
    protected NPC bossNPC;
    protected NPC[] miniBoss;

    private int stage;

    public Item[] COMMON_REWARDS = {

    }, UNCOMMON_REWARDS = {

    }, RARE_REWARDS = {

    }, ULTRA_REWARDS = {

    }, LEGEND_REWARDS = {

    };

    @Override
    public void start() {
        //Build Map
        regionBase = MapBuilder.findEmptyChunkBound(8, 8);
        MapBuilder.copyAllPlanesMap(537, 103, regionBase[0], regionBase[1], 8, 8);
        buildObjects();
        //Set Player
        player.setNextWorldTile(getWorldTile(19, 2));  //14, 15
        stage = 0;

    }

    void buildObjects(){}
    @Override
    public boolean processObjectClick1(WorldObject object) {return false;}
    @Override
    public boolean processObjectClick2(WorldObject object) {return false;}
    @Override
    public void process() {}
    @Override
    public void forceClose() {}
    @Override
    public boolean logout() {
        player.setForceMultiArea(false);
        removeControler();
        return true;
    }
    @Override
    public boolean sendDeath() {

        return false;
    }

    @Override
    public void magicTeleported(int type) {}
    @Override
    public void processNPCDeath(NPC npc){

    }

    public WorldTile getWorldTile(int mapX, int mapY) {
        return new WorldTile(regionBase[0] * 8 + mapX, regionBase[1] * 8 + mapY, 0);
    }

}
