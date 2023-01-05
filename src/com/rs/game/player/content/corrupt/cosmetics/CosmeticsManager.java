package com.rs.game.player.content.corrupt.cosmetics;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CosmeticsManager implements Serializable {

    /**
     * Script 6250 - send animation to interface
     */

    private static final long serialVersionUID = 4055061318822956820L;

    private transient Player player;
    private Map<Cosmetics.CosmeticPiece, Boolean> unlockedPieces;
    private boolean[] unlockedAnimations;
    private Item[] keepsakeItems;

    private int keepsakeKeys;

    public static int INTER = 3015;


    public CosmeticsManager(){
        unlockedPieces = new HashMap<>();
        unlockedAnimations = new boolean[224];
        keepsakeItems = new Item[124];
        keepsakeKeys = 0;

        for(Cosmetics.CosmeticPiece piece : Cosmetics.CosmeticPiece.values())
            unlockedPieces.put(piece, false);

        Arrays.fill(unlockedAnimations, false);

        setFreeCosmetics();

    }

    public boolean isUnlocked( Cosmetics.CosmeticPiece costume){
        return unlockedPieces.get(costume);
    }

    public boolean isUnlockedSet(Cosmetics.CosmeticSets set){
        for(Cosmetics.CosmeticPiece piece : set.getCosmetics())
            if(!isUnlocked(piece))
                return false;
            return true;
    }

    private void setFreeCosmetics() {

        for(Cosmetics.CosmeticPiece piece : Cosmetics.CosmeticSets.HIDE_ALL.getCosmetics())
            unlockedPieces.put(piece, true);

    }

    public void setPlayer(Player player) {
        this.player = player;

        for(Cosmetics.CosmeticPiece piece : Cosmetics.CosmeticPiece.values()){
            unlockedPieces.putIfAbsent(piece, false);
        }
    }

    public void previewCosmeticSet(Cosmetics.CosmeticSets costume) {
        ItemsContainer<Item> cosmeticPreviewItems = player.getEquipment().getCosmeticItems().asItemContainer();
        player.getTemporaryAttributtes().put("Cosmetics", Boolean.TRUE);

        for (Cosmetics.CosmeticPiece cosmetic : costume.getCosmetics())
                cosmeticPreviewItems.set(cosmetic.getCosmetic().getSlot(), new Item(cosmetic.getCosmetic().getItemId()));

        player.getEquipment().setCosmeticPreviewItems(cosmeticPreviewItems);
        player.getGlobalPlayerUpdater().generateAppearenceData();
        player.getPackets().sendConfig(6251, 0);
        sendComponents();
    }

    public void previewCosmeticItem(Cosmetics.CosmeticPiece costume){
        ItemsContainer<Item> cosmeticPreviewItems = player.getEquipment().getCosmeticItems().asItemContainer();
        player.getTemporaryAttributtes().put("Cosmetics", Boolean.TRUE);
        cosmeticPreviewItems.set(costume.getCosmetic().getSlot(), new Item(costume.getCosmetic().getItemId()));
        player.getEquipment().setCosmeticPreviewItems(cosmeticPreviewItems);
        player.getGlobalPlayerUpdater().generateAppearenceData();
        player.getPackets().sendConfig(6251, 0);
        sendComponents();
    }

    public void sendInterface() {
        sendComponents();
        player.getInterfaceManager().sendInterface(INTER);
        player.setCloseInterfacesEvent(() -> {
            player.getEquipment().setCosmeticPreviewItems(null);
            player.getPlayerVars().setSet(null);
            player.getPlayerVars().setPiece(null);
            player.getTemporaryAttributtes().remove("Cosmetics");
            player.getGlobalPlayerUpdater().generateAppearenceData();
            player.sm("testing worka");
        });
    }

    public void sendComponents(){
        player.getPackets().sendRunScript(1516);
                player.stopAll();
                player.getTemporaryAttributtes().put("Cosmetics", Boolean.TRUE);
                player.getPackets().sendConfigByFile(8348, 1);
                player.getPackets().sendConfigByFile(4894, 0);

                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        player.getPackets().sendConfigByFile(8348, 1);
                        player.getPackets().sendRunScriptBlank(2319);
                    }
                });

                //Change Tabs based on active
                player.getPackets().sendHideIComponent(INTER, 12, false);
                player.getPackets().sendHideIComponent(INTER, 13, true);
                player.getPackets().sendHideIComponent(INTER, 16, true);
                player.getPackets().sendHideIComponent(INTER, 17, false);
                player.getPackets().sendHideIComponent(INTER, 20, true);
                player.getPackets().sendHideIComponent(INTER, 21, false);

                //Tabs
                player.getPackets().sendHideIComponent(INTER, 33, false);
                player.getPackets().sendHideIComponent(INTER, 119, true);
                player.getPackets().sendHideIComponent(INTER, 136, true);

                for(int i = 35; i < 87; i+=4){
                    player.getPackets().sendHideIComponent(INTER, i, true);
                }

                player.getPackets().sendIComponentText(INTER, 104, ""+player.getPlayerVars().getInterface3015Page());

                switch(player.getPlayerVars().getInterface3015Piece()){
                    case 1://chest

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4){
                                    int size = Cosmetics.chestPieces().size();
                                    if((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.chestPieces().get(((i - 35) / 4));

                                        if(cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i+3, ""+(player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + ""+cosmetic.getName() : Colors.red + ""+cosmetic.getName() + "<br>"+"Cost : "+cosmetic.getPrice()));
                                    }

                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4){
                                    int size = Cosmetics.chestPieces().size() + 13;
                                    if(((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.chestPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4){
                                    int size = Cosmetics.chestPieces().size() + 26;
                                    if(((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.chestPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }


                        break;

                    case 2://hands

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.handsPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.handsPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));

                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.handsPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.handsPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.handsPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.handsPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 3://head

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.headPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.headPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.headPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.headPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.headPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.headPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 4://Legs

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.legsPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.legsPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.legsPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.legsPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.legsPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.legsPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 5://Weapon

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.weaponPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.weaponPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.weaponPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.weaponPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.weaponPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.weaponPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 6://Off-Hand

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.offHandPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.offHandPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.offHandPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.offHandPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.offHandPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.offHandPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 7://Necklace

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.neckPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.neckPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.neckPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.neckPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.neckPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.neckPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }

                                break;
                        }
                        break;

                    case 8://Back

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.backPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.backPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.backPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.backPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.backPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.backPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 9://Feet

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.bootsPieces().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.bootsPieces().get(((i - 35) / 4));
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.bootsPieces().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.bootsPieces().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.bootsPieces().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticPiece cosmetic = Cosmetics.bootsPieces().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlocked(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;

                    case 10://Set

                        switch(player.getPlayerVars().getInterface3015Page()){

                            case 1:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.sets().size();
                                    if ((i - 35) / 4 < size) {
                                        Cosmetics.CosmeticSets cosmetic = Cosmetics.sets().get((i - 35) / 4);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlockedSet(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 2:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.sets().size();
                                    if (((i - 35) / 4) + 13 < size) {
                                        Cosmetics.CosmeticSets cosmetic = Cosmetics.sets().get(((i - 35) / 4) + 13);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlockedSet(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;

                            case 3:
                                for(int i = 35; i < 87; i += 4) {
                                    int size = Cosmetics.sets().size();
                                    if (((i - 35) / 4) + 26 < size) {
                                        Cosmetics.CosmeticSets cosmetic = Cosmetics.sets().get(((i - 35) / 4) + 26);
                                        if (cosmetic == null)
                                            continue;
                                        player.getPackets().sendHideIComponent(INTER, i, false);
                                        player.getPackets().sendIComponentText(INTER, i + 3, "" + (player.getCosmetics().isUnlockedSet(cosmetic) ? Colors.green + "" + cosmetic.getName() : Colors.red + "" + cosmetic.getName() + "<br>" + "Cost : " + cosmetic.getPrice()));
                                    }
                                }
                                break;
                        }
                        break;


                }

                Cosmetics.CosmeticPiece pieces = player.getPlayerVars().getPiece();
                Cosmetics.CosmeticSets set = player.getPlayerVars().getSet();
                if(pieces != null){
                    if(player.getCosmetics().isUnlocked(pieces)){
                        player.getPackets().sendIComponentText(INTER, 111, "Equip");
                        player.getPackets().sendIComponentText(INTER, 113, "Piece Unlocked");
                    } else {
                        player.getPackets().sendIComponentText(INTER, 111, "Purchase");
                        player.getPackets().sendIComponentText(INTER, 113, "Cost : "+pieces.getPrice());
                    }
                } else {
                    if(set != null){
                        if(player.getCosmetics().isUnlockedSet(set)){
                            player.getPackets().sendIComponentText(INTER, 111, "Equip");
                            player.getPackets().sendIComponentText(INTER, 113, "Set Unlocked");
                        } else {
                            player.getPackets().sendIComponentText(INTER, 111, "Purchase");
                            player.getPackets().sendIComponentText(INTER, 113, "Cost : "+set.getPrice());
                        }
                    } else {
                        player.getPackets().sendIComponentText(INTER, 111, "");
                        player.getPackets().sendIComponentText(INTER, 113, "");
                    }
                }


        player.getInterfaceManager().sendInterface(INTER);

    }


    public void handleInterface(int componentId){

        if(componentId >= 35 && componentId <= 86){
            Cosmetics.CosmeticPiece piece;
            Cosmetics.CosmeticSets set;
            switch(player.getPlayerVars().getInterface3015Piece()){
                case 1:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.chestPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.chestPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.chestPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
               break;

                case 2:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.handsPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.handsPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.handsPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 3:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.headPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.headPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.headPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 4:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.legsPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.legsPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.legsPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 5:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.weaponPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.weaponPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.weaponPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;
                case 6:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.offHandPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.offHandPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.offHandPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 7:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.neckPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.neckPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.neckPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 8:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.backPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.backPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.backPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 9:
                    switch(player.getPlayerVars().getInterface3015Page()){

                        case 1:
                            piece = Cosmetics.bootsPieces().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 2:
                            piece = Cosmetics.bootsPieces().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;

                        case 3:
                            piece = Cosmetics.bootsPieces().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(piece);
                            player.getPlayerVars().setSet(null);
                            previewCosmeticItem(piece);
                            break;
                    }
                    break;

                case 10:
                    switch(player.getPlayerVars().getInterface3015Page()){


                        case 1:
                            set = Cosmetics.sets().get(((componentId - 35) / 4));
                            player.getPlayerVars().setPiece(null);
                            player.getPlayerVars().setSet(set);
                            previewCosmeticSet(set);
                            break;

                        case 2:
                            set = Cosmetics.sets().get(((componentId - 35) / 4) + 13);
                            player.getPlayerVars().setPiece(null);
                            player.getPlayerVars().setSet(set);
                            previewCosmeticSet(set);
                            break;

                        case 3:
                            set = Cosmetics.sets().get(((componentId - 35) / 4) + 26);
                            player.getPlayerVars().setPiece(null);
                            player.getPlayerVars().setSet(set);
                            previewCosmeticSet(set);
                            break;
                    }
                    break;

            }
            return;
        }

        switch(componentId){

            case 91:
                player.getPlayerVars().setInterface3015Piece(1);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 92:
                player.getPlayerVars().setInterface3015Piece(2);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 93:
                player.getPlayerVars().setInterface3015Piece(3);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 94:
                player.getPlayerVars().setInterface3015Piece(4);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 95:
                player.getPlayerVars().setInterface3015Piece(5);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 96:
                player.getPlayerVars().setInterface3015Piece(6);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 97:
                player.getPlayerVars().setInterface3015Piece(7);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 98:
                player.getPlayerVars().setInterface3015Piece(10);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 99:
                player.getPlayerVars().setInterface3015Piece(8);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 100:
                player.getPlayerVars().setInterface3015Piece(9);
                player.getPlayerVars().setInterface3015Page(1);
                sendComponents();
                break;

            case 114:
                player.getEquipment().setCosmeticPreviewItems(null);
                player.getGlobalPlayerUpdater().generateAppearenceData();
                player.getTemporaryAttributtes().remove("Cosmetics");
                player.closeInterfaces();
                break;

            case 107:
                if(player.getPlayerVars().getPiece() != null){//piece
                    if(!unlockedPieces.get(player.getPlayerVars().getPiece())) {
                        Cosmetics.CosmeticPiece piece = player.getPlayerVars().getPiece();
                        player.getPackets().sendIComponentText(INTER, 122, piece.getName());
                        player.getPackets().sendItemOnIComponent(INTER, 123, piece.getCosmetic().getItemId(), 1);
                        player.getPackets().sendIComponentText(INTER, 124, "Cost : " + piece.getPrice());
                        player.getPackets().sendIComponentText(INTER, 125, "Current : "+player.getDonationManager().getCorruptCoins());
                        player.getPackets().sendHideIComponent(INTER, 119, false);
                    } else
                        setCosmetic(player.getPlayerVars().getPiece());
                } else { //set
                    Cosmetics.CosmeticSets set = player.getPlayerVars().getSet();
                    if(!isUnlockedSet(set)) {
                        player.getPackets().sendItemOnIComponent(INTER, 118, set.getCosmetics()[4].getCosmetic().getItemId(), 1);
                        player.getPackets().sendIComponentText(INTER, 122, set.getName());
                        player.getPackets().sendIComponentText(INTER, 124, "Cost : " + set.getPrice());
                        player.getPackets().sendIComponentText(INTER, 125, "Current : "+player.getDonationManager().getCorruptCoins());
                        player.getPackets().sendHideIComponent(INTER, 119, false);
                    } else
                        setSet(set);
                }

                break;

            case 126:
                if(player.getPlayerVars().getPiece() != null) //piece
                    unlockedCosmetic(player.getPlayerVars().getPiece());
                 else
                    unlockedSet(player.getPlayerVars().getSet());
                break;

            case 131:
                player.getPackets().sendHideIComponent(INTER, 119, true);
                break;
        }
    }

    public void unlockedCosmetic(Cosmetics.CosmeticPiece piece){
        if(player.getDonationManager().getCorruptCoins() < piece.getPrice()){
            player.sm("You don't have enough coins to purchase this");
            return;
        }
        if(!unlockCosmetic(piece)){
            player.sm("There was an error when unlocking the cosmetic. Please contact staff with this error message.");
        } else {
            player.getDonationManager().takeCorruptCoins(piece.getPrice());
            player.sm("You have successfully purchased "+piece.getName());
            player.getPlayerVars().setPiece(null);
        }
        player.getPackets().sendHideIComponent(INTER, 114, true);
        sendComponents();
    }

    public void unlockedSet(Cosmetics.CosmeticSets set){
        int price = 0, pieces = 0;
        if(player.getDonationManager().getCorruptCoins() < set.getPrice()){
            player.sm("You don't have enough coins to purchase this");
            return;
        }
        for(Cosmetics.CosmeticPiece piece : set.getCosmetics()){
            if(unlockedPieces.get(piece)){
                player.sm("You already have "+piece.getName()+" unlocked. You will not be charged.");
            } else {
                if(unlockCosmetic(piece)){
                    price += piece.getPrice();
                    pieces++;
                    player.getDonationManager().takeCorruptCoins(piece.getPrice());
                    player.sm("You have successfully purchased "+piece.getName());
                }
            }
        }
        player.sm("You have purchased "+pieces+" of the "+set.getName()+" for a total price of "+price);
        player.getPlayerVars().setSet(null);
        player.getPackets().sendHideIComponent(INTER, 114, true);
        sendComponents();
    }

    public boolean unlockCosmetic(Cosmetics.CosmeticPiece piece){
        if(unlockedPieces.get(piece))
            return false;
        return unlockedPieces.replace(piece, false, true);
    }

    public void setCosmetic(Cosmetics.CosmeticPiece piece){
        player.getEquipment().getCosmeticItems().set(piece.getCosmetic().getSlot(), new Item(piece.getCosmetic().getItemId()));
        player.getGlobalPlayerUpdater().generateAppearenceData();
    }

    public void setSet(Cosmetics.CosmeticSets set){
        for(Cosmetics.CosmeticPiece piece : set.getCosmetics())
            player.getEquipment().getCosmeticItems().set(piece.getCosmetic().getSlot(), new Item(piece.getCosmetic().getItemId()));
        player.getGlobalPlayerUpdater().generateAppearenceData();
    }
}
