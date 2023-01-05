package com.rs.game.npc.godwars.zaros;

import java.util.ArrayList;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.world.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.world.World;
import com.rs.game.world.WorldTile;
import com.rs.game.activites.ZarosGodwars;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.world.controllers.ZGDController;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class Nex extends NPC {

	public NexMinion[] nexMinions = new NexMinion[4];
	private NPC[] bloodReavers = new NPC[2];
	private boolean firstStageAttack, hasShadowTraps, siphioning, isFlying, rangeActivated;
	private NexPhase currentPhase;
	private long lastVirusAttack;
	private int nexAttack, prayerTicks, lastAttack;
	private int taskUID;

	public enum NexPhase {

		START("AT LAST!", new ForceTalk(""), 3295, -1, 0, -1),

		SMOKE("Fumus!", new ForceTalk("Fill my soul with smoke!"), 3325, 3310, 1, 4),

		SHADOW("Umbra!", new ForceTalk("Darken my shadow!"), 3313, 3307, 2, 3),

		BLOOD("Cruor!", new ForceTalk("Flood my lungs with blood!"), 3299, 3298, 3, 2),

		ICE("Glacies!", new ForceTalk("Infuse me with the power of ice!"), 3304, 3327, 4, 1),

		ZAROS("", new ForceTalk("NOW, THE POWER OF ZAROS!"), -1, 3312, 5, 0);

		private String minionName;
		private ForceTalk message;
		private int firstSound, secondSound, phaseValue, flag;

		private NexPhase(String minionName, ForceTalk message, int firstSound, int secondSound, int phaseValue,
				int flag) {
			this.minionName = minionName;
			this.message = message;
			this.firstSound = firstSound;
			this.secondSound = secondSound;
			this.phaseValue = phaseValue;
			this.flag = flag;
		}

		public String getMinionName() {
			return minionName;
		}

		public ForceTalk getMessage() {
			return message;
		}

		public int getFirstSound() {
			return firstSound;
		}

		public int getSecondSound() {
			return secondSound;
		}

		public int getPhaseValue() {
			return phaseValue;
		}

		public int getFlag() {
			return flag;
		}
	}

	public Nex(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
		setCantInteract(true);
		setCapDamage(Utils.random(801, 999));
		setLureDelay(3000);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceFollowClose(true);
		taskUID = ZarosGodwars.TASK_UID;
	}

	public int getTaskUID() {
		return taskUID;
	}

	private void resetPrayerTicks() {
		prayerTicks = getId() == 15776 ? 100 : 35;
	}

	public void transformNex() {
		if (getId() == 15776)
			setNextNPCTransformation(13447);
		else
			setNextNPCTransformation(getId() == 13449 ? 13447 : getId() + 1);
	}

	@Override
	public void processNPC() {
		if (isDead() || isSiphioning() || isCantInteract())
			return;
		lastAttack++;
		if (lastAttack == 14)
			setNextWorldTile(new WorldTile(2925, 5203, 0));
		if (!getCombat().process())
			checkAgressivity();
		if (!rangeActivated && Utils.random(750) == 0 && getId() == 13447) {
			setNextNPCTransformation(15776);
			rangeActivated = true;
		}
		if (getHitpoints() <= getMaxHitpoints() - (currentPhase.getPhaseValue() * 6000)
				&& !nexMinions[currentPhase.getPhaseValue() - 1].isBarrierBroken()) {
			nexMinions[currentPhase.getPhaseValue() - 1].breakBarrier();
			setNextForceTalk(new ForceTalk(currentPhase.getMinionName() + ", dont fail me!"));
			setCapDamage(0);
		}
		if (currentPhase == NexPhase.ZAROS || getId() == 15776) {
			if (prayerTicks > 0)
				prayerTicks--;
			else {
				resetPrayerTicks();
				transformNex();
			}
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (isSiphioning())
			hit.setHealHit();
		if (hit.getDamage() > 0)
			playSoundEffect(ZarosGodwars.HIT_SOUNDS[Utils.random(ZarosGodwars.HIT_SOUNDS.length)]);
		if (currentPhase == null)
			return;
		if (getHitpoints() <= getMaxHitpoints() - (currentPhase.getPhaseValue() * 6000)
				&& !nexMinions[currentPhase.getPhaseValue() - 1].isBarrierBroken()) {
			nexMinions[currentPhase.getPhaseValue() - 1].breakBarrier();
			setNextForceTalk(new ForceTalk(currentPhase.getMinionName() + ", dont fail me!"));
			setCapDamage(0);
		}
		if (currentPhase == NexPhase.ZAROS || getId() == 15776) {
			hit.setDamage((int) (hit.getDamage() * 1.23));
			if (getId() == 13449 && hit.getLook() == HitLook.MELEE_DAMAGE
					|| getId() == 15776 && hit.getLook() == HitLook.RANGE_DAMAGE) {
				Entity source = hit.getSource();
				if (source != null) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * (getId() == 13449 ? source.getMeleePrayerMultiplier()
							: source.getRangePrayerMultiplier())));
					if (deflectedDamage > 0)
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
				}
			}
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDeath(final Entity source) {
		setNextNPCTransformation(13450);
		final NPCCombatDefinitions defs = getCombatDefinitions();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0)
					setNextAnimation(new Animation(17415));
				else if (loop >= defs.getDeathDelay()) {
					drop();
					finish();
					ZarosGodwars.end();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		setNextForceTalk(new ForceTalk("Taste my wrath!"));
		playSoundEffect(3323);
		setNextGraphics(new Graphics(2259));
		final NPC target = this;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				ArrayList<Entity> possibleTargets = getPossibleTargets();
				if (possibleTargets != null) {
					for (Entity entity : possibleTargets) {
						if (entity == null || entity.isDead() || entity.hasFinished()
								|| !entity.withinDistance(target, 10))
							continue;
						World.sendProjectile(target, new WorldTile(getX() + 2, getY() + 2, getPlane()), 2261, 24, 0, 41,
								35, 30, 0);
						World.sendProjectile(target, new WorldTile(getX() + 2, getY(), getPlane()), 2261, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new WorldTile(getX() + 2, getY() - 2, getPlane()), 2261, 41, 0, 41,
								35, 30, 0);
						World.sendProjectile(target, new WorldTile(getX() - 2, getY() + 2, getPlane()), 2261, 41, 0, 41,
								35, 30, 0);
						World.sendProjectile(target, new WorldTile(getX() - 2, getY(), getPlane()), 2261, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new WorldTile(getX() - 2, getY() - 2, getPlane()), 2260, 41, 0, 41,
								35, 30, 0);
						World.sendProjectile(target, new WorldTile(getX(), getY() + 2, getPlane()), 2261, 41, 0, 41, 35,
								30, 0);
						World.sendProjectile(target, new WorldTile(getX(), getY() - 2, getPlane()), 2261, 41, 0, 41, 35,
								30, 0);
						entity.applyHit(new Hit(target, Utils.random(600), HitLook.REGULAR_DAMAGE));
					}
				}
			}
		});
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> targets = getPossibleTargets(true, true);
		ArrayList<Entity> targetsCleaned = new ArrayList<Entity>();
		for (Entity t : targets) {
			if (t instanceof Player && !(((Player) t).getControlerManager().getControler() instanceof ZGDController))
				continue;
			targetsCleaned.add(t);
		}
		return targetsCleaned;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public void setNextAnimation(Animation nextAnimation) {
		if (siphioning)
			return;
		super.setNextAnimation(nextAnimation);
	}

	@Override
	public void setNextGraphics(Graphics nextGraphic) {
		if (siphioning)
			return;
		super.setNextGraphics(nextGraphic);
	}

	public ArrayList<Entity> calculatePossibleTargets(WorldTile current, WorldTile position, boolean northSouth) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e : getPossibleTargets()) {
			if (e.withinArea(current.getX(), current.getY(), position.getX() + (northSouth ? 2 : 0),
					position.getY() + (!northSouth ? 2 : 0))
					|| e.withinArea(position.getX(), position.getY(), current.getX() + (northSouth ? 2 : 0),
							current.getY() + (!northSouth ? 2 : 0)))
				list.add(e);
		}
		return list;
	}

	public Entity getFarthestTarget() {
		int lastDistance = 0;
		Entity target = null;
		for (Entity e : getPossibleTargets()) {
			int currentDistance = Utils.getDistance(this, e);
			if (lastDistance <= currentDistance) {
				lastDistance = currentDistance;
				target = e;
			}
			return target;
		}
		return null;
	}

	public boolean isFirstStageAttack() {
		return firstStageAttack;
	}

	public void setFirstStageAttack(boolean firstStageAttack) {
		this.firstStageAttack = firstStageAttack;
	}

	public NexPhase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(NexPhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public NexMinion getMinion(int index) {
		return nexMinions[index];
	}

	public void setMinion(int index, NexMinion nexMinion) {
		nexMinions[index] = nexMinion;
	}

	public void start() {
		setCantInteract(false);
		Entity target = ZarosGodwars.getRandomNexTarget();
		if (target != null)
			setTarget(target);
	}

	public long getLastVirusAttack() {
		return lastVirusAttack;
	}

	public void addVirusAttackDelay(long time) {
		this.lastVirusAttack = Utils.currentTimeMillis() + (time * 600);
	}

	public int getNexAttack() {
		return nexAttack;
	}

	public void resetNexAttack() {
		this.nexAttack = 0;
	}

	public void incrementNexAttack() {
		nexAttack++;
	}

	public void setNexAttack(int i) {
		nexAttack = i;
	}

	public void removeInfectedPlayers() {
		for (Entity entity : getPossibleTargets()) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.getPackets().sendGameMessage("The clouds around you dissapate.");
				player.getTemporaryAttributtes().remove("nex_infected");
			}
		}
	}

	public void removeShadow() {
		for (Player player : ZarosGodwars.getPlayers())
			player.getPackets().sendGlobalConfig(1435, 255);
	}

	public boolean hasShadowTraps() {
		return hasShadowTraps;
	}

	public void setHasShadowTraps(boolean hasShadowTraps) {
		this.hasShadowTraps = hasShadowTraps;
	}

	public NPC[] getBloodReavers() {
		return bloodReavers;
	}

	public void killBloodReavers() {
		int totalHp = 0;
		for (int i = 0; i < bloodReavers.length; i++) {
			NPC npc = bloodReavers[i];
			if (npc == null || npc.isDead() || npc.hasFinished())
				continue;
			totalHp += npc.getHitpoints();
			npc.finish();
		}
		bloodReavers = new NPC[3];
		if (totalHp > 0)
			heal(totalHp);
	}

	public void sendFinalStage() {
		heal((getMaxHitpoints() * 33) / 100);
		setNextAnimation(new Animation(17410));
		setNextGraphics(new Graphics(3376));// turm
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public void resetLastAttack() {
		lastAttack = 0;
	}

	public void playSoundEffect(int i) {
		playSound(i, 2);
	}

	public int getAttackSpeed() {
		return getCombatDefinitions().getAttackDelay();
	}

	public void setSiphioning(boolean siphion) {
		this.siphioning = siphion;
	}

	public boolean isSiphioning() {
		return this.siphioning;
	}
}