package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.world.Graphics;
import com.rs.game.world.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.summoning.Summoning.Pouches;

public class Littlebloodrager extends Familiar {

	private static final long serialVersionUID = 4970310429889437114L;

	public Littlebloodrager(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Sundering Strike";
	}

	@Override
	public String getSpecialDescription() {
		return "Deals a more damaging attack that is 10% more accurate, and reduces opponent's Defence.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {//COMBAT SPECIAL
		setNextGraphics(new Graphics(2445));
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		return true;
	}
}
