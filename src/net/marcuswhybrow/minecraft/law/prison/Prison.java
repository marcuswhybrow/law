package net.marcuswhybrow.minecraft.law.prison;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import net.marcuswhybrow.minecraft.law.Entity;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.interfaces.PrisonListener;
import net.marcuswhybrow.minecraft.law.serializable.SerializableLocation;
import net.marcuswhybrow.minecraft.law.utilities.Validate;

import org.bukkit.Location;

public class Prison extends Entity implements Serializable {
	private static final long serialVersionUID = 272137797127892865L;
	/** A reference to all the cells of this prison */
	private HashMap<String, PrisonCell> cells;
	/** The LawWorld this prison belongs to */
	private LawWorld lawWorld;
	/** The location where freed players transport to */
	private SerializableLocation exitPoint;
	
	private int hashCode;
	
	public Prison(LawWorld lawWorld, String name) {
		this.cells = new HashMap<String, PrisonCell>();
		this.lawWorld = lawWorld;
		
		setName(name);
		setParentPrisonerContainer(this.lawWorld);
		
		for (PrisonListener listener : Law.get().getPrisonListeners()) {
			listener.onPrisonCreate(this);
		}
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
		this.exitPoint = new SerializableLocation(location);
		
		for (PrisonListener listener : Law.get().getPrisonListeners()) {
			listener.onPrisonSetExit(this);
		}
	}
	
	public void addCell(PrisonCell cell) {
		cells.put(cell.getName().toLowerCase(), cell);
	}
	
	/**
	 * Removes the PrisonCell by name from this prison's cells. Does nothing
	 * if a cell does not exist by that name.
	 * 
	 * @param cellName The name of the cell to remove
	 * @return The PrisonCell removed or null.
	 */
	public PrisonCell removeCell(String cellName) {
		
		return cells.remove(cellName.toLowerCase());
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
		return this.exitPoint.getLocation();
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
	
	public void delete() {
		for (PrisonListener listener : Law.get().getPrisonListeners()) {
			listener.onPrisonDelete(this);
		}
	}
}
