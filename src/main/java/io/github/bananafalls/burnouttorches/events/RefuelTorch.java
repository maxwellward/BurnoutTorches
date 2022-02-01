package io.github.bananafalls.burnouttorches.events;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
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
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class RefuelTorch implements Listener {

    final BurnoutTorches plugin;

    public RefuelTorch() {
        this.plugin = BurnoutTorches.getInstance();
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
        int configTime = config.getInt("time");
        Bukkit.getScheduler().cancelTask(torchManager.torchLocations.get(loc));

        // If "RESET" do the normal stuff
        // If "ADD", get the current remaining time, add the new time, cancel old task, run it again with the new time

        if(config.getString("refuel-type", "RESET").equals("RESET")) {
            torchManager.startBurnoutTimer(loc, (long) configTime);
            //torchManager.torchEndings.replace(loc, System.currentTimeMillis() + (configTime * 1000L));
            //torchManager.torchTimings.replace(loc, System.currentTimeMillis());
        } else if(config.getString("refuel-type", "RESET").equals("ADD")) {
            System.out.println("Torch refuelled ------");
            long endMillis = torchManager.torchEndings.get(loc);
            System.out.println(torchManager.torchEndings);
            System.out.println(endMillis + " < end millis");
            long remaining = endMillis - System.currentTimeMillis();
            System.out.println(remaining + " < remaining");
            long newTime = (configTime * 1000L) + remaining;
            System.out.println(newTime + " < new time before divide");
            System.out.println(newTime/1000 + " < new time");
            torchManager.startBurnoutTimer(loc, newTime); // https://imgur.com/a/tsUt2db Hi Max in the future! This is the issue here. For some reason the new time is being multiplied by a thousand each time the torch is refuelled.
            // It starts at the proper number again for each individual torch, so if one torch is being multiplied by 10,000, a new torch will be fine on it's first refuel.
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
