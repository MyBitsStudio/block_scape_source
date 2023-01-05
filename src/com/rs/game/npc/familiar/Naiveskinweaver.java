package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.world.Graphics;
import com.rs.game.world.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.summoning.Summoning.Pouches;

public class Naiveskinweaver extends Familiar {

	private static final long serialVersionUID = 8073842237001323628L;

	public Naiveskinweaver(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Glimmer of Light";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores 30 hitpoints.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 13;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.heal(30);//Increases by 10 each time
		getOwner().setNextGraphics(new Graphics(1300));
		getOwner().setNextAnimation(new Animation(7660));
		return true;
	}
}
