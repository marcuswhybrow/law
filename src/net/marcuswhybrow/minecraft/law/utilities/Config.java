package net.marcuswhybrow.minecraft.law.utilities;

import net.marcuswhybrow.minecraft.law.Law;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public static Location getLocation(String path) {
		FileConfiguration config = Law.get().getPlugin().getConfig();
		
		String worldName = config.getString(path + ".world");
		
		if (worldName == null) {
			return null;
		}
		
		org.bukkit.World world = Law.get().getLawWorld(worldName).getBukkitWorld();
		Double x = config.getDouble(path + ".x");
		Double y = config.getDouble(path + ".y");
		Double z = config.getDouble(path + ".z");
		Double yaw = config.getDouble(path + ".yaw");
		Double pitch = config.getDouble(path + ".pitch");
		
		if (x != null || y != null || z != null) {
			if (yaw != null || pitch != null) {
				return new Location(world, x, y, z, new Float(yaw), new Float(pitch));
			} else {
				return new Location(world, x, y, z);
			}
		}
		return null;
	}
	
	public static void setLocation(String path, Location location) {
		FileConfiguration config = Law.get().getPlugin().getConfig();
		
		if (location == null) {
			config.set(path, null);
		} else {
			config.set(path + ".world", location.getWorld().getName());
			config.set(path + ".x", location.getX());
			config.set(path + ".y", location.getY());
			config.set(path + ".z", location.getZ());
			config.set(path + ".yaw", location.getYaw());
			config.set(path + ".pitch", location.getPitch());
		}
	}
}
