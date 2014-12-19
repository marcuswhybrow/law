package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.marcuswhybrow.minecraft.law.prison.Prison;

public class LawPrisonDeleteEvent extends LawPrisonEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public LawPrisonDeleteEvent(final Player sourcePlayer, final Prison prison) {
		super("LawPrisonDeleteEvent");
		
		this.setSourcePlayer(sourcePlayer);
		this.setPrison(prison);
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
