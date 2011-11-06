package net.marcuswhybrow.minecraft.law.prison;

import java.io.Serializable;

import net.marcuswhybrow.minecraft.law.Entity;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.serializable.SerializableLocation;

import org.bukkit.Location;

/**
 * 
 * @author marcus
 *
 */
public class PrisonCell extends Entity implements Serializable {
	private static final long serialVersionUID = 275245421338504910L;
	private Prison prison;
	
	public static final String DEFAULT_NAME = "default";
	
	private int hashCode;
	private SerializableLocation location;
	
	public PrisonCell(Prison prison, String name) {
		this(prison, name, null);
	}
	
	public PrisonCell(Prison prison, String name, Location location) {
		this.prison = prison;
		this.location = new SerializableLocation(location);
		
		setName(name);
		setParentPrisonerContainer(this.prison);
	}
	
	public void setLocation(Location location) {
		this.location.setLocation(location);
	}
	
	public Location getLocation() {
		return this.location.getLocation();
	}
	
	public Prison getPrison() {
		return this.prison;
	}
	
	@Override
	public boolean imprisonPlayer(String playerName) {
		if (Law.get().isActive() && !getPrison().isOperational()) {
			return false;
		}
		
		this.addPrisoner(playerName.toLowerCase(), this);
		
		return true;
	}
	
	@Override
	public int hashCode() {
		if (hashCode == 0) {
			hashCode = ("PRISONCELL" + this.getPrison().getName() + this.getName()).hashCode();
		}
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PrisonCell) {
			PrisonCell other = (PrisonCell) obj;
			return this.getName() == other.getName() && this.getPrison() == other.getPrison();
		}
		return false;
	}
}