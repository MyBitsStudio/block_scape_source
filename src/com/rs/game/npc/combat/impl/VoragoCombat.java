package com.rs.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.map.bossInstance.impl.VoragoInstance;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.vorago.Vorago;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class VoragoCombat extends CombatScript {

	private Vorago vorago;

	public static int WHITE_BORDER = 0, RED_BORDER = 1;

	private int bonusAttacks = 0;
	private boolean skippedVitalis = false;

	@Override
	public Object[] getKeys() {
		return new Object[] { "Vorago" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		vorago = (Vorago) npc;
		boolean hardMode = vorago.getVoragoInstance().getSettings().isHardMode();
		if (Utils.colides(vorago, target) && (!hardMode && vorago.getPhase() == 5)
				&& (!hardMode && vorago.getPhase() >= 1)) {
			if (Utils.random(4) == 0 && vorago.getTemporaryAttributtes().get("CantBeAttacked") == null)
				sendBindAttack();
			vorago.calcFollow(target, 2, true, npc.isIntelligentRouteFinder());
			return 0;
		}
		if (vorago.getTemporaryAttributtes().get("BringHimDownClick") != null)
			return 0;
		else if (vorago.getTemporaryAttributtes().get("VoragoType") != null
				&& ((int) vorago.getTemporaryAttributtes().get("VoragoType") == 1)
				&& (!hardMode && vorago.getPhase() != 5)) {
			if (Utils.getDistance(vorago, target) > 14) {
				vorago.setCantFollowUnderCombat(false);
			} else {
				vorago.setCantFollowUnderCombat(true);
			}
		}
		if (bonusAttacks > 0) {
			bonusAttacks--;
			sendBlueBombAttack();
			return 7;
		}
		if (vorago.getTemporaryAttributtes().get("CantBeAttackedOnPhaseStart") != null)
			vorago.getTemporaryAttributtes().remove("CantBeAttackedOnPhaseStart");
		switch (vorago.getPhase()) {
		case 1:// PHASE ONE
			if (vorago.getAttackProgress() > 4) // 5 attacks
				vorago.setAttackProgress(0);
			switch (vorago.getAttackProgress()) {
			case 0:
				sendRedBombAttack();
				break;
			default:
				int attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			}
			vorago.setAttackProgress(vorago.getAttackProgress() + 1);
			break;
		case 2:// PHASE 2
			if (vorago.getAttackProgress() > 16) // 17 attacks
				vorago.setAttackProgress(0);
			switch (vorago.getAttackProgress()) {
			case 0:// 5 smashes
			case 1:
				sendSmash();
				break;
			case 2:
				sendBlueBombAttack(false);
			case 3:
			case 4:
				sendSmash();
				break;
			case 5:// 3 attacks
			case 6:
			case 7:
				int attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			case 8:
				sendReflectAttack();
				sendGravityField();
				break;
			case 9:// 3attacks then case 12 red bomb
			case 10:
			case 11:
			case 13:// 4attacks
			case 14:
			case 15:
			case 16:
				attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			case 12:// red Bomb
				sendRedBombAttack();
				break;
			}
			vorago.setAttackProgress(vorago.getAttackProgress() + 1);
			break;
		case 3:// PHASE 3
			if (hardMode) {// ceiling collapses
				if (vorago.getAttackProgress() > 16) // 17 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// 2 Specials
				case 8:
					if (!sendCeilingCollapse()) {
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					}
					break;
				case 1:// 3 attacks
				case 3:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 9:// 3 attacks
				case 11:
				case 13:// 4 attacks
				case 14:
				case 15:
				case 16:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else
							sendBlueBombAttack();
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						break;
					}
					break;
				case 10:
				case 2:
					sendBlueBombAttack(!vorago.canSendCeilingCollapse());
					break;
				case 4:// reflect
					sendReflectAttack();
					break;
				case 12:
					sendRedBombAttack();
					break;
				}
				vorago.setAttackProgress(vorago.getAttackProgress() + 1);
				break;
			} else {
				switch (Settings.VORAGO_ROTATION) {
				case 0:// Ceiling Collapse
				case 4:// Team Split
				case 5:// THE END
					if (vorago.getAttackProgress() > 16) // 17 attacks
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// 2 Specials
					case 8:
						switch (Settings.VORAGO_ROTATION) {
						case 0:// ceiling
							if (!sendCeilingCollapse()) {
								int attackStyle = Utils.random(3);
								switch (attackStyle) {
								case 0:// melee hit if possible
									if (Utils.isOnRange(vorago, target, 0))
										sendMeleeAttack();
									else
										sendBlueBombAttack();
									break;
								case 1:// magic
								case 2:
									sendBlueBombAttack();
									break;
								}
								break;
							}
							break;
						case 4: // Team Split
							sendTeamSplitAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							return 15;
						case 5:// ZA END
							startTheEnd();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							return 78;
						}
						break;
					case 1:// 3 attacks
					case 3:
					case 5:// 3 attacks
					case 6:
					case 7:
					case 9:// 3 attacks
					case 11:
					case 13:// 4 attacks
					case 14:
					case 15:
					case 16:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					case 10:
					case 2:
						sendBlueBombAttack(Settings.VORAGO_ROTATION == 0 ? !vorago.canSendCeilingCollapse() : true);
						break;
					case 4:// reflect
						sendReflectAttack();
						break;
					case 12:
						sendRedBombAttack();
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 1:// Scopulus
					switch (vorago.getAttackProgress()) {
					case 0:
						sendScopuli();
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 1:
						sendBlueBombAttack();
						break;
					}
					break;
				case 2:// vitalis
					if (vorago.getAttackProgress() > 19)// 20 attacks
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// vitalis orb
					case 8:
						sendVitalisOrb();
						break;
					case 1:// 3 attacks
					case 2:
					case 3:
					case 5:
					case 6:
					case 7:
					case 9:
					case 10:
					case 11:
					case 17:
					case 18:
					case 19:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					case 4:
						sendReflectAttack();
						break;
					case 12:
					case 13:
						sendSmash();
						break;
					case 14:
						sendBlueBombAttack(false);
					case 15:
					case 16:
						sendSmash();
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 3:// GREEN BOMBIE <3
					if (vorago.getAttackProgress() > 20)// 21 attacks
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// green bomb
					case 10:// seconds green
						sendGreenBombAttack();
						break;
					case 1:// 5 attacks Skips to case 6(reflect) if 10hit
					case 2:
					case 3:
					case 4:
					case 5:
					case 7:// 3 attacks
					case 8:
					case 9:
					case 11:// 5 attacks
					case 12:
					case 13:
					case 14:
					case 15:
					case 17:// 4 attacks
					case 18:
					case 19:
					case 20:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					case 6:// reflect
						sendReflectAttack();
						break;
					case 16:// red bomb skips to here if attack progress 10
						sendRedBombAttack();
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
			}
			break;
		case 4:// Phase 4
		case 9:// Phase 9 HardMode
			if (hardMode && vorago.getPhase() == 4) {// scop
				switch (vorago.getAttackProgress()) {
				case 0:
					sendScopuli();
					sendBlueBombAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 1:
					sendBlueBombAttack();
					break;
				}
				break;
			} else {
				switch (Settings.VORAGO_ROTATION) {
				case 5:// purple bomb
					if (vorago.getAttackProgress() > 24) // 25 attacks
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// WaterFall
						if (vorago.getPhaseProgress() == 3) {// skip to stone
							// clown
							int attackStyle = Utils.random(3);
							switch (attackStyle) {
							case 0:// melee hit if possible
								if (Utils.isOnRange(vorago, target, 0))
									sendMeleeAttack();
								else
									sendBlueBombAttack();
								break;
							case 1:// magic
							case 2:
								sendBlueBombAttack();
								break;
							}
							vorago.setAttackProgress(vorago.getAttackProgress() + 5);
							return 7;
						}
						sendWaterFallAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 19;
					case 1:// 3 attacks
					case 2:
					case 3:
					case 5:// 7 attacks
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 13:// 3 attacks after za end
					case 14:
					case 15:
					case 17:// 4 attacks
					case 18:
					case 19:
					case 20:
					case 22:// 3 attacks
					case 23:
					case 24:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					case 4:// Clones + attack(1)
						attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						sendStoneClones();
						break;
					case 12:// Za End
						startTheEnd();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 78;
					case 16:// red bomb
						sendRedBombAttack();
						break;
					case 21:
						sendReflectAttack();
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				default:// other rotations
					if (vorago.getAttackProgress() > 27) // 28 attacks
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// WaterFall
						if (vorago.getPhaseProgress() == 3) {// skip to stone
							// clown
							int attackStyle = Utils.random(3);
							switch (attackStyle) {
							case 0:// melee hit if possible
								if (Utils.isOnRange(vorago, target, 0))
									sendMeleeAttack();
								else
									sendBlueBombAttack();
								break;
							case 1:// magic
							case 2:
								sendBlueBombAttack();
								break;
							}
							vorago.setAttackProgress(vorago.getAttackProgress() + 5);
							return 7;
						}
						sendWaterFallAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 19;
					case 1:// 3 attacks
					case 2:
					case 3:
					case 5:// 7 attacks
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 13:// 3 attacks
					case 15:
					case 21:// 3attacks
					case 22:
					case 23:
					case 25:// 3 attacks
					case 26:
					case 27:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						break;
					case 4:// Clones + attack(1)
						attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else
								sendBlueBombAttack();
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							break;
						}
						sendStoneClones();
						break;
					case 12:
						switch (Settings.VORAGO_ROTATION) {
						case 0:// Ceiling
							if (!sendCeilingCollapse()) {
								attackStyle = Utils.random(3);
								switch (attackStyle) {
								case 0:// melee hit if possible
									if (Utils.isOnRange(vorago, target, 0))
										sendMeleeAttack();
									else
										sendBlueBombAttack();
									break;
								case 1:// magic
								case 2:
									sendBlueBombAttack();
									break;
								}
								break;
							}
							break;
						case 1:// Scopulus
							sendRedBombAttack();
							bonusAttacks++;
							break;
						case 2:// Vitalis
							sendVitalisOrb();
							break;
						case 3:// Green Bomb
							sendGreenBombAttack();
							bonusAttacks += 2;
							break;
						case 4:
							sendTeamSplitAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							return 15;
						}
						break;
					case 14:// Special Blue Bomb in some rotations
						sendBlueBombAttack(Settings.VORAGO_ROTATION == 0 ? !vorago.canSendCeilingCollapse() : true);
						break;
					case 16:// 5 smashes
					case 17:
						sendSmash();
						break;
					case 18:
						sendBlueBombAttack(false);
					case 19:
					case 20:
						sendSmash();
						break;
					case 24:
						sendReflectAttack();
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
			}
			break;
		case 5:
			if (hardMode) {// vitalis
				if (vorago.getAttackProgress() > 19)// 20 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// vitalis orb
				case 8:
					sendVitalisOrb();
					break;
				case 1:// 3 attacks
				case 2:
				case 3:
				case 5:
				case 6:
				case 7:
				case 9:
				case 10:
				case 11:
				case 17:
				case 18:
				case 19:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else
							sendBlueBombAttack();
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						break;
					}
					break;
				case 4:
					sendReflectAttack();
					break;
				case 12:
				case 13:
					sendSmash();
					break;
				case 14:
					sendBlueBombAttack(false);
				case 15:
				case 16:
					sendSmash();
					break;
				}
				vorago.setAttackProgress(vorago.getAttackProgress() + 1);
				break;
			} else {
				switch (Settings.VORAGO_ROTATION) {
				case 0:// ceiling
				case 4:// team split
					if (vorago.getAttackProgress() > 13)// 14 attacks not
						// counting
						// melee hits
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// team split
						sendTeamSplitAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 15;
					case 1:// 3 blues attacks
					case 2:
					case 3:
					case 5:// 3 blues attacks
					case 6:
					case 7:
					case 11:// 3 attacks
					case 12:
					case 13:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else {
								sendBlueBombAttack();
								vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							}
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						break;
					case 4:// reflect
						sendReflectAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 8:// 3 smashes
					case 9:
					case 10:
						sendSmash();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 1:// scop
				case 3:// green bombiee <33
				case 5:// za end <3
					if (vorago.getAttackProgress() > 15)
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// special attack
						switch (Settings.VORAGO_ROTATION) {
						case 1:// za purple bombie <3
						case 5:
							sendPurpleBombAttack();
							break;
						case 3:// za green bomb <3333
							sendGreenBombAttack();
							break;
						}
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 1:// 5 attacks
					case 2:
					case 3:
					case 4:
					case 5:
					case 7:// 3 attacks
					case 8:
					case 9:
					case 13:// 3 attacks
					case 14:
					case 15:
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else {
								sendBlueBombAttack();
								vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							}
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						break;
					case 6:// reflect
						sendReflectAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 10:// 3 smashes
					case 11:
					case 12:
						sendSmash();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 2:// vitalis
					if (vorago.getAttackProgress() > 13)
						vorago.setAttackProgress(0);
					switch (vorago.getAttackProgress()) {
					case 0:// vitalis
						if (!sendVitalisOrb()) {
							sendRedBombAttack();
							bonusAttacks++;
							skippedVitalis = true;
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						skippedVitalis = false;
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 3:// if skipped vitalis this is different
						int attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else {
								sendBlueBombAttack();
								vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
								if (skippedVitalis)
									skippedVitalis = false;
							}
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
							if (skippedVitalis)
								skippedVitalis = false;
							break;
						}
						break;
					case 1:// 3 attacks
					case 2:
					case 5:// 3 attacks
					case 6:
					case 7:
					case 11:// 3 attacks
					case 12:
					case 13:
						attackStyle = Utils.random(3);
						switch (attackStyle) {
						case 0:// melee hit if possible
							if (Utils.isOnRange(vorago, target, 0))
								sendMeleeAttack();
							else {
								sendBlueBombAttack();
								vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							}
							break;
						case 1:// magic
						case 2:
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						break;
					case 4:// reflect
						sendReflectAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 8:// 3 smashes
					case 9:
					case 10:
						sendSmash();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				}
				break;
			}
		case 6:// hardMode green bomb
			if (vorago.getAttackProgress() > 20)// 21 attacks
				vorago.setAttackProgress(0);
			switch (vorago.getAttackProgress()) {
			case 0:// green bomb
			case 10:// seconds green
				sendGreenBombAttack();
				break;
			case 1:// 5 attacks Skips to case 6(reflect) if 10hit
			case 2:
			case 3:
			case 4:
			case 5:
			case 7:// 3 attacks
			case 8:
			case 9:
			case 11:// 5 attacks
			case 12:
			case 13:
			case 14:
			case 15:
			case 17:// 4 attacks
			case 18:
			case 19:
			case 20:
				int attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			case 6:// reflect
				sendReflectAttack();
				break;
			case 16:// red bomb skips to here if attack progress 10
				sendRedBombAttack();
				break;
			}
			vorago.setAttackProgress(vorago.getAttackProgress() + 1);
			break;
		case 7:
			if (vorago.getAttackProgress() > 16) // 17 attacks
				vorago.setAttackProgress(0);
			switch (vorago.getAttackProgress()) {
			case 0:// 2 Specials
			case 8:
				sendTeamSplitAttack();
				vorago.setAttackProgress(vorago.getAttackProgress() + 1);
				return 15;
			case 1:// 3 attacks
			case 3:
			case 5:// 3 attacks
			case 6:
			case 7:
			case 9:// 3 attacks
			case 11:
			case 13:// 4 attacks
			case 14:
			case 15:
			case 16:
				int attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			case 10:
			case 2:
				sendBlueBombAttack(true);
				break;
			case 4:// reflect
				sendReflectAttack();
				break;
			case 12:
				sendRedBombAttack();
				break;
			}
			vorago.setAttackProgress(vorago.getAttackProgress() + 1);
			break;
		case 8:
			if (vorago.getAttackProgress() > 16) // 17 attacks
				vorago.setAttackProgress(0);
			switch (vorago.getAttackProgress()) {
			case 0:// 2 Specials
			case 8:
				startTheEnd();
				vorago.setAttackProgress(vorago.getAttackProgress() + 1);
				return 78;
			case 1:// 3 attacks
			case 3:
			case 5:// 3 attacks
			case 6:
			case 7:
			case 9:// 3 attacks
			case 11:
			case 13:// 4 attacks
			case 14:
			case 15:
			case 16:
				int attackStyle = Utils.random(3);
				switch (attackStyle) {
				case 0:// melee hit if possible
					if (Utils.isOnRange(vorago, target, 0))
						sendMeleeAttack();
					else
						sendBlueBombAttack();
					break;
				case 1:// magic
				case 2:
					sendBlueBombAttack();
					break;
				}
				break;
			case 10:
			case 2:
				sendBlueBombAttack(true);
				break;
			case 4:// reflect
				sendReflectAttack();
				break;
			case 12:
				sendRedBombAttack();
				break;
			}
			vorago.setAttackProgress(vorago.getAttackProgress() + 1);
			break;
		case 10:// Phase 10 HardMode
			switch (Settings.VORAGO_ROTATION) {
			case 0:// Ceiling Collapse Hard Mode
			case 2:// Vitalis Hard Mode
				if (vorago.getAttackProgress() > 13) // 14 attacks
					vorago.setAttackProgress(0);

				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					switch (Settings.VORAGO_ROTATION) {
					case 0:// CEILING
						sendTeamSplitAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 15;
					case 2:// Vitalis
						if (!sendVitalisOrb()) {
							sendRedBombAttack();
							bonusAttacks++;
							skippedVitalis = true;
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						skippedVitalis = false;
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 8:// Special 2
					switch (Settings.VORAGO_ROTATION) {
					case 0:// Ceiling Collapse
						sendGreenBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 2:// Vitalis
						sendPurpleBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 3:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
							if (skippedVitalis)
								skippedVitalis = false;
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
						if (skippedVitalis)
							skippedVitalis = false;
						break;
					}
					break;
				case 1:// 3 attacks
				case 2:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 9:// 5 attacks
				case 10:
				case 11:
				case 12:
				case 13:
					attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 4:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			case 1:// Scopulus hard mode
			case 3:// Green Bomb Hard Mode
				if (vorago.getAttackProgress() > 13) // 14 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					switch (Settings.VORAGO_ROTATION) {
					case 1:// scop
						sendPurpleBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 3:// Green Bomb
						sendGreenBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 10:// Special 2
					switch (Settings.VORAGO_ROTATION) {
					case 1:// scop
						sendTeamSplitAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 15;
					case 3:// Green Bomb
						if (!sendVitalisOrb()) {
							sendRedBombAttack();
							bonusAttacks++;
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
				case 1:// 5 attacks
				case 2:
				case 3:
				case 4:
				case 5:
				case 7:// 3 attacks
				case 8:
				case 9:
				case 11:// 3 attacks
				case 12:
				case 13:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 6:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			case 4:// Team Split Hard Mode
				if (vorago.getAttackProgress() > 11) // 12 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
				case 8:// Special 2
					sendTeamSplitAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					return 15;
				case 1:// 3 attacks
				case 2:
				case 3:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 9:// 3 attacks
				case 10:
				case 11:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 4:// Reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			case 5:// ZA End
				if (vorago.getAttackProgress() > 15) // 16 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special
					sendPurpleBombAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 1:// 5 attacks
				case 2:
				case 3:
				case 4:
				case 5:
				case 7:// 3 attacks
				case 8:
				case 9:
				case 13:// 3 attacks
				case 14:
				case 15:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 6:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 10:// 3 smashes
				case 11:
				case 12:
					sendSmash();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			}
			break;
		case 11:// phase 11
			switch (Settings.VORAGO_ROTATION) {
			case 0:// ceiling collapse
				if (vorago.getAttackProgress() > 11) // 12 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					sendTeamSplitAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					return 15;
				case 8:// Special 2
					if (!sendVitalisOrb()) {
						sendRedBombAttack();
						bonusAttacks++;
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 1:// 3 attacks
				case 2:
				case 3:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 9:// 3 attacks
				case 10:
				case 11:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 4:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;

				}
				break;
			case 1:// scopulas
			case 3:// Green Bombie <3
			case 5:// Za End <3
				if (vorago.getAttackProgress() > 13) // 14 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					switch (Settings.VORAGO_ROTATION) {
					case 1:// scopulas
					case 5:// Za end <3
						sendPurpleBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 3:// Green Bombie <3
						sendGreenBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 10:// Special 2
					switch (Settings.VORAGO_ROTATION) {
					case 1:// scopulas
					case 5:// Za end <3
						if (!sendVitalisOrb()) {
							sendRedBombAttack();
							bonusAttacks++;
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
							break;
						}
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					case 3:// Green Bombie <3
						sendTeamSplitAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						return 15;
					}
					break;
				case 1:// 5 attacks
				case 2:
				case 3:
				case 4:
				case 5:
				case 7:// 3 attacks
				case 8:
				case 9:
				case 11:// 3 attacks
				case 12:
				case 13:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 6:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			case 2:// vitalis
				if (vorago.getAttackProgress() > 13) // 14 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					if (!sendVitalisOrb()) {
						sendRedBombAttack();
						bonusAttacks++;
						skippedVitalis = true;
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					skippedVitalis = false;
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 8:// 3 smashes (Special 2)
				case 9:
				case 10:
					sendSmash();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 3:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
							if (skippedVitalis)
								skippedVitalis = false;
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(skippedVitalis ? 8 : (vorago.getAttackProgress() + 1));
						if (skippedVitalis)
							skippedVitalis = false;
						break;
					}
					break;
				case 1:// 3 attacks
				case 2:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 11:// 3 attacks
				case 12:
				case 13:
					attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 4:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			case 4:// Team split
				if (vorago.getAttackProgress() > 13) // 14 attacks
					vorago.setAttackProgress(0);
				switch (vorago.getAttackProgress()) {
				case 0:// Special 1
					sendTeamSplitAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					return 15;
				case 8:// Special 2
					sendPurpleBombAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				case 1:// 3 attacks
				case 2:
				case 3:
				case 5:// 3 attacks
				case 6:
				case 7:
				case 9:// 5 attacks
				case 10:
				case 11:
				case 12:
				case 13:
					int attackStyle = Utils.random(3);
					switch (attackStyle) {
					case 0:// melee hit if possible
						if (Utils.isOnRange(vorago, target, 0))
							sendMeleeAttack();
						else {
							sendBlueBombAttack();
							vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						}
						break;
					case 1:// magic
					case 2:
						sendBlueBombAttack();
						vorago.setAttackProgress(vorago.getAttackProgress() + 1);
						break;
					}
					break;
				case 4:// reflect
					sendReflectAttack();
					vorago.setAttackProgress(vorago.getAttackProgress() + 1);
					break;
				}
				break;
			}
			break;

		}
		return 7;
	}

	public void startTheEnd() {
		vorago.startTheEnd();
	}

	public void sendPurpleBombAttack() {
		vorago.sendPurpleBombAttack();
	}

	public void sendTeamSplitAttack() {
		vorago.sendTeamSplit();
	}

	public void sendGreenBombAttack() {
		vorago.sendGreenBomb();
	}

	public boolean sendVitalisOrb() {
		return vorago.sendVitalisOrb();
	}

	public void sendScopuli() {
		vorago.spawnScopuli();
	}

	public void sendStoneClones() {
		vorago.getTemporaryAttributtes().put("ReducedDamage", Boolean.TRUE);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				vorago.getTemporaryAttributtes().remove("ReducedDamage");
			}
		}, 33);
		for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
			if (player == null || player.isDead())
				continue;
			player.getTemporaryAttributtes().remove("RecentlyKilledClone");
		}
		vorago.spawnStoneClones();
	}

	public void sendWaterFallAttack() {
		vorago.sendWaterFallAttack();
	}

	public boolean sendCeilingCollapse() {
		return vorago.sendCeilingCollapse();
	}

	public void sendBindAttack() {
		Player player = (Player) vorago.getCombat().getTarget();
		if (player == null)
			return;
		delayHit(vorago, 0, player, new Hit(vorago, 100, HitLook.REGULAR_DAMAGE));
	}

	public void sendMeleeAttack() {
		vorago.setNextAnimation(new Animation(20355));
		for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
			if (player == null || player.isDead() || !Utils.isOnRange(vorago, player, 1))
				continue;
			delayHit(vorago, 0, player,
					getMeleeHit(vorago, getMaxHit(vorago, 400, NPCCombatDefinitions.MELEE, player)));
		}
	}

	public void sendBlueBombAttack() {
		sendBlueBombAttack(true);
	}

	public void sendBlueBombAttack(boolean sendAnimation) {
		boolean hardMode = vorago.getVoragoInstance().getSettings().isHardMode();
		Entity target = getFarestTarget(null);
		Entity target2 = hardMode
				? (vorago.getPlayerOnBattleCount() == 1 ? getFarestTarget(null) : getFarestTarget(target))
				: (vorago.getPlayerOnBattleCount() > 10 ? getFarestTarget(target) : null);
		if (target == null)// shouldn't happen
			return;
		if (sendAnimation)
			vorago.setNextAnimation(new Animation(20356));
		vorago.setNextGraphics(new Graphics(4015));
		int numberOfBlues = (target2 == null) ? 1 : 2;
		for (int i = 0; i < numberOfBlues; i++) {
			Entity target3 = i == 0 ? target : target2;
			long startTime = Utils.currentTimeMillis();
			long arriveTime = startTime + (15000);// arrives after 3 seconds
			double speed = Utils.getProjectileSpeed(vorago, target3, 90, 20, startTime, arriveTime);
			Projectile projectile = World.sendProjectileNew(new WorldTile(vorago), target3, 4016, 90, 20, 10, speed, 0,
					0);
			int cycleTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
			CoresManager.fastExecutor.schedule(new TimerTask() {

				@Override
				public void run() {
					for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
						if (player == null || player.isDead() || Utils.getDistance(target3, player) > 2)
							continue;
						int damage = getMaxHit(vorago, 500, NPCCombatDefinitions.MAGE, player);
						player.setNextGraphics(new Graphics(4017));
						delayHit(vorago, 0, player, getMagicHit(vorago, damage));
					}
				}
			}, (cycleTime * 1000) - 950);
		}
	}

	public void sendGroundBlueBomb(WorldTile arrivelocation) {
		long startTime = Utils.currentTimeMillis();
		long arriveTime = startTime + (12000);// arrives after 2.eshi seconds
		double speed = Utils.getProjectileSpeed(vorago, arrivelocation, 90, 0, startTime, arriveTime);
		Projectile projectile = World.sendProjectileNew(vorago, arrivelocation, 4016, 90, 0, 10, speed, 0, 0);
		int cycleTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
		vorago.setNextGraphics(new Graphics(4020));
		World.sendGraphics(null, new Graphics(4022), vorago);
		long time = (cycleTime * 1000) - 950;
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
					if (player == null || player.isDead() || Utils.getDistance(arrivelocation, player) > 2)
						continue;
					int damage = getMaxHit(vorago, 500, NPCCombatDefinitions.MAGE, player);
					player.setNextGraphics(new Graphics(4017));
					delayHit(vorago, 0, player, getMagicHit(vorago, damage));
				}
			}
		}, time < 0 ? 2000 : time);
	}

	public void sendRedBombAttack() {
		boolean trollRedBombChance = Utils.random(10) == 0;
		Player target = trollRedBombChance ? getRandomTarget() : getFarestTarget(null);
		if (target == null)// shouldn't happen
			return;
		sendMessage(target, RED_BORDER, "<col=ff0000>Vorago has sent a bomb after you. Run!</col>");
		vorago.setNextAnimation(new Animation(20371));
		sendGroundBlueBomb(new WorldTile(target.getX(), target.getY(), target.getPlane()));
		long startTime = Utils.currentTimeMillis();
		long arriveTime = startTime + (20000);// arrives after 4 seconds
		double speed = Utils.getProjectileSpeed(vorago, target, 90, 20, startTime, arriveTime);
		Projectile projectile = World.sendProjectileNew(vorago, target, 4023, 90, 20, 10, speed, 0, 0);
		int cycleTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
		long time = (cycleTime * 1000) - 950;
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
					if (player == null || player.isDead() || Utils.getDistance(player, target) > 2)
						continue;
					boolean hardMode = vorago.getVoragoInstance().getSettings().isHardMode();
					int damage = (hardMode ? 300 : 200) + (getPlayersNearby(target, 3) * (hardMode ? 150 : 100));
					if (damage > (hardMode ? 1050 : 700))
						damage = hardMode ? 1050 : 700;
					player.setNextGraphics(new Graphics(4024));
					World.sendGraphics(null, new Graphics(3522),
							new WorldTile(player.getX(), player.getY(), player.getPlane()));
					delayHit(vorago, 0, player, new Hit(vorago, damage, HitLook.REGULAR_DAMAGE));
				}
			}
		}, time < 0 ? 4000 : time);
	}

	public void sendSmash() {
		boolean hardMode = vorago.getVoragoInstance().getSettings().isHardMode();
		Entity target = ((!hardMode && vorago.getPhase() == 5) || (hardMode && vorago.getPhase() >= 10))
				? getRandomTarget() : vorago.getCombat().getTarget();
		int damage = hardMode ? 450 : 300;
		// int smashDamage = hardMode ? (vorago.getPhase() >= 10 ? 750 : 1000) :
		// (vorago.getPhase() == 5 ? 450 : 600);
		if ((!hardMode && vorago.getPhase() != 5) || (hardMode && vorago.getPhase() < 10)) {
			vorago.getTemporaryAttributtes().put("VoragoType", 0);
			vorago.transform();
		}
		vorago.setNextAnimation(new Animation(20363));
		vorago.setNextGraphics(new Graphics(4018));
		World.sendGraphics(null, new Graphics(4019), new WorldTile(target));
		delayHit(vorago, 0, target, new Hit(vorago, damage, HitLook.REGULAR_DAMAGE));
	}

	public void sendReflectAttack() {
		boolean hardMode = vorago.getVoragoInstance().getSettings().isHardMode();
		vorago.setNextAnimation(new Animation(20319));
		vorago.setNextGraphics(new Graphics(4011));
		vorago.setTargetedPlayer(null);
		vorago.setTargetedPlayer(getRandomTarget());
		Player targetedPlayer = vorago.getTargetedPlayer();
		if (targetedPlayer == null)
			return;
		for (Player player : vorago.getVoragoInstance().getPlayersOnBattle()) {
			if (player == null || player.isDead())
				continue;
			boolean isTargetedPlayer = player == targetedPlayer;
			sendMessage(player, RED_BORDER,
					isTargetedPlayer ? "<col=ff0000>Vorago channels incoming damage to you. Beware!</col>"
							: "<col=FFFFFF>Vorago reflects incoming damage to surrounding foes!</col>");
		}
		targetedPlayer.setNextGraphics(new Graphics(4012));
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				if ((!hardMode && vorago.getPhase() != 5) || (hardMode && vorago.getPhase() < 10)) {
					vorago.getTemporaryAttributtes().put("VoragoType", 1);
					vorago.transform();
				}
				vorago.setTargetedPlayer(null);
				if (vorago.getTemporaryAttributtes().get("BringHimDownClick") != null) {
					targetedPlayer.getPackets().sendGameMessage("<col=00FF00>Vorago releases his mental link on you.");
				} else
					sendMessage(targetedPlayer, RED_BORDER, "<col=00FF00>Vorago releases his mental link on you.");
			}

		}, 9600);
	}

	public void sendGravityField() {
		vorago.spawnGravityField();
	}

	public int getPlayersNearby(Player target, int withinDistance) {
		return vorago.getPlayersNearby(target, withinDistance);
	}

	public void sendMessage(Player player, int border, String message) {
		vorago.getVoragoInstance().sendMessage(player, border, message);
	}

	public Player getRandomTarget() {
		List<Player> availablePlayers = new ArrayList<Player>();
		VoragoInstance instance = vorago.getVoragoInstance();
		for (int i = 0; i < instance.getPlayersOnBattle().size(); i++) {
			Player player = instance.getPlayersOnBattle().get(i);
			if (player == null || player.isDead() || Utils.getDistance(vorago, player) > 24)
				continue;
			availablePlayers.add(player);
		}
		return availablePlayers.isEmpty() ? null : availablePlayers.get(Utils.random(availablePlayers.size()));
	}

	public Player getFarestTarget(Entity exception) {
		int farestDistance = 0;
		int index = 0;
		for (int i = 0; i < vorago.getVoragoInstance().getPlayersOnBattle().size(); i++) {
			Player player = vorago.getVoragoInstance().getPlayersOnBattle().get(i);
			if (player == null || player.isDead() || !Utils.isOnRange(vorago, player, 15)
					|| (exception != null && player == exception))
				continue;
			int distance = Utils.getDistance(vorago, player);
			if (distance > farestDistance) {
				index = i;
				farestDistance = distance;
			}
		}
		return vorago.getVoragoInstance().getPlayersOnBattle().get(index);
	}
}
