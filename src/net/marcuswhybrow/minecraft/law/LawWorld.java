package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.marcuswhybrow.minecraft.law.exceptions.IllegalNameException;
import net.marcuswhybrow.minecraft.law.exceptions.PrisonAlreadyExistsException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class LawWorld extends Saveable {
	private World bukkitWorld;
	private HashMap<String, Prison> prisons;
	private HashMap<String, Prison> selectedPrisons;
	private HashMap<String, PrisonCell> prisoners;
	
	public LawWorld(World bukkitWorld) {
		this(bukkitWorld, true);
	}
	
	public LawWorld(World bukkitWorld, boolean isActive) {
		this.bukkitWorld = bukkitWorld;
		prisons = new HashMap<String, Prison>();
		selectedPrisons = new HashMap<String , Prison>();
		prisoners = new HashMap<String, PrisonCell>();
		
		this.setActive(isActive);
		
		setChanged("prisons", false);
		setChanged("selectedPrisons", false);
	}
	
	public String getName() {
		return this.bukkitWorld.getName();
	}
	
	public Prison createPrison(String name) throws PrisonAlreadyExistsException, IllegalNameException {
		if (prisons.containsKey(name)) {
			throw new PrisonAlreadyExistsException();
		} else {
			if (name.matches("[a-zA-Z0-9_-]+")) {
				Prison prison = new Prison(this, name);
				this.addPrison(prison);
				return prison;
			} else {
				throw new IllegalNameException();
			}
		}
	}
	
	public Prison getPrison(String name) {
		return prisons.get(name);
	}
	
	public Prison getOrCreatePrison(String name) {
		Prison prison = prisons.get(name);
		if (prison == null) {
			prison = new Prison(this, name);
			this.prisons.put(prison.getName(), prison);
			setChanged("prisons");
		}
		return prison;
	}
	
	public void addPrison(Prison prison) {
		prisons.put(prison.getName(), prison);
		setChanged("prisons");
	}
	
	public void removePrison(Prison prison) {
		prisons.remove(prison.getName());
		prison.delete();
		setChanged("prisons");
	}
	
	public Prison setSelectedPrison(String playerName, String name) {
		playerName = playerName.toLowerCase();
		Prison prison = null;
		
		if (name == null) {
			selectedPrisons.remove(playerName);
			setChanged("selectedPrisons");
		} else {
			prison = prisons.get(name);
			if (prison != null) {
				selectedPrisons.put(playerName, prison);
				setChanged("selectedPrisons");
			}
		}
		
		return prison;
	}
	
	public Prison getSelectedPrison(String playerName) {
		playerName = playerName.toLowerCase();
		
		return selectedPrisons.get(playerName);
	}
	
	public Prison[] getPrisons() {
		return prisons.values().toArray(new Prison[prisons.size()]);
	}
	
	public boolean imprisonPlayer(String playerName, String prisonName, String cellName) {
		playerName = playerName.toLowerCase();
		
		if (this.prisoners.containsKey(playerName)) {
			// This player is already imprisoned on this world
			return false;
		}
		
		Prison prison = this.getPrison(prisonName);
		
		if (prison == null) {
			// A prison with that name does not exist
			return false;
		}
		
		if (prison.imprisonPlayer(playerName, cellName)) {
			// The player has been successfully imprisoned
			this.prisoners.put(playerName, prison.getPrisonCellForPlayer(playerName));
			setChanged("prisons");
			return true;
		}
		
		// The player could not be imprisoned because the prison in non-operational or the cell does not exist
		return false;
	}
	
	public boolean freePlayer(String playerName) {
		playerName = playerName.toLowerCase();
		
		PrisonCell cell = prisoners.get(playerName);
		if (cell != null) {
			if (cell.getPrison().hasExitPoint()) {
				prisoners.remove(playerName);
				setChanged("prisons");
				// its important to call the Prison's freePlayer method (which calls the PrisonCell's)
				// so that all containers are notified.
				cell.getPrison().freePlayer(playerName);
				return true;
			}
		}
		return false;
	}
	
	public PrisonCell getPrisonCellForPlayer(String playerName) {
		playerName = playerName.toLowerCase();
		
		return prisoners.get(playerName);
	}
	
	public boolean isPlayerImprisoned(String playerName) {
		playerName = playerName.toLowerCase();
		
		return this.prisoners.containsKey(playerName);
	}

	@Override
	public void save(boolean forceFullSave) {
		if (!isActive()) {
			return;
		}
		
		if (forceFullSave) {
			// For a full save first clean this node
			configSet("", null);
		}
		
		if (forceFullSave || isChanged("selectedPrisons")) {
			// Because selected prisons is a HashMap its quicker to delete all lines
			// in the configuration and write them in again
			configSet("active_prisons", null);
			
			// Write out all the selected prisons to the configuration
			Iterator<Entry<String,Prison>> it = selectedPrisons.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String,Prison> entry = it.next();
				String playerName = entry.getKey();
				String prisonName = entry.getValue().getName();
				configSet("active_prisons." + playerName, prisonName);
			}
		}
		
		if (forceFullSave || isChanged("prisons")) {
			// Tell all child prisons to save also
			Iterator<Prison> it = prisons.values().iterator();
			while (it.hasNext()) {
				it.next().save(forceFullSave);
			}
		}
		
		super.save(forceFullSave);
	}

	@Override
	public String getConfigPrefix() {
		return "worlds." + this.getName();
	}
	
	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);
		
		Iterator<Prison> it = prisons.values().iterator();
		while (it.hasNext()) {
			it.next().setActive(isActive);
		}
	}
}
