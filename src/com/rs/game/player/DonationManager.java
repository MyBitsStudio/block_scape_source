package com.rs.game.player;

import com.rs.WorldSettings;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.content.corrupt.donations.DonationItems;
import com.rs.game.player.content.corrupt.donations.MonthlyPackages;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class DonationManager implements Serializable {
    /**
     *  Donation Manager
     *
     * @author - Quantum
     */

    private static final long serialVersionUID = 1969486620220920617L;

    private transient Player player;

    private int amountDonated, runeCoins, rank, vipPoints, totalVipPoints;

    private int[] monthlyPackage, weeklyPackage;

    private long[] monthlyTimer,weeklyTimer;

    private boolean[] claimToday;

    public static int MANAGER_INTER = 3019, SHOPS_INTER = 3018;


    public DonationManager(){
        monthlyTimer = new long[12];
        Arrays.fill(monthlyTimer, 0);

        weeklyTimer = new long[12];
        Arrays.fill(weeklyTimer, 0);

        monthlyPackage = new int[12];
        Arrays.fill(weeklyTimer,0);
        weeklyPackage = new int[12];
        Arrays.fill(weeklyPackage,0);

        claimToday = new boolean[12];
        Arrays.fill(claimToday, false);

        runeCoins = 0;
        amountDonated = 0;
        rank = 0;
        vipPoints = 0;
        totalVipPoints = 0;
    }

    public int getAmountDonated(){ return this.amountDonated;}
    public int getCorruptCoins(){ return this.runeCoins; }
    public int getRank(){ return this.rank; }
    public int getVIPPoints(){return this.vipPoints;}
    public long getMonthlyTimer(int timer) { return this.monthlyTimer[timer]; }
    public long getWeeklyTimer(int timer) { return this.weeklyTimer[timer]; }
    public int getMonthlyPackage(int pack) {return monthlyPackage[pack];}
    public int getWeeklyPackage(int pack) {return weeklyPackage[pack];}
    public boolean getClaimedToday(int id) { return claimToday[id];}
    public long[] getMonthlyTimer(){ return monthlyTimer; }
    public int[] getMonthlyPackages(){ return monthlyPackage;}
    public int getTotalVipPoints() {return totalVipPoints;}

    public void setPlayer(Player player) {this.player = player; }
    public void addAmountDonated(int amount) {this.amountDonated += amount;}
    public void takeAmountDonated(int amount) {this.amountDonated -= amount;}
    public void addCorruptCoins(int amount) {this.runeCoins += amount;}
    public void takeCorruptCoins(int amount) {this.runeCoins -= amount;}
    public void setRank(int rank) {this.rank = rank;}
    public void addVIPPoints(int amount){this.vipPoints += amount;}
    public void setMonthlyTimer(int timer, long set){this.monthlyTimer[timer]=set;}
    public void setWeeklyTimer(int timer, long set){this.weeklyTimer[timer]=set;}
    public void setMonthlyPackage(int timer, int set){this.monthlyPackage[timer]=set;}
    public void setWeeklyPackage(int timer, int set){this.weeklyPackage[timer]=set;}
    public void setClaimedToday(int id, boolean set){ this.claimToday[id] = set;}
    public void claimMontlyPackage(int id){this.monthlyPackage[id]--;}
    public void addTotalVipPoints(int amount){this.vipPoints += amount;}

    public boolean canClaim(){
       for(int i = 0; i < claimToday.length; i++)
           if(monthlyPackage[i] >= 1 && !claimToday[i])
               return true;
       return false;
    }

    public String getDonorRank(){
        return getRank() == 0 ? "<col=C96800>Player</col>"
                : getRank() == 1 ? Colors.gray + "VIP I</col>"
                : getRank() == 2 ? Colors.gray + "VIP II</col>"
                : getRank() == 3 ? Colors.gray + "VIP III</col>"
                : getRank() == 4 ? Colors.green + "VIP IV</col>"
                : getRank() == 5 ? Colors.green + "VIP V</col>"
                : getRank() == 6 ? Colors.green + "VIP VI</col>"
                : getRank() == 7 ? Colors.blue + "VIP VII</col>"
                : getRank() == 8 ? Colors.blue + "VIP VIII</col>"
                : getRank() == 9 ? Colors.blue + "VIP IX</col>"
                : getRank() == 10 ? Colors.yellow + "VIP X</col>"
                : getRank() == 11 ? Colors.yellow + "VIP XI</col>"
                : getRank() == 12 ? Colors.yellow + "VIP XII</col>"
                : getRank() == 13 ? Colors.red + "VIP XIII</col>"
                : getRank() == 14 ? Colors.red + "VIP XIV</col>"
                : getRank() == 15 ? Colors.red + "VIP XV</col>"
                : getRank() == 16 ? Colors.purple + "VIP XVI</col>"
                : getRank() == 17 ? Colors.purple + "VIP XVII</col>"
                : getRank() == 18 ? Colors.purple + "VIP XVIII</col>"
                : getRank() == 19 ? Colors.purple + "VIP XIX</col>"
                : getRank() == 20 ? Colors.black + "VIP XX</col>"
                : getRank() == 21 ? Colors.black + "VIP XXX</col>"
                : getRank() == 22 ? Colors.black + "VIP XXXX</col>"
                : getRank() == 23 ? Colors.black + "VIP XXXXX</col>"
                : "";
    }

    public void addDonation(int amount){
        int newAmount = (int) (amount * WorldSettings.donationBoost);
        if(WorldSettings.donationBoostActive) {
            addCorruptCoins(newAmount);
            addAmountDonated(newAmount);
        }else {
            addCorruptCoins(amount);
            addAmountDonated(amount);
        }
        if(WorldSettings.donationBoostItemActive) {
            player.getBank().addItem(WorldSettings.donationBoostItem, true);
            player.sm("You got x+"+WorldSettings.donationBoostItem.getAmount()+" "+WorldSettings.donationBoostItem.getName()+" added to your bank.");
        }
        addVIPPoints(amount * 10);
        addTotalVipPoints(amount * 10);
        setRank(getLevelForXP(totalVipPoints));
        player.getAdventurerLogs().addToDonationLogs("[Donation "+ Utils.formatTime(System.currentTimeMillis()) +"] "+amount+" Rune Coins & "+(amount * 10)+" VIP XP");
    }

    public void dailyLogin(){
        if(rank > 0) {
            addVIPPoints(rank);
            player.getAdventurerLogs().addToDonationLogs("[Daily "+ Utils.formatTime(System.currentTimeMillis()) +"] "+rank+" VIP Points added");
            addDonatorBox();
        }
        Arrays.fill(claimToday, false);
    }

    public void addDonatorBox(){

    }

    public int getLevelForXP(int xp){
        if(xp < 50){
            return 0;
        } else if(xp < 150){
            return 1;
        } else if(xp < 500){
            return 2;
        } else if(xp < 750){
            return 3;
        } else if(xp < 1000){
            return 4;
        } else if(xp < 1500){
            return 5;
        } else if(xp < 2000){
            return 6;
        } else if(xp < 2500){
            return 7;
        } else if(xp < 3000){
            return 8;
        } else if(xp < 4000){
            return 9;
        } else if(xp < 5000){
            return 10;
        } else if(xp < 6000){
            return 11;
        } else if(xp < 7000){
            return 12;
        } else if(xp < 8000){
            return 13;
        } else if(xp < 9000){
            return 14;
        } else if(xp < 10000){
            return 15;
        } else if(xp < 12500){
            return 16;
        } else if(xp < 15000){
            return 17;
        } else if(xp < 20000){
            return 18;
        } else if(xp < 25000){
            return 19;
        } else if(xp < 35000){
            return 20;
        } else if(xp < 50000){
            return 21;
        } else if(xp < 75000){
            return 22;
        } else if(xp > 75000){
            return 23;
        }
        return 0;
    }

    public void purchaseMonthlyPackage(int id){
        MonthlyPackages packages = MonthlyPackages.forId(id);
        assert packages != null;
        if(getMonthlyTimer(id) > System.currentTimeMillis())
            return;
        if(getCorruptCoins() < packages.getPrice())
            return;
        if(monthlyPackage[id] >= 1)
            return;
        takeCorruptCoins(packages.getPrice());
        setMonthlyTimer(id, (System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
        setMonthlyPackage(id,30);
        if(!player.getInventory().hasFreeSlots())
            player.sm("You need to make room in your inventory to claim this package.");
        else {
            claimMontlyPackage(id);
            if(packages.getRuneCoins() != -1)
                addCorruptCoins(packages.getRuneCoins());
            if(packages.getItems() != null)
                for(Item item : packages.getItems())
                    player.getInventory().addItem(item);
            setClaimedToday(id, true);
        }

    }

    public void claimPackage(int id){
        MonthlyPackages packages = MonthlyPackages.forId(id);
        assert packages != null;
        if(getClaimedToday(id))
            return;
        if(!player.getInventory().hasFreeSlots())
            player.sm("You need to make room in your inventory to claim this package.");
        else {
            claimMontlyPackage(id);
            if(packages.getRuneCoins() != -1)
                addCorruptCoins(packages.getRuneCoins());
            if(packages.getItems() != null)
                for(Item item : packages.getItems())
                    player.getInventory().addItem(item);
            setClaimedToday(id, true);
        }
    }


    public void sendShopsInterface(Player player){
        ItemsContainer<Item> shopItems = new ItemsContainer<>(56, false);
        int tab = player.getPlayerVars().getInterface3018tabSelected();

        switch(tab){
            case 1:

                shopItems.clear();

                player.getPackets().sendInterSetItemsOptionsScript(SHOPS_INTER, 36, 533, 8, 7, "Examine");
                player.getPackets().sendUnlockIComponentOptionSlots(SHOPS_INTER, 36, 0, 56, 0);

                for(DonationItems items : DonationItems.level1())
                    shopItems.add(items.getItem());

                player.getPackets().sendItems(533, shopItems);

                player.getPackets().sendIComponentText(SHOPS_INTER, 38, "NORMAL VIP SHOP");

                if(player.getPlayerVars().getSelectedVIPItem() != null){
                    Item item = player.getPlayerVars().getSelectedVIPItem();

                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, item.getName());
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, item.getDefinitions().getExamine());

                    DonationItems don = DonationItems.getDonationItem(item);

                    assert don != null;
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, don.getPrice());
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, false);

                } else {
                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, "");
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, true);
                }

                break;

            case 2:

                shopItems.clear();

                player.getPackets().sendInterSetItemsOptionsScript(SHOPS_INTER, 36, 533, 8, 7, "Examine");
                player.getPackets().sendUnlockIComponentOptionSlots(SHOPS_INTER, 36, 0, 56, 0);

                for(DonationItems items : DonationItems.level2())
                    shopItems.add(items.getItem());

                player.getPackets().sendItems(533, shopItems);

                player.getPackets().sendIComponentText(SHOPS_INTER, 38, "SUPER VIP SHOP");

                if(player.getPlayerVars().getSelectedVIPItem() != null){
                    Item item = player.getPlayerVars().getSelectedVIPItem();

                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, item.getName());
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, item.getDefinitions().getExamine());

                    DonationItems don = DonationItems.getDonationItem(item);

                    assert don != null;
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, don.getPrice());
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, false);

                } else {
                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, "");
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, true);
                }

                break;

            case 3:

                shopItems.clear();

                player.getPackets().sendInterSetItemsOptionsScript(SHOPS_INTER, 36, 533, 8, 7, "Examine");
                player.getPackets().sendUnlockIComponentOptionSlots(SHOPS_INTER, 36, 0, 56, 0);

                for(DonationItems items : DonationItems.level3())
                    shopItems.add(items.getItem());

                player.getPackets().sendItems(533, shopItems);

                player.getPackets().sendIComponentText(SHOPS_INTER, 38, "EXTREME VIP SHOP");

                if(player.getPlayerVars().getSelectedVIPItem() != null){
                    Item item = player.getPlayerVars().getSelectedVIPItem();

                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, item.getName());
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, item.getDefinitions().getExamine());

                    DonationItems don = DonationItems.getDonationItem(item);

                    assert don != null;
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, don.getPrice());
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, false);

                } else {
                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, "");
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, true);
                }

                break;

            case 4:

                shopItems.clear();

                player.getPackets().sendInterSetItemsOptionsScript(SHOPS_INTER, 36, 533, 8, 7, "Examine");
                player.getPackets().sendUnlockIComponentOptionSlots(SHOPS_INTER, 36, 0, 56, 0);

                for(DonationItems items : DonationItems.level4())
                    shopItems.add(items.getItem());

                player.getPackets().sendItems(533, shopItems);

                player.getPackets().sendIComponentText(SHOPS_INTER, 38, "VIP POINTS SHOP");

                if(player.getPlayerVars().getSelectedVIPItem() != null){
                    Item item = player.getPlayerVars().getSelectedVIPItem();

                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, item.getName());
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, item.getDefinitions().getExamine());

                    DonationItems don = DonationItems.getDonationItem(item);

                    assert don != null;
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, don.getPrice());
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, false);

                } else {
                    player.getPackets().sendIComponentText(SHOPS_INTER, 45, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 46, "");
                    player.getPackets().sendIComponentText(SHOPS_INTER, 48, "");
                    player.getPackets().sendHideIComponent(SHOPS_INTER, 40, true);
                }

                break;

        }

    }

    public void sendShops(Player player){
        player.getInterfaceManager().sendInterface(SHOPS_INTER);
        sendShopsInterface(player);
    }

    public void sendManager(Player player){
        player.getInterfaceManager().sendInterface(MANAGER_INTER);
        sendDonationManager(player);
    }

    public void handleButtons(Player player, int interfaceId, int componentId, int slot){
        switch(interfaceId){
            case 3019://Manager

                switch(componentId){

                    case 47:
                        player.closeInterfaces();
                        sendShops(player);
                        break;

                }
                break;
            case 3018://Shops

                switch(componentId){

                    case 11:
                        player.getPlayerVars().setInterface3018tabSelected(1);
                        sendShopsInterface(player);
                        break;

                    case 15:
                        if(rank <= 4) {
                            player.sm("You need to be VIP V to access this shop.");
                            return;
                        }
                        player.getPlayerVars().setInterface3018tabSelected(2);
                        sendShopsInterface(player);
                        break;

                    case 19:
                        if(rank <= 14) {
                            player.sm("You need to be VIP XV to access this shop.");
                            return;
                        }
                        player.getPlayerVars().setInterface3018tabSelected(3);
                        sendShopsInterface(player);
                        break;

                    case 23:
                        player.getPlayerVars().setInterface3018tabSelected(4);
                        sendShopsInterface(player);
                        break;


                    case 36:
                        switch(player.getPlayerVars().getInterface3018tabSelected()){

                            case 1:
                                DonationItems item = DonationItems.level1()[slot];
                                player.sm(item.getItem().getName()+" selected");
                                player.getPlayerVars().setSelectedVIPItem(item.getItem());
                                sendShopsInterface(player);
                                break;

                            case 2:
                                DonationItems item1 = DonationItems.level2()[slot];
                                player.sm(item1.getItem().getName()+" selected");
                                player.getPlayerVars().setSelectedVIPItem(item1.getItem());
                                sendShopsInterface(player);
                                break;

                            case 3:
                                DonationItems item3 = DonationItems.level3()[slot];
                                player.sm(item3.getItem().getName()+" selected");
                                player.getPlayerVars().setSelectedVIPItem(item3.getItem());
                                sendShopsInterface(player);
                                break;

                            case 4:
                                DonationItems item4 = DonationItems.level4()[slot];
                                player.sm(item4.getItem().getName()+" selected");
                                player.getPlayerVars().setSelectedVIPItem(item4.getItem());
                                sendShopsInterface(player);
                                break;
                        }
                        break;

                    case 40:
                        if(player.getPlayerVars().getSelectedVIPItem() != null){



                        }
                        break;

                }

                break;
        }
    }

    public void sendDonationManager(Player player){
        StringBuilder logs = new StringBuilder();
        for(String log : player.getAdventurerLogs().getDonationLog()) {
            if(log != null)
                logs.append(log).append("<br>");
        }

        player.getPackets().sendIComponentText(MANAGER_INTER, 24, ""+getCorruptCoins());
        player.getPackets().sendIComponentText(MANAGER_INTER, 27, getAmountDonated()+"$");
        player.getPackets().sendIComponentText(MANAGER_INTER, 30, getDonorRank());
        player.getPackets().sendIComponentText(MANAGER_INTER, 32, ""+getVIPPoints());
        player.getPackets().sendIComponentText(MANAGER_INTER, 34, ""+getTotalVipPoints());

        //Temp
        player.getPackets().sendHideIComponent(MANAGER_INTER, 36, true);

        player.getPackets().sendIComponentText(MANAGER_INTER, 39, (WorldSettings.donationBoostActive ? Colors.green+"ACTIVE - "+WorldSettings.donationBoost+"x": Colors.red+"INACTIVE"));
        player.getPackets().sendIComponentText(MANAGER_INTER, 41, (WorldSettings.donationBoostItemActive ? Colors.green+"ACTIVE - x"+WorldSettings.donationBoostItem.getAmount()+" of "+ WorldSettings.donationBoostItem.getName() : Colors.red+"INACTIVE"));

        player.getPackets().sendIComponentText(MANAGER_INTER, 45, logs.toString());

        player.getPackets().sendHideIComponent(MANAGER_INTER, 67, true);

        int index = 0;
        for(int i = 78; i <= 107; i+=3){
            if(MonthlyPackages.forId(index) == null){
                player.getPackets().sendHideIComponent(MANAGER_INTER, i, true);
            } else {
                player.getPackets().sendIComponentText(MANAGER_INTER, i + 1, Objects.requireNonNull(MonthlyPackages.forId(index)).getName());
                if (monthlyPackage[index] >= 1) {
                    player.getPackets().sendIComponentText(MANAGER_INTER, i + 2, monthlyPackage[index] +" / 30 left");
                } else {
                    player.getPackets().sendIComponentText(MANAGER_INTER, i + 2, "Cost : "+Objects.requireNonNull(MonthlyPackages.forId(index)).getPrice());
                }
            }
            index++;
        }

    }
}
