package net.marcuswhybrow.minecraft.law;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.marcuswhybrow.minecraft.law.interfaces.PrisonerContainer;
import net.marcuswhybrow.minecraft.law.interfaces.Referenceable;
import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

public abstract class Entity implements Referenceable, PrisonerContainer, Serializable {
	private static final long serialVersionUID = -3452269165185206945L;
	/** A map of prisoner names to detainee instances */
	private Map<String, PrisonDetainee> detainees;
	/** The in-game name of this entity */
	private String name;
	private PrisonerContainer parent;
	
	public Entity() {
		this.detainees = new HashMap<String, PrisonDetainee>();
		this.name = null;
		this.parent = null;
	}

	@Override
	public Set<PrisonDetainee> getDetainees() {
		return new HashSet<PrisonDetainee>(this.detainees.values());
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
	public void addDetainee(PrisonDetainee detainee) {
		if (this.parent != null) {
			this.parent.addDetainee(detainee);
		}
		this.detainees.put(detainee.getName().toLowerCase(), detainee);
	}

	@Override
	public void removeDetainee(String detaineeName) {
		if (this.parent != null) {
			this.parent.removeDetainee(detaineeName);
		}
		this.detainees.remove(detaineeName.toLowerCase());
	}
	
	@Override
	public PrisonDetainee getDetainee(String detaineeName) {
		return this.detainees.get(detaineeName.toLowerCase());
	}

	@Override
	public boolean hasPrisoner(String detaineeName) {
		return this.detainees.containsKey(detaineeName.toLowerCase());
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean hasPrisoners() {
		return this.detainees.isEmpty() == false;
	}
}
