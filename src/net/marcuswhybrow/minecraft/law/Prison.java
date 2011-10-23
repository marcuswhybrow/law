package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Prison {
	private String name;
	private HashMap<String, Location> cells;
	
	public Prison(String name) {
		this.name = name;
		this.cells = new HashMap<String, Location>();
		
		// Save the new name
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		config.createSection(this.getConfigPrefix());
		plugin.saveConfig();
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void addCell(String cellName, Location cellLocation) {
		cells.put(cellName, cellLocation);
		
		// Save the new cell
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		String prefix = this.getConfigPrefix() + ".cells." + cellName + ".location.";
		config.set(prefix + "x", cellLocation.getX());
		config.set(prefix + "y", cellLocation.getY());
		config.set(prefix + "z", cellLocation.getZ());
		plugin.saveConfig();
	}
	
	public void removeCell(String cellName) {
		cells.remove(name);
		
		// Remove the cell
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		String prefix = this.getConfigPrefix() + ".cells." + cellName + ".location.";
		config.set(prefix + "x", null);
		config.set(prefix + "y", null);
		config.set(prefix + "z", null);
		plugin.saveConfig();
	}
	
	public void delete() {
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		config.set(this.getConfigPrefix(), null);
		plugin.saveConfig();
	}
	
	private String getConfigPrefix() {
		return "prisons." + this.name;
	}
}
