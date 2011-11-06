package net.marcuswhybrow.minecraft.law.listeners;

import java.util.Collection;

import net.marcuswhybrow.minecraft.law.InventoryManager;
import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonCreate;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSelect;
import net.marcuswhybrow.minecraft.law.events.LawFreeEvent;
import net.marcuswhybrow.minecraft.law.events.LawFreeReleaseEvent;
import net.marcuswhybrow.minecraft.law.events.LawImprisonEvent;
import net.marcuswhybrow.minecraft.law.events.LawImprisonSecureEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonCreateEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonDeleteEvent;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Handles the custom Law plugin events.
 * 
 * @author Marcus Whybrow
 *
 */
public class LawListener extends CustomEventListener implements Listener {
	
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
			
		} else if (event instanceof LawFreeEvent) {
			this.onFree((LawFreeEvent) event);
			
		} else if (event instanceof LawFreeReleaseEvent) {
			this.onFreeRelease((LawFreeReleaseEvent) event);
			
		} else if (event instanceof LawPrisonCreateEvent) {
			this.onPrisonCreate((LawPrisonCreateEvent) event);
			
		} else if (event instanceof LawPrisonDeleteEvent) {
			onPrisonDelete((LawPrisonDeleteEvent) event);
		}
		
		super.onCustomEvent(event);
	}
	
	/**
	 * Called when a player is imprisoned, before the plugin
	 * checks whether the player is online, and thus before the
	 * in-game player is effected in any way.
	 * 
	 * See {@link #onImprisonSecure(LawImprisonSecureEvent)} for
	 * when an in-game player is actually moved into the prison.
	 * 
	 * @param event The {@link LawImprisonEvent} instance
	 */
	public void onImprison(LawImprisonEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		String targetPlayerName = event.getTargetPlayerName();
		PrisonCell cell = event.getPrisonCell();
		Player sourcePlayer = event.getSourcePlayer();
		
		// Imprison the player
		Law.imprisonPlayer(targetPlayerName, cell);
		Law.save();
		
		// If the player is online, secure them in the prison
		Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
		if (targetPlayer != null) {
			Law.fireEvent(new LawImprisonSecureEvent(sourcePlayer, targetPlayer, cell));
			MessageDispatcher.sendMessage(sourcePlayer, "Imprisoned " + Colorise.entity(targetPlayerName) + " in " + Colorise.entity(cell.getPrison().getName()) + " prison.");
		} else {
			MessageDispatcher.sendMessage(sourcePlayer, "Imprisoned " + Colorise.entity(targetPlayerName) + " in " + Colorise.entity(cell.getPrison().getName()) + " prison. This player is offline but will be imprisoned when they return.");
		}
		
		// Console message
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " imprisoned \"" + targetPlayerName + "\" in \"" + cell.getPrison().getName() + "\" prison");
	}
	
	/**
	 * Called when an in-game player is literally imprisoned with a prison cell.
	 * 
	 * @param event The {@link LawImprisonSecureEvent} instance
	 */
	public void onImprisonSecure(LawImprisonSecureEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonCell cell = event.getPrisonCell();
		Player targetPlayer = event.getTargetPlayer();
		
		// Player changes
		targetPlayer.teleport(cell.getLocation());
		InventoryManager.confiscate(targetPlayer);
		targetPlayer.setSleepingIgnored(true);
		
		// Completes the imprisonment of this player in their cell
		cell.securePrisoner(targetPlayer.getName());
		Law.save();
		
		// Messages
		MessageDispatcher.broadcast(Colorise.entity(targetPlayer.getName()) + " has been " + Colorise.action("imprisoned") + " in " + Colorise.entity(cell.getPrison().getName()) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(targetPlayer, "You have been imprisoned. Your inventory will be returned when you are freed.");
	}
	
	/**
	 * Called when a player is freed, before the plugin
	 * checks whether the player is online, and thus before the
	 * in-game player is effected in any way.
	 * 
	 * See {@link #onFreeRelease(LawFreeReleaseEvent)} for when an
	 * in-game player is actually moved out of their prison cell.
	 * 
	 * @param event The {@link LawFreeEvent} insance
	 */
	public void onFree(LawFreeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		String targetPlayerName = event.getTargetPlayerName();
		PrisonCell cell = event.getPrisonCell();
		
		Law.freePlayer(targetPlayerName, cell);
		Law.save();
		
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " freed \"" + targetPlayerName + "\" from \"" + cell.getPrison().getName() + "\" prison");
		
		String message = "Freed " + Colorise.entity(targetPlayerName) + " from " + Colorise.entity(cell.getPrison().getName()) + " prison.";
		if (Bukkit.getPlayerExact(targetPlayerName) == null) {
			message += " This player is offline but will be free when they return.";
		}
		MessageDispatcher.sendMessage(sourcePlayer, message);
	}
	
	/**
	 * Called when a player is literally freed from a prison cell.
	 * 
	 * @param event The {@link LawFreeReleaseEvent} instance
	 */
	public void onFreeRelease(LawFreeReleaseEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonCell cell = event.getPrisonCell();
		Player targetPlayer = event.getTargetPlayer();
		
		// Get the best location
		Location location = cell.getPrison().getExitPoint();
		if (location == null) {
			location = cell.getPrison().getLawWorld().getBukkitWorld().getSpawnLocation();
		}
		
		// Effect the player
		targetPlayer.teleport(location);
		InventoryManager.restore(targetPlayer);
		targetPlayer.setSleepingIgnored(false);
		
		// Completes the removal of this player from the prison
		cell.removePrisoner(targetPlayer.getName());
		
		// Messages
		MessageDispatcher.broadcast(Colorise.entity(targetPlayer.getName()) + " has been " + Colorise.action("freed") + " from " + Colorise.entity(cell.getPrison().getName()) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(targetPlayer, "You have been freed from prison.");
	}
	
	/**
	 * Called when a player creates a new prison.
	 * 
	 * @param event The {@link LawPrisonCreateEvent} instance
	 */
	public void onPrisonCreate(LawPrisonCreateEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		Prison createdPrison = event.getCreatedPrison();
		LawWorld lawWorld = createdPrison.getLawWorld();
		
		// The logic
		lawWorld.addPrison(createdPrison);
		lawWorld.setSelectedPrison(sourcePlayer.getName(), createdPrison.getName());
		
		// Messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " created prison \"" + createdPrison.getName() + "\"");
		MessageDispatcher.sendMessage(sourcePlayer, "Prison " + Colorise.entity(createdPrison.getName()) + " has been created.");
		
		Law.save();
	}
	
	/**
	 * Called when a player deletes a prison.
	 * 
	 * @param event The {@link LawPrisonDeleteEvent} instance
	 */
	public void onPrisonDelete(LawPrisonDeleteEvent event) {
		Prison prison = event.getPrison();
		Player sourcePlayer = event.getSourcePlayer();
		LawWorld lawWorld = prison.getLawWorld();
		Prison prevSelectedPrison = lawWorld.getSelectedPrison(sourcePlayer.getName());
		
		// The logic
		lawWorld.deletePrison(prison);
		
		// Success messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " deleted prison \"" + prison.getName() + "\"");
		MessageDispatcher.sendMessage(sourcePlayer, "The prison " + Colorise.entity(prison.getName()) + " has been deleted.");
		
		// If possible select a new prison automatically, or
		// inform the player how to proceed.
		Collection<Prison> prisonCollection = lawWorld.getPrisons();
		Prison[] prisons = prisonCollection.toArray(new Prison[prisonCollection.size()]);
		int numPrisons = prisons.length;
		if (prevSelectedPrison == prison) {
			if (numPrisons >= 2) {
				MessageDispatcher.sendMessage(sourcePlayer, "Use " + Colorise.command(CommandLawPrisonSelect.DEFINITION) + " to choose one of the remaining" + Colorise.highlight(" " + prisons.length) + " prisons to work on.");
			} else if (numPrisons == 1) {
				MessageDispatcher.sendMessage(sourcePlayer, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
			}
		} else if (prevSelectedPrison == null && numPrisons == 1) {
			// When there is no previously selected prison, one has to
			// set the new selected prison manually, as it cannot be inferred from
			// the current state in the deletePrison method.
			lawWorld.setSelectedPrison(sourcePlayer.getName(), prisons[0].getName());
			MessageDispatcher.sendMessage(sourcePlayer, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
		}
		if (numPrisons == 0) {
			// There are no remaining prisons, the player must create a new one
			MessageDispatcher.sendMessage(sourcePlayer, "There are no remaining prisons. Use " + Colorise.command(CommandLawPrisonCreate.DEFINITION) + " to start a new one.");
		}
		
		Law.save();
	}
}