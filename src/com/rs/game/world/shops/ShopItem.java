package com.rs.game.world.shops;

public class ShopItem {

    private int id;
    private int amount;
    private int price;

    public ShopItem(int id, int amount, int price){
        this.id = id;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
