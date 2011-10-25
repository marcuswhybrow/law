package net.marcuswhybrow.minecraft.law.prison;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.Plugin;
import net.marcuswhybrow.minecraft.law.Saveable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * 
 * @author marcus
 *
 */
public class PrisonCell extends Saveable {
	protected String name;
	protected Prison prison;
	protected HashSet<String> imprisonedPlayers;
	protected Location location;
	
	public static final String DEFAULT_NAME = "default";
	
	public PrisonCell(Prison prison, String name, Location location) {
		this(prison, name, location, true);
	}
	
	public PrisonCell(Prison prison, String name, Location location, boolean isActive) {
		this.prison = prison;
		this.name = name;
		this.imprisonedPlayers = new HashSet<String>();
		this.location = location;
		this.setActive(isActive);
		
		this.setChanged("name", isActive);
		this.setChanged("imprisonedPlayers", false);
		this.setChanged("location", isActive);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setLocation(Location location) {
		this.location = location;
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
	
	public boolean imprisonPlayer(String playerName) {
		if (!getPrison().isOperational()) {
			return false;
		}
		
		playerName = playerName.toLowerCase();
		
		this.imprisonedPlayers.add(playerName);
		
		Player player = Bukkit.getPlayerExact(playerName);
		
		if (isActive && player != null) {
			player.teleport(this.location);
		}
		
		this.setChanged("imprisonedPlayers");
		
		return true;
	}
	
	public boolean freePlayer(String playerName) {
		if (!getPrison().isOperational()) {
			return false;
		}

		// Standardise on lower case names
		playerName = playerName.toLowerCase();
		
		// Free the player
		boolean wasImprisoned = imprisonedPlayers.remove(playerName);
		
		if (wasImprisoned) {
			// If the player was indeed imprisoned here
			
			// Teleport the player if they are online right now
			Player player = Bukkit.getPlayerExact(playerName);
			if (isActive && player != null) {
				player.teleport(this.getPrison().getExitPoint());
			}
			
			// Since the player was removed from the imprisonedPlayers set
			// mark this data section as changed
			setChanged("imprisonedPlayers");
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		
		return imprisonedPlayers.contains(playerName);
	}
	
	@Override
	public String getConfigPrefix() {
		return this.prison.getConfigPrefix() + ".cells."+ this.getName();
	}
	
	@Override
	public void save(boolean forceFullSave) {
		Law.get().logMessage("saving PrisonCell - " + isActive);
		if (!isActive) {
			// If the model is not active yet it cannot save to file
			return;
		}
		
		if (forceFullSave) {
			// For a full save first clean this node
			configSet("", null);
		}
		
		if (forceFullSave || isChanged("name")) {
			configSet("name", this.getName());
		}
		if (forceFullSave || isChanged("imprisonedPlayers")) {
			Law.get().logMessage("saving that shit");
			configSet("imprisoned_players", Arrays.asList(this.getImprisonedPlayers()));
		}
		if (forceFullSave || isChanged("location")) {
			if (location != null) {
				configSet("location.x", location.getX());
				configSet("location.y", location.getY());
				configSet("location.z", location.getZ());
				configSet("location.pitch", location.getPitch());
				configSet("location.yaw", location.getYaw());
			}
		}
		
		super.save(forceFullSave);
	}
}