package net.marcuswhybrow.minecraft.law;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.marcuswhybrow.minecraft.law.interfaces.PrisonerContainer;
import net.marcuswhybrow.minecraft.law.interfaces.Referenceable;
import net.marcuswhybrow.minecraft.law.interfaces.Saveable;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

public abstract class Entity implements Referenceable, PrisonerContainer, Saveable {
	
	private Map<String, PrisonCell> prisoners;
	private String name;
	private Map<String, Boolean> changeMap;
	private PrisonerContainer parent;
	private Set<PrisonerContainer> children;
	private String configPrefix;
	
	public Entity() {
		this.prisoners = new HashMap<String, PrisonCell>();
		this.name = null;
		this.parent = null;
		this.children = null;
		this.changeMap = new HashMap<String, Boolean>();
	}

	@Override
	public abstract boolean imprisonPlayer(String playerName);

	@Override
	public void imprisonPlayers(Set<String> playerNames) {
		if (playerNames == null) {
			throw new IllegalArgumentException("Player names set cannot be null");
		}
		
		for (String playerName : playerNames) {
			this.imprisonPlayer(playerName);
		}
	}

	@Override
	public void freePlayer(String playerName) {
		PrisonCell cell = this.getPrisonerCell(playerName);
		if (cell == null) {
			return;
		}
		
		cell.removePrisoner(playerName);
	}

	@Override
	public void freePlayers(Set<String> playerNames) {
		if (playerNames == null) {
			throw new IllegalArgumentException("Player names set cannot be null");
		}
		
		for (String playerName : playerNames) {
			this.freePlayer(playerName);
		}
	}

	@Override
	public void freePlayers() {
		this.freePlayers(this.prisoners.keySet());
	}

	@Override
	public Set<String> getPrisoners() {
		return this.prisoners.keySet();
	}
	
	@Override
	public void setParentPrisonerContainer(PrisonerContainer prisonerContiainer) {
		this.parent = prisonerContiainer;
	}

	@Override
	public PrisonerContainer getParentPrisonerContainer() {
		return this.parent;
	}
	
	@Override
	public void addChildPrisonerContainer(PrisonerContainer prisonerContiainer) {
		this.children.add(prisonerContiainer);
	}
	
	@Override
	public void removeChildPrisonerContainer(PrisonerContainer prisonerContiainer) {
		this.children.remove(prisonerContiainer);
	}

	@Override
	public Set<PrisonerContainer> getChildPrisonerContainers() {
		return this.children;
	}

	@Override
	public void addPrisoner(String playerName, PrisonCell cell) {
		if (this.parent != null) {
			this.parent.addPrisoner(playerName, cell);
		}
		this.prisoners.put(playerName.toLowerCase(), cell);
		setChanged("prisoners");
	}

	@Override
	public void removePrisoner(String playerName) {
		if (this.parent != null) {
			this.parent.removePrisoner(playerName);
		}
		this.prisoners.remove(playerName.toLowerCase());
		setChanged("prisoners");
	}

	@Override
	public PrisonCell getPrisonerCell(String playerName) {
		return this.prisoners.get(playerName.toLowerCase());
	}

	@Override
	public boolean hasPrisoner(String playerName) {
		return this.prisoners.containsKey(playerName.toLowerCase());
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
		setChanged("name");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public abstract void onSetup();
	
	@Override
	public void setConfigPrefix(String configPrefix) {
		this.configPrefix = configPrefix;
	}

	@Override
	public String getConfigPrefix() {
		return this.configPrefix;
	}
	
	public void save() {
		this.save(false);
	}
	
	@Override
	public final void save(boolean forceFullSave) {
		if (Law.get().isActive() == false) {
			return;
		}
		
		this.onSave(forceFullSave);
		
		this.configSave();
		
		this.changeMap.clear();
	}
	
	public abstract void onSave(boolean forceFullSave);
	
	@Override
	public void setup() {
		if (Law.get().isActive() == false) {
			this.onSetup();
		}
	}
	
	public void setChanged(String sectionName) {
		this.setChanged(sectionName, true);
	}
	
	@Override
	public void setChanged(String name, boolean state) {
		if (Law.get().isActive()) {
			this.changeMap.put(name, state);
		}
	}
	
	@Override
	public boolean isChanged(String name) {
		if (Law.get().isActive() == false) {
			return false;
		}
		
		Boolean state = changeMap.get(name);
		if (state == null) {
			return false;
		}
		
		return state;
	}
	
	@Override
	public void delete() {
		// Remove from configuration file
		this.configSet("", null);
		this.configSave();
	}
	
	@Override
	public void configSet(String relativePath, Object o) {
		Law.get().getPlugin().getConfig().set(getConfigPrefix() + (relativePath.length() > 0 ? "." : "") + relativePath, o);
	}
	
	@Override
	public void configSave() {
		Law.get().getPlugin().saveConfig();
	}

}
