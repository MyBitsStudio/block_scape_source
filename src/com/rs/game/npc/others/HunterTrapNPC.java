package com.rs.game.npc.others;

import java.util.List;

import com.rs.game.Animation;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.hunter.TrapAction.HunterNPC;
import com.rs.game.player.actions.hunter.TrapAction.Traps;
import com.rs.game.player.content.OwnedObjectManager;
import com.rs.game.player.content.corrupt.perks.Perks;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class HunterTrapNPC extends NPC {

	private final Traps trap;
	private final HunterNPC hNPC;

	private WorldObject o;
	private int captureTicks;

	public HunterTrapNPC(HunterNPC hNPC, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		this.hNPC = hNPC;
		this.trap = hNPC.getTrap();
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (captureTicks > 0) {
			captureTicks++;
			if (captureTicks == 5) {
				if (hNPC.equals(HunterNPC.CRIMSON_SWIFT) || hNPC.equals(HunterNPC.GOLDEN_WARBLER) || hNPC.equals(HunterNPC.COPPER_LONGTAIL) || hNPC.equals(HunterNPC.CERULEAN_TWITCH) || hNPC.equals(HunterNPC.TROPICAL_WAGTAIL) || hNPC.equals(HunterNPC.WIMPY_BIRD))
					addWalkSteps(o.getX(), o.getY(), -1, false);
			} else if (captureTicks == 6) {
				setNextAnimation(new Animation(hNPC.getIds()[1]));
			} else if (captureTicks == 7) {// up to five
				if (!OwnedObjectManager.convertIntoObject(o, new WorldObject(hNPC.getIds()[0], o.getType(), o.getRotation(), new WorldTile(o.getTileHash())), player -> {
					if (player == null || isDead())
						return false;
					int currentLevel = player.getSkills().getLevel(Skills.HUNTER),
							lureLevel = hNPC.getLureLevel();
					double ratio = ((double) (trap.getRequirementLevel() + 20) / lureLevel) * currentLevel;
					return currentLevel >= lureLevel && !(ratio < Utils.random(player.getPerks().isUnlocked(Perks.HUNTSMAN.getId()) ? 80 : 95));
				})) {
					int anim = hNPC.getIds()[2];
					if (anim != -1)
						setNextAnimation(new Animation(anim));
					OwnedObjectManager.convertIntoObject(o, new WorldObject(trap.getIds()[2], o.getType(), o.getRotation(), new WorldTile(o.getTileHash())), null);
				} else
					setRespawnTask();
			} else if (captureTicks == 8) {
				setCantInteract(false);
			} else if (captureTicks == 10) {
				o = null;
				captureTicks = 0;
			}
			return;
		}

		if (o != null || hasFinished())
			return;
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects == null)
			return;
		for (final WorldObject o : objects) {
			if (o.getId() != trap.getIds()[1] || !withinDistance(o, 4) || Utils.random(25) != 0)
				continue;
			this.o = o;
			this.captureTicks = 1;
			setCantInteract(true);
			resetWalkSteps();
			calcFollow(o, true);
			faceObject(o);
			break;
		}
	}
}