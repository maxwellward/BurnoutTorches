package io.github.bananafalls.burnouttorches;

import io.github.bananafalls.burnouttorches.commands.Reload;
import io.github.bananafalls.burnouttorches.listeners.TorchPlace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public final class BurnoutTorches extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("burnouttorches").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new TorchPlace(), this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        Metrics metrics = new Metrics(this, 7047);
        metrics.addCustomChart(new Metrics.SingleLineChart("players", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // (This is useless as there is already a player chart by default.)
                return Bukkit.getOnlinePlayers().size();
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("used_language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getConfig().getString("language", "en");
            }
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
