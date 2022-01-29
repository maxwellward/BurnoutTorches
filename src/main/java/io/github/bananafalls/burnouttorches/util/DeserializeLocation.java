package io.github.bananafalls.burnouttorches.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class DeserializeLocation {

	public Location deserializeLoc(final String input) {
		try {
			final String[] string = input.split(",");
			final double x;
			final double y;
			final double z;
			float yaw = 0;
			float pitch = 0;
			x = Double.parseDouble(string[0]);
			y = Double.parseDouble(string[1]);
			z = Double.parseDouble(string[2]);

			if (string.length == 5) {
				yaw = Float.parseFloat(string[3]);
				pitch = Float.parseFloat(string[4]);
			}

			return new Location(Bukkit.getWorlds().get(0), x, y, z, yaw, pitch);
		} catch (final NullPointerException e) {
			return null;
		} catch (final NumberFormatException e) {
			Bukkit.getLogger().log(Level.SEVERE, "There was an error parsing a string into a location (something isn't a number)! Please check your configs.");
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
			return null;
		} catch (final ArrayIndexOutOfBoundsException e) {
			Bukkit.getLogger().log(Level.SEVERE, "There was an error parsing a string into a location (there is not 3 values)! Please check your configs.");
			Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
}
