package com.rs.game.world.controllers.bossInstance;

import com.rs.game.world.WorldObject;
import com.rs.game.map.bossInstance.BossInstance;

public class DagannothKingsInstanceController extends BossInstanceController {

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 10229) {
			getInstance().leaveInstance(player, BossInstance.EXITED);
			removeControler();
			return false;
		}
		return true;
	}
}
