package com.rs.game.world.shops;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hyperledger.BlockchainConnector;
import com.hyperledger.BlockchainUtils;
import com.hyperledger.connector.packet.outgoing.StoreTransactionRequestPacket;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.QuestManager.Quests;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.clans.content.perks.ClanPerk;
import com.rs.game.world.WorldCurrency;
import com.rs.utils.ItemExamines;
import com.rs.utils.ItemSetsKeyGenerator;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class Shop {

	/**
	 * New Shop for Blockchain
	 * @author Corrupt
	 */

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();
	private static final int NEW_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;

	private ShopItem[] items;
	private int money;
	private int amount;

	private boolean ironMan;

	private boolean canSell;

	private WorldCurrency currency;

	private String wallet, shortName;

	private CopyOnWriteArrayList<Player> viewingPlayers;

//	public static int[][] pkShop = { { 13896, 5000 }, { 13884, 5000 }, { 13890, 5000 }, { 13887, 5000 },
//			{ 13893, 5000 }, { 13876, 5000 }, { 13870, 5000 }, { 13873, 5000 }, { 13864, 5000 }, { 13858, 5000 },
//			{ 13861, 5000 }, { 13899, 2500 }, { 13905, 2500 }, { 13902, 2500 }, { 13867, 2500 }, { 13879, 2500 },
//			{ 13883, 2500 }, { 11846, 3500 }, { 11848, 3500 }, { 11850, 3500 }, { 11852, 3500 }, { 11854, 3500 },
//			{ 11856, 3500 }, { 21768, 3500 }, { 23680, 5000 }, { 23682, 5000 }, { 23679, 7500 }, { 23681, 7500 } };
//
//	public static int[][] Starfire = { { 35588, 6000 }, { 35589, 7000 }, { 35590, 6500 }, { 35593, 6500 },
//			{ 35594, 7500 }, { 35595, 6800 }, { 35598, 6000 }, { 35599, 6500 }, { 35600, 6500 } };
//
//	public static int[][] reaperShop = { { 31875, 500 }, { 31872, 550 }, { 31869, 350 }, { 31878, 450 }, { 11789, 300 },
//			{ 1419, 300 }, { 27996, 1000 }, { 31846, 0 }, { 31851, 350 }, { 39501, 750 } };
//
//	public static int[][] ZombieMinigame = { { 24154, 1500 }, { 34409, 5550 }, { 34411, 5350 }, { 34413, 4450 },
//			{ 34415, 3300 }, { 34417, 3300 }, { 7592, 4500 }, { 7593, 4500 }, { 7595, 4100 }, { 7596, 4800 } };
//	public static int[][] FightCaves = { { 26128, 15000 }, { 26132, 15000 }, { 26130, 15000 }, { 26140, 10000 },
//			{ 26136, 9000 }, { 26124, 5000 }, { 26126, 5000 }, { 6523, 8000 }, { 6524, 7000 }, { 6525, 8000 },
//			{ 6526, 7000 }, { 6528, 7000 }, { 6527, 7000 } };
//
//	public static int[][] PrestigeShop = { { 21472, 15 }, { 21473, 20 }, { 21474, 20 }, { 21475, 2 }, { 21476, 3 },
//			{ 21467, 10 }, { 21468, 15 }, { 21469, 15 }, { 21470, 2 }, { 21471, 3 }, { 21462, 10 }, { 21463, 15 },
//			{ 21464, 15 }, { 21465, 2 }, { 21466, 3 }, { 14295, 25 }, { 14305, 20 }, { 14385, 20 }, { 14315, 25 },
//			{ 24186, 20 }, { 34200, 7 }, { 34201, 10 }, { 34202, 9 }, { 34203, 2 }, { 34204, 3 }, { 31580, 7 },
//			{ 31581, 10 }, { 31582, 9 }, { 31583, 2 }, { 31584, 3 } };
//	public static int[][] PrestigeShop2 = { { 32347, 7 }, { 32348, 10 }, { 32349, 9 }, { 32350, 3 }, { 32351, 2 } };
//	public static int[][] PvmShop1 = { { 34836, 150 }, { 29441, 150 }, { 29442, 250 }, { 17273, 550 }, { 28566, 350 },
//			{ 35886, 350 }, { 24154, 250 }, { 4503, 500 }, { 4504, 500 }, { 4505, 500 }, { 4506, 500 }, { 4507, 500 } };
//
//	public static int[][] PvmShop2 = { { 11663, 4000 }, { 11664, 4000 }, { 11665, 4000 }, { 8839, 6000 },
//			{ 8840, 6000 }, { 8842, 3000 }, { 25430, 15000 }, { 32622, 4 }, { 989, 500 }, { 6746, 7500 },
//			{ 13663, 50000 }, { 25202, 2000 }, { 40191, 6000 } };
//
//	public static int[][] PvmShop3 = { { 11780, 150 }, { 27360, 150 }, { 10330, 15000 }, { 10332, 18000 },
//			{ 10334, 17000 }, { 10336, 15000 }, { 10338, 12000 }, { 10340, 15000 }, { 10342, 13500 }, { 10344, 12000 },
//			{ 10346, 15000 }, { 10348, 15500 }, { 10350, 15250 }, { 10352, 13250 } };
//
//	public static int[][] dungShop = { { 36164, 100000 }, { 25991, 250000 }, { 25993, 250000 }, { 16955, 1000000 },
//			{ 16403, 1000000 }, { 27663, 1200000 }, { 27913, 1200000 }, { 16337, 1000000 }, { 17017, 1000000 },
//			{ 10025, 12500 }, { 25995, 250000 }, { 31445, 50000 }, { 36164, 100000 }, { 31463, 250000 },
//			{ 27069, 180000 }, { 27071, 160000 } };
//
//	public static int[][] voteShop = {
//
//			{ 25430, 40 }, { 34233, 25 },
//			/** First tower **/
//			{ 26107, 19 }, { 26108, 19 }, { 26119, 19 }, { 6665, 5 }, { 6666, 5 }, { 6199, 7 },
//
//			/** Barrows box sets **/
//			{ 11846, 45 }, { 11848, 45 }, { 11850, 45 }, { 11852, 45 }, { 11854, 45 }, { 11856, 45 }, { 21768, 45 },
//
//			/** Clue Scroll dyes **/
//			{ 33294, 160 }, { 33296, 160 }, { 33298, 160 }, { 36274, 160 }, { 41887, 160 },
//
//			/** Ringmaster outfit **/
//			// { 13672, 15 }, { 13673, 30 }, { 13674, 12 }, { 13675, 8 },
//
//			/** Darklight C. Key D.Bone Kit **/
//			{ 6746, 14 }, { 989, 3 }, { 24352, 6 }, { 34889, 10 },
//
//			/** Imbued Rings **/
//			{ 15018, 45 }, { 15019, 45 }, { 15020, 25 }, { 15220, 80 },
//
//			/** Relic Helm **/
//			{ 28355, 200 }, { 28358, 200 }, { 28353, 200 },
//
//			/** Rare cosmetic tokens and new box **/ // circus ticket for bank
//			{ 34027, 29 }, { 35886, 19 }, { 3188, 25 }, { 13663, 180 } };
//
//	public static int[][] HweenShop = { { 11789, 60 }, { 39569, 100 }, { 24154, 3 }, { 34409, 30 }, { 34411, 30 },
//			{ 34413, 30 }, { 34415, 30 }, { 34417, 30 }, { 7592, 30 }, { 7593, 30 }, { 7595, 30 }, { 7596, 30 },
//			{ 6106, 5 }, { 6107, 10 }, { 6109, 10 }, { 6110, 5 }, { 6111, 8 } };
//
//	public static int[][] SilverShop = { { 4151, 2 }, { 8839, 3 }, { 8842, 2 }, { 8840, 2 }, { 11665, 2 }, { 11663, 2 },
//			{ 11664, 2 }, { 989, 2 }, { 6585, 3 }, { 11694, 7 }, { 11696, 5 }, { 11700, 5 }, { 11698, 5 }, { 6739, 3 },
//			{ 15259, 3 }, };
//
//	public static int[][] GoldShop = { { 19785, 4 }, { 19786, 4 }, { 25025, 2 }, { 25022, 3 }, { 11724, 6 },
//			{ 11726, 5 }, { 25019, 3 }, { 25013, 2 }, { 25016, 2 }, { 11728, 2 }, { 11718, 3 }, { 11720, 5 },
//			{ 11722, 4 }, { 25010, 2 }, };
//	public static int[][] PlatinumShop = { { 1038, 21 }, { 1040, 20 }, { 1042, 21 }, { 1044, 20 }, { 1046, 20 },
//			{ 1048, 24 }, { 1050, 15 }, { 1053, 10 }, { 1055, 10 }, { 1057, 10 }, { 39448, 10 }, { 39252, 10 },
//			{ 35892, 10 }, { 35889, 10 }, { 34478, 10 }, { 34196, 10 }, { 31025, 10 }, { 44239, 10 }, };
//
//	public static int[][] auraShops = { { 22889, 200 }, { 22895, 200 },
//			// Tier 1
//			{ 22897, 200 }, { 20966, 200 }, { 22905, 200 }, { 22891, 200 }, { 22294, 200 }, { 23848, 200 },
//			{ 22296, 200 }, { 22280, 200 }, { 22300, 200 }, { 20958, 200 }, { 22284, 200 }, { 22893, 200 },
//			{ 22292, 200 }, { 20965, 200 }, { 20962, 200 }, { 22899, 200 }, { 20967, 200 }, { 20964, 200 },
//			{ 22927, 200 }, { 22298, 200 }, { 30784, 200 },
//			// Tier 2
//			{ 22268, 400 }, { 22270, 400 }, { 22274, 400 }, { 22276, 400 }, { 22278, 400 }, { 22282, 400 },
//			{ 22286, 400 }, { 22290, 400 }, { 22885, 400 }, { 22901, 400 }, { 22907, 400 }, { 22929, 400 },
//			{ 23842, 400 }, { 23850, 400 }, { 22302, 400 }, { 22272, 400 }, { 30786, 400 },
//			// Tier 3
//			{ 22887, 800 }, { 22903, 800 }, { 22909, 800 }, { 22911, 800 }, { 22917, 800 }, { 22919, 800 },
//			{ 22921, 800 }, { 22923, 800 }, { 22925, 800 }, { 22931, 800 }, { 22933, 800 }, { 23844, 800 },
//			{ 23852, 800 }, { 22913, 800 }, { 22915, 800 }, { 30788, 800 },
//			// Tier 4
//			{ 23854, 1300 }, { 23856, 1300 }, { 23858, 1300 }, { 23860, 1300 }, { 23862, 1300 }, { 23864, 1300 },
//			{ 23866, 1300 }, { 23868, 1300 }, { 23870, 1300 }, { 23872, 1300 }, { 23874, 1300 }, { 23876, 1300 },
//			{ 23878, 1300 }, { 30790, 1300 },
//			// Tier 5
//			{ 30792, 2600 }, { 30794, 2600 }, { 30796, 2600 }, { 30798, 2600 }, { 30800, 2600 }, { 30802, 2600 },
//			{ 30804, 2600 },
//			// cosmetics
//			{ 33655, 6000 }, { 33853, 6000 }, { 34123, 5000 }, { 34124, 5000 }, { 34125, 5000 }, { 34126, 5500 },
//			{ 34129, 5000 }, { 34130, 5000 }, { 34131, 5000 }, { 34132, 5500 }, };
//
//	public static int[][] triviaShop = { { 15027, 8 }, { 15028, 15 }, { 15029, 12 }, { 15031, 5 }, { 15032, 5 },
//			{ 15033, 16 }, { 15034, 30 }, { 15035, 24 }, { 15037, 10 }, { 15038, 10 }, { 5554, 10 }, { 5553, 15 },
//			{ 5555, 12 }, { 5556, 5 }, { 5557, 5 }, { 18744, 50 }, { 18745, 50 }, { 18746, 50 }, { 18747, 50 },
//			{ 32228, 100 }, { 32240, 85 }, { 1763, 3 }, { 1765, 3 }, { 1767, 3 }, { 29760, 60 }, { 29761, 60 },
//			{ 15043, 5 }, { 15044, 5 }, // sliske masks lord marshal boots &
//										// gloves
//			{ 15039, 10 }, { 15040, 15 }, { 15041, 12 }, { 15042, 12 }, // lord
//																		// marshall
//																		// armor
//																		// and
//																		// hat
//			{ 28140, 30 }, { 9005, 40 }, { 15598, 75 }, // cake hat, fancy
//														// boots, caitlin staff
//			{ 15602, 45 }, { 15600, 90 }, { 15604, 70 }, // white trivia dye
//															// items
//			{ 15620, 25 }, { 15618, 50 }, { 15622, 35 }, // dark infinity items
//			{ 32210, 100 }, { 36164, 125 } // crystal staff
//	};
//
//	public static int[][] raresShop = { { 23675, 3 }, { 23676, 3 }, { 23677, 3 }, { 23678, 3 }, { 28020, 3 }, // squeal
//																												// horns
//			{ 37130, 4 }, { 34007, 5 }, { 34008, 5 }, { 34036, 5 }, // chic
//																	// scarf,
//																	// horns,
//																	// halo,
//																	// sunglasses
//			{ 33733, 7 }, { 33734, 7 }, { 33735, 7 }, // snowboards
//			{ 20929, 7 }, { 24474, 7 }, { 37131, 10 }, // katana, boogie bow,
//														// coin
//			{ 34006, 10 }, { 962, 10 }, { 34028, 15 }
//
//	};
//	public static int[][] damageShop = { { 24950, 10000 }, { 11335, 2000000 }, { 25758, 3000000 }, { 14479, 5000000 },
//			{ 27360, 1000000 }, { 21472, 4000000 }, { 11780, 1000000 }, { 21473, 7000000 }, { 21474, 5000000 },
//			{ 21475, 2000000 }, { 21476, 1000000 }, { 10346, 17000000 }, { 10348, 17000000 }, { 10350, 5000000 },
//			{ 10352, 10000000 }, { 10330, 12000000 }, { 10332, 12000000 }, { 10334, 2000000 }, { 10336, 5000000 },
//			{ 10342, 5000000 }, { 10338, 17000000 }, { 10340, 17000000 }, { 10344, 8000000 } };
//
//	public static int[][] xmasShop = {
//			/** Santa costume */
//			{ 14595, 400 }, { 14602, 150 }, { 14605, 150 }, { 14603, 400 }, { 14596, 250 },
//
//			/** Reindeer and Yo-yo */
//			{ 10507, 150 }, { 4079, 450 },
//
//			/** Christmas ghostly outfit */
//			{ 15422, 200 }, { 15423, 250 }, { 15425, 250 },
//
//			/** Penguin outfit */
//			{ 36082, 200 }, { 36084, 200 }, { 36085, 200 }, { 36086, 150 }, { 36087, 150 },
//
//	};
//	public static int[][] SlayerShop = {
//
//			{ 30656, 650 }, { 30686, 650 }, { 30716, 650 }, { 40377, 650 }, { 40312, 500 }, { 40316, 500 },
//			{ 36066, 150 }, { 37125, 750 }, { 37126, 700 } };
//
//	public static int[][] TuskenShop = { { 35159, 200 }, { 35165, 600 }, { 35167, 450 }, { 35161, 100 }, { 35163, 100 },
//			{ 35174, 200 }, { 35180, 600 }, { 35182, 450 }, { 35176, 100 }, { 35178, 100 }, { 35189, 200 },
//			{ 35195, 600 }, { 35197, 450 }, { 35191, 100 }, { 35193, 100 } };
//
//	public static int[][] EliteDungeonShop = { { 43164, 18000 }, { 43165, 18000 }, { 43375, 200000 },
//			{ 28095, 1800000 }, { 28099, 1800000 }, { 28103, 1800000 }
//
//	};

	public Shop(String name, int money, Item @NotNull [] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public Shop(String name, WorldCurrency currency, ShopItem @NotNull [] items, boolean sell, boolean ironman, String wallet){
		viewingPlayers = new CopyOnWriteArrayList<>();
		this.name = name;
		this.currency = currency;
		this.items = items;
		this.canSell = sell;
		this.ironMan = ironman;
		this.wallet = wallet;
		defaultQuantity = new int[items.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = items[i].getAmount();
	}

	public Shop(String shortName, ShopItem @NotNull [] items){
		this.shortName = shortName;
		this.items = items;
		defaultQuantity = new int[items.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = items[i].getAmount();
	}


	public int getMoney() {
		return money;
	}

	public Item[] getGeneralStock() {
		return generalStock;
	}

	public int[] getDefaultQuantity(){
		return defaultQuantity;
	}

	public String getName(){
		return name;
	}

	public String getWallet(){
		return wallet;
	}

	public String getShortName(){
		return shortName;
	}

	private boolean addItem(int itemId, int quantity) {
		for (ShopItem item : items) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(() -> {
			viewingPlayers.remove(player);
			player.getTemporaryAttributtes().remove("Shop");
		});
		sendNewShop(player);
//		player.getPackets().sendConfig(118, MAIN_STOCK_ITEMS_KEY);
//		sendStore(player);
//		player.getInterfaceManager().sendInterface(1265);
//		player.getPackets().sendGlobalConfig(1876, -1);
//		player.getPackets().sendConfig(1496, -1);
//		player.getPackets().sendConfig(532, 995);
//		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize() * 6, 1150);
//		player.getPackets().sendIComponentSettings(1265, 26, 0, getStoreSize() * 6, 82903066);
//		player.getPackets().sendIComponentText(1265, 85, name);
//		if (isGeneralStore())
//			player.getPackets().sendHideIComponent(1265, 52, false);
//		sendInventory(player);
		sendExtraConfigs(player);
	}

	public void sendNewShop(Player player){
		player.getPackets().sendConfig(118, NEW_ITEMS_KEY);
		//sendItems(player);
		player.getInterfaceManager().sendInterface(3022);
		player.getPackets().sendIComponentText(1265, 85, name);
	}

	public void sendItems(Player player, int key){
		Item[] stock = new Item[items.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(stockItems(), 0, stock, 0, items.length);
		player.getPackets().sendItems(key, stock);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[items.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(stockItems(), 0, stock, 0, items.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, items.length, generalStock.length);
		player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
	}

	public Item[] stockItems(){
		Item[] item = new Item[items.length];
		for(int i = 0; i < items.length; i++){
			item[i] = new Item(items[i].getId(), items[i].getAmount());
		}
		return item;
	}

	public double getBuyPrice(@NotNull Item item) {

		switch (item.getId()) {
			case 21773:
			case 25202:
				item.getDefinitions().setValue(3000);
				break;
			case 13316:
			case 13317:
				item.getDefinitions().setValue(1);
				break;
			case 1785:
				item.getDefinitions().setValue(168);
				break;
			case 1265:
				item.getDefinitions().setValue(20);
				break;
			case 2347:
				item.getDefinitions().setValue(120);
				break;
			case 952:
				item.getDefinitions().setValue(10);
				break;
			case 5343:
				item.getDefinitions().setValue(260);
				break;
			case 5329:
				item.getDefinitions().setValue(126);
				break;
			case 1355:
				item.getDefinitions().setValue(160);
				break;
			case 1359:
				item.getDefinitions().setValue(7564);
				break;
			case 946:
				item.getDefinitions().setValue(521);
				break;
			case 21452:
				item.getDefinitions().setValue(100000000);
				break;
			case 21512:
				item.getDefinitions().setValue(2);
				break;
			case 303:
				item.getDefinitions().setValue(248);
				break;
			case 305:
				item.getDefinitions().setValue(108);
				break;
			case 301:
				item.getDefinitions().setValue(258);
				break;
			case 311:
				item.getDefinitions().setValue(300);
				break;
			case 307:
				item.getDefinitions().setValue(25);
				break;
			case 309:
				item.getDefinitions().setValue(5);
				break;
			case 314:
				item.getDefinitions().setValue(25);
				break;
			case 313:
				item.getDefinitions().setValue(236);
				break;
			case 1935:
				item.getDefinitions().setValue(25);
				break;
			case 11814:
				item.getDefinitions().setValue(636);
				break;
			case 11816:
				item.getDefinitions().setValue(984);
				break;
			case 11818:
				item.getDefinitions().setValue(1476);
				break;
			case 11820:
				item.getDefinitions().setValue(1808);
				break;
			case 11822:
				item.getDefinitions().setValue(3847);
				break;
			case 11824:
				item.getDefinitions().setValue(19700);
				break;
			case 11826:
				item.getDefinitions().setValue(16600);
				break;
			case 11828:
				item.getDefinitions().setValue(7767);
				break;
			case 11830:
				item.getDefinitions().setValue(8329);
				break;
			case 11832:
				item.getDefinitions().setValue(20900);
				break;
			case 11834:
				item.getDefinitions().setValue(22700);
				break;
			case 11836:
				item.getDefinitions().setValue(135400);
				break;
			case 11838:
				item.getDefinitions().setValue(205000);
				break;
			case 11840:
				item.getDefinitions().setValue(205000);
				break;
			case 221:
				item.getDefinitions().setValue(26);
				break;
			case 223:
				item.getDefinitions().setValue(942);
				break;
			case 225:
				item.getDefinitions().setValue(2758);
				break;
			case 4255:
				item.getDefinitions().setValue(5795);
				break;
			case 9594:
				item.getDefinitions().setValue(1004);
				break;
			case 4621:
				item.getDefinitions().setValue(2000);
				break;
			case 6693:
				item.getDefinitions().setValue(11506);
				break;
			case 241:
				item.getDefinitions().setValue(1297);
				break;
			case 12539:
				item.getDefinitions().setValue(3847);
				break;
			case 5972:
				item.getDefinitions().setValue(2483);
				break;
			case 231:
				item.getDefinitions().setValue(283);
				break;
			case 235:
				item.getDefinitions().setValue(2145);
				break;
			case 239:
				item.getDefinitions().setValue(2225);
				break;
			case 245:
				item.getDefinitions().setValue(2716);
				break;
			case 1975:
				item.getDefinitions().setValue(401);
				break;
			case 2970:
				item.getDefinitions().setValue(840);
				break;
			case 6070:
				item.getDefinitions().setValue(654);
				break;
			case 2437:
				item.getDefinitions().setValue(1025);
				break;
			case 2441:
				item.getDefinitions().setValue(2628);
				break;
			case 2443:
				item.getDefinitions().setValue(2732);
				break;
			case 2445:
				item.getDefinitions().setValue(10800);
				break;
			case 2435:
				item.getDefinitions().setValue(5801);
				break;
			case 2453:
				item.getDefinitions().setValue(5715);
				break;
			case 1725:
				item.getDefinitions().setValue(1205);
				break;
			case 6107:
				item.getDefinitions().setValue(5000);
				break;
			case 3138:
				item.getDefinitions().setValue(717);
				break;
			case 2481:
				item.getDefinitions().setValue(10000);
				break;
			case 19613:
				item.getDefinitions().setValue(5000);
				break;
			case 7633:
				item.getDefinitions().setValue(5000);
				break;
			case 1540:
				item.getDefinitions().setValue(128);
				break;
			case 21621:
				item.getDefinitions().setValue(6000);
				break;
			//thieving stalls
			case 532:
				item.getDefinitions().setValue(10000);
				break;
			case 15333://Overload Prestige10
				item.getDefinitions().setValue(100000);
				break;
			case 10499:
				item.getDefinitions().setValue(1000);
				break;

			case 6528://tzhaar-ket-om
				item.getDefinitions().setValue(68100);
				break;
			case 13445:
			case 4153:
			case 4154://g maul
				item.getDefinitions().setValue(200000);
				break;

			case 13506:
			case 1187:
			case 1188://d sq shield
				item.getDefinitions().setValue(159000);
				break;
			case 3105:
			case 3106://rock climbers
				item.getDefinitions().setValue(75000);
				break;

			//mage equipment store
			case 2580:
			case 2579://wizard boots
				item.getDefinitions().setValue(1000000);
				break;
			case 4675:
			case 4676://anc staff
				item.getDefinitions().setValue(65000);
				break;
			case 15486:
			case 15487://sol
				item.getDefinitions().setValue(2350000);
				break;

			//max

			case 20767:
				item.getDefinitions().setValue(50000000);
			case 20768:
				item.getDefinitions().setValue(5000000);
				break;
			case 24365://dragon kiteshield
				item.getDefinitions().setValue(75000000);
				break;
			case 13734://spirit shield
				item.getDefinitions().setValue(5000000);
				break;

			//skillcapes/hoods
			case 9748:
			case 9749:
			case 9751:
			case 9752:
			case 9754:
			case 9755:
			case 9757:
			case 9758:
			case 9760:
			case 9761:
			case 9763:
			case 9764:
			case 9766:
			case 9767:
			case 9769:
			case 9770:
			case 9772:
			case 9773:
			case 9775:
			case 9776:
			case 9778:
			case 9779:
			case 9781:
			case 9782:
			case 9784:
			case 9785:
			case 9787:
			case 9788:
			case 9790:
			case 9791:
			case 9793:
			case 9794:
			case 9796:
			case 9799:
			case 9800:
			case 9802:
			case 9803:
			case 9805:
			case 9806:
			case 9808:
			case 9809:
			case 9811:
			case 9812:
			case 18509:
			case 18510:
				item.getDefinitions().setValue(99000);
				break;
			case 6571://Uncut onyx
				item.getDefinitions().setValue(100000);
				break;
			case 590:
				item.getDefinitions().setValue(150);
				break;

		}
		return ItemDefinitions.getItemDefinitions(item.getId()).getValue();
	}

	public void buy(Player player, int slotId, int quantity, MultiShop shop) {
		BlockchainConnector connect = BlockchainConnector.singleton();
		if (slotId >= getStoreSize())
			return;
		ShopItem item = slotId >= items.length ? items[slotId - items.length] : items[slotId];
		if (item == null)
			return;
		if (slotId >= items.length) {
			if (!player.canTrade(null))
				return;
		}
		if(player.getWallet().isUpdating()){
			player.sendMessage("Your wallet is currently updating, please wait.");
			return;
		}
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("You need some inventory space to do this!");
			return;
		}
		int price = item.getPrice();
		Item itemx = new Item(item.getId());
		ClansManager manager = player.getClanManager();
		if (!(manager == null)) {
			if (player.getClanManager().getClan().getClanPerks().hasPerk(ClanPerk.Perks.BARTERING)) {
				price = (int) (itemx.getDefinitions().getValue() / 1.50);
			}
		}
		HashMap<Integer, Integer> requiriments = itemx.getDefinitions().getWearingSkillRequiriments();
		int buyQ = Math.min(item.getAmount(), quantity);
		boolean hasRequiriments = true;
		if (itemx.getName().contains("cape (t)") || itemx.getName().contains(" master cape")
				|| itemx.getName().equalsIgnoreCase("Dungeoneering cape")) {
			int skill = 0, level = 99;
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				level = requiriments.get(skillId);
				skill = skillId;
				if (player.getSkills().getLevelForXp(skillId) < requiriments.get(skillId))
					hasRequiriments = false;
			}
			if (!hasRequiriments) {
				player.sendMessage("You need " + Utils.getAorAn(itemx.getName()) + " "
						+ player.getSkills().getSkillName(skill) + " of " + level + " to buy "
						+ Utils.getAorAn(itemx.getName()) + " " + itemx.getName() + ".");
				return;
			}
		}
		if (itemx.getName().equalsIgnoreCase("blue cape") || itemx.getName().equalsIgnoreCase("red cape")) {
			if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
				player.sendMessage("You need to complete Nomad's Requiem to buy a " + itemx.getName() + ".");
				return;
			}
		}
		if (quantity > buyQ) {
			quantity = buyQ;
		}
		if (itemx.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.sendMessage("Inventory full. To make more room, sell, drop or bank something.");
			}
		}
		if (buyQ != 0) {
			double totalPrice = price * buyQ;
			float finalPrice = BlockchainUtils.coinToChain((int) totalPrice);
			if(player.getWallet().getBalance(shop.getCurrency().getName()) < finalPrice) {
				player.sendMessage("You don't have enough "+shop.getCurrency().getName()+" to buy this item.");
				return;
			}

			System.out.println("DEBUG : total price : "+totalPrice + " final price : "+finalPrice);
			StoreTransactionRequestPacket packet = new StoreTransactionRequestPacket(player, shop, finalPrice, shop.getCurrency().getName(),
					false, item, buyQ);
			connect.sendEncrypted(packet);


//			if (amountCoins + price > 0) {
//				if (player.getWallet().getBalance(this.moneyAddress) >= totalPrice) {
//					PurchaseTransaction purchase = player.getWallet().purchaseFromShop(this.getWallet().getPublicKey(), this, totalPrice, new Item(item.getId(), buyQ));
//					if (purchase != null) {
//						if (player.getInventory().addItem(item.getId(), buyQ)) {
//							MasterChain.singleton.addTransactionToChain(purchase);
//							player.sendMessage(totalPrice + " has been sent to the shop.");
//
//							item.setAmount(item.getAmount() - buyQ);
//							if (item.getAmount() <= 0 && slotId >= mainStock.length) {
//								generalStock[slotId - mainStock.length] = null;
//							}
//						}
//					}
//				} else {
//					player.getPackets().sendGameMessage("You can't afford to buy that many");
//					return;
//				}
//			}
			//refreshShop();
			sendInventory(player);
		}
	}

	public int getAmount() {
		return this.amount;
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < items.length; i++)
			if (items[i].getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	public Item[] getMainStock() {
		return this.mainStock;
	}

	public int getStoreSize() {
		return items.length;
	}

	/**
	 * Checks if the player is buying an item or selling it.
	 * 
	 * @param player The player
	 * @param slotId The slot id
	 * @param amount The amount
	 */
	public void handleShop(@NotNull Player player, int slotId, int amount) {
		boolean isBuying = player.getTemporaryAttributtes().get("shop_buying") != null;
		if (isBuying)
			buy(player, slotId, amount, null);
		else
			sell(player, slotId, amount, null);
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0, getStoreSize() * 6, 1150);
		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getAmount() < defaultQuantity[i]) {
				items[i].setAmount(items[i].getAmount() + 1);
				needRefresh = true;
			} else if (items[i].getAmount() > defaultQuantity[i]) {
				items[i].setAmount(items[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	public void sell(@NotNull Player player, int slotId, int quantity, MultiShop shop) {
		BlockchainConnector connect = BlockchainConnector.singleton();
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		int originalId = item.getId();
		if (ItemConstants.getItemDefaultCharges(item.getId()) != -1 || !ItemConstants.isTradeable(item)
				|| item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		if (item.getDefinitions().isDestroyItem()) {
			player.getPackets().sendGameMessage("This item cannot be sold to stores.");
			return;
		}
		if(!shop.canSell()){
			player.sendMessage("You can't sell items to this shop.");
			return;
		}
		ClansManager manager = player.getClanManager();
		double price = getSellPrice(item);
		if (!(manager == null)) {
			if (player.getClanManager().getClan().getClanPerks().hasPerk(ClanPerk.Perks.BARTERING)) {
				price = (int) (item.getDefinitions().getTipitPrice() / 3.3);
			}
		}
		if (item.getDefinitions().isNoted()) {
			Item newItem = new Item(item.getDefinitions().getCertId());
			price = newItem.getDefinitions().getValue();
		}
		int numberOff = player.getInventory().getItems().getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (numberOff + quantity == Integer.MAX_VALUE || numberOff + quantity < 0) {
			player.sendMessage("The shop would go over max if you sold all these items.");
			return;
		}
		if (addItem(item.getId(), quantity)) {
			double fPrice = price * quantity;

			System.out.println("DEBUG : total price : "+fPrice);
			StoreTransactionRequestPacket packet = new StoreTransactionRequestPacket(player, shop, BlockchainUtils.getFromCoins(fPrice), shop.getCurrency().getName(),
					true, item, quantity);
			connect.sendEncrypted(packet);

		} else {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		refreshShop();
	}

	public static int getSellPrice(@NotNull Item item) {

		if (item.getDefinitions().getName().toLowerCase().contains("lucky")) {
			item.getDefinitions().setValue(10000000);
		}

		switch (item.getId()) {
			//SHOP BUYS FOR HALF OF THE VALUE
			//thieving stalls
			case 5020 -> item.getDefinitions().setValue(1000000000);
			case 5023 -> item.getDefinitions().setValue(2000000000);
			case 5022 -> item.getDefinitions().setValue(1000000);

			case 1963 -> item.getDefinitions().setValue(2000);
			case 590 -> item.getDefinitions().setValue(150);
			case 6571 ->//Uncut onyx
					item.getDefinitions().setValue(100000);
			case 25202 -> item.getDefinitions().setValue(3000);
			case 17815 -> //Raw CaveMoray
					item.getDefinitions().setValue(10000);
			case 24431 -> //Fish Mask
					item.getDefinitions().setValue(6000000);
			case 18177 -> //Cooked CaveMoray
					item.getDefinitions().setValue(1000);


			//tools store
			case 15259 ->//d pickaxe
					item.getDefinitions().setValue(11000000);
			case 6528 ->//tzhaar-ket-om
					item.getDefinitions().setValue(60000);
			case 13445, 4153, 4154 ->//g maul
					item.getDefinitions().setValue(170000);
			case 13506, 1187, 1188 ->//d sq shield
					item.getDefinitions().setValue(100000);
			case 11732, 3105, 3106 ->//rock climbers
					item.getDefinitions().setValue(65000);


			//mage equipment store
			case 2580, 2579 ->//wizard boots
					item.getDefinitions().setValue(750000);
			case 4675, 4676 ->//anc staff
					item.getDefinitions().setValue(60000);
			case 15486, 15487 ->//sol
					item.getDefinitions().setValue(2000000);
			case 24365 ->//dragon kiteshield
					item.getDefinitions().setValue(15000000);
			case 13734 ->//spirit shield
					item.getDefinitions().setValue(2500000);


			//skillcapes/hoods
			case 9748, 9749, 9751, 9752, 9754, 9755, 9757, 9758, 9760, 9761, 9763, 9764, 9766, 9767, 9769, 9770, 9772, 9773, 9775, 9776, 9778, 9779, 9781, 9782, 9784, 9785, 9787, 9788, 9790, 9791, 9793, 9794, 9796, 9799, 9800, 9802, 9803, 9805, 9806, 9808, 9809, 9811, 9812, 18509, 18510 ->
					item.getDefinitions().setValue(99000);
		}
		return ItemDefinitions.getItemDefinitions(item.getId()).getValue() / 2;
	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		ShopItem item = slotId >= items.length ? items[slotId - items.length] : items[slotId];
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getGEExamine(new Item(item.getId())));
	}

	public ShopItem getItemForSlot(Player player, int slotId){
		if (slotId >= getStoreSize())
			return null;
		return slotId >= items.length ? items[slotId - items.length] : items[slotId];
	}

	public void sendExtraConfigs(@NotNull Player player) {
		player.getPackets().sendConfig(2561, -1);
		player.getPackets().sendConfig(2562, -1);
		player.getPackets().sendConfig(2563, -1);
		player.getPackets().sendConfig(2565, 0);
		player.getTemporaryAttributtes().put("shop_buying", true);
		setAmount(player, 1);
	}

	public void sendInfo(Player player, int slotId, boolean isBuying) {
		if (isBuying) {
			if (slotId >= getStoreSize())
				return;
			ShopItem item = slotId >= items.length ? items[slotId - items.length] : items[slotId];
			if (item == null)
				return;
			if (Settings.DEBUG)
				Logger.log("ButtonHandler", "Item ID: " + item.getId() + ".");
			float price = BlockchainUtils.getFromCoins(item.getPrice());
			player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
			player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
			player.getPackets().sendConfig(2562, item.getId());
			player.getPackets().sendConfig(2563, item.getId() == 995 ? -1 : slotId);
			player.getPackets().sendGlobalConfig(1876, ItemDefinitions.getEquipType(new Item(item.getId()).getName()));
			player.getPackets().sendConfig(2564, 1);
			player.getPackets().sendIComponentText(1265, 40, ItemExamines.getGEExamine(new Item(item.getId())));
			player.getPackets().sendIComponentText(1265, 205, "" + price);

			if (isGeneralStore()) {
				player.getPackets().sendHideIComponent(1265, 52, false);
			}
			player.getPackets()
					.sendGameMessage(new Item(item.getId()).getDefinitions().getName() + ": shop will " + "sell"
							+ " for " + price);
		} else {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				return;
			double price = getBuyPrice(item);
			player.getTemporaryAttributtes().put("SellSelectedSlot", slotId);
			player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
			player.getPackets().sendConfig(2562, item.getId());
			player.getPackets().sendConfig(2563, item.getId() == 995 ? -1 : slotId);
			player.getPackets().sendConfig(1496, item.getId());
			player.getPackets().sendGlobalConfig(1876, ItemDefinitions.getEquipType(item.getName().toLowerCase()));
			player.getPackets().sendConfig(2564, 1);
			player.getPackets().sendIComponentText(1265, 40, ItemExamines.getGEExamine(item));
			player.getPackets()
					.sendGameMessage(item.getDefinitions().getName() + ": shop will " + "buy"
							+ " for " +price);
		}
	}

	public void getSellInfo(Player player, int slotId){
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		double price = getBuyPrice(item);
		player.getTemporaryAttributtes().put("SellSelectedSlot", slotId);
		player.getPackets()
				.sendGameMessage(item.getDefinitions().getName() + ": shop will " + "buy"
						+ " for " +price);
	}

	public void sendInventory(@NotNull Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 28, 0, 1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Value", "Sell 1", "Sell 5", "Sell 10",
				"Sell 500", "Examine");
	}

	/**
	 * Sends Shop item value.
	 * 
	 * @param player The player to send to.
	 * @param slotId The Shops item slotID.
	 */
	public void sendValue(@NotNull Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage(item.getName() + " cannot be sold to shops.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && items == null) {
			player.getPackets().sendGameMessage("You can't sell " + item.getName() + " to this shop.");
			return;
		}
		double price = getSellPrice(item);
		if (item.getDefinitions().isNoted()) {
			Item newItem = new Item(item.getDefinitions().getCertId());
			price = newItem.getDefinitions().getValue();
		}
		if (money == 995)
			player.sendMessage(item.getDefinitions().getName() + ": shop will buy for: "
					+ price+"; right-click to sell.");

	}

	public void setAmount(@NotNull Player player, int amount) {
		this.amount = amount;
		player.getPackets().sendIComponentText(1265, 67, String.valueOf(amount));
	}
}