package com.rs.game.player;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerWallet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1113309885494849608L;

    private final AtomicBoolean updating;
    private final String address;
    private final Map<String, Float> balances = new ConcurrentHashMap<>();
    private final List<String> transactions = new CopyOnWriteArrayList<>();

    private final List<String> pending = new CopyOnWriteArrayList<>();

    public PlayerWallet(String address, float balance) {
        this.address = address;
        this.updating = new AtomicBoolean(false);
        this.balances.put("Block Coin", balance);
    }

    public String getAddress() {
        return address;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(String transaction) {
        transactions.remove(transaction);
    }

    public boolean getPending(String serial) {
        return pending.contains(serial);
    }

    public void addPending(String transaction) {
        pending.add(transaction);
    }

    public void removePending(String transaction) {
        pending.remove(transaction);
    }

    public boolean isUpdating() {
        return updating.get();
    }

    public void setUpdating(boolean updating) {
        this.updating.set(updating);
    }

    public Map<String, Float> getBalances() {
        return balances;
    }

    public void setBalance(String currency, float balance) {
        balances.put(currency, balance);
    }

    public float getBalance(String currency) {
        return balances.getOrDefault(currency, 0f);
    }


}
