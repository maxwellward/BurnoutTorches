package io.github.bananafalls.burnouttorches;

import io.github.bananafalls.burnouttorches.commands.Reload;
import io.github.bananafalls.burnouttorches.util.DeserializeLocation;
import io.github.bananafalls.burnouttorches.util.SerializeLocation;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class BurnoutTorches extends JavaPlugin {

    private static BurnoutTorches instance;
    public static BurnoutTorches getInstance() { return instance; }

    @Getter
    private DeserializeLocation deserializeLocation;
    @Getter
    private SerializeLocation serializeLocation;
    @Getter
    private TorchManager torchManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        initConfigs();
        TorchManager torchManager = new TorchManager();
        this.getCommand("burnouttorches").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(torchManager, this);

        this.torchManager = torchManager;
        this.deserializeLocation = new DeserializeLocation();
        this.serializeLocation = new SerializeLocation();

        InitTorches initTorches = new InitTorches(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private FileConfiguration torchesConfig;
    private void initConfigs() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        File torchesFile = new File(getDataFolder(), "torches.yml");
        if (!torchesFile.exists()) {
            torchesFile.getParentFile().mkdirs();
            saveResource("torches.yml", false);
        }

        torchesConfig = new YamlConfiguration();
        try {
            torchesConfig.load(torchesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getTorchesConfig() {
        return this.torchesConfig;
    }
}
