package com.rise_of_the_six;

import java.util.List;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.world.map.MapBuilder;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.world.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public class RiseOfTheSix extends Controller {
	private static final Object THHAAR_MEJ_JAL = 13633;

	private int[] regionBase;
	public WorldTile base;

	public boolean spawned;
	protected NPC bossNPC;

	private int WaveId;

	public int Chest = 87996;
	public int Barrier = 87994;

	public int dharok = 18540;
	public int verac = 18545;
	public int guthan = 18541;
	public int torag = 18544;
	public int ahrim = 18538;
	public int karil = 18543;


	
	private static final Item[] COMMUM_REWARDS = { new Item(558, 1790), new Item(562, 773), new Item(560, 3910),
			new Item(565, 1640), new Item(4740, 880), new Item(1128, 10), new Item(1514, 75), new Item(15271, 60),
			new Item(1748, 80), new Item(9245, 60), new Item(1392, 45), new Item(452, 34), new Item(9144, 300),
			new Item(9193, 300), new Item(1616, 50) };
	
	private static final Item[] MALEVOLENT_ENERGY = { new Item(30027, 1) }; 
			
	
	private static final Item[] DECENT_REWARDS = { new Item(12163, 50), new Item(12158, 100), new Item(12160, 100),
			new Item(12159, 100), new Item(12183, 1000) };

	private static final Item[] RARE_REWARDS = { new Item(1149, 1), new Item(987, 1), new Item(985, 1),
			new Item(4708, 1), new Item(4710, 1), new Item(4712, 1), new Item(4714, 1), new Item(4716, 1),
			new Item(4718, 1),

			new Item(4720, 1), new Item(4722, 1), new Item(4724, 1), new Item(4726, 1), new Item(4728, 1),
			new Item(4730, 1), new Item(4732, 1), new Item(4734, 1), new Item(4736, 1), new Item(4738, 1),
			new Item(4745, 1), new Item(4747, 1), new Item(4749, 1), new Item(4751, 1), new Item(4753, 1),
			new Item(4755, 1), new Item(4757, 1), new Item(4087, 1), new Item(24452, 1), new Item(24453, 1),
			new Item(24454, 1) };

	private static final Item[] MALEV_REWARDS = { new Item(30014, 1), new Item(30018, 1), new Item(30022, 1) };
	private static final Item[] Chromatic_REWARDS = { new Item(34356, 1) };
	private static final Item[] PET_REWARDS = { new Item(30031, 1), new Item(30032, 1), new Item(30033, 1),
			new Item(30034, 1), new Item(30035, 1), new Item(30036, 1) };

	@Override
	public void start() {
		// = RegionBuilder.findEmptyChunkBound(8, 8);
		// RegionBuilder.copyAllPlanesMap(418, , regionChucks[0], regionChucks[1], 8);
		regionBase = MapBuilder.findEmptyChunkBound(8, 8);
		MapBuilder.copyAllPlanesMap(288, 736, regionBase[0], regionBase[1], 10, 10);   //418, 1176 good old one
		player.setNextWorldTile(getWorldTile(15, 16));
		player.getInventory().deleteItem(30004, 1);
		player.sm("As you enter the barrows brothers sense your hostility");
		player.sm("Enter the barrier to begin.");
		hasStarted = false;
		WaveId = 0;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == Chest) {
			lootChest();
		}
		if (object.getId() == Barrier && hasStarted == false) {
			passBarrier();
		}
		return false;
	}

	private boolean noSpaceOnInv;
	private boolean hasStarted;

	public void drop(Item item) {
		Item dropItem = new Item(item.getId(),		
				Utils.random(
						item.getDefinitions().isStackable() ? item.getAmount() * Settings.DROP_RATE : item.getAmount())
						+ 1);
		if (!noSpaceOnInv && player.getInventory().addItem(dropItem))
			return;
		noSpaceOnInv = true;
		player.getBank().addItem(dropItem, false);
		player.getPackets().sendGameMessage("Your loot was placed into your bank.");
	}

	public void lootChest() {
		player.setrosPoints(player.getrosPoints() + 1);
		player.sm(Colors.red+"[Rise Of The Six] </col>"+Colors.green+ "Kill count:</col> "+ player.getrosPoints());
		int chromatic = 1000 + (int) player.getDropRate();
		int pet = 300 + (int) player.getDropRate();
		int armor = 75 + (int) player.getDropRate();
		int rare = 15 + (int) player.getDropRate();
        int energy = 4 + (int) player.getDropRate();
		
		if (Utils.random(chromatic) == 0) {
			World.sendWorldMessage(
					"<img=7><col=9A2EFE>News: " + player.getDisplayName()
							+ " has just found the impossibly rare Chromatic PartyHat from the Rise of the Six Chest!",
					false);
			drop(Chromatic_REWARDS[Utils.random(Chromatic_REWARDS.length)]);
		}
		if (Utils.random(pet) == 0) {
			World.sendWorldMessage("<img=2><col=9A2EFE>News: " + player.getDisplayName()
					+ " has just found a rare pet from the Rise of the Six Chest!", false);
			drop(PET_REWARDS[Utils.random(PET_REWARDS.length)]);
		}
		if (Utils.random(armor) == 0) {
			World.sendWorldMessage("<img=2><col=9A2EFE>News: " + player.getDisplayName()
					+ " has just found a Rare Shield from the Rise of the Six Chest!", false);
			drop(MALEV_REWARDS[Utils.random(MALEV_REWARDS.length)]);
		}
		if (Utils.random(rare) == 0) {
			World.sendWorldMessage("<img=2><col=9A2EFE>News: " + player.getDisplayName()
					+ " has just found a  Rare Reward from the Rise of the Six Chest!", false);
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
		
		}
		if (Utils.random(energy) == 0) {
			//World.sendWorldMessage("<img=2><col=9A2EFE>News: " + player.getDisplayName()
			//		+ " has just found some  Malevolent energy from the Rise of the Six Chest!", false);
			drop(MALEVOLENT_ENERGY[Utils.random(MALEVOLENT_ENERGY.length)]);

		}
		if (Utils.random(10) == 0)
			drop(DECENT_REWARDS[Utils.random(DECENT_REWARDS.length)]);
		if (Utils.random(5) == 0)
			drop(COMMUM_REWARDS[Utils.random(COMMUM_REWARDS.length)]);
		drop(new Item(995, 250000));
		//drop(new Item(30027, 1));                           //(30027, Utils.random(0, 1)));

		player.sm("You managed to slay all the Barrows brothers and escape with some loot.");
		player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
		player.setForceMultiArea(false);
		removeControler();
	}

	@Override
	public void process() {
		if (spawned) {
			List<Integer> npcsInArea = World.getRegion(player.getRegionId()).getNPCsIndexes();
			if (npcsInArea == null || npcsInArea.isEmpty()) {
				spawned = false;
				WaveId += 1;
				nextWave(WaveId);
				System.out.println("next");
			}
		}

	}

	private void passBarrier() {
		//if (player.getFamiliar() != null || player.getPet() != null || Pets.hasPet(player)) {
		//	player.getDialogueManager().startDialogue("SimpleNPCMessage", THHAAR_MEJ_JAL,
		//			"Pets and Familiars are not allowed in Minigames or your account will Reset to 0!"
		//					+ " Please Dismiss your pet/familiar and remove your pet/pouches in your inventory");
		//	return;
		//}
		player.setForceMultiArea(true);
		player.lock(2);
		barrowsBros1();
		barrowsBros2();
		barrowsBros3();
		barrowsBros4();
		barrowsBros5();
		barrowsBros6();
		player.setNextWorldTile(getWorldTile(14, 21));
		hasStarted = true;

	}

	public void barrowsBros1() {
		bossNPC = new NPC(ahrim, getWorldTile(13, 23), -1, true, true);   //(16, 22)
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	public void barrowsBros2() {
		bossNPC = new NPC(dharok, getWorldTile(15, 24), -1, true, true);   //(15, 21)
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	public void barrowsBros3() {
		bossNPC = new NPC(guthan, getWorldTile(14, 20), -1, true, true);   //(14, 20)
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	public void barrowsBros4() {
		bossNPC = new NPC(karil, getWorldTile(19, 20), -1, true, true);   //(16, 20)
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	public void barrowsBros5() {
		bossNPC = new NPC(torag, getWorldTile(15, 21), -1, true, true);  //(15, 21)
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	public void barrowsBros6() {
		bossNPC = new NPC(verac, getWorldTile(12, 25), -1, true, true);    //(14, 21
		bossNPC.setForceMultiArea(true);
		bossNPC.setForceAgressive(true);
		bossNPC.setForceTargetDistance(64);
		bossNPC.setCapDamage(1500);
		spawned = true;
	}

	private void nextWave(int waveid) {
		if (waveid == 1) {
			player.getPackets().sendGameMessage("Congratulations! You've defeated the Barrows Brothers.");
			player.getPackets().sendGameMessage("You now have access to the chest.");
			World.spawnObject(new WorldObject(Chest, 10, 0, getWorldTile(16, 19)), true);
			return;
		}
	}

	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(regionBase[0] * 8 + mapX, regionBase[1] * 8 + mapY, 0);
	}

	@Override
	public boolean logout() {
		player.setForceMultiArea(false);
		removeControler();
		hasStarted = false;
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		hasStarted = false;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.sendMessage("You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					exitCave(1);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					player.unlock();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public void exitCave(int type) {
		if (type == 1) {
			hasStarted = false;
			player.setForceMultiArea(false);
			player.setNextWorldTile(new WorldTile(player.getHomeTile()));
		}
		removeControler();
	}

	@Override
	public void magicTeleported(int type) {
		player.setForceMultiArea(false);
		hasStarted = false;
		removeControler();
	}
}
