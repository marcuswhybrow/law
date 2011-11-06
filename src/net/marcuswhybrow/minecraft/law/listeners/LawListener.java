package net.marcuswhybrow.minecraft.law.listeners;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.events.LawImprisonEvent;
import net.marcuswhybrow.minecraft.law.events.LawImprisonSecureEvent;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.entity.Player;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class LawListener extends CustomEventListener implements Listener {
	
	/**
	 * Called when a player is imprisoned, before the plugin
	 * checks whether the player is online, and thus before the
	 * in-game player is effected in any way.
	 * 
	 * See {@link #onImprisonSecure(LawImprisonSecureEvent)} for
	 * when an in-game player is actually moved into the prison.
	 * 
	 * @param event The {@link LawImprisonEvent}
	 */
	public void onImprison(LawImprisonEvent event) {
		
	}
	
	/**
	 * Called when an in-game player is literally imprisoned with a prison cell.
	 * 
	 * @param event The {@link LawImprisonSecureEvent}
	 */
	public void onImprisonSecure(LawImprisonSecureEvent event) {
		PrisonCell cell = event.getPrisonCell();
		Player player = event.getTargetPlayer();
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
	
	/**
	 * Calls the appropriate method depending on the type of custom event.
	 * 
	 * @param event The custom event
	 */
	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof LawImprisonEvent) {
			this.onImprison((LawImprisonEvent) event);
		} else if (event instanceof LawImprisonSecureEvent) {
			this.onImprisonSecure((LawImprisonSecureEvent) event);
		}
		
		super.onCustomEvent(event);
	}
}
