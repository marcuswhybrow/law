package net.marcuswhybrow.minecraft.law.interfaces;

import net.marcuswhybrow.minecraft.law.prison.Prison;

/**
 * An interface for objects which respond to changes
 * in a Prison object.
 * 
 * @author Marcus Whybrow
 *
 */
public interface PrisonListener {
	/**
	 * Called when a prison is created.
	 * 
	 * @param prison The newly created prison
	 */
	public void onPrisonCreate(Prison prison);
	
	/**
	 * Called when a prison is deleted.
	 * 
	 * @param prison The to be delete prison
	 */
	public void onPrisonDelete(Prison prison);
	
	/**
	 * Called when a prison has been selected for the
	 * purposes of using prison commands.
	 * 
	 * @param prison The prison which has been selected
	 */
	public void onPrisonSelected(Prison prison);
	
	/**
	 * Called when a prison has become deselected, which can happen
	 * because another prison has been selected, or this prison is being
	 * deleted.
	 * 
	 * @param prison The prison which is being deselected
	 */
	public void onPrisonDeselected(Prison prison);
	
	/**
	 * Called when a prison's exit point has been set.
	 * 
	 * @param prison The prison which has has it's exit point set
	 */
	public void onPrisonSetExit(Prison prison);
}
