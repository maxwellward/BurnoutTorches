package io.github.bananafalls.burnouttorches.util;

import org.bukkit.Location;

public class SerializeTorch {
    public String serializeTorch(final Location input, final Long remaining) {
        try {
            int x = input.getBlockX();
            int y = input.getBlockY();
            int z = input.getBlockZ();

            return x + ", " + y + ", " + z + ", " + remaining;
        } catch (final NullPointerException e) {
            return null;
        }
    }
}
