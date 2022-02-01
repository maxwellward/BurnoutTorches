package io.github.bananafalls.burnouttorches.events;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.HashMap;

import static org.bukkit.Material.*;

public class TorchManager implements Listener {

    public HashMap<Location, Integer> torchLocations = new HashMap<>();
    // Might not be needed anymore
    //public HashMap<Location, Long> torchTimings = new HashMap<>(); // Torch location, time torch was placed (UNIX timestamp, milliseconds)
    public HashMap<Location, Long> torchEndings = new HashMap<>(); // Torch location, when the torch is set to burn out
    final BurnoutTorches plugin;

    public TorchManager() {
        this.plugin = BurnoutTorches.getInstance();
    }

    @EventHandler
    private void onTorchPlace(BlockPlaceEvent e){
        if(isTorch(e.getBlock().getLocation())) {
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE && !plugin.getConfig().getBoolean("burnout-in-creative", true)) { return; }
            startBurnoutTimer(e.getBlockPlaced().getLocation(), plugin.getConfig().getLong("time"));
        }
    }

    @EventHandler
    private void onTorchBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        if(isTorch(block.getLocation())){
            torchLocations.remove(block.getLocation());
            torchEndings.remove(block.getLocation());
            //torchTimings.remove(block.getLocation());
        }
    }

    public void startBurnoutTimer(Location torchLoc, Long time){
        BukkitTask timer = new BukkitRunnable() {
            @Override
            public void run() {
                if(isTorch(torchLoc)){
                    torchLoc.getBlock().setType(AIR);
                    if(Material.getMaterial(plugin.getConfig().getString("drop").toUpperCase()) != null){
                        torchLoc.getWorld().dropItemNaturally(torchLoc, new ItemStack(Material.getMaterial(plugin.getConfig().getString("drop")), 1));
                    }
                    torchLocations.remove(torchLoc);
                    torchEndings.remove(torchLoc);
                    //torchTimings.remove(torchLoc);
                }
            }
        }.runTaskLater(plugin, time * 20L);
        torchEndings.put(torchLoc, (time * 1000) + System.currentTimeMillis());
        torchLocations.put(torchLoc, timer.getTaskId());
        //torchTimings.put(torchLoc, System.currentTimeMillis());
    }

    @EventHandler
    private void onShutdown(PluginDisableEvent e) throws IOException {
        plugin.getSaveManagement().saveTorches();
    }

    private boolean isTorch(Location loc) {
        Material type = loc.getBlock().getType();
        return type == TORCH || type == WALL_TORCH || type == SOUL_TORCH || type == SOUL_WALL_TORCH;
    }

}
