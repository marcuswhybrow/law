package net.marcuswhybrow.minecraft.law;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.marcuswhybrow.minecraft.law.interfaces.PrisonListener;
import net.marcuswhybrow.minecraft.law.prison.Prison;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class LawWorld extends Entity implements Serializable {
	private static final long serialVersionUID = 6234852634237392865L;
	private String bukkitWorldName;
	private transient World bukkitWorld = null;
	private HashMap<String, Prison> prisons;
	private HashMap<String, Prison> selectedPrisons;
	private int hashCode;
	
	public LawWorld(World bukkitWorld) {
		this.bukkitWorld = bukkitWorld;
		this.bukkitWorldName = this.bukkitWorld.getName();
		prisons = new HashMap<String, Prison>();
		selectedPrisons = new HashMap<String , Prison>();
		
		setName(this.bukkitWorld.getName());
	}
	
	public boolean hasPrison(String prisonName) {
		return prisons.containsKey(prisonName);
	}
	
	public Prison getPrison(String name) {
		return prisons.get(name.toLowerCase());
	}
	
	public void addPrison(Prison prison) {
		prisons.put(prison.getName().toLowerCase(), prison);
	}
	
	public void deletePrison(Prison prison) {
		prisons.remove(prison.getName().toLowerCase());
		prison.delete();
		
		// If there is only one prison remaining
		if (prisons.size() == 1) {
			// Get that prison
			Prison lastPrison = prisons.values().iterator().next();
			// And set it as everyones selected prison
			for (String playerName : selectedPrisons.keySet()) {
				selectedPrisons.put(playerName, lastPrison);
			}
		}
		
		// Remove the the prison from any selectedPrison entries
		Iterator<Prison> it = selectedPrisons.values().iterator();
		while (it.hasNext()) {
			if (it.next() == prison) {
				it.remove();
			}
		}
	}
	
	public Prison setSelectedPrison(String playerName, String prisonName) {
		playerName = playerName.toLowerCase();
		Prison prison = null;
		
		Prison removedPrison = selectedPrisons.remove(playerName.toLowerCase());
		if (removedPrison != null) {
			
			// Notify listeners that a prison has been deselected
			for (PrisonListener listener : Law.get().getPrisonListeners()) {
				listener.onPrisonDeselected(removedPrison);
			}
		}
		
		if (prisonName != null) {
			prison = this.getPrison(prisonName.toLowerCase());
			if (prison != null) {
				selectedPrisons.put(playerName.toLowerCase(), prison);
				
				// Notify listeners that a prison has been selected
				for (PrisonListener listener : Law.get().getPrisonListeners()) {
					listener.onPrisonSelected(prison);
				}
			}
		}
		
		return prison;
	}
	
	public Prison getSelectedPrison(String playerName) {
		return selectedPrisons.get(playerName.toLowerCase());
	}
	
	public Collection<Prison> getPrisons() {
		return prisons.values();
	}
	
	public World getBukkitWorld() {
		if (this.bukkitWorld == null) {
			this.bukkitWorld = Bukkit.getWorld(this.bukkitWorldName);
		}
		return this.bukkitWorld;
	}

	@Override
	public boolean imprisonPlayer(String playerName) {
		// A world cannot imprison a player automatically
		return false;
	}
	
	@Override
	public int hashCode() {
		if (hashCode == 0) {
			hashCode = ("WORLD" + this.getName()).hashCode();
		}
		
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LawWorld) {
			LawWorld other = (LawWorld) obj;
			return this.getName() == other.getName();
		}
		return false;
	}
}
