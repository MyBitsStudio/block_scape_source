package com.rs.game.npc.rfd;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.world.WorldTile;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.controllers.ImpossibleJad;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class AgrithNaNa extends ImpossibleJadNPC {

    private ImpossibleJad controler;

    public AgrithNaNa(int id, WorldTile tile, ImpossibleJad controler) {
		super(id, tile);
		this.controler = controler;
    }

    @Override
    public void processNPC() {
    	super.processNPC();
    }

    @Override
    public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
		    int loop;
	
		    @Override
		    public void run() {
				if (loop == 0)
				    setNextAnimation(new Animation(defs.getDeathEmote()));
				else if (loop >= defs.getDeathDelay()) {
				    reset();
				    finish();
				    controler.removeNPC();
				    stop();
				}
				loop++;
		    }
		}, 0, 1);
    }
}