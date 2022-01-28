package io.github.bananafalls.burnouttorches;

import io.github.bananafalls.burnouttorches.commands.Reload;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class BurnoutTorches extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("burnouttorches").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new TorchPlace(), this);

        initConfigs();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private FileConfiguration torchesConfig;
    private void initConfigs() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        File torchesFile = new File(getDataFolder(), "custom.yml");
        if (!torchesFile.exists()) {
            torchesFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
        }

        torchesConfig = new YamlConfiguration();
        try {
            torchesConfig.load(torchesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getTorches() {
        return this.torchesConfig;
    }
}
