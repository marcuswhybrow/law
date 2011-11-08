package net.marcuswhybrow.minecraft.law.listeners;

import java.util.Collection;

import net.marcuswhybrow.minecraft.law.Law;
import net.marcuswhybrow.minecraft.law.LawWorld;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonCreate;
import net.marcuswhybrow.minecraft.law.commands.prison.CommandLawPrisonSelect;
import net.marcuswhybrow.minecraft.law.events.LawDetainEndEvent;
import net.marcuswhybrow.minecraft.law.events.LawFreeEvent;
import net.marcuswhybrow.minecraft.law.events.LawDetainStartEvent;
import net.marcuswhybrow.minecraft.law.events.LawImprisonEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonCellCreateEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonCellDeleteEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonCellMoveEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonCreateEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonDeleteEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonSelectEvent;
import net.marcuswhybrow.minecraft.law.events.LawPrisonSetExitEvent;
import net.marcuswhybrow.minecraft.law.prison.Prison;
import net.marcuswhybrow.minecraft.law.prison.PrisonCell;
import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;
import net.marcuswhybrow.minecraft.law.utilities.Colorise;
import net.marcuswhybrow.minecraft.law.utilities.MessageDispatcher;

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
		
		if (event instanceof LawDetainStartEvent) {
			this.onDetainStart((LawDetainStartEvent) event);
			
		} else if (event instanceof LawImprisonEvent) {
			this.onImprison((LawImprisonEvent) event);
			
		} else if (event instanceof LawDetainEndEvent) {
			this.onDetainEnd((LawDetainEndEvent) event);
			
		} else if (event instanceof LawFreeEvent) {
			this.onFree((LawFreeEvent) event);
			
		} else if (event instanceof LawPrisonCreateEvent) {
			this.onPrisonCreate((LawPrisonCreateEvent) event);
			
		} else if (event instanceof LawPrisonDeleteEvent) {
			this.onPrisonDelete((LawPrisonDeleteEvent) event);
			
		} else if (event instanceof LawPrisonSelectEvent) {
			this.onPrisonSelect((LawPrisonSelectEvent) event);
			
		} else if (event instanceof LawPrisonSetExitEvent) {
			this.onPrisonSetExit((LawPrisonSetExitEvent) event);
			
		} else if (event instanceof LawPrisonCellCreateEvent) {
			this.onPrisonCellCreate((LawPrisonCellCreateEvent) event);
			
		} else if (event instanceof LawPrisonCellDeleteEvent) {
			this.onPrisonCellDelete((LawPrisonCellDeleteEvent) event);
			
		} else if (event instanceof LawPrisonCellMoveEvent) {
			this.onPrisonCellMove((LawPrisonCellMoveEvent) event);
			
		}
		
		super.onCustomEvent(event);
	}
	
	/**
	 * Called when a playeris detained by another player.
	 * Remember that the detained player may not be online
	 * at this point.
	 * 
	 * See {@link #onImprison(LawImprisonEvent)} for
	 * when an in-game player is actually moved into the prison.
	 * 
	 * @param event The {@link LawDetainStartEvent} instance
	 */
	public void onDetainStart(LawDetainStartEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonDetainee detainee = event.getDetainee();
		Player sourcePlayer = event.getSourcePlayer();
		PrisonCell cell = detainee.getPrisonCell();
		String detaineeName = detainee.getName();
		String prisonName = cell.getPrison().getName();
		
		// The logic
		boolean detentionWasStarted = detainee.startDetention();
		
		if (detentionWasStarted == false)
			return;
		
		boolean playerIsOnline = detainee.imprisonInGamePlayer();
		
		// Messages
		if (playerIsOnline) {
			MessageDispatcher.sendMessage(sourcePlayer, "Imprisoned " + Colorise.entity(detaineeName) + " in " + Colorise.entity(prisonName) + " prison.");
		} else {
			MessageDispatcher.sendMessage(sourcePlayer, "Imprisoned " + Colorise.entity(detaineeName) + " in " + Colorise.entity(prisonName) + " prison. This player is offline but will be imprisoned when they return.");
		}
		
		// Console message
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " imprisoned \"" + detaineeName + "\" in \"" + prisonName + "\" prison");
		
		Law.save();
	}
	
	/**
	 * Called when an in-game player is literally imprisoned with a prison cell.
	 * 
	 * @param event The {@link LawImprisonEvent} instance
	 */
	public void onImprison(LawImprisonEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonDetainee detainee = event.getDetainee();
		PrisonCell cell = detainee.getPrisonCell();
		String detaineeName = detainee.getName();
		
		// The logic
		boolean playerWasImprisoned = detainee.imprisonInGamePlayer();
		
		if (playerWasImprisoned == false)
			return;
		
		// Messages
		MessageDispatcher.broadcast(Colorise.entity(detaineeName) + " has been " + Colorise.action("imprisoned") + " in " + Colorise.entity(cell.getPrison().getName()) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(detainee.getPlayer(), "You have been imprisoned. Your inventory will be returned when you are freed.");
		
		Law.save();
	}
	
	/**
	 * Called when a player's detention is ended by another
	 * player. Remember that the player whose detention has just
	 * ended may not be online.
	 * 
	 * See {@link #onFree(LawFreeEvent)} for when an
	 * in-game player is actually moved out of their prison cell.
	 * 
	 * @param event The {@link LawDetainEndEvent} instance
	 */
	public void onDetainEnd(LawDetainEndEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonDetainee detainee = event.getDetainee();
		String detaineeName = detainee.getName();
		String prisonName = detainee.getPrisonCell().getPrison().getName();
		// source player can be null for this event
		Player sourcePlayer = event.getSourcePlayer();
		
		boolean detentionWasEnded = detainee.endDetention();
		
		if (detentionWasEnded == false)
			return;
		
		boolean playerIsOnline = detainee.freeInGamePlayer();
		
		StringBuilder message = new StringBuilder(Colorise.entity(detaineeName)).append(" was ").append(Colorise.action("freed")).append(" from ").append(Colorise.entity(prisonName)).append(" prison");
		
		if (sourcePlayer != null) {
			message.append(" early by ").append(Colorise.entity(sourcePlayer.getName()));
		} else {
			message.append(" after serving their sentence.");
		}
		
		MessageDispatcher.broadcast(message.toString(), "law.broadcasts.free");
		
		if (sourcePlayer != null && playerIsOnline == false) {
			MessageDispatcher.sendMessage(sourcePlayer, new StringBuilder(Colorise.entity(detaineeName)).append(" will be free when they next join.").toString());
		}
		
		Law.save();
	}
	
	/**
	 * Called when an in-game player is freed from a prison cell.
	 * 
	 * @param event The {@link LawFreeEvent} instance
	 */
	public void onFree(LawFreeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		PrisonDetainee detainee = event.getDetainee();
		Player targetPlayer = detainee.getPlayer();
		PrisonCell cell = detainee.getPrisonCell();
		String detaineeName = detainee.getName();
		String prisonName = cell.getPrison().getName();
		
		// The logic
		boolean playerWasFreed = detainee.freeInGamePlayer();
		
		if (playerWasFreed == false)
			return;
		
		// Messages
		MessageDispatcher.broadcast(Colorise.entity(detaineeName) + " has been " + Colorise.action("freed") + " from " + Colorise.entity(prisonName) + " prison.", "law.broadcasts.imprison");
		MessageDispatcher.sendMessage(targetPlayer, "You have been freed from prison.");
		
		Law.save();
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
		Prison createdPrison = event.getPrison();
		LawWorld lawWorld = createdPrison.getLawWorld();
		
		// The logic
		lawWorld.addPrison(createdPrison);
		Law.fireEvent(new LawPrisonSelectEvent(sourcePlayer, createdPrison));
		
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
		if (event.isCancelled()) {
			return;
		}
		
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
				Law.fireEvent(new LawPrisonSelectEvent(sourcePlayer, prisons[0]));
				MessageDispatcher.sendMessage(sourcePlayer, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
			}
		} else if (prevSelectedPrison == null && numPrisons == 1) {
			// When there is no previously selected prison, one has to
			// set the new selected prison manually, as it cannot be inferred from
			// the current state in the deletePrison method.
			Law.fireEvent(new LawPrisonSelectEvent(sourcePlayer, prisons[0]));
			MessageDispatcher.sendMessage(sourcePlayer, "The only remaining prison " + Colorise.entity(prisons[0].getName()) + " is now the selected prison.");
		}
		if (numPrisons == 0) {
			// There are no remaining prisons, the player must create a new one
			Law.fireEvent(new LawPrisonSelectEvent(sourcePlayer, null));
			MessageDispatcher.sendMessage(sourcePlayer, "There are no remaining prisons. Use " + Colorise.command(CommandLawPrisonCreate.DEFINITION) + " to start a new one.");
		}
		
		Law.save();
	}
	
	/**
	 * Called when a player selects a new prison, or has
	 * their selection set to no prison (represented by null.)
	 * 
	 * @param event The {@link LawPrisonSelectEvent} instance
	 */
	public void onPrisonSelect(LawPrisonSelectEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		Prison prison = event.getPrison();
		LawWorld lawWorld = Law.get().getLawWorldForPlayer(sourcePlayer);
		
		// The logic
		lawWorld.setSelectedPrison(sourcePlayer.getName(), prison);
		
		// Messages
		MessageDispatcher.sendMessage(sourcePlayer, "The prison " + Colorise.entity(prison.getName()) + " has been selected. All prison commands now apply to this prison.");
		
		Law.save();
	}
	
	/**
	 * Called when a player sets the exit point for a prison.
	 * 
	 * @param event The {@link LawPrisonSetExitEvent} instance
	 */
	public void onPrisonSetExit(LawPrisonSetExitEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		Prison prison = event.getPrison();
		
		// The Logic
		prison.setExitPoint(sourcePlayer.getLocation());
		
		// Messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " set the exit point location for prison \"" + prison.getName() + "\"");
		MessageDispatcher.sendMessage(sourcePlayer, "The " + Colorise.entity("exit point") + " for " + Colorise.entity(prison.getName()) + " prison has been set.");
		
		Law.save();
	}
	
	/**
	 * Called when a player creates a new prison cell.
	 * 
	 * @param event The {@link LawPrisonCellCreateEvent} instance
	 */
	public void onPrisonCellCreate(LawPrisonCellCreateEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		PrisonCell cell = event.getPrisonCell();
		Prison prison = cell.getPrison();
		
		// The logic
		prison.addCell(cell);
		
		// Messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " created the cell \"" + cell.getName() + "\" for \"" + prison.getName() + "\" prison.");
		MessageDispatcher.sendMessage(sourcePlayer, "The cell " + Colorise.entity(cell.getName()) + " has been created for " + Colorise.entity(prison.getName()) + " prison.");
		
		Law.save();
	}
	
	/**
	 * Called when a player deletes a prison cell.
	 * 
	 * @param event The {@link LawPrisonCellDeleteEvent} instance
	 */
	public void onPrisonCellDelete(LawPrisonCellDeleteEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		PrisonCell cell = event.getPrisonCell();
		Prison prison = cell.getPrison();
		
		// The logic
		prison.removeCell(cell.getName());
		
		// Messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " deleted the cell \"" + cell.getName() + "\" for \"" + prison.getName() + "\" prison");
		MessageDispatcher.sendMessage(sourcePlayer, "The cell " + Colorise.entity(cell.getName()) + " has been deleted from " + Colorise.entity(prison.getName()) + " prison.");
		
		Law.save();
	}
	
	/**
	 * Called when a player moves a prison cell to a new location.
	 * 
	 * @param event The {@link LawPrisonCellMoveEvent} instance
	 */
	public void onPrisonCellMove(LawPrisonCellMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player sourcePlayer = event.getSourcePlayer();
		PrisonCell cell = event.getPrisonCell();
		Prison prison = cell.getPrison();
		
		// The logic
		cell.setLocation(sourcePlayer.getLocation());
		
		// Messages
		MessageDispatcher.consoleInfo(sourcePlayer.getName() + " moved the cell \"" + cell.getName() + "\" belonging to \"" + prison.getName() + "\" prison");
		MessageDispatcher.sendMessage(sourcePlayer, "The cell " + Colorise.entity(cell.getName()) + " has been moved, belonging to " + Colorise.entity(prison.getName()) + " prison.");
		
		Law.save();
	}
}
