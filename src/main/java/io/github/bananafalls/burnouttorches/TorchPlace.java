package io.github.bananafalls.burnouttorches;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import static org.bukkit.Material.*;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TorchPlace implements Listener {

    ArrayList<Location> torchLocations = new ArrayList<Location>();
    private Plugin plugin = BurnoutTorches.getPlugin(BurnoutTorches.class);

    @EventHandler
    public void onTorchPlace(BlockPlaceEvent e){
        if(e.getBlockPlaced().getType() == TORCH || e.getBlockPlaced().getType() == WALL_TORCH){
            torchLocations.add(e.getBlockPlaced().getLocation());
            StartBurnoutTimer(e.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void onTorchBreak(BlockBreakEvent e){
        if(e.getBlock().getType() == TORCH || e.getBlock().getType() == WALL_TORCH){
            torchLocations.remove(e.getBlock().getLocation());
        }
    }

    public void StartBurnoutTimer(Location torchLoc){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                if(torchLocations.contains(torchLoc)){
                    torchLoc.getBlock().setType(AIR);
                    if(Material.getMaterial(plugin.getConfig().getString("drop").toUpperCase()) != null){
                        torchLoc.getWorld().dropItemNaturally(torchLoc, new ItemStack(Material.getMaterial(plugin.getConfig().getString("drop")), 1));
                    }
                    torchLocations.remove(torchLoc);
                }
            }
        },  plugin.getConfig().getLong("time") * 20L);
    }

}
