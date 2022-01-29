package io.github.bananafalls.burnouttorches.events;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
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
        PlayerInventory inventory = e.getPlayer().getInventory();

        List<String> refuelItems = plugin.getConfig().getStringList("refuel-items");

        if(refuelItems.contains(inventory.getItemInMainHand().getType().toString())) {
            Material type = inventory.getItemInMainHand().getType();
            int count = inventory.getItemInMainHand().getAmount();
            inventory.setItemInMainHand(new ItemStack(type, count - 1));
        } else if(refuelItems.contains(inventory.getItemInOffHand().getType().toString())) {
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
        long time = config.getLong("time");

        torchManager.torchLocations.get(loc).cancel();
        torchManager.StartBurnoutTimer(loc, time);
        torchManager.torchTimings.replace(loc, System.currentTimeMillis());
        if(config.getBoolean("particle-on-fuel", true)) {
            block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.toCenterLocation(), 5, 0.1, 0.1, 0.1);
        }
        if(config.getBoolean("sound-on-fuel", true)) {
            block.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 5, 1);
        }
    }
}
