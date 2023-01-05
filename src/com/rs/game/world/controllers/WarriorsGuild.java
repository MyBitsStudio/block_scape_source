package com.rs.game.world.controllers;

import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.world.World;
import com.rs.game.world.WorldObject;
import com.rs.game.world.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.AnimatedArmor;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.combat.PlayerCombat;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.network.protocol.codec.decode.impl.ButtonHandler;
import com.rs.utils.Utils;

public class WarriorsGuild extends Controller {

    public transient static WarriorTimer timer;
    public transient static int killedCyclopses;
    public transient static int amountOfPlayers;

    /**
     * Initiate the "Warrior's Guild" timer task.
     */
    public static void init() {
	CoresManager.fastExecutor.schedule(
		WarriorsGuild.timer = new WarriorTimer(), 600, 600);
    }

    /**
     * Static timer used for accurate measure within the
     * "Warrior's Guild Minigame".
     * 
     * @author Khaled
     * 
     */
    public static class WarriorTimer extends TimerTask {

	/**
	 * The current tick we are on.
	 */
	private int ticks;

	/**
	 * The last dummy UID generated by the timer.
	 */
	private double lastDummy;

	/**
	 * The projectile placeholder in order to properly check the
	 * "Catapult Minigame".
	 */
	public byte projectileType;

	@Override
	public void run() {
	    if (amountOfPlayers == 0) {
		this.cancel();
		return;
	    }
	    ticks++;
	    if (ticks % 14 == 0) {
		switchDummieAction();
		lastDummy += 0.000000001D;
		World.sendObjectAnimation(CATAPULT, new Animation(4164));
		projectileType = (byte) Utils.random(4);
		World.sendProjectile(CATAPULT_PROJECTILE_BASE, CATAPULT_TARGET,
			679 + projectileType, 85, 0, 15, 16, 15, 0);
	    }
	}

	/**
	 * Switch the current dummy location in the "Dummy Activity".
	 */
	private void switchDummieAction() {
	    int index = Utils.random(DUMMY_LOCATIONS.length);
	    World.spawnObjectTemporary(
		    new WorldObject(Utils.random(15624, 15630), 10,
			    DUMMY_ROTATIONS[index], DUMMY_LOCATIONS[index]),
		    6000);
	}
    }

    /**
     * Animation Activity (Combat)
     */
    public static final double[] ARMOR_POINTS = { 5, 10, 15, 20, 50, 60, 80 };
    public static final int[][] ARMOUR_SETS = { { 1155, 1117, 1075 },
	    { 1153, 1115, 1067 }, { 1157, 1119, 1069 }, { 1165, 1125, 1077 },
	    { 1159, 1121, 1071 }, { 1161, 1123, 1073 }, { 1163, 1127, 1079 } };
    private static final String[] ARMOUR_TYPE = { "Bronze", "Iron", "Steel",
	    "Black", "Mithril", "Adamant", "Rune" };

    /**
     * Dummy Activity (Attack)
     */
    private static final WorldTile[] DUMMY_LOCATIONS = {
	    new WorldTile(2860, 3549, 0), new WorldTile(2860, 3547, 0),
	    new WorldTile(2859, 3545, 0), new WorldTile(2857, 3545, 0),
	    new WorldTile(2855, 3546, 0), new WorldTile(2855, 3548, 0),
	    new WorldTile(2856, 3550, 0), new WorldTile(2858, 3550, 0) };
    private static final int[] DUMMY_ROTATIONS = { 1, 1, 2, 2, 3, 3, 0, 0 };

    /**
     * Catapult Activity (Defence)
     */
    private static final WorldTile CATAPULT_TARGET = new WorldTile(2842, 3541,
	    1);
    private static final NPC CATAPULT_PROJECTILE_BASE = new NPC(1957,
	    new WorldTile(2842, 3550, 1), -1, false);
    private static final Animation[] DEFENCIVE_ANIMATIONS = new Animation[] {
	    new Animation(4169), new Animation(4168), new Animation(4171),
	    new Animation(4170) };
    private static final WorldObject CATAPULT = new WorldObject(15616, 10, 0,
	    2840, 3548, 1);

    /**
     * Shotput Activity (Strength)
     */
    private static final WorldTile SHOTPUT_FACE_18LB = new WorldTile(2876,
	    3549, 1), SHOTPUT_FACE_22LB = new WorldTile(2876, 3543, 1);

    /**
     * Cylopes Activity
     */
    private static final int[] DEFENDERS = { 20072, 8850, 8849, 8848, 8847,
	    8846, 8845, 8844 };
    public static final WorldTile CYCLOPS_LOBBY = new WorldTile(2843, 3535, 2);

    /**
     * The available token output values.
     */
    public static final int STRENGTH = 0, DEFENCE = 1, ATTACK = 2, COMBAT = 3,
	    BARRLES = 4, ALL = 5;

    /**
     * The defencive style chosen to use during the "Catapult Activity".
     */
    private transient byte defensiveStyle;

    /**
     * A significant UID for the last dummy the {@code Player} has attacked.
     */
    private transient double lastDummy;

    /**
     * 
     */
    private transient byte kegCount;

    /**
     * 
     */
    private transient int kegTicks;

    public boolean inCyclopse;
    private int cyclopseOption;

    /**
     * If you have the required levels for proper enterance.
     * 
     * @param player
     *            The player we are checking.
     * @return whether or not the door will open.
     */
    public static boolean canEnter(Player player) {
    	if (player.getSkills().getLevelForXp(Skills.STRENGTH)
    			+ player.getSkills().getLevelForXp(Skills.ATTACK) < 130) {
    		player.sendMessage("You need at least 99 in either Attack/Strength or to have 130 combined in both.");
    		return false;
    	}
    	player.getControlerManager().startControler("WarriorsGuild");
    	player.setNextWorldTile(new WorldTile(2876, 3542, player.getPlane()));
    	return true;
    }
    
    public static void exit(Player player) {
    	player.getControlerManager().forceStop();
    	player.setNextWorldTile(new WorldTile(2877, 3542, player.getPlane()));
    	return;
    }

    @Override
    public void start() {
	if (getArguments() == null || getArguments().length != 2)
	    setArguments(new Object[] { false, -1 });
	if (amountOfPlayers == 0)
	    init();
	inCyclopse = (boolean) getArguments()[0];
	cyclopseOption = (int) getArguments()[1];
	sendInterfaces();
	amountOfPlayers++;
    }

    @Override
    public boolean canAttack(Entity target) {
	if (target instanceof AnimatedArmor) {
	    AnimatedArmor npc = (AnimatedArmor) target;
	    if (player != npc.getCombat().getTarget())
		return false;
	}
	return true;
    }

    @Override
    public void sendInterfaces() {
	if (inCatapultArea(player)
		&& player.getEquipment().getShieldId() == 8856)
	    sendShieldInterfaces();
	player.getInterfaceManager().sendOverlay(1057, false);
	for (int i = 0; i < player.getWarriorPoints().length; i++)
	    player.refreshWarriorPoints(i);
    }

    @Override
    public void process() {
	if (player.withinDistance(CATAPULT_TARGET, 0)) {
	    if (timer.ticks % 10 == 0) {
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			if (defensiveStyle == timer.projectileType) {
			    player.getSkills().addXp(Skills.DEFENCE, 15);
			    player.setWarriorPoints(DEFENCE, 5);
			    player.setNextAnimation(DEFENCIVE_ANIMATIONS[timer.projectileType]);
			    player.getPackets().sendGameMessage(
				    "You deflect the incomming attack.");
			} else {
			    player.getPackets()
				    .sendGameMessage(
					    "You fail to deflect the incomming attack.");
			    player.applyHit(new Hit(player, Utils
				    .random(10, 50), HitLook.REGULAR_DAMAGE));
			}
		    }
		}, 7);
	    }
	} else if (kegCount >= 1) {
	    if (kegCount == 5)
		kegTicks++;
	    if (timer.ticks % 15 == 0)
		player.setRunEnergy(player.getRunEnergy() - 9);
	    player.drainRunEnergy();
	    if (((double) player.getRunEnergy() / 101) <= Math.random()
		    || player.hasWalkSteps() && player.getRun()) {
		loseBalance();
		return;
	    }
		} else if (cyclopseOption != -1 && inCyclopse) {
			if (timer.ticks % 100 == 0) {
				if (cyclopseOption == ALL) {
					for (int index = 0; index < player.getWarriorPoints().length; index++)
						player.setWarriorPoints(index, -3);
				} else {
					player.setWarriorPoints(cyclopseOption, -10);
					player.sendMessage("10 of your warrior guild combat tokens crumble away.", true);
				}
	    	}
		}
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
	if (npc.getId() == 4290)
	    player.getDialogueManager().startDialogue("Shanomi");
	else if (npc.getId() == 8267)
	    player.getDialogueManager().startDialogue("HarlaakMenarous");
	else if (npc.getId() == 4293)
	    player.getDialogueManager().startDialogue("Lidio");
	else if (npc.getId() == 4294)
	    player.getDialogueManager().startDialogue("Lilly");
	else if (npc.getId() == 4295)
	    player.getDialogueManager().startDialogue("Anton");
	return false;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
	if (object.getId() >= 15624 && object.getId() <= 15630) {
	    if (lastDummy == timer.lastDummy) {
		player.getPackets().sendGameMessage(
			"You have already tagged a dummy.");
		return false;
	    }
	    submitDummyHit(object);
	    return false;
	} else if (object.getId() == 15656) {
	    player.getInterfaceManager().sendInterface(412);
	    return false;
	} else if (object.getId() == 66604) {
	    player.getInterfaceManager().sendInterface(410);
	    return false;
	} else if (object.getId() == 15664 || object.getId() == 15665) {
	    if (player.getTemporaryAttributtes().get("thrown_delay") != null) {
		int random = Utils.getRandom(3);
		player.getDialogueManager().startDialogue(
			"SimpleNPCMessage",
			4300,
			random == 0 ? "Just a moment, I dropped my hanky."
				: random == 1 ? "Pace yourself."
					: "Sorry, I'm not ready yet.");
		return false;
	    } else if (!hasEmptyHands()) {
		player.getDialogueManager()
			.startDialogue("SimpleMessage",
				"You must have both your hands free in order to throw a shotput.");
		return false;
	    }
	    player.getDialogueManager().startDialogue("ShotputD",
		    object.getId() == 15664);
	    return false;
	} else if (object.getId() == 15647 || object.getId() == 15641
		|| object.getId() == 15644) {
	    
	    boolean inLobby = player.getY() == object.getY();
	    if (object.getId() == 15647) {
		if (!inLobby) {
		    if (player.getEquipment().getShieldId() == 8856) {
			ButtonHandler.sendRemove(player, Equipment.SLOT_SHIELD, false);
			player.getInterfaceManager().sendInterfaces();
		    }
		}
	    }
	    player.setNextWorldTile(new WorldTile(object.getX(), inLobby ? object.getY()
		    + (object.getId() == 15647 ? 1 : -1) : object.getY(), player.getPlane()));
	    return false;
	} else if (object.getId() == 15658 || object.getId() == 15660
		|| object.getId() == 15653 || object.getId() == 66758
		&& object.getX() == 2861 && object.getY() == 3538
		&& object.getPlane() == 1) {
	    if (object.getId() == 15653)
		player.getControlerManager().forceStop();
	    else if (object.getId() == 66758 && player.getX() == object.getX())
		resetKegBalance();
	    player.setNextWorldTile(new WorldTile(player.getX() == object.getX() ? object.getX()
		    + (object.getId() == 66758 ? -1 : 1) : object.getX(), object.getY(), player.getPlane()));
	    return false;
	} else if (object.getId() >= 15669 && object.getId() <= 15673) {
	    if (hasEmptyHands()
		    && (player.getEquipment().getHatId() == -1 || kegCount >= 1))
		balanceKeg(object);
	    else if (kegCount == 0)
		player.getDialogueManager()
			.startDialogue("SimpleMessage",
				"You must have both your hands and head free to balance kegs.");
	    return false;
	} else if (object.getId() == 66599 || object.getId() == 66601) {
		if (player.getX() >= 2847) {
			player.setNextFaceWorldTile(CYCLOPS_LOBBY);
			boolean withinArea = player.getX() == object.getX();
			if (!withinArea) {
				player.setNextWorldTile(new WorldTile(withinArea ? 
						object.getX() + 1 : object.getX(), object.getY(), player.getPlane()));
				inCyclopse = false;
			}
		} else {
			if (player.getWarriorPoints()[COMBAT] < 100) {
				player.sendMessage("You need at least 100 Warrior Guild Combat tokens to enter.");
				return false;
			}
			for (int i = 0; i < 2; i++) {
			    WorldObject object1 = new WorldObject(i == 1 ? 66599 : 66601, 0, 2, 2846, 3535, 2);
			    player.setNextWorldTile(new WorldTile(player.getX() == object1.getX() ? object1.getX() + 1
					    : object1.getX(), object1.getY(), player.getPlane()));
			    player.closeInterfaces();
			    cyclopseOption = COMBAT;
			    inCyclopse = true;
			}
		}
	    return false;
	} else if (object.getId() == 56887) {
	    player.getDialogueManager()
		    .startDialogue(
			    "SimpleMessage",
			    "Kamfreena reports that "
				    + killedCyclopses
				    + " cyclopes have been slain in the guild today. She hopes that warriors will step up and kill more!");
	    return false;
	}
	return true;
    }

    @Override
    public boolean handleItemOnObject(final WorldObject object, Item item) {
	if (object.getId() == 15621) {
	    if (player.getTemporaryAttributtes().get("animator_spawned") != null) {
		player.getPackets().sendGameMessage(
			"You are already in combat with an animation.");
		return false;
	    }
	    int realIndex = getIndex(item.getId());
	    if (realIndex == -1)
		return false;
	    for (int armor : ARMOUR_SETS[realIndex])
		player.getInventory().deleteItem(armor, 1);
	    player.setNextAnimation(new Animation(827));
	    player.lock();
	    final int finalIndex = realIndex;
	    WorldTasksManager.schedule(new WorldTask() {
		int ticks;

		@Override
		public void run() {
		    ticks++;
		    if (ticks == 0)
			player.faceObject(object);
		    else if (ticks == 1)
			player.getDialogueManager()
				.startDialogue("SimpleMessage",
					"The animator hums, something appears to be working.");
		    else if (ticks == 2) {
			player.getDialogueManager().startDialogue(
				"SimpleMessage", "You get pushed away..");
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 3, player.getPlane()));
		    } else if (ticks == 3) {
			player.faceObject(object);
			player.getDialogueManager().finishDialogue();
			AnimatedArmor npc = new AnimatedArmor(player,
					4278 + finalIndex, object, -1, true);
				npc.setRun(false);
				npc.setIntelligentRouteFinder(true);
				npc.setNextForceTalk(new ForceTalk("IM ALIVE!"));
				npc.setNextAnimation(new Animation(4166));
				npc.getCombat().setTarget(player);
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 2, player.getPlane()));
				player.getHintIconsManager().addHintIcon(npc, 1, -1, false);
				player.getTemporaryAttributtes().put("animator_spawned", true);
				player.unlock();
				stop();
		    }
		}
	    }, 1, 1);
	    return false;
	}
	return true;
    }

    private int getIndex(int checkedId) {
	for (int i = 0; i < ARMOUR_SETS.length; i++) {
	    for (int j = 0; j < ARMOUR_SETS[i].length; j++) {
		if (ARMOUR_SETS[i][j] == checkedId) {
		    for (int k = 0; k < 3; k++) {
			if (!player.getInventory().containsItem(
				ARMOUR_SETS[i][k], 1)) {
			    player.getPackets().sendGameMessage(
				    "You need a full set of " + ARMOUR_TYPE[i]
					    + " to use the animator.");
			    return -1;
			}
		    }
		    return i;
		}
	    }
	}
	return -1;
    }

    /**
     * Attacking a dummy during the "Dummy Activity".
     * 
     * @param object
     *            The object we are striking.
     */
    private void submitDummyHit(final WorldObject object) {
	player.setNextAnimation(new Animation(PlayerCombat
		.getWeaponAttackEmote(player.getEquipment().getWeaponId(),
			player.getCombatDefinitions().getAttackStyle(), player)));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		if (isProperHit(object)) {
		    player.lock(2);
		    player.getSkills().addXp(Skills.ATTACK, 15);
		    player.setWarriorPoints(ATTACK, 5);
		    player.getPackets().sendGameMessage(
			    "You whack the dummy sucessfully!");
		    lastDummy = timer.lastDummy;
		} else {
		    player.lock(5);
		    player.applyHit(new Hit(player, 10, HitLook.REGULAR_DAMAGE));
		    player.setNextAnimation(new Animation(424));
		    player.setNextGraphics(new Graphics(80, 5, 60));
		    player.getPackets()
			    .sendGameMessage(
				    "You whack the dummy whistle using the wrong attack style.");
		}
	    }
	});
    }

    private boolean isProperHit(WorldObject object) {
	int attackStyle = player.getCombatDefinitions().getAttackStyle();
	int xpStyle = CombatDefinitions.getXpStyle(player.getEquipment()
		.getWeaponId(), attackStyle);
	if (object.getId() == 15624)
	    return xpStyle == Skills.ATTACK;
	else if (object.getId() == 15625)
	    return attackStyle == CombatDefinitions.SLASH_ATTACK;
	else if (object.getId() == 15626)
	    return xpStyle == Skills.STRENGTH;
	else if (object.getId() == 15627)
	    return xpStyle == CombatDefinitions.SHARED;
	else if (object.getId() == 15628)
	    return attackStyle == CombatDefinitions.CRUSH_ATTACK;
	else if (object.getId() == 15629)
	    return attackStyle == CombatDefinitions.STAB_ATTACK;
	else if (object.getId() == 15630)
	    return xpStyle == Skills.DEFENCE;
	return false;
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId,
	    int slotId, int packetId) {
	if (interfaceId == 411) {
	    if (componentId == 13)
		defensiveStyle = 0;
	    else if (componentId == 22)
		defensiveStyle = 1;
	    else if (componentId == 31)
		defensiveStyle = 2;
	    else if (componentId == 40)
		defensiveStyle = 3;
	} else if (interfaceId == 1058) {
	    if (componentId == 22) {
		cyclopseOption = BARRLES;
		player.getPackets().sendConfigByFile(8668, 5);
	    } else if (componentId == 23) {
		cyclopseOption = STRENGTH;
		player.getPackets().sendConfigByFile(8668, 3);
	    } else if (componentId == 24) {
		cyclopseOption = COMBAT;
		player.getPackets().sendConfigByFile(8668, 4);
	    } else if (componentId == 26) {
		cyclopseOption = DEFENCE;
		player.getPackets().sendConfigByFile(8668, 2);
	    } else if (componentId == 25) {
		cyclopseOption = ATTACK;
		player.getPackets().sendConfigByFile(8668, 1);
	    } else if (componentId == 3) {
		cyclopseOption = ALL;
	    } else if (componentId == 22) {
		player.getPackets().sendConfigByFile(8668, 0);
		cyclopseOption = -1;
	    }
	} else if (interfaceId == 387 && kegCount >= 1) {
	    if (componentId == 6) {
		player.getPackets().sendGameMessage(
			"You can't remove the kegs off your head.");
		return false;
	    }
	} else if (interfaceId == 750 && kegCount >= 1) {
	    if (componentId == 4) {
		player.getPackets()
			.sendGameMessage(
				"You cannot do this action while balancing the kegs on your head.");
		return false;
	    }
	} else if (interfaceId == 271 || interfaceId == 749 && componentId == 4) {
	    if (player.getPrayer().isAncientCurses()) {
		player.getPackets()
			.sendGameMessage(
				"Harllaak frowns upon using curses in the Warrior's Guild.");
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean canEquip(int slot, int itemId) {
	if (itemId == 8856) {
	    if (!inCatapultArea(player))
		return false;
	    else
		sendShieldInterfaces();
	} else if (slot == Equipment.SLOT_HAT && kegCount >= 1)
	    return false;
	return true;
    }

    /**
     * Close all the unnecessary interfaces for the "Catapult Activity".
     */
    private void sendShieldInterfaces() {
	player.getInterfaceManager().sendInterface(411);
	player.getInterfaceManager().closeCombatStyles();
	player.getInterfaceManager().closeSkills();
	player.getInterfaceManager().closeInventory();
	player.getInterfaceManager().closePrayerBook();
	player.getInterfaceManager().closeMagicBook();
	player.getInterfaceManager().closeEmotes();
	player.getInterfaceManager().openGameTab(5);
    }

    /**
     * A check to see if the player is within the "Catapult Activity"
     * boundaries.
     * 
     * @param player
     *            The player we are checking.
     * @return If the player is within the "Catapult Activity".
     */
    public static boolean inCatapultArea(Player player) {
	return player.withinArea(2837, 3538, 2847, 3552)
		&& player.getPlane() == 1;
    }

    private boolean hasEmptyHands() {
	return player.getEquipment().getGlovesId() == -1
		&& player.getEquipment().getWeaponId() == -1
		&& player.getEquipment().getShieldId() == -1;
    }

    @Override
    public boolean login() {
	start();
	return false;
    }

    @Override
    public boolean logout() {
	resetKegBalance();
	amountOfPlayers--;
	this.setArguments(new Object[] { this.inCyclopse, this.cyclopseOption });
	return false;
    }

    @Override
    public void magicTeleported(int teleType) {
	player.getControlerManager().forceStop();
    }

    @Override
    public void forceClose() {
    	resetKegBalance();
    	inCyclopse = false;
    	cyclopseOption = -1;
    	player.getInterfaceManager().closeOverlay(false);
    	amountOfPlayers--;
    }

    public void prepareShotput(final byte stage, final boolean is18LB) {
	player.lock(7);
	player.setNextFaceWorldTile(is18LB ? SHOTPUT_FACE_18LB
		: SHOTPUT_FACE_22LB);
	if (stage == 0 || stage == 2)
	    player.getPackets().sendGameMessage(
		    "You take a deep breath and prepare yourself.");
	else if (stage == 1)
	    player.getPackets().sendGameMessage(
		    "You take a step and throw the shot as hard as you can.");
	if ((player.getSkills().getLevel(Skills.STRENGTH) / 100) > Math
		.random()) {
	    player.getPackets().sendGameMessage(
		    "You fumble and drop the shot onto your toe. Ow!");
	    player.applyHit(new Hit(player, 10, HitLook.REGULAR_DAMAGE));
	    player.unlock();
	    return;
	}
	WorldTasksManager.schedule(new WorldTask() {

	    int ticks;

	    @Override
	    public void run() {
		ticks++;
		int distance = Utils.random(1,
			(player.getSkills().getLevel(Skills.STRENGTH) / 10)
				+ (is18LB ? 5 : 3));
		if (ticks == 3) {
		    WorldTile tile = new WorldTile(player.getX() + distance,
			    player.getY(), 1);
		    World.sendProjectile(player, tile, 690, 50, 0, 30, 16, 15,
			    0);
		} else if (ticks == ((distance / 2) + 4)) {
		    player.getSkills().addXp(Skills.STRENGTH, distance);
		    player.getTemporaryAttributtes().put("thrown_delay", true);
		} else if (ticks >= ((distance / 2) + 5)) {
		    int random = Utils.getRandom(3);
		    if (random == 0)
			player.getPackets()
				.sendGameMessage(
					"The shot is perfectly thrown and gently drops to the floor.");
		    else if (random == 1)
			player.getPackets().sendGameMessage(
				"The shot drops to the floor.");
		    else
			player.getPackets()
				.sendGameMessage(
					"The shot falls from the air like a brick, landing with a sickening thud.");
		    int base = random == 0 ? distance * 7
			    : random == 1 ? distance * 4 : distance;
		    player.setWarriorPoints(STRENGTH, base + Utils.random(2));
		    player.getTemporaryAttributtes().remove("thrown_delay");
		    stop();
		    return;
		}
	    }
	}, 0, 0);
    }

    private void balanceKeg(final WorldObject object) {
	player.lock(4);
	player.setNextAnimation(new Animation(4180));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		if (kegCount == 0)
		    player.getGlobalPlayerUpdater().setRenderEmote(2671);
		kegCount++;
		player.getPackets().sendConfigByFile(
			object.getDefinitions().configFileId, 1);
		player.getEquipment().getItems()
			.set(Equipment.SLOT_HAT, new Item(8859 + kegCount));
		player.getEquipment().refresh(Equipment.SLOT_HAT);
		player.getGlobalPlayerUpdater().generateAppearenceData();
	    }

	}, 2);
    }

    private void loseBalance() {
	player.setNextGraphics(new Graphics(689 - kegCount));
	player.lock(2);
	player.applyHit(new Hit(null, Utils.random(20, 40),
		HitLook.REGULAR_DAMAGE));
	player.getPackets().sendGameMessage(
		"You lose balance and the kegs fall onto your head.");
	player.setNextForceTalk(new ForceTalk("Ouch!"));
	if (kegCount != 1) {
	    player.getSkills().addXp(Skills.STRENGTH, 10 * kegCount);
	    player.setWarriorPoints(BARRLES, (10 * kegCount) + (kegTicks / 2));
	}
	resetKegBalance();
    }

    private void resetKegBalance() {
	if (kegCount >= 1) {
	    player.getEquipment().getItems().set(Equipment.SLOT_HAT, null);
	    player.getEquipment().refresh(Equipment.SLOT_HAT);
	    player.getGlobalPlayerUpdater().generateAppearenceData();
	    player.getGlobalPlayerUpdater().setRenderEmote(-1);
	}
	kegCount = 0;
	kegTicks = 0;
	for (int i = 0; i < 6; i++)
	    player.getPackets().sendConfigByFile(2252 + i, 0);
    }

    public static int getBestDefender(Player player) {
	for (int index = 0; index < DEFENDERS.length; index++) {
	    if (player.getEquipment().getShieldId() == DEFENDERS[index]
		    || player.getInventory().containsItem(DEFENDERS[index], 1))
		return DEFENDERS[index - 1 < 0 ? 0 : index - 1];
	}
	return DEFENDERS[7];
    }
}
