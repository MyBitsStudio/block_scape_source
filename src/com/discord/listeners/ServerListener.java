package com.discord.listeners;

import org.javacord.api.event.server.ServerBecomesAvailableEvent;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;
import org.jetbrains.annotations.NotNull;

/**
 * Simple server listener, not ready yet
 * @author Corrupt
 */

public class ServerListener implements ServerBecomesAvailableListener {

    @Override
    public void onServerBecomesAvailable(@NotNull ServerBecomesAvailableEvent event) {
        System.out.println("Loaded " + event.getServer().getName());
    }

}
