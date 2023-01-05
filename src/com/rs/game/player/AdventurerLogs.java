package com.rs.game.player;

import com.rs.game.world.World;
import com.rs.game.item.Item;
import com.rs.game.npc.Drop;
import com.rs.game.npc.NPC;
import com.rs.utils.NPCDrops;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdventurerLogs implements Serializable {

    private static final long serialVersionUID = -8216251274045669425L;

    private transient Player player;

    private ArrayList<String> donationLog, spentLog;
    private HashMap<Integer, Integer> bossKills;
    private ConcurrentHashMap<Integer, HashMap<Item, Boolean>> bossDrops;
    private ConcurrentHashMap<Integer, HashMap<Item, Boolean>> npcDrops;
    private ArrayList<Integer> bosses;

    public AdventurerLogs(){
        setBosses();
        donationLog = new ArrayList<>();
        spentLog = new ArrayList<>();
        bossKills = new HashMap<>();
        setBossKills();
        setBossDrops();
        setNPCDrops();
    }


    public void setPlayer(Player player){ this.player = player; }
    public ArrayList<String> getDonationLog(){ return donationLog;}
    public ArrayList<String> getSpentLog() { return spentLog; }
    public HashMap<Integer, Integer> getBossKills(){ return bossKills; }
    public void addToBossKills(int id){
        this.bossKills.replace(id, this.bossKills.get(id) + 1);
    }

    public void printOut(){
        for (Map.Entry<Integer, HashMap<Item, Boolean>> npcDropping : npcDrops.entrySet()) {
            NPC npc = World.getNpc(npcDropping.getKey());
            assert npc != null;

            for (Map.Entry<Item, Boolean> nameEntry : npcDropping.getValue().entrySet()) {
                Item item = new Item(nameEntry.getKey());
                boolean logged = nameEntry.getValue();
                System.out.println(npc.getName() + " - "+item.getName()+" / "+logged);
            }
        }
    }

    void setBosses(){
        bosses = new ArrayList<>();

    }

    void setBossKills() {
        for(Integer boss : bosses)
            bossKills.put(boss, 0);
    }

    void setBossDrops(){
        bossDrops = new ConcurrentHashMap<>();
        for(Integer id : bosses) {
            bossDrops.put(id, new HashMap<>());
            if(NPCDrops.getDrops(id) != null){
               for(Drop drop : NPCDrops.getDrops(id)) {
                   bossDrops.get(id).put(new Item(drop.getItemId()), false);
                   System.out.println(""+id+" added "+npcDrops.get(id).size());
               }
            } else {
                System.out.println(""+id+" boss drops are null");
            }
        }

    }

    void setNPCDrops(){
        npcDrops = new ConcurrentHashMap<>();
        for(NPC npc : World.getNPCs()){
            for(Integer boss : bosses) {
                if (npc.getId() != boss) {
                    npcDrops.put(npc.getId(), new HashMap<>());
                    if(NPCDrops.getDrops(npc.getId()) != null){
                        for(Drop drop : NPCDrops.getDrops(npc.getId())) {
                            npcDrops.get(npc.getId()).put(new Item(drop.getItemId()), false);
                            System.out.println(""+npc.getName()+" added "+npcDrops.get(npc.getId()).size());
                        }
                    } else {
                        System.out.println(""+npc.getName()+" npc drops are null");
                    }
                }
            }
        }
    }

    public void addToLogs(NPC npc, Item item){
        if(bosses.contains(npc.getId())){
            if(bossDrops.get(npc.getId()) != null){
                if(!bossDrops.get(npc.getId()).get(item)) {
                    bossDrops.get(npc.getId()).replace(item, true);
                    player.sm(""+item.getName()+" added to "+npc.getName()+" logs");
                }
            }
        } else {
            if(npcDrops.get(npc.getId()) != null){
                if(!npcDrops.get(npc.getId()).get(item)) {
                    npcDrops.get(npc.getId()).replace(item, true);
                    player.sm("" + item.getName() + " added to " + npc.getName() + " logs");
                }
            }
        }
    }

    public void addToSpentLogs(Item item, int cost, String items){

    }

    public void addToDonationLogs(String add){
        donationLog.add(add);
    }


}
