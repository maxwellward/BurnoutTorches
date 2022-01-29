package io.github.bananafalls.burnouttorches.events;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;

public class TorchManager implements Listener {

    HashMap<Location, BukkitTask> torchLocations = new HashMap<>();
    HashMap<Location, Long> torchTimings = new HashMap<>(); // Torch location, time left until burn (in milliseconds)
    BurnoutTorches plugin;

    public TorchManager() {
        this.plugin = BurnoutTorches.getInstance();
    }

    @EventHandler
    private void onTorchPlace(BlockPlaceEvent e){
        if(isTorch(e.getBlock().getLocation())) {
            StartBurnoutTimer(e.getBlockPlaced().getLocation(), plugin.getConfig().getLong("time"));
        }
    }

    @EventHandler
    private void onTorchBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        if(isTorch(block.getLocation())){
            torchLocations.remove(block.getLocation());
            torchTimings.remove(block.getLocation());
        }
    }

    public void StartBurnoutTimer(Location torchLoc, Long time){
        BukkitTask timer = new BukkitRunnable() {
            @Override
            public void run() {
                if(isTorch(torchLoc)){
                    torchLoc.getBlock().setType(AIR);
                    if(Material.getMaterial(plugin.getConfig().getString("drop").toUpperCase()) != null){
                        torchLoc.getWorld().dropItemNaturally(torchLoc, new ItemStack(Material.getMaterial(plugin.getConfig().getString("drop")), 1));
                    }
                    torchLocations.remove(torchLoc);
                    torchTimings.remove(torchLoc);
                }
            }
        }.runTaskLater(plugin, time * 20L);
        torchLocations.put(torchLoc, timer);
        torchTimings.put(torchLoc, System.currentTimeMillis());
    }

    @EventHandler
    private void onShutdown(PluginDisableEvent e) throws IOException {
        ArrayList<String> serializedLocations = new ArrayList<>();
        FileConfiguration torchConfig = plugin.getTorchesConfig();
        for (Map.Entry<Location, BukkitTask> entry : torchLocations.entrySet()) {
            long passed = System.currentTimeMillis() - torchTimings.get(entry.getKey());
            long remaining = (plugin.getConfig().getInt("time") * 1000L) - passed;
            serializedLocations.add(plugin.getSerializeTorch().serializeTorch(entry.getKey(), remaining));
        }
        torchConfig.set("torches", serializedLocations);
        File torchesFile = new File(plugin.getDataFolder(), "torches.yml");
        torchConfig.save(torchesFile);
    }

    private boolean isTorch(Location loc) {
        Material type = loc.getBlock().getType();
        return type == TORCH || type == WALL_TORCH || type == SOUL_TORCH || type == SOUL_WALL_TORCH;
    }

}
