package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class Saveable {
	protected boolean isActive;
	protected HashMap<String, Boolean> changeMap = new HashMap<String, Boolean>();
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void save() {
		this.save(false);
	}
	
	public void save(boolean forceFullSave) {
		this.configSave();
	}
	
	public void setChanged(String name) {
		this.setChanged(name, true);
	}
	
	public void setChanged(String name, boolean state) {
		if (isActive) {
			this.changeMap.put(name, state);
		}
	}
	
	public boolean isChanged(String name) {
		if (!isActive) {
			return false;
		}
		
		Boolean state = changeMap.get(name);
		if (state != null) {
			return this.changeMap.get(name);
		}
		return false;
	}
	
	public abstract String getConfigPrefix();
	
	public void delete() {
		// Remove from configuration file
		this.configSet("", null);
		this.configSave();
	}
	
	public void configSet(String relativePath, Object o) {
		Law.get().getPlugin().getConfig().set(getConfigPrefix() + (relativePath.length() > 0 ? "." : "") + relativePath, o);
		Law.get().logMessage(getConfigPrefix() + (relativePath.length() > 0 ? "." : "") + relativePath + " - " + o);
	}
	
	public void configSave() {
		Law.get().getPlugin().saveConfig();
	}
}
