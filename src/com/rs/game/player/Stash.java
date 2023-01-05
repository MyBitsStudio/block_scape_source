package com.rs.game.player;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.npc.Drop;
import com.rs.utils.Colors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Stash implements Serializable {

    private static final long serialVersionUID = 6240272776665743114L;

    private transient Player player;

    private int[] currency;
    private int[] points;
    private boolean[] unlocked;
    private String[] currencyName, pointsName;
    private ArrayList<Item> bossDrops;
    private ItemsContainer<Item> items;

    //currency
    public static int BOND = 0, UNTRADEABLE_BOND = 1;
    //points
    public static int CORRUPT_LOYALTY = 0;

    //unlocks
    public int UNLOCKED = 0, TUSKEN_RAID = 1, MAZCAB_RAID = 2;

    public Stash(){
        currency = new int[52];
        points = new int[52];
        unlocked = new boolean[52];
        currencyName = new String[52];
        pointsName = new String[52];
        Arrays.fill(currency, 0);
        Arrays.fill(points, 0);
        Arrays.fill(unlocked, false);
        Arrays.fill(currencyName, "");
        Arrays.fill(pointsName, "");

        //Add Names here for points
        pointsName[0] = "Corrupt Loyalty";

        //Add Names here for currency
        currencyName[0] = "Bond";
        currencyName[1] = "Untradeable Bond";

        items = new ItemsContainer<>(124, true);

        bossDrops = new ArrayList<>(128);
        bossDrops.add(new Item(995, 500000));
        bossDrops.add(new Item(991, 10));

    }

    public void setPlayer(Player player){ this.player = player;}
    public int getCurrency(int id){ return this.currency[id]; }
    public void addCurrency(int id, int amount) { this.currency[id] += amount; }
    public void takeCurrency(int id, int amount) { this.currency[id] -= amount; }
    public int getPoints(int id) { return this.points[id];}
    public void addPoints(int id, int amount) { this.points[id] += amount; }
    public void takePoints(int id, int amount) {this.points[id] -= amount; }
    public ArrayList<Item> getBossDrops(){ return bossDrops; }
    public boolean bossDropsUnlocked() { return unlocked[0];}

    public int getBossDropSize() {return bossDrops.size();}

    public boolean canAdd(Item item){
        if(!bossDropsUnlocked())
            return false;
        if(getBossDropSize() >= 128){
            return bossDrops.contains(item);
        }
        return true;
    }

    public boolean addToBossDrops(Item item){
        if(!canAdd(item))
            return false;
        if(bossDrops.contains(item)){
            int amount = bossDrops.get(bossDrops.indexOf(item)).getAmount();
            bossDrops.remove(item);
            return bossDrops.add(new Item(item.getId(), amount));
        } else if(!bossDrops.contains(item) && bossDrops.size() < 128){
            return bossDrops.add(item);
        }
        return false;
    }

    public void sendInterface(){
        int tab = player.getPlayerVars().getInterface3013Tab();
        int inter = 3013;

        player.getInterfaceManager().sendInterface(inter);

        switch(tab){
            case 1:
                //Hide
                player.getPackets().sendHideIComponent(inter, 33, false);
                player.getPackets().sendHideIComponent(inter, 165, true);
                player.getPackets().sendHideIComponent(inter, 177, true);
                player.getPackets().sendHideIComponent(inter, 12, true);
                player.getPackets().sendHideIComponent(inter, 13, false);
                player.getPackets().sendHideIComponent(inter, 16, false);
                player.getPackets().sendHideIComponent(inter, 17, true);
                player.getPackets().sendHideIComponent(inter, 20, false);
                player.getPackets().sendHideIComponent(inter, 21, true);

                for(int i = 40; i < 100; i+=4){
                    player.getPackets().sendHideIComponent(inter, i, true);
                }
                for(int k = 101; k < 165; k+=4){
                    player.getPackets().sendHideIComponent(inter, k, true);
                }

                //Currency
                int m = 40;
                for(int p = 0; p < 2; p++){
                    player.getPackets().sendHideIComponent(inter, m, false);
                    player.getPackets().sendIComponentSprite(inter, m+1, -1);
                    player.getPackets().sendIComponentText(inter, m+2, currencyName[p]);
                    player.getPackets().sendIComponentText(inter, m+3, currency[p]);
                    m+=4;
                }

                //Points
                int s = 101;
                for(int ss = 0; ss < 1; ss++){
                    player.getPackets().sendHideIComponent(inter, s, false);
                    player.getPackets().sendIComponentSprite(inter, s+1, -1);
                    player.getPackets().sendIComponentText(inter, s+2, pointsName[ss]);
                    player.getPackets().sendIComponentText(inter, s+3, points[ss]);
                    s+=4;
                }

                break;

            case 2:
                //Hide
                player.getPackets().sendHideIComponent(inter, 33, true);
                player.getPackets().sendHideIComponent(inter, 165, false);
                player.getPackets().sendHideIComponent(inter, 177, true);
                player.getPackets().sendHideIComponent(inter, 12, false);
                player.getPackets().sendHideIComponent(inter, 13, true);
                player.getPackets().sendHideIComponent(inter, 16, true);
                player.getPackets().sendHideIComponent(inter, 17, false);
                player.getPackets().sendHideIComponent(inter, 20, false);
                player.getPackets().sendHideIComponent(inter, 21, true);

                player.getPackets().sendInterSetItemsOptionsScript(inter, 169, 532, 8, 16, "Take", "Examine");
                player.getPackets().sendUnlockIComponentOptionSlots(inter, 169, 0, 128, 1);

                items.clear();
                for(Item item : bossDrops)
                    items.add(item);

                player.getPackets().sendItems(532, items);

                player.getPackets().sendIComponentText(inter, 175, (unlocked[0] ? Colors.green+"UNLOCKED" : Colors.red+"LOCKED"));
                player.getPackets().sendIComponentText(inter, 176, (unlocked[0] ? Colors.green+bossDrops.size()+" / 128" : Colors.red+"0 / 0"));
                player.getPackets().sendIComponentText(inter, 174, (unlocked[0] ? Colors.green+"Activate" : Colors.red+"UNLOCK"));
                break;

            case 3:
                //Hide
                player.getPackets().sendHideIComponent(inter, 33, true);
                player.getPackets().sendHideIComponent(inter, 165, true);
                player.getPackets().sendHideIComponent(inter, 177, false);
                player.getPackets().sendHideIComponent(inter, 12, false);
                player.getPackets().sendHideIComponent(inter, 13, true);
                player.getPackets().sendHideIComponent(inter, 16, false);
                player.getPackets().sendHideIComponent(inter, 17, true);
                player.getPackets().sendHideIComponent(inter, 20, true);
                player.getPackets().sendHideIComponent(inter, 21, false);

                break;


        }
    }

    public void handleButtons(int componentId){
        switch(componentId){
            case 11:
                player.getPlayerVars().setInterface3013Tab(1);
                sendInterface();
                break;

            case 15:
                player.getPlayerVars().setInterface3013Tab(2);
                sendInterface();
                break;

            case 19:
                player.sm("Coming soon!");
                break;

        }
    }

}
