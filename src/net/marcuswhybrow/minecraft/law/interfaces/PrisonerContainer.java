package net.marcuswhybrow.minecraft.law.interfaces;

import java.util.Set;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

public interface PrisonerContainer {
	
	public boolean imprisonPlayer(String playerName);
	public void imprisonPlayers(Set<String> playerNames);
	
	public void freePlayer(String playerName);
	public void freePlayers(Set<String> playerNames);
	public void freePlayers();
	
	public Set<String> getPrisoners();
	
	public void setParentPrisonerContainer(PrisonerContainer prisonerContiainer);
	public PrisonerContainer getParentPrisonerContainer();
	
	public void addChildPrisonerContainer(PrisonerContainer prisonerContiainer);
	public void removeChildPrisonerContainer(PrisonerContainer prisonerContiainer);
	public Set<PrisonerContainer> getChildPrisonerContainers();
	

	/**
	 * Adds a player to the prisoner list of the PrisonerContainer.
	 * 
	 * @param playerName The name of the player which is imprisoned
	 * @param cell The PrisonCell in which this player is actually imprisoned within
	 */
	public void addPrisoner(String playerName, PrisonCell cell);
	public void removePrisoner(String playerName);
	public PrisonCell getPrisonerCell(String playerName);
	public boolean hasPrisoner(String playerName);
}
