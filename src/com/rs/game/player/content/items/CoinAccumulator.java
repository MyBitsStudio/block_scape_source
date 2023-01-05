package com.rs.game.player.content.items;

import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.utils.Utils;

/**
 * Handles the Coin Accumulator item.
 * @author Zeus
 */
public class CoinAccumulator {

	/**
	 * Handles the item.
	 * @param player The player.
	 * @return true if coin drop.
	 */
	public static boolean handleCoinAccumulator(Player player, NPC npc, int quantity) {
		if (Utils.random((player.getPerks().isUnlocked(Perks.COLLECTOR.getId()) ? 4 : 5)) == 0) {
			if (player.getInventory().containsItem(15585, 1) || player.getPerks().isUnlocked(Perks.COLLECTOR.getId())) {
				dropCoins(player, npc, (int) (quantity * (player.getPerks().isUnlocked(Perks.COLLECTOR.getId()) ? 1.25 : 1)), true);
				return true;
			}
			if (player.getPetManager().isReceivePet()) {
				dropCoins(player, npc, (int) (quantity * (player.getPerks().isUnlocked(Perks.COLLECTOR.getId()) ? 1.25 : 1)), true);
				return true;
			}
			dropCoins(player, npc, (int) (quantity * (player.getPerks().isUnlocked(Perks.COLLECTOR.getId()) ? 1.25 : 1)), false);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a coin drop.
	 * @param player The player.
	 * @param npc The NPC.
	 * @param quantity Coin quantity.
	 * @param inventory if add to Inventory.
	 */
	private static void dropCoins(Player player, NPC npc, int quantity, boolean inventory) {
		Item item = new Item(995, quantity);
		if(player.getMoneyPouchValue() + quantity < 0 || player.getMoneyPouchValue() + quantity == Integer.MAX_VALUE)
			return;
		if (!inventory) {
			World.updateGroundItem(item, new WorldTile(npc.getLastWorldTile()), player, 60, 0);
		} else
			player.addMoney(quantity);
	}
}