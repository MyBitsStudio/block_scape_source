package com.rs.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.*;
import com.rs.game.player.Player;
import com.rs.game.world.shops.MultiShop;
import com.rs.game.world.shops.Shop;
import com.rs.game.world.WorldCurrency;
import com.rs.game.world.shops.ShopItem;

/**
 * Used to handle Shops.
 * @author Corrupt
 */
public class ShopsHandler {

    private static final HashMap<Integer, MultiShop> handledShops = new HashMap<>();
	private static final String JSON_PATH = "./data/json/shops.json";

    public static void addShop(int key, MultiShop shop) {
    	handledShops.put(key, shop);
    }

    public static MultiShop getShop(int key) {
    	return handledShops.get(key);
    }

    public static void init() {
		loadShops();
    }

	private static void loadShops(){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonArray object = new JsonArray();

		try (BufferedReader reader = new BufferedReader(new FileReader(JSON_PATH))) {
			object = gson.fromJson(reader, JsonArray.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (JsonElement element : object) {
			JsonObject shop = element.getAsJsonObject();
			WorldCurrency currency = WorldCurrency.valueOf(shop.get("currency").getAsString());
			JsonArray shops = shop.get("shop").getAsJsonArray();
			Shop[] shopArray = new Shop[shops.size()];
			for(int i = 0; i < shops.size(); i++){
				JsonObject itemObj = shops.get(i).getAsJsonObject();
				JsonArray items = itemObj.get("items").getAsJsonArray();
				ShopItem[] itemArray = new ShopItem[items.size()];
				for(int j = 0; j < items.size(); j++){
					JsonObject item = items.get(j).getAsJsonObject();
					itemArray[j] = new ShopItem(item.get("id").getAsInt(), item.get("amount").getAsInt(), item.get("price").getAsInt());
				}
				shopArray[i] = new Shop(itemObj.get("shortName").getAsString(), itemArray);
			}
			addShop(shop.get("id").getAsInt(), new MultiShop(shop.get("id").getAsInt(), currency, shop.get("wallet").getAsString(), shop.get("name").getAsString(),
					shop.get("sell").getAsBoolean(), shop.get("ironman").getAsBoolean(), shopArray));
		}


//		for (JsonElement element : object) {
//			JsonObject shop = element.getAsJsonObject();
//			JsonArray shops = shop.get("shop").getAsJsonArray();
//
//			//JsonArray items = shop.get("items").getAsJsonArray();
//			ShopItem[] item = new ShopItem[items.size()];
//			for(int i = 0; i < items.size(); i++){
//				JsonObject itemObj = items.get(i).getAsJsonObject();
//				item[i] = new ShopItem(itemObj.get("id").getAsInt(), itemObj.get("amount").getAsInt(), itemObj.get("price").getAsInt());
//			}
//			WorldCurrency currency = WorldCurrency.valueOf(shop.get("currency").getAsString());
//			addShop(shop.get("id").getAsInt(), new Shop(shop.get("name").getAsString(), currency, item, shop.get("sell").getAsBoolean(), shop.get("ironman").getAsBoolean(),
//					shop.get("wallet").getAsString()));
//		}
	}

    public static boolean openShop(Player player, int key) {
    	MultiShop shop = getShop(key);
		if (shop == null)
		    return false;
		else {
			shop.addPlayer(player);
			return true;
		}
    }

    public static void restoreShops() {
    	MultiShop shop;
    	for (Iterator<MultiShop> iterator = handledShops.values().iterator();
    			iterator.hasNext(); 
    			shop.restoreItems())
    		shop = iterator.next();
    }

}