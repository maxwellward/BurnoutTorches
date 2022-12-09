package io.github.bananafalls.burnouttorches.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class ServerClose implements Listener {

    @EventHandler
    private void onServerShutdown(PluginDisableEvent e) {
        removeLingeringHolograms();
    }

    @EventHandler
    private void onServerStart(PluginEnableEvent e) {
        removeLingeringHolograms();
    }

    private void removeLingeringHolograms() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if(entity.getScoreboardTags().contains("btholo")) {
                    entity.remove();
                }
            }
        }
    }

}
