package io.github.bananafalls.burnouttorches;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.bukkit.Material.*;

public class TorchManager implements Listener {

    ArrayList<Location> torchLocations = new ArrayList<Location>();
    BurnoutTorches plugin;

    public TorchManager() {
        this.plugin = BurnoutTorches.getInstance();
    }

    @EventHandler
    private void onTorchPlace(BlockPlaceEvent e){
        if(isTorch(e.getBlock().getLocation())) {
            StartBurnoutTimer(e.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    private void onTorchBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == TORCH || e.getBlock().getType() == WALL_TORCH){
            torchLocations.remove(e.getBlock().getLocation());
        }
    }

    public void StartBurnoutTimer(Location torchLoc){
        torchLocations.add(torchLoc);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                if(isTorch(torchLoc)){
                    torchLoc.getBlock().setType(AIR);
                    if(Material.getMaterial(plugin.getConfig().getString("drop").toUpperCase()) != null){
                        torchLoc.getWorld().dropItemNaturally(torchLoc, new ItemStack(Material.getMaterial(plugin.getConfig().getString("drop")), 1));
                    }
                    torchLocations.remove(torchLoc);
                }
            }
        },  plugin.getConfig().getLong("time") * 20L);
    }

    @EventHandler
    private void onShutdown(PluginDisableEvent e) throws IOException {
        ArrayList<String> serializedLocations = new ArrayList<>();
        FileConfiguration torchConfig = plugin.getTorchesConfig();
        for(Location loc : torchLocations) {
            serializedLocations.add(plugin.getSerializeLocation().serializeLoc(loc));
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
