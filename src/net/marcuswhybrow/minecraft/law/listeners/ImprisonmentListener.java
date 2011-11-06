package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

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
		MessageDispatcher.broadcast(Colorise.entity(player.getName()) + " has been " + Colorise.action("imprisoned") + " in " + Colorise.entity(cell.getPrison().getName()) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(player, "You have been imprisoned. Your inventory will be returned when you are freed.");
		
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
		MessageDispatcher.broadcast(Colorise.entity(player.getName()) + " has been " + Colorise.action("freed") + " from " + Colorise.entity(cell.getPrison().getName()) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(player, "You have been freed from prison.");
		
		// Completes the removal of this player from the prison
		cell.removePrisoner(player.getName());
	}
}
