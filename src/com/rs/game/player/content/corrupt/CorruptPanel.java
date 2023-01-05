package com.rs.game.player.content.corrupt;

import com.rs.Settings;
import com.rs.game.world.World;
import com.rs.game.player.Player;
import com.rs.game.player.content.corrupt.animations.teleports.BossTeleporting;
import com.rs.game.player.content.corrupt.animations.teleports.TrainingTeleporting;
import com.rs.game.player.content.corrupt.inters.impl.BlockExplorer;
import com.rs.utils.Colors;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CorruptPanel {

    public static int tab = 3007;

    public static void sendTab(Player player){
        player.getPackets().sendIComponentText(tab, 17, "Corrupt Server Info <br>" +
                "Time : "+ Colors.green + time("hh:mm:ss a")+"<br>"+
                "Online : "+Colors.green + World.getPlayersOnline()+"<br>" +
                "Double XP : "+(World.isWeekend() ? "Active (weekend)" : (World.isWellActive() ? "Active (well)" : Colors.red + "Not Active"))+"<br>" +
                "World Info<br>" +
                "Vorago : "+Colors.green + Settings.VORAGO_ROTATION_NAMES[Settings.VORAGO_ROTATION]);
    }

    public static void handleButtons(Player player, int componentId){
        switch(componentId){
            case 4:
                player.getInterfaceManager().sendInterface(new BlockExplorer(player, 3021));
                break;
            case 5:
                player.getPlayerVars().setInterface3018tabSelected(1);
                player.getPlayerVars().setSelectedVIPItem(null);
                player.getDonationManager().sendManager(player);
                break;
            case 6:
                player.getPlayerVars().setInterface3015Tab(1);
                player.getPlayerVars().setInterface3015Piece(1);
                player.getCosmetics().sendInterface();
                break;
            case 7:
                TrainingTeleporting.sendInterface(player);
                break;
            case 8:
                BossTeleporting.sendInterface(player);
                break;
            case 9:
                player.getPlayerVars().setInterface3013Tab(1);
                player.getPlayerStash().sendInterface();
                break;
        }
    }

    public static String time(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }
}
