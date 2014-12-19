package net.marcuswhybrow.minecraft.law.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class LawEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	/** True if the event has been cancelled. */
	private boolean isCancelled;

	public LawEvent(String event) {
		this.setCancelled(false);
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
