package io.github.bananafalls.burnouttorches.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.logging.Level;

public class DeserializeTorch {

	public HashMap<Location, Long> deserializeTorch(final String input) {
		try {
			final String[] string = input.split(", ");
			final double x = Double.parseDouble(string[0]);
			final double y = Double.parseDouble(string[1]);
			final double z = Double.parseDouble(string[2]);
			final long remaining = Long.parseLong(string[3]);

			HashMap<Location, Long> torch = new HashMap<Location, Long>();
			torch.put(new Location(Bukkit.getWorlds().get(0), x, y, z), remaining);

			return torch;
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
