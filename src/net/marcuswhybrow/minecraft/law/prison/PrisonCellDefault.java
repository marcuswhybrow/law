package net.marcuswhybrow.minecraft.law.prison;

import org.bukkit.Location;

/**
 * 
 * @author marcus
 *
 */
public class PrisonCellDefault extends PrisonCell {
	
	public PrisonCellDefault(Prison prison, Location location) {
		this(prison, location, true);
	}
	
	public PrisonCellDefault(Prison prison, Location location, boolean save) {
		super(prison, "default_cell", location, save);
	}
	
	@Override
	protected String getConfigPrefix() {
		return this.prison.getConfigPrefix() + ".default_cell";
	}
}