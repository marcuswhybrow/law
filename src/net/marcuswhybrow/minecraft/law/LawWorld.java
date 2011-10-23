package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LawWorld {
	private World bukkitWorld;
	private HashMap<String, Prison> prisons;
	private HashMap<Player, Prison> selectedPrisons;
	
	public LawWorld(World bukkitWorld) {
		this.bukkitWorld = bukkitWorld;
		prisons = new HashMap<String, Prison>();
		selectedPrisons = new HashMap<Player , Prison>();
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
	
	public Prison setSelectedPrison(Player player, String name) { 
		return setSelectedPrison(player, name, true);
	}
	
	public Prison setSelectedPrison(Player player, String name, Boolean save) {
		Plugin plugin = Law.get().getPlugin();
		FileConfiguration config = plugin.getConfig();
		
		Prison prison = null;
		
		if (name == null) {
			selectedPrisons.remove(player);
			if (save) {
				// Save the selected prison
				config.set("worlds." + this.getName() + ".active_prisons." + player.getDisplayName(), null);
			}
		} else {
			prison = prisons.get(name);
			if (prison != null) {
				selectedPrisons.put(player, prison);
				
				if (save) {
					// Save the selected prison
					config.set("worlds." + this.getName() + ".active_prisons." + player.getDisplayName(), prison.getName());
				}
			}
		}
		
		plugin.saveConfig();
		
		return prison;
	}
	
	public Prison getSelectedPrison(Player player) {
		return selectedPrisons.get(player);
	}
	
	public Prison[] getPrisons() {
		return prisons.values().toArray(new Prison[prisons.size()]);
	}
}
