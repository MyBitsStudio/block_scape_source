package com.rs.game.player.content.corrupt.donations;

import com.rs.game.item.Item;

public enum MonthlyPackages {

    GOLD_RUSH(0,"Gold Rush",250,15,new Item[]{
            new Item(995,5000000)
    }),
    RUNECOIN_INVESTMENT(1, "RuneCoins Investing", 200, 10, null),

    ;

    private final int id,runeCoins,price;
    private final String name;
    private final Item[] items;

    MonthlyPackages(int id, String name,int price,int runeCoins, Item[] items){
        this.id= id;
        this.name= name;
        this.price= price;
        this.runeCoins= runeCoins;
        this.items= items;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getRuneCoins() {
        return runeCoins;
    }

    public Item[] getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public static MonthlyPackages forId(int id){
        for(MonthlyPackages packages : MonthlyPackages.values())
            if(packages.getId() == id)
                return packages;
        return null;
    }

}
