package com.rs.game.player;

import com.rs.game.item.Item;
import com.rs.game.player.content.corrupt.cosmetics.Cosmetics;

import java.io.Serializable;

public class PlayerVariables implements Serializable {

    private static final long serialVersionUID = 5294039267995580176L;
    /**
     * Stores player variables
     *
     * @author - Quantum
     */

    private transient Player player;

    //Interface Tabs / Trackers

    private int interface3004tab, interface3004modeSelected, interface3009tab, interface3009boss, interface3010tab, interface3010boss,
    interface3012tab, interface3013tab, interface3015tab, interface3015piece, interface3015selected, interface3015page, interface3018Tab,
    interface3017Tab, interface3017animationSelected;

    private long doubleXpTimerNew;
    public long doubleXpTimer;
    //Others
    private Item selectedVIPItem;
    private int graveStone, paDamage;
    private long quadExp;
    private boolean filterLocked, augury, renewal, rigour, efficiency, life, cleansing;
    private String[] activeRaids;
    private boolean[] unlockedRaids;
    private Cosmetics.CosmeticPiece piece;
    private Cosmetics.CosmeticSets set;

    private int viewingTabOfShop;


    public PlayerVariables(){
        unlockedRaids = new boolean[124];

        setActiveRaids();
    }

    void setActiveRaids(){
        activeRaids = new String[]{

        };
    }

    public void setPlayer(Player player) {this.player = player;}

    //Sets & Gets


    public long getQuadExp() {
        return quadExp;
    }
    public void setQuadExp(long quadExp) {
        this.quadExp = quadExp;
    }
    public boolean isFilterLocked() {
        return filterLocked;
    }
    public void setFilterLocked(boolean filterLocked) {
        this.filterLocked = filterLocked;
    }
    public int getGravestone() {
        return this.graveStone;
    }
    public void setGravestone(int id) {
        this.graveStone = id;
    }
    public int getPAdamage() {
        return paDamage;
    }
    public void setPAdamage(int PAdamage) {
        this.paDamage = PAdamage;
    }
    public Cosmetics.CosmeticPiece getPiece(){ return piece;}
    public void setPiece(Cosmetics.CosmeticPiece piece) { this.piece = piece;}
    public Cosmetics.CosmeticSets getSet(){ return set; }
    public void setSet(Cosmetics.CosmeticSets set){ this.set = set;}
    public void setAugury(boolean aug) {
        this.augury = aug;
    }
    public boolean hasAuguryActivated() {
        return augury;
    }
    public void setRenewal(boolean ren) {
        this.renewal = ren;
    }
    public boolean hasRenewalActivated() {
        return renewal;
    }
    public void setRigour(boolean rig) {
        this.rigour = rig;
    }
    public boolean hasRigourActivated() {
        return rigour;
    }
    public void setEfficiency(boolean eff) {
        this.efficiency = eff;
    }
    public boolean hasEfficiencyActivated() {return this.efficiency; }
    public void setLife(boolean life) {
        this.life = life;
    }
    public boolean hasLifeActivated() {
        return this.life;
    }
    public void setCleansing(boolean cleanse) {
        this.cleansing = cleanse;
    }
    public boolean hasCleansingActivated() {
        return this.cleansing;
    }
    public boolean hasBonusEXP() {
        return getTimeLeft() > 1;
    }
    public long getTimeLeft() {
        return doubleXpTimer / 100;
    }
    public boolean isDoubleXp() {
        return doubleXpTimerNew > 1;
    }
    public void addDoubleXpTimer(long timer) {
        doubleXpTimerNew += timer;
    }
    public void setDoubleXpTimer(long timer) {
        doubleXpTimerNew = timer;
    }
    public long getDoubleXpTimer() {
        return doubleXpTimerNew;
    }

    //interface stuff
    public int getInterface3009Tab() {return interface3009tab;}
    public void setInterface3009Tab(int set){ this.interface3009tab = set;}
    public int getInterface3009Boss() {return interface3009boss;}
    public void setInterface3009Boss(int set){ this.interface3009boss = set;}
    public int getInterface3010Tab() {return interface3010tab;}
    public void setInterface3010Tab(int set){ this.interface3010tab = set;}
    public int getInterface3010Boss() {return interface3010boss;}
    public void setInterface3010Boss(int set){ this.interface3010boss = set;}
    public int getInterface3012Tab() {return interface3012tab;}
    public void setInterface3012Tab(int set){ this.interface3012tab = set;}
    public int getInterface3013Tab() {return interface3013tab;}
    public void setInterface3013Tab(int set){ this.interface3013tab = set;}
    public int getInterface3015Tab() {return interface3015tab;}
    public void setInterface3015Tab(int set){ this.interface3015tab = set;}
    public int getInterface3015Piece() {return interface3015piece;}
    public void setInterface3015Piece(int set){ this.interface3015piece = set;}
    public int getInterface3015Selected() {return interface3015selected;}
    public void setInterface3015Selected(int set){ this.interface3015selected = set;}
    public int getInterface3015Page() {return interface3015page;}
    public void setInterface3015Page(int set){ this.interface3015page = set;}
    public int getInterface3004Tab() {return interface3004tab;}
    public void setInterface3004Tab(int set){ this.interface3004tab = set;}
    public int getInterface3004modeSelected() {return interface3004modeSelected;}
    public void setInterface3004ModeSelected(int set){ this.interface3004modeSelected = set;}
    public int getInterface3018tabSelected() {return interface3018Tab;}
    public void setInterface3018tabSelected(int set){ this.interface3018Tab = set;}
    public Item getSelectedVIPItem(){ return this.selectedVIPItem;}
    public void setSelectedVIPItem(Item item){ this.selectedVIPItem = item;}

    public void setViewingTabOfShop(int tab){ this.viewingTabOfShop = tab;}

    public int getViewingTabOfShop(){ return this.viewingTabOfShop;}


}
