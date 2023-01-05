package com.rs.tools;

import com.google.gson.*;
import com.rs.ServerLauncher;
import com.rs.game.player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class TestJson {

    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ServerLauncher.main(new String[0]);

        JsonArray object = new JsonArray();
        try (BufferedReader reader = new BufferedReader(new FileReader("./data/json/shops.json"))) {
            object = gson.fromJson(reader, JsonArray.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (JsonElement element : object) {
            JsonObject shop = element.getAsJsonObject();
            System.out.println(shop.get("name").getAsString());
            JsonArray items = shop.get("items").getAsJsonArray();
            for (JsonElement item : items) {
                JsonObject itemObj = item.getAsJsonObject();
                System.out.println(itemObj.get("id").getAsInt());
                System.out.println(itemObj.get("amount").getAsInt());
                System.out.println(itemObj.get("price").getAsInt());
            }
        }

    }
}
