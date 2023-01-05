package com.corrupt.raids;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.world.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;

public class RaidsController extends Controller {


    @Override
    public void start() {
    }

    @Override
    public boolean sendDeath() {
        player.lock(7);
        player.stopAll();
        if (player.getRaidsManager().getLives() > 0) {
            player.getRaidsManager().takeLife();
            player.sm(Colors.red + "[RAIDS] You have lost a life. You now have : " + player.getRaidsManager().getLives());
            player.heal(player.getMaxHitpoints());
            return false;
        } else {
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
                        removeControler();
                        player.getRaidsManager().resetRaidSettings();
                        player.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
                        player.setNextAnimation(new Animation(-1));
                    } else if (loop == 4) {
                        player.getPackets().sendMusicEffect(90);
                        player.unlock();
                        stop();
                    }
                    loop++;
                }
            }, 0, 1);
        }
        return false;
    }

    @Override
    public boolean logout() {
        player.setForceMultiArea(false);
        removeControler();
        player.getRaidsManager().setLives(0);
        player.getRaidsManager().resetRaidSettings();
        player.getRaidsManager().setSkelCount(0);
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        player.setForceMultiArea(false);
        removeControler();
        player.getRaidsManager().setLives(0);
        player.getRaidsManager().resetRaidSettings();
        player.getRaidsManager().setSkelCount(0);
    }

    @Override
    public void forceClose() {
        player.setForceMultiArea(false);
        removeControler();
        player.getRaidsManager().setLives(0);
        player.getRaidsManager().resetRaidSettings();
        player.getRaidsManager().setSkelCount(0);
    }

}
