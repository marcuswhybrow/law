package net.marcuswhybrow.minecraft.law.events;

import net.marcuswhybrow.minecraft.law.prison.PrisonDetainee;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Represents the event triggered when a player is imprisoned.
 * The plugin has yet to check whether the player is actually
 * online and thus has not effect the actual player in anyway.
 * 
 * The {@link LawImprisonEvent} class represents when an in-game
 * player is actually imprisoned. 
 * 
 * @author Marcus Whybrow
 *
 */
public class LawDetainStartEvent extends LawPrisonDetaineeEvent {
	private static final HandlerList handlers = new HandlerList();

	public LawDetainStartEvent(final Player sourcePlayer, final PrisonDetainee detainee) {
		super("LawImprisonEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setDetainee(detainee);
	}
}
