package io.github.bananafalls.burnouttorches.util;

import io.github.bananafalls.burnouttorches.BurnoutTorches;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class HologramManager {

    final BurnoutTorches plugin;
    public HologramManager() {
        this.plugin = BurnoutTorches.getInstance();
    }

    public void displayHologram(long msLeft, Location loc) {
        for (Entity entity : loc.getNearbyEntities(1, 1, 1)) {
            if(entity.getScoreboardTags().contains("btholo")) {
                entity.remove();
            }
        }

        String formattedTime = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(msLeft) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(msLeft) % TimeUnit.MINUTES.toSeconds(1));

        Location spawnLoc = loc.clone().toCenterLocation().add(0, 120, 0);
        ArmorStand as = (ArmorStand) loc.getWorld().spawn(spawnLoc, ArmorStand.class);
        as.teleport(loc.toCenterLocation().subtract(0, 2, 0));
        as.setGravity(false);
        as.setInvisible(true);
        as.setCanPickupItems(false);
        as.setCustomName(formattedTime);
        as.setCustomNameVisible(true);
        as.addScoreboardTag("btholo");

        BukkitTask timer = new BukkitRunnable() {
            @Override
            public void run() {
                as.remove();
            }
        }.runTaskLater(plugin, 30);
    }
}
