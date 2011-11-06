package net.marcuswhybrow.minecraft.law.interfaces;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

/**
 * An interface for objects which respond to changes
 * in a PrisonCell.
 * 
 * @author Marcus Whybrow
 *
 */
public interface PrisonCellListener {
	/**
	 * Called after a prison cell is created.
	 * 
	 * @param cell The cell which has being created
	 * @param sender The person who created this cell
	 */
	public void onPrisonCellCreate(PrisonCell cell);
	
	/**
	 * Called before a prison cell is deleted.
	 * 
	 * @param cell The cell which will be deleted
	 * @param sender The person who deleted this cell
	 */
	public void onPrisonCellDelete(PrisonCell cell);
	
	/**
	 * Called when a cell's location is moved
	 * 
	 * @param cell The cell which has had it's location moved
	 * @param sender The person who moved the cell's location
	 */
	public void onPrisonCellMove(PrisonCell cell);
}
