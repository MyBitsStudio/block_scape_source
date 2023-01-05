package com.corrupt.raids.controllers;

import com.corrupt.raids.RaidsController;
import com.corrupt.raids.RaidsSettings;
import com.rs.game.world.map.MapBuilder;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;

public class CorruptedCrystalController extends RaidsController {

        private RaidsSettings raid;

        public int CORRUPTED_CRYSTAL_MINE = 108951, SOLID_CRYSTAL = 98537, FLOATING_CRYSTAL = 92698;
        public int CRYSTAL_1_DEAD = 49507, CRYSTAL_1_ALIVE = 49510, CRYSTAL_1_ACTIVE = 49513, CRYSTAL_2_DEAD = 49508, CRYSTAL_2_ALIVE = 49511, CRYSTAL_2_ACTIVE = 49514,
                CRYSTAL_3_DEAD = 49509, CRYSTAL_3_ALIVE = 49512, CRYSTAL_3_ACTIVE = 49515;
        public int PORTAL1 = 101725, BOSS_PORTAL = 106706, BARRIER = 31436;

        public WorldObject[] corruptedMines, solidMines, floatingCrstals;
        public WorldObject crystal1, crystal2, crystal3, portal1, portal2, portal3, bossPortal;
        public WorldTile[] corruptedMinesTiles, solidMinesTiles, floatingCrystalsTiles, portalTiles, mob1Tiles, mob2Tiles, mob3Tiles, mob4Tiles, barriers;
        public WorldTile crystal1Tile, crystal2Tile, crystal3Tile, mini1Tile, mini2Tile, mini3Tile;
        public boolean[] unlocked;



        @Override
        public void start(){
            raid = (RaidsSettings) getArguments()[0];
            buildArrays();
            buildConstants();
            buildMap();
            checkStage();
        }

        private void checkStage() {
            if(raid.getStage() <= 0){

            } else {
                switch(raid.getStage()){
                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                }
            }

        }


        public void bossStage(){
            MapBuilder.destroyMap(8, 8, 8, 8);
            int[] boundChunks = MapBuilder.findEmptyChunkBound(8, 8);
            MapBuilder.copyAllPlanesMap(261, 830, boundChunks[0], boundChunks[1], 8);
            player.setNextWorldTile(raid.getTile(0, 0));

        }

        private void buildArrays(){
            corruptedMinesTiles = new WorldTile[10];
            solidMinesTiles = new WorldTile[10];
            floatingCrystalsTiles = new WorldTile[10];
            portalTiles = new WorldTile[4];
            mob1Tiles = new WorldTile[20];
            mob2Tiles = new WorldTile[20];
            mob3Tiles = new WorldTile[20];
            mob4Tiles = new WorldTile[20];
            corruptedMines = new WorldObject[10];
            solidMines = new WorldObject[10];
            floatingCrstals = new WorldObject[10];
            barriers = new WorldTile[4];
            unlocked = new boolean[4];
        }

        private void buildConstants() {
            //Tiles
            crystal1Tile = raid.getTile(1185, -8368);

            crystal2 = new WorldObject(CRYSTAL_2_DEAD, 10, 0, crystal2Tile);
            crystal3 = new WorldObject(CRYSTAL_3_DEAD, 10, 0, crystal3Tile);



        }

        private void buildMap() {
            World.spawnObject(crystal1, true);
            World.spawnObject(crystal2, true);
            World.spawnObject(crystal3, true);
        }

    @Override
    public boolean processObjectClick1(WorldObject object) {
           switch(object.getId()){

               case 31436:
                   if(object.getWorldTile().matches(barriers[0])){
                       player.sm("barrier 0");
                   } else if(object.getWorldTile().matches(barriers[1])){
                       player.sm("barrier 1");
                   } else if(object.getWorldTile().matches(barriers[2])){
                       player.sm("barrier 2");
                   } else if(object.getWorldTile().matches(barriers[3])){
                       player.sm("barrier 3");
                   }
                   return true;
           }
            return false;
    }
}
