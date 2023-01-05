package com.discord.listeners;

import com.discord.Constants;
import com.discord.DiscordBot;
import com.rs.game.world.World;
import com.rs.game.player.Player;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This is the listener for all commands. After inputting a command inside of NewDiscord, you need to add the listener
 * case "command" -> { // do something }
 *
 * To send a message back :
 * slashCommandInteraction.createImmediateResponder()
 *                             .setContent("Message sent back.")
 *                             .setFlags(MessageFlag.EPHEMERAL) // only the user who sent the message can see this
 *                             .respond();
 *
 * @author Corrupt
 */


public class SlashCommandListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(@NotNull SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
        switch(slashCommandInteraction.getCommandName()){
            case "ping" -> slashCommandInteraction.createImmediateResponder()
                    .setContent("Pong!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            case "cmds" -> slashCommandInteraction.createImmediateResponder()
                    .setContent("!spotlight, !events, !players, !bosspets..")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            case "players" -> slashCommandInteraction.createImmediateResponder()
                    .setContent("There are currently " + World.getPlayers().size() + " players online..")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            case "bosspets" -> slashCommandInteraction.createImmediateResponder()
                    .setContent("Boss pet drop rates: Party Demon 1/50, the Assassin 1/75, Celestia 1/500, Giant Mole 1/400 Hydra 1/2000, Zulrah 1/400, Kraken 1/1000, Cerberus 1/750, Vet'ion 1/750, Callisto 1/750, Anivia 1/750, Hope Devourer 1/400, Smoke Devil 1/1000, Abyssal Sire 1/1000, Bad Santa 1/3000, Nex 1/250, Vorago 1/150, Thunderous 1/1000, KBD 1/500, Dark Feast 1/1000, Frosty 1/1000, Garg 1/1000, Skotizo 1/65, Venenatis 1/750, Scorpia 1/750, Dryax 1/1200, Corp 1/600, Glacor 1/1000, GwD 1/800, Aquatic Wyrm 1/800, Vorkath 1/1000, Chambers of Xeric 1/65, Theatre of Blood 1/65...")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            case "happyhour"->
                    slashCommandInteraction.createImmediateResponder()
                            .setContent("10am Happy Hours occur on: Monday, Wednesday, Friday, Saturday.\n7pm Happy Hours occur on: Tuesday, Thursday, Saturday, Sunday.")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
            case "roles" -> slashCommandInteraction.createImmediateResponder()
                    .setContent("Coming soon!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            case "restart" -> {
                File file1 = new File("data/npcs/packedCombatDefinitions.ncd");

                if (file1.delete()) {
                    System.out.println(file1.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }

                for (Player p : World.getPlayers()) {
                    p.getDialogueManager().startDialogue("SimpleNPCMessage", 646, "<col=000000><shad=DEED97>This is a server restart authorised by Discord");
                }
                DiscordBot.sendMessage("Server restarting in 60 seconds!", Constants.ANNOUNCEMENTS_CHANNEL);
                World.safeShutdown(true, 60);
                slashCommandInteraction.createImmediateResponder()
                        .setContent("Server has been restarted. Please wait..")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }

        }
    }
}
