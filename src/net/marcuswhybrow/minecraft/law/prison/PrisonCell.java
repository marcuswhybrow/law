package net.marcuswhybrow.minecraft.law.prison;

import java.util.Arrays;
import java.util.HashSet;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * 
 * @author marcus
 *
 */
public class PrisonCell {
	protected String name;
	protected Prison prison;
	protected HashSet<String> imprisonedPlayers;
	protected Location location;
	
	public PrisonCell(Prison prison, String name, Location location) {
		this(prison, name, location, true);
	}
	
	public PrisonCell(Prison prison, String name, Location location, boolean save) {
		this.prison = prison;
		this.name = name;
		this.imprisonedPlayers = new HashSet<String>();
		this.location = location;
		
		if (save) {
			// Save the new cell
			Plugin plugin = Law.get().getPlugin();
			FileConfiguration config = plugin.getConfig();
			config.set(getConfigPrefix() + ".name", this.getName());
			config.set(getConfigPrefix() + ".location.x", location.getX());
			config.set(getConfigPrefix() + ".location.y", location.getY());
			config.set(getConfigPrefix() + ".location.z", location.getZ());
			config.set(getConfigPrefix() + ".location.pitch", location.getPitch());
			config.set(getConfigPrefix() + ".location.yaw", location.getYaw());
			plugin.saveConfig();
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void delete() {
		// Remove from configuration file
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		config.set(getConfigPrefix(), null);
		plugin.saveConfig();
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public Prison getPrison() {
		return this.prison;
	}
	
	public String[] getImprisonedPlayers() {
		return this.imprisonedPlayers.toArray(new String[this.imprisonedPlayers.size()]);
	}
	
	public void imprisonPlayer(String playerName) {
		this.imprisonPlayer(playerName);
	}
	
	public void imprisonPlayer(String playerName, boolean save) {
		this.imprisonedPlayers.add(playerName);
		
		Player player = Bukkit.getPlayerExact(playerName);
		
		if (player != null) {
			player.teleport(this.location);
		}
		
		if (save) {
			// Save the new cell
			Plugin plugin = Law.get().getPlugin();
			FileConfiguration config = plugin.getConfig();
			config.set(getConfigPrefix() + ".imprisoned_players", Arrays.asList(this.getImprisonedPlayers()));
			plugin.saveConfig();
		}
	}
	
	public void freePlayer(Player player) {
		this.freePlayer(player, true);
	}
	
	public void freePlayer(Player player, boolean save) {
		this.imprisonedPlayers.remove(player.getDisplayName());
		
		if (save) {
			// Save the new cell
			Plugin plugin = Law.get().getPlugin();
			FileConfiguration config = plugin.getConfig();
			config.set(getConfigPrefix() + ".imprisoned_players", this.getImprisonedPlayers());
			plugin.saveConfig();
		}
	}
	
	protected String getConfigPrefix() {
		return this.prison.getConfigPrefix() + ".cells."+ this.getName();
	}
}