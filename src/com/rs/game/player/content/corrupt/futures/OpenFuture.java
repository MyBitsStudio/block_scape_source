package com.rs.game.player.content.corrupt.futures;

import com.rs.game.item.Item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class OpenFuture {

    protected Map<String, List<Item>> capped = new ConcurrentHashMap<>();
    protected Map<String, Object> properties = new ConcurrentHashMap<>();
    protected String[] usernames;
    protected float[] values;

    public abstract OpenFuture build();
    public abstract OpenFuture finish();
    public abstract OpenFuture process();
    public abstract OpenFuture cancel();

    public String getStatus(){
        return (String) properties.get("status");
    }
}
