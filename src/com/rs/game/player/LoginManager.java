package com.rs.game.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

//import com.rs.DiscordMessageHandler;
import com.rs.Settings;
import com.rs.WorldSettings;
import com.rs.game.world.World;
import com.rs.game.activites.BountyHunter;
import com.rs.game.activites.goldrush.GRManager;
import com.rs.game.activites.resourcegather.ResourceGatherBuff;
import com.rs.game.player.achievements.AchievementManager;
import com.rs.game.player.actions.ActionManager;
import com.rs.game.player.content.*;
//import com.rs.game.player.content.HourlyBoxManager;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.construction.House;
import com.rs.game.player.content.corrupt.cosmetics.CosmeticsManager;
import com.rs.game.player.content.corrupt.modes.prestige.Prestige;
import com.rs.game.player.content.corrupt.perks.PlayerPerks;
import com.rs.game.player.content.corrupt.tutorial.BlockTutorial;
import com.rs.game.player.content.corrupt.modes.GameModeManager;
import com.rs.game.player.content.death.DeathManager;
import com.rs.game.player.content.death.Gravestone;
import com.rs.game.player.content.grandExchange.GrandExchangeManager;
import com.rs.game.player.content.items.PrayerBooks;
import com.rs.game.player.content.miscellania.ThroneOfMiscellania;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.content.slayer.CooperativeSlayer;
import com.rs.game.player.content.corrupt.animations.teleports.TeleportManager;
import com.rs.game.player.content.xmas.XmasEvent;
import com.rs.game.player.cutscenes.CutscenesManager;
import com.rs.game.player.dialogue.DialogueManager;
import com.rs.game.player.newquests.NewQuestManager;
import com.rs.network.Session;
import com.rs.utils.Colors;
import com.rs.utils.DonationRank;
import com.rs.utils.IsaacKeyPair;
import com.rs.utils.Logger;
import com.rs.utils.LoggingSystem;
import com.rs.utils.Utils;

/**
 * Handles player login.
 * 
 * @author Zeus
 */
public class LoginManager {

	/**
	 * Inits the login; from LoginPacketsDecoder.
	 * 
	 * @param player
	 *            The player to login.
	 * @param session
	 *            The current Session data.
	 * @param username
	 *            The players username.
	 * @param mac
	 *            The players MACAddress.
	 * @param displayMode
	 *            The players display mode.
	 * @param isaacKeyPair
	 *            The players unique isaac key.
	 */
	public static void init(Player player, Session session, String username, String mac, int displayMode,
			IsaacKeyPair isaacKeyPair) {
		player.session = session;
		player.username = username;
		player.displayMode = displayMode;
		player.setCurrentMac(mac);
		player.isaacKeyPair = isaacKeyPair;
		if (player.FastestTime == null) {
			player.FastestTime = new long[40];
		}
		
		if (player.getBossTimerManager() == null)
			player.setBossTimerManager(new BossTimerManager(player));
		if (player.getNotes() == null)
			player.notesL = new Notes();
		if (player.getDeathManager() == null)
			player.setDeathManager(new DeathManager());
		player.getDeathManager().setPlayer(player);
		if (player.geManager == null)
			player.geManager = new GrandExchangeManager();
		if (player.ArtisansWorkShopSupplies == null)
			player.ArtisansWorkShopSupplies = new int[5];
		if (player.dungManager == null)
			player.dungManager = new DungManager();
		if (player.petLootManager == null)
			player.petLootManager = new PetLootManager();
		if (player.slayerManager == null) {
			player.slayerManager = new SlayerManager();
		}
		if (player.getAchManager() == null) {
			player.setAchManager(new AchievementManager(player));
		}
		if (player.getGrManager() == null) {
			player.setGrManager(new GRManager(player));
		}
		if(player.getFutures() == null){
			player.setFutures(new PlayerFutures(player));
		}
		if (player.getResourceGather() == null)
			player.setResourceGather(new ResourceGatherBuff(player));
		if (player.squealOfFortune == null)
			player.squealOfFortune = new SquealOfFortune();
		if (player.elderTreeManager == null)
			player.elderTreeManager = new ElderTreeManager();
		if (player.gearPresets == null)
			player.gearPresets = new GearPresets();
		if (player.newQuestManager == null)
			player.newQuestManager = new NewQuestManager();
		if (player.dailyTaskManager == null)
			player.dailyTaskManager = new DailyTaskManager();
		if (player.dayOfWeekManager == null)
			player.dayOfWeekManager = new DayOfWeekManager();
		if (player.mauledWeeksNM == null)
			player.mauledWeeksNM = new boolean[6];
		if (player.herbicideSettings == null)
			player.herbicideSettings = new boolean[17];
		if (player.bonecrusherSettings == null)
			player.bonecrusherSettings = new boolean[16];
		if (player.mauledWeeksHM == null)
			player.mauledWeeksHM = new boolean[6];
		if (player.prayerBook == null)
			player.prayerBook = new boolean[PrayerBooks.BOOKS.length];
		if (player.boons == null)
			player.boons = new boolean[12];
		if (player.overrides == null)
			player.overrides = new CosmeticOverrides();
		if (player.unlockedCostumesIds == null)
			player.unlockedCostumesIds = new ArrayList<>();
		if (player.animations == null)
			player.animations = new AnimationOverrides();
		if (player.ports == null)
			player.ports = new PlayerOwnedPort();
		if (player.xmas == null)
			player.xmas = new XmasEvent();
		if (player.banksManager == null)
			player.banksManager = new BanksManager();
		if (player.throne == null)
			player.throne = new ThroneOfMiscellania();
		if(player.playerVars == null)
			player.playerVars = new PlayerVariables();
		if(player.gameModeManager == null)
			player.gameModeManager = new GameModeManager();
		if(player.donationManager == null)
			player.donationManager = new DonationManager();
		if(player.adventurerLogs == null)
			player.adventurerLogs = new AdventurerLogs();
		if(player.teleportManager == null)
			player.teleportManager = new TeleportManager();
		if(player.playerStash == null)
			player.playerStash = new Stash();
		if(player.raidsManager == null)
			player.raidsManager = new RaidsManager();
		if(player.dailyLogin == null)
			player.dailyLogin = new DailyLoginManager();
		if(player.getBlockTutorial() == null)
			player.setBlockTutorial(new BlockTutorial());
		player.interfaceManager = new InterfaceManager(player);
		player.dialogueManager = new DialogueManager(player);
		player.hintIconsManager = new HintIconsManager(player);
		player.priceCheckManager = new PriceCheckManager(player);
		player.varsManager = new VarsManager(player);
		player.localPlayerUpdate = new LocalPlayerUpdate(player);
		player.localNPCUpdate = new LocalNPCUpdate(player);
		player.actionManager = new ActionManager(player);
		player.cutscenesManager = new CutscenesManager(player);
		player.pouch = new MoneyPouch(player);
		if (player.skills == null)
			player.skills = new Skills();
		if (player.petManager == null)
			player.petManager = new PetManager();
		if (player.auraManager == null)
			player.auraManager = new AuraManager();
		if (player.VBM == null)
			player.VBM = new VarBitManager(player);
		if (player.house == null)
			player.house = new House();
		player.coOpSlayer = new CooperativeSlayer();
		if (player.dominionTower == null)
			player.dominionTower = new DominionTower();
		player.trade = new Trade(player);
		player.loyaltyManager = new LoyaltyManager(player);
		// player.HourlyBoxManager = new HourlyBoxManager(player);
		if (player.getGlobalPlayerUpdater() == null)
			player.setGlobalPlayerUpdater(new GlobalPlayerUpdater());
		if (player.farmingManager == null)
			player.farmingManager = new FarmingManager();
		if (player.getToolBelt() == null)
			player.toolBelt = new Toolbelt(player);
		if (player.getToolBeltNew() == null)
			player.toolBeltNew = new ToolbeltNew(player);
		if (player.getTitles() == null)
			player.titles = new Titles();
		if (player.perkManager == null)
			player.perkManager = new PerkManager();
		if (player.treasureTrails == null)
			player.treasureTrails = new TreasureTrails();
		if (player.bountyHunter == null)
			player.bountyHunter = new BountyHunter();
		if(player.perks == null)
			player.perks = new PlayerPerks();
		if(player.cosmetics == null)
			player.cosmetics = new CosmeticsManager();
		if(player.prestige == null)
			player.prestige = new Prestige();
		if (player.getHome() == null || player.getHomeName() == null)
			player.setHome(Settings.RESPAWN_PLAYER_LOCATION, "Gold Mine");
		player.getGlobalPlayerUpdater().setPlayer(player);
		player.getVarBitManager().setPlayer(player);
		player.getInventory().setPlayer(player);
		player.getEquipment().setPlayer(player);
		player.getFarmingManager().setPlayer(player);
		player.getToolBelt().setPlayer(player);
		player.getToolBeltNew().setPlayer(player);
		player.getDungManager().setPlayer(player);
		player.getHouse().setPlayer(player);
		player.getGearPresets().setPlayer(player);
		player.getBanksManager().setPlayer(player);
		player.getSquealOfFortune().setPlayer(player);
		player.getDailyTaskManager().setPlayer(player);
		player.getDayOfWeekManager().setPlayer(player);
		player.getSkills().setPlayer(player);
		player.getCombatDefinitions().setPlayer(player);
		player.getSlayerManager().setPlayer(player);
		player.getPrayer().setPlayer(player);
		player.getPrestige().setPlayer(player);
		player.getPerks().setPlayer(player);
		player.getPetLootManager().setPlayer(player);
		player.getCosmetics().setPlayer(player);
		player.getElderTreeManager().setPlayer(player);
		player.getBank().setPlayer(player);
		player.getControlerManager().setPlayer(player);
		player.getTitles().setPlayer(player);
		player.getNewQuestManager().setPlayer(player);
		player.getOverrides().setPlayer(player);
		player.getAnimations().setPlayer(player);
		player.getGEManager().setPlayer(player);
		player.getMusicsManager().setPlayer(player);
		player.getEmotesManager().setPlayer(player);
		player.getPorts().setPlayer(player);
		player.getXmas().setPlayer(player);
		player.getFriendsIgnores().setPlayer(player);
		player.getDominionTower().setPlayer(player);
		player.getAuraManager().setPlayer(player);
		player.getTreasureTrails().setPlayer(player);
		player.getPerkManager().setPlayer(player);
		player.getNotes().setPlayer(player);
		player.getCharges().setPlayer(player);
		player.getQuestManager().setPlayer(player);
		player.getPetManager().setPlayer(player);
		player.getBountyHunter().setPlayer(player);
		player.getThrone().setPlayer(player);
		player.getPlayerVars().setPlayer(player);
		player.getGameModeManager().setPlayer(player);
		player.getDayOfWeekManager().setPlayer(player);
		player.getAdventurerLogs().setPlayer(player);
		player.getTeleports().setPlayer(player);
		player.getPlayerStash().setPlayer(player);
		player.getRaidsManager().setPlayer(player);
		player.getDailyLogin().setPlayer(player);
		player.getBlockTutorial().setPlayer(player);
		player.setDirection(Utils.getFaceDirection(0, -1));
		player.fairyRingCombination = new int[3];
		player.warriorCheck();
		player.setTemporaryMoveType(-1);
		player.logicPackets = new ConcurrentLinkedQueue<>();
		player.setSwitchItemCache(Collections.synchronizedList(new ArrayList<>()));
		player.initEntity();
		World.addPlayer(player);
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		World.updateEntityRegion(player);
		player.increaseAFKTimer();
		//player.setFilterLocked(true);
	}

	private static void sendGameData(Player player) {
		LoggingSystem.logIP(player);
		LoggingSystem.logAddress(player);
		player.farmingManager.init();
		// friend chat connect
		player.dayOfWeekManager.init();
		player.getThrone().init();
		player.sendSawMillConfig();
		player.sendLoginMessages();
		if (player.getCurrentFriendChatOwner() != null) {
			FriendChatsManager.joinChat(player.getCurrentFriendChatOwner(), player);
			if (player.getCurrentFriendChat() == null)
				player.setCurrentFriendChatOwner(null);
		}

		// connect to current clan
		if (player.getClanName() != null) {
			if (!ClansManager.connectToClan(player, player.getClanName(), false))
				player.setClanName(null);
		}

		if (player.prestigedSkills == null)
			player.prestigedSkills = new int[Skills.SKILL_NAME.length];

		// respawn familiar
		if (player.getFamiliar() != null)
			player.getFamiliar().respawnFamiliar(player);
		else
			player.getPetManager().init();
		if (player.getSkills().getSpinMap() == null)
			player.getSkills().setSpinMap(new HashMap<String, Boolean>());
		if (player.getXpSharing() == null)
			player.setXpSharing(new XPSharing(player));

		// Vecna timer
		player.vecnaTimer(player.getVecnaTimer());

		if (player.isOwner())
			player.setRights(2);

		/**
		 * Inits G.E. for the player.
		 */
		player.getGEManager().init();

		player.getNewQuestManager().initialize();

		/**
		 * Refreshes the TOP Donation ranks.
		 */
		if (player.getMoneySpent() >= 1)
			DonationRank.checkRank(player);

		/**
		 * Refreshes the Toolbelt.
		 */
		player.getToolBelt().refresh();

		/**
		 * Refreshes the NEW Toolbelt.
		 */
		player.getToolBeltNew().refresh();

		/**
		 * Refreshes the Notes.
		 */
		player.getNotes().refresh();

		/**
		 * Player-owned house.
		 */
		player.house.init();
		if (!player.hasHouse)
			player.hasHouse = true;

		/**
		 * Loyalty Manager.
		 */
		player.getLoyaltyManager().startTimer();

		/**
		 * Hourly Box Manager.
		 */
		// player.getHourlyBoxManager().startTimer();

		/**
		 * Play time.
		 */
		player.setRecordedPlayTime(Utils.currentTimeMillis());

		/**
		 * Reset thieving delay.
		 */
		player.setThievingDelay(0);

	}

	public static void sendLogin(final Player player) {
		if (World.exiting_start != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - World.exiting_start) / 1000);
			player.getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
		}
		
		player.sendMessage("Welcome to " + Settings.SERVER_NAME + ".");
		LividFarm.sendAreaConfigs(player);
		player.currentTimeOnline = Utils.currentTimeMillis();
		if (!player.resetedTimePlayedWeekly) {
			player.timePlayedWeekly = 0;
			player.resetedTimePlayedWeekly = true;
		}
		sendStaticConfigs(player);
		sendGameData(player);
		if (player.getPlayerVars().getQuadExp() > Utils.currentTimeMillis()) {
			long quadexpmin = TimeUnit.MILLISECONDS.toMinutes(player.getPlayerVars().getQuadExp() - Utils.currentTimeMillis());
			player.sm(Colors.orange + "[QuadExp] Active: " + quadexpmin + "min left.");
		}
		if (World.isWeekend())
			player.sendMessage("<img=7>Double experience weekend is currently: <shad=000000>" + Colors.green
					+ "Activated</col></shad>.");

		if (WorldSettings.isDungeonWeekend())
			player.sendMessage("<img=5>Double Dungeoneering weekend is currently: <shad=000000>" + Colors.green
					+ "Activated</col></shad>.");

		player.sendMessage(Colors.cyan + "<shad=000000><img=6>Latest updates</col></shad>: " + Settings.LATEST_UPDATE);
		player.checkPorts();

		if (Settings.DEBUG)
			player.sendMessage("Server is currently in development mode!");

		if (!player.getBlockTutorial().hasFinished())
			player.getBlockTutorial().begin();

		Gravestone.login(player);
		player.setRights(2);

		player.getDailyLogin().checkDaily();

		Logger.log("Player " + player.getUsername() + " has logged in, there are " + World.getPlayers().size()
				+ " players on.");

		ClansManager manager = player.getClanManager();
		if (!(manager == null) && player.getClanManager().getClan().getClanMessage() != null) {
			player.getPackets().sendGameMessage("[<col=" + player.getClanManager().getClan().getCmHex() + ">"
					+ player.getClanName() + " Message</col>]<col=" + player.getClanManager().getClan().getCmHex() + ">"
					+ player.getClanManager().getClan().getClanMessage());
		}

		player.getSkills().restoreNewSkills();

	}

	private static void sendStaticConfigs(Player player) {
		player.getPackets().sendConfig(281, 1000);
		player.getPackets().sendConfigByFile(6774, 1);
		player.getPrayer().refreshPrayerPoints();
		player.getPoison().refresh();
		player.getPackets().sendRunEnergy();
		player.getInterfaceManager().sendInterfaces();

		player.refreshAllowChatEffects();
		player.refreshMouseButtons();
		player.refreshReportOption();
		player.refreshPrivateChatSetup();
		player.refreshOtherChatsSetup();
		player.sendRunButtonConfig();
		player.sendDefaultPlayersOptions();
		player.checkMultiArea();
		player.refreshAcceptAid();
		player.refreshProfanityFilter();
		player.getInventory().init();
		player.getEquipment().init();
		player.getSkills().init();
		player.getCombatDefinitions().init();
		player.getPrayer().init();
		player.getFriendsIgnores().init();
		player.refreshHitPoints();
		player.getNotes().init();
		player.getEmotesManager().init();
		player.getPackets().sendGameBarStages();
		player.getQuestManager().init();
		player.getElderTreeManager().init();
		player.getMusicsManager().init();
		player.sendUnlockedObjectConfigs();
		// GPI
		player.setRunning(true);
		player.setUpdateMovementType(true);
		player.getGlobalPlayerUpdater().generateAppearenceData();
		player.getControlerManager().login();
		OwnedObjectManager.linkKeys(player);
	}
}