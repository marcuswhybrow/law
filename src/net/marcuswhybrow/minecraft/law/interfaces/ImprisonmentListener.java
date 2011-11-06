package net.marcuswhybrow.minecraft.law.interfaces;

import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.entity.Player;

/**
 * An interface for objects which respond to players being
 * imprisoned.
 *  
 * @author Marcus Whybrow
 *
 */
public interface ImprisonmentListener {
	/**
	 * Called when an in-game player is imprisoned
	 * and not necessarily when the imprison command
	 * has been executed. This can be the case when
	 * a player is offline.
	 * 
	 * @param player The player which has been imprisoned
	 * @param cell The cell the player is imprisoned in
	 */
	public void onImprison(Player player, PrisonCell cell);
	
	/**
	 * Called when an in-game player is freed and
	 * not necessarily when the free command has
	 * been executed. This can be the case when a
	 * player is offline.
	 * 
	 * @param player The player which has been freed
	 * @param cell The cell the player has been freed from
	 */
	public void onFree(Player player, PrisonCell cell);
}
