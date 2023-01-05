package com.discord;

import com.discord.listeners.ServerListener;
import com.discord.listeners.SlashCommandListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * New discord Class
 * @author Corrupt
 */
public class DiscordBot {

    public static DiscordApi api;

    /*
     * Builds the instance, to start the instance, just do a new instance, new NewDiscord();
    */
    public DiscordBot() {
        api = new DiscordApiBuilder().
                setAllIntents().
                addListener(new ServerListener()).
                addSlashCommandCreateListener(new SlashCommandListener()).
                setToken(Constants.TOKEN).login().join();

        createCommands();

        if(Constants.announceInvite)
            System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }

    /*
     * Create new commands here ---
     *
      SlashCommand.with("ping", "Checks the functionality of this command")
               .createGlobal(api)
               .join();
     * replace "ping" with the command, than the description.
     *
     */
    private void createCommands(){
        SlashCommand.with("ping", "Checks the functionality of this command")
                .createGlobal(api)
                .join();
        SlashCommand.with("cmds", "List of available commands")
                .createGlobal(api)
                .join();
        SlashCommand.with("players", "Current Online Player Count")
                .createGlobal(api)
                .join();
        SlashCommand.with("bosspets", "Boss Pet Drop Rates")
                .createGlobal(api)
                .join();
        SlashCommand.with("happyhour", "Displays happy hour times")
                .createGlobal(api)
                .join();
        SlashCommand.with("roles", "Assign roles based on game roles")
                .createGlobal(api)
                .join();
        SlashCommand.with("restart", "Restarts the server")
                .setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR)
                .setEnabledInDms(false)
                .createGlobal(api)
                .join();
    }

    /*
     * Static method to send messages to any channel
     * NewDiscord.sendMessage("message", Constants.CHANNEL);
     */
    public static void sendMessage(String message, long channelId){
        getChannel(channelId).sendMessage(message);
    }

    public static void sendMessage(@NotNull MessageBuilder builder, long channelId){
        builder.send(getChannel(channelId));
    }

    public static @NotNull @Unmodifiable TextChannel getChannel(long id){
        Optional<TextChannel> channel = api.getTextChannelById(id);
        final TextChannel[] channels = new TextChannel[1];
        channel.ifPresent(textChannel -> channels[0] = channel.get());
        return channels[0];
    }

    /*
     * NOT FINISHED
     */

    public CompletableFuture<Void> addRoleToUser(@NotNull User user, @NotNull Role role, String reason) {
        return new RestRequest<Void>(api, RestMethod.PUT, RestEndpoint.SERVER_MEMBER_ROLE)
                .setUrlParameters(user.getIdAsString(), role.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }


}
