package net.marcuswhybrow.minecraft.law.interfaces;

import java.util.Set;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

public interface PrisonerContainer {
	
	/**
	 * Sets the parent container for this container.
	 * 
	 * @param prisonerContiainer The parent container
	 */
	public void setParentPrisonerContainer(PrisonerContainer prisonerContiainer);
	
	/**
	 * Gets the parent container for this container.
	 * 
	 * @return The parent container
	 */
	public PrisonerContainer getParentPrisonerContainer();	

	/**
	 * Adds a player to the prisoner list of the PrisonerContainer.
	 * 
	 * @param detainee The detainee to add to this prison
	 */
	public void addDetainee(PrisonDetainee detainee);
	
	/**
	 * Removes a player completely from the containers lists.
	 * 
	 * @param detaineeName The player name to effect
	 */
	public void removeDetainee(String detaineeName);
	
	/**
	 * Get a {@link PrisonDetainee} instance of for the player
	 * name if that player is imprisoned in this container.
	 * 
	 * @param detaineeName The name of the player to get a {@link PrisonDetainee} instance of
	 * @return The {@link PrisonDetainee} instance or null if that player is not detained within this prisoner container
	 */
	public PrisonDetainee getDetainee(String detaineeName);
	
	/**
	 * Get the set of {@link PrisonDetainee} instances which represent the
	 * prisoners in this prison container.
	 * 
	 * @return The set of {@link PrisonDetainee} instances
	 */
	public Set<PrisonDetainee> getDetainees();
	
	/**
	 * Returns true if the prisoner container has the prisoner.
	 * 
	 * @param detaineeName The name of the prisoner to check for
	 * @return True if the player is detianed within this prison, false otherwise
	 */
	public boolean hasPrisoner(String detaineeName);
	
	/**
	 * Determines if this prisoner container has any prisoners.
	 * 
	 * @return True if there are 1 or more detainees, false if there are none
	 */
	public boolean hasPrisoners();
}
