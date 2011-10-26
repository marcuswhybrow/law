package net.marcuswhybrow.minecraft.law.prison;

import java.util.Collection;
import java.util.HashMap;

import net.marcuswhybrow.minecraft.law.Entity;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.utilities.Config;
import net.marcuswhybrow.minecraft.law.utilities.Validate;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Prison extends Entity {
	/** A reference to all the cells of this prison */
	private HashMap<String, PrisonCell> cells;
	/** The LawWorld this prison belongs to */
	private LawWorld lawWorld;
	/** The location where freed players transport to */
	private Location exitPoint = null;
	
	private int hashCode;
	
	public Prison(LawWorld lawWorld, String name) {
		this.cells = new HashMap<String, PrisonCell>();
		this.lawWorld = lawWorld;
		
		setName(name);
		setParentPrisonerContainer(this.lawWorld);
		setConfigPrefix(lawWorld.getConfigPrefix() + ".prisons." + this.getName());
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	/**
	 * Sets the exit point for this prison, which is where freed
	 * players will appear.
	 * 
	 * @param location The location at which freed players should appear.
	 */
	public void setExitPoint(Location location) {
		this.exitPoint = location;
		
		if (Law.get().isActive()) {
			setChanged("exitPoint");
		}
	}
	
	public void addCell(PrisonCell cell) {
		cells.put(cell.getName().toLowerCase(), cell);
		setChanged("cells");
	}
	
	/**
	 * Removes the PrisonCell by name from this prison's cells. Does nothing
	 * if a cell does not exist by that name.
	 * 
	 * @param cellName The name of the cell to remove
	 * @return The PrisonCell removed or null.
	 */
	public PrisonCell removeCell(String cellName) {
		
		PrisonCell cell = cells.remove(cellName.toLowerCase());
		
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
		return cells.get(cellName.toLowerCase());
	}
	
	public Collection<PrisonCell> getCells() {
		return cells.values();
	}
	
	/**
	 * Determines if this prison has a cell with the provided name
	 * 
	 * @param cellName The name of the cell to check for
	 * @return True if the cell exists, false otherwise
	 */
	public boolean hasCell(String cellName) {
		return cells.containsKey(cellName.toLowerCase());
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
	public void onSave(boolean forceFullSave) {
		if (forceFullSave) {
			// For a full save first clean this node
			configSet("", null);
		}
		
		if (forceFullSave || isChanged("name")) {
			configSet("name", getName());
		}
		if (forceFullSave || isChanged("exitPoint")) {
			Config.setLocation(getConfigPrefix() + ".exit_point.location", exitPoint);
		}
		if (forceFullSave || isChanged("cells")) {
			if (cells.size() == 0) {
				// Writes an empty dictionary to the cells node
				configSet("cells.inside", null);
			} else {
				for (PrisonCell cell : cells.values()) {
					cell.save(forceFullSave);
				}
			}
		} else if (isChanged("prisoners")) {
			for (String playerName : this.getPrisoners()) {
				// forceFullSave is definitely false here
				this.getPrisonerCell(playerName).save(forceFullSave);
			}
		}
	}
	
	@Override
	public void setChanged(String section, boolean state) {
		super.setChanged(section, state);
		this.getLawWorld().setChanged("prisons");
	}

	@Override
	public void onSetup() {
		FileConfiguration config = Law.get().getPlugin().getConfig();
		
		setupExitPoint(config);
		setupCells(config);
	}
	
	private void setupCells(FileConfiguration config) {
		ConfigurationSection cells = config.getConfigurationSection(getConfigPrefix() + ".cells");
		
		if (cells != null) {
			for (String cellName : cells.getKeys(false)) {
				PrisonCell cell = new PrisonCell(this, cellName);
				addCell(cell);
				
				// Only setup the cell after adding it to the prison
				cell.setup();
			}
		}
	}
	
	private void setupExitPoint(FileConfiguration config) {
		if (config.contains(getConfigPrefix() + ".exit_point")) {
			Location location = Config.getLocation(getConfigPrefix() + ".exit_point.location");
			setExitPoint(location);
		}
	}
	
	public static boolean validateName(String prisonName) {
		return Validate.name(prisonName);
	}
	
	public LawWorld getLawWorld() {
		return this.lawWorld;
	}

	@Override
	public boolean imprisonPlayer(String playerName) {
		if (this.hasDefaultCell() == false) {
			return false;
		}
		
		PrisonCell cell = this.getCell(PrisonCell.DEFAULT_NAME);
		return cell.imprisonPlayer(playerName);
	}
	
	@Override
	public int hashCode() {
		if (hashCode == 0) {
			hashCode = ("PRISON" + this.getLawWorld().getName() + this.getName()).hashCode();
		}
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Prison) {
			Prison other = (Prison) obj;
			return this.getName() == other.getName() && this.getLawWorld() == other.getLawWorld();
		}
		return false;
	}
}
