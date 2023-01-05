package com.rs.game.npc.others;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.game.world.controllers.WarriorsGuild;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class AnimatedArmor extends NPC {

    private final transient Player player;

    public AnimatedArmor(Player player, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.player = player;
    }

    @Override
    public void processNPC() {
		super.processNPC();
		if (!withinDistance(player, 10))
			finish();
    }

    @Override
    public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
		    int loop;
	
		    @Override
		    public void run() {
				if (loop == 0) {
				    setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
				    if (source instanceof Player) {
						Player player = (Player) source;
						for (Integer items : getDroppedItems()) {
						    if (items == -1)
						    	continue;
						    World.updateGroundItem(new Item(items), player, player, 60, 0);
						}
						player.setWarriorPoints(3, WarriorsGuild.ARMOR_POINTS[getId() - 4278] 
								* (player.getPerks().isUnlocked(Perks.MINI_GAMER.getId()) ? 2 : 1));
				    }
				    finish();
				    stop();
				}
				loop++;
		    }
		}, 0, 1);
    }

    public int[] getDroppedItems() {
		int index = getId() - 4278;
		return WarriorsGuild.ARMOUR_SETS[index];
    }

    @Override
    public void finish() {
		if (hasFinished())
		    return;
		super.finish();
		if (player != null) {
		    player.getTemporaryAttributtes().remove("animator_spawned");
		    if (!isDead()) {
				for (int item : getDroppedItems()) {
				    if (item == -1)
				    	continue;
				    player.getInventory().addItem(item, 1);
				}
		    }
		}
    }
}
