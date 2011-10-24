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

public class Prison extends Saveable {
	/** The in-game name of this prison */
	private String name;
	/** A reference to all the cells of this prison */
	private HashMap<String, PrisonCell> cells;
	/** The LawWorld this prison belongs to */
	private LawWorld lawWorld;
	/** The default cell for this prison */
	private PrisonCell defaultCell = null;
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
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void setExitPoint(Location location) {
		this.exitPoint = location;
		
		if (isActive) {
			this.changeMap.put("exitPoint", true);
		}
	}
	
	public PrisonCell createCellAsDefault(Location cellLocation) {
		defaultCell = createCell(PrisonCell.DEFAULT_NAME, cellLocation);
		return defaultCell;
	}
	
	public PrisonCell createCell(String cellName, Location cellLocation) {
		PrisonCell cell = new PrisonCell(this, cellName, cellLocation, isActive);
		cells.put(cellName, cell);
		setChanged("cells");
		return cell;
	}
	
	public void removeCell(String cellName) {
		PrisonCell cell = cells.remove(name);
		if (cell != null) {
			cell.delete();
			setChanged("cells");
		}
	}
	
	public boolean imprisonPlayer(String playerName, String cellName) {
		if (!isOperational()) {
			return false;
		}
		
		playerName = playerName.toLowerCase();
		
		PrisonCell cell = (cellName == PrisonCell.DEFAULT_NAME) ? defaultCell : this.cells.get(cellName);
		
		if (cell == null) {
			return false;
		}
		
		cell.imprisonPlayer(playerName);
		setChanged("cells");
		this.prisoners.put(playerName, cell);
		return true;
	}
	
	public boolean freePlayer(String playerName) {
		if (!isOperational()) {
			return false;
		}
		
		playerName = playerName.toLowerCase();
		
		PrisonCell cell = prisoners.remove(playerName);
		if (cell != null) {
			cell.freePlayer(playerName);
			setChanged("cells");
			return true;
		}
		return false;
	}
	
	public boolean hasCell(String cellName) {
		if (cellName == null) {
			return defaultCell != null;
		} else {
			return this.cells.containsKey(cellName);
		}
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
		return this.defaultCell != null && this.exitPoint != null;
	}
	
	public boolean hasDefaultCell() {
		return this.defaultCell != null;
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
