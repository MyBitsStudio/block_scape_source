package com.rs.game.player.content.items;

import com.rs.game.Animation;
import com.rs.game.world.region.Region;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * Handles all skilling portables.
 * @author Zeus
 */
public class Portables {
    
    /**
     * Handles the portable object placement.
     * @param owner The player placing the object.
     * @param item The portable Item.
     * @param objectId The Object ID.
     */
    public static void placePortable(Player owner, Item item, int objectId) {
    	WorldTile tile = new WorldTile(owner.getX() + 1, owner.getY(), owner.getPlane());
    	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    		tile = new WorldTile(owner.getX() - 1, tile.getY(), tile.getPlane());
        	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
        		tile = new WorldTile(owner.getX(), tile.getY() + 1, tile.getPlane());
            	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    	    		tile = new WorldTile(tile.getX(), owner.getY() - 1, tile.getPlane());
    	        	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    					owner.sendMessage("You cannot place a portable here; try moving around.");
    					return;
    				}
    			}
    		}
    	}
    	final WorldTile finalTile = tile;
    	WorldObject object = new WorldObject(objectId, 10, 0, finalTile.getX(), finalTile.getY(), finalTile.getPlane(), owner);
		World.spawnTemporaryDivineObject(object, 300000, owner); //5 minutes
	    owner.setNextAnimation(new Animation(21217));
		owner.faceObject(object);
		owner.getInventory().deleteItem(item);
    }

	public static void placePortableBank(Player owner, Item item, int objectId) {
		WorldTile tile = new WorldTile(owner.getX() + 1, owner.getY(), owner.getPlane());
		if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
			tile = new WorldTile(owner.getX() - 1, tile.getY(), tile.getPlane());
			if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
				tile = new WorldTile(owner.getX(), tile.getY() + 1, tile.getPlane());
				if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
					tile = new WorldTile(tile.getX(), owner.getY() - 1, tile.getPlane());
					if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
						owner.sendMessage("You cannot place a portable here; try moving around.");
						return;
					}
				}
			}
		}
		final WorldTile finalTile = tile;
		WorldObject object = new WorldObject(objectId, 10, 0, finalTile.getX(), finalTile.getY(), finalTile.getPlane(), owner);
		World.spawnTemporaryDivineObject(object, 3600000, owner); //1 Hr
		owner.setNextAnimation(new Animation(21217));
		owner.faceObject(object);
		owner.getInventory().deleteItem(item);
	}
    
    /**
     * Checks if the object can be placed.
     * @param plane The plane coord.
     * @param x The X coord.
     * @param y The Y coord.
     * @return if can be placed.
     */
    private static boolean canPlaceObject(Player owner, int plane, int x, int y) {
		return World.canMoveNPC(plane, x, y, 2)
				&& World.getObjectWithSlot(owner, Region.OBJECT_SLOT_FLOOR) == null
				&& owner.getControlerManager().getControler() == null;
	}
}