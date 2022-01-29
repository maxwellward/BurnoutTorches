package io.github.bananafalls.burnouttorches.util;

import org.bukkit.Location;

public class SerializeLocation {
    public String serializeLoc(final Location input) {
        try {
            int x = input.getBlockX();
            int y = input.getBlockY();
            int z = input.getBlockZ();

            return x + ", " + y + ", " + z;
        } catch (final NullPointerException e) {
            return null;
        }
    }
}
