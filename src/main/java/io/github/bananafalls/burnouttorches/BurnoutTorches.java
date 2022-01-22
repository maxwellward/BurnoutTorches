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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
