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
	 * May be called when a player is online or offline.
	 * 
	 * @param playerName The name of the player which is imprisoned
	 * @param cell The PrisonCell in which this player is actually imprisoned within
	 */
	public void addPrisoner(String playerName, PrisonCell cell);
	
	/**
	 * Transitions a player into the regular prisoners list.
	 * May only be called when a player is online. This indicates
	 * that the player has had the in-game modifications applied
	 * which are required of a prisoner.
	 * 
	 * @param playerName The player to effect
	 */
	public void securePrisoner(String playerName);
	
	/**
	 * Transitions a player into the "prisoners being released" list.
	 * May be called when a player is online or offline.
	 * 
	 * @param playerName The name of the player to effect
	 */
	public void releasePrisoner(String playerName);
	
	/**
	 * Removes a player completely from the containers lists.
	 * May only be called when a player is online. This indicates
	 * that the player has has the in-game modifications applied
	 * which are required to restore full funtionality to a once
	 * imprisoned player.
	 * 
	 * @param playerName The player name to effect
	 */
	public void removePrisoner(String playerName);
	
	public PrisonCell getPrisonerCell(String playerName);
	
	public boolean hasUnsecuredPrisoner(String playerName);
	public boolean hasUnreleasedPrisoner(String playerName);
	public boolean hasPrisoner(String playerName);
	
	public boolean hasPrisoners();
}
