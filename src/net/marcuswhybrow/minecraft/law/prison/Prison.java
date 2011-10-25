package net.marcuswhybrow.minecraft.law.prison;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.Plugin;
import net.marcuswhybrow.minecraft.law.Saveable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sun.tools.javac.code.Attribute.Array;

public class Prison extends Saveable {
	/** The in-game name of this prison */
	private String name;
	/** A reference to all the cells of this prison */
	private HashMap<String, PrisonCell> cells;
	/** The LawWorld this prison belongs to */
	private LawWorld lawWorld;
	/** The location where freed players transport to */
	private Location exitPoint = null;
	/** An in-memory only account of imprisoned players and the cells they are in */
	private HashMap<String, PrisonCell> prisoners;
	
	public Prison(LawWorld lawWorld, String name) {
		this(lawWorld, name, true);
	}
	
	public Prison(LawWorld lawWorld, String name, boolean isActive) {
		this.name = name;
		this.cells = new HashMap<String, PrisonCell>();
		this.prisoners = new HashMap<String, PrisonCell>();
		this.lawWorld = lawWorld;
		this.setActive(isActive);
		
		setChanged("name", isActive);
		setChanged("defaultCell", false);
		setChanged("exitPoint", false);
		setChanged("cells", false);
	}
	
	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);
		
		Iterator<PrisonCell> it = cells.values().iterator();
		while (it.hasNext()) {
			it.next().setActive(isActive);
		}
	}
	
	/**
	 * Gets the in-game name used to reference this prison.
	 * 
	 * @return The name of this prison
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Sets the exit point for this prison, which is where freed
	 * players will appear.
	 * 
	 * @param location The location at which freed players should appear.
	 */
	public void setExitPoint(Location location) {
		this.exitPoint = location;
		
		if (isActive) {
			this.changeMap.put("exitPoint", true);
		}
	}
	
	/**
	 * Creates a new PrisonCell and adds it to the cells for this prison.
	 * If a cell with that given name already exists it will be overridden.
	 * 
	 * @param cellName The name of the new cell.
	 * @param cellLocation The location the new cell should use.
	 * @return The created PrisonCell instance
	 */
	public PrisonCell createCell(String cellName, Location cellLocation) {
		// If a cell of that name already exists in this prison, remove it
		this.removeCell(cellName);
		
		// Create and add the new PrisonCell instance 
		PrisonCell cell = new PrisonCell(this, cellName, cellLocation, isActive);
		cells.put(cellName, cell);
		setChanged("cells");
		
		return cell;
	}
	
	/**
	 * Removes the PrisonCell by name from this prison's cells. Does nothing
	 * if a cell does not exist by that name.
	 * 
	 * @param cellName The name of the cell to remove
	 * @return The PrisonCell removed or null.
	 */
	public PrisonCell removeCell(String cellName) {
		
		PrisonCell cell = cells.remove(cellName);
		
		if (cell != null) {
			cell.delete();
			setChanged("cells");
		}
		return cell;
	}
	
	/**
	 * Gets a cell owned by this prison by name.
	 * 
	 * @param cellName The name of the cell to get
	 * @return The PrisonCell instance or null
	 */
	public PrisonCell getCell(String cellName) {
		return cells.get(cellName);
	}
	
	/**
	 * Determines if this prison has a cell with the provided name
	 * 
	 * @param cellName The name of the cell to check for
	 * @return True if the cell exists, false otherwise
	 */
	public boolean hasCell(String cellName) {
		return cells.containsKey(cellName);
	}
	
	/**
	 * Imprisons a player with a cell inside of this prison.
	 * 
	 * @param playerName The name of the player to imprison
	 * @param cellName The name of the cell to imprison the player within
	 * @return True if the player could be imprisoned, false if not
	 */
	public boolean imprisonPlayer(String playerName, String cellName) {
		if (!isOperational()) {
			// If the prison in not operational, it cannot accept prisoners
			return false;
		}
		
		// Standardise on lower case names
		playerName = playerName.toLowerCase();
		
		// Get the cell to imprison the player in
		PrisonCell cell = this.cells.get(cellName);
		
		if (cell == null) {
			// If that cell does not exist, we cannot imprison the player
			return false;
		}
		
		// Otherwise imprison the player
		cell.imprisonPlayer(playerName);
		// Keep a local record of the imprisonment
		this.prisoners.put(playerName, cell);
		setChanged("cells");
		
		return true;
	}
	
	/**
	 * Remove a player from being imprisoned in their cell and this prison
	 * 
	 * @param playerName The name of the player to free
	 * @return True if the player could be freed, false if the player was not imprisoned here
	 */
	public boolean freePlayer(String playerName) {
		if (!isOperational()) {
			return false;
		}
		
		playerName = playerName.toLowerCase();
		
		PrisonCell cell = prisoners.remove(playerName);
		if (cell != null) {
			boolean couldBeFreed = cell.freePlayer(playerName);
			if (couldBeFreed) {
				setChanged("cells");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getConfigPrefix() {
		return lawWorld.getConfigPrefix() + ".prisons." + this.getName();
	}
	
	public PrisonCell getPrisonCellForPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		
		return this.prisoners.get(playerName);
	}
	
	public boolean isOperational() {
		return hasDefaultCell() && hasExitPoint();
	}
	
	public boolean hasDefaultCell() {
		return cells.containsKey(PrisonCell.DEFAULT_NAME);
	}
	
	public boolean hasExitPoint() {
		return this.exitPoint != null;
	}
	
	public Location getExitPoint() {
		return this.exitPoint;
	}
	
	@Override
	public void save(boolean forceFullSave) {
		if (!isActive()) {
			// If the model is not active yet it cannot save to file
			return;
		}
		
		if (forceFullSave) {
			// For a full save first clean this node
			configSet("", null);
		}
		
		if (forceFullSave || isChanged("name")) {
			configSet("name", getName());
		}
		if (forceFullSave || isChanged("exitPoint")) {
			if (exitPoint != null) {
				configSet("exit_point.location.x", exitPoint.getX());
				configSet("exit_point.location.y", exitPoint.getY());
				configSet("exit_point.location.z", exitPoint.getZ());
				configSet("exit_point.location.pitch", exitPoint.getPitch());
				configSet("exit_point.location.yaw", exitPoint.getYaw());
			}
		}
		if (forceFullSave || isChanged("cells")) {
			Iterator<PrisonCell> it = cells.values().iterator();
			while (it.hasNext()) {
				it.next().save(forceFullSave);
			}
		}
		
		super.save(forceFullSave);
	}
}
