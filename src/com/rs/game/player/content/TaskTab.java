package com.rs.game.player.content;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.World;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;/*
									import com.rs.game.world.controllers.InstancedPVPControler;*/
import com.rs.utils.Colors;
import com.rs.utils.Utils;
import com.rs.utils.mysql.impl.VoteManager;

/**
 * @author Zeus
 * @date 15.01.2014
 */
public class TaskTab {

	/**
	 * Sends the actual Noticeboard tab.
	 * 
	 * @param player The player to send to.
	 */
	public static void sendTab(final Player player) {
		int rights = player.getRights();
		String pName = player.getUsername();
		String title = Colors.cyan + "Player";
		String gameMode = null;
		String status = null;
		String npcName = null;
		String slayerTask = null;

		// I've added check for if is not null, always check if it's not null
		// else RIP >.>
		if (player.getContract() != null)
			npcName = NPCDefinitions.getNPCDefinitions(player.getContract().getNpcId()).getName().toLowerCase();
		if (player.getSlayerManager().getCurrentTask() != null)
			slayerTask = player.getSlayerManager().getCurrentTask().getName();

		/**
		 * Defining player ranks
		 */
		if (player.isExpert())
			gameMode = Colors.cyan + "Expert (x" + Settings.EXPERT_XP + " XP)";
		if (player.isVeteran())
			gameMode = Colors.cyan + "Veteran (x" + Settings.VET_XP + " XP)";
		if (player.isIntermediate())
			gameMode = Colors.cyan + "Intermed. (x" + Settings.INTERM_XP + " XP)";
		if (player.isEasy())
			gameMode = Colors.cyan + "Easy (x" + Settings.EASY_XP + " XP)";
		if (player.isIronMan())
			gameMode = Colors.cyan + "Ironman (x" + Settings.IRONMAN_XP + " XP)";
		if (player.isHCIronMan())
			gameMode = Colors.cyan + "HC Ironman (x" + Settings.IRONMAN_XP + " XP)";
		if (player.isMod())
			title = "<col=B5B5B5>Moderator</col>";
		if (player.isForumManager())
			title = Colors.green + "Forum Manager";
		if (player.isCommunityManager())
			title = Colors.blue + "Community Manager";
		if (player.isBronze())
			status = "<col=8B4513>Bronze";
		if (player.isSilver())
			status = "<col=ffffff>Silver";
		if (player.isGold())
			status = "<col=e6e600>Gold";
		if (player.isDicer())
			status = "<col=e6e600>Dicer";
		if (player.isPlatinum())
			status = "<col=008000>Platinum";
		if (player.isDiamond())
			status = "<col=00FFFF>Diamond";
		if (player.isSponsor())
			status = "<shad=FFD700><col=FF8C00>Sponsor</shad>";
		if (player.isYoutube())
			status = "<shad=ff0000><col=000000>Youtube</shad>";
		if (player.isDev())
			status = "<shad=0000FF><col=00BFFF>Dev</shad>";
		if (player.isSupport())
			title = "<col=83A6F2>Support</col>";
		if (rights == 13)
			title = "<col=B5B5B5>Support</col>";
		if (player.isWiki())
			title = "<col=B5B5B5>Wiki</col>";
		if (rights == 1)
			title = "<col=B5B5B5>Moderator</col>";
		if (rights == 2)
			title = "<col=1589FF>Administrator</col>";

		if (pName.equalsIgnoreCase("Zeus"))
			title = "<col=D400FF>Owner</col>";

		// DAILY TASK
		String TaskType = player.getDailyTaskManager().getCurrentTask() == null ? ""
				: player.getDailyTaskManager().getCurrentTask().toString();

		// wellofGoodwill timer

		/**
		 * Start sending the tab
		 */
		player.getPackets().sendIComponentText(930, 10, Colors.white + Settings.SERVER_NAME + " Noticeboard</col>");

		String text = Colors.red + "-- Server --<br>"

				+ Colors.white + "- Server time: " + Colors.green + time("hh:mm:ss a") + "<br>" + Colors.white
				+ "- Players online: " + Colors.green + World.getPlayersOnline() + "<br>" + Colors.white
				+ "- Double XP: " + Colors.green
				+ (World.isWeekend() ? "Active (weeknd)"
						: (World.isWellActive() ? "Active (well)" : Colors.red + "Not Active"))
				+ "<br>" + Colors.white + "- Vote party total: " + Colors.green + VoteManager.VOTES + "<br>";
		if (VoteManager.gotDXP()) {
			text += Colors.white + "- Party XP " + ((World.isWellActive() || World.isWeekend()) ? "+10%" : "+100%")
					+ ": " + Utils.formatTime(VoteManager.PARTY_DXP - Utils.currentTimeMillis()).replace("00:", "")
					+ "<br>";
		}
		text += (World.getLastVoter() != null
				? "<br>" + Colors.white + " -- Last voter: " + Colors.green + World.getLastVoter() + "<br>"
				: "") + "<br>" + Colors.red + "-- Player --<br>" + Colors.white + "- Name: " + Colors.green
				+ Utils.formatString(player.getDisplayName()) + "<br>" + Colors.white + "- Rank: " + title
				+ (gameMode != null ? "<br>" + Colors.white + "- Mode: " + gameMode : "")
				+ (player.isBronze() || player.isYoutube() ? "<br>" + Colors.white + "- Status: " + status : "")
				+ "<br>" + Colors.white + "- Time played: " + Colors.green + Utils.getTimePlayed(player.getTimePlayed())
				+ "<br>" + Colors.white + "- Store Credits: " + Colors.green + player.getReferralPoints()
				+ (player.getPlayerVars().isDoubleXp() ? "<br>" + Colors.white + "- Exp Boost: " + Colors.green
						+ Utils.formatTime(player.getPlayerVars().getDoubleXpTimer()) : "")
				+
				"<br><br>" + Colors.orange + " -- Divination --<br>" + Colors.white + " - Level: " + Colors.green
				+ player.getSkills().getLevelForXp(Skills.DIVINATION) + "<br>" + Colors.white + " - Exp: "
				+ Colors.green + Utils.getFormattedNumber((int) player.getSkills().getXp(Skills.DIVINATION))
				// + "<br><br>" + Colors.orange + " -- Invention --<br>" + Colors.white + " -
				// Level: " + Colors.green
				// + player.getSkills().getLevelForXp(Skills.INVENTION) + "<br>" + Colors.white
				// + " - Exp: "
				// + Colors.green + Utils.getFormattedNumber((int)
				// player.getSkills().getXp(Skills.INVENTION))
				+ "<br><br>" + Colors.white + Colors.red + "-- Statistics --<br>" + Colors.white + "- Donated: "
				+ Colors.green + "$" + Utils.getFormattedNumber(player.getMoneySpent()) + "<br>" + Colors.white
				+ "- Helwyr Coins: " + Colors.green + "" + Utils.getFormattedNumber(player.getDonationManager().getCorruptCoins()) + "<br>"
				+ Colors.white + "- Quest Points: " + Colors.green + player.getQuestPoints() + "<br>" + Colors.white
				+ "- PvM Points: " + Colors.green + Utils.formatNumber(player.getPVMPoints()) + "<br>" + Colors.white
				+ "- Prestige Points: " + Colors.green + Utils.formatNumber(player.prestigePoints) + "<br>"
				+ Colors.white + "- Vote points: " + Colors.green + Utils.getFormattedNumber(player.getVotePoints())
				+ "<br>" + Colors.red + "- Halloween points: " + Colors.green
				+ Utils.getFormattedNumber(player.getHweenPoints()) + "<br>" + Colors.white + "- Quest points: "
				+ Colors.green + Utils.getFormattedNumber(player.getQuestPoints()) + "<br>" + Colors.white
				+ "- Loyalty points: " + Colors.green + Utils.getFormattedNumber(player.getLoyaltyPoints()) + "<br>"
				+ Colors.white + "- Trivia points: " + Colors.green + Utils.getFormattedNumber(player.getTriviaPoints())
				+ "<br>" + Colors.white + "- PC points: " + Colors.green
				+ Utils.getFormattedNumber(player.getPestPoints()) + "<br>" + Colors.white + "- SW zeals: "
				+ Colors.green + Utils.getFormattedNumber(player.getZeals()) + "<br>" + Colors.white + "- Dung tokens: "
				+ Colors.green + Utils.getFormattedNumber(player.getDungeoneeringTokens()) + "<br>" + Colors.white
				+ "- DT Kills: " + Colors.green
				+ Utils.getFormattedNumber(player.getDominionTower().getKilledBossesCount()) + "<br>" + Colors.white
				+ "- Tusken points: " + Colors.green + Utils.getFormattedNumber(player.getTuskenPoints()) + "<br>"+ Colors.white 
				+ "- Elite Dungeon point: " + Colors.green+ Utils.getFormattedNumber(player.getElitePoints()) + "<br>" + Colors.white
				+ "- Starfire points: "	+ Colors.green + Utils.getFormattedNumber(player.getStarfirePoints());

		if (player.getXpSharing() != null)
			if (player.getXpSharing().getReceiverName() != null)
				if (!player.getXpSharing().getReceiverName().equals(""))
					text += Colors.white + "- XP Share: " + player.getXpSharing().getReceiverName() + "<br>";

		text += "<br><br>" + Colors.red + "-- PvP Information --" + "<br>" + Colors.white + "- Pk Points: "
				+ Colors.green + Utils.getFormattedNumber(player.getPkPoints()) + "<br>" + Colors.white
				+ "- Killstreak Points: " + Colors.green + Utils.getFormattedNumber(player.getTotalKillStreakPoints())
				+ "<br>" + Colors.white + "- Kills: " + Colors.green + Utils.getFormattedNumber(player.getKillCount())
				+ "<br>" + Colors.white + "- Deaths: " + Colors.green + Utils.getFormattedNumber(player.getDeathCount())
				+ "<br>" + Colors.white + "- Killstreak: " + Colors.green
				+ Utils.getFormattedNumber(player.getKillStreak())

				+ "<br><br>" + Colors.red + "-- Slayer Information --"
				+ (player.getSlayerManager().getCurrentTask() != null ? "<br>" + Colors.white + "- Task: "
						+ Colors.green + slayerTask + "" + "<br>" + Colors.white + "- Kills Left: " + Colors.green
						+ Utils.getFormattedNumber(player.getSlayerManager().getCount()) : "")
				+ "<br>" + Colors.white + "- Slayer Points: " + Colors.green
				+ Utils.getFormattedNumber(player.getSlayerManager().getSlayerPoints())

				+ "<br>" + Colors.white + "- Special Slayer Points: " + Colors.green
				+ Utils.getFormattedNumber(player.getSlayerManager().getSlayerPoints2())
				// XXX I've added check for if is not null, always check if it's
				// not null else RIP >.>
				+ (player.getContract() != null ? !player.getContract().hasCompleted() ? "<br>" + Colors.white
						+ "- Contract: <br>" + Colors.green + "" + npcName + "' <br>" + Colors.white + "- Kills Left: "
						+ Colors.green + Utils.getFormattedNumber(player.getContract().getKillAmount()) + "<br>"
						+ Colors.white + "- Reward Amount: " + Colors.green
						+ Utils.getFormattedNumber(player.getContract().getRewardAmount()) : "" : "")
				+ "<br>" + Colors.white + "- Reaper Points: " + Colors.green
				+ Utils.getFormattedNumber(player.getReaperPoints()) + "<br>" + Colors.white + "- Total kills: "
				+ Colors.green + Utils.getFormattedNumber(player.getTotalKills()) + "<br>" + Colors.white
				+ "- Completed Contracts: " + Colors.green + Utils.getFormattedNumber(player.getTotalContract())

				+ "<br><br>" + Colors.red + "-- Daily Task --" + "<br>" + Colors.white + "- Task Type: " + Colors.green
				+ TaskType + "<br>" + Colors.white + "- Task: " + Colors.green
				+ ItemDefinitions.getItemDefinitions(player.getDailyTaskManager().getProductId()).getName() + "<br>"
				+ Colors.white + "- Task Count: " + Colors.green + player.getDailyTaskManager().getAmountLeft()

				+ "<br><br>" + Colors.red + "-- Vorago --" + "<br>" + Colors.white + "- rotation: " + Colors.green
				+ Settings.VORAGO_ROTATION_NAMES[Settings.VORAGO_ROTATION] + "<br><br>"

				+ Colors.red + "-- Player Owned Ports --" + "<br>" + Colors.white + "- Ship 'Alpha' : "
				+ (!player.getPorts().hasFirstShip ? Colors.red + "Locked."
						: (!player.getPorts().hasFirstShipReturned()
								? Colors.red + "Minutes Left: " + player.getPorts().getFirstVoyageTimeLeft() + "</col>."
								: (!player.getPorts().firstShipReward ? Colors.green + "Ready to Claim</col>."
										: Colors.green + "Ready to Deploy.")))
				+ "<br>" + Colors.white + "- Ship 'Beta' : "
				+ (!player.getPorts().hasSecondShip ? Colors.red + "Locked."
						: (!player.getPorts().hasSecondShipReturned()
								? Colors.red + "Minutes Left: " + player.getPorts().getSecondVoyageTimeLeft()
										+ "</col>."
								: (!player.getPorts().secondShipReward ? Colors.green + "Ready to Claim</col>."
										: Colors.green + "Ready to Deploy.")))
				+ "<br>" + Colors.white + "- Ship 'Gamma' : "
				+ (!player.getPorts().hasThirdShip ? Colors.red + "Locked."
						: (!player.getPorts().hasThirdShipReturned()
								? Colors.red + "Minutes Left: " + player.getPorts().getThirdVoyageTimeLeft() + "</col>."
								: (!player.getPorts().thirdShipReward ? Colors.green + "Ready to Claim</col>."
										: Colors.green + "Ready to Deploy.")))
				+ "<br>" + Colors.white + "- Ship 'Delta' : "
				+ (!player.getPorts().hasFourthShip ? Colors.red + "Locked."
						: (!player.getPorts().hasFourthShipReturned()
								? Colors.red + "Minutes Left: " + player.getPorts().getFourthVoyageTimeLeft()
										+ "</col>."
								: (!player.getPorts().fourthShipReward ? Colors.green + "Ready to Claim</col>."
										: Colors.green + "Ready to Deploy.")))
				+ "<br>" + Colors.white + "- Ship 'Epsilon' : "
				+ (!player.getPorts().hasFifthShip ? Colors.red + "Locked."
						: (!player.getPorts().hasFifthShipReturned()
								? Colors.red + "Minutes Left: " + player.getPorts().getFifthVoyageTimeLeft() + "</col>."
								: (!player.getPorts().fifthShipReward ? Colors.green + "Ready to Claim</col>."
										: Colors.green + "Ready to Deploy.")));
		;

		player.getPackets().sendIComponentText(930, 16, text);
	}

	/**
	 * Gets the current systems time.
	 * 
	 * @param dateFormat The format to use.
	 * @return The formatted time.
	 */
	public static String time(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
}