package com.rs.game.player.content.corrupt.modes.prestige;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Prestige implements Serializable {

    private static final long serialVersionUID = -3936273284476809477L;

    private transient Player player;

    public void setPlayer(Player player){ this.player = player;}

    private Map<Integer, HashMap<Object, Integer>> prestige;
    private Map<Integer, HashMap<Object, Integer>> prestigeMax;
    private short[] levels;
    private int totalLevel;
    private boolean prestigeActivated;

    /**
     * Integers representing Skill ID's.
     */
    public static final int MELEE = 0, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
            COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
            MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
            CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24, DIVINATION = 25, INVENTION = 26;

    public Prestige(){
        prestige = new HashMap<>();
        prestigeMax = new HashMap<>();
        levels = new short[27];
        totalLevel = 0;
        Arrays.fill(levels, (short) 0);
    }

    public boolean isPrestigedItem(int id){
        return id == 379 || id == 1511;
    }

    public boolean isPrestigedNPC(int id){
        return id == 1;
    }


    public boolean isPrestiged(){ return this.prestigeActivated;}
    public void setPrestiged(boolean set){ this.prestigeActivated = set;}

    private void setPrestige() {
        for(int i = 0; i < 27; i++){
            prestige.put(i, new HashMap<>());
        }
        for(int i = 0; i < 27; i++){
            prestigeMax.put(i, new HashMap<>());
        }

        //Attack-Defence-Strength
        prestige.get(MELEE).put(1265, 0);//RockCrab
        //Magic
        prestige.get(MAGIC).put(1265, 0);//RockCrab
        //Range
        prestige.get(RANGE).put(1265, 0);//RockCrab
        //Hitpoints
        prestige.get(HITPOINTS).put(379, 0);//Lobster

        //Prayer

        //Cooking

        //Woodcutting
        prestige.get(WOODCUTTING).put(1511, 0);//Normal Logs
        prestige.get(WOODCUTTING).put(1521, 0);//Oak Logs
        prestige.get(WOODCUTTING).put(1519, 0);//Willow Logs
        prestige.get(WOODCUTTING).put(1517, 0);//Maple Logs
        prestige.get(WOODCUTTING).put(1515, 0);//Yew Logs
        prestige.get(WOODCUTTING).put(1513, 0);//Magic Logs

        for(int i = 0; i < prestige.size(); i++){
            for( Map.Entry<Object, Integer> maxes : prestige.get(i).entrySet()){
//                switch(//player.getGameModeManager().getMode(1)){
//                    case 0:
//                        prestigeMax.get(i).put(maxes.getKey(), 250);
//                        break;
//                    case 1:
//                        prestigeMax.get(i).put(maxes.getKey(), 500);
//                        break;
//
//                    case 2:
//                        prestigeMax.get(i).put(maxes.getKey(), 1000);
//                        break;
//
//                    case 3:
//                        prestigeMax.get(i).put(maxes.getKey(), 2500);
//                        break;
//                }
            }
        }


    }

    private int getTotalLevel(){
        int total = 0;
        for (Map.Entry<Integer, HashMap<Object, Integer>> prestige: prestige.entrySet()) {
            for( Map.Entry<Object, Integer> maxes : prestige.getValue().entrySet()){
                if(maxes.getValue() >= prestigeMax.get(prestige.getKey()).get(maxes.getKey()))
                    total += 1;

            }
        }
        return total;
    }

    public void handleTask(int skill, int id, int amount, String name){
        if(!prestigeActivated)
            return;

        switch(skill){

            case 0://Melee
                switch(id){
                    case 1265://Rockcrabs
                        prestige.get(MELEE).replace(1265, prestige.get(MELEE).get(1265) + 1);
                        break;
                }
                break;

            case 4://Range
                switch(id){
                    case 1265://Rockcrabs
                        prestige.get(RANGE).replace(1265, prestige.get(RANGE).get(1265) + 1);
                        break;
                }
                break;

            case 6://Magic
                switch(id){
                    case 1265://Rockcrabs
                        prestige.get(MAGIC).replace(1265, prestige.get(MAGIC).get(1265) + 1);
                        break;
                }
                break;

            case 8://Woodcutting
                switch(id){
                    case 1511://Normal
                        prestige.get(WOODCUTTING).replace(1511, prestige.get(WOODCUTTING).get(1511) + 1);
                        break;
                    case 1521://Oak
                        prestige.get(WOODCUTTING).replace(1521, prestige.get(WOODCUTTING).get(1521) + 1);
                        break;
                    case 1519://Willow
                        prestige.get(WOODCUTTING).replace(1519, prestige.get(WOODCUTTING).get(1519) + 1);
                        break;
                    case 1517://Maple
                        prestige.get(WOODCUTTING).replace(1517, prestige.get(WOODCUTTING).get(1517) + 1);
                        break;
                    case 1515://Yew
                        prestige.get(WOODCUTTING).replace(1515, prestige.get(WOODCUTTING).get(1515) + 1);
                        break;
                    case 1513://Maple
                        prestige.get(WOODCUTTING).replace(1513, prestige.get(WOODCUTTING).get(1513) + 1);
                        break;
                }
                break;
        }

    }

}
