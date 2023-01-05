package com.rs.game.player;

import com.rs.game.player.content.corrupt.futures.OpenFuture;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerFutures {

    private final Player player;

    private final List<OpenFuture> futures = new CopyOnWriteArrayList<>();

    public PlayerFutures(Player player) {
        this.player = player;
    }

    public void addFuture(OpenFuture future) {
        futures.add(future);
    }

    public void removeFuture(OpenFuture future) {
        futures.remove(future);
    }

    public void process() {
        futures.forEach(OpenFuture::process);
    }
}
