package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.world.Graphics;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.summoning.Summoning.Pouches;

public class Clayfamiliarclass2 extends Familiar {

	private static final long serialVersionUID = 7289857564889907408L;

	public Clayfamiliarclass2(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Clay Deposit";
	}

	@Override
	public String getSpecialDescription() {
		return "Deposit all items in the beast of burden's possession in exchange for points.";
	}

	@Override
	public int getBOBSize() {
		return 6;
	}

	@Override
	public int getSpecialAmount() {
		return 30;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		//TODO
		//if (getOwner().getControlerManager().getControler() == null || !(getOwner().getControlerManager().getControler() instanceof StealingCreationController)) {
		//	dissmissFamiliar(false);
		//	return false;
		//}
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		//StealingCreationController sc = (StealingCreationController) getOwner().getControlerManager().getControler();
		//Score score = sc.getGame().getScore(getOwner());
		//if (score == null)
		//	return false;
		for (Item item : getBob().getBeastItems().getItems()) {
			if (item == null)
				continue;
			//sc.getGame().sendItemToBase(getOwner(), item, sc.getTeam(), true, false);
		}
		return true;
	}
}
