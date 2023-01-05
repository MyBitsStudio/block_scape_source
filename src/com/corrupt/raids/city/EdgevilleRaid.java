package com.corrupt.raids.city;

import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.world.controllers.Controller;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.world.map.MapBuilder;
import com.rs.game.world.map.MapInstance;
import com.rs.game.world.region.Region;

public class EdgevilleRaid extends Controller {

    private int[] regionBase;
    public WorldTile base;
    private MapInstance map;
    private int[] mask;

    public boolean spawned;
    protected NPC bossNPC;
    protected NPC[] miniBoss;
    protected WorldObject[] barricades;

    private int stage;

    public Item[] COMMON_REWARDS = {

    }, UNCOMMON_REWARDS = {

    }, RARE_REWARDS = {

    }, ULTRA_REWARDS = {

    }, LEGEND_REWARDS = {

    };

    @Override
    public void start() {
        barricades = new WorldObject[24];
        //Build Map
        regionBase = MapBuilder.findEmptyChunkBound(10, 10);
        map = new MapInstance(383, 431, 3,3);
        map.load(() -> {
            buildObjects();
            spawnObjects();
            player.setNextWorldTile(getWorldTile(19, 2));
            stage = 0;
        });

    }

    private void spawnObjects() {
        for(WorldObject object : barricades)
            if(object != null)
                World.spawnObject(object);
    }

    void buildObjects(){
        Region region = new Region(12342);
        barricades[0] = new WorldObject(6856, 10, 1, getWorldTile(12, 13));
        mask[0] = region.getMask(getWorldTile(12, 13).getX(), getWorldTile(12, 13).getY(), 0);
        barricades[1] = new WorldObject(6856, 10, 1, getWorldTile(12, 12));
        barricades[2] = new WorldObject(6856, 10, 1, getWorldTile(12, 11));
    }
    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch(object.getId()){
            default:
                return false;
        }
    }
    @Override
    public boolean processObjectClick2(WorldObject object) {
        switch(object.getId()){
            default:
                return false;
        }
    }
    @Override
    public void process() {}
    @Override
    public void forceClose() {
        player.setForceMultiArea(false);
        removeControler();
    }
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
    public void magicTeleported(int type) {
        player.setForceMultiArea(false);
        removeControler();
    }
    @Override
    public void processNPCDeath(NPC npc){
        switch(npc.getId()){

        }
    }

    public WorldTile getWorldTile(int mapX, int mapY) {
        return new WorldTile(regionBase[0] * 8 + mapX, regionBase[1] * 8 + mapY, 0);
    }

}
