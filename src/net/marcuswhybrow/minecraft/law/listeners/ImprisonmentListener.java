package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Takes care of imprisoning and freeing players.
 * 
 * @author Marcus Whybrow
 *
 */
public class ImprisonmentListener implements net.marcuswhybrow.minecraft.law.interfaces.ImprisonmentListener {
	
	@Override
	public void onImprison(Player player, PrisonCell cell) {
		if (cell == null) {
			return;
		}
		player.teleport(cell.getLocation());
		InventoryManager.confiscate(player);
		player.setSleepingIgnored(true);
		
		// Completes the imprisonment of this player in their cell
		cell.securePrisoner(player.getName());
	}
	
	@Override
	public void onFree(Player player, PrisonCell cell) {
		Location location = cell.getPrison().getExitPoint();
		
		// If the prison has no exit point, then teleport the prisoner
		// to the spawn point.
		if (location == null) {
			location = cell.getPrison().getLawWorld().getBukkitWorld().getSpawnLocation();
		}
		
		player.teleport(location);
		InventoryManager.restore(player);
		player.setSleepingIgnored(false);
		
		// Completes the removal of this player from the prison
		cell.removePrisoner(player.getName());
	}
}
