package com.rs.game.player.content.corrupt.futures.impl;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.BlockchainUtils;
import com.hyperledger.connector.packet.outgoing.PlayerToPlayerTransactionRequestPacket;
import com.hyperledger.connector.packet.outgoing.ServerTransactionRequestPacket;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.futures.OpenFuture;
import com.rs.game.world.World;
import com.rs.utils.SerializableFilesManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;


public class DuelArenaFuture extends OpenFuture {

    private final Player start, target;

    public DuelArenaFuture(@NotNull Player start, @NotNull Player target) {
        this.start = start;
        this.target = target;
        this.usernames = new String[] { start.getUsername() , target.getUsername() };
        this.values = new float[] { 0 , 0 };
        this.properties.put("start", start.getUsername());
        this.properties.put("target", target.getUsername());
        this.properties.put("status", "start");
    }

    public DuelArenaFuture addBalance(float value, float value2){
        this.values[0] = value;
        this.values[1] = value2;
        return this;
    }

    public DuelArenaFuture addItems(List<Item> first, List<Item> second){
        this.capped.put("first", first);
        this.capped.put("second", second);
        return this;
    }

    public DuelArenaFuture setVictor(String victor){
        this.properties.put("victor", victor);
        return this;
    }

    @Override
    public DuelArenaFuture build() {
        this.properties.replace("status", "build");
        this.start.getFutures().addFuture(this);
        this.target.getFutures().addFuture(this);

        this.properties.replace("status", "running");
        return this;
    }

    @Override
    public DuelArenaFuture finish() {
        this.properties.replace("status", "pending");
        String victor = (String) this.properties.get("victor");
        if(victor.equals("draw")){
            Optional<Player> one = World.getPlayerOption((String) this.properties.get("start"));
            Optional<Player> two = World.getPlayerOption((String) this.properties.get("target"));
            if(one.isPresent()){
                Player start = one.get();
                List<Item> first = this.capped.get("first");
                for(Item item : first){
                    start.getInventory().addOrBank(item);
                }
                start.getInventory().refresh();
            }
            if(two.isPresent()){
                Player target = two.get();
                List<Item> first = this.capped.get("second");
                for(Item item : first){
                   target.getInventory().addOrBank(item);
                }
                target.getInventory().refresh();
            }
        } else {
            Optional<Player> winner = World.getPlayerOption(victor);
            if(winner.isPresent()){
                Player player = winner.get();
                String other = victor.equals(this.properties.get("start")) ? (String) this.properties.get("target") : (String) this.properties.get("start");
                Optional<Player> loser = World.getPlayerOption(other);
                Player others = loser.orElseGet(() -> SerializableFilesManager.loadPlayer(other));
                List<Item> first = this.capped.get("first");
                for(Item item : first){
                    player.getInventory().addOrBank(item);
                }
                List<Item> second = this.capped.get("second");
                for(Item item : second){
                    player.getInventory().addOrBank(item);
                }
                player.getInventory().refresh();

                BlockchainConnector connect = BlockchainConnector.singleton();

                PlayerToPlayerTransactionRequestPacket packet = new PlayerToPlayerTransactionRequestPacket(others, player,
                        (this.properties.get("victor").equals(this.properties.get("start")) ? values[1] : values[0]), "Block Coin");
                connect.sendEncrypted(packet);
            }
        }
        this.start.getFutures().removeFuture(this);
        this.target.getFutures().removeFuture(this);
        this.properties.replace("status", "finished");
        return this;
    }

    @Override
    public DuelArenaFuture process() {
        this.properties.replace("status", "processing");

        this.properties.replace("status", "running");
        return this;
    }

    @Override
    public DuelArenaFuture cancel() {
        this.properties.replace("status", "cancelled");
        return this;
    }
}
