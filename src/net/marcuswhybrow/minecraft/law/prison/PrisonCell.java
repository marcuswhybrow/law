package net.marcuswhybrow.minecraft.law.prison;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.marcuswhybrow.minecraft.law.Entity;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.utilities.Config;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 
 * @author marcus
 *
 */
public class PrisonCell extends Entity {
	protected Prison prison;
	protected Location location;
	
	public static final String DEFAULT_NAME = "default";
	
	private int hashCode;
	
	public PrisonCell(Prison prison, String name) {
		this(prison, name, null);
	}
	
	public PrisonCell(Prison prison, String name, Location location) {
		this.prison = prison;
		this.setLocation(location);
		
		setName(name);
		setParentPrisonerContainer(this.prison);
		setConfigPrefix(this.prison.getConfigPrefix() + ".cells."+ this.getName());
	}
	
	public void setLocation(Location location) {
		this.location = location;
		setChanged("location");
	}
	
	public Location getLocation() {
		return this.location;
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
	public void onSave(boolean forceFullSave) {
		MessageDispatcher.consoleInfo("PrisonCell onSave()");
		if (forceFullSave) {
			// For a full save first clean this node
			configSet("", null);
		}
		
		if (forceFullSave || isChanged("name")) {
			configSet("name", this.getName());
		}
		if (forceFullSave || isChanged("prisoners")) {
			configSet("prisoners", new ArrayList<String>(this.getPrisoners()));
		}
		if (forceFullSave || isChanged("location")) {
			Config.setLocation(getConfigPrefix() + ".location", location);
		}
	}

	@Override
	public void onSetup() {
		FileConfiguration config = Law.get().getPlugin().getConfig();
		
		// Get the cells imprisoned players
		@SuppressWarnings("unchecked")
		List<String> prisonerList  = (List<String>) config.getList(getConfigPrefix() + ".prisoners");
		MessageDispatcher.consoleInfo("setup PrisonCell: " + prisonerList);
		if (prisonerList != null) {
			for (String s : prisonerList) {
				MessageDispatcher.consoleInfo("  prisoner: " + s);
			}
			
			// Imprison the players in this cell right away
			this.imprisonPlayers(new HashSet<String>(prisonerList));
		}
		
		// Get the cell location
		setLocation(Config.getLocation(getConfigPrefix() + ".location"));
	}
	
	@Override
	public void setChanged(String section, boolean state) {
		super.setChanged(section, state);
		this.getPrison().setChanged("cells");
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