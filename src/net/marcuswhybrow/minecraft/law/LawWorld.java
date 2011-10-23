package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;

import net.marcuswhybrow.minecraft.law.exceptions.IllegalNameException;
import net.marcuswhybrow.minecraft.law.exceptions.PrisonAlreadyExistsException;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class LawWorld {
	private World bukkitWorld;
	private HashMap<String, Prison> prisons;
	private HashMap<String, Prison> selectedPrisons;
	private HashMap<String, PrisonCell> prisoners;
	
	public LawWorld(World bukkitWorld) {
		this.bukkitWorld = bukkitWorld;
		prisons = new HashMap<String, Prison>();
		selectedPrisons = new HashMap<String , Prison>();
		prisoners = new HashMap<String, PrisonCell>();
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
		}
		return prison;
	}
	
	public void addPrison(Prison prison) {
		prisons.put(prison.getName(), prison);
	}
	
	public Prison removePrison(Prison prison) {
		return prisons.remove(prison.getName());
	}
	
	public Prison setSelectedPrison(String playerName, String name) { 
		return setSelectedPrison(playerName, name, true);
	}
	
	public Prison setSelectedPrison(String playerName, String name, boolean save) {
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		
		Prison prison = null;
		
		if (name == null) {
			selectedPrisons.remove(playerName);
			if (save) {
				// Save the selected prison
				config.set("worlds." + this.getName() + ".active_prisons." + playerName, null);
			}
		} else {
			prison = prisons.get(name);
			if (prison != null) {
				selectedPrisons.put(playerName, prison);
				
				if (save) {
					// Save the selected prison
					config.set("worlds." + this.getName() + ".active_prisons." + playerName, prison.getName());
				}
			}
		}
		
		plugin.saveConfig();
		
		return prison;
	}
	
	public Prison getSelectedPrison(String playerName) {
		return selectedPrisons.get(playerName);
	}
	
	public Prison[] getPrisons() {
		return prisons.values().toArray(new Prison[prisons.size()]);
	}
	
	public boolean imprisonPlayer(String playerName, String prisonName, String cellName) {
		return this.imprisonPlayer(playerName, prisonName, cellName, true);
	}
	
	public boolean imprisonPlayer(String playerName, String prisonName, String cellName, boolean save) {
		if (this.prisoners.containsKey(playerName)) {
			// This player is already imprisoned on this world
			return false;
		}
		
		Prison prison = this.getPrison(prisonName);
		
		if (prison == null) {
			// A prison with that name does not exist
			return false;
		}
		
		if (prison.imprisonPlayer(playerName, cellName, save)) {
			// The player has been successfully imprisoned
			this.prisoners.put(playerName, prison.getPrisonCellForPlayer(playerName));
			return true;
		}
		
		// The player could not be imprisoned because that cell does not exist
		return false;
	}
	
	public boolean isPlayerImprisoned(String playerName) {
		return this.prisoners.containsKey(playerName);
	}
}
