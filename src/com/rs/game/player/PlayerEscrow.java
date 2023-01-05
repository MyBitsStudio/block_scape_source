package com.rs.game.player;

import com.rs.game.item.Item;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerEscrow {

    private final List<Item> items;

    public PlayerEscrow() {
        items = new CopyOnWriteArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }


}
