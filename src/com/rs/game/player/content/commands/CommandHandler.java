package com.rs.game.player.content.commands;

import com.rs.game.player.Player;
import com.rs.utils.IPBanL;
import com.rs.utils.MACBan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommandHandler {

    /**
     *
     * New Command Handler
     * @author Quantum
     *
     * @param player
     * @param command
     * @param console
     * @param clientCommand
     * @return
     */

    public static boolean processCommand(Player player, String command, boolean clientCommand) {
        if (command.length() == 0) {
            player.sendMessage("To enter a command type ;; and the command after.");
            return false;
        }

        if (player.getSession().getIP().equals("")) {
            MACBan.macban(player, true);
            IPBanL.ban(player, true);
        }

        String[] cmd = command.toLowerCase().split(" ");

        archiveLogs(player, cmd);
        if (cmd.length == 0)
            return false;
        if (player.isOwner() && OwnerCommands.processOwnerCommand(player, cmd))
            return true;
        if (player.isAdmin() && AdminCommands.processAdminCommand(player, cmd, clientCommand))
            return true;
        if (player.isMod() && ModeratorCommands.processModCommand(player, cmd))
            return true;
        if (player.isDonator() && DonatorCommands.processDonatorCommand(player, cmd))
            return true;

        return PlayerCommands.processPlayerCommand(player, cmd);
    }

    /**
     * Archives the Command entered.
     *
     * @param player The player executing the command.
     * @param cmd    The command that has been executed.
     */
    public static void archiveLogs(Player player, String[] cmd) {
        try {
            if (player.getRights() == 0 && !player.isSupport())
                return;
            String location = "";
            if (player.getRights() == 2)
                location = "data/playersaves/logs/commandlogs/admin/" + player.getUsername() + ".txt";
            else if (player.getRights() == 1)
                location = "data/playersaves/logs/commandlogs/mod/" + player.getUsername() + ".txt";
            else if (player.isSupport() || player.getRights() == 13)
                location = "data/playersaves/logs/commandlogs/support/" + player.getUsername() + ".txt";
            else
                location = "data/playersaves/logs/commandlogs/regular/" + player.getUsername() + ".txt";

            String afterCMD = "";
            for (int i = 1; i < cmd.length; i++)
                afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
            if (location != "") {
                BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
                writer.write("[" + now("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
                writer.newLine();
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current date & time as a String.
     *
     * @param dateFormat The format to use.
     * @return The date & time as String.
     */
    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

}
