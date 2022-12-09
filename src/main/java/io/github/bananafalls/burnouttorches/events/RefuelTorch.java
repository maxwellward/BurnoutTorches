package io.github.bananafalls.burnouttorches.events;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import io.github.bananafalls.burnouttorches.util.HologramManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class RefuelTorch implements Listener {

    final BurnoutTorches plugin;
    final HologramManager hologramManager;

    public RefuelTorch() {
        this.plugin = BurnoutTorches.getInstance();
        this.hologramManager = plugin.getHologramManager();
    }

    @EventHandler
    private void onClickTorch(PlayerInteractEvent e) {
        if(e.getHand() == EquipmentSlot.OFF_HAND) { return; }
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if(e.getClickedBlock() == null) { return; }
        if(!plugin.getConfig().getBoolean("allow-refueling", true)) { return; }
        // If the torch was placed in creative mode, it won't have an entry, so don't allow it to be refuelled
        if(!plugin.getTorchManager().torchLocations.containsKey(e.getClickedBlock().getLocation())) { return; }

        PlayerInventory inventory = e.getPlayer().getInventory();
        List<String> refuelItems = plugin.getConfig().getStringList("refuel-items");

        if(refuelItems.contains(inventory.getItemInMainHand().getType().toString())) {
            if(!plugin.getConfig().getBoolean("burnout-in-creative", true) && e.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
            Material type = inventory.getItemInMainHand().getType();
            int count = inventory.getItemInMainHand().getAmount();
            inventory.setItemInMainHand(new ItemStack(type, count - 1));
        } else if(refuelItems.contains(inventory.getItemInOffHand().getType().toString())) {
            if(!plugin.getConfig().getBoolean("burnout-in-creative", true) && e.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
            Material type = inventory.getItemInOffHand().getType();
            int count = inventory.getItemInOffHand().getAmount();
            inventory.setItemInOffHand(new ItemStack(type, count - 1));
        } else { return; }
        refuelTorch(e.getClickedBlock());
    }

    private void refuelTorch(Block block) {
        TorchManager torchManager = plugin.getTorchManager();
        Location loc = block.getLocation();
        FileConfiguration config = plugin.getConfig();
        Bukkit.getScheduler().cancelTask(torchManager.torchLocations.get(loc));
        int configTime = config.getInt("time");

        if(config.getString("refuel-type", "RESET").equals("RESET")) {
            torchManager.startBurnoutTimer(loc, (long) configTime * 1000);
        } else if(config.getString("refuel-type", "RESET").equals("ADD")) {
            long endMillis = torchManager.torchEndings.get(loc);
            long remaining = endMillis - System.currentTimeMillis();
            long newEnd = System.currentTimeMillis() + remaining + (configTime * 1000L);
            torchManager.torchEndings.replace(loc, newEnd);
            long newRemaining = newEnd - System.currentTimeMillis();
            torchManager.startBurnoutTimer(loc, newRemaining);
            if(plugin.getConfig().getBoolean("hologram-on-fuel")) {
                hologramManager.displayHologram(newRemaining, loc);
            }
        }

        // Refuel cosmetics
        if(config.getBoolean("particle-on-fuel", true)) {
            block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.toCenterLocation(), 5, 0.1, 0.1, 0.1);
        }
        if(config.getBoolean("sound-on-fuel", true)) {
            block.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 1);
        }
    }
}
