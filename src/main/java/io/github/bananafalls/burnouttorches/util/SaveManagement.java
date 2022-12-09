package io.github.bananafalls.burnouttorches.util;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import io.github.bananafalls.burnouttorches.events.TorchManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SaveManagement {

    final BurnoutTorches plugin;
    final TorchManager torchManager;

    public SaveManagement() {
        this.plugin = BurnoutTorches.getInstance();
        this.torchManager = plugin.getTorchManager();
        runAutosave();
    }

    public void saveTorches() {
        ArrayList<String> serializedLocations = new ArrayList<>();
        FileConfiguration torchConfig = plugin.getTorchesConfig();
        for (Map.Entry<Location, Integer> entry : torchManager.torchLocations.entrySet()) {
            long remaining = torchManager.torchEndings.get(entry.getKey()) - System.currentTimeMillis();
            serializedLocations.add(plugin.getSerializeTorch().serializeTorch(entry.getKey(), remaining));
        }
        try {
            torchConfig.set("torches", serializedLocations);
            File torchesFile = new File(plugin.getDataFolder(), "torches.yml");
            torchConfig.save(torchesFile);
            Bukkit.getLogger().info("[BurnoutTorches] Saved all torches to torches.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BukkitTask autosaveTask;
    public void runAutosave() {
        if(autosaveTask != null) {
            if(!autosaveTask.isCancelled()) {
                autosaveTask.cancel();
            }
        }

        FileConfiguration config = plugin.getConfig();
        if(config.getBoolean("autosave", true)) {
            autosaveTask = new BukkitRunnable() {
                @Override
                public void run() {
                    saveTorches();
                    runAutosave();
                }
            }.runTaskLater(plugin, config.getInt("autosave-frequency", 600) * 20L);
        }
    }
}
