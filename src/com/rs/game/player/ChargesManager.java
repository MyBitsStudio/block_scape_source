package com.rs.game.player;

import java.io.Serializable;
import java.util.HashMap;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class ChargesManager implements Serializable {

	private static final long serialVersionUID = -5978513415281726450L;

	private transient Player player;

	private HashMap<Integer, Integer> charges;

	public ChargesManager() {
		charges = new HashMap<Integer, Integer>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		if (player.getPerks().isUnlocked(Perks.CHARGE.getId()))
	        return;
		Item[] items = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			if (player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				int newId = ItemConstants.getDegradeItemWhenCombating(item.getId());
				if (newId != -1) {
					player.getPackets().sendGameMessage(item.getDefinitions().getName() + " has degraded slightly!");
					item.setId(newId);
					player.getEquipment().refresh(slot);
					player.getGlobalPlayerUpdater().generateAppearenceData();
				}
			}
			int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstants.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Utils.currentTimeMillis() && ItemConstants.itemDegradesWhileCombating(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
		}
	}

	public void die() {
		Item[] equipItems = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < equipItems.length; slot++) {
			if (equipItems[slot] != null && degradeCompletly(equipItems[slot]))
				player.getEquipment().getItems().set(slot, null);
		}
		Item[] invItems = player.getInventory().getItems().getItems();
		for (int slot = 0; slot < invItems.length; slot++) {
			if (invItems[slot] != null && degradeCompletly(invItems[slot]))
				player.getInventory().getItems().set(slot, null);
		}
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(Item item) {
		int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstants.itemDegradesWhileWearing(item.getId()) 
					|| ItemConstants.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstants.getItemDefaultCharges(item.getId()) == -1 ? false : true;
				item.setId(newId);
			} else {
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId(newId);
				}
				break;
			}
		}
		return false;
	}

	public void wear(int slot) {
		Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getGlobalPlayerUpdater().generateAppearenceData();
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + " has degraded slightly!");
	}

	private void degrade(int itemId, int defaultCharges, int slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems().set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1)
					player.getPackets().sendGameMessage(ItemDefinitions.getItemDefinitions(itemId).getName() + " turned into dust.");
				else
					player.getPackets().sendGameMessage(ItemDefinitions.getItemDefinitions(itemId).getName() + " has degraded slightly!");
				player.getEquipment().refresh(slot);
				player.getGlobalPlayerUpdater().generateAppearenceData();
				return;
			}
		}
		charges.put(itemId, c);
	}
	
	public void dust(Item item) {
		int newId = ItemConstants.getItemDustOnDeath(item.getId());
		String itemname = item.getDefinitions().getName();
		if (newId != -1) {
			item.setId(newId);
			player.sendMessage(Colors.red+"Your "+itemname+" has turned into ashes.");
		}
	}
	
	public void death(Item item) {
		int newId = ItemConstants.getDegradeItemOnDeath(item.getId());
		if (newId != -1)
			item.setId(newId);
	}
	
	public static final String REPLACE = "##";

	public void checkPercentage(String message, int id, boolean reverse) {
		int charges = getCharges(id);
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		int percentage = reverse ? (charges == 0 ? 0 : (100 - (charges * 100 / maxCharges))) : charges == 0 ? 100 : (charges * 100 / maxCharges);
		player.getPackets().sendGameMessage(message.replace(REPLACE, String.valueOf(percentage)));
		if (Settings.DEBUG)
			System.out.println("Charges left: "+charges+".");
	}
	
	public boolean checkCharges(Item item) {
		if (item.getDefinitions().containsOption(2, "Inspect") 
				|| item.getDefinitions().containsOption(2, "Check") 
				|| item.getDefinitions().containsOption(2, "Check state") 
				|| item.getDefinitions().containsOption(2, "Check-charges") 
				|| item.getDefinitions().containsOption(2, "Check charges")) {
			//exeptions.
			if (item.getId() == 11284 || item.getId() == 25559 || item.getId() == 25562)
				player.getDialogueManager().startDialogue("SimpleMessage", "There are no charges left within this shield.");
			else if (item.getId() == 11283 || item.getId() == 25558 || item.getId() == 25561)
				checkCharges("There are " + REPLACE + " charges remaining in your dragonfire shield.", item.getId());
			else if (item.getId() == 22444)
				checkCharges("There is " + REPLACE + " doses of neem oil remaining.", item.getId());
			else if ((item.getId() >= 24450 && item.getId() <= 24454) || (item.getId() >= 22358 && item.getId() <= 22369))
				checkPercentage("The gloves are " + REPLACE + "% degraded.", item.getId(), true);
			else if (item.getId() >= 22458 && item.getId() <= 22497)
				checkPercentage(item.getName() + ": " + REPLACE + "% remaining.", item.getId(), false);
			else if (item.getId() == 20171 || item.getId() == 20173)
				checkPercentage(item.getName() + ": has " + getCharges(item.getId()) + " shots left.", item.getId(), false);
			else
				//default, add exeption if not same message in rs.
				checkPercentage("Your " + item.getName().toLowerCase() + " has " + REPLACE + "% of its charges left.", item.getId(), false);
			return true;
		}
		return false;
	}

	public void checkCharges(String message, int id) {
		player.getPackets().sendGameMessage(message.replace(REPLACE, String.valueOf(getCharges(id))));
	}

	public int getCharges(int id) {
		Integer c = charges.get(id);
		return c == null ? 0 : c;
	}

	/*
	 * -1 inv
	 */
	public void addCharges(int id, int amount, int wearSlot) {
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		if (maxCharges == -1) {
			System.out.println("This item cant get charges atm " + id);
			return;
		}
		Integer c = charges.get(id);
		int amt = c == null ? maxCharges : amount + c;
		if (amt > maxCharges)
			amt = maxCharges;
		if (amt <= 0) {
			int newId = ItemConstants.getItemDegrade(id);
			if (newId == -1) {
				if (wearSlot == -1)
					player.getInventory().deleteItem(id, 1);
				else
					player.getEquipment().getItems().set(wearSlot, null);
			} else if (wearSlot == -1) {
				player.getInventory().deleteItem(id, 1);
				player.getInventory().addItem(newId, 1);
			} else {
				Item item = player.getEquipment().getItem(wearSlot);
				if (item == null)
					return;
				item.setId(newId);
				player.getEquipment().refresh(wearSlot);
				player.getGlobalPlayerUpdater().generateAppearenceData();
			}
			resetCharges(id);
		} else
			charges.put(id, amt);
	}

	public void resetCharges(int id) {
		charges.remove(id);
	}
}