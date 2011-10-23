package net.marcuswhybrow.minecraft.law.prison;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Prison {
	private String name;
	private HashMap<String, PrisonCell> cells;
	private LawWorld lawWorld;
	private PrisonCellDefault defaultCell = null;
	private Location exitPoint = null;
	private HashMap<String, PrisonCell> prisoners;
	
	public Prison(LawWorld lawWorld, String name) {
		this(lawWorld, name, true);
	}
	
	public Prison(LawWorld lawWorld, String name, boolean save) {
		this.name = name;
		this.cells = new HashMap<String, PrisonCell>();
		this.prisoners = new HashMap<String, PrisonCell>();
		this.lawWorld = lawWorld;
		
		if (save) {
			// Save the new name
			Plugin plugin = Law.get().getPlugin();
			FileConfiguration config = plugin.getConfig();
			config.set(this.getConfigPrefix() + ".name", this.name);
			plugin.saveConfig();
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void setDefaultCell(Location cellLocation) {
		this.setDefaultCell(cellLocation, true);
	}
	
	public void setDefaultCell(Location cellLocation, boolean save) {
		this.defaultCell = new PrisonCellDefault(this, cellLocation, save);
	}
	
	public void setExitPoint(Location location) {
		this.setExitPoint(location, true);
	}
	
	public void setExitPoint(Location location, boolean save) {
		this.exitPoint = location;
		
		// Save the exit point to file
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		config.set(getConfigPrefix() + ".exit_point.location.x", location.getX());
		config.set(getConfigPrefix() + ".exit_point.location.y", location.getY());
		config.set(getConfigPrefix() + ".exit_point.location.z", location.getZ());
		config.set(getConfigPrefix() + ".exit_point.location.pitch", location.getPitch());
		config.set(getConfigPrefix() + ".exit_point.location.yaw", location.getYaw());
		plugin.saveConfig();
	}
	
	public void addCell(String cellName, Location cellLocation) {
		this.addCell(cellName, cellLocation);
	}
	
	public void addCell(String cellName, Location cellLocation, boolean save) {
		cells.put(cellName, new PrisonCell(this, cellName, cellLocation));
	}
	
	public void removeCell(String cellName) {
		PrisonCell cell = cells.remove(name);
		if (cell != null) {
			cell.delete();
		}
	}
	
	public void delete() {
		lawWorld.removePrison(this);
		
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		config.set(this.getConfigPrefix(), null);
		plugin.saveConfig();
	}
	
	public boolean imprisonPlayer(String playerName, String cell) {
		return this.imprisonPlayer(playerName, cell, true);
	}
	
	public boolean imprisonPlayer(String playerName, String cellName, boolean save) {
		PrisonCell cell = (cellName == null) ? defaultCell : this.cells.get(cellName);
		
		if (cell == null) {
			return false;
		}
		
		cell.imprisonPlayer(playerName, save);
		this.prisoners.put(playerName, cell);
		return true;
	}
	
	public boolean hasCell(String cellName) {
		if (cellName == null) {
			return defaultCell != null;
		} else {
			return this.cells.containsKey(cellName);
		}
	}
	
	public String getConfigPrefix() {
		return "worlds." + this.lawWorld.getName() + ".prisons." + this.getName();
	}
	
	public PrisonCell getPrisonCellForPlayer(String playerName) {
		return this.prisoners.get(playerName);
	}
	
	public boolean isOperational() {
		return this.defaultCell != null && this.exitPoint != null;
	}
	
	public boolean hasDefaultCell() {
		return this.defaultCell != null;
	}
	
	public boolean hasExitPoint() {
		return this.exitPoint != null;
	}
}
